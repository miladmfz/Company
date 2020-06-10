package com.kits.company.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.kits.company.R;
import com.kits.company.webService.APIClient;
import com.kits.company.webService.APIInterface;
import com.kits.company.webService.API_image;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SliderAdapterExample extends SliderViewAdapter<SliderAdapterExample.SliderAdapterVH> {
    APIInterface apiInterface_image = API_image.getCleint().create(APIInterface.class);
    byte[] imageByteArray ;

    private Context mcontext;
    private Integer code=0;
    private Integer img_count=1;

    public SliderAdapterExample(Context context) {
        this.mcontext = context;
        this.img_count = 4;

    }
    public SliderAdapterExample(Context context,Integer code,Integer img_count) {
        this.mcontext = context;
        this.code = code;
        this.img_count = img_count;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(final SliderAdapterVH viewHolder, final int position) {



        Call<String> call2 = apiInterface_image.GetImage("getImagecompany", code.toString(),position,250);
        call2.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call2, Response<String> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        try {


                            if (!response.body().equals("no_photo")) {
                                Glide.with(viewHolder.itemView)
                                        .setDefaultRequestOptions(new RequestOptions().timeout(30000))
                                        .asBitmap()
                                        .load(Base64.decode(response.body(), Base64.DEFAULT))
                                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                        .placeholder(R.drawable.no_photo)
                                        .error(R.drawable.no_photo) //6
                                        .fallback(R.drawable.no_photo)
                                        .override(2000, 2000)
                                        .fitCenter()
                                        .into(viewHolder.imageViewBackground);
                            } else {
                                Glide.with(viewHolder.itemView)
                                        .setDefaultRequestOptions(new RequestOptions().timeout(30000))
                                        .asBitmap()
                                        .load(R.drawable.no_photo)
                                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                        .placeholder(R.drawable.no_photo)
                                        .error(R.drawable.no_photo) //6
                                        .fallback(R.drawable.no_photo)
                                        .override(2000, 2000)
                                        .fitCenter()
                                        .into(viewHolder.imageViewBackground);
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




//
//
//
//        Call<String> call2 = apiInterface_image.GetImage("getImagecompany",goodView.getGoodCode().toString(),0);
//        call2.enqueue(new Callback<String>() {
//            @Override
//            public void onResponse(Call<String> call2, Response<String> response) {
//                if (response.isSuccessful()) {
//                    assert response.body() != null;
//                    if(!response.body().equals("no_photo")) {
//                        Glide.with(holder.img)
//                                .setDefaultRequestOptions(new RequestOptions().timeout(30000))
//                                .asBitmap()
//                                .load(Base64.decode(response.body(), Base64.DEFAULT))
//                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
//                                .placeholder(R.drawable.no_photo)
//                                .error(R.drawable.no_photo) //6
//                                .fallback(R.drawable.no_photo)
//                                .override(2000, 2000)
//                                .fitCenter()
//                                .into(holder.img);
//                    }else {
//                        Glide.with(holder.img)
//                                .setDefaultRequestOptions(new RequestOptions().timeout(30000))
//                                .asBitmap()
//                                .load(R.drawable.no_photo)
//                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
//                                .placeholder(R.drawable.no_photo)
//                                .error(R.drawable.no_photo) //6
//                                .fallback(R.drawable.no_photo)
//                                .override(2000, 2000)
//                                .fitCenter()
//                                .into(holder.img);
//                    }
//                }
//            }
//            @Override
//            public void onFailure(Call<String> call2, Throwable t) {
//                Log.e("onFailure",""+t.toString());
//            }
//        });
//



        viewHolder.fl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_zome_view();
            }
        });


    }

    @Override
    public int getCount() {
        return img_count;
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        FrameLayout fl;
        public SliderAdapterVH(View itemView) {
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
        SliderAdapter adapter = new SliderAdapter(mcontext,code,img_count);
        sliderView.setSliderAdapter(adapter);
        sliderView.setIndicatorAnimation(IndicatorAnimations.SCALE); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        dialog.show();


    }

}
