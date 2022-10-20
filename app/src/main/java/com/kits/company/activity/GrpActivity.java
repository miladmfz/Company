package com.kits.company.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.kits.company.R;
import com.kits.company.adapter.GetShared;
import com.kits.company.adapter.Good_ProSearch_Adapter;
import com.kits.company.adapter.Good_ProSearch_Line_Adapter;
import com.kits.company.adapter.Grp_Vlist_detail_Adapter;
import com.kits.company.adapter.InternetConnection;
import com.kits.company.adapter.Search_box;
import com.kits.company.application.App;
import com.kits.company.model.Good;
import com.kits.company.model.GoodGroup;
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


public class GrpActivity extends AppCompatActivity {


    Integer id=0,conter=0;
    Intent intent;
    String title="گروه ها";
    MaterialCardView line_pro, line ;
    Button change_search , filter_active;
    EditText edtsearch;
    Handler handler= new Handler();
    APIInterface apiInterface = APIClient.getCleint().create(APIInterface.class);
    RecyclerView rc_grp,rc_good;
    Toolbar toolbar;
    ArrayList<Good> goods;
    ArrayList<GoodGroup> Groups;
    ProgressBar prog;
    TextView textCartItemCount;
    ArrayList<Good> Goods,Goods_setupBadge;
    GridLayoutManager gridLayoutManager;
    public String srch="", sq="";
    private boolean loading = true;
    int pastVisiblesItems=0, visibleItemCount, totalItemCount;
    public int PageNo=0;
    Good_ProSearch_Adapter adapter;
    Good_ProSearch_Line_Adapter adapter_line;
    ArrayList<String[]> Multi_buy = new ArrayList<>();
    FloatingActionButton fab;
    Menu item_multi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grp);


        intent();

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

    //*************************************************


    public void init() {


        toolbar =  findViewById(R.id.GrpActivity_toolbar);
        change_search = findViewById(R.id.GrpActivity_change_search);
        line = findViewById(R.id.GrpActivity_search_line);
        edtsearch = findViewById(R.id.GrpActivity_edtsearch);
        prog = findViewById(R.id.GrpActivity_prog);
        rc_grp = findViewById(R.id.GrpActivity_grp);
        rc_good = findViewById(R.id.GrpActivity_good);
        fab = findViewById(R.id.GrpActivity_fab);




        toolbar.setTitle(title);
        setSupportActionBar(toolbar);


        final SwitchMaterial mySwitch_activestack = findViewById(R.id.grpActivity_switch);
        mySwitch_activestack.setChecked(GetShared.ReadBoolan("available_good"));
        mySwitch_activestack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                GetShared.EditBoolan("available_good",!GetShared.ReadBoolan("available_good"));
                if(GetShared.ReadString("view").equals( "grid")){
                    adapter.notifyDataSetChanged();
                    gridLayoutManager = new GridLayoutManager(App.getContext(),2);
                    gridLayoutManager.scrollToPosition(pastVisiblesItems+2);
                    rc_good.setLayoutManager(gridLayoutManager);
                    rc_good.setAdapter(adapter);
                }else{
                    adapter_line.notifyDataSetChanged();
                    gridLayoutManager = new GridLayoutManager(App.getContext(),1);
                    gridLayoutManager.scrollToPosition(pastVisiblesItems+1);
                    rc_good.setLayoutManager(gridLayoutManager);
                    rc_good.setAdapter(adapter_line);
                }


            }
        });


        allgrp();


        edtsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtsearch.selectAll();
            }
        });

        edtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(final Editable editable) {
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(() -> {

                    srch = NumberFunctions.EnglishNumber(editable.toString());
                    srch= srch.replaceAll(" ","%");
                    PageNo=0;

                    allgood(srch,sq);
                },500);


            }
        });

        allgood(srch,sq);

        change_search.setOnClickListener(view -> {
            Search_box search_box = new Search_box(GrpActivity.this);
            search_box.search_pro();
        });


        rc_good.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount =   gridLayoutManager.getChildCount();
                    totalItemCount =   gridLayoutManager.getItemCount();
                    pastVisiblesItems =   gridLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount-2) {
                            loading = false;
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

                final Dialog dialog = new Dialog(GrpActivity.this);
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
                        final String amo = amount_mlti.getText().toString();
                        if (!amo.equals("")) {
                            if (Integer.parseInt(amo) != 0) {

                                for (final String[] s : Multi_buy) {

                                    Call<RetrofitResponse> call_amount = apiInterface.GetAllGood
                                            ("goodinfo",
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
                                                    GetShared.ReadString("mobile")
                                            );
                                            call2.enqueue(new Callback<RetrofitResponse>() {
                                                @Override
                                                public void onResponse(@NotNull Call<RetrofitResponse> call, @NotNull Response<RetrofitResponse> response) {
                                                    Log.e("onResponse", "" + response.body());
                                                    assert response.body() != null;
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
                                if(GetShared.ReadString("view").equals("grid")){
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
            }
        });
    }


    private void allgrp() {


        Call<RetrofitResponse> call = apiInterface.Getgrp(
                "GoodGroupInfo",
                String.valueOf(id)
        );
        call.enqueue(new Callback<RetrofitResponse>() {
            @Override
            public void onResponse(@NotNull Call<RetrofitResponse> call, @NotNull Response<RetrofitResponse> response) {
                if (response.isSuccessful()) {
                    Groups = response.body().getGroups();
                    Grp_Vlist_detail_Adapter adapter = new Grp_Vlist_detail_Adapter(Groups, App.getContext());
                    LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(App.getContext(), LinearLayoutManager.HORIZONTAL, false);
                    rc_grp.setLayoutManager(horizontalLayoutManager);
                    rc_grp.setAdapter(adapter);
                    rc_grp.setItemAnimator(new DefaultItemAnimator());
                    prog.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(@NotNull Call<RetrofitResponse> call, @NotNull Throwable t) {
                rc_grp.setVisibility(View.GONE);
            }
        });
    }

    public void allgood(String edtsearch,String where) {
        prog.setVisibility(View.VISIBLE);
        prog.setVisibility(View.VISIBLE);
        Call<RetrofitResponse> call = apiInterface.GetAllGood(
                "goodinfo",
                "0",
                edtsearch,
                where,
                String.valueOf(id),
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
                        adapter = new Good_ProSearch_Adapter(goods, GrpActivity.this);
                        adapter_line = new Good_ProSearch_Line_Adapter(goods, GrpActivity.this);
                        gridLayoutManager = new GridLayoutManager(App.getContext(),2);
                        rc_good.setLayoutManager(gridLayoutManager);
                        rc_good.setAdapter(adapter);
                        rc_good.setItemAnimator(new FlipInTopXAnimator());
                    }else{
                        adapter = new Good_ProSearch_Adapter(goods, GrpActivity.this);
                        adapter_line = new Good_ProSearch_Line_Adapter(goods, GrpActivity.this);
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
                prog.setVisibility(View.GONE);
                App.showToast("کالایی در این گروه یافت نشد");
                PageNo=0;
            }
        });
    }


    private void allgood_more(String edtsearch,String where) {
        prog.setVisibility(View.VISIBLE);
        Call<RetrofitResponse> call = apiInterface.GetAllGood(
                "goodinfo",
                "0",
                edtsearch,
                where,
                String.valueOf(id),
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

    public void intent() {
        Bundle data = getIntent().getExtras();
        assert data != null;
        id = data.getInt("id");
        title = data.getString("title");
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
                adapter.multi_select=false;
                adapter.notifyDataSetChanged();
            }else{
                adapter_line.multi_select=false;
                adapter_line.notifyDataSetChanged();
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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
            adapter = new Good_ProSearch_Adapter(goods, this);
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
