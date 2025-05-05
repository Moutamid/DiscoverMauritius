package com.moutamid.sqlapp.activities.Organizer;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.AppCompatActivity;import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.moutamid.sqlapp.R;
import com.moutamid.sqlapp.activities.AppInfo.AppInfoActivity;
import com.moutamid.sqlapp.activities.Calender.calenderapp.MainActivity;
import com.moutamid.sqlapp.activities.ContactUs.ContactUsActivity;
import com.moutamid.sqlapp.activities.DashboardActivity;
import com.moutamid.sqlapp.activities.Explore.ExploreActivity;
import com.moutamid.sqlapp.activities.Iteneraries.ItinerariesActivity;
import com.moutamid.sqlapp.activities.MyTripsActivity;
import com.moutamid.sqlapp.activities.Organizer.Fragment.DocumentFragment;
import com.moutamid.sqlapp.activities.TravelTipsActivity;
import com.moutamid.sqlapp.helper.Utils;

public class MyDocsActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    @Override
protected void onCreate(Bundle savedInstanceState) {
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_docs);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        replaceFragment(new DocumentFragment());
        bottomNavigationView.setSelectedItemId(R.id.doc);
        Utils.loginBtnMenuListener(this);

       bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int menuItemId = item.getItemId();

                if (menuItemId == R.id.home) {
                    startActivity(new Intent(MyDocsActivity.this, DashboardActivity.class));
                    return true;
                } else if (menuItemId == R.id.doc) {
                    replaceFragment(new DocumentFragment());
                    return true;
                } else if (menuItemId == R.id.calender_menu) {
                    startActivity(new Intent(MyDocsActivity.this, MainActivity.class));
                    return true;
                }

                return true;
            }
        });
     }

    public void BackPress(View view) {
        onBackPressed();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();


    }

    public void menu(View view) {
        LinearLayout more_layout;
        more_layout = findViewById(R.id.more_layout);
        ImageView menu = findViewById(R.id.menu);
        ImageView close = findViewById(R.id.closebtn);
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
        startActivity(new Intent(MyDocsActivity.this, ExploreActivity.class));
    }

    public void my_trips(View view) {
        startActivity(new Intent(MyDocsActivity.this, MyTripsActivity.class));
    }

    public void iternties(View view) {
        startActivity(new Intent(MyDocsActivity.this, ItinerariesActivity.class));

    }

    public void organier(View view) {
        startActivity(new Intent(MyDocsActivity.this, OrganizerActivity.class));
    }

    public void contact_us(View view) {
        startActivity(new Intent(MyDocsActivity.this, ContactUsActivity.class));
    }


    public void tips(View view) {
        startActivity(new Intent(MyDocsActivity.this, TravelTipsActivity.class));

    }

    public void about(View view) {
        startActivity(new Intent(MyDocsActivity.this, AppInfoActivity.class));

    }

  /*  public void login(View view) {
        startActivity(new Intent(MyDocsActivity.this, LoginActivity.class));
    }*/

    public void home(View view) {
        startActivity(new Intent(MyDocsActivity.this, DashboardActivity.class));

    }
}