package com.kits.company.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.kits.company.R;
import com.kits.company.adapter.InternetConnection;
import com.kits.company.model.Farsi_number;
import com.kits.company.model.GoodGroupRespons;
import com.kits.company.model.User;
import com.kits.company.model.UsersRespons;
import com.kits.company.webService.APIClient;
import com.kits.company.webService.APIInterface;
import com.kits.company.webService.APIVerification;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    APIInterface apiInterface = APIClient.getCleint().create(APIInterface.class);

    SharedPreferences shPref;
    SharedPreferences.Editor sEdit;
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

        fname_profile.setHint(shPref.getString("fname", null));
        lname_profile.setHint(shPref.getString("lname", null));
        mobile_profile.setHint(shPref.getString("mobile", null));
        email_profile.setHint(shPref.getString("email", null));
        address_profile.setHint(shPref.getString("address", null));
        postaddress_profile.setHint(shPref.getString("PostalCode", null));
        postaddress_profile.setHint(shPref.getString("CustomerName", null));

        update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!fname_profile.getText().toString().equals("")){
                    efname = fname_profile.getText().toString();
                }else {
                    efname=shPref.getString("fname", null);
                }

                if(!lname_profile.getText().toString().equals("")){
                    elname = lname_profile.getText().toString();
                }else {
                    elname=shPref.getString("lname", null);
                }

                if(!email_profile.getText().toString().equals("")){
                    eemail = email_profile.getText().toString();
                }else {
                    eemail=shPref.getString("email", null);
                }

                if(!address_profile.getText().toString().equals("")){
                    address = address_profile.getText().toString();
                }else {
                    address=shPref.getString("address", null);
                }
                if(!postaddress_profile.getText().toString().equals("")){
                    postaddress = postaddress_profile.getText().toString();
                }else {
                    postaddress=shPref.getString("PostalCode", null);
                }
                euser = user_profile.getText().toString();
                epass = pass_profile.getText().toString();
                XUserCreate(euser, epass,"",efname,elname,shPref.getString("mobile", null),address,postaddress,eemail,"1");

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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

        update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                euser = change_user.getText().toString();
                epass = change_pass.getText().toString();
                enewpass = change_newpass.getText().toString();
                erenewepass = change_renewpass.getText().toString();
                if(!erenewepass.equals(enewpass)){
                    change_renewpass.setError("مشکل در تایید ");
                    change_renewpass.requestFocus();
                    return;
                }
                XUserCreate(euser, epass,enewpass,"","",shPref.getString("mobile", null),"","","","2");

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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
        mobilee.setText(Farsi_number.PerisanNumber(xmobile_recovery));
        edtuser.setText(Farsi_number.PerisanNumber(xuser));
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

        update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                epass = edtpass.getText().toString();
                enewpass = edtrepass.getText().toString();

                if(!enewpass.equals(epass)){
                    edtrepass.setError("مشکل در تایید ");
                    edtrepass.requestFocus();
                    return;
                }

                XUserCreate(xuser, "",epass,"","",xmobile_recovery,"","","","3");

            }
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
        mobilee.setText(Farsi_number.PerisanNumber(xmobile_recovery));
        Verification();
        exrandom.addTextChangedListener(
                new TextWatcher() {
                    @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                    @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
                    @Override
                    public void afterTextChanged(final Editable editable) {

                        if(xrandom.equals(exrandom.getText().toString())){
                            Toast.makeText(ProfileActivity.this, "خوش آمدید", Toast.LENGTH_SHORT).show();
                            finish();
                            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                            startActivity(intent);
                            shPref = getSharedPreferences("profile", Context.MODE_PRIVATE);
                            sEdit = shPref.edit();
                            sEdit.putString("Active", "1");
                            sEdit.apply();
                        }

                    }
                });


    }


    public void Verification() {
        APIInterface apiInterface = APIVerification.getCleint().create(APIInterface.class);

        Call<String> call_Verification = apiInterface.Verification("Verification",Integer.parseInt(xrandom),xmobile_recovery);
        call_Verification.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.e("",response.body());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });


    }



    private void XUserCreate(String reuser, String repass, String renewpass, String refname, String relname, String remobile, String readdress, String posataddres, String reemail, final String flag) {


        Call<UsersRespons> call =apiInterface.XUserCreate("XUserCreate",reuser,repass,renewpass,refname,relname,remobile,readdress,posataddres,reemail,flag);
        call.enqueue(new Callback<UsersRespons>() {
            @Override
            public void onResponse(Call<UsersRespons> call, Response<UsersRespons> response) {
                if(response.isSuccessful()) {
                    ArrayList<User> users = response.body().getUsers();

                    if (Integer.parseInt(users.get(0).getUserFieldValue("XUserCode"))>0) {
                        if(flag.equals("1"))
                        {
                            config();
                        }
                        if(flag.equals("3"))
                        {
                            shPref = getSharedPreferences("profile", Context.MODE_PRIVATE);
                            sEdit = shPref.edit();
                            sEdit.putString("Active", users.get(0).getUserFieldValue("Active"));
                            sEdit.putString("fname", users.get(0).getUserFieldValue("FName"));
                            sEdit.putString("lname", users.get(0).getUserFieldValue("LName"));
                            sEdit.putString("mobile", users.get(0).getUserFieldValue("mobile"));
                            sEdit.putString("email", users.get(0).getUserFieldValue("email"));
                            sEdit.putString("address", users.get(0).getUserFieldValue("address"));
                            sEdit.putString("PostalCode", users.get(0).getUserFieldValue("PostalCode"));
                            sEdit.putString("img", " ");
                            sEdit.apply();
                        }

                        Toast.makeText(ProfileActivity.this, " تغییرات با موفقیت ثبت شد", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ProfileActivity.this, SplashActivity.class);
                        finish();
                        startActivity(intent);
                    }
                    if (users.get(0).getUserFieldValue("XUserCode").equals("-1")) {
                        Toast.makeText(ProfileActivity.this, "نام کاربری یا رمز عبور اشتباه است", Toast.LENGTH_SHORT).show();
                    }
                    if (users.get(0).getUserFieldValue("XUserCode").equals("-2")) {
                        Toast.makeText(ProfileActivity.this, "این شماره وجود نداربا مرکز تماس بگیرید", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UsersRespons> call, Throwable t) {
                if(t instanceof IOException){
                    Log.e("22",""+t.getMessage());
                }
            }
        });


    }

    public void config() {
        shPref = getSharedPreferences("profile", Context.MODE_PRIVATE);
        sEdit = shPref.edit();
        sEdit.putString("Active", "1");
        sEdit.putString("fname", efname);
        sEdit.putString("lname", elname);
        sEdit.putString("email", eemail);
        sEdit.putString("address", address);
        sEdit.putString("PostalCode", postaddress);
        sEdit.putString("img", " ");
        sEdit.apply();
    }


}
