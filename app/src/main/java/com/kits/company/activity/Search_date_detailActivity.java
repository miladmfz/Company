package com.kits.company.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.kits.company.R;
import com.kits.company.adapter.GetShared;
import com.kits.company.adapter.Good_ProSearch_Adapter;
import com.kits.company.adapter.Good_ProSearch_Line_Adapter;
import com.kits.company.adapter.InternetConnection;
import com.kits.company.application.App;
import com.kits.company.model.Good;
import com.kits.company.model.NumberFunctions;
import com.kits.company.model.RetrofitResponse;
import com.kits.company.webService.APIClient;
import com.kits.company.webService.APIInterface;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.FlipInTopXAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Search_date_detailActivity extends AppCompatActivity {

    APIInterface apiInterface = APIClient.getCleint().create(APIInterface.class);
    Intent intent;

    RecyclerView rc_good;
    ArrayList<Good> goods;
    TextView textCartItemCount;
    ArrayList<Good> Goods,Goods_setupBadge;
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



        InternetConnection ic =new  InternetConnection(this);
        if(ic.has()){
            try {
                init();
            }catch (Exception e){
                GetShared.ErrorLog(e.getMessage());
            }
        } else{
            intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
            finish();
        }



    }
    //***************************************************

    public void init() {

        prog = findViewById(R.id.search_date_prog);
        rc_good =  findViewById(R.id.search_date_recycler);
        toolbar = findViewById(R.id.search_date_toolbar);
        setSupportActionBar(toolbar);
        fab =  findViewById(R.id.search_date_fab);

        final SwitchMaterial mySwitch_activestack = findViewById(R.id.search_date_switch);
        mySwitch_activestack.setChecked(GetShared.ReadBoolan("available_good"));
        mySwitch_activestack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                GetShared.EditBoolan("available_good",!GetShared.ReadBoolan("available_good"));
                if(GetShared.ReadString("view").equals("grid")){
                    adapter.notifyDataSetChanged();
                }else{
                    adapter_line.notifyDataSetChanged();
                }
                gridLayoutManager = new GridLayoutManager(App.getContext(),2);
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
                            PageNo++;
                            allgood_more();
                        }
                    }
                }
            }
        });


        fab.setOnClickListener(v -> {

           final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//title laye nadashte bashim
            dialog.setContentView(R.layout.box_multi_buy);
            MaterialButton boxbuy = dialog.findViewById(R.id.box_multi_buy_btn);
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
                    final String amo = amount_mlti.getText().toString();
                    if (!amo.equals("")) {
                        if (Integer.parseInt(amo) != 0) {

                            for (final String[] s : Multi_buy) {

                                Call<RetrofitResponse> call_amount = apiInterface.GetAllGood(
                                        "goodinfo",
                                        "0",
                                        "",
                                        "goodcode="+ Integer.parseInt(s[0]),
                                        "0",
                                        "0",
                                        GetShared.ReadString("mobile"),
                                        "0");
                                call_amount.enqueue(new Callback<RetrofitResponse>() {
                                    @Override
                                    public void onResponse(@NotNull Call<RetrofitResponse> call, @NotNull Response<RetrofitResponse> response) {
                                        ArrayList<Good> goods = response.body().getGoods();
                                        Good good= goods.get(0);
                                        Call<RetrofitResponse> call2 = apiInterface.InsertBasket(
                                                "Insertbasket",
                                                "DeviceCode",
                                                s[0],
                                                String.valueOf(Float.parseFloat(amo)+ Float.parseFloat(good.getGoodFieldValue("BasketAmount"))),
                                                s[1],
                                                s[3],
                                                s[4],
                                                "test",
                                                GetShared.ReadString("mobile"));
                                        call2.enqueue(new Callback<RetrofitResponse>() {
                                            @Override
                                            public void onResponse(@NotNull Call<RetrofitResponse> call, @NotNull Response<RetrofitResponse> response) {
                                                Goods = response.body().getGoods();
                                                if (Integer.parseInt(Goods.get(0).getGoodFieldValue("ErrCode"))> 0){
                                                    App.showToast( Goods.get(0).getGoodFieldValue("ErrDesc")+"برای"+s[2]);
                                                }else{
                                                    setupBadge();
                                                }
                                            }
                                            @Override
                                            public void onFailure(@NotNull Call<RetrofitResponse> call, @NotNull Throwable t) {
                                                Log.e("onFailure", "" + t.toString());
                                            }
                                        });
                                    }
                                    @Override
                                    public void onFailure(@NotNull Call<RetrofitResponse> call, @NotNull Throwable t) {
                                    }
                                });

                            }

                            dialog.dismiss();
                            item_multi.findItem(R.id.menu_multi).setVisible(false);
                            for (Good good : goods) {
                                good.setCheck(false);
                            }
                            Multi_buy.clear();
                            if(GetShared.ReadString("view").equals( "grid")){
                                adapter.multi_select=false;
                                adapter.notifyDataSetChanged();
                            }else{
                                adapter_line.multi_select=false;
                                adapter_line.notifyDataSetChanged();
                            }
                            fab.setVisibility(View.GONE);
                        } else {
                            App.showToast("تعداد مورد نظر صحیح نمی باشد.");
                        }
                    } else {
                        App.showToast("تعداد مورد نظر صحیح نمی باشد.");
                    }
                }
            });

        });


    }

    private void allgood() {

        Call<RetrofitResponse> call = apiInterface.GetAllGood(
                "goodinfo",
                "0",
                "",
                "",
                "0",
                String.valueOf(PageNo),
                GetShared.ReadString("mobile"),
                "0"
        );
        call.enqueue(new Callback<RetrofitResponse>() {
            @Override
            public void onResponse(@NotNull Call<RetrofitResponse> call, @NotNull Response<RetrofitResponse> response) {
                if (response.isSuccessful()) {
                    goods = response.body().getGoods();
                    if(GetShared.ReadString("view").equals("grid")){
                        adapter = new Good_ProSearch_Adapter(goods,Search_date_detailActivity.this);
                        adapter_line = new Good_ProSearch_Line_Adapter(goods, Search_date_detailActivity.this);
                        adapter.multi_select=false;
                        gridLayoutManager = new GridLayoutManager(App.getContext(),2);
                        rc_good.setLayoutManager(gridLayoutManager);
                        rc_good.setAdapter(adapter);
                        rc_good.setItemAnimator(new FlipInTopXAnimator());

                    }else{
                        adapter_line = new Good_ProSearch_Line_Adapter(goods, Search_date_detailActivity.this);
                        adapter = new Good_ProSearch_Adapter(goods, Search_date_detailActivity.this);
                        adapter_line.multi_select=false;
                        gridLayoutManager = new GridLayoutManager(App.getContext(),1);
                        rc_good.setLayoutManager(gridLayoutManager);
                        rc_good.setAdapter(adapter_line);
                        rc_good.setItemAnimator(new FlipInTopXAnimator());
                    }
                    item_multi.findItem(R.id.menu_grid).setVisible(true);

                    prog.setVisibility(View.GONE);
                    loading = true;

                }
            }

            @Override
            public void onFailure(@NotNull Call<RetrofitResponse> call, @NotNull Throwable t) {
                    Log.e("retrofit_fail",t.getMessage());
           }
        });

    }


    private void allgood_more() {
        prog.setVisibility(View.VISIBLE);
        Call<RetrofitResponse> call = apiInterface.GetAllGood(
                "goodinfo",
                "0",
                "",
                "",
                "0",
                String.valueOf(PageNo),
                GetShared.ReadString("mobile"),
                "0"
        );
        call.enqueue(new Callback<RetrofitResponse>() {
            @Override
            public void onResponse(@NotNull Call<RetrofitResponse> call, @NotNull Response<RetrofitResponse> response) {
                if (response.isSuccessful()) {
                    ArrayList<Good> good_page = response.body().getGoods();
                    goods.addAll(good_page);
                    if(GetShared.ReadString("view").equals("grid")){
                        adapter.notifyDataSetChanged();
                    }else{
                        adapter_line.notifyDataSetChanged();
                    }
                    prog.setVisibility(View.GONE);
                    loading = true;

                }
            }

            @Override
            public void onFailure(@NotNull Call<RetrofitResponse> call, @NotNull Throwable t) {
                PageNo--;
                prog.setVisibility(View.GONE);
                loading = true;

           }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        item_multi=menu;
        if (GetShared.ReadString("view").equals("grid"))
            item_multi.findItem(R.id.menu_grid).setIcon(R.drawable.ic_view_line_24dp);
        else
            item_multi.findItem(R.id.menu_grid).setIcon(R.drawable.ic_view_grid_24dp);

        final MenuItem menuItem = menu.findItem(R.id.basket_menu);
        View actionView = menuItem.getActionView();
        textCartItemCount =  actionView.findViewById(R.id.cart_badge);
        setupBadge();
        actionView.setOnClickListener(v -> onOptionsItemSelected(menuItem));

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.basket_menu) {
            intent = new Intent(this, BuyActivity.class);
            GetShared.EditString("basket_position", "0");
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


            if(GetShared.ReadString("view").equals("grid")){

                item_multi.findItem(R.id.menu_grid).setIcon(R.drawable.ic_view_grid_24dp);
                GetShared.EditString("view","line");
                adapter_line.multi_select=false;
                gridLayoutManager = new GridLayoutManager(App.getContext(),1);
                gridLayoutManager.scrollToPosition(pastVisiblesItems+1);
                rc_good.setLayoutManager(gridLayoutManager);
                rc_good.setAdapter(adapter_line);

            }else{

                item_multi.findItem(R.id.menu_grid).setIcon(R.drawable.ic_view_line_24dp);
                GetShared.EditString("view","grid");
                adapter.multi_select=false;
                gridLayoutManager = new GridLayoutManager(App.getContext(),2);
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
            if(GetShared.ReadString("view").equals("grid")){
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
            Call<RetrofitResponse> call2 = apiInterface.GetbasketSum(
                    "BasketSum",
                    GetShared.ReadString("mobile")
            );
            call2.enqueue(new Callback<RetrofitResponse>() {
                @Override
                public void onResponse(@NotNull Call<RetrofitResponse> call, @NotNull Response<RetrofitResponse> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        Goods_setupBadge = response.body().getGoods();

                        textCartItemCount.setText(NumberFunctions.PerisanNumber(Goods_setupBadge.get(0).getGoodFieldValue("SumFacAmount")));
                        if(Integer.parseInt(Goods_setupBadge.get(0).getGoodFieldValue("SumFacAmount"))>0) {
                            if (textCartItemCount.getVisibility() != View.VISIBLE) {
                                textCartItemCount.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
                @Override
                public void onFailure(@NotNull Call<RetrofitResponse> call, @NotNull Throwable t) {
                    Log.e("retrofit_fail",t.getMessage());
                }
            });
        }
    }




    public void good_select_function(Good goodView, int flag) {

        if (flag == 1) {
            fab.setVisibility(View.VISIBLE);
            Multi_buy.add(new String[]{goodView.getGoodFieldValue("GoodCode"),
                    goodView.getGoodFieldValue("SellPrice"),
                    goodView.getGoodFieldValue("GoodName"),
                    goodView.getGoodFieldValue("GoodUnitRef"),
                    goodView.getGoodFieldValue("DefaultUnitValue")});
            item_multi.findItem(R.id.menu_multi).setVisible(true);
        } else {
            int b = 0, c = 0;
            for (String[] s : Multi_buy) {
                if (s[0].equals(goodView.getGoodFieldValue("GoodCode"))) b = c;
                c++;
            }
            Multi_buy.remove(b);
            if (Multi_buy.size() < 1) {
                if(GetShared.ReadString("view").equals( "grid")){
                    adapter.multi_select=false;
                }else{
                    adapter_line.multi_select=false;
                }
                fab.setVisibility(View.GONE);
                item_multi.findItem(R.id.menu_multi).setVisible(false);
                set_rc_good();
            }
        }
    }

    public void set_rc_good() {
        if(GetShared.ReadString("view").equals("grid")){
            adapter = new Good_ProSearch_Adapter(goods,this);
            adapter.multi_select=false;
            gridLayoutManager = new GridLayoutManager(App.getContext(),2);
            gridLayoutManager.scrollToPosition(pastVisiblesItems+2);
            rc_good.setLayoutManager(gridLayoutManager);
            rc_good.setAdapter(adapter);
            rc_good.setItemAnimator(new FlipInTopXAnimator());

        }else{
            adapter_line = new Good_ProSearch_Line_Adapter(goods, this);
            adapter_line.multi_select=false;
            gridLayoutManager = new GridLayoutManager(App.getContext(),1);
            gridLayoutManager.scrollToPosition(pastVisiblesItems+1);
            rc_good.setLayoutManager(gridLayoutManager);
            rc_good.setAdapter(adapter_line);
            rc_good.setItemAnimator(new FlipInTopXAnimator());
        }

    }

}
