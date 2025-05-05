package com.moutamid.sqlapp.activities.Organizer;

import static android.view.View.GONE;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.fxn.stash.Stash;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.moutamid.sqlapp.R;
import com.moutamid.sqlapp.activities.AppInfo.AppInfoActivity;
import com.moutamid.sqlapp.activities.Beaches.BeachesTypeActivity;
import com.moutamid.sqlapp.activities.Calender.calenderapp.MainActivity;
import com.moutamid.sqlapp.activities.ContactUs.ContactUsActivity;
import com.moutamid.sqlapp.activities.CreateAccountActivity;
import com.moutamid.sqlapp.activities.DashboardActivity;
import com.moutamid.sqlapp.activities.Explore.ExploreActivity;
import com.moutamid.sqlapp.activities.InAppPurchase.SliderAdapterExample;
import com.moutamid.sqlapp.activities.Iteneraries.ItinerariesActivity;
import com.moutamid.sqlapp.activities.MyTripsActivity;
import com.moutamid.sqlapp.activities.TravelTipsActivity;
import com.moutamid.sqlapp.helper.Constants;
import com.moutamid.sqlapp.helper.Utils;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OrganizerActivity extends AppCompatActivity {
    LinearLayout my_docs, my_calender;
    public static LinearLayout premium_layout, faq_layout;
    RelativeLayout lifetime_premium;
    TextView restore_purchase;
    SliderView sliderView;
    SliderAdapterExample adapter;
    ImageView close, close_faq;
    public static TextView faq_txt, text1, text2;
    private final String LIFETIME_SUBSCRIPTION_ID = "com.moutamid.sqlapp.lifetime"; // Replace with your product ID

    private BillingClient billingClient;
    private final PurchasesUpdatedListener purchasesUpdatedListener = (billingResult, purchases) -> {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (Purchase purchase : purchases) {
                if (purchase.getProducts().contains(LIFETIME_SUBSCRIPTION_ID)) {
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
        setContentView(R.layout.activity_organizer);
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
                    Toast.makeText(OrganizerActivity.this, "Billing connected", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                Toast.makeText(OrganizerActivity.this, "Billing disconnected", Toast.LENGTH_SHORT).show();
            }
        });
        my_docs = findViewById(R.id.my_docs);
        my_calender = findViewById(R.id.my_calender);
        my_docs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Stash.getBoolean(Constants.IS_PREMIUM, false)) {
                    startActivity(new Intent(OrganizerActivity.this, MyDocsActivity.class));
                }
                else {
                    premium_layout.setVisibility(View.VISIBLE);
                }
            }
        });      my_calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Stash.getBoolean(Constants.IS_PREMIUM, false)) {
                    startActivity(new Intent(OrganizerActivity.this, MainActivity.class));
                }
                else {
                    premium_layout.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    public void BackPress(View view) {
        startActivity(new Intent(OrganizerActivity.this, DashboardActivity.class));
    finishAffinity();}

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
        startActivity(new Intent(OrganizerActivity.this, ExploreActivity.class));
    }

    public void my_trips(View view) {
        startActivity(new Intent(OrganizerActivity.this, MyTripsActivity.class));
    }

    public void iternties(View view) {
        startActivity(new Intent(OrganizerActivity.this, ItinerariesActivity.class));

    }

    public void organier(View view) {
        startActivity(new Intent(OrganizerActivity.this, OrganizerActivity.class));
    }

    public void contact_us(View view) {
        startActivity(new Intent(OrganizerActivity.this, ContactUsActivity.class));
    }


    public void tips(View view) {
        startActivity(new Intent(OrganizerActivity.this, TravelTipsActivity.class));

    }

    public void about(View view) {
        startActivity(new Intent(OrganizerActivity.this, AppInfoActivity.class));

    }

    /*public void login(View view) {
        startActivity(new Intent(OrganizerActivity.this, LoginActivity.class));
    }*/

    public void home(View view) {
        startActivity(new Intent(OrganizerActivity.this, DashboardActivity.class));

    }
    public void premium() {
        TextView already_purchased, premium_amount;
        already_purchased = findViewById(R.id.already_purchased);
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
        premium_amount = findViewById(R.id.premium_amount);
        premium_amount.setText(Stash.getString(Constants.PRODUCT_PREMIUM_VALUE));

        lifetime_premium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Stash.getBoolean(Constants.IS_LOGGED_IN, false)) {
                    premium_layout.setVisibility(GONE);
                    faq_layout.setVisibility(GONE);
                    Toast.makeText(OrganizerActivity.this, "Create account to become a premium user", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(OrganizerActivity.this, CreateAccountActivity.class));
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
        } else {
            already_purchased.setVisibility(GONE);
            restore_purchase.setVisibility(GONE);
        }
    }
   
    private void initiatePurchase() {
        List<QueryProductDetailsParams.Product> products = new ArrayList<>();
        products.add(QueryProductDetailsParams.Product.newBuilder()
                .setProductId(LIFETIME_SUBSCRIPTION_ID)
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

                billingClient.launchBillingFlow(OrganizerActivity.this, billingFlowParams);
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
                    Toast.makeText(OrganizerActivity.this, "VIP status saved", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(OrganizerActivity.this, "Failed to save VIP status", Toast.LENGTH_SHORT).show();
                });
    }
}