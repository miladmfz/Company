package com.kits.company.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.kits.company.BuildConfig;
import com.kits.company.R;
import com.kits.company.adapter.BuyBox;
import com.kits.company.adapter.GetShared;
import com.kits.company.adapter.Good_view_Adapter;
import com.kits.company.adapter.Grp_Vlist_detail_Adapter;
import com.kits.company.adapter.InternetConnection;
import com.kits.company.adapter.SliderAdapter;
import com.kits.company.application.App;
import com.kits.company.application.AppDialog;
import com.kits.company.model.Column;
import com.kits.company.model.NumberFunctions;
import com.kits.company.model.Good;
import com.kits.company.model.GoodGroup;
import com.kits.company.model.RetrofitRespons;
import com.kits.company.webService.APIClient;
import com.kits.company.webService.APIInterface;
import com.kits.company.adapter.myDialog;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.Objects;

import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    APIInterface apiInterface = APIClient.getCleint().create(APIInterface.class);
    TextView textCartItemCount,profilename,grp1,grp2;
    String grp_name1,grp_name2,image_name1,image_name2;
    int grp_id1,grp_id2,image_id1,image_id2;
    ArrayList<Good> Goods;
    Intent intent;
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    public RecyclerView rc_grp;
    RecyclerView rc_allgood,rc_allgood_2;
    ArrayList<Good> goods,banner_goods;
    ArrayList<Good> available_goods1 = new ArrayList<>();
    ArrayList<Good> available_goods2 = new ArrayList<>();
    ArrayList<Good> goods1 = new ArrayList<>();
    ArrayList<Good> goods2 = new ArrayList<>();
    ArrayList<GoodGroup> Groups;
    ArrayList<GoodGroup> Groups_defult;
    ArrayList<GoodGroup> Groups_image;
    Button btn1,btn2,kowsarsamaneh,kowsar_version;
    ImageView imagebtn1,imagebtn2;
    CardView card_imagebtn1,card_imagebtn2;
    ProgressBar prog;
    public SliderView sliderView;
    private boolean doubleBackToExitPressedOnce = false;
    LinearLayoutManager horizontalLayoutManager;
    Good_view_Adapter adapter;
    AppDialog dialog=new AppDialog();

    LinearLayoutCompat line_mainpage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
        config();
        InternetConnection ic =new  InternetConnection(App.getContext());

        if(ic.has()){

            init();

            Button test = findViewById(R.id.mainactivity_test);

            if (getString(R.string.app_name).equals("company")) {
                test.setVisibility(View.VISIBLE);

            }
            test.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    test_fun();

                }
            });

        } else{
            intent = new Intent(App.getContext(), SplashActivity.class);
            startActivity(intent);
            finish();
        }

    }


//****************************************************************



    public void config() {
        toolbar =  findViewById(R.id.MainActivity_toolbar);
        drawer =  findViewById(R.id.NavActivity_drawer_layout);
        navigationView =  findViewById(R.id.NavActivity_nav);
        rc_grp =  findViewById(R.id.main_rc_grp);
        rc_allgood =  findViewById(R.id.main_rc_allgood);
        rc_allgood_2 =  findViewById(R.id.main_rc_allgood_2);
        btn1 =  findViewById(R.id.main_btn1);
        btn2 =  findViewById(R.id.main_btn2);
        imagebtn1 =  findViewById(R.id.main_image_btn1);
        imagebtn2 =  findViewById(R.id.main_image_btn2);
        card_imagebtn1 =  findViewById(R.id.main_card_image_btn1);
        card_imagebtn2 =  findViewById(R.id.main_card_image_btn2);
        kowsarsamaneh = findViewById(R.id.kits_version);
        profilename = findViewById(R.id.nav_profile_name);
        prog = findViewById(R.id.main_prog);
        grp1 = findViewById(R.id.main_grp1);
        grp2 = findViewById(R.id.main_grp2);
        line_mainpage = findViewById(R.id.ll_mainpage);

        sliderView = findViewById(R.id.imageSlider);


    }

    public void test_fun() {


    }

    @SuppressLint("SetTextI18n")
    public void init() {

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        kowsarsamaneh.setText(NumberFunctions.PerisanNumber(  "نسخه نرم افزار     "+ BuildConfig.VERSION_NAME+"\n"+"تمامی حقوق این نرم افزار متعلق به گروه نرم افزاری کوثر می باشد شماره تماس3–66569320"));

        profilename.setText(GetShared.ReadString("fname")+"  "+GetShared.ReadString("lname"));



        allgrp();
        kowsar_good();

        kowsar_image_good();

        SliderView();

        noti();



        btn1.setOnClickListener(view -> {
            intent = new Intent(App.getContext(), GrpActivity.class);
            intent.putExtra("id", grp_id1);
            intent.putExtra("title",""+grp_name1);
            startActivity(intent);

        });


        btn2.setOnClickListener(view -> {
            intent = new Intent(App.getContext(), GrpActivity.class);
            intent.putExtra("id", grp_id2);
            intent.putExtra("title",""+grp_name2);
            startActivity(intent);
        });

        imagebtn1.setOnClickListener(view -> {
            intent = new Intent(App.getContext(), GrpActivity.class);
            intent.putExtra("id", image_id1);
            intent.putExtra("title",""+image_name1);
            startActivity(intent);
        });

        imagebtn2.setOnClickListener(view -> {
            intent = new Intent(App.getContext(), GrpActivity.class);
            intent.putExtra("id", image_id2);
            intent.putExtra("title",""+image_name2);
            startActivity(intent);
        });



    }



    private void allgrp() {

        Call<RetrofitRespons> call1 = apiInterface.Getgrp(
                "GoodGroupInfo",
                "0"
        );
        call1.enqueue(new Callback<RetrofitRespons>() {
            @Override
            public void onResponse(Call<RetrofitRespons> call, Response<RetrofitRespons> response) {
                if (response.isSuccessful()) {
                    Groups = response.body().getGroups();
                    if(Groups.size()>0) {
                        Grp_Vlist_detail_Adapter adapter = new Grp_Vlist_detail_Adapter(Groups, App.getContext());
                        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(App.getContext(), LinearLayoutManager.HORIZONTAL, false);
                        rc_grp.setLayoutManager(horizontalLayoutManager);
                        rc_grp.setAdapter(adapter);
                        rc_grp.setItemAnimator(new DefaultItemAnimator());
                    }else{
                        LinearLayout linearLayoutq=findViewById(R.id.grp_main_linearlayout);
                        linearLayoutq.setVisibility(View.GONE);
                    }
                }
            }
            @Override
            public void onFailure(Call<RetrofitRespons> call, Throwable t) {
                LinearLayout linearLayoutq=findViewById(R.id.grp_main_linearlayout);
                linearLayoutq.setVisibility(View.GONE);
                Log.e("call1","1");
                Log.e("call1",t.getMessage());

            }
        });
    }

    private void kowsar_image_good() {
        Call<RetrofitRespons> call2 = apiInterface.Getkowsar_grp("GoodGroupInfo_DefaultImage");
        call2.enqueue(new Callback<RetrofitRespons>() {
            @Override
            public void onResponse(Call<RetrofitRespons> call, Response<RetrofitRespons> response) {
                if (response.isSuccessful()) {
                    Groups_image = response.body().getGroups();

                    if(Integer.parseInt(Groups_image.get(0).getGoodGroupFieldValue("ErrCode"))>0)
                    {
                        Log.e("Groups_image",Groups_image.get(0).getGoodGroupFieldValue("ErrDesc"));
                    }else {
                        image_name1=Groups_image.get(0).getGoodGroupFieldValue("Name");
                        image_id1=Integer.parseInt(Groups_image.get(0).getGoodGroupFieldValue("groupcode"));
                        Glide.with(imagebtn1)
                                .load("http://" + getString(R.string.SERVERIP) + "/login/slide_img/imageview1.jpg")
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .into(imagebtn1);
                        card_imagebtn1.setVisibility(View.VISIBLE);

                        if(Groups_image.size()>1) {
                            image_name2 = Groups_image.get(1).getGoodGroupFieldValue("Name");
                            image_id2 = Integer.parseInt(Groups_image.get(1).getGoodGroupFieldValue("groupcode"));
                            card_imagebtn2.setVisibility(View.VISIBLE);
                            Glide.with(imagebtn2)
                                    .load("http://" + getString(R.string.SERVERIP) + "/login/slide_img/imageview2.jpg")
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .into(imagebtn2);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<RetrofitRespons> call, Throwable t) {
                Log.e("call2","2");

            }
        });

    }


    private void kowsar_good() {


        Call<RetrofitRespons> call3 = apiInterface.Getkowsar_grp("GoodGroupInfo_Default");

        call3.enqueue(new Callback<RetrofitRespons>() {
            @Override
            public void onResponse(Call<RetrofitRespons> call, Response<RetrofitRespons> response) {
                if (response.isSuccessful()) {
                    Groups_defult = response.body().getGroups();
                    if(Integer.parseInt(Groups_defult.get(0).getGoodGroupFieldValue("ErrCode"))>0)
                    {

                        Log.e("",Groups_defult.get(0).getGoodGroupFieldValue("ErrDesc"));
                    }
                    else
                    {

                        grp_name1=Groups_defult.get(0).getGoodGroupFieldValue("Name");
                        grp_name2=Groups_defult.get(1).getGoodGroupFieldValue("Name");
                        grp_id1=Integer.parseInt(Groups_defult.get(0).getGoodGroupFieldValue("groupcode"));
                        grp_id2=Integer.parseInt(Groups_defult.get(1).getGoodGroupFieldValue("groupcode"));

                        grp1.setText(Groups_defult.get(0).getGoodGroupFieldValue("Name"));
                        Call<RetrofitRespons> call4 = apiInterface.GetAllGood(
                                "goodinfo",
                                "0",
                                "",
                                "",
                                Groups_defult.get(0).getGoodGroupFieldValue("groupcode"),
                                "0",
                                GetShared.ReadString("mobile"),
                                "0");
                        call4.enqueue(new Callback<RetrofitRespons>() {
                            @Override
                            public void onResponse(Call<RetrofitRespons> call, Response<RetrofitRespons> response) {
                                if (response.isSuccessful()) {
                                    goods1 = response.body().getGoods();
                                    available_goods1.clear();
                                    for (Good g : goods1) {
                                        if (Integer.parseInt(g.getGoodFieldValue("HasStackAmount"))>0) {
                                           available_goods1.add(g);
                                        }
                                    }
                                    adapter = new Good_view_Adapter( available_goods1, MainActivity.this);
                                    horizontalLayoutManager = new LinearLayoutManager(App.getContext(), LinearLayoutManager.HORIZONTAL, false);
                                    rc_allgood.setLayoutManager(horizontalLayoutManager);
                                    rc_allgood.setAdapter(adapter);
                                    rc_allgood.setItemAnimator(new FadeInUpAnimator());
                                    prog.setVisibility(View.GONE);
                                }
                            }
                            @Override
                            public void onFailure(Call<RetrofitRespons> call, Throwable t) {
                                Log.e("call4","3");

                            }
                        });

                        grp2.setText(Groups_defult.get(1).getGoodGroupFieldValue("Name"));

                        Call<RetrofitRespons> call5 = apiInterface.GetAllGood(
                                "goodinfo",
                                "0",
                                "",
                                "",
                                Groups_defult.get(1).getGoodGroupFieldValue("groupcode"),
                                "0",
                                GetShared.ReadString("mobile"),
                                "0"
                        );
                        call5.enqueue(new Callback<RetrofitRespons>() {
                            @Override
                            public void onResponse(Call<RetrofitRespons> call, Response<RetrofitRespons> response) {
                                if (response.isSuccessful()) {
                                    goods2 = response.body().getGoods();
                                    available_goods2.clear();

                                    for (Good g : goods2) {
                                        if (Integer.parseInt(g.getGoodFieldValue("HasStackAmount"))>0) {
                                            available_goods2.add(g);
                                        }
                                    }
                                    adapter = new Good_view_Adapter( available_goods2, MainActivity.this);
                                    LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(App.getContext(), LinearLayoutManager.HORIZONTAL, false);
                                    rc_allgood_2.setLayoutManager(horizontalLayoutManager);
                                    rc_allgood_2.setAdapter(adapter);
                                    rc_allgood_2.setItemAnimator(new FadeInUpAnimator());
                                }
                            }
                            @Override
                            public void onFailure(Call<RetrofitRespons> call, Throwable t) {
                                Log.e("call5","4");

                            }
                        });
                    }
                    kowsar_good_dynamic();
                }
            }
            @Override
            public void onFailure(Call<RetrofitRespons> call, Throwable t) {

            }
        });

    }
    @SuppressLint("ResourceType")
    private void kowsar_good_dynamic() {


        if(Groups_defult.size()>2) {
            Groups_defult.remove(0);
            Groups_defult.remove(0);
            for (GoodGroup groups:Groups_defult){

                LinearLayoutCompat line_new = new LinearLayoutCompat(MainActivity.this);
                line_new.setOrientation(LinearLayoutCompat.HORIZONTAL);
                line_new.setLayoutParams(new LinearLayoutCompat.LayoutParams(
                        LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                        LinearLayoutCompat.LayoutParams.WRAP_CONTENT)
                );
                line_new.setWeightSum(1);



                TextView extra_TextView1 = new TextView(MainActivity.this);
                extra_TextView1.setText(NumberFunctions.PerisanNumber(groups.getGoodGroupFieldValue("name")));
                extra_TextView1.setLayoutParams(new LinearLayoutCompat.LayoutParams(
                        LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                        LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                        (float) 0.3)
                );
                extra_TextView1.setTextSize(20);
                extra_TextView1.setPadding(5, 5, 20, 5);
                extra_TextView1.setGravity(Gravity.RIGHT);
                extra_TextView1.setTextColor(getResources().getColor(R.color.red_900));


                MaterialButton extra_materialbutton = new MaterialButton(MainActivity.this);
                extra_materialbutton.setText("بیشتر");
                extra_materialbutton.setLayoutParams(new LinearLayoutCompat.LayoutParams(
                        LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                        LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                        (float) 0.7)
                );
                extra_materialbutton.setIcon(getResources().getDrawable(R.drawable.ic_expand_more_black_24dp));
                extra_materialbutton.setTextColor(getResources().getColor(R.color.blue_500));
                extra_materialbutton.setTextSize(18);
                extra_materialbutton.setPadding(2, 2, 2, 2);
                extra_materialbutton.setIconGravity(MaterialButton.ICON_GRAVITY_TEXT_START);
                extra_materialbutton.setBackgroundColor(getResources().getColor(R.color.white));
                extra_materialbutton.setIconTint(ColorStateList.valueOf(getResources().getColor(R.color.blue_500)));
                extra_materialbutton.setOnClickListener(v -> {
                    intent = new Intent(getApplicationContext(), GrpActivity.class);
                    intent.putExtra("id", groups.getGoodGroupFieldValue("groupcode"));
                    intent.putExtra("title",groups.getGoodGroupFieldValue("name"));
                    startActivity(intent);

                });

                RecyclerView extra_recyclerview = new RecyclerView(MainActivity.this);
                extra_recyclerview.setLayoutParams(new LinearLayoutCompat.LayoutParams(
                        LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                        LinearLayoutCompat.LayoutParams.MATCH_PARENT
                ));

                Call<RetrofitRespons> call = apiInterface.GetAllGood
                        ("goodinfo","0","","",groups.getGoodGroupFieldValue("groupcode"),"0",GetShared.ReadString("mobile"),"0" );
                call.enqueue(new Callback<RetrofitRespons>() {
                    @Override
                    public void onResponse(Call<RetrofitRespons> call, Response<RetrofitRespons> response) {
                        if (response.isSuccessful()) {
                            ArrayList<Good> available_goods = new ArrayList<>();


                            for (Good g : response.body().getGoods()) {
                                if (Integer.parseInt(g.getGoodFieldValue("HasStackAmount"))>0) {
                                    available_goods.add(g);
                                }
                            }
                            adapter = new Good_view_Adapter( available_goods, MainActivity.this);
                            LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
                            extra_recyclerview.setLayoutManager(horizontalLayoutManager);
                            extra_recyclerview.setAdapter(adapter);
                            extra_recyclerview.setItemAnimator(new FadeInUpAnimator());
                        }
                    }
                    @Override
                    public void onFailure(Call<RetrofitRespons> call, Throwable t) {
                        //Log.e("retrofit_fail",t.getMessage());
                    }
                });

                line_new.addView(extra_TextView1);
                line_new.addView(extra_materialbutton);


                line_mainpage.addView(line_new);
                line_mainpage.addView(extra_recyclerview);
            }
        }

    }

    private void SliderView(){


        Call<RetrofitRespons> call6 = apiInterface.Banner_get("Banner");
        call6.enqueue(new Callback<RetrofitRespons>() {
            @Override
            public void onResponse(Call<RetrofitRespons> call, Response<RetrofitRespons> response) {
                if (response.isSuccessful()) {
                    banner_goods = response.body().getGoods();

                    if(banner_goods.size()>0) {
                        sliderView.setVisibility(View.VISIBLE);
                        sliderView.getLayoutParams().height= Integer.parseInt(String.valueOf(sliderView.getMeasuredWidthAndState()/3));
                        SliderAdapter adapter = new SliderAdapter(App.getContext(),0,banner_goods.size(),banner_goods,false);
                        sliderView.setSliderAdapter(adapter);
                        sliderView.setIndicatorAnimation(IndicatorAnimations.SCALE); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                        sliderView.setIndicatorSelectedColor(Color.WHITE);
                        sliderView.setIndicatorUnselectedColor(Color.GRAY);
                        sliderView.setScrollTimeInSec(5); //set scroll delay in seconds :
                        sliderView.startAutoCycle();

                    }else{
                        sliderView.setVisibility(View.GONE);
                    }

                }
            }
            @Override
            public void onFailure(Call<RetrofitRespons> call, Throwable t) {
                Log.e("call6","6");

                Log.e("retrofit_fail",t.getMessage());
            }
        });




    }
    private void noti(){
        Call<RetrofitRespons> call7 = apiInterface.VersionInfo("VersionInfo");
        call7.enqueue(new Callback<RetrofitRespons>() {
            @Override
            public void onResponse(Call<RetrofitRespons> call, Response<RetrofitRespons> response) {
                if (response.isSuccessful()) {

                    assert response.body() != null;
                    if(!response.body().getText().equals(BuildConfig.VERSION_NAME)){
                        dialog.show(getSupportFragmentManager(),"Version");
                    }
                }
            }
            @Override
            public void onFailure(Call<RetrofitRespons> call, Throwable t) {

                Log.e("retrofit_fail",t.getMessage());
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.NavActivity_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        App.showToast("برای خروج مجددا کلیک کنید");
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


        @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_Search) {
            intent = new Intent(App.getContext(), SearchActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_news) {
            intent = new Intent(App.getContext(), Search_date_detailActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_Favorite) {
            Intent intent = new Intent(App.getContext(), FavoriteActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_buybag) {
            Intent intent = new Intent(App.getContext(), BuyActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_factors) {
            Intent intent = new Intent(App.getContext(), BuyhistoryActivity.class);
            startActivity(intent);
        } else if (id == R.id.calltous) {
            Intent intent = new Intent(App.getContext(), CallusActivity.class);
            startActivity(intent);
        }else if (id == R.id.aboutus) {
            Intent intent = new Intent(App.getContext(), AboutusActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_reg) {
            Intent intent = new Intent(App.getContext(), RegisterActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_allview) {
            Intent intent = new Intent(App.getContext(), AllviewActivity.class);
            startActivity(intent);

        }
        DrawerLayout drawer =  findViewById(R.id.NavActivity_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);

        final MenuItem menuItem = menu.findItem(R.id.basket_menu);
        View actionView = menuItem.getActionView();
        textCartItemCount =  actionView.findViewById(R.id.cart_badge);
        setupBadge();
        MenuItem menuItem2 = menu.findItem(R.id.search_menu);
        menuItem2.setVisible(true);
        actionView.setOnClickListener(v -> onOptionsItemSelected(menuItem));

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.basket_menu) {
            intent = new Intent(App.getContext(), BuyActivity.class);
            GetShared.EditString("basket_position", "0");
            startActivity(intent);
            return true;
        }if(item.getItemId() == R.id.search_menu){
            intent = new Intent(App.getContext(), SearchActivity.class);
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
            Call<RetrofitRespons> call8 = apiInterface.GetbasketSum(
                    "BasketSum",
                    GetShared.ReadString("mobile")
            );
            call8.enqueue(new Callback<RetrofitRespons>() {
                @Override
                public void onResponse(Call<RetrofitRespons> call, Response<RetrofitRespons> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        Goods = response.body().getGoods();

                        textCartItemCount.setText(NumberFunctions.PerisanNumber(Goods.get(0).getGoodFieldValue("SumFacAmount")));
                        if(Integer.parseInt(Goods.get(0).getGoodFieldValue("SumFacAmount"))>0) {
                            if (textCartItemCount.getVisibility() != View.VISIBLE) {
                                textCartItemCount.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
                @Override
                public void onFailure(Call<RetrofitRespons> call, Throwable t) {
                    Log.e("call8","7");

                    Log.e("retrofit_fail",t.getMessage());
                }
            });
        }
    }

}
