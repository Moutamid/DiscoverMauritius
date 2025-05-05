package com.moutamid.sqlapp.activities.Beaches;

import static android.view.View.GONE;
import static com.moutamid.sqlapp.model.DatabaseHelper.TABLE_NAME;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.AppCompatActivity;import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.sqlapp.R;
import com.moutamid.sqlapp.activities.AppInfo.AppInfoActivity;
import com.moutamid.sqlapp.activities.ContactUs.ContactUsActivity;
import com.moutamid.sqlapp.activities.CreateAccountActivity;
import com.moutamid.sqlapp.activities.DashboardActivity;
import com.moutamid.sqlapp.activities.Explore.ExploreActivity;

import com.moutamid.sqlapp.activities.InAppPurchase.SliderAdapterExample;
import com.moutamid.sqlapp.activities.Iteneraries.ItinerariesActivity;
import com.moutamid.sqlapp.activities.MyTripsActivity;
import com.moutamid.sqlapp.activities.Organizer.OrganizerActivity;
import com.moutamid.sqlapp.activities.TravelTipsActivity;
import com.moutamid.sqlapp.adapter.ReviewAdapter;
import com.moutamid.sqlapp.helper.Constants;
import com.moutamid.sqlapp.helper.Utils;
import com.moutamid.sqlapp.model.BeacModel;
import com.moutamid.sqlapp.model.DatabaseHelper;
import com.moutamid.sqlapp.model.Review;
import com.moutamid.sqlapp.offlinemap.MapActivity;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class BeachDetails extends AppCompatActivity {
    public static LinearLayout premium_layout, faq_layout;
    public static TextView faq_txt, text1, text2;

    String fullPath;
    RelativeLayout lifetime_premium;
    SliderView sliderView;
    SliderAdapterExample adapter;
    ImageView close, close_faq;
    TextView restore_purchase;
    TextView count_rating;
    MaterialRatingBar ratingBar;
    ImageView weather_icon;
    TextView weather_txt;
    TextView review_text;
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
    private RecyclerView recyclerView;
    private ReviewAdapter reviewAdapter;
    private List<Review> reviewList;

    @Override
protected void onCreate(Bundle savedInstanceState) {
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beach_details);
        premium();
        billingClient = BillingClient.newBuilder(this)
                .setListener(purchasesUpdatedListener)
                .enablePendingPurchases()
                .build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    Toast.makeText(BeachDetails.this, "Billing connected", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                Toast.makeText(BeachDetails.this, "Billing disconnected", Toast.LENGTH_SHORT).show();
            }
        });
        ratingBar = findViewById(R.id.ratingbar);
        count_rating = findViewById(R.id.count);
        weather_icon = findViewById(R.id.weather_icon);
        weather_txt = findViewById(R.id.weather_txt);
        review_text = findViewById(R.id.review_text);
        Utils.loginBtnMenuListener(this);
        LinearLayout map_layout = findViewById(R.id.map_layout);
        ImageView mainImg = findViewById(R.id.main_img);
        ImageView menu = findViewById(R.id.menu);
        ImageView mainImage = findViewById(R.id.main_image);
        ImageView add = findViewById(R.id.add);
        ImageView map = findViewById(R.id.map);
        TextView text1 = findViewById(R.id.text1);
        TextView beach_type = findViewById(R.id.beach_type);
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
        ImageView image6 = findViewById(R.id.image6);
        ImageView image7 = findViewById(R.id.image7);
        ImageView image8 = findViewById(R.id.image8);
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
        TextView text13 = findViewById(R.id.text13);
        TextView text14 = findViewById(R.id.text14);
        TextView text15 = findViewById(R.id.text15);

        TextView add_not = findViewById(R.id.add_not);
        LinearLayout add_lyt = findViewById(R.id.add_lyt);
        int isTrip = getIntent().getIntExtra("is_trip", 1);
        if (isTrip == 0) {
            beach_type.setVisibility(GONE);
        }
        DatabaseHelper databaseHelper;
        databaseHelper = new DatabaseHelper(BeachDetails.this);
        BeacModel model = (BeacModel) Stash.getObject("model", BeacModel.class);
        fetchWeatherData(model.lat, model.lng);
        recyclerView = findViewById(R.id.recyclerView);
        reviewList = new ArrayList<>();
        reviewAdapter = new ReviewAdapter(reviewList, model.title);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(reviewAdapter);
        fetchReviewsByPlace(model.title);
        ratingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signInAnonymously()
                        .addOnSuccessListener(authResult -> {

                            String userId = authResult.getUser().getUid();
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ratings").child(model.title).child(userId);
                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (!snapshot.exists()) {
                                        showRatingDialog(model.title);
                                    } else {
//                                            Toast.makeText(context, "You already did rating", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        });

                return false;
            }
        });
        loadRating(model.title, ratingBar);

        Log.d("model", model.image1 + " test");
        beach_header.setText(Html.fromHtml(model.title));
        beach_type.setText(Stash.getString("type"));
//        mainImage.setImageResource();
        Glide.with(this)
                .load(new File(model.main_image))
                .into(mainImage);
        if (!model.text1.isEmpty()) {
            text1.setText(Html.fromHtml(model.text1));
            text1.setVisibility(View.VISIBLE);
        } else {
            text1.setVisibility(GONE);
        }
        if (!model.title1.isEmpty()) {
            title1.setText(Html.fromHtml(model.title1));
            title1.setVisibility(View.VISIBLE);
        } else {
            title1.setVisibility(GONE);
        }

        if (!model.image1.equals("")) {
            Glide.with(this)
                    .load(new File(model.image1))
                    .into(image1);
            image1.setVisibility(View.VISIBLE);
        } else {
            image1.setVisibility(GONE);
        }
        if (!model.text2.isEmpty()) {
            text2.setText(Html.fromHtml(model.text2));
            text2.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            text2.setVisibility(GONE);
        }

        if (!model.title2.isEmpty()) {
            title2.setText(Html.fromHtml(model.title2));
            title2.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            title2.setVisibility(GONE);
        }

        if (!model.image2.equals("")) {
            Glide.with(this)
                    .load(new File(model.image2))
                    .into(image2);
            image2.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            image2.setVisibility(GONE);
        }

        if (!model.text3.isEmpty()) {
            text3.setText(Html.fromHtml(model.text3));
            text3.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            text3.setVisibility(GONE);
        }

        if (!model.title3.isEmpty()) {
            title3.setText(Html.fromHtml(model.title3));
            title3.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            title3.setVisibility(GONE);
        }
        if (!model.image3.equals("")) {
            Glide.with(this)
                    .load(new File(model.image3))
                    .into(image3);
            image3.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            image3.setVisibility(GONE);
        }
        if (!model.text4.isEmpty()) {
            text4.setText(Html.fromHtml(model.text4));
            text4.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            text4.setVisibility(GONE);
        }
        if (!model.title4.isEmpty()) {
            title4.setText(Html.fromHtml(model.title4));
            title4.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            title4.setVisibility(GONE);
        }

        if (!model.image4.equals("")) {
            Glide.with(this)
                    .load(new File(model.image4))
                    .into(image4);
            image4.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            image4.setVisibility(GONE);
        }

        if (!model.text5.isEmpty()) {
            text5.setText(Html.fromHtml(model.text5));
            text5.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            text5.setVisibility(GONE);
        }

        if (!model.title5.isEmpty()) {
            title5.setText(Html.fromHtml(model.title5));
            title5.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            title5.setVisibility(GONE);
        }

        if (!model.image5.equals("")) {
            Glide.with(this)
                    .load(new File(model.image5))
                    .into(image5);
            image5.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            image5.setVisibility(GONE);
        }

        if (!model.text6.isEmpty()) {
            text6.setText(Html.fromHtml(model.text6));
            text6.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            text6.setVisibility(GONE);
        }

        if (!model.title6.isEmpty()) {
            title6.setText(Html.fromHtml(model.title6));
            title6.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            title6.setVisibility(GONE);
        }
// Set text or image and adjust visibility for the views
        if (!model.text7.isEmpty()) {
            text7.setText(Html.fromHtml(model.text7));
            text7.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            text7.setVisibility(GONE);
        }

        if (!model.title7.isEmpty()) {
            title7.setText(Html.fromHtml(model.title7));
            title7.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            title7.setVisibility(GONE);
        }

        if (!model.text8.isEmpty()) {
            text8.setText(Html.fromHtml(model.text8));
            text8.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            text8.setVisibility(GONE);
        }

        if (!model.title8.isEmpty()) {
            title8.setText(Html.fromHtml(model.title8));
            title8.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            title8.setVisibility(GONE);
        }

        if (!model.text9.isEmpty()) {
            text9.setText(Html.fromHtml(model.text9));
            text9.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            text9.setVisibility(GONE);
        }

        if (!model.text10.isEmpty()) {
            text10.setText(Html.fromHtml(model.text10));
            text10.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            text10.setVisibility(GONE);
        }

        if (!model.text11.isEmpty()) {
            text11.setText(Html.fromHtml(model.text11));
            text11.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            text11.setVisibility(GONE);
        }

        if (!model.text12.isEmpty()) {
            text12.setText(Html.fromHtml(model.text12));
            text12.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            text12.setVisibility(GONE);
        }

        if (!model.image6.equals("")) {
            Glide.with(this)
                    .load(new File(model.image6))
                    .into(image6);
            image6.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            image6.setVisibility(GONE);
        }

        if (!model.text13.isEmpty()) {
            text13.setText(Html.fromHtml(model.text13));
            text13.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            text13.setVisibility(GONE);
        }

        if (!model.image7.equals("")) {
            Glide.with(this)
                    .load(new File(model.image7))
                    .into(image7);
            image7.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            image7.setVisibility(GONE);
        }

        if (!model.text14.isEmpty()) {
            text14.setText(Html.fromHtml(model.text14));
            text14.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            text14.setVisibility(GONE);
        }

        if (!model.image8.equals("")) {
            Glide.with(this)
                    .load(new File(model.image8))
                    .into(image8);
            image8.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            image8.setVisibility(GONE);
        }

        if (!model.text15.isEmpty()) {
            text15.setText(Html.fromHtml(model.text15));
            text15.setVisibility(View.VISIBLE); // Set visibility to VISIBLE explicitly
        } else {
            text15.setVisibility(GONE);
        }


        List<BeacModel> beacModels = databaseHelper.getAllBeacModels();
        boolean isDataAvailable = false;
        for (BeacModel model1 : beacModels) {
            if (model1.title.equals(beach_header.getText().toString())) {
                isDataAvailable = true;
                break;
            }
        }
        if (isDataAvailable) {
            add.setVisibility(GONE);
            remove.setVisibility(View.VISIBLE);
            add_not.setText("Remove");
        } else {
            add.setVisibility(View.VISIBLE);
            remove.setVisibility(GONE);
            add_not.setText("Add");
        }

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Stash.getBoolean(Constants.IS_PREMIUM, false)) {
                    String deleteName = beach_header.getText().toString();
                    SQLiteDatabase db = databaseHelper.getWritableDatabase();
                    db.delete(TABLE_NAME, DatabaseHelper.COLUMN_TITLE + "=?", new String[]{deleteName});
                    db.close();
                    remove.setVisibility(GONE);
                    add.setVisibility(View.VISIBLE);
                    add_not.setText("Add");
                } else {
                    premium_layout.setVisibility(View.VISIBLE);
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseHelper databaseHelper = new DatabaseHelper(BeachDetails.this);
                databaseHelper.insertBeacModel(model);
                add.setVisibility(GONE);
                remove.setVisibility(View.VISIBLE);
                add_not.setText("Remove");

            }
        });
        boolean finalIsDataAvailable = isDataAvailable;
        add_lyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (finalIsDataAvailable) {
                    String deleteName = beach_header.getText().toString();
                    SQLiteDatabase db = databaseHelper.getWritableDatabase();
                    db.delete(TABLE_NAME, DatabaseHelper.COLUMN_TITLE + "=?", new String[]{deleteName});
                    db.close();
                    remove.setVisibility(GONE);
                    add.setVisibility(View.VISIBLE);
                    add_not.setText("Add");
                } else {
                    DatabaseHelper databaseHelper = new DatabaseHelper(BeachDetails.this);
                    databaseHelper.insertBeacModel(model);
                    add.setVisibility(GONE);
                    remove.setVisibility(View.VISIBLE);
                    add_not.setText("Remove");

                }

            }
        });
        map_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Stash.getBoolean(Constants.IS_PREMIUM, false)) {
                    Stash.put("map_lat", model.lat);
                    Stash.put("map_lng", model.lng);
                    Stash.put("map_name", model.title);
                    Stash.put("map_img", model.main_image);
                    Intent intent = new Intent(BeachDetails.this, MapActivity.class);
                    intent.putExtra("map_lat", model.lat);
                    intent.putExtra("map_lng", model.lng);
                    startActivity(intent);
                } else {
                    premium_layout.setVisibility(View.VISIBLE);
                }

            }
        });

    }

    public void BackPress(View view) {
        onBackPressed();
    }


    public void premium() {
        TextView already_purchased, premium_amount;
        already_purchased = findViewById(R.id.already_purchased);
        premium_amount = findViewById(R.id.premium_amount);
        restore_purchase = findViewById(R.id.restore_purchase);
        lifetime_premium = findViewById(R.id.lifetime_premium);
        premium_layout = findViewById(R.id.premium_layout);
        faq_layout = findViewById(R.id.faq_layout);
        close = findViewById(R.id.close_btn);
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
                    Toast.makeText(BeachDetails.this, "Create account to become a premium user", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(BeachDetails.this, CreateAccountActivity.class));
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

                billingClient.launchBillingFlow(BeachDetails.this, billingFlowParams);
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
                    Toast.makeText(BeachDetails.this, "VIP status saved", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(BeachDetails.this, "Failed to save VIP status", Toast.LENGTH_SHORT).show();
                });
    }
    public void menu(View view) {
        LinearLayout more_layout;
        more_layout = findViewById(R.id.more_layout);
        ImageView menu = findViewById(R.id.menu);
        ImageView closebtn = findViewById(R.id.closebtn);
        menu.setVisibility(GONE);
        more_layout.setVisibility(View.VISIBLE);
        closebtn.setVisibility(View.VISIBLE);
        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.setVisibility(View.VISIBLE);
                more_layout.setVisibility(GONE);
                closebtn.setVisibility(GONE);
            }
        });
    }

    public void explore(View view) {
        startActivity(new Intent(BeachDetails.this, ExploreActivity.class));
    }

    public void my_trips(View view) {
        startActivity(new Intent(BeachDetails.this, MyTripsActivity.class));
    }

    public void iternties(View view) {
        startActivity(new Intent(BeachDetails.this, ItinerariesActivity.class));

    }

    public void organier(View view) {
        startActivity(new Intent(BeachDetails.this, OrganizerActivity.class));
    }

    public void contact_us(View view) {
        startActivity(new Intent(BeachDetails.this, ContactUsActivity.class));
    }


    public void tips(View view) {
        startActivity(new Intent(BeachDetails.this, TravelTipsActivity.class));

    }

    public void about(View view) {
        startActivity(new Intent(BeachDetails.this, AppInfoActivity.class));

    }

   /* public void login(View view) {
        startActivity(new Intent(BeachDetails.this, LoginActivity.class));
    }*/

    public void home(View view) {
        startActivity(new Intent(BeachDetails.this, DashboardActivity.class));

    }

    private void showRatingDialog(String placeName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_rating, null);
        builder.setView(view);

        MaterialRatingBar ratingBar = view.findViewById(R.id.ratingBar);
        EditText reviewText = view.findViewById(R.id.review_text);
        TextView charCount = view.findViewById(R.id.char_count);
        Button btnDismiss = view.findViewById(R.id.btn_dismiss);
        Button btnDone = view.findViewById(R.id.btn_done);
        reviewText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 512) {
                    charCount.setText(String.valueOf(s.length()));
                } else {
                    Toast.makeText(BeachDetails.this, "You have reached to maximum limit", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        AlertDialog dialog = builder.create();

        btnDismiss.setOnClickListener(v -> dialog.dismiss());

        btnDone.setOnClickListener(v -> {
            float rating = ratingBar.getRating();
            String review = reviewText.getText().toString();
            saveRatingToFirebase(placeName, rating, review);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void saveRatingToFirebase(String placeName, float rating, String review) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInAnonymously()
                .addOnSuccessListener(authResult -> {
                    String userId = authResult.getUser().getUid();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ratings").child(placeName).child(userId);
                    Map<String, Object> map = new HashMap<>();
                    map.put("rating", rating);
                    map.put("review", review);
                    map.put("timestamp", System.currentTimeMillis());

                    ref.setValue(map).addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Thanks for rating!", Toast.LENGTH_SHORT).show();
                    });
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Anonymous sign-in failed: " + e.getMessage());
                });


    }

    private void loadRating(String placeName, MaterialRatingBar ratingBar) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ratings").child(placeName);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                float total = 0;
                int count = 0;
                for (DataSnapshot child : snapshot.getChildren()) {
                    Float rating = child.child("rating").getValue(Float.class);
                    if (rating != null) {
                        total += rating;
                        count++;
                    }
                }

                if (count > 0) {
                    float average = total / count;
                    ratingBar.setRating(average);
                    // Optional: show total reviewers
                    count_rating.setText(average + "  (" + count + ")");
                    Log.d("Rating", "Average: " + average + ", Count: " + count);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Rating", "Error: " + error.getMessage());
            }
        });
    }

    private void fetchWeatherData(double lat, double lng) {

        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lng + "&appid=" + Constants.WEATHER_API + "&units=metric";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String city = jsonObject.getString("name");
                        JSONObject main = jsonObject.getJSONObject("main");
                        double temp = main.getDouble("temp");
                        JSONArray weatherArray = jsonObject.getJSONArray("weather");
                        JSONObject weather = weatherArray.getJSONObject(0);
                        String mainWeather = weather.getString("description");
                        String icon = weather.getString("icon");
                        String iconUrl = "https://openweathermap.org/img/wn/" + icon + "@2x.png";
                        Glide.with(this).load(iconUrl).into(weather_icon);
                        weather_txt.setText(temp + " °C");
                        Log.d("Weather", "City: " + city + ", Temp: " + temp + "°C" + jsonObject.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            error.printStackTrace();
        });

        queue.add(stringRequest);
    }

    private void fetchReviewsByPlace(String placeName) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ratings").child(placeName);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reviewList.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Review review = child.getValue(Review.class);
                    if (review != null) {
                        reviewList.add(review);
                    }
                }
                if (!reviewList.isEmpty()) {
                    review_text.setVisibility(View.VISIBLE);
                } else {
                    review_text.setVisibility(GONE);


                }
                reviewAdapter.setReviews(reviewList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BeachDetails.this, "Failed to load reviews", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
