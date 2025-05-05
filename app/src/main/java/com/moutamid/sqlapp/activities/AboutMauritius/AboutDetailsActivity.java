package com.moutamid.sqlapp.activities.AboutMauritius;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.moutamid.sqlapp.R;
import com.moutamid.sqlapp.activities.AppInfo.AppInfoActivity;
import com.moutamid.sqlapp.activities.ContactUs.ContactUsActivity;
import com.moutamid.sqlapp.activities.DashboardActivity;
import com.moutamid.sqlapp.activities.Explore.ExploreActivity;
import com.moutamid.sqlapp.activities.Iteneraries.ItinerariesActivity;
import com.moutamid.sqlapp.activities.MyTripsActivity;
import com.moutamid.sqlapp.activities.Organizer.OrganizerActivity;
import com.moutamid.sqlapp.activities.TravelTipsActivity;
import com.moutamid.sqlapp.helper.Utils;
import com.moutamid.sqlapp.model.BeacModel;

import java.io.File;

public class AboutDetailsActivity extends AppCompatActivity {

    @Override
protected void onCreate(Bundle savedInstanceState) {
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_details);

        Utils.loginBtnMenuListener(this);

        LinearLayout map_layout = findViewById(R.id.map_layout);
        ImageView mainImg = findViewById(R.id.main_img);
        ImageView menu = findViewById(R.id.menu);
        ImageView mainImage = findViewById(R.id.main_image);
        ImageView add = findViewById(R.id.add);
        ImageView map = findViewById(R.id.map);
        TextView text1 = findViewById(R.id.text1);
        TextView beach_header = findViewById(R.id.beach_header);
        TextView title1 = findViewById(R.id.title1);
        ImageView image1 = findViewById(R.id.image1);
        ImageView remove = findViewById(R.id.remove);
        TextView text2 = findViewById(R.id.text2);
        TextView title2 = findViewById(R.id.title2);
        ImageView image2 = findViewById(R.id.image2);
        TextView text3 = findViewById(R.id.text3);
        TextView title3 = findViewById(R.id.title3);
        ImageView image3 = findViewById(R.id.image3);
        TextView text4 = findViewById(R.id.text4);
        TextView title4 = findViewById(R.id.title4);
        ImageView image4 = findViewById(R.id.image4);
        TextView text5 = findViewById(R.id.text5);
        TextView title5 = findViewById(R.id.title5);
        ImageView image5 = findViewById(R.id.image5);
        TextView text6 = findViewById(R.id.text6);
        TextView title6 = findViewById(R.id.title6);
        TextView text7 = findViewById(R.id.text7);
        TextView title7 = findViewById(R.id.title7);
        TextView text8 = findViewById(R.id.text8);
        TextView title8 = findViewById(R.id.title8);
        TextView text9 = findViewById(R.id.text9);
        TextView text10 = findViewById(R.id.text10);
        TextView text11 = findViewById(R.id.text11);
        TextView text12 = findViewById(R.id.text12);
        TextView add_not = findViewById(R.id.add_not);
//

        BeacModel model = (BeacModel) Stash.getObject("model", BeacModel.class);
        Log.d("model", model.text1 + " test");
        beach_header.setText(Html.fromHtml(model.title));
        Glide.with(this)
                .load(new File(model.main_image))
                .into(mainImage);
        if (!model.text1.isEmpty()) {
            text1.setText(Html.fromHtml(model.text1));
            text1.setVisibility(View.VISIBLE);
        } else {
            text1.setVisibility(View.GONE);
        }

        if (!model.title1.isEmpty()) {
            title1.setText(Html.fromHtml(model.title1));
            title1.setVisibility(View.VISIBLE);
        } else {

            title1.setVisibility(View.GONE);

        }
        if (!model.image1.equals("")) {
            Glide.with(this)
                    .load(new File(model.image1))
                    .into(image1);
            image1.setVisibility(View.VISIBLE);
        } else {
            image1.setVisibility(View.GONE);
        }

        if (!model.text2.isEmpty()) {
            text2.setText(Html.fromHtml(model.text2));
            text2.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            text2.setVisibility(View.GONE);
        }

        if (!model.title2.isEmpty()) {
            title2.setText(Html.fromHtml(model.title2));
            title2.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            title2.setVisibility(View.GONE);
        }

        if (!model.image2.equals("")) {


            Glide.with(this)
                    .load(new File(model.image2))
                    .into(image2);
            image2.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            image2.setVisibility(View.GONE);
        }

        if (!model.text3.isEmpty()) {
            text3.setText(Html.fromHtml(model.text3));
            text3.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            text3.setVisibility(View.GONE);
        }

        if (!model.title3.isEmpty()) {
            title3.setText(Html.fromHtml(model.title3));
            title3.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            title3.setVisibility(View.GONE);
        }

        if (!model.image3.equals("")) {
            Glide.with(this)
                    .load(new File(model.image3))
                    .into(image3);
            image3.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            image3.setVisibility(View.GONE);
        }
        // Set text or image and adjust visibility for the views
        if (!model.text4.isEmpty()) {
            text4.setText(Html.fromHtml(model.text4));
            text4.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            text4.setVisibility(View.GONE);
        }

        if (!model.title4.isEmpty()) {
            title4.setText(Html.fromHtml(model.title4));
            title4.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            title4.setVisibility(View.GONE);
        }

        if (!model.image4.equals("")) {
            Glide.with(this)
                    .load(new File(model.image4))
                    .into(image4);
            image4.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            image4.setVisibility(View.GONE);
        }

        if (!model.text5.isEmpty()) {
            text5.setText(Html.fromHtml(model.text5));
            text5.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            text5.setVisibility(View.GONE);
        }

        if (!model.title5.isEmpty()) {
            title5.setText(Html.fromHtml(model.title5));
            title5.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            title5.setVisibility(View.GONE);
        }
        if (!model.image5.equals("")) {
            Glide.with(this)
                    .load(new File(model.image5))
                    .into(image5);
            image5.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            image5.setVisibility(View.GONE);
        }

        if (!model.text6.isEmpty()) {
            text6.setText(Html.fromHtml(model.text6));
            text6.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            text6.setVisibility(View.GONE);
        }

        if (!model.title6.isEmpty()) {
            title6.setText(Html.fromHtml(model.title6));
            title6.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            title6.setVisibility(View.GONE);
        }
// Set text or image and adjust visibility for the views
        if (!model.text7.isEmpty()) {
            text7.setText(Html.fromHtml(model.text7));
            text7.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            text7.setVisibility(View.GONE);
        }

        if (!model.title7.isEmpty()) {
            title7.setText(Html.fromHtml(model.title7));
            title7.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            title7.setVisibility(View.GONE);
        }

        if (!model.text8.isEmpty()) {
            text8.setText(Html.fromHtml(model.text8));
            text8.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            text8.setVisibility(View.GONE);
        }

        if (!model.title8.isEmpty()) {
            title8.setText(Html.fromHtml(model.title8));
            title8.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            title8.setVisibility(View.GONE);
        }

        if (!model.text9.isEmpty()) {
            text9.setText(Html.fromHtml(model.text9));
            text9.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            text9.setVisibility(View.GONE);
        }

        if (!model.text10.isEmpty()) {
            text10.setText(Html.fromHtml(model.text10));
            text10.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            text10.setVisibility(View.GONE);
        }

        if (!model.text11.isEmpty()) {
            text11.setText(Html.fromHtml(model.text11));
            text11.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            text11.setVisibility(View.GONE);
        }

        if (!model.text12.isEmpty()) {
            text12.setText(Html.fromHtml(model.text12));
            text12.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            text12.setVisibility(View.GONE);
        }


    }

    public void BackPress(View view) {
        onBackPressed();
    }

    public void menu(View view) {
        LinearLayout more_layout;
        more_layout = findViewById(R.id.more_layout);
        ImageView menu = findViewById(R.id.menu);
        ImageView close = findViewById(R.id.close);
        menu.setVisibility(View.GONE);
        more_layout.setVisibility(View.VISIBLE);
        close.setVisibility(View.VISIBLE);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.setVisibility(View.VISIBLE);
                more_layout.setVisibility(View.GONE);
                close.setVisibility(View.GONE);
            }
        });
    }

    public void explore(View view) {
        startActivity(new Intent(AboutDetailsActivity.this, ExploreActivity.class));
    }

    public void my_trips(View view) {
        startActivity(new Intent(AboutDetailsActivity.this, MyTripsActivity.class));
    }

    public void iternties(View view) {
        startActivity(new Intent(AboutDetailsActivity.this, ItinerariesActivity.class));

    }

    public void organier(View view) {
        startActivity(new Intent(AboutDetailsActivity.this, OrganizerActivity.class));
    }

    public void contact_us(View view) {
        startActivity(new Intent(AboutDetailsActivity.this, ContactUsActivity.class));
    }


    public void tips(View view) {
        startActivity(new Intent(AboutDetailsActivity.this, TravelTipsActivity.class));

    }

    public void about(View view) {
        startActivity(new Intent(AboutDetailsActivity.this, AppInfoActivity.class));

    }

   /* public void login(View view) {
        startActivity(new Intent(AboutDetailsActivity.this, LoginActivity.class));
    }*/

    public void home(View view) {
        startActivity(new Intent(AboutDetailsActivity.this, DashboardActivity.class));

    }
}
