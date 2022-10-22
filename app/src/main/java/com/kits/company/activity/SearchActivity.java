package com.kits.company.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.kits.company.R;
import com.kits.company.adapter.GetShared;
import com.kits.company.adapter.GoodAdapter;
import com.kits.company.adapter.GrpAdapter;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchActivity extends AppCompatActivity {


    Integer id = 0, conter = 0;
    Intent intent;
    String title = "گروه ها";
    MaterialCardView line_pro, line;
    Button change_search, filter_active;
    EditText edtsearch;
    Handler handler = new Handler();
    APIInterface apiInterface = APIClient.getCleint().create(APIInterface.class);
    RecyclerView rc_grp, rc_good;
    Toolbar toolbar;
    ArrayList<Good> goods;
    ArrayList<GoodGroup> Groups;
    LottieAnimationView prog;
    TextView textCartItemCount;
    TextView tvstatus;
    ArrayList<Good> Goods, Goods_setupBadge;
    GridLayoutManager gridLayoutManager;
    public String srch = "", sq = "";
    private boolean loading = true;
    int pastVisiblesItems = 0, visibleItemCount, totalItemCount;
    public int PageNo = 0;
    GoodAdapter adapter;
    ArrayList<Good> Multi_Good = new ArrayList<>();
    FloatingActionButton fab;
    Menu item_multi;

    LottieAnimationView lottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grp);

        intent();

        InternetConnection ic = new InternetConnection(this);
        if (ic.has()) {
            try {
                init();
            } catch (Exception e) {
                GetShared.ErrorLog(e.getMessage());
            }
        } else {
            intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
            finish();
        }

    }

    //*************************************************

    public void intent() {
        Bundle data = getIntent().getExtras();
        assert data != null;
        id = data.getInt("id");
        title = data.getString("title");
    }

    public void GetFirstData() {
        Call<RetrofitResponse> call1 = apiInterface.info("kowsar_info", "AppCustomer_DefaultGroupCode");
        call1.enqueue(new Callback<RetrofitResponse>() {
            @Override
            public void onResponse(@NotNull Call<RetrofitResponse> call, @NotNull Response<RetrofitResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    id = Integer.parseInt(response.body().getText());
                    allgrp();
                    allgood(srch, sq);
                }
            }

            @Override
            public void onFailure(@NotNull Call<RetrofitResponse> call, @NotNull Throwable t) {
            }
        });

    }

    @SuppressLint("NotifyDataSetChanged")
    public void init() {


        toolbar = findViewById(R.id.SearchActivity_toolbar);
        change_search = findViewById(R.id.SearchActivity_change_search);
        line = findViewById(R.id.SearchActivity_search_line);
        edtsearch = findViewById(R.id.SearchActivity_edtsearch);
        prog = findViewById(R.id.SearchActivity_prog);
        rc_grp = findViewById(R.id.SearchActivity_grp);
        rc_good = findViewById(R.id.SearchActivity_good);
        fab = findViewById(R.id.SearchActivity_fab);
        lottie = findViewById(R.id.SearchActivity_lottie);
        tvstatus = findViewById(R.id.SearchActivity_tvstatus);


        toolbar.setTitle(title);
        setSupportActionBar(toolbar);


        final SwitchMaterial mySwitch_activestack = findViewById(R.id.SearchActivity_switch);
        mySwitch_activestack.setChecked(GetShared.ReadBoolan("available_good"));
        mySwitch_activestack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                GetShared.EditBoolan("available_good", !GetShared.ReadBoolan("available_good"));
                if (GetShared.ReadString("view").equals("grid")) {
                    gridLayoutManager = new GridLayoutManager(App.getContext(), 2);
                    gridLayoutManager.scrollToPosition(pastVisiblesItems + 2);
                } else {
                    gridLayoutManager = new GridLayoutManager(App.getContext(), 1);
                    gridLayoutManager.scrollToPosition(pastVisiblesItems + 1);
                }
                adapter.notifyDataSetChanged();
                rc_good.setLayoutManager(gridLayoutManager);
                rc_good.setAdapter(adapter);


            }
        });


        edtsearch.setOnClickListener(view -> edtsearch.selectAll());

        edtsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(final Editable editable) {
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(() -> {

                    srch = NumberFunctions.EnglishNumber(editable.toString());
                    srch = srch.replaceAll(" ", "%");
                    PageNo = 0;

                    allgood(srch, sq);
                }, 500);


            }
        });


        change_search.setOnClickListener(view -> {
            Search_box search_box = new Search_box(SearchActivity.this);
            search_box.search_pro();
        });


        rc_good.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount = gridLayoutManager.getChildCount();
                    totalItemCount = gridLayoutManager.getItemCount();
                    pastVisiblesItems = gridLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount - 2) {
                            loading = false;
                            PageNo++;
                            allgood_more(srch, sq);
                        }
                    }
                }
            }
        });
        fab.setOnClickListener(v -> {

            final Dialog dialog = new Dialog(SearchActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//title laye nadashte bashim
            dialog.setContentView(R.layout.box_multi_buy);
            Button boxbuy = dialog.findViewById(R.id.box_multi_buy_btn);
            final EditText amount_mlti = dialog.findViewById(R.id.box_multi_buy_amount);
            final TextView tv = dialog.findViewById(R.id.box_multi_buy_factor);
            dialog.show();
            amount_mlti.requestFocus();
            amount_mlti.postDelayed(() -> {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(amount_mlti, InputMethodManager.SHOW_IMPLICIT);
            }, 500);

            boxbuy.setOnClickListener(view -> {

                final String amo = amount_mlti.getText().toString();
                if (!amo.equals("")) {
                    if (Integer.parseInt(amo) != 0) {

                        for (Good good : Multi_Good) {

                            Call<RetrofitResponse> call_amount = apiInterface.GetAllGood
                                    ("goodinfo",
                                            "0",
                                            "",
                                            "goodcode=" + Integer.parseInt(good.getGoodFieldValue("GoodCode")),
                                            "0",
                                            "0",
                                            GetShared.ReadString("mobile"),
                                            "0");
                            call_amount.enqueue(new Callback<RetrofitResponse>() {
                                @Override
                                public void onResponse(@NotNull Call<RetrofitResponse> call, @NotNull Response<RetrofitResponse> response) {
                                    assert response.body() != null;
                                    ArrayList<Good> goods = response.body().getGoods();
                                    Good good = goods.get(0);
                                    Call<RetrofitResponse> call2 = apiInterface.InsertBasket(
                                            "Insertbasket",
                                            "DeviceCode",
                                            good.getGoodFieldValue("GoodCode"),
                                            String.valueOf(Float.parseFloat(amo) + Float.parseFloat(good.getGoodFieldValue("BasketAmount"))),
                                            good.getGoodFieldValue("SellPrice"),
                                            good.getGoodFieldValue("GoodUnitRef"),
                                            good.getGoodFieldValue("DefaultUnitValue"),
                                            "test",
                                            GetShared.ReadString("mobile")
                                    );
                                    call2.enqueue(new Callback<RetrofitResponse>() {
                                        @Override
                                        public void onResponse(@NotNull Call<RetrofitResponse> call, @NotNull Response<RetrofitResponse> response) {
                                            assert response.body() != null;
                                            Goods = response.body().getGoods();
                                            if (Integer.parseInt(Goods.get(0).getGoodFieldValue("ErrCode")) > 0) {
                                                App.showToast(Goods.get(0).getGoodFieldValue("ErrDesc") + "برای" + good.getGoodFieldValue("GoodName"));
                                            } else {
                                                setupBadge();
                                            }
                                        }

                                        @Override
                                        public void onFailure(@NotNull Call<RetrofitResponse> call, @NotNull Throwable t) {
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
                        Multi_Good.clear();
                        adapter.multi_select = false;
                        adapter.notifyDataSetChanged();
                        fab.setVisibility(View.GONE);
                    } else {
                        App.showToast("تعداد مورد نظر صحیح نمی باشد.");
                    }
                } else {
                    App.showToast("تعداد مورد نظر صحیح نمی باشد.");
                }
            });
        });

        if (id == 0) {
            GetFirstData();
        } else {
            allgrp();
            allgood(srch, sq);
        }
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
                    assert response.body() != null;
                    Groups = response.body().getGroups();
                    GrpAdapter adapter = new GrpAdapter(Groups, App.getContext());
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

    public void allgood(String edtsearch, String where) {
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
                    assert response.body() != null;
                    goods = response.body().getGoods();
                    CallRecycler();
                }
            }

            @Override
            public void onFailure(@NotNull Call<RetrofitResponse> call, @NotNull Throwable t) {
                App.showToast("کالایی در این گروه یافت نشد");
                goods.clear();
                CallRecycler();
                PageNo = 0;
            }
        });
    }


    private void allgood_more(String edtsearch, String where) {
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
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NotNull Call<RetrofitResponse> call, @NotNull Response<RetrofitResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    goods.addAll(response.body().getGoods());
                    adapter.notifyDataSetChanged();
                    prog.setVisibility(View.GONE);
                    loading = true;
                }
            }

            @Override
            public void onFailure(@NotNull Call<RetrofitResponse> call, @NotNull Throwable t) {
                App.showToast("کالای بیشتری یافت نشد");
                prog.setVisibility(View.GONE);
                PageNo--;
                loading = true;
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        item_multi = menu;
        if (GetShared.ReadString("view").equals("grid"))
            item_multi.findItem(R.id.menu_grid).setIcon(R.drawable.ic_view_line_24dp);
        else
            item_multi.findItem(R.id.menu_grid).setIcon(R.drawable.ic_view_grid_24dp);
        final MenuItem menuItem = menu.findItem(R.id.basket_menu);
        View actionView = menuItem.getActionView();
        textCartItemCount = actionView.findViewById(R.id.cart_badge);
        setupBadge();
        actionView.setOnClickListener(v -> onOptionsItemSelected(menuItem));
        return true;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.basket_menu) {
            intent = new Intent(this, BasketActivity.class);
            GetShared.EditString("basket_position", "0");
            startActivity(intent);
            return true;
        }
        if (item.getItemId() == R.id.menu_grid) {

            for (Good good : goods) {
                good.setCheck(false);
            }
            if (GetShared.ReadString("view").equals("grid")) {
                item_multi.findItem(R.id.menu_grid).setIcon(R.drawable.ic_view_grid_24dp);
                GetShared.EditString("view", "line");
            } else {
                item_multi.findItem(R.id.menu_grid).setIcon(R.drawable.ic_view_line_24dp);
                GetShared.EditString("view", "grid");
            }
            CallRecycler();
            return true;
        }
        if (item.getItemId() == R.id.menu_multi) {
            item_multi.findItem(R.id.menu_multi).setVisible(false);
            for (Good good : goods) {
                good.setCheck(false);
            }
            Multi_Good.clear();
            adapter.multi_select = false;
            adapter.notifyDataSetChanged();
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
                        if (Integer.parseInt(Goods_setupBadge.get(0).getGoodFieldValue("SumFacAmount")) > 0) {
                            if (textCartItemCount.getVisibility() != View.VISIBLE) {
                                textCartItemCount.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<RetrofitResponse> call, @NotNull Throwable t) {
                }
            });
        }
    }

    public void good_select_function(Good good) {
        if (!Multi_Good.contains(good)) {
            Multi_Good.add(good);

            fab.setVisibility(View.VISIBLE);
            item_multi.findItem(R.id.menu_multi).setVisible(true);
        } else {
            Multi_Good.remove(good);

            if (Multi_Good.size() < 1) {
                fab.setVisibility(View.GONE);
                adapter.multi_select = false;
                item_multi.findItem(R.id.menu_multi).setVisible(false);
            }
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    public void RefreshData(Good good) {
        for (Good singlegood:goods){
            if(singlegood.getGoodFieldValue("GoodCode").equals(good.getGoodFieldValue("GoodCode"))){
                goods.get(goods.indexOf(singlegood)).setFacAmount(good.getGoodFieldValue("FacAmount"));
            }
        }
        adapter.notifyDataSetChanged();
    }
    public void CallRecycler() {
        adapter = new GoodAdapter(goods, this);

        Multi_Good.clear();
        fab.setVisibility(View.GONE);
        item_multi.findItem(R.id.menu_multi).setVisible(false);
        item_multi.findItem(R.id.menu_grid).setVisible(true);
        loading = true;
        adapter.multi_select = false;

        if (adapter.getItemCount() == 0) {
            tvstatus.setText("کالایی یافت نشد");
            tvstatus.setVisibility(View.VISIBLE);
            lottie.setVisibility(View.VISIBLE);
        } else {
            tvstatus.setVisibility(View.GONE);
            lottie.setVisibility(View.GONE);
        }

        if (GetShared.ReadString("view").equals("grid")) {
            gridLayoutManager = new GridLayoutManager(App.getContext(), 2);
            gridLayoutManager.scrollToPosition(pastVisiblesItems + 2);
        } else {
            gridLayoutManager = new GridLayoutManager(App.getContext(), 1);
            gridLayoutManager.scrollToPosition(pastVisiblesItems + 1);
        }
        rc_good.setLayoutManager(gridLayoutManager);
        rc_good.setAdapter(adapter);
        rc_good.setItemAnimator(new DefaultItemAnimator());
        prog.setVisibility(View.GONE);
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        setupBadge();
        super.onWindowFocusChanged(hasFocus);
    }
}
