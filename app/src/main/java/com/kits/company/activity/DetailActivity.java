package com.kits.company.activity;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.button.MaterialButton;
import com.kits.company.R;
import com.kits.company.adapter.BuyBox;
import com.kits.company.adapter.GetShared;
import com.kits.company.adapter.Good_view_Adapter;
import com.kits.company.adapter.InternetConnection;
import com.kits.company.adapter.SliderAdapter;
import com.kits.company.application.App;
import com.kits.company.model.Column;
import com.kits.company.model.Good;
import com.kits.company.model.NumberFunctions;
import com.kits.company.model.RetrofitResponse;
import com.kits.company.webService.APIClient;
import com.kits.company.webService.APIInterface;

import org.jetbrains.annotations.NotNull;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailActivity extends AppCompatActivity {

    APIInterface apiInterface = APIClient.getCleint().create(APIInterface.class);
    String ImageName, name, price;
    TextView goodcode, goodname, sellprice, details, sellpercent, goodexplain1, goodexplain2, goodexplain3;


    TextView goodtype;
    TextView barcode;
    TextView isbn;
    TextView writer, dragoman, tahvildate, printperiod, covertype, size, pageno, grp, maxsellpriceTextView;
    TextView tv_property, tv_detail;
    LinearLayoutCompat line_property, line_detail, line_slideshow;
    ArrayList<Good> goods;
    Good good;
    Button btnbuy;
    MaterialButton favorite;
    RecyclerView rc_likegood;
    Integer id;
    Integer img_count = 0;
    Intent intent;
    DecimalFormat decimalFormat = new DecimalFormat("0,000");
    SliderView sliderView;
    ProgressBar prog;
    LinearLayoutManager horizontalLayoutManager;
    TextView textCartItemCount;
    ArrayList<Good> Goods;
    int pastVisiblesItems = 0, visibleItemCount, totalItemCount, PageNo = 0, favorite_bol = 0;
    ArrayList<Good> available_goods1 = new ArrayList<>();
    Good_view_Adapter adapter;
    ArrayList<Column> Columns;
    BuyBox buyBox;
    private boolean loading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = findViewById(R.id.DetailActivity_toolbar);
        setSupportActionBar(toolbar);


        intent();

        InternetConnection ic = new InternetConnection(this);
        if (ic.has()) {
            try {
                init();
            }catch (Exception e){
                GetShared.ErrorLog(e.getMessage());
            }
        } else {
            intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
            finish();
        }
    }

    //*********************************************************************


    public void intent() {
        Bundle data = getIntent().getExtras();
        id = data.getInt("id");
    }


    public void init() {
        buyBox = new BuyBox(this);
        config();

        good_call();
        good_groups();


        btnbuy.setOnClickListener(view -> buyBox.buydialog(good));


        favorite.setOnClickListener(view -> {

            if (favorite_bol == 1) {
                Call<RetrofitResponse> call = apiInterface.Favorite_action(
                        "Favorite_action",
                        GetShared.ReadString("mobile"),
                        id.toString(),
                        "1");
                call.enqueue(new Callback<RetrofitResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<RetrofitResponse> call, @NotNull Response<RetrofitResponse> response) {
                        Log.e("onResponse", "" + response.body());
                        assert response.body() != null;
                        if (response.body().getText().equals("1")) {
                            Log.e("onResponse2", "" + response.body());
                            favorite_bol = 0;
                            favorite.setIconResource(R.drawable.ic_favorite_border_black_24dp);
                            favorite.setIconTintResource(R.color.grey_900);

                            App.showToast("از علاقه مندی ها حذف گردید");
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<RetrofitResponse> call, @NotNull Throwable t) {
                        Log.e("onFailure", "" + t.toString());
                    }
                });

            } else {


                Call<RetrofitResponse> call = apiInterface.Favorite_action(
                        "Favorite_action",
                        GetShared.ReadString("mobile"),
                        id.toString(),
                        "0"
                );
                call.enqueue(new Callback<RetrofitResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<RetrofitResponse> call, @NotNull Response<RetrofitResponse> response) {
                        Log.e("onResponse", "" + response.body());
                        assert response.body() != null;
                        if (response.body().getText().equals("1")) {
                            favorite_bol = 1;
                            App.showToast("به علاقه مندی ها اضافه شد");
                            favorite.setIconResource(R.drawable.ic_favorite_black_24dp);
                            favorite.setIconTintResource(R.color.red_900);
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<RetrofitResponse> call, @NotNull Throwable t) {
                        Log.e("onFailure", "" + t.toString());
                    }
                });
            }
        });


        rc_likegood.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dx < 0) { //check for scroll down
                    visibleItemCount = horizontalLayoutManager.getChildCount();
                    totalItemCount = horizontalLayoutManager.getItemCount();
                    pastVisiblesItems = horizontalLayoutManager.findFirstVisibleItemPosition();


                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount - 2) {
                            loading = false;
                            PageNo++;
                            good_groups_more();
                        }
                    }
                }
            }
        });


    }

    private void SliderView() {

        sliderView = findViewById(R.id.DetailActivity_imageSlider);
        SliderAdapter adapter = new SliderAdapter(this, id, img_count, goods, true);
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimations.SCALE); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3); //set scroll delay in seconds :
        sliderView.startAutoCycle();
    }

    public void good_groups() {
        Call<RetrofitResponse> call31 = apiInterface.GetLikeGood(
                "goodinfo",
                id.toString(),
                String.valueOf(PageNo)
        );
        call31.enqueue(new Callback<RetrofitResponse>() {
            @Override
            public void onResponse(@NotNull Call<RetrofitResponse> call, @NotNull Response<RetrofitResponse> response) {
                if (response.isSuccessful()) {
                    goods = response.body().getGoods();
                    available_goods1.clear();
                    for (Good g : goods) {
                        if (Integer.parseInt(g.getGoodFieldValue("HasStackAmount")) > 0) {
                            available_goods1.add(g);
                        }
                    }
                    Good_view_Adapter adapter = new Good_view_Adapter(available_goods1, App.getContext());
                    horizontalLayoutManager = new LinearLayoutManager(App.getContext(), LinearLayoutManager.HORIZONTAL, false);
                    rc_likegood.setLayoutManager(horizontalLayoutManager);
                    rc_likegood.setAdapter(adapter);
                    rc_likegood.setItemAnimator(new DefaultItemAnimator());
                }
            }

            @Override
            public void onFailure(@NotNull Call<RetrofitResponse> call, @NotNull Throwable t) {
                Log.e("retrofit_fail", t.getMessage());


            }
        });


    }

    public void good_groups_more() {
        Call<RetrofitResponse> call31 = apiInterface.GetLikeGood(
                "goodinfo",
                id.toString(),
                String.valueOf(PageNo)
        );
        call31.enqueue(new Callback<RetrofitResponse>() {
            @Override
            public void onResponse(@NotNull Call<RetrofitResponse> call, @NotNull Response<RetrofitResponse> response) {
                if (response.isSuccessful()) {
                    ArrayList<Good> good_page = response.body().getGoods();
                    goods.addAll(good_page);
                    adapter = new Good_view_Adapter(goods, App.getContext());
                    horizontalLayoutManager = new LinearLayoutManager(App.getContext(), LinearLayoutManager.HORIZONTAL, false);
                    horizontalLayoutManager.scrollToPosition(pastVisiblesItems + 1);
                    rc_likegood.setLayoutManager(horizontalLayoutManager);
                    rc_likegood.setAdapter(adapter);
                    rc_likegood.setItemAnimator(new DefaultItemAnimator());
                    loading = true;

                }
            }

            @Override
            public void onFailure(@NotNull Call<RetrofitResponse> call, @NotNull Throwable t) {
                PageNo--;

                Log.e("retrofit_fail", t.getMessage());


            }
        });


    }

    public void good_call() {
        Call<RetrofitResponse> call = apiInterface.GetColumn(
                "GetColumnList",
                String.valueOf(id),
                "",
                "0"
        );
        Log.e("test111",String.valueOf(id));
        call.enqueue(new Callback<RetrofitResponse>() {
            @Override
            public void onResponse(@NotNull Call<RetrofitResponse> call, @NotNull Response<RetrofitResponse> response) {
                if (response.isSuccessful()) {
                    Columns = response.body().getColumns();

                    Call<RetrofitResponse> call2 = apiInterface.GetAllGood
                            ("goodinfo",
                                    String.valueOf(id),
                                    "",
                                    "",
                                    "0",
                                    "0",
                                    GetShared.ReadString("mobile"),
                                    "0"
                            );
                    call2.enqueue(new Callback<RetrofitResponse>() {
                        @Override
                        public void onResponse(@NotNull Call<RetrofitResponse> call, @NotNull Response<RetrofitResponse> response) {
                            if (response.isSuccessful()) {
                                goods = response.body().getGoods();
                                good = goods.get(0);
                                name = good.getGoodFieldValue("GoodName");
                                price = good.getGoodFieldValue("SellPrice");
                                id = Integer.parseInt(good.getGoodFieldValue("GoodCode"));
                                img_count = Integer.parseInt(good.getGoodFieldValue("imagecount"));
                                goodname.setText(NumberFunctions.PerisanNumber(good.getGoodFieldValue("GoodName")));
                                sellprice.setText(NumberFunctions.PerisanNumber(good.getGoodFieldValue("SellPrice")));
                                if (good.getGoodFieldValue("MaxSellPrice").equals(good.getGoodFieldValue("SellPrice"))) {
                                    maxsellpriceTextView.setVisibility(View.GONE);
                                } else {
                                    maxsellpriceTextView.setVisibility(View.VISIBLE);
                                    SpannableString spannableString = new SpannableString(NumberFunctions.PerisanNumber(decimalFormat.format(Integer.parseInt(good.getGoodFieldValue("MaxSellPrice")))));
                                    spannableString.setSpan(new StrikethroughSpan(), 0, good.getGoodFieldValue("MaxSellPrice").length(), Spanned.SPAN_MARK_MARK);
                                    maxsellpriceTextView.setText(spannableString);
                                }

                                if (Integer.parseInt(good.getGoodFieldValue("HasStackAmount")) == 0) {

                                    btnbuy.setText("ناموجود");
                                    btnbuy.setClickable(false);
                                    btnbuy.setTextColor(App.getContext().getResources().getColor(R.color.white));
                                    btnbuy.setBackgroundColor(App.getContext().getResources().getColor(R.color.red_300));
                                }

                                if (Float.parseFloat(good.getGoodFieldValue("totalamount")) > 0) {
                                    btnbuy.setText("افزودن به سبدخرید");

                                } else {
                                    btnbuy.setText("ناموجود _ ثبت سفارش");
                                }

                                prog.setVisibility(View.GONE);
                                favorite_bol = Integer.parseInt(good.getGoodFieldValue("IsFavorite"));
                                if (Integer.parseInt(good.getGoodFieldValue("IsFavorite")) > 0) {
                                    favorite.setIconResource(R.drawable.ic_favorite_black_24dp);
                                    favorite.setIconTintResource(R.color.red_900);

                                } else {
                                    favorite.setIconResource(R.drawable.ic_favorite_border_black_24dp);
                                    favorite.setIconTintResource(R.color.grey_900);
                                }

                                SliderView();
                                for (Column Column : Columns) {

                                    if (Integer.parseInt(Column.getColumnFieldValue("SortOrder")) > 0) {
                                        LinearLayoutCompat ll = findViewById(R.id.DetailActivity_line_property);
                                        ll.setOrientation(LinearLayoutCompat.VERTICAL);
                                        LinearLayoutCompat ll_1 = new LinearLayoutCompat(App.getContext());
                                        ll_1.setOrientation(LinearLayoutCompat.HORIZONTAL);
                                        ll_1.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
                                        ll_1.setWeightSum(1);

                                        TextView extra_TextView1 = new TextView(App.getContext());
                                        extra_TextView1.setText(NumberFunctions.PerisanNumber(Column.getColumnFieldValue("ColumnDesc")));
                                        extra_TextView1.setBackgroundResource(R.color.grey_20);
                                        extra_TextView1.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.MATCH_PARENT, (float) 0.7));
                                        extra_TextView1.setTextSize(18);
                                        extra_TextView1.setPadding(2, 2, 2, 2);
                                        extra_TextView1.setGravity(Gravity.CENTER);
                                        extra_TextView1.setTextColor(getResources().getColor(R.color.grey_800));
                                        ll_1.addView(extra_TextView1);

                                        TextView extra_TextView2 = new TextView(App.getContext());
                                        extra_TextView2.setText(NumberFunctions.PerisanNumber(good.getGoodFieldValue(Column.getColumnFieldValue("columnname"))));
                                        extra_TextView2.setBackgroundResource(R.color.white);
                                        extra_TextView2.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.MATCH_PARENT, (float) 0.3));
                                        extra_TextView2.setTextSize(18);
                                        extra_TextView2.setPadding(2, 2, 2, 2);
                                        extra_TextView2.setGravity(Gravity.CENTER);
                                        extra_TextView2.setTextColor(getResources().getColor(R.color.grey_1000));
                                        ll_1.addView(extra_TextView2);

                                        ViewPager extra_ViewPager = new ViewPager(App.getContext());
                                        extra_ViewPager.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, 3));
                                        extra_ViewPager.setBackgroundResource(R.color.grey_40);

                                        ll.addView(ll_1);
                                        ll.addView(extra_ViewPager);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(@NotNull Call<RetrofitResponse> call, @NotNull Throwable t) {
                            finish();
                            Log.e("retrofit_fail", t.getMessage());
                        }
                    });


                }
            }

            @Override
            public void onFailure(@NotNull Call<RetrofitResponse> call, @NotNull Throwable t) {

            }
        });


    }

    public void config() {
        goodname = findViewById(R.id.DetailActivity_name);
        sellprice = findViewById(R.id.DetailActivity_sellprice);
        maxsellpriceTextView = findViewById(R.id.DetailActivity_maxsellprice);
        rc_likegood = findViewById(R.id.detailactivity_rc);
        btnbuy = findViewById(R.id.DetailActivity_btnbuy);
        line_property = findViewById(R.id.DetailActivity_line_property);
        tv_property = findViewById(R.id.tv_property);
        sliderView = findViewById(R.id.DetailActivity_imageSlider);
        line_slideshow = findViewById(R.id.DetailActivity_ln_imageSlider);
        prog = findViewById(R.id.DetailActivity_prog);
        favorite = findViewById(R.id.DetailActivity_favorite);
    }

    @Override
    public void onRestart() {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
        setupBadge();
        super.onRestart();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);

        final MenuItem menuItem = menu.findItem(R.id.basket_menu);
        View actionView = menuItem.getActionView();
        textCartItemCount = actionView.findViewById(R.id.cart_badge);
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
                        Goods = response.body().getGoods();

                        textCartItemCount.setText(NumberFunctions.PerisanNumber(Goods.get(0).getGoodFieldValue("SumFacAmount")));
                        if (Integer.parseInt(Goods.get(0).getGoodFieldValue("SumFacAmount")) > 0) {
                            if (textCartItemCount.getVisibility() != View.VISIBLE) {
                                textCartItemCount.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call<RetrofitResponse> call, @NotNull Throwable t) {
                    Log.e("retrofit_fail", t.getMessage());
                }
            });
        }
    }


}











