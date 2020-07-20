package com.kits.company.activity;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import androidx.appcompat.widget.LinearLayoutCompat;

import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.button.MaterialButton;
import com.kits.company.R;
import com.kits.company.adapter.Buy_box;
import com.kits.company.adapter.Good_view_Adapter;
import com.kits.company.adapter.InternetConnection;
import com.kits.company.adapter.SliderAdapter;
import com.kits.company.model.Column;
import com.kits.company.model.ColumnRespons;
import com.kits.company.model.Farsi_number;
import com.kits.company.model.Good;
import com.kits.company.model.GoodBuy;
import com.kits.company.model.GoodBuyRespons;
import com.kits.company.model.GoodRespons;
import com.kits.company.webService.APIClient;
import com.kits.company.webService.APIInterface;
import com.kits.company.webService.API_image;
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
    APIInterface apiInterface_image= API_image.getCleint().create(APIInterface.class);
    String ImageName,name,price;
    TextView goodcode,goodname,sellprice,details,sellpercent,goodexplain1,goodexplain2,goodexplain3;
    TextView goodexplain4,goodexplain5,goodexplain6,goodmaincode,goodsubcode,goodtype,barcode,isbn;
    TextView writer,dragoman,nasher,tahvildate,printperiod,covertype,size,pageno,grp,maxsellpriceTextView;
    TextView tv_property,tv_detail;
    byte[] imageByteArray ;
    LinearLayoutCompat line_property,line_detail,line_slideshow;
    ArrayList<Good> goods;
    Button btnbuy;
    MaterialButton favorite;
    RecyclerView rc_likegood;
    Integer id,conter =1,img_count=0,basketamount=0;
    Intent intent;
    SharedPreferences shPref ;
    DecimalFormat decimalFormat= new DecimalFormat("0,000");
    SliderView sliderView;
    ProgressBar prog;
    LinearLayoutManager horizontalLayoutManager;
    TextView textCartItemCount;
    ArrayList<GoodBuy> goodbuys;
    private boolean loading = true;
    int pastVisiblesItems=0, visibleItemCount, totalItemCount,PageNo=0,favorite_bol=0;
    SharedPreferences.Editor sEdit;

    Good_view_Adapter adapter;
    ArrayList<Column> Columns;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar =  findViewById(R.id.DetailActivity_toolbar);
        setSupportActionBar(toolbar);


        intent();

        InternetConnection ic =new  InternetConnection(getApplicationContext());
        if(ic.has()){
            init();
        } else{
            intent = new Intent(getApplicationContext(), SplashActivity.class);
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

        shPref = getSharedPreferences("profile", Context.MODE_PRIVATE);
        config();
        good_call();
        good_groups();



        btnbuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Buy_box buy_box = new Buy_box(DetailActivity.this);
                buy_box.buydialog(name,price,id);

            }
        });


        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(favorite_bol==1){

                    Call<String> call = apiInterface.Favorite_action("Favorite_action",  shPref.getString("mobile", null),id,1);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                            Log.e("onResponse", "" + response.body());
                            assert response.body() != null;
                            if (response.body().equals("1")) {
                                Log.e("onResponse2", "" + response.body());
                                favorite_bol=0;
                                favorite.setIconResource(R.drawable.ic_favorite_border_black_24dp);
                                favorite.setIconTintResource(R.color.grey_900);

                                Toast toast = Toast.makeText(DetailActivity.this, "از علاقه مندی ها حذف گردید", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 10, 10);
                                toast.show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.e("onFailure", "" + t.toString());
                        }
                    });

                }else {

                    Call<String> call = apiInterface.Favorite_action("Favorite_action", shPref.getString("mobile", null),id,0);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                            Log.e("onResponse", "" + response.body());
                            assert response.body() != null;
                            if (response.body().equals("1")) {
                                Log.e("onResponse2", "" + response.body());
                                favorite_bol=1;
                                Toast toast = Toast.makeText(DetailActivity.this, "به علاقه مندی ها اضافه شد", Toast.LENGTH_SHORT);
                                favorite.setIconResource(R.drawable.ic_favorite_black_24dp);
                                favorite.setIconTintResource(R.color.red_900);
                                toast.setGravity(Gravity.CENTER, 10, 10);
                                toast.show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.e("onFailure", "" + t.toString());
                        }
                    });

                }

            }
        });





        rc_likegood.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dx < 0) { //check for scroll down
                    visibleItemCount =   horizontalLayoutManager.getChildCount();
                    totalItemCount =   horizontalLayoutManager.getItemCount();
                    pastVisiblesItems =   horizontalLayoutManager.findFirstVisibleItemPosition();


                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount-2) {
                            loading = false;
                            Log.e("rc_...", "Last Item Wow !");
                            // Do pagination.. i.e. fetch new data
                            PageNo++;
                            good_groups_more();
                        }
                    }
                }
            }
        });



    }
    private void SliderView(){

        sliderView = findViewById(R.id.DetailActivity_imageSlider);
        SliderAdapter adapter = new SliderAdapter(this,id,img_count,goods,true);
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimations.SCALE); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(3); //set scroll delay in seconds :
        sliderView.startAutoCycle();
    }
    public void good_groups(){
        Call<GoodRespons> call31= apiInterface.GetLikeGood("goodinfo",id,PageNo);
        call31.enqueue(new Callback<GoodRespons>() {
            @Override
            public void onResponse(Call<GoodRespons> call, Response<GoodRespons> response) {
                if (response.isSuccessful()) {
                    goods = response.body().getGoods();
                    Good_view_Adapter adapter = new Good_view_Adapter( goods  , DetailActivity.this);
                    horizontalLayoutManager = new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    rc_likegood.setLayoutManager(horizontalLayoutManager);
                    rc_likegood.setAdapter(adapter);
                    rc_likegood.setItemAnimator(new DefaultItemAnimator());
                }
            }

            @Override
            public void onFailure(Call<GoodRespons> call, Throwable t) {
                    Log.e("retrofit_fail",t.getMessage());


            }
        });



    }
    public void good_groups_more(){
        Call<GoodRespons> call31= apiInterface.GetLikeGood("goodinfo",id,PageNo);
        call31.enqueue(new Callback<GoodRespons>() {
            @Override
            public void onResponse(Call<GoodRespons> call, Response<GoodRespons> response) {
                if (response.isSuccessful()) {
                    ArrayList<Good> good_page = response.body().getGoods();
                    goods.addAll(good_page);
                    adapter = new Good_view_Adapter( goods  , DetailActivity.this);
                    horizontalLayoutManager = new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    horizontalLayoutManager.scrollToPosition(pastVisiblesItems+1);
                    rc_likegood.setLayoutManager(horizontalLayoutManager);
                    rc_likegood.setAdapter(adapter);
                    rc_likegood.setItemAnimator(new DefaultItemAnimator());
                    loading = true;

                }
            }
            @Override
            public void onFailure(Call<GoodRespons> call, Throwable t) {
                    Log.e("retrofit_fail",t.getMessage());


            }
        });



    }

    public void good_call() {
        Call<ColumnRespons> call = apiInterface.GetColumn("GetColumnList",id);
        call.enqueue(new Callback<ColumnRespons>() {
            @Override
            public void onResponse(Call<ColumnRespons> call, Response<ColumnRespons> response) {
                if (response.isSuccessful()) {
                    Columns = response.body().getColumns();

                    Call<GoodRespons> call2 = apiInterface.GetAllGood
                            ("goodinfo",id,"","",0,0,shPref.getString("mobile", null),0);
                    call2.enqueue(new Callback<GoodRespons>() {
                        @Override
                        public void onResponse(Call<GoodRespons> call, Response<GoodRespons> response) {
                            if (response.isSuccessful()) {
                                goods = response.body().getGoods();
                                final Good good= goods.get(0);
                                name=good.getGoodFieldValue("GoodName");
                                price=good.getGoodFieldValue("SellPrice");
                                id=Integer.parseInt(good.getGoodFieldValue("GoodCode"));
                                goodname.setText(Farsi_number.PerisanNumber(good.getGoodFieldValue("GoodName")));
                                sellprice.setText(Farsi_number.PerisanNumber(good.getGoodFieldValue("SellPrice")));
                                if(good.getGoodFieldValue("MaxSellPrice").equals(good.getGoodFieldValue("SellPrice")))
                                {
                                    maxsellpriceTextView.setVisibility(View.GONE);
                                }else{
                                    maxsellpriceTextView.setVisibility(View.VISIBLE);
                                    SpannableString spannableString =new SpannableString( Farsi_number.PerisanNumber(decimalFormat.format(Integer.parseInt(good.getGoodFieldValue("MaxSellPrice")))));
                                    spannableString.setSpan(new StrikethroughSpan(),0,good.getGoodFieldValue("MaxSellPrice").length(), Spanned.SPAN_MARK_MARK);
                                    maxsellpriceTextView.setText(spannableString);
                                }

                                if(Integer.parseInt(good.getGoodFieldValue("HasStackAmount")) ==0){

                                    btnbuy.setText("ناموجود");
                                    btnbuy.setClickable(false);
                                    btnbuy.setTextColor(DetailActivity.this.getResources().getColor(R.color.white));
                                    btnbuy.setBackgroundColor(DetailActivity.this.getResources().getColor(R.color.red_300));
                                }
                                prog.setVisibility(View.GONE);
                                favorite_bol=Integer.parseInt(good.getGoodFieldValue("IsFavorite"))                                ;
                                if(Integer.parseInt(good.getGoodFieldValue("IsFavorite")) >0){
                                    favorite.setIconResource(R.drawable.ic_favorite_black_24dp);
                                    favorite.setIconTintResource(R.color.red_900);

                                }else {
                                    favorite.setIconResource(R.drawable.ic_favorite_border_black_24dp);
                                    favorite.setIconTintResource(R.color.grey_900);
                                }

                                SliderView();
                                for ( Column Column : Columns){

                                    if(Column.getSortOrder()>0) {
                                        LinearLayoutCompat ll = findViewById(R.id.DetailActivity_line_property);
                                        ll.setOrientation(LinearLayoutCompat.VERTICAL);
                                        LinearLayoutCompat ll_1 = new LinearLayoutCompat(DetailActivity.this);
                                        ll_1.setOrientation(LinearLayoutCompat.HORIZONTAL);
                                        ll_1.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
                                        ll_1.setWeightSum(1);

                                        TextView extra_TextView1 = new TextView(DetailActivity.this);
                                        extra_TextView1.setText(Farsi_number.PerisanNumber(Column.getColumnDesc()));
                                        extra_TextView1.setBackgroundResource(R.color.grey_20);
                                        extra_TextView1.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.MATCH_PARENT, (float) 0.7));
                                        extra_TextView1.setTextSize(18);
                                        extra_TextView1.setPadding(2, 2, 2, 2);
                                        extra_TextView1.setGravity(Gravity.CENTER);
                                        extra_TextView1.setTextColor(getResources().getColor(R.color.grey_800));
                                        ll_1.addView(extra_TextView1);

                                        TextView extra_TextView2 = new TextView(DetailActivity.this);
                                        extra_TextView2.setText(Farsi_number.PerisanNumber(good.getGoodFieldValue(Column.getColumnName())));
                                        extra_TextView2.setBackgroundResource(R.color.white);
                                        extra_TextView2.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.MATCH_PARENT, (float) 0.3));
                                        extra_TextView2.setTextSize(18);
                                        extra_TextView2.setPadding(2, 2, 2, 2);
                                        extra_TextView2.setGravity(Gravity.CENTER);
                                        extra_TextView2.setTextColor(getResources().getColor(R.color.grey_1000));
                                        ll_1.addView(extra_TextView2);

                                        ViewPager extra_ViewPager = new ViewPager(DetailActivity.this);
                                        extra_ViewPager.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, 3));
                                        extra_ViewPager.setBackgroundResource(R.color.grey_40);

                                        ll.addView(ll_1);
                                        ll.addView(extra_ViewPager);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<GoodRespons> call, Throwable t) {

                            finish();
                            Log.e("retrofit_fail",t.getMessage());
                        }
                    });


                }
            }

            @Override
            public void onFailure(Call<ColumnRespons> call, Throwable t) {

            }
        });


    }

    public void config() {
        goodname =  findViewById(R.id.DetailActivity_name);
        sellprice =  findViewById(R.id.DetailActivity_sellprice);
         maxsellpriceTextView =  findViewById(R.id.DetailActivity_maxsellprice);
         rc_likegood =  findViewById(R.id.detailactivity_rc);
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
        super.onRestart();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);

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
            intent = new Intent(DetailActivity.this, BuyActivity.class);
            sEdit = shPref.edit();
            sEdit.putString("basket_position","0");
            sEdit.apply();
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
            Call<GoodBuyRespons> call2 = apiInterface.GetbasketSum("BasketSum",shPref.getString("mobile", null));
            call2.enqueue(new Callback<GoodBuyRespons>() {
                @Override
                public void onResponse(Call<GoodBuyRespons> call, Response<GoodBuyRespons> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        goodbuys = response.body().getGoodsbuy();

                        textCartItemCount.setText(Farsi_number.PerisanNumber(goodbuys.get(0).getSumFacAmount()));
                        if(Integer.parseInt(goodbuys.get(0).getSumFacAmount())>0) {
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
    @Override
    public void onResume(){
        super.onResume();
        setupBadge();
    }


}











