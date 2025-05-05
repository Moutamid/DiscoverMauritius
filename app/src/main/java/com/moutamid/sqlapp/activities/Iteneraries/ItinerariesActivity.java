package com.moutamid.sqlapp.activities.Iteneraries;

import static android.view.View.GONE;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.AppCompatActivity;
import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.ProductDetailsResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.QueryPurchasesParams;
import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.moutamid.sqlapp.R;
import com.moutamid.sqlapp.activities.AppInfo.AppInfoActivity;
import com.moutamid.sqlapp.activities.ContactUs.ContactUsActivity;
import com.moutamid.sqlapp.activities.CreateAccountActivity;
import com.moutamid.sqlapp.activities.DashboardActivity;
import com.moutamid.sqlapp.activities.Explore.ExploreActivity;
import com.moutamid.sqlapp.activities.InAppPurchase.SliderAdapterExample;
import com.moutamid.sqlapp.activities.MyTripsActivity;
import com.moutamid.sqlapp.activities.Organizer.OrganizerActivity;
import com.moutamid.sqlapp.activities.TravelTipsActivity;
import com.moutamid.sqlapp.adapter.ItenerariesAdapter;
import com.moutamid.sqlapp.helper.Constants;
import com.moutamid.sqlapp.helper.Utils;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ItinerariesActivity extends AppCompatActivity implements View.OnClickListener {
    public static LinearLayout premium_layout, faq_layout;
    RelativeLayout lifetime_premium;
    TextView restore_purchase;
    SliderView sliderView;
    SliderAdapterExample adapter;

    ImageView image, close, close_faq;
    public static TextView faq_txt, text1, text2;

    private static final int BUTTON_DAY_1 = 1;
    private static final int BUTTON_DAY_2 = 2;
    private static final int BUTTON_DAY_3 = 3;
    private static final int BUTTON_DAY_4 = 4;
    private static final int BUTTON_DAY_5 = 5;
    private static int BUTTON_DAY = 1;

    double[] itemLatitudes;
    double[] itemLongitudes;
    TextView buttonDay1, buttonDay2, buttonDay3, buttonDay4, buttonDay5;
    View view1, view2, view3, view4, view5;
    TextView pressButtonDay1, pressButtonDay2, pressButtonDay3, pressButtonDay4, pressButtonDay5;
    View pressView1, pressView2, pressView3, pressView4, pressView5;
String fullPath;
    private TextView subbuttonDay1, subbuttonDay2, subbuttonDay3, subbuttonDay4, subbuttonDay5;
    private View subview1, subview2, subview3, subview4, subview5;
    private TextView subpressbuttonDay1, subpressbuttonDay2, subpressbuttonDay3, subpressbuttonDay4, subpressbuttonDay5;
    private View subpressview1, subpressview2, subpressview3, subpressview4, subpressview5;
    RelativeLayout tab_layout2;
    public static TextView total_stop, time, distance;
    EditText search;
    private BillingClient billingClient;
    private final PurchasesUpdatedListener purchasesUpdatedListener = (billingResult, purchases) -> {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (Purchase purchase : purchases) {
                if (purchase.getProducts().contains(Constants.PRODUCT_PREMIUM)) {
                    // Grant the user access
                    Toast.makeText(this, "Lifetime purchase successful!", Toast.LENGTH_SHORT).show();
                    handlePurchase(purchase);
                }
            }
        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            Toast.makeText(this, "Purchase canceled", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error: " + billingResult.getDebugMessage(), Toast.LENGTH_SHORT).show();
        }
    };


    @Override
protected void onCreate(Bundle savedInstanceState) {
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itineraries);

        premium();
        Utils.loginBtnMenuListener(this);
        billingClient = BillingClient.newBuilder(this)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases()
                .build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    Toast.makeText(ItinerariesActivity.this, "Billing connected", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                Toast.makeText(ItinerariesActivity.this, "Billing disconnected", Toast.LENGTH_SHORT).show();
            }
        });  total_stop = findViewById(R.id.total_stop);
        search = findViewById(R.id.search);
        time = findViewById(R.id.time);
        distance = findViewById(R.id.distance);
        image = findViewById(R.id.image);
        buttonDay1 = findViewById(R.id.buttonDay1);
        buttonDay2 = findViewById(R.id.buttonDay2);
        buttonDay3 = findViewById(R.id.buttonDay3);
        buttonDay4 = findViewById(R.id.buttonDay4);
        buttonDay5 = findViewById(R.id.buttonDay5);

        pressButtonDay1 = findViewById(R.id.pressbuttonDay1);
        pressButtonDay2 = findViewById(R.id.pressbuttonDay2);
        pressButtonDay3 = findViewById(R.id.pressbuttonDay3);
        pressButtonDay4 = findViewById(R.id.pressbuttonDay4);
        pressButtonDay5 = findViewById(R.id.pressbuttonDay5);

        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        view3 = findViewById(R.id.view3);
        view4 = findViewById(R.id.view4);
        view5 = findViewById(R.id.view5);

        pressView1 = findViewById(R.id.pressview1);
        pressView2 = findViewById(R.id.pressview2);
        pressView3 = findViewById(R.id.pressview3);
        pressView4 = findViewById(R.id.pressview4);
        pressView5 = findViewById(R.id.pressview5);

        subbuttonDay1 = findViewById(R.id.subbuttonDay1);
        subbuttonDay2 = findViewById(R.id.subbuttonDay2);
        subbuttonDay3 = findViewById(R.id.subbuttonDay3);
        subbuttonDay4 = findViewById(R.id.subbuttonDay4);
        subbuttonDay5 = findViewById(R.id.subbuttonDay5);

        subview1 = findViewById(R.id.subview1);
        subview2 = findViewById(R.id.subview2);
        subview3 = findViewById(R.id.subview3);
        subview4 = findViewById(R.id.subview4);
        subview5 = findViewById(R.id.subview5);

        subpressbuttonDay1 = findViewById(R.id.subpressbuttonDay1);
        subpressbuttonDay2 = findViewById(R.id.subpressbuttonDay2);
        subpressbuttonDay3 = findViewById(R.id.subpressbuttonDay3);
        subpressbuttonDay4 = findViewById(R.id.subpressbuttonDay4);
        subpressbuttonDay5 = findViewById(R.id.subpressbuttonDay5);

        subpressview1 = findViewById(R.id.subpressview1);
        subpressview2 = findViewById(R.id.subpressview2);
        subpressview3 = findViewById(R.id.subpressview3);
        subpressview4 = findViewById(R.id.subpressview4);
        subpressview5 = findViewById(R.id.subpressview5);
        tab_layout2 = findViewById(R.id.tab_layout2);
        fullPath = getFilesDir().getAbsolutePath() + "/cached_images";
        Glide.with(this).load(new File(fullPath + "/belle_mare_1.jpg")).into(image);

        // Set click listeners for all buttons
        subbuttonDay1.setOnClickListener(this);
        subbuttonDay2.setOnClickListener(this);
        subbuttonDay3.setOnClickListener(this);
        subbuttonDay4.setOnClickListener(this);
        subbuttonDay5.setOnClickListener(this);

        subpressbuttonDay1.setOnClickListener(this);
        subpressbuttonDay2.setOnClickListener(this);
        subpressbuttonDay3.setOnClickListener(this);
        subpressbuttonDay4.setOnClickListener(this);
        subpressbuttonDay5.setOnClickListener(this);

        buttonDay1.setOnClickListener(this);
        buttonDay2.setOnClickListener(this);
        buttonDay3.setOnClickListener(this);
        buttonDay4.setOnClickListener(this);
        buttonDay5.setOnClickListener(this);

        pressButtonDay1.setOnClickListener(this);
        pressButtonDay2.setOnClickListener(this);
        pressButtonDay3.setOnClickListener(this);
        pressButtonDay4.setOnClickListener(this);
        pressButtonDay5.setOnClickListener(this);

        // Initialize views
        resetButtons();
        tab_layout2.setVisibility(GONE);
        buttonDay1.setVisibility(View.INVISIBLE);
        view1.setVisibility(View.INVISIBLE);
        pressButtonDay1.setVisibility(View.VISIBLE);
        pressView1.setVisibility(View.VISIBLE);

        search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (Stash.getBoolean(Constants.IS_PREMIUM, false)) {

                } else {
                    premium_layout.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });
        // Initialize list view
        String[] itemTexts = {
                "Le Caudan Waterfront",//1
                "Aapravasi Ghat",//2
                "Port Louis Central Market",//3
                "Marie Reine de la Paix Chapel",//4
                "Fort Adelaide",//5
                "Le Morne Beach",//6
                "Le Morne Brabant",//7
                "Maconde Viewpoint", //8
                "Seven Coloured Earths",//9
                "Chamarel Waterfalls",//10
                "Black River Gorges", //11
                "Grand Bassin", //12
                "Gris Gris Beach"//13
        };
        String[] itemName = {"Admission Free", "Admission Entrance Fee", "Admission Free", "Admission Free", "Admission Free", "Admission Free", "Admission Free", "Admission Free", "Admission Free", "Admission Entrance Fee", "Chamarel", "Admission Free", "Admission Free"};
        String[] itemName1 = {"South • 30 minutes - 1 hour", "North • 1 hour", "North • 1 hour", "North • 45 minutes", "North • 1 hour", "Southwest • 1 hour", "Southwest • 3 - 4 hours", "Southwest • 30 minutes", "Southwest • 1 hour 30 minutes", "Southwest • 1 hour 30 minutes", "Southwest • 1 hour 30 minutes", "South • 1 hour 30 minutes", "South • 30 minutes - 1 hour"};
      String[] itemImages = new String[]{
                fullPath + "/port_louis_3.jpg",
                fullPath + "/aapravasi_ghat_1.jpg",
                fullPath + "/port_louis_4.jpg",
                fullPath + "/marie_reine_de_la_paix_3.jpg",
                fullPath + "/citadelle.jpg",
                fullPath + "/le_morne_beach_2.jpg",
                fullPath + "/le_morne_1.jpg",
                fullPath + "/maconde_1.jpg",
                fullPath + "/chamarel_2.jpg",
                fullPath + "/chamarel_1.jpg",
                fullPath + "/black_river_georges_2.jpg",
                fullPath + "/grand_bassin_1.jpg",
                fullPath + "/gris_gris_1.jpg"
        };  itemLatitudes = new double[]{
                -20.1608170,//1
                -20.1586888,//2
                -20.1606798,//3
                -20.1704784,//4
                -20.1637132,//5
                -20.4499767,//6
                -20.45306,//7
                -20.4911178,//8
                -20.4401637,//9
                -20.44373,//10
                -20.408077,//11
                -20.4167126,//12
                -20.5245931//13
        };

        itemLongitudes = new double[]{
                57.4980775,//1
                57.5029467,//2
                57.5029272,//3
                57.4962069,//4
                57.5103489,//5
                57.3165315,//6
                57.32442,//7
                57.3711084,//8
                57.3733048,//9
                57.38386,//10
                57.473234,//11
                57.4933252,//12
                57.5303065//13
        };
        ListView listView = findViewById(R.id.listView);
        ItenerariesAdapter adapter = new ItenerariesAdapter(ItinerariesActivity.this, itemTexts, itemName, itemName1, itemImages, itemLatitudes, itemLongitudes);
        listView.setAdapter(adapter);
    }

    public void BackPress(View view) {
        onBackPressed();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        // Handle normal buttons and views
        if (id == R.id.buttonDay1 || id == R.id.buttonDay2 || id == R.id.buttonDay3 || id == R.id.buttonDay4 || id == R.id.buttonDay5) {
            resetButtons(); // Reset all normal buttons and views
            toggleButtonsVisibility(id); // Toggle visibility for the clicked button and view
        } else if (id == R.id.pressbuttonDay1 || id == R.id.pressbuttonDay2 || id == R.id.pressbuttonDay3 || id == R.id.pressbuttonDay4 || id == R.id.pressbuttonDay5) {
            toggleButtonsVisibility(id); // Toggle visibility for the clicked button and view

        }

        // Handle sub buttons and views
        if (id == R.id.subbuttonDay1 || id == R.id.subbuttonDay2 || id == R.id.subbuttonDay3 || id == R.id.subbuttonDay4 || id == R.id.subbuttonDay5) {
            subresetButtons(); // Reset all sub buttons and views
            toggleSubButtonsVisibility(id); // Toggle visibility for the clicked sub button and view


        } else if (id == R.id.subpressbuttonDay1 || id == R.id.subpressbuttonDay2 || id == R.id.subpressbuttonDay3 || id == R.id.subpressbuttonDay4 || id == R.id.subpressbuttonDay5) {
            toggleSubButtonsVisibility(id); // Toggle visibility for the clicked sub button and view
        }
    }

    // Toggle visibility for normal buttons and views
    private void toggleButtonsVisibility(int buttonId) {
        if (buttonId == R.id.buttonDay1) {
            buttonDay1.setVisibility(View.INVISIBLE);
            view1.setVisibility(View.INVISIBLE);
            pressButtonDay1.setVisibility(View.VISIBLE);
            pressView1.setVisibility(View.VISIBLE);
            showSubButtons(1);
            tab_layout2.setVisibility(GONE);
            handle_sub_visibility(0, 0);

        } else if (buttonId == R.id.buttonDay2) {
            buttonDay2.setVisibility(View.INVISIBLE);
            view2.setVisibility(View.INVISIBLE);
            pressButtonDay2.setVisibility(View.VISIBLE);
            pressView2.setVisibility(View.VISIBLE);
            showSubButtons(2);
            tab_layout2.setVisibility(View.VISIBLE);
            handle_sub_visibility(2, 1);


        } else if (buttonId == R.id.buttonDay3) {
            buttonDay3.setVisibility(View.INVISIBLE);
            view3.setVisibility(View.INVISIBLE);
            pressButtonDay3.setVisibility(View.VISIBLE);
            pressView3.setVisibility(View.VISIBLE);
            showSubButtons(3);
            tab_layout2.setVisibility(View.VISIBLE);
            handle_sub_visibility(3, 1);


        } else if (buttonId == R.id.buttonDay4) {
            buttonDay4.setVisibility(View.INVISIBLE);
            view4.setVisibility(View.INVISIBLE);
            pressButtonDay4.setVisibility(View.VISIBLE);
            pressView4.setVisibility(View.VISIBLE);
            showSubButtons(4);
            tab_layout2.setVisibility(View.VISIBLE);
            handle_sub_visibility(4, 1);


        } else if (buttonId == R.id.buttonDay5) {
            buttonDay5.setVisibility(View.INVISIBLE);
            view5.setVisibility(View.INVISIBLE);
            pressButtonDay5.setVisibility(View.VISIBLE);
            pressView5.setVisibility(View.VISIBLE);
            showSubButtons(5);
            tab_layout2.setVisibility(View.VISIBLE);
            handle_sub_visibility(5, 1);


        }
    }

    // Reset all normal buttons and views
    private void resetButtons() {
        buttonDay1.setVisibility(View.VISIBLE);
        view1.setVisibility(View.VISIBLE);
        pressButtonDay1.setVisibility(View.INVISIBLE);
        pressView1.setVisibility(View.INVISIBLE);

        buttonDay2.setVisibility(View.VISIBLE);
        view2.setVisibility(View.VISIBLE);
        pressButtonDay2.setVisibility(View.INVISIBLE);
        pressView2.setVisibility(View.INVISIBLE);

        buttonDay3.setVisibility(View.VISIBLE);
        view3.setVisibility(View.VISIBLE);
        pressButtonDay3.setVisibility(View.INVISIBLE);
        pressView3.setVisibility(View.INVISIBLE);

        buttonDay4.setVisibility(View.VISIBLE);
        view4.setVisibility(View.VISIBLE);
        pressButtonDay4.setVisibility(View.INVISIBLE);
        pressView4.setVisibility(View.INVISIBLE);

        buttonDay5.setVisibility(View.VISIBLE);
        view5.setVisibility(View.VISIBLE);
        pressButtonDay5.setVisibility(View.INVISIBLE);
        pressView5.setVisibility(View.INVISIBLE);
    }

    // Toggle visibility for sub buttons and views
    private void toggleSubButtonsVisibility(int id) {

        if (id == R.id.subbuttonDay1) {
            handle_sub_visibility(BUTTON_DAY, 1);

        } else if (id == R.id.subbuttonDay2) {
            handle_sub_visibility(BUTTON_DAY, 2);

        } else if (id == R.id.subbuttonDay3) {
            handle_sub_visibility(BUTTON_DAY, 3);

        } else if (id == R.id.subbuttonDay4) {
            handle_sub_visibility(BUTTON_DAY, 4);

        } else if (id == R.id.subbuttonDay5) {
            handle_sub_visibility(BUTTON_DAY, 5);

        }
    }

    // Reset all sub buttons and views
    private void subresetButtons() {
        subbuttonDay1.setVisibility(View.VISIBLE);
        subbuttonDay2.setVisibility(View.VISIBLE);
        subbuttonDay3.setVisibility(View.VISIBLE);
        subbuttonDay4.setVisibility(View.VISIBLE);
        subbuttonDay5.setVisibility(View.VISIBLE);

        subview1.setVisibility(View.VISIBLE);
        subview2.setVisibility(View.VISIBLE);
        subview3.setVisibility(View.VISIBLE);
        subview4.setVisibility(View.VISIBLE);
        subview5.setVisibility(View.VISIBLE);

        subpressbuttonDay1.setVisibility(View.INVISIBLE);
        subpressbuttonDay2.setVisibility(View.INVISIBLE);
        subpressbuttonDay3.setVisibility(View.INVISIBLE);
        subpressbuttonDay4.setVisibility(View.INVISIBLE);
        subpressbuttonDay5.setVisibility(View.INVISIBLE);

        subpressview1.setVisibility(View.INVISIBLE);
        subpressview2.setVisibility(View.INVISIBLE);
        subpressview3.setVisibility(View.INVISIBLE);
        subpressview4.setVisibility(View.INVISIBLE);
        subpressview5.setVisibility(View.INVISIBLE);
    }

    private void showSubButtons(int day) {
        // Show sub buttons based on the selected day
        switch (day) {
            case BUTTON_DAY_1:

            case BUTTON_DAY_2:
                BUTTON_DAY = 2;
                subbuttonDay1.setVisibility(View.INVISIBLE);
                subview1.setVisibility(View.INVISIBLE);
                subbuttonDay2.setVisibility(View.VISIBLE);
                subview2.setVisibility(View.VISIBLE);
                subbuttonDay3.setVisibility(GONE);
                subview3.setVisibility(GONE);
                subbuttonDay4.setVisibility(GONE);
                subview4.setVisibility(GONE);
                subbuttonDay5.setVisibility(GONE);
                subview5.setVisibility(GONE);

                subpressbuttonDay1.setVisibility(View.VISIBLE);
                subpressview1.setVisibility(View.VISIBLE);
                subpressbuttonDay2.setVisibility(View.INVISIBLE);
                subpressview2.setVisibility(View.INVISIBLE);
                subpressbuttonDay3.setVisibility(GONE);
                subpressview3.setVisibility(GONE);
                subpressbuttonDay4.setVisibility(GONE);
                subpressview4.setVisibility(GONE);
                subpressbuttonDay5.setVisibility(GONE);
                subpressview5.setVisibility(GONE);
                break;
            case BUTTON_DAY_3:
                BUTTON_DAY = 3;

                subbuttonDay1.setVisibility(View.INVISIBLE);
                subview1.setVisibility(View.INVISIBLE);
                subbuttonDay2.setVisibility(View.VISIBLE);
                subview2.setVisibility(View.VISIBLE);
                subbuttonDay3.setVisibility(View.VISIBLE);
                subview3.setVisibility(View.VISIBLE);
                subbuttonDay4.setVisibility(GONE);
                subview4.setVisibility(GONE);
                subbuttonDay5.setVisibility(GONE);
                subview5.setVisibility(GONE);

                subpressbuttonDay1.setVisibility(View.VISIBLE);
                subpressview1.setVisibility(View.VISIBLE);
                subpressbuttonDay2.setVisibility(View.INVISIBLE);
                subpressview2.setVisibility(View.INVISIBLE);
                subpressbuttonDay3.setVisibility(View.INVISIBLE);
                subpressview3.setVisibility(View.INVISIBLE);
                subpressbuttonDay4.setVisibility(GONE);
                subpressview4.setVisibility(GONE);
                subpressbuttonDay5.setVisibility(GONE);
                subpressview5.setVisibility(GONE);
                break;
            case BUTTON_DAY_4:
                BUTTON_DAY = 4;

                subbuttonDay1.setVisibility(View.INVISIBLE);
                subview1.setVisibility(View.INVISIBLE);
                subbuttonDay2.setVisibility(View.VISIBLE);
                subview2.setVisibility(View.VISIBLE);
                subbuttonDay3.setVisibility(View.VISIBLE);
                subview3.setVisibility(View.VISIBLE);
                subbuttonDay4.setVisibility(View.VISIBLE);
                subview4.setVisibility(View.VISIBLE);
                subbuttonDay5.setVisibility(GONE);
                subview5.setVisibility(GONE);

                subpressbuttonDay1.setVisibility(View.VISIBLE);
                subpressview1.setVisibility(View.VISIBLE);
                subpressbuttonDay2.setVisibility(View.INVISIBLE);
                subpressview2.setVisibility(View.INVISIBLE);
                subpressbuttonDay3.setVisibility(View.INVISIBLE);
                subpressview3.setVisibility(View.INVISIBLE);
                subpressbuttonDay4.setVisibility(View.INVISIBLE);
                subpressview4.setVisibility(View.INVISIBLE);
                subpressbuttonDay5.setVisibility(GONE);
                subpressview5.setVisibility(GONE);
                break;
            case BUTTON_DAY_5:
                BUTTON_DAY = 5;

                subbuttonDay1.setVisibility(View.INVISIBLE);
                subview1.setVisibility(View.INVISIBLE);
                subbuttonDay2.setVisibility(View.VISIBLE);
                subview2.setVisibility(View.VISIBLE);
                subbuttonDay3.setVisibility(View.VISIBLE);
                subview3.setVisibility(View.VISIBLE);
                subbuttonDay4.setVisibility(View.VISIBLE);
                subview4.setVisibility(View.VISIBLE);
                subbuttonDay5.setVisibility(View.VISIBLE);
                subview5.setVisibility(View.VISIBLE);

                subpressbuttonDay1.setVisibility(View.VISIBLE);
                subpressview1.setVisibility(View.VISIBLE);
                subpressbuttonDay2.setVisibility(View.INVISIBLE);
                subpressview2.setVisibility(View.INVISIBLE);
                subpressbuttonDay3.setVisibility(View.INVISIBLE);
                subpressview3.setVisibility(View.INVISIBLE);
                subpressbuttonDay4.setVisibility(View.INVISIBLE);
                subpressview4.setVisibility(View.INVISIBLE);
                subpressbuttonDay5.setVisibility(View.INVISIBLE);
                subpressview5.setVisibility(View.INVISIBLE);
                break;
        }
    }

    public void handle_sub_visibility(int main_day, int sub_day) {
        String[] itemTexts = new String[0];
        String[] itemName1 = new String[0];
        String[] itemImages = new String[0];
        String[] itemName = new String[0];
        if (main_day == 0 && sub_day == 0) {
            Stash.put("day", "day1");
            tab_layout2.setVisibility(GONE);
            itemTexts = new String[]{
                    "Le Caudan Waterfront", //1
                    "Aapravasi Ghat", //2
                    "Port Louis Central Market", //3
                    "Marie Reine de la Paix Chapel", //4
                    "Fort Adelaide", //5
                    "Le Morne Beach",//6
                    "Le Morne Brabant",//7
                    "Maconde Viewpoint",//8
                    "Seven Coloured Earths",//9
                    "Chamarel Waterfalls",//10
                    "Black River Gorges", //11
                    "Grand Bassin", //12
                    "Gris Gris Beach"//13
            };
            itemName = new String[]{"Admission Free", "Admission Entrance Fee", "Admission Free", "Admission Free", "Admission Free", "Admission Free", "Admission Free", "Admission Free", "Admission Free", "Admission Entrance Fee", "Chamarel", "Admission Free", "Admission Free"};
            itemName1 = new String[]{"South • 30 minutes - 1 hour", "North • 1 hour", "North • 1 hour", "North • 45 minutes", "North • 1 hour", "Southwest • 1 hour", "Southwest • 3 - 4 hours", "Southwest • 30 minutes", "Southwest • 1 hour 30 minutes", "Southwest • 1 hour 30 minutes", "Southwest • 1 hour 30 minutes", "South • 1 hour 30 minutes", "South • 30 minutes - 1 hour"};
           itemImages = new String[]{
                    fullPath + "/port_louis_3.jpg",
                    fullPath + "/aapravasi_ghat_1.jpg",
                    fullPath + "/port_louis_4.jpg",
                    fullPath + "/marie_reine_de_la_paix_3.jpg",
                    fullPath + "/citadelle.jpg",
                    fullPath + "/le_morne_beach_2.jpg",
                    fullPath + "/le_morne_1.jpg",
                    fullPath + "/maconde_1.jpg",
                    fullPath + "/chamarel_2.jpg",
                    fullPath + "/chamarel_1.jpg",
                    fullPath + "/black_river_georges_2.jpg",
                    fullPath + "/grand_bassin_1.jpg",
                    fullPath + "/gris_gris_1.jpg"
            };                     itemLatitudes = new double[]{
                    -20.1608170,//1
                    -20.1586888,//2
                    -20.1606798,//3
                    -20.1704784,//4
                    -20.1637132,//5
                    -20.4499767,//6
                    -20.45306,//7
                    -20.4911178,//8
                    -20.4401637,//9
                    -20.44373,//10
                    -20.408077,//11
                    -20.4167126,//12
                    -20.5245931//13
            };

            itemLongitudes = new double[]{
                    57.4980775,//1
                    57.5029467,//2
                    57.5029272,//3
                    57.4962069,//4
                    57.5103489,//5
                    57.3165315,//6
                    57.32442,//7
                    57.3711084,//8
                    57.3733048,//9
                    57.38386,//10
                    57.473234,//11
                    57.4933252,//12
                    57.5303065//13
            };

        }
        if (main_day == 2 && sub_day == 1) {
            Stash.put("day", "day21");

            tab_layout2.setVisibility(View.VISIBLE);
            subbuttonDay1.setVisibility(View.INVISIBLE);
            subview1.setVisibility(View.INVISIBLE);
            subbuttonDay2.setVisibility(View.VISIBLE);
            subview2.setVisibility(View.VISIBLE);
            subbuttonDay3.setVisibility(GONE);
            subview3.setVisibility(GONE);
            subbuttonDay4.setVisibility(GONE);
            subview4.setVisibility(GONE);
            subbuttonDay5.setVisibility(GONE);
            subview5.setVisibility(GONE);

            subpressbuttonDay1.setVisibility(View.VISIBLE);
            subpressview1.setVisibility(View.VISIBLE);
            subpressbuttonDay2.setVisibility(View.INVISIBLE);
            subpressview2.setVisibility(View.INVISIBLE);
            subpressbuttonDay3.setVisibility(GONE);
            subpressview3.setVisibility(GONE);
            subpressbuttonDay4.setVisibility(GONE);
            subpressview4.setVisibility(GONE);
            subpressbuttonDay5.setVisibility(GONE);
            subpressview5.setVisibility(GONE);
            itemTexts = new String[]{
                    "SSR Botanical Garden", //1
                    "L’Aventure du Sucre Museum", //2
                    "Le Caudan Waterfront",//3
                    "Aapravasi Ghat", //4
                    "Blue Penny Museum",//5
                    "Black River Gorges", //6
                    "Seven Coloured Earths"//7
            };
            itemName = new String[]{"Admission Entrance Fee", "Admission Entrance Fee", "Admission Free", "Admission Entrance Fee", "Admission Entrance Fee", "Admission Free", "Admission Entrance Fee"};
            itemName1 = new String[]{"North • 2 hours", "North • 2 hours", "North • 2 hours", "North • 1 hours", "North • 1 hour", "Southwest • 30 minutes", "Southwest • 1 hour 30 minutes"};
           itemImages = new String[]{
                    fullPath + "/pamplemousse_garden.jpg",
                    fullPath + "/sugar_museum_pamplemousses.jpg",
                    fullPath + "/port_louis_3.jpg",
                    fullPath + "/aapravasi_ghat_1.jpg",
                    fullPath + "/blue_penny_museum_2.jpg",
                    fullPath + "/black_river_georges_2.jpg",
                    fullPath + "/chamarel_2.jpg"
            };      itemLatitudes = new double[]{

                    -20.1042691,//1
                    -20.0978896,//2
                    -20.1608170,//3
                    -20.1586888,//4
                    -20.1609300,//5
                    -20.408077,//6
                    -20.4401637//7
            };

            itemLongitudes = new double[]{

                    57.5799724,//1
                    57.5743742,//2
                    57.4980775,//3
                    57.5029467,//4
                    57.4974394,//5
                    57.473234,//6
                    57.3733048//7
            };

        }
        if (main_day == 2 && sub_day == 2) {
            Stash.put("day", "day22");

            tab_layout2.setVisibility(View.VISIBLE);
            subbuttonDay1.setVisibility(View.VISIBLE);
            subview1.setVisibility(View.VISIBLE);
            subbuttonDay2.setVisibility(View.INVISIBLE);
            subview2.setVisibility(View.INVISIBLE);
            subbuttonDay3.setVisibility(GONE);
            subview3.setVisibility(GONE);
            subbuttonDay4.setVisibility(GONE);
            subview4.setVisibility(GONE);
            subbuttonDay5.setVisibility(GONE);
            subview5.setVisibility(GONE);
            subpressbuttonDay1.setVisibility(View.INVISIBLE);
            subpressview1.setVisibility(View.INVISIBLE);
            subpressbuttonDay2.setVisibility(View.VISIBLE);
            subpressview2.setVisibility(View.VISIBLE);
            subpressbuttonDay3.setVisibility(GONE);
            subpressview3.setVisibility(GONE);
            subpressbuttonDay4.setVisibility(GONE);
            subpressview4.setVisibility(GONE);
            subpressbuttonDay5.setVisibility(GONE);
            subpressview5.setVisibility(GONE);
            itemTexts = new String[]{
                    "Trou aux Cerfs", //1
                    "Grand Bassin", //2
                    "Bois Cheri Tea Museum",//3
                    "La Vanille Nature Park",//4
                    "Gris Gris Beach"//5
            };
            itemName = new String[]{"Admission Fee", "Admission Fee", "Admission Entrance Free", "Admission Entrance Fee", "Admission Fee"};
            itemName1 = new String[]{"Center • 40 minutes", "Center • 1 hour", "South • 3 hours", "South • 4 hours", "South • 1 hour"};
           itemImages = new String[]{
                    fullPath + "/trou_aux_cerfs_4.jpg",
                    fullPath + "/grand_bassin_1.jpg",
                    fullPath + "/bois_cheri_1.jpg",
                    fullPath + "/la_vanilla_1.jpg",
                    fullPath + "/gris_gris_1.jpg"
            };            itemLatitudes = new double[]{

                    -20.31823,//1
                    -20.4167126,//2
                    -20.4263131,//3
                    -20.498825,//4
                    -20.5245931//5
            };

            itemLongitudes = new double[]{

                    57.51165,//1
                    57.4933252,//2
                    57.5256388,//3
                    57.563247,//4
                    57.5303065//5
            };

        }
        if (main_day == 3 && sub_day == 1) {
            Stash.put("day", "day31");

            tab_layout2.setVisibility(View.VISIBLE);
            subbuttonDay1.setVisibility(View.INVISIBLE);
            subview1.setVisibility(View.INVISIBLE);
            subbuttonDay2.setVisibility(View.VISIBLE);
            subview2.setVisibility(View.VISIBLE);
            subbuttonDay3.setVisibility(View.VISIBLE);
            subview3.setVisibility(View.VISIBLE);
            subbuttonDay4.setVisibility(GONE);
            subview4.setVisibility(GONE);
            subbuttonDay5.setVisibility(GONE);
            subview5.setVisibility(GONE);
            subpressbuttonDay1.setVisibility(View.VISIBLE);
            subpressview1.setVisibility(View.VISIBLE);
            subpressbuttonDay2.setVisibility(View.INVISIBLE);
            subpressview2.setVisibility(View.INVISIBLE);
            subpressbuttonDay3.setVisibility(View.INVISIBLE);
            subpressview3.setVisibility(View.INVISIBLE);
            subpressbuttonDay4.setVisibility(GONE);
            subpressview4.setVisibility(GONE);
            subpressbuttonDay5.setVisibility(GONE);
            subpressview5.setVisibility(GONE);
            itemTexts = new String[]{"SSR Botanical Garden", "L’Aventure du Sucre Museum", "Chateau de Labourdonnais", "Le Caudan Waterfront", "Aapravasi Ghat", "Blue Penny Museum"};
            itemName1 = new String[]{"North • 2 hours", "North • 2 hours", "North • 2 hours", "North • 2 hours", "North • 1 hour", "North • 1 hour"};
           itemImages = new String[]{
                    fullPath + "/pamplemousse_garden.jpg",
                    fullPath + "/sugar_museum_pamplemousses.jpg",
                    fullPath + "/chateau_de_labourdonnais.jpg",
                    fullPath + "/port_louis_3.jpg",
                    fullPath + "/aapravasi_ghat_1.jpg",
                    fullPath + "/blue_penny_museum_2.jpg"
            };            itemName = new String[]{"Admission Entrance Fee", "Admission Entrance Fee", "Admission Entrance Free", "Admission Fee", "Admission Entrance Fee", "Admission Entrance Free"};
            itemLatitudes = new double[]{

                    -20.1042691,
                    -20.0978896,
                    -20.0736144,
                    -20.1608170,
                    -20.1586888,
                    -20.1609300
            };

            itemLongitudes = new double[]{

                    57.5799724,
                    57.5743742,
                    57.6176456,
                    57.4980775,
                    57.5029467,
                    57.4974394
            };

        }
        if (main_day == 3 && sub_day == 2) {
            Stash.put("day", "day32");

            subbuttonDay1.setVisibility(View.VISIBLE);
            subview1.setVisibility(View.VISIBLE);
            subbuttonDay2.setVisibility(View.INVISIBLE);
            subview2.setVisibility(View.INVISIBLE);
            subbuttonDay3.setVisibility(View.VISIBLE);
            subview3.setVisibility(View.VISIBLE);
            subbuttonDay4.setVisibility(GONE);
            subview4.setVisibility(GONE);
            subbuttonDay5.setVisibility(GONE);
            subview5.setVisibility(GONE);
            subpressbuttonDay1.setVisibility(View.INVISIBLE);
            subpressview1.setVisibility(View.INVISIBLE);
            subpressbuttonDay2.setVisibility(View.VISIBLE);
            subpressview2.setVisibility(View.VISIBLE);
            subpressbuttonDay3.setVisibility(View.INVISIBLE);
            subpressview3.setVisibility(View.INVISIBLE);
            subpressbuttonDay4.setVisibility(GONE);
            subpressview4.setVisibility(GONE);
            subpressbuttonDay5.setVisibility(GONE);
            subpressview5.setVisibility(GONE);
            itemTexts = new String[]{
                    "Bois Cheri Tea Museum", //1
                    "La Vanille Nature Park ", //2
                    "Gris Gris Beach"//3
            };
            itemName = new String[]{"Admission Entrance Fee", "Admission Entrance Fee", "Admission Free"};
            itemName1 = new String[]{"South • 3 hours", "South • 4 hour", "South • 1 hour"};

           itemImages = new String[]{
                    fullPath + "/bois_cheri_1.jpg",
                    fullPath + "/la_vanilla_1.jpg",
                    fullPath + "/gris_gris_1.jpg"
            };
            itemLatitudes = new double[]{
                    -20.4263131,//1
                    -20.498825,//2
                    -20.5245931//3
            };

            itemLongitudes = new double[]{
                    57.5256388,//1
                    57.563247,//2
                    57.5303065//3
            };
        }
        if (main_day == 3 && sub_day == 3) {
            Stash.put("day", "day33");

            subbuttonDay1.setVisibility(View.VISIBLE);
            subview1.setVisibility(View.VISIBLE);
            subbuttonDay2.setVisibility(View.VISIBLE);
            subview2.setVisibility(View.VISIBLE);
            subbuttonDay3.setVisibility(View.INVISIBLE);
            subview3.setVisibility(View.INVISIBLE);
            subbuttonDay4.setVisibility(GONE);
            subview4.setVisibility(GONE);
            subbuttonDay5.setVisibility(GONE);
            subview5.setVisibility(GONE);
            subpressbuttonDay1.setVisibility(View.INVISIBLE);
            subpressview1.setVisibility(View.INVISIBLE);
            subpressbuttonDay2.setVisibility(View.INVISIBLE);
            subpressview2.setVisibility(View.INVISIBLE);
            subpressbuttonDay3.setVisibility(View.VISIBLE);
            subpressview3.setVisibility(View.VISIBLE);
            subpressbuttonDay4.setVisibility(GONE);
            subpressview4.setVisibility(GONE);
            subpressbuttonDay5.setVisibility(GONE);
            subpressview5.setVisibility(GONE);
            itemTexts = new String[]{
                    "Tamarin Bay",//1
                    "Trou aux Cerfs",//2
                    "Grand Bassin", //3
                    "Black River Gorges",//4
                    "Seven Coloured Earths"//5
            };
            itemName = new String[]{"Admission Fee", "Admission Fee", "Admission Free", "Admission Fee", "Admission Entrance Fee"};
            itemName1 = new String[]{"West • 1 hour", "Center •1 hour 30 minutes", "South • 1 hour 30 minutes", "Southwest • 1 hour", "Southwest • 1 hour"};
            String fullPath = getFilesDir().getAbsolutePath() + "/cached_images";

           itemImages = new String[]{
                    fullPath + "/tamarin_3.jpg",
                    fullPath + "/trou_aux_cerfs_4.jpg",
                    fullPath + "/grand_bassin_1.jpg",
                    fullPath + "/black_river_georges_2.jpg",
                    fullPath + "/chamarel_2.jpg"
            };
            itemLatitudes = new double[]{

                    -20.3262782,//1
                    -20.31823,//2
                    -20.4167126,//3
                    -20.408077,//4
                    -20.4401637//5
            };

            itemLongitudes = new double[]{

                    57.3778870,//1
                    57.51165,//2
                    57.4933252,//3
                    57.473234,//4
                    57.3733048//5
            };
        }
        if (main_day == 4 && sub_day == 1) {
            Stash.put("day", "day41");

            subbuttonDay1.setVisibility(View.INVISIBLE);
            subview1.setVisibility(View.INVISIBLE);
            subbuttonDay2.setVisibility(View.VISIBLE);
            subview2.setVisibility(View.VISIBLE);
            subbuttonDay3.setVisibility(View.VISIBLE);
            subview3.setVisibility(View.VISIBLE);
            subbuttonDay4.setVisibility(View.VISIBLE);
            subview4.setVisibility(View.VISIBLE);
            subbuttonDay5.setVisibility(GONE);
            subview5.setVisibility(GONE);
            subpressbuttonDay1.setVisibility(View.VISIBLE);
            subpressview1.setVisibility(View.VISIBLE);
            subpressbuttonDay2.setVisibility(View.INVISIBLE);
            subpressview2.setVisibility(View.INVISIBLE);
            subpressbuttonDay3.setVisibility(View.INVISIBLE);
            subpressview3.setVisibility(View.INVISIBLE);
            subpressbuttonDay4.setVisibility(View.INVISIBLE);
            subpressview4.setVisibility(View.INVISIBLE);
            subpressbuttonDay5.setVisibility(GONE);
            subpressview5.setVisibility(GONE);
            itemTexts = new String[]{"SSR Botanical Garden", "L’Aventure du Sucre Museum", "Chateau de Labourdonnais", "Le Caudan Waterfront", "Aapravasi Ghat", "Blue Penny Museum"};
            itemName = new String[]{"Admission Entrance Fee", "Admission Entrance Fee", "Admission Entrance Free", "Admission Fee", "Admission Entrance Fee", "Admission Entrance Fee"};
            itemName1 = new String[]{"North • 2 hours", "North • 2 hours", "North • 2 hours", "North • 2 hours", "North • 1 hours", "North • 1 hours"};
           itemImages = new String[]{
                    fullPath + "/pamplemousse_garden.jpg",
                    fullPath + "/sugar_museum_pamplemousses.jpg",
                    fullPath + "/chateau_de_labourdonnais.jpg",
                    fullPath + "/port_louis_3.jpg",
                    fullPath + "/aapravasi_ghat_1.jpg",
                    fullPath + "/blue_penny_museum_2.jpg"
            };            itemLatitudes = new double[]{

                    -20.1042691,
                    -20.0978896,
                    -20.0736144,
                    -20.1608170,
                    -20.1586888,
                    -20.1609300
            };

            itemLongitudes = new double[]{

                    57.5799724,
                    57.5743742,
                    57.6176456,
                    57.4980775,
                    57.5029467,
                    57.4974394
            };

        }
        if (main_day == 4 && sub_day == 2) {
            Stash.put("day", "day42");

            subbuttonDay1.setVisibility(View.VISIBLE);
            subview1.setVisibility(View.VISIBLE);
            subbuttonDay2.setVisibility(View.INVISIBLE);
            subview2.setVisibility(View.INVISIBLE);
            subbuttonDay3.setVisibility(View.VISIBLE);
            subview3.setVisibility(View.VISIBLE);
            subbuttonDay4.setVisibility(View.VISIBLE);
            subview4.setVisibility(View.VISIBLE);
            subbuttonDay5.setVisibility(GONE);
            subview5.setVisibility(GONE);
            subpressbuttonDay1.setVisibility(View.INVISIBLE);
            subpressview1.setVisibility(View.INVISIBLE);
            subpressbuttonDay2.setVisibility(View.VISIBLE);
            subpressview2.setVisibility(View.VISIBLE);
            subpressbuttonDay3.setVisibility(View.INVISIBLE);
            subpressview3.setVisibility(View.INVISIBLE);
            subpressbuttonDay4.setVisibility(View.INVISIBLE);
            subpressview4.setVisibility(View.INVISIBLE);
            subpressbuttonDay5.setVisibility(GONE);
            subpressview5.setVisibility(GONE);
            itemTexts = new String[]{"Ile aux Cerfs Beach"};
            itemName = new String[]{"Admission Catamaran Fee"};
            itemName1 = new String[]{"East • Full Day"};

           itemImages = new String[]{
                    fullPath + "/ile_aux_cerfs_mauritius_1.jpg"
            };            // Latitude and Longitude for Ile aux Cerfs Beach
            itemLatitudes = new double[]{
                    -20.2668829};
            itemLongitudes = new double[]{
                    57.8057047};


        }
        if (main_day == 4 && sub_day == 3) {
            Stash.put("day", "day43");

            subbuttonDay1.setVisibility(View.VISIBLE);
            subview1.setVisibility(View.VISIBLE);
            subbuttonDay2.setVisibility(View.VISIBLE);
            subview2.setVisibility(View.VISIBLE);
            subbuttonDay3.setVisibility(View.INVISIBLE);
            subview3.setVisibility(View.INVISIBLE);
            subbuttonDay4.setVisibility(View.VISIBLE);
            subview4.setVisibility(View.VISIBLE);
            subbuttonDay5.setVisibility(GONE);
            subview5.setVisibility(GONE);
            subpressbuttonDay1.setVisibility(View.INVISIBLE);
            subpressview1.setVisibility(View.INVISIBLE);
            subpressbuttonDay2.setVisibility(View.INVISIBLE);
            subpressview2.setVisibility(View.INVISIBLE);
            subpressbuttonDay3.setVisibility(View.VISIBLE);
            subpressview3.setVisibility(View.VISIBLE);
            subpressbuttonDay4.setVisibility(View.INVISIBLE);
            subpressview4.setVisibility(View.INVISIBLE);
            subpressbuttonDay5.setVisibility(GONE);
            subpressview5.setVisibility(GONE);
            itemTexts = new String[]{
                    "Casela Adventure Park",//1
                    "Trou aux Cerfs",//2
                    "Black River Gorges", //3
                    "Chamarel Waterfalls", //4
                    "Seven Coloured Earths", //5
                    "Tamarin Bay Beach"//6
            };
            itemName = new String[]{"Admission Entrance Fee", "Admission Fee", "Admission Free", "Admission Fee", "Admission Entrance Fee", "Admission Fee"};
            itemName1 = new String[]{"West • 2 hours 30 minutes", "Center • 40 minutes", "Southwest • 30 minutes", "Southwest • 35 minutes", "Southwest • 1 hour 30 minutes", "West • 1 hour"};
           itemImages = new String[]{
                    fullPath + "/casela.jpg",
                    fullPath + "/trou_aux_cerfs_4.jpg",
                    fullPath + "/black_river_georges_2.jpg",
                    fullPath + "/chamarel_1.jpg",
                    fullPath + "/chamarel_2.jpg",
                    fullPath + "/tamarin_3.jpg"
            };
            itemLatitudes = new double[]{

                    -20.290801,//1
                    -20.31823,//2
                    -20.408077,//3
                    -20.44373,//4
                    -20.4330,//5
                    -20.3262782//6
            };

            itemLongitudes = new double[]{

                    57.402796,//1
                    57.51165,//2
                    57.473234,//3
                    57.38386,//4
                    57.3983,//5
                    57.3778870//6
            };
        }
        if (main_day == 4 && sub_day == 4) {
            Stash.put("day", "day44");
            subbuttonDay1.setVisibility(View.VISIBLE);
            subview1.setVisibility(View.VISIBLE);
            subbuttonDay2.setVisibility(View.VISIBLE);
            subview2.setVisibility(View.VISIBLE);
            subbuttonDay3.setVisibility(View.VISIBLE);
            subview3.setVisibility(View.VISIBLE);
            subbuttonDay4.setVisibility(View.INVISIBLE);
            subview4.setVisibility(View.INVISIBLE);
            subbuttonDay5.setVisibility(GONE);
            subview5.setVisibility(GONE);
            subpressbuttonDay1.setVisibility(View.INVISIBLE);
            subpressview1.setVisibility(View.INVISIBLE);
            subpressbuttonDay2.setVisibility(View.INVISIBLE);
            subpressview2.setVisibility(View.INVISIBLE);
            subpressbuttonDay3.setVisibility(View.INVISIBLE);
            subpressview3.setVisibility(View.INVISIBLE);
            subpressbuttonDay4.setVisibility(View.VISIBLE);
            subpressview4.setVisibility(View.VISIBLE);
            subpressbuttonDay5.setVisibility(GONE);
            subpressview5.setVisibility(GONE);
            itemTexts = new String[]{
                    "Eau Bleu Waterfall", //1
                    "Pont Naturel Bridge",//2
                    "Le Souffleur", //3
                    "Gris Gris Beach", //4
                    "La Roche qui Pleure",//5
                    "Rochester Falls", //6
                    "Maconde Viewpoint", //7
                    "La Prairie Beach"//8
            };
            itemName = new String[]{"Admission Fee", "Admission Fee", "Admission Free", "Admission Fee", "Admission Fee", "Admission Fee", "Admission Fee", "Admission Fee"};
            itemName1 = new String[]{"Southeast • 35 minutes", "South • 30 minutes", "South • 30 minutes", "South • 35 minutes", "South • 35 minutes", "South • 45 minutes", "Southwest • 30 minutes", "Southwest • 1 hour 30 minutes"};
           itemImages = new String[]{
                    fullPath + "/eau_bleu_1.jpg",
                    fullPath + "/pont_naturel_2.jpg",
                    fullPath + "/le_souffleur_1.jpg",
                    fullPath + "/gris_gris_1.jpg",
                    fullPath + "/la_roche_qui_pleure.jpg",
                    fullPath + "/rochester_falls_1.jpg",
                    fullPath + "/maconde_1.jpg",
                    fullPath + "/la_prairie_2.jpg"
            };
            itemLatitudes = new double[]{

                    -20.372057,//1
                    -20.480281,//2
                    -20.485210,//3
                    -20.5243435,//4
                    -20.5040054,//5
                    -20.4953821,//6
                    -20.4911178,//7
                    -20.5272001//8
            };

            itemLongitudes = new double[]{

                    57.5575362,//1
                    57.669441,//2
                    57.642585,//3
                    57.5323138,//4
                    57.7561275,//5
                    57.5070605,//6
                    57.3711084,//7
                    57.7364779//8
            };
        }
        if (main_day == 5 && sub_day == 1) {
            Stash.put("day", "day51");

            subbuttonDay1.setVisibility(View.INVISIBLE);
            subview1.setVisibility(View.INVISIBLE);
            subbuttonDay2.setVisibility(View.VISIBLE);
            subview2.setVisibility(View.VISIBLE);
            subbuttonDay3.setVisibility(View.VISIBLE);
            subview3.setVisibility(View.VISIBLE);
            subbuttonDay4.setVisibility(View.VISIBLE);
            subview4.setVisibility(View.VISIBLE);
            subbuttonDay5.setVisibility(View.VISIBLE);
            subview5.setVisibility(View.VISIBLE);
            subpressbuttonDay1.setVisibility(View.VISIBLE);
            subpressview1.setVisibility(View.VISIBLE);
            subpressbuttonDay2.setVisibility(View.INVISIBLE);
            subpressview2.setVisibility(View.INVISIBLE);
            subpressbuttonDay3.setVisibility(View.INVISIBLE);
            subpressview3.setVisibility(View.INVISIBLE);
            subpressbuttonDay4.setVisibility(View.INVISIBLE);
            subpressview4.setVisibility(View.INVISIBLE);
            subpressbuttonDay5.setVisibility(View.INVISIBLE);
            subpressview5.setVisibility(View.INVISIBLE);
            itemTexts = new String[]{"Le Caudan Waterfront", "Aapravasi Ghat", "Port Louis Central Market", "SSR Botanical Garden", "Grand Bay"};
            itemName = new String[]{"Admission Fee", "Admission Entrance Fee", "Admission Free", "Admission Entrance Fee", "Admission Entrance  Fee"};
            itemName1 = new String[]{"North • 2 hours", "North • 1 hours", "North • 1 hours", "North • 2 hours", "North • 1 hours"};
           itemImages = new String[]{
                    fullPath + "/port_louis_3.jpg",
                    fullPath + "/aapravasi_ghat_1.jpg",
                    fullPath + "/port_louis_4.jpg",
                    fullPath + "/pamplemousse_garden.jpg",
                    fullPath + "/grand_baie_1.jpg"
            };
// Latitude and Longitude for the given locations
            itemLatitudes = new double[]{
                    -20.1608170,
                    -20.1586888,
                    -20.1606798,
                    -20.1042691,
                    -20.0089233
            };

            itemLongitudes = new double[]{
                    57.4980775,
                    57.5029467,
                    57.5029272,
                    57.5799724,
                    57.5812308
            };

        }
        if (main_day == 5 && sub_day == 2) {
            Stash.put("day", "day52");

            subbuttonDay1.setVisibility(View.VISIBLE);
            subview1.setVisibility(View.VISIBLE);
            subbuttonDay2.setVisibility(View.INVISIBLE);
            subview2.setVisibility(View.INVISIBLE);
            subbuttonDay3.setVisibility(View.VISIBLE);
            subview3.setVisibility(View.VISIBLE);
            subbuttonDay4.setVisibility(View.VISIBLE);
            subview4.setVisibility(View.VISIBLE);
            subbuttonDay5.setVisibility(View.VISIBLE);
            subview5.setVisibility(View.VISIBLE);
            subpressbuttonDay1.setVisibility(View.INVISIBLE);
            subpressview1.setVisibility(View.INVISIBLE);
            subpressbuttonDay2.setVisibility(View.VISIBLE);
            subpressview2.setVisibility(View.VISIBLE);
            subpressbuttonDay3.setVisibility(View.INVISIBLE);
            subpressview3.setVisibility(View.INVISIBLE);
            subpressbuttonDay4.setVisibility(View.INVISIBLE);
            subpressview4.setVisibility(View.INVISIBLE);
            subpressbuttonDay5.setVisibility(View.INVISIBLE);
            subpressview5.setVisibility(View.INVISIBLE);
            itemTexts = new String[]{
                    "Seven Coloured Earths",//1
                    "Chamarel Waterfalls", //2
                    "Black River Gorges", //3
                    "Rhumerie de Chamarel",//4
                    "Tamarin Bay Beach"//5
            };
            itemName = new String[]{"Admission Entrance Fee", "Admission Fee", "Admission Free", "Admission Entrance Fee", "Admission  Fee"};
            itemName1 = new String[]{"Southwest • 1 hour 30 minutes", "Southwest • 1 hour 30 minutes", "Southwest • 1 hour 30 minutes", "Southwest • 1 hour", "West • 1 hour"};
           itemImages = new String[]{
                    fullPath + "/chamarel_2.jpg",
                    fullPath + "/chamarel_1.jpg",
                    fullPath + "/black_river_georges_2.jpg",
                    fullPath + "/rhumerie_de_chamarel_1.jpg",
                    fullPath + "/tamarin_3.jpg"
            };
            itemLatitudes = new double[]{
                    -20.4330,//1
                    -20.44373,//2
                    -20.408077,//3
                    -20.4279001,//4
                    -20.3262782//5
            };

            itemLongitudes = new double[]{
                    57.3983,//1
                    57.38386,//2
                    57.473234,//3
                    57.3963121,//4
                    57.3778870//5
            };
        }
        if (main_day == 5 && sub_day == 3) {
            Stash.put("day", "day53");

            subbuttonDay1.setVisibility(View.VISIBLE);
            subview1.setVisibility(View.VISIBLE);
            subbuttonDay2.setVisibility(View.VISIBLE);
            subview2.setVisibility(View.VISIBLE);
            subbuttonDay3.setVisibility(View.INVISIBLE);
            subview3.setVisibility(View.INVISIBLE);
            subbuttonDay4.setVisibility(View.VISIBLE);
            subview4.setVisibility(View.VISIBLE);
            subbuttonDay5.setVisibility(View.VISIBLE);
            subview5.setVisibility(View.VISIBLE);
            subpressbuttonDay1.setVisibility(View.INVISIBLE);
            subpressview1.setVisibility(View.INVISIBLE);
            subpressbuttonDay2.setVisibility(View.INVISIBLE);
            subpressview2.setVisibility(View.INVISIBLE);
            subpressbuttonDay3.setVisibility(View.VISIBLE);
            subpressview3.setVisibility(View.VISIBLE);
            subpressbuttonDay4.setVisibility(View.INVISIBLE);
            subpressview4.setVisibility(View.INVISIBLE);
            subpressbuttonDay5.setVisibility(View.INVISIBLE);
            subpressview5.setVisibility(View.INVISIBLE);
            itemTexts = new String[]{
                    "Casela Adventure Park",//1
                    "La Preneuse Beach",//2
                    "Martello Tower",//3
                    "Le Morne Brabant",//4
                    "Flic en Flac Beach"//5
            };
            itemName = new String[]{"Admission Entrance Fee", "Admission Fee", "Admission Entrance Free", "Admission Fee", "Admission  Fee"};
            itemName1 = new String[]{"West • 2 hours 30 minutes", "West • 1 hour", "West • 50 minutes", "West • 1 – 3 hours", "West • 1 hour"};
           itemImages = new String[]{
                    fullPath + "/casela.jpg",
                    fullPath + "/la_preneuse_4.jpg",
                    fullPath + "/martello_tower_4.jpg",
                    fullPath + "/le_morne_1.jpg",
                    fullPath + "/flic_en_flac_3.jpg"
            };
            itemLatitudes = new double[]{
                    -20.290801,//1
                    -20.3547236,//2
                    -20.3546962,//3
                    -20.45306,//4
                    -20.2993385//5
            };

            itemLongitudes = new double[]{
                    57.402796,//1
                    57.3614249,//2
                    57.3619205,//3
                    57.32442,//4
                    57.3636901//5
            };
        }
        if (main_day == 5 && sub_day == 4) {
            Stash.put("day", "day54");

            subbuttonDay1.setVisibility(View.VISIBLE);
            subview1.setVisibility(View.VISIBLE);
            subbuttonDay2.setVisibility(View.VISIBLE);
            subview2.setVisibility(View.VISIBLE);
            subbuttonDay3.setVisibility(View.VISIBLE);
            subview3.setVisibility(View.VISIBLE);
            subbuttonDay4.setVisibility(View.INVISIBLE);
            subview4.setVisibility(View.INVISIBLE);
            subbuttonDay5.setVisibility(View.VISIBLE);
            subview5.setVisibility(View.VISIBLE);
            subpressbuttonDay1.setVisibility(View.INVISIBLE);
            subpressview1.setVisibility(View.INVISIBLE);
            subpressbuttonDay2.setVisibility(View.INVISIBLE);
            subpressview2.setVisibility(View.INVISIBLE);
            subpressbuttonDay3.setVisibility(View.INVISIBLE);
            subpressview3.setVisibility(View.INVISIBLE);
            subpressbuttonDay4.setVisibility(View.VISIBLE);
            subpressview4.setVisibility(View.VISIBLE);
            subpressbuttonDay5.setVisibility(View.INVISIBLE);
            subpressview5.setVisibility(View.INVISIBLE);
            itemTexts = new String[]{
                    "Mahebourg Waterfront",
                    "Mahebourg Museum",
                    "Blue Bay Beach",
                    "Gris Gris Beach",
                    "La Roche qui Pleure",
                    "Maconde Viewpoint",
                    "La Prairie Beach"
            };
            itemName = new String[]{"Admission Fee", "Admission Fee", "Admission Free", "Admission Fee", "Admission  Fee", "Admission  Fee", "Admission  Fee"};
            itemName1 = new String[]{"Southeast • 1 – 2 hours", "Southeast • 1 – 2 hours", "Southeast • 1 – 1.5 hours", "South • 35 minutes", "South • 35 minutes", "Southwest • 30 minutes", "Southwest • 1 hour 30 minutes"};
           itemImages = new String[]{
                    fullPath + "/mahebourg.jpg",
                    fullPath + "/mahebourg_museum_2.jpg",
                    fullPath + "/blue_bay.jpg",
                    fullPath + "/gris_gris_1.jpg",
                    fullPath + "/la_roche_qui_pleure.jpg",
                    fullPath + "/maconde_1.jpg",
                    fullPath + "/la_prairie_2.jpg"
            };
            itemLatitudes = new double[]{

                    -20.1608170,
                    -20.1586888,
                    -20.1606798,
                    -20.1042691,
                    -20.482992
            };

            itemLongitudes = new double[]{

                    57.4980775,
                    57.5029467,
                    57.5029272,
                    57.5799724,
                    57.353737
            };
        }
        if (main_day == 5 && sub_day == 5) {
            Stash.put("day", "day55");

            subbuttonDay1.setVisibility(View.VISIBLE);
            subview1.setVisibility(View.VISIBLE);
            subbuttonDay2.setVisibility(View.VISIBLE);
            subview2.setVisibility(View.VISIBLE);
            subbuttonDay3.setVisibility(View.VISIBLE);
            subview3.setVisibility(View.VISIBLE);
            subbuttonDay4.setVisibility(View.VISIBLE);
            subview4.setVisibility(View.VISIBLE);
            subbuttonDay5.setVisibility(View.INVISIBLE);
            subview5.setVisibility(View.INVISIBLE);
            subpressbuttonDay1.setVisibility(View.INVISIBLE);
            subpressview1.setVisibility(View.INVISIBLE);
            subpressbuttonDay2.setVisibility(View.INVISIBLE);
            subpressview2.setVisibility(View.INVISIBLE);
            subpressbuttonDay3.setVisibility(View.INVISIBLE);
            subpressview3.setVisibility(View.INVISIBLE);
            subpressbuttonDay4.setVisibility(View.INVISIBLE);
            subpressview4.setVisibility(View.INVISIBLE);
            subpressbuttonDay5.setVisibility(View.VISIBLE);
            subpressview5.setVisibility(View.VISIBLE);
            itemTexts = new String[]{"Ile aux Cerfs Beach"};
            itemName = new String[]{"Admission Catamaran Fee"};
            itemName1 = new String[]{"East • Full Day"};
           itemImages = new String[]{
                    fullPath + "/ile_aux_cerfs_mauritius_1.jpg"
            };
            itemLatitudes = new double[]{
                    -20.2668829};
            itemLongitudes = new double[]{
                    57.8057047};

        }
        ListView listView = findViewById(R.id.listView);
        ItenerariesAdapter adapter = new ItenerariesAdapter(ItinerariesActivity.this, itemTexts, itemName, itemName1, itemImages, itemLatitudes, itemLongitudes);
        listView.setAdapter(adapter);
    }


    public void menu(View view) {
        LinearLayout more_layout;
        more_layout = findViewById(R.id.more_layout);
        ImageView menu = findViewById(R.id.menu);
        ImageView close = findViewById(R.id.closebtn);
        menu.setVisibility(GONE);
        more_layout.setVisibility(View.VISIBLE);
        close.setVisibility(View.VISIBLE);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.setVisibility(View.VISIBLE);
                more_layout.setVisibility(GONE);
                close.setVisibility(GONE);
            }
        });
    }

    public void explore(View view) {
        startActivity(new Intent(ItinerariesActivity.this, ExploreActivity.class));
    }

    public void my_trips(View view) {
        startActivity(new Intent(ItinerariesActivity.this, MyTripsActivity.class));
    }

    public void iternties(View view) {
        startActivity(new Intent(ItinerariesActivity.this, ItinerariesActivity.class));

    }

    public void organier(View view) {
        startActivity(new Intent(ItinerariesActivity.this, OrganizerActivity.class));
    }

    public void contact_us(View view) {
        startActivity(new Intent(ItinerariesActivity.this, ContactUsActivity.class));
    }


    public void tips(View view) {
        startActivity(new Intent(ItinerariesActivity.this, TravelTipsActivity.class));

    }

    public void about(View view) {
        startActivity(new Intent(ItinerariesActivity.this, AppInfoActivity.class));

    }

    /*public void login(View view) {
        startActivity(new Intent(ItinerariesActivity.this, LoginActivity.class));
    }*/

    public void home(View view) {
        startActivity(new Intent(ItinerariesActivity.this, DashboardActivity.class));

    }
    public void premium() {
        TextView already_purchased, premium_amount;
        already_purchased = findViewById(R.id.already_purchased);
        premium_amount = findViewById(R.id.premium_amount);
        restore_purchase = findViewById(R.id.restore_purchase);
        lifetime_premium = findViewById(R.id.lifetime_premium);
        premium_layout = findViewById(R.id.premium_layout);
        faq_layout = findViewById(R.id.faq_layout);
        close = findViewById(R.id.close);
        sliderView = findViewById(R.id.slider);
        close_faq = findViewById(R.id.close_faq);
        faq_txt = findViewById(R.id.faq_txt);
        text1 = findViewById(R.id.text111);
        text2 = findViewById(R.id.text112);
        RelativeLayout purchase_lyt = findViewById(R.id.purchase_lyt);
        premium_amount.setText(Stash.getString(Constants.PRODUCT_PREMIUM_VALUE));
        purchase_lyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Stash.getBoolean(Constants.IS_LOGGED_IN, false)) {
                    premium_layout.setVisibility(GONE);
                    faq_layout.setVisibility(GONE);
                    Toast.makeText(ItinerariesActivity.this, "Create account to become a premium user", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ItinerariesActivity.this, CreateAccountActivity.class));
                } else {
                    initiatePurchase();
                }
            }
        });
        faq_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                faq_layout.setVisibility(View.VISIBLE);
            }
        });

        List<String> sliderData = new ArrayList<>();
        File cacheDir = new File(getFilesDir(), "cached_images");
        String fullPath = cacheDir.getAbsolutePath();
        sliderData.add(fullPath + "/" + "img_1" + ".png");
        sliderData.add(fullPath + "/" + "img_2" + ".png");
        sliderData.add(fullPath + "/" + "img5" + ".jpeg");
        sliderData.add(fullPath + "/" + "img_4" + ".png");
        sliderData.add(fullPath + "/" + "img_6" + ".jpeg");


        adapter = new SliderAdapterExample(this, sliderData);
        sliderView.setSliderAdapter(adapter);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);

//    sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
//    sliderView.startAutoCycle();
        sliderView.setCurrentPageListener(new SliderView.OnSliderPageListener() {
            @Override
            public void onSliderPageChanged(int position) {
                if (position == 0) {
                    text1.setText("Customize Itinerary");
                    text2.setText("Create and manage your travel itinerary");
                } else if (position == 1) {
                    text1.setText("Save your Itinerary");
                    text2.setText("Save your itinerary so you may access it from any other devices");
                } else if (position == 2) {
                    text1.setText("Offline Maps");
                    text2.setText("Get locations without connecting to the internet");
                } else if (position == 3) {
                    text1.setText("Travel Itinerary Organizer");
                    text2.setText("Gather all your hotel bookings and planes, trains, and rental carsdocuments all in one place");
                } else if (position == 4) {
                    text1.setText("Calender-based Trip Manager");
                    text2.setText("Organize your trip with the calendar-based interface to see what you have booked and when");
                }
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                premium_layout.setVisibility(GONE);
            }
        });
        close_faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                faq_layout.setVisibility(GONE);
            }
        });
        if (!Stash.getBoolean(Constants.IS_PREMIUM, false)) {
            already_purchased.setText("You are not premium user?");
            restore_purchase.setText("Purchase Now");
        } else  {
            already_purchased.setVisibility(GONE);
            restore_purchase.setVisibility(GONE);
        }
    }

    private void initiatePurchase() {
        List<QueryProductDetailsParams.Product> products = new ArrayList<>();
        products.add(QueryProductDetailsParams.Product.newBuilder()
                .setProductId(Constants.PRODUCT_PREMIUM)
                .setProductType(BillingClient.ProductType.INAPP)
                .build());

        QueryProductDetailsParams params = QueryProductDetailsParams.newBuilder()
                .setProductList(products)
                .build();

        billingClient.queryProductDetailsAsync(params, (billingResult, productDetailsList) -> {
            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && !productDetailsList.isEmpty()) {
                ProductDetails productDetails = productDetailsList.get(0);
                Log.d("TAG", productDetails + "  product");
                List<BillingFlowParams.ProductDetailsParams> productDetailsParamsList = new ArrayList<>();
                productDetailsParamsList.add(
                        BillingFlowParams.ProductDetailsParams.newBuilder()
                                .setProductDetails(productDetails)
                                .build()
                );

                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                        .setProductDetailsParamsList(productDetailsParamsList)
                        .build();

                billingClient.launchBillingFlow(ItinerariesActivity.this, billingFlowParams);
            } else {
                Toast.makeText(this, "Product not found or error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handlePurchase(Purchase purchase) {
        if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged()) {
                AcknowledgePurchaseParams acknowledgeParams =
                        AcknowledgePurchaseParams.newBuilder()
                                .setPurchaseToken(purchase.getPurchaseToken())
                                .build();

                billingClient.acknowledgePurchase(acknowledgeParams, billingResult -> {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        Stash.put(Constants.IS_PREMIUM, true); // Only after acknowledgment
                        saveVipToFirebase();
                        Toast.makeText(this, "Purchase acknowledged", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void saveVipToFirebase() {
        String uid = FirebaseAuth.getInstance().getCurrentUser() != null ?
                FirebaseAuth.getInstance().getCurrentUser().getUid() : null;

        if (uid == null) {
            Log.e("TAG", "User not logged in. Cannot save VIP status.");
            return;
        }

        FirebaseDatabase.getInstance()
                .getReference("DiscoverMauritius")
                .child("Users")
                .child(uid)
                .child("vip")
                .setValue(true)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(ItinerariesActivity.this, "VIP status saved", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ItinerariesActivity.this, "Failed to save VIP status", Toast.LENGTH_SHORT).show();
                });
    }
}

