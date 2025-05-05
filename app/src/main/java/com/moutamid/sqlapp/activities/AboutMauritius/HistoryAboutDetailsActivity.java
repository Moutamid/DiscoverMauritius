package com.moutamid.sqlapp.activities.AboutMauritius;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
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

import java.io.File;

public class HistoryAboutDetailsActivity extends AppCompatActivity {
ImageView mauritius_history, jacques_henri_bernardin, isle_of_france, indian_labourers_arriving_in_mauritius, stamp_mauritius;
    @Override
protected void onCreate(Bundle savedInstanceState) {
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_about_details);
        Utils.loginBtnMenuListener(this);

        File cacheDir = new File(getFilesDir(), "cached_images");
        String fullPath = cacheDir.getAbsolutePath();

        mauritius_history = findViewById(R.id.mauritius_history);
        jacques_henri_bernardin = findViewById(R.id.jacques_henri_bernardin);
        isle_of_france = findViewById(R.id.isle_of_france);
        indian_labourers_arriving_in_mauritius = findViewById(R.id.indian_labourers_arriving_in_mauritius);
        stamp_mauritius = findViewById(R.id.stamp_mauritius);

// Load images using Glide
        Glide.with(this).load(new File(fullPath + "/mauritius_history.jpg")).into(mauritius_history);
        Glide.with(this).load(new File(fullPath + "/jacques_henri_bernardin.jpg")).into(jacques_henri_bernardin);
        Glide.with(this).load(new File(fullPath + "/isle_of_france.jpg")).into(isle_of_france);
        Glide.with(this).load(new File(fullPath + "/indian_labourers_arriving_in_mauritius.jpg")).into(indian_labourers_arriving_in_mauritius);
        Glide.with(this).load(new File(fullPath + "/stamp_mauritius.jpg")).into(stamp_mauritius);


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
        startActivity(new Intent(HistoryAboutDetailsActivity.this, ExploreActivity.class));
    }

    public void my_trips(View view) {
        startActivity(new Intent(HistoryAboutDetailsActivity.this, MyTripsActivity.class));
    }

    public void iternties(View view) {
        startActivity(new Intent(HistoryAboutDetailsActivity.this, ItinerariesActivity.class));

    }

    public void organier(View view) {
        startActivity(new Intent(HistoryAboutDetailsActivity.this, OrganizerActivity.class));
    }

    public void contact_us(View view) {
        startActivity(new Intent(HistoryAboutDetailsActivity.this, ContactUsActivity.class));
    }


    public void tips(View view) {
        startActivity(new Intent(HistoryAboutDetailsActivity.this, TravelTipsActivity.class));

    }

    public void about(View view) {
        startActivity(new Intent(HistoryAboutDetailsActivity.this, AppInfoActivity.class));

    }

    /*public void login(View view) {
        startActivity(new Intent(HistoryAboutDetailsActivity.this, LoginActivity.class));
    }*/

    public void home(View view) {
        startActivity(new Intent(HistoryAboutDetailsActivity.this, DashboardActivity.class));

    }

}
