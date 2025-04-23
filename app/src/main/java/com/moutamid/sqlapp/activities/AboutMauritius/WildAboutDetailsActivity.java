package com.moutamid.sqlapp.activities.AboutMauritius;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.moutamid.sqlapp.R;
import com.moutamid.sqlapp.activities.AppInfo.AppInfoActivity;
import com.moutamid.sqlapp.activities.ContactUs.ContactUsActivity;
import com.moutamid.sqlapp.activities.DashboardActivity;
import com.moutamid.sqlapp.activities.Explore.ExploreActivity;
import com.moutamid.sqlapp.activities.Iteneraries.ItinerariesActivity;
import com.moutamid.sqlapp.activities.LoginActivity;
import com.moutamid.sqlapp.activities.MyTripsActivity;
import com.moutamid.sqlapp.activities.Organizer.OrganizerActivity;
import com.moutamid.sqlapp.activities.TravelTipsActivity;
import com.moutamid.sqlapp.helper.Utils;
import com.moutamid.sqlapp.model.BeacModel;

import java.io.File;

public class WildAboutDetailsActivity extends AppCompatActivity {
ImageView mauritius_wildlife, monkey_mauritius, pink_pigeon_image, mauritius_fody, geko, shrimps, lantana, black_river;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wild_about_details);
        Utils.loginBtnMenuListener(this);
        
        File cacheDir = new File(getFilesDir(), "cached_images");
        String fullPath = cacheDir.getAbsolutePath();


        mauritius_wildlife = findViewById(R.id.mauritius_wildlife);
        monkey_mauritius = findViewById(R.id.monkey_mauritius);
        pink_pigeon_image = findViewById(R.id.pink_pigeon_image);
        mauritius_fody = findViewById(R.id.mauritius_fody);
        geko = findViewById(R.id.geko);
        shrimps = findViewById(R.id.shrimps);
        lantana = findViewById(R.id.lantana);
        black_river = findViewById(R.id.black_river);

        Glide.with(this).load(new File(fullPath + "/mauritius_wildlife.jpg")).into(mauritius_wildlife);
        Glide.with(this).load(new File(fullPath + "/monkey_mauritius.jpg")).into(monkey_mauritius);
        Glide.with(this).load(new File(fullPath + "/pink_pigeon_image.jpg")).into(pink_pigeon_image);
        Glide.with(this).load(new File(fullPath + "/mauritius_fody.jpg")).into(mauritius_fody);
        Glide.with(this).load(new File(fullPath + "/geko.jpg")).into(geko);
        Glide.with(this).load(new File(fullPath + "/shrimps.jpg")).into(shrimps);
        Glide.with(this).load(new File(fullPath + "/lantana.jpg")).into(lantana);
        Glide.with(this).load(new File(fullPath + "/black_river.jpg")).into(black_river);

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
        startActivity(new Intent(WildAboutDetailsActivity.this, ExploreActivity.class));
    }

    public void my_trips(View view) {
        startActivity(new Intent(WildAboutDetailsActivity.this, MyTripsActivity.class));
    }

    public void iternties(View view) {
        startActivity(new Intent(WildAboutDetailsActivity.this, ItinerariesActivity.class));

    }

    public void organier(View view) {
        startActivity(new Intent(WildAboutDetailsActivity.this, OrganizerActivity.class));
    }

    public void contact_us(View view) {
        startActivity(new Intent(WildAboutDetailsActivity.this, ContactUsActivity.class));
    }


    public void tips(View view) {
        startActivity(new Intent(WildAboutDetailsActivity.this, TravelTipsActivity.class));

    }

    public void about(View view) {
        startActivity(new Intent(WildAboutDetailsActivity.this, AppInfoActivity.class));

    }

  /*  public void login(View view) {
        startActivity(new Intent(WildAboutDetailsActivity.this, LoginActivity.class));
    }*/

    public void home(View view) {
        startActivity(new Intent(WildAboutDetailsActivity.this, DashboardActivity.class));

    }

}
