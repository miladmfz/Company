package com.kits.company.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.kits.company.R;
import com.kits.company.adapter.Good_ProSearch_Adapter;
import com.kits.company.adapter.Good_ProSearch_Line_Adapter;
import com.kits.company.adapter.InternetConnection;
import com.kits.company.model.Farsi_number;
import com.kits.company.model.Good;
import com.kits.company.model.GoodBuy;
import com.kits.company.model.GoodBuyRespons;
import com.kits.company.model.GoodRespons;
import com.kits.company.webService.APIClient;
import com.kits.company.webService.APIInterface;

import java.util.ArrayList;
import java.util.Objects;

import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;
import jp.wasabeef.recyclerview.animators.FlipInTopXAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Search_date_detailActivity extends AppCompatActivity {

    APIInterface apiInterface = APIClient.getCleint().create(APIInterface.class);
    Intent intent;
    SharedPreferences shPref;
    SharedPreferences.Editor sEdit;
    RecyclerView rc_good;
    ArrayList<Good> goods;
    TextView textCartItemCount;
    ArrayList<GoodBuy> goodbuys,goodbuys_setupBadge;
    ProgressBar prog;
    GridLayoutManager gridLayoutManager;
    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount,PageNo=0;
    Good_ProSearch_Adapter adapter;
    Good_ProSearch_Line_Adapter adapter_line;
    ArrayList<String[]> Multi_buy = new ArrayList<>();
    FloatingActionButton fab;
    Menu item_multi;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_date_detail);



        InternetConnection ic =new  InternetConnection(getApplicationContext());
        if(ic.has()){
            init();
        } else{
            intent = new Intent(getApplicationContext(), SplashActivity.class);
            startActivity(intent);
            finish();
        }



    }
    //***************************************************

    public void init() {
        shPref = getSharedPreferences("profile", Context.MODE_PRIVATE);
        sEdit = shPref.edit();
        prog = findViewById(R.id.search_date_prog);
        rc_good =  findViewById(R.id.search_date_recycler);
        toolbar = findViewById(R.id.search_date_toolbar);
        setSupportActionBar(toolbar);
        fab =  findViewById(R.id.search_date_fab);

        final SwitchMaterial mySwitch_activestack = findViewById(R.id.search_date_switch);
        mySwitch_activestack.setChecked(shPref.getBoolean("available_good", true));
        mySwitch_activestack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sEdit.putBoolean("available_good",!shPref.getBoolean("available_good", true));
                sEdit.apply();
                if(Objects.equals(shPref.getString("view", null), "grid")){
                    adapter.notifyDataSetChanged();
                }else{
                    adapter_line.notifyDataSetChanged();
                }
                gridLayoutManager = new GridLayoutManager(Search_date_detailActivity.this,2);
                gridLayoutManager.scrollToPosition(pastVisiblesItems+2);
                rc_good.setLayoutManager(gridLayoutManager);
                rc_good.setAdapter(adapter);

            }
        });




        allgood();


        rc_good.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount =   gridLayoutManager.getChildCount();
                    totalItemCount =   gridLayoutManager.getItemCount();
                    pastVisiblesItems =   gridLayoutManager.findFirstVisibleItemPosition();


                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount-2) {
                            loading = false;
                            Log.e("rc_...", "Last Item Wow !");
                            // Do pagination.. i.e. fetch new data
                            PageNo++;
                            allgood_more();
                        }
                    }
                }
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               final Dialog dialog = new Dialog(Search_date_detailActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//title laye nadashte bashim
                dialog.setContentView(R.layout.box_multi_buy);
                Button boxbuy = dialog.findViewById(R.id.box_multi_buy_btn);
                final EditText amount_mlti = dialog.findViewById(R.id.box_multi_buy_amount);
                final TextView tv = dialog.findViewById(R.id.box_multi_buy_factor);
                dialog.show();
                amount_mlti.requestFocus();
                amount_mlti.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(amount_mlti, InputMethodManager.SHOW_IMPLICIT);
                    }
                }, 500);

                boxbuy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       final  String amo = amount_mlti.getText().toString();
                        if (!amo.equals("")) {
                            if (Integer.parseInt(amo) != 0) {

                                for (final String[] s : Multi_buy) {

                                    Call<GoodRespons> call_amount = apiInterface.GetAllGood
                                            ("goodinfo",0,"","goodcode="+ Integer.parseInt(s[0]),0,0,shPref.getString("mobile", null),0);
                                    call_amount.enqueue(new Callback<GoodRespons>() {
                                        @Override
                                        public void onResponse(Call<GoodRespons> call, Response<GoodRespons> response) {
                                            ArrayList<Good> goods = response.body().getGoods();
                                            Good good= goods.get(0);
                                            Call<GoodBuyRespons> call2 = apiInterface.InsertBasket("Insertbasket", "DeviceCode", Integer.parseInt(s[0]), Integer.parseInt(amo)+Integer.parseInt(good.getGoodFieldValue("BasketAmount")), Integer.parseInt(s[1]), "test", shPref.getString("mobile", null));
                                            call2.enqueue(new Callback<GoodBuyRespons>() {
                                                @Override
                                                public void onResponse(Call<GoodBuyRespons> call, retrofit2.Response<GoodBuyRespons> response) {
                                                    Log.e("onResponse", "" + response.body());
                                                    goodbuys = response.body().getGoodsbuy();
                                                    if (Integer.parseInt(goodbuys.get(0).getGoodBuyFieldValue("ErrCode"))> 0){
                                                        Toast.makeText(Search_date_detailActivity.this, goodbuys.get(0).getGoodBuyFieldValue("ErrDesc")+"????????"+s[2], Toast.LENGTH_SHORT).show();
                                                    }else{
                                                        setupBadge();
                                                    }
                                                }
                                                @Override
                                                public void onFailure(Call<GoodBuyRespons> call, Throwable t) {
                                                    Log.e("onFailure", "" + t.toString());
                                                }
                                            });
                                        }
                                        @Override
                                        public void onFailure(Call<GoodRespons> call, Throwable t) {
                                        }
                                    });

                                }

                                dialog.dismiss();
                                item_multi.findItem(R.id.menu_multi).setVisible(false);
                                for (Good good : goods) {
                                    good.setCheck(false);
                                }
                                Multi_buy.clear();
                                if(Objects.equals(shPref.getString("view", null), "grid")){
                                    adapter.multi_select=false;
                                    adapter.notifyDataSetChanged();
                                }else{
                                    adapter_line.multi_select=false;
                                    adapter_line.notifyDataSetChanged();
                                }
                                fab.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(Search_date_detailActivity.this, "?????????? ???????? ?????? ???????? ?????? ????????.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Search_date_detailActivity.this, "?????????? ???????? ?????? ???????? ?????? ????????.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }

    private void allgood() {

        Call<GoodRespons> call = apiInterface.GetAllGood
                ("goodinfo",0,"","",0,PageNo,shPref.getString("mobile", null),0);
        call.enqueue(new Callback<GoodRespons>() {
            @Override
            public void onResponse(Call<GoodRespons> call, Response<GoodRespons> response) {
                if (response.isSuccessful()) {
                    goods = response.body().getGoods();
                    if(Objects.equals(shPref.getString("view", null), "grid")){
                        adapter = new Good_ProSearch_Adapter(goods, Search_date_detailActivity.this);
                        adapter_line = new Good_ProSearch_Line_Adapter(goods, Search_date_detailActivity.this);
                        adapter.multi_select=false;
                        gridLayoutManager = new GridLayoutManager(Search_date_detailActivity.this,2);
                        rc_good.setLayoutManager(gridLayoutManager);
                        rc_good.setAdapter(adapter);
                        rc_good.setItemAnimator(new FlipInTopXAnimator());

                    }else{
                        adapter_line = new Good_ProSearch_Line_Adapter(goods, Search_date_detailActivity.this);
                        adapter = new Good_ProSearch_Adapter(goods, Search_date_detailActivity.this);
                        adapter_line.multi_select=false;
                        gridLayoutManager = new GridLayoutManager(Search_date_detailActivity.this,1);
                        rc_good.setLayoutManager(gridLayoutManager);
                        rc_good.setAdapter(adapter_line);
                        rc_good.setItemAnimator(new FlipInTopXAnimator());
                    }                    item_multi.findItem(R.id.menu_grid).setVisible(true);

                    prog.setVisibility(View.GONE);
                    loading = true;

                }
            }

            @Override
            public void onFailure(Call<GoodRespons> call, Throwable t) {


                    Log.e("retrofit_fail",t.getMessage());

           }
        });

    }


    private void allgood_more() {
        prog.setVisibility(View.VISIBLE);
        Call<GoodRespons> call = apiInterface.GetAllGood
                ("goodinfo",0,"","",0,PageNo,shPref.getString("mobile", null),0);
        call.enqueue(new Callback<GoodRespons>() {
            @Override
            public void onResponse(Call<GoodRespons> call, Response<GoodRespons> response) {
                if (response.isSuccessful()) {
                    ArrayList<Good> good_page = response.body().getGoods();
                    goods.addAll(good_page);
                    if(Objects.equals(shPref.getString("view", null), "grid")){
                        adapter.notifyDataSetChanged();
                    }else{
                        adapter_line.notifyDataSetChanged();
                    }
                    prog.setVisibility(View.GONE);
                    loading = true;

                }
            }

            @Override
            public void onFailure(Call<GoodRespons> call, Throwable t) {
                PageNo--;
                prog.setVisibility(View.GONE);
                loading = true;
                Toast.makeText(Search_date_detailActivity.this, "?????????? ???????????? ?????????? ????????", Toast.LENGTH_SHORT).show();

           }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        item_multi=menu;
        if (Objects.equals(shPref.getString("view", null), "grid"))
            item_multi.findItem(R.id.menu_grid).setIcon(R.drawable.ic_view_line_24dp);
        else
            item_multi.findItem(R.id.menu_grid).setIcon(R.drawable.ic_view_grid_24dp);

        final MenuItem menuItem = menu.findItem(R.id.basket_menu);
        View actionView = menuItem.getActionView();
        textCartItemCount =  actionView.findViewById(R.id.cart_badge);
        setupBadge();
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.basket_menu) {
            intent = new Intent(Search_date_detailActivity.this, BuyActivity.class);
            sEdit = shPref.edit();
            sEdit.putString("basket_position","0");
            sEdit.apply();
            startActivity(intent);
            return true;

        }
        if (item.getItemId() == R.id.menu_grid) {

            item_multi.findItem(R.id.menu_multi).setVisible(false);
            for (Good good : goods) {
                good.setCheck(false);
            }
            Multi_buy.clear();
            fab.setVisibility(View.GONE);


            if(Objects.equals(shPref.getString("view", null), "grid")){

                item_multi.findItem(R.id.menu_grid).setIcon(R.drawable.ic_view_grid_24dp);
                sEdit.putString("view","line");
                sEdit.apply();
                adapter_line.multi_select=false;
                gridLayoutManager = new GridLayoutManager(Search_date_detailActivity.this,1);
                gridLayoutManager.scrollToPosition(pastVisiblesItems+1);
                rc_good.setLayoutManager(gridLayoutManager);
                rc_good.setAdapter(adapter_line);

            }else{

                item_multi.findItem(R.id.menu_grid).setIcon(R.drawable.ic_view_line_24dp);
                sEdit.putString("view","grid");
                sEdit.apply();
                //set_rc_good();
                adapter.multi_select=false;
                gridLayoutManager = new GridLayoutManager(Search_date_detailActivity.this,2);
                gridLayoutManager.scrollToPosition(pastVisiblesItems+2);
                rc_good.setLayoutManager(gridLayoutManager);
                rc_good.setAdapter(adapter);


            }
            return true;
        }
        if (item.getItemId() == R.id.menu_multi) {
            item_multi.findItem(R.id.menu_multi).setVisible(false);
            for (Good good : goods) {
                good.setCheck(false);
            }
            Multi_buy.clear();
            if(Objects.equals(shPref.getString("view", null), "grid")){
                adapter.notifyDataSetChanged();
                adapter.multi_select=false;

            }else{
                adapter_line.notifyDataSetChanged();
                adapter_line.multi_select=false;

            }
            fab.setVisibility(View.GONE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setupBadge() {

        if (textCartItemCount != null) {

            if (textCartItemCount.getVisibility() != View.GONE) {
                textCartItemCount.setVisibility(View.GONE);
            }
            Call<GoodBuyRespons> call2 = apiInterface.GetbasketSum("BasketSum",shPref.getString("mobile", null));
            call2.enqueue(new Callback<GoodBuyRespons>() {
                @Override
                public void onResponse(Call<GoodBuyRespons> call, Response<GoodBuyRespons> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        goodbuys_setupBadge = response.body().getGoodsbuy();

                        textCartItemCount.setText(Farsi_number.PerisanNumber(goodbuys_setupBadge.get(0).getGoodBuyFieldValue("SumFacAmount")));
                        if(Integer.parseInt(goodbuys_setupBadge.get(0).getGoodBuyFieldValue("SumFacAmount"))>0) {
                            if (textCartItemCount.getVisibility() != View.VISIBLE) {
                                textCartItemCount.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
                @Override
                public void onFailure(Call<GoodBuyRespons> call, Throwable t) {
                    Log.e("retrofit_fail",t.getMessage());


                    Log.e("retrofit_fail",t.getMessage());

           }
            });
        }
    }



    @Override
    public void onResume(){
        super.onResume();
        setupBadge();
    }


    public void good_select_function(String price_fun, int code_fun,String name, int flag) {

        if (flag == 1) {
            fab.setVisibility(View.VISIBLE);
            Multi_buy.add(new String[]{String.valueOf(code_fun), price_fun,name});
            item_multi.findItem(R.id.menu_multi).setVisible(true);

        } else {
            int b = 0, c = 0;
            for (String[] s : Multi_buy) {
                if (s[0].equals(String.valueOf(code_fun))) b = c;
                c++;
            }
            Multi_buy.remove(b);
            if (Multi_buy.size() < 1) {
                fab.setVisibility(View.GONE);
                if(Objects.equals(shPref.getString("view", null), "grid")){
                    adapter.multi_select=false;
                }else{
                    adapter_line.multi_select=false;
                }
                item_multi.findItem(R.id.menu_multi).setVisible(false);
                set_rc_good();
            }
        }
    }
    public void set_rc_good() {
        if(Objects.equals(shPref.getString("view", null), "grid")){
            adapter = new Good_ProSearch_Adapter(goods, Search_date_detailActivity.this);
            adapter.multi_select=false;
            gridLayoutManager = new GridLayoutManager(Search_date_detailActivity.this,2);
            gridLayoutManager.scrollToPosition(pastVisiblesItems+2);
            rc_good.setLayoutManager(gridLayoutManager);
            rc_good.setAdapter(adapter);
            rc_good.setItemAnimator(new FlipInTopXAnimator());

        }else{
            adapter_line = new Good_ProSearch_Line_Adapter(goods, Search_date_detailActivity.this);
            adapter_line.multi_select=false;
            gridLayoutManager = new GridLayoutManager(Search_date_detailActivity.this,1);
            gridLayoutManager.scrollToPosition(pastVisiblesItems+1);
            rc_good.setLayoutManager(gridLayoutManager);
            rc_good.setAdapter(adapter_line);
            rc_good.setItemAnimator(new FlipInTopXAnimator());
        }

    }

}
