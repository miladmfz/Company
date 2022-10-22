package com.kits.company.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputLayout;
import com.kits.company.R;
import com.kits.company.adapter.GetShared;
import com.kits.company.adapter.InternetConnection;
import com.kits.company.application.App;
import com.kits.company.model.NumberFunctions;
import com.kits.company.model.RetrofitResponse;
import com.kits.company.model.User;
import com.kits.company.webService.APIClient;
import com.kits.company.webService.APIInterface;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    APIInterface apiInterface = APIClient.getCleint().create(APIInterface.class);
    Intent intent;

    public EditText edtuser,edtpass,edtrepass,edtmobile,edtcompany,edtfname,edtlname,edtemail;
    String euser,epass,erepass,emobile,ecompany,efname,elname,eemail,address,postaddress;

    public EditText eloginuser, eloginpass,emobilerecovery;
    public TextInputLayout eloginuser_l, eloginpass_l,emobilerecovery_l;
    String loginuser, loginpass;

    Button to_login,to_reg,reg_btn,login_btn,update_profile,exit_profile,backtohome,btn_edit_pass;
    TextView reg_status_text,fname_profile,lname_profile,mobile_profile,email_profile,address_profile,customernam_profile,postaddress_profile,login_recovery;
    ScrollView register_CardView;
    MaterialCardView profile_CardView,login_CardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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

//****************************************************************

    public void setupBadge() {


    }

    public void init() {

        find();


        if (GetShared.ReadString("Active").equals("1")) {
            reg_status_text.setText("مشخصات کاربری");
            register_CardView.setVisibility(View.GONE);
            profile_CardView.setVisibility(View.VISIBLE);
            login_CardView.setVisibility(View.GONE);

        }else{

            reg_status_text.setText("ثبت نام");
            login_CardView.setVisibility(View.VISIBLE);
            profile_CardView.setVisibility(View.GONE);
            register_CardView.setVisibility(View.GONE);
        }

        update_profile.setOnClickListener(view -> {

            intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("id", 0);
            finish();
            startActivity(intent);
        });

        btn_edit_pass.setOnClickListener(view -> {

            intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("id", 1);
            finish();
            startActivity(intent);
        });

        login_recovery.setOnClickListener(view -> {

            if(login_recovery.getText() == "فراموشی رمز عبور؟"){
                eloginuser_l.setVisibility(View.GONE);
                eloginpass_l.setVisibility(View.GONE);
                emobilerecovery_l.setVisibility(View.VISIBLE);
                login_recovery.setText("ورود با نام کاربری و رمز عبور");
                login_btn.setText("تایید");

            }else{

                eloginuser_l.setVisibility(View.VISIBLE);
                eloginpass_l.setVisibility(View.VISIBLE);
                emobilerecovery_l.setVisibility(View.GONE);
                login_recovery.setText("فراموشی رمز عبور؟");
                login_btn.setText("ورود");

            }
        });

        exit_profile.setOnClickListener(view -> {
            exit_profile();
            finish();
            startActivity(getIntent());
            App.showToast( "خروج از حساب کاربری");

        });
        backtohome.setOnClickListener(view -> finish());


        to_login.setOnClickListener(view -> {
            register_CardView.setVisibility(View.GONE);
            login_CardView.setVisibility(View.VISIBLE);

        });

        to_reg.setOnClickListener(view -> {
            login_CardView.setVisibility(View.GONE);
            register_CardView.setVisibility(View.VISIBLE);

        });

        login_btn.setOnClickListener(view -> {
            if(login_recovery.getText().equals("فراموشی رمز عبور؟")){
                loginuser = eloginuser.getText().toString();
                loginpass = eloginpass.getText().toString();
                if (loginuser.isEmpty()) {
                    eloginuser.setError("لطفا این قسمت را کامل کنید");
                    eloginuser.requestFocus();
                    return;
                }
                if (loginpass.isEmpty()) {
                    eloginpass.setError("لطفا این قسمت را کامل کنید");
                    eloginpass.requestFocus();
                    return;
                }
                login(loginuser, loginpass);
            }else{

                final String mobile_recovery= emobilerecovery.getText().toString();

                Call<RetrofitResponse> call =apiInterface.XUserCreate(
                        "XUserCreate",
                        "",
                        "",
                        "",
                        "",
                        "",
                        mobile_recovery,
                        "",
                        "",
                        "",
                        "",
                        "4");
                call.enqueue(new Callback<RetrofitResponse>() {
                    @Override
                    public void onResponse(@NotNull Call<RetrofitResponse> call, @NotNull Response<RetrofitResponse> response) {
                        if(response.isSuccessful()) {
                            ArrayList<User> users = response.body().getUsers();
                            TextView status = findViewById(R.id.registration_status);
                            if (Integer.parseInt(users.get(0).getUserFieldValue("XUserCode"))>0) {
                                intent = new Intent(RegisterActivity.this, ProfileActivity.class);
                                intent.putExtra("id", 2);
                                intent.putExtra("XUserName", users.get(0).getUserFieldValue("XUserName"));
                                intent.putExtra("XRandomCode", users.get(0).getUserFieldValue("XRandomCode"));
                                intent.putExtra("mobile_recovery", mobile_recovery);
                                startActivity(intent);
                            }
                            if (users.get(0).getUserFieldValue("XUserCode").equals("-1")) {
                                status.setText("این نام کاربری قبلا ثبت شده است");
                                App.showToast("");
                                App.showToast("این نام کاربری قبلا ثبت شده است");
                                status.setVisibility(View.VISIBLE);
                                status.setTextColor(ContextCompat.getColor(App.getContext(), R.color.red_900));
                            }
                            if (users.get(0).getUserFieldValue("XUserCode").equals("-2")) {
                                App.showToast( "شماره وارد شده صحیح نمی باشد");
                                status.setVisibility(View.VISIBLE);
                                status.setTextColor(ContextCompat.getColor(App.getContext(), R.color.red_900));
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<RetrofitResponse> call, @NotNull Throwable t) {

                        if(t instanceof IOException){
                            App.showToast( "بروز مشکل در برقراری ارتباط!");
                        }
                    }
                });
            }
        });


        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                efname = edtfname.getText().toString();
                elname = edtlname.getText().toString();
                emobile = edtmobile.getText().toString();
                eemail = edtemail.getText().toString();
                euser = edtuser.getText().toString();
                epass = edtpass.getText().toString();
                erepass = edtrepass.getText().toString();
                ecompany = edtcompany.getText().toString();

                if(efname.isEmpty()) {
                    edtfname.setError("لطفا این قسمت را کامل کنید");
                    edtfname.requestFocus();
                    return;
                }
                if(elname.isEmpty()) {
                    edtlname.setError("لطفا این قسمت را کامل کنید");
                    edtlname.requestFocus();
                    return;
                }


                if(emobile.length()<8) {
                    edtmobile.setError("لطفاشماره موبایل را بدرستی وارد کنید");
                    edtmobile.requestFocus();
                    return;
                }
                if(ecompany.isEmpty()) {
                    edtcompany.setError("لطفا این قسمت را کامل کنید");
                    edtcompany.requestFocus();
                    return;
                }

                if(!eemail.isEmpty()) {
                    if (!isValidMail(eemail)) {
                        edtemail.setError("ادرس ایمیل صحیح نمی باشد");
                        edtemail.requestFocus();
                        return;
                    }
                }

                if(euser.isEmpty()){
                    edtuser.setError("لطفا این قسمت را کامل کنید");
                    edtuser.requestFocus();
                    return;
                }

                if(epass.isEmpty()){
                    edtpass.setError("لطفا این قسمت را کامل کنید");
                    edtpass.requestFocus();
                    return;
                }
                if(!erepass.equals(epass)){
                    edtrepass.setError("مشکل در تایید ");
                    edtrepass.requestFocus();
                    return;
                }
                XUserCreate(euser, epass,efname,elname,emobile,ecompany,eemail);
            }
        });


        fname_profile.setText(NumberFunctions.PerisanNumber(GetShared.ReadString("fname")));
        lname_profile.setText(NumberFunctions.PerisanNumber(GetShared.ReadString("lname")));
        mobile_profile.setText(NumberFunctions.PerisanNumber(GetShared.ReadString("mobile")));
        email_profile.setText(GetShared.ReadString("email"));
        address_profile.setText(NumberFunctions.PerisanNumber(GetShared.ReadString("address")));
        postaddress_profile.setText(NumberFunctions.PerisanNumber(GetShared.ReadString("PostalCode")));
        customernam_profile.setText(NumberFunctions.PerisanNumber(GetShared.ReadString("CustomerName")));



    }

    private boolean isValidMail(String email) {
        if (!TextUtils.isEmpty(email)) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
        return false;
    }


    private void XUserCreate(
            String reuser,
            String repass,
            String refname,
            String relname,
            String remobile,
            String recompany,
            String reemail
    )
    {

        Call<RetrofitResponse> call =apiInterface.XUserCreate(
                "XUserCreate",
                reuser,
                repass,
                "",
                refname,
                relname,
                remobile,
                recompany,
                "",
                "",
                reemail,
                "0"
        );
        call.enqueue(new Callback<RetrofitResponse>() {
            @Override
            public void onResponse(@NotNull Call<RetrofitResponse> call, @NotNull Response<RetrofitResponse> response) {
                if(response.isSuccessful()) {
                    ArrayList<User> users = response.body().getUsers();
                    TextView status = findViewById(R.id.registration_status);


                    if(users.get(0).getUserFieldValue("errcode").equals("0")){
                        if (Integer.parseInt(users.get(0).getUserFieldValue("XUserCode"))>0) {
                            status.setText("ثبت نام با موفقیت انجام شد");
                            status.setVisibility(View.VISIBLE);
                            status.setTextColor(ContextCompat.getColor(App.getContext(), R.color.green_900));
                            config();


                            intent = new Intent(RegisterActivity.this, ProfileActivity.class);
                            intent.putExtra("id", 3);
                            intent.putExtra("XRandomCode", users.get(0).getUserFieldValue("XRandomCode"));
                            intent.putExtra("mobile_recovery", emobile);
                            finish();
                            startActivity(intent);

                        }

                        if (users.get(0).getUserFieldValue("XUserCode").equals("-1")) {
                            status.setText("این نام کاربری قبلا ثبت شده است");
                            App.showToast("این نام کاربری قبلا ثبت شده است");
                            status.setVisibility(View.VISIBLE);
                            status.setTextColor(ContextCompat.getColor(App.getContext(), R.color.red_900));
                        }
                        if (users.get(0).getUserFieldValue("XUserCode").equals("-2")) {
                            status.setText("این شماره قبلا ثبت شده است");
                            App.showToast("این شماره قبلا ثبت شده است");

                            status.setVisibility(View.VISIBLE);
                            status.setTextColor(ContextCompat.getColor(App.getContext(), R.color.red_900));
                        }
                    }else {
                        status.setText(users.get(0).getUserFieldValue("errdesc"));
                        App.showToast( users.get(0).getUserFieldValue("errdesc"));
                        status.setVisibility(View.VISIBLE);
                        status.setTextColor(ContextCompat.getColor(App.getContext(), R.color.red_900));
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<RetrofitResponse> call, @NotNull Throwable t) {

                if(t instanceof IOException){
                        App.showToast("بروز مشکل در برقراری ارتباط!");
                }
            }
        });


    }
    private void login(String reuser, String repass) {


         Call<RetrofitResponse> call =apiInterface.XUserCreate("XUserCreate",reuser,repass,"","","","","","","","","5");
        call.enqueue(new Callback<RetrofitResponse>() {
            @Override
            public void onResponse(@NotNull Call<RetrofitResponse> call, @NotNull Response<RetrofitResponse> response) {
                if (response.isSuccessful()) {
                    ArrayList<User> users = response.body().getUsers();
                    if(users.get(0).getUserFieldValue("Active").equals("1")) {

                        GetShared.EditString("Active", users.get(0).getUserFieldValue("Active"));
                        GetShared.EditString("fname", users.get(0).getUserFieldValue("FName"));
                        GetShared.EditString("lname", users.get(0).getUserFieldValue("LName"));
                        GetShared.EditString("mobile", users.get(0).getUserFieldValue("mobile"));
                        GetShared.EditString("email", users.get(0).getUserFieldValue("email"));
                        GetShared.EditString("address", users.get(0).getUserFieldValue("address"));
                        GetShared.EditString("PostalCode", users.get(0).getUserFieldValue("PostalCode"));
                        GetShared.EditString("CustomerName", users.get(0).getUserFieldValue("CustomerName"));
                        GetShared.EditString("img", " ");
                        App.showToast("خوش آمدید");
                        finish();
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    if(Integer.parseInt(users.get(0).getUserFieldValue("Active")) == -1) {
                        App.showToast("نام کاربری یا رمز عبور اشتباه می باشد");
                    }
                    if(Integer.parseInt(users.get(0).getUserFieldValue("Active")) == -2) {
                        App.showToast("کاربری شما غیر فعال شده است");
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<RetrofitResponse> call, @NotNull Throwable t) {

            }
        });



    }

    public void find() {
        edtfname = findViewById(R.id.registration_fname);
        edtlname = findViewById(R.id.registration_lname);
        edtmobile = findViewById(R.id.registration_mobile);
        edtemail = findViewById(R.id.registration_email);
        edtuser = findViewById(R.id.registration_user);
        edtpass = findViewById(R.id.registration_pass);
        edtrepass = findViewById(R.id.registration_repass);
        edtcompany = findViewById(R.id.registration_company);

        to_login = findViewById(R.id.registration_to_login);
        to_reg = findViewById(R.id.login_to_register);

        reg_btn = findViewById(R.id.registration_reg);
        reg_status_text = findViewById(R.id.reg_status_text);

        register_CardView = findViewById(R.id.register_form);
        profile_CardView = findViewById(R.id.profile_form);
        login_CardView = findViewById(R.id.login_form);

        fname_profile= findViewById(R.id.profile_fname);
        lname_profile= findViewById(R.id.profile_lname);
        mobile_profile= findViewById(R.id.profile_mobile);
        email_profile= findViewById(R.id.profile_email);
        address_profile= findViewById(R.id.profile_address);
        postaddress_profile= findViewById(R.id.profile_postaddress);
        customernam_profile= findViewById(R.id.profile_customername);
        update_profile= findViewById(R.id.update_profile);
        exit_profile= findViewById(R.id.exit_profile);
        backtohome= findViewById(R.id.backtohome_profile);
        btn_edit_pass= findViewById(R.id.profile_edit_pass);

        eloginuser = findViewById(R.id.login_user);
        eloginpass = findViewById(R.id.login_pass);
        emobilerecovery = findViewById(R.id.login_mobile);
        eloginuser_l = findViewById(R.id.login_user_layout);
        eloginpass_l = findViewById(R.id.login_pass_layout);
        emobilerecovery_l = findViewById(R.id.login_mobile_layout);
        login_btn = findViewById(R.id.login_btn);
        login_recovery = findViewById(R.id.login_recovery);
        login_recovery.setText("فراموشی رمز عبور؟");

    }
    public void config() {
        GetShared.EditString("Active", "-1");
        GetShared.EditString("fname", efname);
        GetShared.EditString("lname", elname);
        GetShared.EditString("mobile", emobile);
        GetShared.EditString("email", eemail);
        GetShared.EditString("address", "معرفی نشده");
        GetShared.EditString("PostalCode", "معرفی نشده");
        GetShared.EditString("CustomerName", "معرفی نشده");
        GetShared.EditString("img", " ");


    }
    public void exit_profile() {
        GetShared.EditString("Active", "-1");
        GetShared.EditString("fname", " ");
        GetShared.EditString("lname", " ");
        GetShared.EditString("mobile", " ");
        GetShared.EditString("email", " ");
        GetShared.EditString("address", " ");
        GetShared.EditString("PostalCode", " ");
        GetShared.EditString("CustomerName", " ");
        GetShared.EditString("img", " ");

    }

}
