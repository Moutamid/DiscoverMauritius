package com.moutamid.sqlapp.activities.InAppPurchase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.moutamid.sqlapp.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.io.File;
import java.util.List;

public class SliderAdapterExample extends SliderViewAdapter<SliderAdapterExample.SliderAdapterVH> {

    private Context context;
    private List<String> mSliderItems;

    public SliderAdapterExample(Context context, List<String> sliderItems) {
        this.context = context;
        this.mSliderItems = sliderItems;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {
        String sliderItem = mSliderItems.get(position);
        Glide.with(context)
                .load(new File(sliderItem))
                .into(viewHolder.imageViewBackground);
    }

    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    static class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            this.itemView = itemView;
        }
    }
}
