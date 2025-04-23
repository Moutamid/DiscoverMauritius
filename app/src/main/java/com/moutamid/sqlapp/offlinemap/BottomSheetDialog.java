package com.moutamid.sqlapp.offlinemap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.moutamid.sqlapp.R;

public class BottomSheetDialog extends BottomSheetDialogFragment {
    private ImageView image1, image2, image3;
    private TextView textView1, textView2, textView3;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
    ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet,
                container, false);
      ImageView close_button = v.findViewById(R.id.close_button);
        // Initialize ImageViews and TextViews
        image1 =  v.findViewById(R.id.image1);
        image2 =  v.findViewById(R.id.image2);
        image3 =  v.findViewById(R.id.image3);

        textView1 =  v.findViewById(R.id.text_view1);
        textView2 =  v.findViewById(R.id.text_view2);
        textView3 =  v.findViewById(R.id.text_view3);

        // Set click listeners
        setClickListener(image1, textView1, image2, textView2, image3, textView3);
        setClickListener(image2, textView2, image1, textView1, image3, textView3);
        setClickListener(image3, textView3, image1, textView1, image2, textView2);


        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });
        return v;
    }
    private void setClickListener(final ImageView clickedImage, final TextView clickedText,
                                  final ImageView otherImage1, final TextView otherText1,
                                  final ImageView otherImage2, final TextView otherText2) {
        clickedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Make the clicked image and text active
                clickedImage.setImageResource(R.drawable.grey_map_bg_outline);
                clickedText.setTextColor(getResources().getColor(R.color.active_color));

                // Make the other images and texts inactive
                otherImage1.setImageResource(R.drawable.grey_map_bg);
                otherText1.setTextColor(getResources().getColor(R.color.inactive_color));

                otherImage2.setImageResource(R.drawable.grey_map_bg);
                otherText2.setTextColor(getResources().getColor(R.color.inactive_color));
            }
        });
    }
}
