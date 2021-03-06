package com.kits.company.activity;



import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.QuickContactBadge;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.card.MaterialCardView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.kits.company.R;
import com.kits.company.adapter.Good_ProSearch_Adapter;
import com.kits.company.adapter.Good_ProSearch_Line_Adapter;
import com.kits.company.adapter.InternetConnection;
import com.kits.company.adapter.Search_box;
import com.kits.company.model.Column;
import com.kits.company.model.ColumnRespons;
import com.kits.company.model.Farsi_number;
import com.kits.company.model.Good;
import com.kits.company.model.GoodBuy;
import com.kits.company.model.GoodBuyRespons;
import com.kits.company.model.GoodRespons;
import com.kits.company.webService.APIClient;
import com.kits.company.webService.APIInterface;

import java.util.ArrayList;
import java.util.Objects;

import jp.wasabeef.recyclerview.animators.FlipInTopXAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SearchActivity extends AppCompatActivity  {
Intent intent;
    Integer conter=0;
    private SharedPreferences shPref;
    SharedPreferences.Editor sEdit;
    MaterialCardView line_pro, line ;
    Button change_search , filter_active;
    EditText edtsearch;
    Handler handler= new Handler();
    APIInterface apiInterface = APIClient.getCleint().create(APIInterface.class);
    RecyclerView rc_good;
    Toolbar toolbar;
    ProgressBar prog;
    public String srch="", sq="";
    TextView textCartItemCount;
    GridLayoutManager gridLayoutManager;
    ArrayList<GoodBuy> goodbuys,goodbuys_setupBadge;
    Good_ProSearch_Adapter adapter;
    ArrayList<Good> goods=new ArrayList<Good>();
    Good_ProSearch_Line_Adapter adapter_line;
    private boolean loading = true;
    int pastVisiblesItems=0, visibleItemCount, totalItemCount;
    public int PageNo=0;
    ArrayList<String[]> Multi_buy = new ArrayList<>();
    FloatingActionButton fab;
    Menu item_multi;
    ArrayList<Column> Goodtype;
    ArrayList<String> Goodtype_array=new ArrayList<>() ;
    ArrayList<Column> Columns;
    Spinner spinner;
    LinearLayoutCompat layout_view;
    Button btn_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        InternetConnection ic =new  InternetConnection(getApplicationContext());
        if(ic.has()){
                init();
        } else{
            intent = new Intent(getApplicationContext(), SplashActivity.class);
            startActivity(intent);
            finish();
        }




    }


    //*************************************************





    public void init() {


        shPref = getSharedPreferences("profile", Context.MODE_PRIVATE);
        sEdit = shPref.edit();
        change_search = findViewById(R.id.SearchActivity_change_search);

        line = findViewById(R.id.SearchActivity_search_line);
        toolbar=findViewById(R.id.SearchActivity_toolbar);
        rc_good = findViewById (R.id.SearchActivity_R1);
        edtsearch = findViewById(R.id.SearchActivity_edtsearch);
        prog = findViewById(R.id.SearchActivity_prog);
        fab = findViewById(R.id.SearchActivity_fab);
        setSupportActionBar(toolbar);

        final SwitchMaterial mySwitch_activestack = findViewById(R.id.SearchActivity_switch);
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
                gridLayoutManager = new GridLayoutManager(SearchActivity.this,2);
                gridLayoutManager.scrollToPosition(pastVisiblesItems+2);
                rc_good.setLayoutManager(gridLayoutManager);
                rc_good.setAdapter(adapter);

            }
        });

        edtsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtsearch.selectAll();
            }
        });
        edtsearch.addTextChangedListener(
                new TextWatcher() {
                    @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                    @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                    @Override
                    public void afterTextChanged(final Editable editable) {
                        handler.removeCallbacksAndMessages(null);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                srch = arabicToenglish(editable.toString());
                                srch= srch.replaceAll(" ","%");
                                PageNo=0;

                                allgood(srch,"");
                            }
                        },1000);
                        handler.removeCallbacks(null);


                    }
                });



        allgood("","");
        //pro();


        change_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Search_box search_box = new Search_box(SearchActivity.this);
                search_box.search_pro();
            }
        });



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
                            allgood_more(srch,sq);
                        }
                    }
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(SearchActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//title laye nadashte bashim
                dialog.setContentView(R.layout.box_multi_buy);
                Button boxbuy = dialog.findViewById(R.id.box_multi_buy_btn);
                final EditText amount_mlti = dialog.findViewById(R.id.box_multi_buy_amount);
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
                        final String amo = amount_mlti.getText().toString();
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
                                                    assert response.body() != null;
                                                    goodbuys = response.body().getGoodsbuy();
                                                    if (Integer.parseInt(goodbuys.get(0).getGoodBuyFieldValue("ErrCode"))> 0){
                                                        Toast.makeText(SearchActivity.this, goodbuys.get(0).getGoodBuyFieldValue("ErrDesc")+"????????"+s[2], Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(SearchActivity.this, "?????????? ???????? ?????? ???????? ?????? ????????.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SearchActivity.this, "?????????? ???????? ?????? ???????? ?????? ????????.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });

    }




    public void allgood(String edtsearch,String where) {
        prog.setVisibility(View.VISIBLE);
        Call<GoodRespons> call = apiInterface.GetAllGood
                ("goodinfo", 0,edtsearch, where,0,PageNo,shPref.getString("mobile", null),0);
        call.enqueue(new Callback<GoodRespons>() {
            @Override
            public void onResponse(Call<GoodRespons> call, Response<GoodRespons> response) {
                if (response.isSuccessful()) {
                    goods = response.body().getGoods();
                    if(Objects.equals(shPref.getString("view", null), "grid")){
                        adapter = new Good_ProSearch_Adapter(goods, SearchActivity.this);
                        adapter_line = new Good_ProSearch_Line_Adapter(goods, SearchActivity.this);

                        adapter.multi_select=false;
                        gridLayoutManager = new GridLayoutManager(SearchActivity.this,2);
                        rc_good.setLayoutManager(gridLayoutManager);
                        rc_good.setAdapter(adapter);
                        rc_good.setItemAnimator(new FlipInTopXAnimator());

                    }else{
                        adapter_line = new Good_ProSearch_Line_Adapter(goods, SearchActivity.this);
                        adapter = new Good_ProSearch_Adapter(goods, SearchActivity.this);

                        adapter_line.multi_select=false;
                        gridLayoutManager = new GridLayoutManager(SearchActivity.this,1);
                        rc_good.setLayoutManager(gridLayoutManager);
                        rc_good.setAdapter(adapter_line);
                        rc_good.setItemAnimator(new FlipInTopXAnimator());
                    }
                    rc_good.setVisibility(View.VISIBLE);
                    item_multi.findItem(R.id.menu_grid).setVisible(true);
                    prog.setVisibility(View.GONE);
                    loading=true;

                }
            }
            @Override
            public void onFailure(Call<GoodRespons> call, Throwable t) {
                rc_good.setVisibility(View.GONE);
                prog.setVisibility(View.GONE);
                Toast toast =Toast.makeText(SearchActivity.this, "???????????? ???????? ??????", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 10, 10);
                toast.show();
            }
        });



    }



    private void allgood_more(String edtsearch,String where) {
        prog.setVisibility(View.VISIBLE);
        Call<GoodRespons> call = apiInterface.GetAllGood
                ("goodinfo",0, edtsearch, where,0,PageNo,shPref.getString("mobile", null),0);
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
                    rc_good.setVisibility(View.VISIBLE);
                    prog.setVisibility(View.GONE);
                    loading=true;
                }
            }

            @Override
            public void onFailure(Call<GoodRespons> call, Throwable t) {
                PageNo--;
                Toast.makeText(SearchActivity.this, "?????????? ???????????? ?????????? ????????", Toast.LENGTH_SHORT).show();
                prog.setVisibility(View.GONE);
                loading = true;

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        item_multi = menu;
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
            intent = new Intent(SearchActivity.this, BuyActivity.class);
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
                gridLayoutManager = new GridLayoutManager(SearchActivity.this,1);
                gridLayoutManager.scrollToPosition(pastVisiblesItems+1);
                rc_good.setLayoutManager(gridLayoutManager);
                rc_good.setAdapter(adapter_line);

            }else{

                item_multi.findItem(R.id.menu_grid).setIcon(R.drawable.ic_view_line_24dp);
                sEdit.putString("view","grid");
                sEdit.apply();
                //set_rc_good();
                adapter.multi_select=false;
                gridLayoutManager = new GridLayoutManager(SearchActivity.this,2);
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
                adapter.multi_select=false;
                adapter.notifyDataSetChanged();
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

                }
            });
        }
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
                set_rc_good();
                item_multi.findItem(R.id.menu_multi).setVisible(false);
            }
        }
    }




    private static String arabicToenglish(String number) {
        char[] chars = new char[number.length()];
        for(int i=0;i<number.length();i++) {
            char ch = number.charAt(i);
            if (ch >= 0x0660 && ch <= 0x0669)
                ch -= 0x0660 - '0';
            else if (ch >= 0x06f0 && ch <= 0x06F9)
                ch -= 0x06f0 - '0';
            chars[i] = ch;
        }
        return new String(chars);
    }
    @Override
    public void onResume(){
        super.onResume();
        setupBadge();
    }


    public void set_rc_good() {
        if(Objects.equals(shPref.getString("view", null), "grid")){
            adapter = new Good_ProSearch_Adapter(goods, SearchActivity.this);
            adapter.multi_select=false;
            gridLayoutManager = new GridLayoutManager(SearchActivity.this,2);
            gridLayoutManager.scrollToPosition(pastVisiblesItems+2);
            rc_good.setLayoutManager(gridLayoutManager);
            rc_good.setAdapter(adapter);
            rc_good.setItemAnimator(new FlipInTopXAnimator());

        }else{
            adapter_line = new Good_ProSearch_Line_Adapter(goods, SearchActivity.this);
            adapter_line.multi_select=false;
            gridLayoutManager = new GridLayoutManager(SearchActivity.this,1);
            gridLayoutManager.scrollToPosition(pastVisiblesItems+1);
            rc_good.setLayoutManager(gridLayoutManager);
            rc_good.setAdapter(adapter_line);
            rc_good.setItemAnimator(new FlipInTopXAnimator());
        }

    }

}




