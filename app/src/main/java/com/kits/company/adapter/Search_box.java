package com.kits.company.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.google.android.material.button.MaterialButton;
import com.kits.company.R;
import com.kits.company.activity.GrpActivity;
import com.kits.company.activity.SearchActivity;
import com.kits.company.model.Column;
import com.kits.company.model.NumberFunctions;
import com.kits.company.model.RetrofitResponse;
import com.kits.company.webService.APIClient;
import com.kits.company.webService.APIInterface;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Search_box {

    private final Context mContext;


    private final APIInterface apiInterface = APIClient.getCleint().create(APIInterface.class);

    Spinner spinner;
    String sq,srch;
    ArrayList<Column> Goodtype;
    ArrayList<String> Goodtype_array=new ArrayList<>() ;
    ArrayList<Column> Columns;
    LinearLayoutCompat layout_view;
    MaterialButton btn_search;
    Dialog dialog;

    public Search_box(Context context)
    {
        this.mContext=context;
        this.sq ="";
        this.srch ="";
    }


    public void search_pro( ) {


        dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//title laye nadashte bashim
        dialog.setContentView(R.layout.search_box);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);

        spinner = dialog.findViewById(R.id.search_box_spinner);
        layout_view = dialog.findViewById(R.id.search_box_layout_pro);



        Call<RetrofitResponse> call = apiInterface.GetGoodType("GetGoodType");
        call.enqueue(new Callback<RetrofitResponse>() {
            @Override
            public void onResponse(@NotNull Call<RetrofitResponse> call, @NotNull Response<RetrofitResponse> response) {
                if (response.isSuccessful()) {

                    Integer i=0;
                    Integer j=0;
                    Goodtype = response.body().getColumns();
                    Column Column_all = new Column();
                    Column_all.setGoodType("همه");
                    Column_all.setIsDefault("0");
                    Goodtype.add(0,Column_all);
                    for ( Column Column_Goodtype : Goodtype){
                        Goodtype_array.add(Column_Goodtype.getColumnFieldValue("goodtype"));
                        if(Integer.parseInt(Column_Goodtype.getColumnFieldValue("IsDefault"))==1){
                            j=i;
                        }
                        i++;
                    }

                    ArrayAdapter<String> spinner_adapter = new ArrayAdapter<>(mContext,
                            android.R.layout.simple_spinner_item, Goodtype_array);
                    spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(spinner_adapter);
                    spinner.setSelection(j);

                }
            }

            @Override
            public void onFailure(@NotNull Call<RetrofitResponse> call, @NotNull Throwable t) {

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                layout_view.removeAllViews();
                pro_c(Goodtype_array.get(position));

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        dialog.show();

    }





    public void pro_c(String Goodtype) {

        Call<RetrofitResponse> call = apiInterface.GetColumn("GetColumnList","0",Goodtype,"3");
        call.enqueue(new Callback<RetrofitResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(@NotNull Call<RetrofitResponse> call, @NotNull Response<RetrofitResponse> response) {
                if (response.isSuccessful()) {
                    Columns = response.body().getColumns();
                    for ( Column Column : Columns){
                        Column.setSearch("");

                        if(Integer.parseInt(Column.getColumnFieldValue("SortOrder"))>0) {

                            layout_view.setOrientation(LinearLayoutCompat.VERTICAL);
                            LinearLayoutCompat layout_view_child = new LinearLayoutCompat(mContext);
                            layout_view_child.setOrientation(LinearLayoutCompat.HORIZONTAL);
                            layout_view_child.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT));
                            layout_view_child.setWeightSum(1);
                            layout_view_child.setPadding(5,5,5,5);

                            TextView extra_TextView1 = new TextView(mContext);
                            extra_TextView1.setText(NumberFunctions.PerisanNumber(Column.getColumnFieldValue("ColumnDesc")));
                            extra_TextView1.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.MATCH_PARENT, (float) 0.7));
                            extra_TextView1.setTextSize(14);
                            extra_TextView1.setPadding(2, 2, 2, 2);
                            extra_TextView1.setGravity(Gravity.CENTER);
                            extra_TextView1.setTextColor(mContext.getResources().getColor(R.color.grey_1000));
                            layout_view_child.addView(extra_TextView1);

                            EditText extra_EditText= new EditText(mContext);
                            extra_EditText.setBackgroundResource(R.drawable.bg_round_dark);
                            extra_EditText.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.MATCH_PARENT, (float) 0.3));
                            extra_EditText.setTextSize(15);
                            extra_EditText.setId(Integer.parseInt(Column.getColumnFieldValue("sortorder")));
                            extra_EditText.setHint(Column.getColumnFieldValue("ColumnName"));
                            extra_EditText.setHintTextColor(mContext.getResources().getColor(R.color.white));
                            extra_EditText.setId(View.generateViewId());
                            extra_EditText.setPadding(2, 2, 2, 2);
                            extra_EditText.setGravity(Gravity.CENTER);
                            extra_EditText.setTextColor(mContext.getResources().getColor(R.color.grey_1000));
                            layout_view_child.addView(extra_EditText);



                            layout_view.addView(layout_view_child);


                        }
                    }




                    btn_search= new MaterialButton(mContext);

                    btn_search.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.MATCH_PARENT, LinearLayoutCompat.LayoutParams.MATCH_PARENT));
                    btn_search.setText(NumberFunctions.PerisanNumber("اعمال فیلتر ها"));
                    btn_search.setTextSize(12);
                    btn_search.setTextColor(mContext.getResources().getColor(R.color.grey_1000));
                    btn_search.setStrokeColor(ColorStateList.valueOf(mContext.getResources().getColor(R.color.grey_1000)));
                    btn_search.setStrokeWidth(2);
                    btn_search.setBackgroundTintList(ColorStateList.valueOf(mContext.getResources().getColor(R.color.white)));
                    btn_search.setOnClickListener(v -> {
                        for (int i = 0; i < layout_view.getChildCount(); i++) {
                            if(layout_view.getChildAt(i) instanceof LinearLayoutCompat) {
                                LinearLayoutCompat LinearLayoutCompat=(androidx.appcompat.widget.LinearLayoutCompat) layout_view.getChildAt(i);
                                for (int j = 0; j < LinearLayoutCompat.getChildCount(); j++) {
                                    if(LinearLayoutCompat.getChildAt(j) instanceof EditText) {
                                        EditText et = (EditText) LinearLayoutCompat.getChildAt(j);
                                        for ( Column Column : Columns) {
                                            if (et.getHint().toString().equals(Column.getColumnFieldValue("ColumnName"))){
                                                if(!et.getText().toString().equals("")) {
                                                    Column.setSearch(NumberFunctions.EnglishNumber(et.getText().toString()));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        sq=" GoodType = N''"+Columns.get(0).getColumnFieldValue("goodtype")+"'' ";
                        for ( Column Column : Columns) {
                            if(!Column.getColumnFieldValue("search").equals("")) {

                                if(!Column.getColumnFieldValue("columndefinition").equals(""))
                                    if(Column.getColumnFieldValue("ColumnName").equals("MinPrice")){
                                        sq = sq + " And " + Column.getColumnFieldValue("columndefinition") + " >= " + Column.getColumnFieldValue("search") ;

                                    }else if(!Column.getColumnFieldValue("ColumnName").equals("MaxPrice")){
                                        sq = sq + " And " + Column.getColumnFieldValue("columndefinition") + " <= " + Column.getColumnFieldValue("search") ;

                                    }else {
                                        sq = sq + " And " + Column.getColumnFieldValue("columndefinition") + " Like N''%" + Column.getColumnFieldValue("search") + "%'' ";
                                    }
                                else{
                                    sq= sq + " And "+Column.getColumnFieldValue("ColumnName")+" Like N''%"+Column.getColumnFieldValue("search")+"%'' ";
                                }

                            }
                        }




                        if(mContext.getClass().getName().equals("com.kits.company.activity.SearchActivity")){
                            SearchActivity activity= (SearchActivity) mContext ;
                            activity.srch="";
                            activity.sq=sq;
                            activity.PageNo=0;
                            activity.allgood(srch,sq);
                            dialog.dismiss();

                        }
                        else if(mContext.getClass().getName().equals("com.kits.company.activity.GrpActivity")){
                            GrpActivity activity= (GrpActivity) mContext ;
                            activity.srch="";
                            activity.sq=sq;
                            activity.PageNo=0;
                            activity.allgood(srch,sq);
                            dialog.dismiss();
                        }


                    });
                    layout_view.addView(btn_search);

                }
            }
            @Override
            public void onFailure(@NotNull Call<RetrofitResponse> call, @NotNull Throwable t) {

            }
        });

    }




}
