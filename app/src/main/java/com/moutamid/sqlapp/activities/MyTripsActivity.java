package com.moutamid.sqlapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.ProductDetailsResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.billingclient.api.QueryPurchasesParams;
import com.fxn.stash.Stash;
import com.moutamid.sqlapp.R;
import com.moutamid.sqlapp.activities.AppInfo.AppInfoActivity;
import com.moutamid.sqlapp.activities.Beaches.BeachDetails;
import com.moutamid.sqlapp.activities.ContactUs.ContactUsActivity;
import com.moutamid.sqlapp.activities.Explore.ExploreActivity;
import com.moutamid.sqlapp.activities.InAppPurchase.SliderAdapterExample;
import com.moutamid.sqlapp.activities.Iteneraries.ItinerariesActivity;
import com.moutamid.sqlapp.activities.Organizer.CreateActivity;
import com.moutamid.sqlapp.activities.Organizer.MyDocsActivity;
import com.moutamid.sqlapp.activities.Organizer.OrganizerActivity;
import com.moutamid.sqlapp.adapter.MyAdapter;
import com.moutamid.sqlapp.helper.Constants;
import com.moutamid.sqlapp.helper.Utils;
import com.moutamid.sqlapp.model.BeacModel;
import com.moutamid.sqlapp.model.DatabaseHelper;
import com.moutamid.sqlapp.offlinemap.FullMapActivity;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyTripsActivity extends AppCompatActivity implements MyAdapter.OnStartDragListener {
    private DatabaseHelper databaseHelper;
    private List<BeacModel> beacModels;
    private MyAdapter adapter;
    private ItemTouchHelper itemTouchHelper;
    public static TextView time, distance, total_stop;
    Button map_it_btn;
    public static LinearLayout premium_layout, faq_layout;
    RelativeLayout lifetime_premium;
    TextView restore_purchase;
    SliderView sliderView;
    SliderAdapterExample adapterslider;
    ImageView close, close_faq;
    public static TextView faq_txt, text1, text2;
    private static String PRODUCT_PREMIUM = "lifetime";
    private BillingClient billingClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trips);
        premium();
        Utils.loginBtnMenuListener(this);

        databaseHelper = new DatabaseHelper(MyTripsActivity.this);
        beacModels = databaseHelper.getAllBeacModels();
        updatePositions();
        RecyclerView recyclerView = findViewById(R.id.listView);
        time = findViewById(R.id.time);
        distance = findViewById(R.id.distance);
        total_stop = findViewById(R.id.total_stop);
        map_it_btn = findViewById(R.id.map_it_btn);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter(beacModels, MyTripsActivity.this, this);
        recyclerView.setAdapter(adapter);
        ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                return makeMovementFlags(dragFlags, 0);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                beacModels.get(fromPosition).id_copy = toPosition;
                beacModels.get(toPosition).id_copy = fromPosition;
                Collections.swap(beacModels, fromPosition, toPosition);
                adapter.notifyItemMoved(fromPosition, toPosition);
                adapter.notifyItemChanged(fromPosition);
                adapter.notifyItemChanged(toPosition);
                databaseHelper.updateBeacModel(beacModels.get(fromPosition)); // Implement this method in your DatabaseHelper class
                databaseHelper.updateBeacModel(beacModels.get(toPosition)); // Implement this method in your DatabaseHelper class
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }
        };

        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        map_it_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Stash.getBoolean(Constants.IS_PREMIUM, false)) {
                    passLatLngListToNextActivity();      }
                else {
                    premium_layout.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    private void passLatLngListToNextActivity() {
        Intent intent = new Intent(MyTripsActivity.this, FullMapActivity.class); // Change NextActivity to your target activity

        double[] latitudes = new double[beacModels.size()];
        double[] longitudes = new double[beacModels.size()];

        for (int i = 0; i < beacModels.size(); i++) {
            latitudes[i] = beacModels.get(i).lat;
            longitudes[i] = beacModels.get(i).lng;
        }

        intent.putExtra("latitudes", latitudes);
        intent.putExtra("longitudes", longitudes);
        startActivity(intent);
    }

    private void updatePositions() {
        for (int i = 0; i < beacModels.size(); i++) {
            BeacModel model = beacModels.get(i);
            model.id_copy = i + 1;
            // Update the model in the database
            databaseHelper.updateBeacModel(model); // Assuming you have a method to update BeacModel in the database
        }
    }


    public void BackPress(View view) {
        onBackPressed();
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
        startActivity(new Intent(MyTripsActivity.this, ExploreActivity.class));
    }

    public void my_trips(View view) {
        startActivity(new Intent(MyTripsActivity.this, MyTripsActivity.class));
    }

    public void iternties(View view) {
        startActivity(new Intent(MyTripsActivity.this, ItinerariesActivity.class));

    }

    public void organier(View view) {
        startActivity(new Intent(MyTripsActivity.this, OrganizerActivity.class));
    }

    public void contact_us(View view) {
        startActivity(new Intent(MyTripsActivity.this, ContactUsActivity.class));
    }


    public void tips(View view) {
        startActivity(new Intent(MyTripsActivity.this, TravelTipsActivity.class));

    }

    public void about(View view) {
        startActivity(new Intent(MyTripsActivity.this, AppInfoActivity.class));

    }

    /*public void login(View view) {
        startActivity(new Intent(MyTripsActivity.this, LoginActivity.class));
    }*/

    public void home(View view) {
        startActivity(new Intent(MyTripsActivity.this, DashboardActivity.class));

    }


    @Override
    public void onStartDrag(MyAdapter.ViewHolder holder) {
        // Start dragging the item
        itemTouchHelper.startDrag(holder);
    }

    @Override
    protected void onResume() {
        super.onResume();
        databaseHelper = new DatabaseHelper(MyTripsActivity.this);
        beacModels = databaseHelper.getAllBeacModels();
        updatePositions();
        RecyclerView recyclerView = findViewById(R.id.listView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter(beacModels, MyTripsActivity.this, this);
        recyclerView.setAdapter(adapter);
        billingClient.queryPurchasesAsync(
                QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.SUBS).build(),
                (billingResult, list) -> {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        for (Purchase purchase : list) {
                            if (purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged()) {
                                verifySubPurchase(purchase);
                            }
                        }
                    }
                }
        );
    }

    public void premium() {
        TextView already_purchased;
        already_purchased = findViewById(R.id.already_purchased);
        restore_purchase = findViewById(R.id.restore_purchase);
  lifetime_premium = findViewById(R.id.lifetime_premium);
        lifetime_premium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Stash.getBoolean(Constants.IS_LOGGED_IN, false)) {
                    premium_layout.setVisibility(View.GONE);
                    faq_layout.setVisibility(View.GONE);
                    Toast.makeText(MyTripsActivity.this, "Create account to become a premium user", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MyTripsActivity.this, CreateAccountActivity.class));
                } else {
                    GetSubPurchases();
                }

            }
        });
        premium_layout = findViewById(R.id.premium_layout);
        faq_layout = findViewById(R.id.faq_layout);
        close = findViewById(R.id.close);
        sliderView = findViewById(R.id.slider);
        close_faq = findViewById(R.id.close_faq);
        faq_txt = findViewById(R.id.faq_txt);
        text1 = findViewById(R.id.text111);
        text2 = findViewById(R.id.text112);
        faq_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                faq_layout.setVisibility(View.VISIBLE);
            }
        });
        billingClient = BillingClient.newBuilder(this)
                .enablePendingPurchases()
                .setListener(
                        (billingResult, list) -> {

                            if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
                                for (Purchase purchase : list) {
                                    verifySubPurchase(purchase);
                                }
                            }
                        }
                ).build();

        //start the connection after initializing the billing client
        establishConnection();
        List<String> sliderData = new ArrayList<>();
        File cacheDir = new File(getFilesDir(), "cached_images");
        String fullPath = cacheDir.getAbsolutePath();
        sliderData.add(fullPath + "/" + "img_1" + ".jpg");
        sliderData.add(fullPath + "/" + "img_2" + ".jpg");
        sliderData.add(fullPath + "/" + "img5" + ".jpg");
        sliderData.add(fullPath + "/" + "img_4" + ".jpg");
        sliderData.add(fullPath + "/" + "img_6" + ".jpg");

        adapterslider = new SliderAdapterExample(this, sliderData);
        sliderView.setSliderAdapter(adapterslider);
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
                premium_layout.setVisibility(View.GONE);
            }
        });
        close_faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                faq_layout.setVisibility(View.GONE);
            }
        });
        if (!Stash.getBoolean(Constants.IS_PREMIUM, false)) {
            already_purchased.setText("You are not premium user?");
            restore_purchase.setText("Purchase Now");
        }
        restore_purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (restore_purchase.getText().toString().equals("Restore purchase")) {
                    restorePurchases();
                } else {
                    if (!Stash.getBoolean(Constants.IS_LOGGED_IN, false)) {
                        premium_layout.setVisibility(View.GONE);
                        faq_layout.setVisibility(View.GONE);
                        Toast.makeText(MyTripsActivity.this, "Create account to become a premium user", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MyTripsActivity.this, CreateAccountActivity.class));
                    } else {
                        GetSubPurchases();
                    }
                }
            }
        });
    }


    void establishConnection() {
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    //Use any of function below to get details upon successful connection
                    Log.d("TAG", "Connection Established");
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                Log.d("TAG", "Connection NOT Established");
                establishConnection();
            }
        });
    }

    void GetSubPurchases() {
        ArrayList<QueryProductDetailsParams.Product> productList = new ArrayList<>();

        productList.add(
                QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(PRODUCT_PREMIUM)
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build()
        );

        QueryProductDetailsParams params = QueryProductDetailsParams.newBuilder()
                .setProductList(productList)
                .build();


        billingClient.queryProductDetailsAsync(params, new ProductDetailsResponseListener() {
            @Override
            public void onProductDetailsResponse(@NonNull BillingResult billingResult, @NonNull List<ProductDetails> list) {
                LaunchSubPurchase(list.get(0));
                Log.d("TAG", "Product Price" + list.get(0).getSubscriptionOfferDetails().get(0).getPricingPhases().getPricingPhaseList().get(0).getFormattedPrice());

            }
        });
    }

    void LaunchSubPurchase(ProductDetails productDetails) {
        assert productDetails.getSubscriptionOfferDetails() != null;
        ArrayList<BillingFlowParams.ProductDetailsParams> productList = new ArrayList<>();

        productList.add(
                BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(productDetails)
                        .setOfferToken(productDetails.getSubscriptionOfferDetails().get(0).getOfferToken())
                        .build()
        );

        BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(productList)
                .build();

        billingClient.launchBillingFlow(this, billingFlowParams);
    }

    void verifySubPurchase(Purchase purchases) {
        if (!purchases.isAcknowledged()) {
            billingClient.acknowledgePurchase(AcknowledgePurchaseParams
                    .newBuilder()
                    .setPurchaseToken(purchases.getPurchaseToken())
                    .build(), billingResult -> {

                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    for (String pur : purchases.getProducts()) {
                        if (pur.equalsIgnoreCase(PRODUCT_PREMIUM)) {
                            Log.d("TAG", "Purchase is successful" + pur);
                            Toast.makeText(this, "Purchase is Successful", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(this, "kjdhhjd", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    void restorePurchases() {

        billingClient = BillingClient.newBuilder(this).enablePendingPurchases().setListener((billingResult, list) -> {
        }).build();
        final BillingClient finalBillingClient = billingClient;
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingServiceDisconnected() {
            }

            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {

                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    finalBillingClient.queryPurchasesAsync(
                            QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.SUBS).build(), (billingResult1, list) -> {
                                if (billingResult1.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                                    if (list.size() > 0) {
                                        for (int i = 0; i < list.size(); i++) {
                                            if (list.get(i).getProducts().contains(PRODUCT_PREMIUM)) {
                                                Toast.makeText(MyTripsActivity.this, "Premium Restored", Toast.LENGTH_SHORT).show();

                                                Log.d("TAG", "Product id " + PRODUCT_PREMIUM + " will restore here");
                                            }
                                        }
                                    } else {
                                        Toast.makeText(MyTripsActivity.this, "Nothing found at restore", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}
