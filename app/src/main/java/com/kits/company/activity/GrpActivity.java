package com.kits.company.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.card.MaterialCardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kits.company.R;
import com.kits.company.adapter.Good_ProSearch_Adapter;
import com.kits.company.adapter.Good_ProSearch_Line_Adapter;
import com.kits.company.adapter.Grp_Vlist_detail_Adapter;
import com.kits.company.adapter.InternetConnection;
import com.kits.company.model.Farsi_number;
import com.kits.company.model.Good;
import com.kits.company.model.GoodBuy;
import com.kits.company.model.GoodBuyRespons;
import com.kits.company.model.GoodGroup;
import com.kits.company.model.GoodGroupRespons;
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


public class GrpActivity extends AppCompatActivity {


    Integer id=0,conter=0;
    Intent intent;
    SharedPreferences shPref ;
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
    ArrayList<GoodBuy> goodbuys,goodbuys_setupBadge;
    GridLayoutManager gridLayoutManager;
    String srch="",sq="";
    private boolean loading = true;
    int pastVisiblesItems=0, visibleItemCount, totalItemCount,PageNo=0;
    SharedPreferences.Editor sEdit;
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

        toolbar =  findViewById(R.id.GrpActivity_toolbar);

        change_search = findViewById(R.id.GrpActivity_change_search);
        filter_active = findViewById(R.id.GrpActivity_filter_active);
        line_pro = findViewById(R.id.GrpActivity_search_line_p);
        line = findViewById(R.id.GrpActivity_search_line);
        edtsearch = findViewById(R.id.GrpActivity_edtsearch);
        prog = findViewById(R.id.GrpActivity_prog);


        rc_grp = findViewById(R.id.GrpActivity_grp);
        rc_good = findViewById(R.id.GrpActivity_good);
        fab = findViewById(R.id.GrpActivity_fab);




        toolbar.setTitle(title);
        setSupportActionBar(toolbar);


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
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        srch = arabicToenglish(editable.toString());
                        srch= srch.replaceAll(" ","%");
                        PageNo=0;

                        allgood(srch,sq);
                    }
                },500);


            }
        });

        allgood(srch,sq);

        change_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(conter==0) {
                    line_pro.setVisibility(View.VISIBLE);
                    filter_active.setVisibility(View.VISIBLE);
                    line.setVisibility(View.GONE);
                    change_search.setText("جستجوی عادی");
                    conter = conter + 1;
                    Log.e("conter", "" + conter);
                }else{
                    line_pro.setVisibility(View.GONE);
                    filter_active.setVisibility(View.GONE);
                    line.setVisibility(View.VISIBLE);
                    change_search.setText("جستجوی پیشرفته");
                    conter= conter-1;
                    Log.e("conter",""+conter);
                }
            }
        });

        filter_active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText goodname = findViewById(R.id.GrpActivity_search_pro_good);
                EditText dragoman = findViewById(R.id.GrpActivity_search_pro_dragoman);
                EditText nasher = findViewById(R.id.GrpActivity_search_pro_nasher);
                EditText period = findViewById(R.id.GrpActivity_search_pro_period);
                EditText writer = findViewById(R.id.GrpActivity_search_pro_writer);
                EditText PrintYear = findViewById(R.id.GrpActivity_search_pro_PrintYear);
                int aperiod;
                String agoodname = arabicToenglish(goodname.getText().toString());
                srch=agoodname;
                srch= srch.replaceAll(" ","%");

                String adragoman = arabicToenglish(dragoman.getText().toString());
                String anasher =arabicToenglish (nasher.getText().toString());
                String periodd = arabicToenglish(period.getText().toString());
                String awriter = arabicToenglish(writer.getText().toString());
                String aprintyear = arabicToenglish(PrintYear.getText().toString());

                if(!periodd.equals("")) {
                    aperiod = Integer.parseInt(periodd);
                    sq = "PrintPeriod = "+aperiod +" ";}
                else { sq = "PrintPeriod >1 ";}

                if(!anasher.equals("")) { sq = sq + "And nasher Like N''%"+anasher+"%'' ";}
                if(!adragoman.equals("")) { sq = sq + "And DragoMan Like N''%"+adragoman+"%'' ";}
                if(!awriter.equals("")) { sq = sq + "And Writer Like N''%"+awriter+"%'' ";}
                if(!aprintyear.equals("")) { sq = sq + "And PrintYear Like N''%"+aprintyear+"%'' ";}
                if(!srch.equals("")) { sq = sq + "And GoodName Like N''%"+srch+"%'' ";}

                PageNo=0;
                allgood(srch,sq);
                Toast.makeText(GrpActivity.this, "انجام شد ", Toast.LENGTH_SHORT).show();
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
                                                    if (goodbuys.get(0).getErrCode() > 0){
                                                        Toast.makeText(GrpActivity.this, goodbuys.get(0).getGoodBuyFieldValue("ErrDesc")+"برای"+s[2], Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(GrpActivity.this, "تعداد مورد نظر صحیح نمی باشد.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(GrpActivity.this, "تعداد مورد نظر صحیح نمی باشد.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }


    private void allgrp() {


        Call<GoodGroupRespons> call = apiInterface.Getgrp("GoodGroupInfo", id);
        call.enqueue(new Callback<GoodGroupRespons>() {
            @Override
            public void onResponse(Call<GoodGroupRespons> call, Response<GoodGroupRespons> response) {
                if (response.isSuccessful()) {
                    Groups = response.body().getGroups();
                    Grp_Vlist_detail_Adapter adapter = new Grp_Vlist_detail_Adapter(Groups, GrpActivity.this);
                    LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(GrpActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    rc_grp.setLayoutManager(horizontalLayoutManager);
                    rc_grp.setAdapter(adapter);
                    rc_grp.setItemAnimator(new DefaultItemAnimator());
                    prog.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<GoodGroupRespons> call, Throwable t) {
                rc_grp.setVisibility(View.GONE);
            }
        });
    }

    private void allgood(String edtsearch,String where) {
        prog.setVisibility(View.VISIBLE);
        Call<GoodRespons> call = apiInterface.GetAllGood
                ("goodinfo",0, edtsearch, where,id,PageNo,shPref.getString("mobile", null),0);
        call.enqueue(new Callback<GoodRespons>() {
            @Override
            public void onResponse(Call<GoodRespons> call, Response<GoodRespons> response) {
                if (response.isSuccessful()) {
                    goods = response.body().getGoods();
                    if(Objects.equals(shPref.getString("view", null), "grid")){
                        adapter = new Good_ProSearch_Adapter(goods, GrpActivity.this);
                        adapter_line = new Good_ProSearch_Line_Adapter(goods, GrpActivity.this);
                        gridLayoutManager = new GridLayoutManager(GrpActivity.this,2);
                        rc_good.setLayoutManager(gridLayoutManager);
                        rc_good.setAdapter(adapter);
                        rc_good.setItemAnimator(new FlipInTopXAnimator());
                    }else{
                        adapter = new Good_ProSearch_Adapter(goods, GrpActivity.this);
                        adapter_line = new Good_ProSearch_Line_Adapter(goods, GrpActivity.this);
                        gridLayoutManager = new GridLayoutManager(GrpActivity.this,1);
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
            public void onFailure(Call<GoodRespons> call, Throwable t) {
                rc_good.setVisibility(View.GONE);
                prog.setVisibility(View.GONE);
                Toast toast =Toast.makeText(GrpActivity.this, "کالایی یافت نشد", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 10, 10);
                toast.show();            }
        });
    }


    private void allgood_more(String edtsearch,String where) {
        prog.setVisibility(View.VISIBLE);
        Call<GoodRespons> call = apiInterface.GetAllGood
                ("goodinfo", 0,edtsearch, where,id,PageNo,shPref.getString("mobile", null),0);
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
                Toast.makeText(GrpActivity.this, "کالای بیشتری موجود نیست", Toast.LENGTH_SHORT).show();
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


    private static String arabicToenglish(String number)
    {
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
            intent = new Intent(GrpActivity.this, BuyActivity.class);
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
                gridLayoutManager = new GridLayoutManager(GrpActivity.this,1);
                gridLayoutManager.scrollToPosition(pastVisiblesItems+1);
                rc_good.setLayoutManager(gridLayoutManager);
                rc_good.setAdapter(adapter_line);

            }else{
                item_multi.findItem(R.id.menu_grid).setIcon(R.drawable.ic_view_line_24dp);
                sEdit.putString("view","grid");
                sEdit.apply();
                //set_rc_good();
                adapter.multi_select=false;
                gridLayoutManager = new GridLayoutManager(GrpActivity.this,2);
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
                if(Objects.equals(shPref.getString("view", null), "grid")){
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

        if(Objects.equals(shPref.getString("view", null), "grid")){
            adapter = new Good_ProSearch_Adapter(goods, GrpActivity.this);
            adapter.multi_select=false;
            gridLayoutManager = new GridLayoutManager(GrpActivity.this,2);
            gridLayoutManager.scrollToPosition(pastVisiblesItems+2);
            rc_good.setLayoutManager(gridLayoutManager);
            rc_good.setAdapter(adapter);
            rc_good.setItemAnimator(new FlipInTopXAnimator());
        }else{
            adapter_line = new Good_ProSearch_Line_Adapter(goods, GrpActivity.this);
            adapter_line.multi_select=false;
            gridLayoutManager = new GridLayoutManager(GrpActivity.this,1);
            gridLayoutManager.scrollToPosition(pastVisiblesItems+1);
            rc_good.setLayoutManager(gridLayoutManager);
            rc_good.setAdapter(adapter_line);
            rc_good.setItemAnimator(new FlipInTopXAnimator());
        }

    }
}
