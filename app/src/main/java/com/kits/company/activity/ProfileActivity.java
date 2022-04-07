package com.kits.company.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.google.android.material.card.MaterialCardView;
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
import com.kits.company.webService.APIVerification;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    APIInterface apiInterface = APIClient.getCleint().create(APIInterface.class);

    EditText fname_profile,lname_profile,mobile_profile,email_profile,address_profile,postaddress_profile,pass_profile,user_profile;
    String euser,epass,erenewepass,enewpass,emobile,efname,elname,eemail,address,postaddress;

    Button update_profile,back;
    Intent intent;
    Integer id=0;
    String xuser,xrandom,xmobile_recovery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initdata();
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


        //edite profile
        if(id==0){
            edite_profile();
        }

        //new pass
        if(id== 1){
            new_pass();
        }

        //recovery pass
        if(id== 2){
            recovery();
        }
        //recovery pass
        if(id== 3){
            Createuser();
        }

    }


    public void initdata() {
        Bundle data = getIntent().getExtras();
        id = data.getInt("id");
    }

    public void edite_profile() {
        MaterialCardView materialCardView= findViewById(R.id.profileactivity_form_update_cardview);
        materialCardView.setVisibility(View.VISIBLE);

        fname_profile= findViewById(R.id.profileactivity_fname);
        lname_profile= findViewById(R.id.profileactivity_lname);
        mobile_profile= findViewById(R.id.profileactivity_mobile);
        email_profile= findViewById(R.id.profileactivity_email);
        address_profile= findViewById(R.id.profileactivity_address);
        postaddress_profile= findViewById(R.id.profileactivity_postaddress);
        pass_profile= findViewById(R.id.profileactivity_pass);
        user_profile= findViewById(R.id.profileactivity_user);

        update_profile= findViewById(R.id.update_profileactivity_profile);
        back= findViewById(R.id.back_profileactivity_profile);

        fname_profile.setHint(GetShared.ReadString("fname"));
        lname_profile.setHint(GetShared.ReadString("lname"));
        mobile_profile.setHint(GetShared.ReadString("mobile"));
        email_profile.setHint(GetShared.ReadString("email"));
        address_profile.setHint(GetShared.ReadString("address"));
        postaddress_profile.setHint(GetShared.ReadString("PostalCode"));
        postaddress_profile.setHint(GetShared.ReadString("CustomerName"));

        update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!fname_profile.getText().toString().equals("")){
                    efname = fname_profile.getText().toString();
                }else {
                    efname=GetShared.ReadString("fname");
                }

                if(!lname_profile.getText().toString().equals("")){
                    elname = lname_profile.getText().toString();
                }else {
                    elname=GetShared.ReadString("lname");
                }

                if(!email_profile.getText().toString().equals("")){
                    eemail = email_profile.getText().toString();
                }else {
                    eemail=GetShared.ReadString("email");
                }

                if(!address_profile.getText().toString().equals("")){
                    address = address_profile.getText().toString();
                }else {
                    address=GetShared.ReadString("address");
                }
                if(!postaddress_profile.getText().toString().equals("")){
                    postaddress = postaddress_profile.getText().toString();
                }else {
                    postaddress=GetShared.ReadString("PostalCode");
                }
                euser = user_profile.getText().toString();
                epass = pass_profile.getText().toString();
                XUserupdate(euser, epass,"",efname,elname,GetShared.ReadString("mobile"),address,postaddress,eemail,"1");

            }
        });

        back.setOnClickListener(view -> finish());
    }

    public void new_pass() {
        MaterialCardView materialCardView= findViewById(R.id.profileactivity_form_changepass_cardview);
        materialCardView.setVisibility(View.VISIBLE);
        final EditText change_user,change_pass,change_newpass,change_renewpass;

        change_user= findViewById(R.id.profileactivity_user_change_pass);
        change_pass= findViewById(R.id.profileactivity_changepass_oldpass);
        change_newpass= findViewById(R.id.profileactivity_changepass_newpass);
        change_renewpass= findViewById(R.id.profileactivity_changepass_newrepass);

        update_profile= findViewById(R.id.update_profileactivity_changepass);
        back= findViewById(R.id.back_profileactivity_changepass_profile);

        update_profile.setOnClickListener(view -> {
            euser = change_user.getText().toString();
            epass = change_pass.getText().toString();
            enewpass = change_newpass.getText().toString();
            erenewepass = change_renewpass.getText().toString();
            if(!erenewepass.equals(enewpass)){
                change_renewpass.setError("مشکل در تایید ");
                change_renewpass.requestFocus();
                return;
            }
            XUserupdate(
                    euser,
                    epass,
                    enewpass,
                    "",
                    "",
                    GetShared.ReadString("mobile"),
                    "",
                    "",
                    "",
                    "2");

        });

        back.setOnClickListener(view -> finish());

    }

    public void recovery() {
        MaterialCardView materialCardView= findViewById(R.id.profileactivity_form_recovery);
        materialCardView.setVisibility(View.VISIBLE);

        final EditText exrandom,edtuser,edtpass,edtrepass;
        TextView mobilee =findViewById(R.id.profileactivity_recovery_mobile);
        exrandom= findViewById(R.id.profileactivity_recovery_xrandom);
        edtuser= findViewById(R.id.profileactivity_recovery_user);
        edtpass= findViewById(R.id.profileactivity_recovery_pass);
        edtrepass= findViewById(R.id.profileactivity_recovery_repass);
        update_profile= findViewById(R.id.profileactivity_recovery_setbtn);
        Bundle data = getIntent().getExtras();
        xuser = data.getString("XUserName");
        xrandom = data.getString("XRandomCode");
        xmobile_recovery = data.getString("mobile_recovery");
        mobilee.setText(NumberFunctions.PerisanNumber(xmobile_recovery));
        edtuser.setText(NumberFunctions.PerisanNumber(xuser));
        Verification();
        exrandom.addTextChangedListener(
                new TextWatcher() {
                    @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                    @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                    @Override
                    public void afterTextChanged(final Editable editable) {

                        if(xrandom.equals(exrandom.getText().toString())){
                            LinearLayoutCompat line_random =findViewById(R.id.profileactivity_xrandom_line);
                            LinearLayoutCompat line_recovery =findViewById(R.id.profileactivity_recovery_pass_line);
                            line_random.setVisibility(View.GONE);
                            line_recovery.setVisibility(View.VISIBLE);
                        }

                    }
                });

        update_profile.setOnClickListener(view -> {

            epass = edtpass.getText().toString();
            enewpass = edtrepass.getText().toString();

            if(!enewpass.equals(epass)){
                edtrepass.setError("مشکل در تایید ");
                edtrepass.requestFocus();
                return;
            }

            XUserupdate(
                    xuser,
                    "",
                    epass,
                    "",
                    "",
                    xmobile_recovery,
                    "",
                    "",
                    "",
                    "3");

        });
    }

    public void Createuser() {
        MaterialCardView materialCardView= findViewById(R.id.profileactivity_form_recovery);
        materialCardView.setVisibility(View.VISIBLE);

        final EditText exrandom;
        TextView mobilee =findViewById(R.id.profileactivity_recovery_mobile);
        exrandom= findViewById(R.id.profileactivity_recovery_xrandom);
        update_profile= findViewById(R.id.profileactivity_recovery_setbtn);
        Bundle data = getIntent().getExtras();
        xrandom = data.getString("XRandomCode");
        xmobile_recovery = data.getString("mobile_recovery");
        mobilee.setText(NumberFunctions.PerisanNumber(xmobile_recovery));
        Verification();
        exrandom.addTextChangedListener(
                new TextWatcher() {
                    @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                    @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                    @Override
                    public void afterTextChanged(final Editable editable) {

                        if(xrandom.equals(exrandom.getText().toString())){
                            App.showToast("خوش آمدید");
                            finish();
                            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                            startActivity(intent);
                            GetShared.EditString("Active", "1");
                        }

                    }
                });


    }


    public void Verification() {
        APIInterface apiInterface = APIVerification.getCleint().create(APIInterface.class);

        Call<RetrofitResponse> call_Verification = apiInterface.Verification(
                "Verification",
                xrandom,
                xmobile_recovery);
        call_Verification.enqueue(new Callback<RetrofitResponse>() {
            @Override
            public void onResponse(@NotNull Call<RetrofitResponse> call, @NotNull Response<RetrofitResponse> response) {
                if (response.isSuccessful()) {
                    Log.e("",response.body().getText());
                }
            }

            @Override
            public void onFailure(@NotNull Call<RetrofitResponse> call, @NotNull Throwable t) {
            }
        });


    }



    private void XUserupdate(
            String reuser,
            String repass,
            String renewpass,
            String refname,
            String relname,
            String remobile,
            String readdress,
            String posataddres,
            String reemail,
            final String flag) {


        Call<RetrofitResponse> call =apiInterface.XUserCreate(
                "XUserCreate",
                reuser,
                repass,
                renewpass,
                refname,
                relname,
                remobile,
                "",
                readdress,
                posataddres,
                reemail,
                flag
        );

        call.enqueue(new Callback<RetrofitResponse>() {
            @Override
            public void onResponse(@NotNull Call<RetrofitResponse> call, @NotNull Response<RetrofitResponse> response) {
                if(response.isSuccessful()) {
                    ArrayList<User> users = response.body().getUsers();

                    if (Integer.parseInt(users.get(0).getUserFieldValue("XUserCode"))>0) {
                        if(flag.equals("1"))
                        {
                            config();
                        }
                        if(flag.equals("3"))
                        {
                            GetShared.EditString("Active", users.get(0).getUserFieldValue("Active"));
                            GetShared.EditString("fname", users.get(0).getUserFieldValue("FName"));
                            GetShared.EditString("lname", users.get(0).getUserFieldValue("LName"));
                            GetShared.EditString("mobile", users.get(0).getUserFieldValue("mobile"));
                            GetShared.EditString("email", users.get(0).getUserFieldValue("email"));
                            GetShared.EditString("address", users.get(0).getUserFieldValue("address"));
                            GetShared.EditString("PostalCode", users.get(0).getUserFieldValue("PostalCode"));
                            GetShared.EditString("img", " ");
                        }
                        App.showToast(" تغییرات با موفقیت ثبت شد");
                        Intent intent = new Intent(ProfileActivity.this, SplashActivity.class);
                        finish();
                        startActivity(intent);
                    }
                    if (users.get(0).getUserFieldValue("XUserCode").equals("-1")) {
                        App.showToast("نام کاربری یا رمز عبور اشتباه است");
                    }
                    if (users.get(0).getUserFieldValue("XUserCode").equals("-2")) {
                        App.showToast("این شماره وجود نداربا مرکز تماس بگیرید");
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<RetrofitResponse> call, @NotNull Throwable t) {

            }
        });


    }

    public void config() {
        GetShared.EditString("Active", "1");
        GetShared.EditString("fname", efname);
        GetShared.EditString("lname", elname);
        GetShared.EditString("email", eemail);
        GetShared.EditString("address", address);
        GetShared.EditString("PostalCode", postaddress);
        GetShared.EditString("img", " ");

    }


}
