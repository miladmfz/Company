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

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.kits.company.R;
import com.kits.company.adapter.Buy_box;
import com.kits.company.adapter.Good_view_Adapter;
import com.kits.company.adapter.InternetConnection;
import com.kits.company.adapter.SliderAdapter;
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
    Integer id,code=0,conter =1,img_count=0,basketamount=0;
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
                buy_box.buydialog(name,price,code);

            }
        });


        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(favorite_bol==1){
                    Call<String> call = apiInterface.Favorite_action("Favorite_action",  shPref.getString("mobile", null),code,1);
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

                    Call<String> call = apiInterface.Favorite_action("Favorite_action", shPref.getString("mobile", null),code,0);
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



        tv_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(conter==1) {
                    line_detail.setVisibility(View.VISIBLE);
                    line_property.setVisibility(View.GONE);
                    tv_property.setBackgroundResource(R.drawable.detail_tab_gray);
                    tv_detail.setBackgroundResource(R.drawable.detail_tab);
                    conter = conter - 1;
                }
            }
        });


        tv_property.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(conter==0) {
                    line_detail.setVisibility(View.GONE);
                    line_property.setVisibility(View.VISIBLE);
                    tv_property.setBackgroundResource(R.drawable.detail_tab);
                    tv_detail.setBackgroundResource(R.drawable.detail_tab_gray);
                    conter = conter + 1;
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
        SliderAdapter adapter = new SliderAdapter(this,code,img_count,goods,true);
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




    public void good_call()  {
        Call<GoodRespons> call = apiInterface.GetAllGood
                ("goodinfo","","goodcode="+id,0,0,shPref.getString("mobile", null),0);
        call.enqueue(new Callback<GoodRespons>() {
            @Override
            public void onResponse(Call<GoodRespons> call, Response<GoodRespons> response) {
                if (response.isSuccessful()) {
                    goods = response.body().getGoods();
                    Good good= goods.get(0);

                    ImageName=good.getGoodExplain6();
                    goodcode.setText(Farsi_number.PerisanNumber(good.getGoodCode()+""));
                    code=good.getGoodCode();
                    price=good.getSellPrice();
                    name=good.getGoodName();
                    basketamount=good.getBasketAmount();
                    img_count=good.getImageCount();
                    goodname.setText(Farsi_number.PerisanNumber(good.getGoodName()+""));
                    sellprice.setText(Farsi_number.PerisanNumber(good.getSellPrice()+""));
                    if(good.getMaxSellPrice().equals(Integer.parseInt(good.getSellPrice())))
                    {
                        maxsellpriceTextView.setVisibility(View.GONE);
                    }else{
                        maxsellpriceTextView.setVisibility(View.VISIBLE);
                        SpannableString spannableString =new SpannableString( Farsi_number.PerisanNumber(decimalFormat.format(Integer.parseInt(""+good.getMaxSellPrice()))));
                        spannableString.setSpan(new StrikethroughSpan(),0,good.getMaxSellPrice().toString().length(), Spanned.SPAN_MARK_MARK);
                        maxsellpriceTextView.setText(spannableString);
                    }
                    SliderView();


                    if(good.getWriter() != null){
                        writer.setText(Farsi_number.PerisanNumber(good.getWriter()+""));
                    }else {
                        writer.setText("");
                    }

                    if(good.getDragoMan() != null){
                        dragoman.setText(Farsi_number.PerisanNumber(good.getDragoMan()+""));
                    }else {
                        dragoman.setText("");
                    }

                    if(good.getNasher() != null){
                        nasher.setText(Farsi_number.PerisanNumber(good.getNasher()+""));
                    }else {
                        nasher.setText("");
                    }

                    if(good.getTahvilDate() != null){
                        tahvildate.setText(Farsi_number.PerisanNumber(good.getTahvilDate()+""));
                    }else {
                        tahvildate.setText("");
                    }

                    if(good.getPrintPeriod() != null){
                        printperiod.setText(good.getPrintPeriod());
                    }else {
                        printperiod.setText("");
                    }

                    if(good.getCoverType() != null){
                        covertype.setText(Farsi_number.PerisanNumber(good.getCoverType()+""));
                    }else {
                        covertype.setText("");
                    }

                    if(good.getSize() != null){
                        size.setText(Farsi_number.PerisanNumber(good.getSize()+""));
                    }else {
                        size.setText("");
                    }

                    if(good.getPageNo() != null){
                        pageno.setText(good.getPageNo());
                    }else {
                        pageno.setText("");
                    }

                    if(good.getGroupsWhitoutCode() != null){
                        grp.setText(Farsi_number.PerisanNumber(good.getGroupsWhitoutCode()+""));
                    }else {
                        grp.setText("");
                    }

                    if(good.getISBN() != null){
                        isbn.setText(Farsi_number.PerisanNumber(good.getISBN()+""));
                    }else {
                        isbn.setText("");
                    }


                    if(good.getDetails() != null){
                        details.setText(Farsi_number.PerisanNumber(good.getDetails()+""));
                    }else {
                        details.setText("");
                    }


                    if(good.getHasStackAmount()==0){
                        btnbuy.setText("ناموجود");
                        btnbuy.setClickable(false);
                        btnbuy.setTextColor(DetailActivity.this.getResources().getColor(R.color.white));
                        btnbuy.setBackgroundColor(DetailActivity.this.getResources().getColor(R.color.red_300));
                    }
                    prog.setVisibility(View.GONE);
                    favorite_bol=good.getIsFavorite();
                    if(good.getIsFavorite()>0){
                        favorite.setIconResource(R.drawable.ic_favorite_black_24dp);
                        favorite.setIconTintResource(R.color.red_900);

                    }else {
                        favorite.setIconResource(R.drawable.ic_favorite_border_black_24dp);
                        favorite.setIconTintResource(R.color.grey_900);
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



    public void config() {

        goodcode =  findViewById(R.id.DetailActivity_code);
        goodname =  findViewById(R.id.DetailActivity_name);
        sellprice =  findViewById(R.id.DetailActivity_sellprice);
         maxsellpriceTextView =  findViewById(R.id.DetailActivity_maxsellprice);
         writer =  findViewById(R.id.DetailActivity_writer);
         dragoman =  findViewById(R.id.DetailActivity_dragoman);
         nasher =  findViewById(R.id.DetailActivity_nasher);
         tahvildate =  findViewById(R.id.DetailActivity_tahvildate);
         printperiod =  findViewById(R.id.DetailActivity_printperiod);
         covertype =  findViewById(R.id.DetailActivity_covertype);
         size =  findViewById(R.id.DetailActivity_size);
         pageno =  findViewById(R.id.DetailActivity_pageno);
         grp =  findViewById(R.id.DetailActivity_grp);
         isbn =  findViewById(R.id.DetailActivity_isbn);
         rc_likegood =  findViewById(R.id.detailactivity_rc);
         details =  findViewById(R.id.DetailActivity_details);
         btnbuy = findViewById(R.id.DetailActivity_btnbuy);
         line_property = findViewById(R.id.DetailActivity_line_property);
         line_detail = findViewById(R.id.DetailActivity_line_details);
         tv_property = findViewById(R.id.tv_property);
         tv_detail = findViewById(R.id.tv_detail);
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











