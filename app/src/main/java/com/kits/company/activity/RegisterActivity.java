package com.kits.company.activity;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.kits.company.R;
import com.kits.company.adapter.InternetConnection;
import com.kits.company.model.Farsi_number;
import com.kits.company.model.User;
import com.kits.company.model.UsersRespons;
import com.kits.company.webService.APIClient;
import com.kits.company.webService.APIInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    APIInterface apiInterface = APIClient.getCleint().create(APIInterface.class);
    Intent intent;

    public EditText edtuser,edtpass,edtrepass,edtmobile,edtfname,edtlname,edtemail;
    String euser,epass,erepass,emobile,efname,elname,eemail,address,postaddress;

    public EditText eloginuser, eloginpass,emobilerecovery;
    public TextInputLayout eloginuser_l, eloginpass_l,emobilerecovery_l;
    String loginuser, loginpass;

    Button to_login,to_reg,reg_btn,login_btn,update_profile,exit_profile,backtohome,btn_edit_pass;
    TextView reg_status_text,fname_profile,lname_profile,mobile_profile,email_profile,address_profile,customernam_profile,postaddress_profile,login_recovery;
    ScrollView register_CardView;
    MaterialCardView  profile_CardView,login_CardView;

    SharedPreferences shPref;
    SharedPreferences.Editor sEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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

    public void setupBadge() {


    }

    public void init() {
        shPref = getSharedPreferences("profile", Context.MODE_PRIVATE);

        find();


        if (Integer.parseInt(shPref.getString("Active", null))== 1)
        {
            reg_status_text.setText("???????????? ????????????");
            register_CardView.setVisibility(View.GONE);
            profile_CardView.setVisibility(View.VISIBLE);
            login_CardView.setVisibility(View.GONE);

        }else{

            reg_status_text.setText("?????? ??????");
            login_CardView.setVisibility(View.VISIBLE);
            profile_CardView.setVisibility(View.GONE);
            register_CardView.setVisibility(View.GONE);
        }

        update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent = new Intent(RegisterActivity.this, ProfileActivity.class);
                intent.putExtra("id", 0);
                finish();
                startActivity(intent);
            }
        });

        btn_edit_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent = new Intent(RegisterActivity.this, ProfileActivity.class);
                intent.putExtra("id", 1);
                finish();
                startActivity(intent);
            }
        });

        login_recovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(login_recovery.getText() == "?????????????? ?????? ??????????"){
                    eloginuser_l.setVisibility(View.GONE);
                    eloginpass_l.setVisibility(View.GONE);
                    emobilerecovery_l.setVisibility(View.VISIBLE);
                    login_recovery.setText("???????? ???? ?????? ???????????? ?? ?????? ????????");
                    login_btn.setText("??????????");

                }else{

                    eloginuser_l.setVisibility(View.VISIBLE);
                    eloginpass_l.setVisibility(View.VISIBLE);
                    emobilerecovery_l.setVisibility(View.GONE);
                    login_recovery.setText("?????????????? ?????? ??????????");
                    login_btn.setText("????????");

                }
            }
        });

        exit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exit_profile();
                finish();
                startActivity(getIntent());
                Toast.makeText(RegisterActivity.this, "???????? ???? ???????? ????????????", Toast.LENGTH_SHORT).show();

            }
        });
        backtohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register_CardView.setVisibility(View.GONE);
                login_CardView.setVisibility(View.VISIBLE);

            }
        });

        to_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_CardView.setVisibility(View.GONE);
                register_CardView.setVisibility(View.VISIBLE);

            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(login_recovery.getText() == "?????????????? ?????? ??????????") {
                    loginuser = eloginuser.getText().toString();
                    loginpass = eloginpass.getText().toString();
                    if (loginuser.isEmpty()) {
                        eloginuser.setError("???????? ?????? ???????? ???? ???????? ????????");
                        eloginuser.requestFocus();
                        return;
                    }
                    if (loginpass.isEmpty()) {
                        eloginpass.setError("???????? ?????? ???????? ???? ???????? ????????");
                        eloginpass.requestFocus();
                        return;
                    }
                    login(loginuser, loginpass);
                }else{

                    final String mobile_recovery= emobilerecovery.getText().toString();

                    Call<UsersRespons> call =apiInterface.XUserCreate("XUserCreate","","","","","",mobile_recovery,"","","","4");
                    call.enqueue(new Callback<UsersRespons>() {
                        @Override
                        public void onResponse(Call<UsersRespons> call, Response<UsersRespons> response) {
                            if(response.isSuccessful()) {
                                ArrayList<User> users = response.body().getUsers();
                                TextView status = findViewById(R.id.registration_status);

                                if (Integer.parseInt(users.get(0).getUserFieldValue("XUserCode"))>0) {
                                    intent = new Intent(RegisterActivity.this, ProfileActivity.class);
                                    intent.putExtra("id", 2);
                                    intent.putExtra("XUserName", users.get(0).getUserFieldValue("XUserName"));
                                    intent.putExtra("XRandomCode", users.get(0).getUserFieldValue("XRandomCode"));
                                    intent.putExtra("mobile_recovery", mobile_recovery);
                                    Log.e("getXRandomCode", users.get(0).getUserFieldValue("XRandomCode"));
                                    startActivity(intent);
                                }

                                if (users.get(0).getUserFieldValue("XUserCode").equals("-1")) {
                                    status.setText("?????? ?????? ???????????? ???????? ?????? ?????? ??????");
                                    Toast.makeText(RegisterActivity.this, "?????? ?????? ???????????? ???????? ?????? ?????? ??????", Toast.LENGTH_SHORT).show();
                                    status.setVisibility(View.VISIBLE);
                                    status.setTextColor(ContextCompat.getColor(RegisterActivity.this, R.color.red_900));
                                }
                                if (users.get(0).getUserFieldValue("XUserCode").equals("-2")) {
                                    Toast.makeText(RegisterActivity.this, "?????????? ???????? ?????? ???????? ?????? ????????", Toast.LENGTH_SHORT).show();
                                    status.setVisibility(View.VISIBLE);
                                    status.setTextColor(ContextCompat.getColor(RegisterActivity.this, R.color.red_900));
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UsersRespons> call, Throwable t) {

                            if(t instanceof IOException){
                                Toast.makeText(RegisterActivity.this, "???????? ???????? ???? ?????????????? ????????????!", Toast.LENGTH_SHORT).show();
                                Log.e("22",""+t.getMessage());
                            }
                        }
                    });


                }


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

                if(efname.isEmpty()) {
                    edtfname.setError("???????? ?????? ???????? ???? ???????? ????????");
                    edtfname.requestFocus();
                    return;
                }
                if(elname.isEmpty()) {
                    edtlname.setError("???????? ?????? ???????? ???? ???????? ????????");
                    edtlname.requestFocus();
                    return;
                }


                if(emobile.length()<8) {
                    edtmobile.setError("?????????????????? ???????????? ???? ???????????? ???????? ????????");
                    edtmobile.requestFocus();
                    return;
                }

                if(!eemail.isEmpty()) {
                    if (!isValidMail(eemail)) {
                        edtemail.setError("???????? ?????????? ???????? ?????? ????????");
                        edtemail.requestFocus();
                        return;
                    }
                }

                if(euser.isEmpty()){
                    edtuser.setError("???????? ?????? ???????? ???? ???????? ????????");
                    edtuser.requestFocus();
                    return;
                }

                if(epass.isEmpty()){
                    edtpass.setError("???????? ?????? ???????? ???? ???????? ????????");
                    edtpass.requestFocus();
                    return;
                }
                if(!erepass.equals(epass)){
                    edtrepass.setError("???????? ???? ?????????? ");
                    edtrepass.requestFocus();
                    return;
                }


                XUserCreate(euser, epass,efname,elname,emobile,eemail);


            }
        });


        fname_profile.setText(Farsi_number.PerisanNumber(Objects.requireNonNull(shPref.getString("fname", null))));
        lname_profile.setText(Farsi_number.PerisanNumber(Objects.requireNonNull(shPref.getString("lname", null))));
        mobile_profile.setText(Farsi_number.PerisanNumber(Objects.requireNonNull(shPref.getString("mobile", null))));
        email_profile.setText(shPref.getString("email", null));
        address_profile.setText(Farsi_number.PerisanNumber(Objects.requireNonNull(shPref.getString("address", null))));
        postaddress_profile.setText(Farsi_number.PerisanNumber(Objects.requireNonNull(shPref.getString("PostalCode", null))));
        customernam_profile.setText(Farsi_number.PerisanNumber(Objects.requireNonNull(shPref.getString("CustomerName", null))));



    }

    private boolean isValidMail(String email) {
        if (!TextUtils.isEmpty(email)) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
        return false;
    }


    private void XUserCreate(String reuser,String repass,String refname,String relname,String remobile, String reemail) {


        Call<UsersRespons> call =apiInterface.XUserCreate("XUserCreate",reuser,repass,"",refname,relname,remobile,"","",reemail,"0");
        call.enqueue(new Callback<UsersRespons>() {
            @Override
            public void onResponse(Call<UsersRespons> call, Response<UsersRespons> response) {
                if(response.isSuccessful()) {
                    ArrayList<User> users = response.body().getUsers();
                    TextView status = findViewById(R.id.registration_status);

                    if (Integer.parseInt(users.get(0).getUserFieldValue("XUserCode"))>0) {
                        status.setText("?????? ?????? ???? ???????????? ?????????? ????");
                        status.setVisibility(View.VISIBLE);
                        status.setTextColor(ContextCompat.getColor(RegisterActivity.this, R.color.green_900));
                        config();


                        intent = new Intent(RegisterActivity.this, ProfileActivity.class);
                        intent.putExtra("id", 3);

                        intent.putExtra("XRandomCode", users.get(0).getUserFieldValue("XRandomCode"));
                        intent.putExtra("mobile_recovery", emobile);

                        finish();
                        startActivity(intent);

                    }

                    if (users.get(0).getUserFieldValue("XUserCode").equals("-1")) {
                        status.setText("?????? ?????? ???????????? ???????? ?????? ?????? ??????");
                        Toast.makeText(RegisterActivity.this, "?????? ?????? ???????????? ???????? ?????? ?????? ??????", Toast.LENGTH_SHORT).show();
                        status.setVisibility(View.VISIBLE);
                        status.setTextColor(ContextCompat.getColor(RegisterActivity.this, R.color.red_900));
                    }
                    if (users.get(0).getUserFieldValue("XUserCode").equals("-2")) {
                        status.setText("?????? ?????????? ???????? ?????? ?????? ??????");
                        Toast.makeText(RegisterActivity.this, "?????? ?????????? ???????? ?????? ?????? ??????", Toast.LENGTH_SHORT).show();
                        status.setVisibility(View.VISIBLE);
                        status.setTextColor(ContextCompat.getColor(RegisterActivity.this, R.color.red_900));
                    }
                }
            }

            @Override
            public void onFailure(Call<UsersRespons> call, Throwable t) {

                if(t instanceof IOException){
                    Toast.makeText(RegisterActivity.this, "???????? ???????? ???? ?????????????? ????????????!", Toast.LENGTH_SHORT).show();
                    Log.e("22",""+t.getMessage());
                }
            }
        });


    }
    private void login(String reuser, String repass) {


         Call<UsersRespons> call =apiInterface.XUserCreate("XUserCreate",reuser,repass,"","","","","","","","5");
        call.enqueue(new Callback<UsersRespons>() {
            @Override
            public void onResponse(Call<UsersRespons> call, Response<UsersRespons> response) {
                if (response.isSuccessful()) {
                    ArrayList<User> users = response.body().getUsers();
                    if(Integer.parseInt(users.get(0).getUserFieldValue("Active")) == 1) {
                        shPref = getSharedPreferences("profile", Context.MODE_PRIVATE);
                        sEdit = shPref.edit();
                        sEdit.putString("Active", users.get(0).getUserFieldValue("Active"));
                        sEdit.putString("fname", users.get(0).getUserFieldValue("FName"));
                        sEdit.putString("lname", users.get(0).getUserFieldValue("LName"));
                        sEdit.putString("mobile", users.get(0).getUserFieldValue("mobile"));
                        sEdit.putString("email", users.get(0).getUserFieldValue("email"));
                        sEdit.putString("address", users.get(0).getUserFieldValue("address"));
                        sEdit.putString("PostalCode", users.get(0).getUserFieldValue("PostalCode"));
                        sEdit.putString("CustomerName", users.get(0).getUserFieldValue("CustomerName"));
                        sEdit.putString("img", " ");
                        sEdit.apply();
                        Toast.makeText(RegisterActivity.this, "?????? ??????????", Toast.LENGTH_SHORT).show();
                        finish();
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    if(Integer.parseInt(users.get(0).getUserFieldValue("Active")) == -1) {
                        Toast.makeText(RegisterActivity.this, "?????? ???????????? ???? ?????? ???????? ???????????? ???? ????????", Toast.LENGTH_SHORT).show();
                    }
                    if(Integer.parseInt(users.get(0).getUserFieldValue("Active")) == -2) {
                        Toast.makeText(RegisterActivity.this, "???????????? ?????? ?????? ???????? ?????? ??????", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UsersRespons> call, Throwable t) {

                    Log.e("retrofit_fail",t.getMessage());


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
        login_recovery.setText("?????????????? ?????? ??????????");

    }
    public void config() {
        shPref = getSharedPreferences("profile", Context.MODE_PRIVATE);
        sEdit = shPref.edit();
        sEdit.putString("Active", "-1");
        sEdit.putString("fname", efname);
        sEdit.putString("lname", elname);
        sEdit.putString("mobile", emobile);
        sEdit.putString("email", eemail);
        sEdit.putString("address", "?????????? ????????");
        sEdit.putString("PostalCode", "?????????? ????????");
        sEdit.putString("CustomerName", "?????????? ????????");
        sEdit.putString("img", " ");
        sEdit.apply();
    }
    public void exit_profile() {
        shPref = getSharedPreferences("profile", Context.MODE_PRIVATE);
        sEdit = shPref.edit();
        sEdit.putString("Active", "-1");
        sEdit.putString("fname", " ");
        sEdit.putString("lname", " ");
        sEdit.putString("mobile", " ");
        sEdit.putString("email", " ");
        sEdit.putString("address", " ");
        sEdit.putString("PostalCode", " ");
        sEdit.putString("CustomerName", " ");
        sEdit.putString("img", " ");
        sEdit.apply();
    }

}
