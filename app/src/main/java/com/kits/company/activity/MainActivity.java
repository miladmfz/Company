package com.kits.company.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;


import com.google.firebase.messaging.FirebaseMessaging;
import com.kits.company.R;
import com.kits.company.adapter.Good_view_Adapter;
import com.kits.company.adapter.Grp_Vlist_detail_Adapter;
import com.kits.company.adapter.InternetConnection;
import com.kits.company.adapter.SliderAdapter;
import com.kits.company.model.Farsi_number;
import com.kits.company.model.Good;
import com.kits.company.model.GoodBuy;
import com.kits.company.model.GoodBuyRespons;
import com.kits.company.model.GoodGroup;
import com.kits.company.model.GoodGroupRespons;
import com.kits.company.model.GoodRespons;
import com.kits.company.webService.APIClient;
import com.kits.company.webService.APIInterface;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.zarinpal.ewallets.purchase.PaymentRequest;
import com.zarinpal.ewallets.purchase.ZarinPal;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener  {

    APIInterface apiInterface = APIClient.getCleint().create(APIInterface.class);
    SharedPreferences shPref ;
    TextView textCartItemCount,profilename,grp1,grp2;
    String grp_name1,grp_name2,image_name1,image_name2;
    int grp_id1,grp_id2,image_id1,image_id2;
    ArrayList<GoodBuy> goodbuys;
    Intent intent;
    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    RecyclerView rc_grp,rc_allgood,rc_allgood_2;
    ArrayList<Good> goods,banner_goods;
    ArrayList<GoodGroup> Groups,Groups_defult,Groups_image;
    Button btn1,btn2,kowsarsamaneh;
    ImageView imagebtn1,imagebtn2;
    CardView card_imagebtn1,card_imagebtn2;
    ProgressBar prog;
    public SliderView sliderView;
    private boolean doubleBackToExitPressedOnce = false;
    SharedPreferences.Editor sEdit;
    LinearLayoutManager horizontalLayoutManager;
    private boolean loading = true;
    Good_view_Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);



       config();

        InternetConnection ic =new  InternetConnection(getApplicationContext());

        if(ic.has()){

            init();
        } else{
            intent = new Intent(getApplicationContext(), SplashActivity.class);
            startActivity(intent);
            finish();
        }

    }


//****************************************************************



    public void config() {
        shPref = getSharedPreferences("profile", Context.MODE_PRIVATE);
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
        kowsarsamaneh = findViewById(R.id.kits);
        profilename = findViewById(R.id.nav_profile_name);
        prog = findViewById(R.id.main_prog);
        grp1 = findViewById(R.id.main_grp1);
        grp2 = findViewById(R.id.main_grp2);
        sliderView = findViewById(R.id.imageSlider);


    }

    public void test_fun() {

//        ZarinPal purchase = ZarinPal.getPurchase(this);
//        PaymentRequest peyment = ZarinPal.getPaymentRequest();
//
//        peyment.setMerchantID("");
//        peyment.setAmount(100);
//        peyment.setDescription("test_pardasht");
//        peyment.getCallBackURL();



    }

    public void init() {


        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        kowsarsamaneh.setText(Farsi_number.PerisanNumber("تمامی حقوق این نرم افزار متعلق به گروه نرم افزاری کوثر می باشد شماره تماس3–66569320"));

        profilename.setText(shPref.getString("fname", null)+"  "+shPref.getString("lname", null));

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

        allgrp();
        kowsar_good();
        kowsar_image_good();

        SliderView();
        noti();



        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), GrpActivity.class);
                intent.putExtra("id", grp_id1);
                intent.putExtra("title",""+grp_name1);
                startActivity(intent);

            }
        });


        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), GrpActivity.class);
                intent.putExtra("id", grp_id2);
                intent.putExtra("title",""+grp_name2);
                startActivity(intent);
            }
        });

        imagebtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), GrpActivity.class);
                intent.putExtra("id", image_id1);
                intent.putExtra("title",""+image_name1);
                startActivity(intent);
            }
        });

        imagebtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), GrpActivity.class);
                intent.putExtra("id", image_id2);
                intent.putExtra("title",""+image_name2);
                startActivity(intent);
            }
        });



    }



    private void allgrp() {

        Call<GoodGroupRespons> call = apiInterface.Getgrp("GoodGroupInfo",1);
        call.enqueue(new Callback<GoodGroupRespons>() {
            @Override
            public void onResponse(Call<GoodGroupRespons> call, Response<GoodGroupRespons> response) {
                if (response.isSuccessful()) {
                    Groups = response.body().getGroups();
                    if(Groups.size()>0) {
                        Grp_Vlist_detail_Adapter adapter = new Grp_Vlist_detail_Adapter(Groups, MainActivity.this);
                        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
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
            public void onFailure(Call<GoodGroupRespons> call, Throwable t) {
                LinearLayout linearLayoutq=findViewById(R.id.grp_main_linearlayout);
                linearLayoutq.setVisibility(View.GONE);
                }
        });
    }

    private void kowsar_image_good() {
        Call<GoodGroupRespons> call = apiInterface.Getkowsar_grp("GoodGroupInfo_DefaultImage");


        call.enqueue(new Callback<GoodGroupRespons>() {
            @Override
            public void onResponse(Call<GoodGroupRespons> call, Response<GoodGroupRespons> response) {
                if (response.isSuccessful()) {
                    Groups_image = response.body().getGroups();

                    if(Groups_image.get(0).getErrCode()>0)
                    {
                        Log.e("",""+Groups_image.get(0).getErrDesc());
                    }else {
                        image_name1=Groups_image.get(0).getName();
                        image_id1=Groups_image.get(0).getGroupCode();
                        Glide.with(imagebtn1)
                                .load("http://" + getString(R.string.SERVERIP) + "/login/slide_img/imageview1.jpg")
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .into(imagebtn1);
                        card_imagebtn1.setVisibility(View.VISIBLE);

                        if(Groups_image.size()>1) {
                            image_name2 = Groups_image.get(1).getName();
                            image_id2 = Groups_image.get(1).getGroupCode();
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
            public void onFailure(Call<GoodGroupRespons> call, Throwable t) {

            }
        });

    }


    private void kowsar_good() {


        Call<GoodGroupRespons> call = apiInterface.Getkowsar_grp("GoodGroupInfo_Default");

        call.enqueue(new Callback<GoodGroupRespons>() {
            @Override
            public void onResponse(Call<GoodGroupRespons> call, Response<GoodGroupRespons> response) {
                if (response.isSuccessful()) {
                    Groups_defult = response.body().getGroups();
                    if(Groups_defult.get(0).getErrCode()>0)
                    {
                        Log.e("",""+Groups_defult.get(0).getErrDesc());
                    }else {
                        grp_name1=Groups_defult.get(0).getName();
                        grp_name2=Groups_defult.get(1).getName();
                        grp_id1=Groups_defult.get(0).getGroupCode();
                        grp_id2=Groups_defult.get(1).getGroupCode();

                        grp1.setText(Groups_defult.get(0).getName());
                        Call<GoodRespons> call2 = apiInterface.GetAllGood
                                ("goodinfo","","",Groups_defult.get(0).getGroupCode(),0,shPref.getString("mobile", null),0);
                        call2.enqueue(new Callback<GoodRespons>() {
                            @Override
                            public void onResponse(Call<GoodRespons> call, Response<GoodRespons> response) {
                                if (response.isSuccessful()) {
                                    goods = response.body().getGoods();
                                    adapter = new Good_view_Adapter( goods, MainActivity.this);
                                    horizontalLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
                                    rc_allgood.setLayoutManager(horizontalLayoutManager);
                                    rc_allgood.setAdapter(adapter);
                                    rc_allgood.setItemAnimator(new FadeInUpAnimator());
                                    prog.setVisibility(View.GONE);
                                }
                            }
                            @Override
                            public void onFailure(Call<GoodRespons> call, Throwable t) {
                               // Log.e("retrofit_fail",t.getMessage());


                            }
                        });



                        grp2.setText(Groups_defult.get(1).getName());

                        Call<GoodRespons> call3 = apiInterface.GetAllGood
                                ("goodinfo","","",Groups_defult.get(1).getGroupCode(),0,shPref.getString("mobile", null),0 );
                        call3.enqueue(new Callback<GoodRespons>() {
                            @Override
                            public void onResponse(Call<GoodRespons> call, Response<GoodRespons> response) {
                                if (response.isSuccessful()) {
                                    goods = response.body().getGoods();
                                    adapter = new Good_view_Adapter( goods, MainActivity.this);
                                    LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
                                    rc_allgood_2.setLayoutManager(horizontalLayoutManager);
                                    rc_allgood_2.setAdapter(adapter);
                                    rc_allgood_2.setItemAnimator(new FadeInUpAnimator());
                                }
                            }
                            @Override
                            public void onFailure(Call<GoodRespons> call, Throwable t) {
                                //Log.e("retrofit_fail",t.getMessage());
                            }
                        });


                    }






                }
            }

            @Override
            public void onFailure(Call<GoodGroupRespons> call, Throwable t) {

            }
        });

    }

    private void SliderView(){


        Call<GoodRespons> call3 = apiInterface.Banner_get("Banner");
        call3.enqueue(new Callback<GoodRespons>() {
            @Override
            public void onResponse(Call<GoodRespons> call, Response<GoodRespons> response) {
                if (response.isSuccessful()) {
                    banner_goods = response.body().getGoods();

                    if(banner_goods.size()>0)
                    {
                        sliderView.setVisibility(View.VISIBLE);
                        for (final Good Goodss : banner_goods) {
                            Log.e("company_gn",Goodss.getGoodName());
                            Log.e("company_gurl",Goodss.getGoodImageUrl());
                            SliderAdapter adapter = new SliderAdapter(MainActivity.this,0,banner_goods.size(),banner_goods,false);
                            sliderView.setSliderAdapter(adapter);
                            sliderView.setIndicatorAnimation(IndicatorAnimations.SCALE); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                            sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                            sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                            sliderView.setIndicatorSelectedColor(Color.WHITE);
                            sliderView.setIndicatorUnselectedColor(Color.GRAY);
                            sliderView.setScrollTimeInSec(5); //set scroll delay in seconds :
                            sliderView.startAutoCycle();
                        }
                    }else{
                        sliderView.setVisibility(View.GONE);
                    }

                }
            }
            @Override
            public void onFailure(Call<GoodRespons> call, Throwable t) {
                Log.e("retrofit_fail",t.getMessage());
            }
        });




    }
    private void noti(){

        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("Kowsarmobile","Kowsarmobile", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }


        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Successfull";
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                        }
                        Log.e("FbaseMessag_general",msg);
                    }
                });

        FirebaseMessaging.getInstance().subscribeToTopic("broker")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Successfull";
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                        }
                        Log.e("FbaseMessag_broker",msg);
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
        Toast.makeText(this, "برای خروج مجددا کلیک کنید", Toast.LENGTH_SHORT).show();

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
            intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_news) {
            intent = new Intent(MainActivity.this, Search_date_detailActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_Favorite) {
            Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_buybag) {
            Intent intent = new Intent(MainActivity.this, BuyActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_factors) {
            Intent intent = new Intent(MainActivity.this, BuyhistoryActivity.class);
            startActivity(intent);
        } else if (id == R.id.calltous) {
            Intent intent = new Intent(MainActivity.this, CallusActivity.class);
            startActivity(intent);
        }else if (id == R.id.aboutus) {
            Intent intent = new Intent(MainActivity.this, AboutusActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_reg) {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        }else if (id == R.id.nav_allview) {
            Intent intent = new Intent(MainActivity.this, AllviewActivity.class);
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
            intent = new Intent(MainActivity.this, BuyActivity.class);
            sEdit = shPref.edit();
            sEdit.putString("basket_position","0");
            sEdit.apply();
            startActivity(intent);
            return true;
        }if(item.getItemId() == R.id.search_menu){
            intent = new Intent(MainActivity.this, SearchActivity.class);
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
