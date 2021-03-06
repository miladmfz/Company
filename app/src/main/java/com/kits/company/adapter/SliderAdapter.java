package com.kits.company.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kits.company.R;
import com.kits.company.activity.DetailActivity;
import com.kits.company.activity.MainActivity;
import com.kits.company.model.Good;
import com.kits.company.webService.APIInterface;
import com.kits.company.webService.API_image;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SliderAdapter extends SliderViewAdapter<SliderAdapter.GoodViewHolder> {
    APIInterface apiInterface_image = API_image.getCleint().create(APIInterface.class);
    private List<Good> goods;
    private Intent intent;
    private Context mcontext;
    private Integer code=0;
    private Integer img_count=1;
    private boolean image_zoom;
    private Good goodView;

    public SliderAdapter(Context context,Integer code, Integer img_count, List<Good> goods,boolean zoom) {
        this.mcontext = context;
        this.img_count = img_count;
        this.code = code;
        this.goods = goods;
        this.image_zoom = zoom;

    }

    public SliderAdapter(Context context,Integer code,Integer img_count,boolean zoom) {
        this.mcontext = context;
        this.img_count = img_count;
        this.code = code;
        this.image_zoom = zoom;

    }

    @Override
    public GoodViewHolder onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new GoodViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final GoodViewHolder holder,final  int position) {

        String SERVER_IP_ADDRESS = mcontext.getString(R.string.SERVERIP);

        if(code.equals(0)) {
            goodView = goods.get(position);

            Glide.with(holder.itemView)
                    .load("http://"+SERVER_IP_ADDRESS+goodView.getGoodFieldValue("goodimageurl"))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(holder.imageViewBackground);


            MainActivity activity = (MainActivity) mcontext;
            activity.sliderView.getLayoutParams().height=Integer.parseInt(String.valueOf(activity.sliderView.getMeasuredWidthAndState()/3));



        }else {
            Call<String> call2 = apiInterface_image.GetImage("getImage", code.toString(),position,400);
            call2.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call2, Response<String> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        try {


                            if (!response.body().equals("no_photo")) {
                                Glide.with(holder.itemView)
                                        .setDefaultRequestOptions(new RequestOptions().timeout(30000))
                                        .asBitmap()
                                        .load(Base64.decode(response.body(), Base64.DEFAULT))
                                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                        .placeholder(R.drawable.no_photo)
                                        .error(R.drawable.no_photo) //6
                                        .fallback(R.drawable.no_photo)
                                        .fitCenter()
                                        .into(holder.imageViewBackground);
                            } else {
                                Glide.with(holder.itemView)
                                        .setDefaultRequestOptions(new RequestOptions().timeout(30000))
                                        .asBitmap()
                                        .load(R.drawable.no_photo)
                                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                        .placeholder(R.drawable.no_photo)
                                        .error(R.drawable.no_photo) //6
                                        .fallback(R.drawable.no_photo)
                                        .fitCenter()
                                        .into(holder.imageViewBackground);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                @Override
                public void onFailure(Call<String> call2, Throwable t) {
                    Log.e("onFailure", "" + t.toString());
                }
            });


        }

        holder.fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(image_zoom){
                    image_zome_view();
                }else {
                    try {
                        if (Integer.parseInt(goodView.getGoodFieldValue("GoodName").substring(2)) > 0) {

                            intent = new Intent(mcontext, DetailActivity.class);
                            intent.putExtra("id", Integer.parseInt(goods.get(position).getGoodFieldValue("GoodName").substring(2)));
                            mcontext.startActivity(intent);
                        }
                    }catch (Exception e)
                    {

                    }
                }

            }
        });

    }
    @Override
    public int getCount() {
        return img_count;
    }
    class GoodViewHolder extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        FrameLayout fl;

        public GoodViewHolder(View itemView) {

            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            fl = itemView.findViewById(R.id.iv_main_slider);
            this.itemView = itemView;
        }
    }
    public void image_zome_view() {
        final Dialog dialog = new Dialog(mcontext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);//title laye nadashte bashim
        dialog.setContentView(R.layout.image_zoom);
        SliderView sliderView =  dialog.findViewById(R.id.imageSlider_zoom_view);
        SliderAdapter adapter = new SliderAdapter(mcontext,code,img_count,false);
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimations.SCALE); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        dialog.show();


    }

}
