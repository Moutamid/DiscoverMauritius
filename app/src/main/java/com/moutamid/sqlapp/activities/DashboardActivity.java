package com.moutamid.sqlapp.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.moutamid.sqlapp.R;
import com.moutamid.sqlapp.activities.AppInfo.AppInfoActivity;
import com.moutamid.sqlapp.activities.ContactUs.ContactUsActivity;
import com.moutamid.sqlapp.activities.Explore.ExploreActivity;
import com.moutamid.sqlapp.activities.InAppPurchase.SliderAdapterExample;
import com.moutamid.sqlapp.activities.Iteneraries.ItinerariesActivity;
import com.moutamid.sqlapp.activities.Organizer.OrganizerActivity;
import com.moutamid.sqlapp.activities.Stay.EatActivity;
import com.moutamid.sqlapp.activities.Stay.StayActivity;
import com.moutamid.sqlapp.activities.Tour.ToursActivity;
import com.moutamid.sqlapp.helper.Constants;
import com.moutamid.sqlapp.model.DatabaseHelper;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    public static LinearLayout premium_layout, faq_layout;
    public static TextView faq_txt, text1, text2;
    private static String PRODUCT_PREMIUM = "lifetime";
    RelativeLayout lifetime_premium;
    TextView restore_purchase;
    SliderView sliderView;
    SliderAdapterExample adapter;
    ImageView close, close_faq;
    LinearLayout logout_dailogue;
    TextView login_txt, cancel, yes;
    String[] firebaseImageNames = {
            "map1.kmz",
            "mauritius.map",
            "aapravasi_ghat_1.jpg",
            "aapravasi_ghat_2.jpg",
            "albion_lighthouse.jpg",
            "bagatelle_mall_1.jpg",
            "baie_de_larsenal.jpg",
            "baie_de_larsenal_2.jpg",
            "bel_ombre_1.jpg",
            "bel_ombre_11.jpg",
            "bel_ombre_12.jpg",
            "bel_ombre_16.jpg",
            "bel_ombre_17.jpg",
            "bel_ombre_18.jpg",
            "bel_ombre_6.jpg",
            "bel_ombre_9.jpg",
            "belle_mare_1.jpg",
            "belle_mare_2.jpg",
            "belle_mare_3.jpg",
            "black_river.jpg",
            "black_river_georges_2.jpg",
            "blue_bay.jpg",
            "blue_bay_2.jpg",
            "blue_bay_3.jpg",
            "blue_bay_4.jpg",
            "blue_bay_6.jpg",
            "blue_bay_8.jpg",
            "blue_penny_museum_1.jpg",
            "blue_penny_museum_2.jpg",
            "bois_cheri_1.jpg",
            "bois_cheri_4.jpg",
            "bois_cheri_6.jpg",
            "bois_cheri_7.jpg",
            "bois_cheri_8.jpg",
            "botanical_garden_1.jpg",
            "botanical_garden_2.jpg",
            "botanical_garden_3.jpg",
            "botanical_garden_4.jpg",
            "botanical_garden_5.jpg",
            "brabant_10.jpg",
            "brabant_11.jpg",
            "brabant_4.jpg",
            "brabant_6.jpg",
            "brabant_7.jpg",
            "brabant_8.jpg",
            "brabant_9.jpg",
            "cap_malheureux_1.jpg",
            "cap_malheureux_2.jpg",
            "cap_malheureux_6.jpg",
            "cap_malheureux_7.jpg",
            "cap_malheureux_9.jpg",
            "casela.jpg",
            "casela_3.jpg",
            "casela_4.jpg",
            "casela_6.jpg",
            "catamaran_cruise_west_coast_2.jpg",
            "chamarel_1.jpg",
            "chamarel_2.jpg",
            "champ_de_aars_racecourse.jpg",
            "chateau_de_labourdonnais.jpg",
            "chateau_de_labourdonnais_2.jpg",
            "chateau_de_labourdonnais_4.jpg",
            "church_pl.jpg",
            "citadelle.jpg",
            "coin_de_mire_10.jpg",
            "curious_corner_2.jpg",
            "curious_corner_3.jpg",
            "curious_corner_4.jpg",
            "curious_corner_5.jpg",
            "dodo.jpg",
            "eau_bleu_1.jpg",
            "eau_bleu_3.jpg",
            "eau_bleu_4.jpg",
            "ebony.jpg",
            "etymology.jpg",
            "eureka_house.jpg",
            "eureka_house_2.jpg",
            "eureka_house_3.jpg",
            "ferney_1.jpg",
            "ferney_3.jpg",
            "ferney_4.jpg",
            "ferney_conservation_park_1.jpg",
            "fody.jpg",
            "frederik_hendrik_museum_1.jpg",
            "frederik_hendrik_museum_2.jpg",
            "frederik_hendrik_museum_4.jpg",
            "frederik_hendrik_museum_5.jpg",
            "geko.jpg",
            "grand_baie_1.jpg",
            "grand_baie_3.jpg",
            "grand_bassin_1.jpg",
            "grand_bassin_2.jpg",
            "grand_bay_1.jpg",
            "grand_gaube_1.jpg",
            "gris_gris_1.jpg",
            "gris_gris_3.jpg",
            "gris_gris_4.jpg",
            "gris_gris_coastal_4.jpg",
            "grisgris_2.jpg",
            "gymkhana_2.jpg",
            "ile_aux_aigrettes_1.jpg",
            "ile_aux_aigrettes_2.jpg",
            "ile_aux_aigrettes_3.jpg",
            "ile_aux_aigrettes_4.jpg",
            "ile_aux_cerf_5.jpg",
            "ile_aux_cerfs_3.jpg",
            "ile_aux_cerfs_5_1.jpg",
            "ile_aux_cerfs_6.jpg",
            "ile_aux_cerfs_7.jpg",
            "ile_aux_cerfs_7_1.jpg",
            "ile_aux_cerfs_island_2.jpg",
            "ile_aux_cerfs_mauritius_1.jpg",
            "ile_aux_fouqets_3.jpg",
            "ile_aux_fouquets.jpg",
            "image6.jpeg",
            "image7.jpeg",
            "image8.jpeg",
            "img5.jpeg",
            "img_6.jpeg",
            "indian_labourers_arriving_in_mauritius.jpg",
            "isle_of_france.jpg",
            "jacques_henri_bernardin.jpg",
            "jardin_de_la_compagnie_1.jpg",
            "kite_surfing.jpg",
            "la_cambuse_2.jpg",
            "la_cambuse_3.jpg",
            "la_prairie_2.jpg",
            "la_preneuse_4.jpg",
            "la_roche_qui_pleure.jpg",
            "la_vanilla_1.jpg",
            "la_vanille_4.jpg",
            "la_vanille_6.jpg",
            "la_vanille_8.jpg",
            "la_vanille_9.jpg",
            "lantana.jpg",
            "laventure_du_sucre.jpg",
            "le_morne_1.jpg",
            "le_morne_beach_2.jpg",
            "le_morne_brabant.jpg",
            "le_pouce_1.jpg",
            "le_pouce_2.jpg",
            "le_pouce_3.jpg",
            "le_souffleur_1.jpg",
            "le_souffleur_2.jpg",
            "les_jardins_de_la_compagnie_2.jpg",
            "maconde_1.jpg",
            "maconde_2.jpg",
            "mahebourg.jpg",
            "mahebourg_3.jpg",
            "mahebourg_5.jpg",
            "mahebourg_6.jpg",
            "mahebourg_museum_2.jpg",
            "mahebourg_museum_4.jpg",
            "mahebourg_museum_5.jpg",
            "mahebourg_waterfront_1.jpg",
            "marie_reine_de_la_paix_3.jpg",
            "marie_reine_de_la_paix_4.jpg",
            "marie_reine_de_la_paix_5.jpg",
            "martello_tower_2.jpg",
            "martello_tower_3.jpg",
            "martello_tower_4.jpg",
            "matello_tower_1.jpg",
            "mauritius_aquarium_1.jpg",
            "mauritius_aquarium_2.jpg",
            "mauritius_culture.jpg",
            "mauritius_demography.jpg",
            "mauritius_economy.jpg",
            "mauritius_environment.jpg",
            "mauritius_flag.jpg",
            "mauritius_fody.jpg",
            "mauritius_geography.jpg",
            "mauritius_government.jpg",
            "mauritius_history.jpg",
            "mauritius_map_1.jpg",
            "mauritius_territory.jpg",
            "mauritius_wildlife.jpg",
            "metro_express_mauritius.jpg",
            "monkey_mauritius.jpg",
            "mont_choisy_2.jpg",
            "natural_history_musuem.jpg",
            "odysseo_1.jpg",
            "odysseo_2.jpg",
            "odysseo_3.jpg",
            "paddle_boarding.jpg",
            "pamplemousse_garden.jpg",
            "parasailing.jpg",
            "pereybere_beach_1.jpg",
            "pereybere_beach_3.jpg",
            "pereybere_beach_4.jpg",
            "pereybere_beach_5.jpg",
            "pereybere_beach_6.jpg",
            "photography_musuem_3.jpg",
            "pieter_both_1.jpg",
            "pieter_both_2.jpg",
            "pieter_both_3.jpg",
            "pink_pigeon.jpg",
            "pink_pigeon_image.jpg",
            "place_darmes_2.jpg",
            "place_des_armes.jpg",
            "pont_naturel_2.jpg",
            "pont_naturel_3.jpg",
            "port_louis_13.jpg",
            "port_louis_3.jpg",
            "port_louis_4.jpg",
            "port_louis_5.jpg",
            "port_louis_6.jpg",
            "port_louis_7.jpg",
            "port_louis_8.jpg",
            "port_louis_9.jpg",
            "port_louis_photography_museum.jpg",
            "port_louis_photography_museum_inside.jpg",
            "post_office.jpg",
            "poste_lafayette_1.jpg",
            "poste_lafayette_2.jpg",
            "poste_lafayette_3.jpg",
            "poste_lafayette_4.jpg",
            "red_church_inside.jpg",
            "red_roof_church.jpg",
            "red_roof_church_2.jpg",
            "rhumerie_2.jpg",
            "rhumerie_de_chamarel_1.jpg",
            "rhumerie_de_chamarel_3.jpg",
            "riviere_des_galets_1.jpg",
            "riviere_des_galets_4.jpg",
            "roche_noire_2.jpg",
            "roche_noire_3.jpg",
            "rochester_falls_1.jpg",
            "shore_1.jpg",
            "shrimps.jpg",
            "st_brandon.jpg",
            "st_felix_1.jpg",
            "stamp_mauritius.jpg",
            "sugar_museum_pamplemousses.jpg",
            "tamarin_1.jpg",
            "tamarin_2.jpg",
            "tamarin_3.jpg",
            "tamarin_falls_1.jpg",
            "tamarin_falls_2.jpg",
            "test_imag.jpg",
            "text1.jpg",
            "trou_aux_biches_1.jpg",
            "trou_aux_biches_2.jpg",
            "trou_aux_cerfs_1.jpg",
            "trou_aux_cerfs_4.jpg",
            "valle_des_couleurs.jpg",
            "valle_des_couleurs_12.jpg",
            "valle_des_couleurs_17.jpg",
            "valle_des_couleurs_2.jpg",
            "valle_des_couleurs_5.jpg",
            "valle_des_couleurs_9.jpg",
            "vallee_des_couleurs_1.jpg",
            "folder_transparent.png",
            "google_plus.png",
            "googlelogo_dark20_color_132x44.png",
            "ic_eye_closed.png",
            "ic_eye_open.png",
            "image1.PNG",
            "image2.PNG",
            "image3.PNG",
            "img.png",
            "img_1.png",
            "img_2.png",
            "img_4.png",
            "img_6.jpeg",

            "ferney_1.jpg",
            "frederik_hendrik_museum_1.jpg",
            "belle_mare_1.jpg",
            "poste_lafayette_1.jpg",
            "roche_noire_2.jpg",
            "ile_aux_cerfs_mauritius_1.jpg",
            "red_roof_church.jpg",
            "pamplemousse_garden.jpg",
            "sugar_museum_pamplemousses.jpg",
            "baie_de_larsenal_2.jpg",
            "grand_baie_1.jpg",
            "port_louis_3.jpg",
            "port_louis_4.jpg",
            "port_louis_9.jpg",
            "place_des_armes.jpg",
            "blue_penny_museum_2.jpg",
            "church_pl.jpg",
            "natural_history_musuem.jpg",
            "aapravasi_ghat_1.jpg",
            "post_office.jpg",
            "citadelle.jpg",
            "port_louis_photography_museum.jpg",
            "marie_reine_de_la_paix_3.jpg",
            "jardin_de_la_compagnie_1.jpg",
            "odysseo_1.jpg",
            "chateau_de_labourdonnais.jpg",
            "gris_gris_coastal_4.jpg",
            "rochester_falls_1.jpg",
            "bel_ombre_1.jpg",
            "valle_des_couleurs.jpg",
            "blue_bay.jpg",
            "mahebourg.jpg",
            "mahebourg_museum_2.jpg",
            "ile_aux_fouquets.jpg",
            "ile_aux_aigrettes_1.jpg",
            "bagatelle_mall_1.jpg",
            "bois_cheri_1.jpg",
            "eureka_house.jpg",
            "grand_bassin_1.jpg",
            "gymkhana_2.jpg",
            "le_pouce_1.jpg",
            "pieter_both_1.jpg",
            "tamarin_falls_1.jpg",
            "trou_aux_cerfs_4.jpg",
            "albion_lighthouse.jpg",
            "flic_en_flac_3.jpg",
            "casela.jpg",
            "tamarin_3.jpg",
            "la_preneuse_4.jpg",
            "martello_tower_4.jpg",
            "le_morne_1.jpg",
            "maconde_1.jpg",
            "chamarel_2.jpg",
            "chamarel_1.jpg",
            "black_river_georges_2.jpg",
            "le_morne_beach_2.jpg",
            "rhumerie_de_chamarel_1.jpg",
            "curious_corner_2.jpg",
            "flic_en_flac_1.jpg"
    };
    private BillingClient billingClient;

    public static void checkApp(Activity activity) {
        String appName = "My Trips2";

        new Thread(() -> {
            URL google = null;
            try {
                google = new URL("https://raw.githubusercontent.com/Moutamid/Moutamid/main/apps.txt");
            } catch (final MalformedURLException e) {
                e.printStackTrace();
            }
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(google != null ? google.openStream() : null));
            } catch (final IOException e) {
                e.printStackTrace();
            }
            String input = null;
            StringBuffer stringBuffer = new StringBuffer();
            while (true) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if ((input = in != null ? in.readLine() : null) == null) break;
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                }
                stringBuffer.append(input);
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
            String htmlData = stringBuffer.toString();

            try {
                JSONObject myAppObject = new JSONObject(htmlData).getJSONObject(appName);

                boolean value = myAppObject.getBoolean("value");
                String msg = myAppObject.getString("msg");

                if (value) {
                    activity.runOnUiThread(() -> {
                        new AlertDialog.Builder(activity)
                                .setMessage(msg)
                                .setCancelable(false)
                                .show();
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        logout_dailogue = findViewById(R.id.logout_dailogue);
        cancel = findViewById(R.id.cancel);
        yes = findViewById(R.id.yes);
        getAllImagesOnce();


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout_dailogue.setVisibility(View.GONE);
            }
        });
        /*yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                login_txt.setText("Login");
                logout_dailogue.setVisibility(View.GONE);
            }
        });*/
        premium();
        checkApp(DashboardActivity.this);
        if (Stash.getBoolean("wasRunning", false)) {
            handleAppReopen();

        }
//
        Stash.put("wasRunning", false);

        findViewById(R.id.eatBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, EatActivity.class));
            }
        });

        findViewById(R.id.toursBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, ToursActivity.class));
            }
        });


    }

    private void handleAppReopen() {
        if (!Stash.getBoolean(Constants.IS_PREMIUM, false)) {

            com.moutamid.sqlapp.model.DatabaseHelper db = new DatabaseHelper(DashboardActivity.this);
            db.deleteAllBeacModels();
        }

    }

    public void explore(View view) {
        startActivity(new Intent(DashboardActivity.this, ExploreActivity.class));
    }

    public void my_trips(View view) {
        startActivity(new Intent(DashboardActivity.this, MyTripsActivity.class));
    }

    public void iternties(View view) {
        startActivity(new Intent(DashboardActivity.this, ItinerariesActivity.class));

    }

    public void organier(View view) {
        startActivity(new Intent(DashboardActivity.this, OrganizerActivity.class));
    }

    public void contact_us(View view) {
        startActivity(new Intent(DashboardActivity.this, ContactUsActivity.class));
    }

    public void tips(View view) {
        startActivity(new Intent(DashboardActivity.this, TravelTipsActivity.class));

    }

    public void about(View view) {
        startActivity(new Intent(DashboardActivity.this, AppInfoActivity.class));

    }

    public void login(View view) {
        if (!Stash.getBoolean(Constants.IS_PREMIUM, false)) {
            findViewById(R.id.login_layout).setVisibility(View.VISIBLE);
        } else {
            if (Stash.getBoolean(Constants.IS_LOGGED_IN, false)) {
                new androidx.appcompat.app.AlertDialog.Builder(DashboardActivity.this)
                        .setTitle("Discover Mauritius")
                        .setMessage("Are you sure you want to log out?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            Toast.makeText(DashboardActivity.this, "Logged out!", Toast.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().signOut();
                            Stash.put(Constants.IS_LOGGED_IN, false);
                            login_txt.setText("Login");
                            Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                            startActivity(intent);
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
            } else {
                startActivity(new Intent(DashboardActivity.this, LoginActivity.class));

            }
        }
    }

    public void menu(View view) {
        findViewById(R.id.login_layout).setVisibility(View.GONE);
    }

    public void premium(View view) {
        if (Stash.getBoolean(Constants.IS_PREMIUM, false)) {
            findViewById(R.id.login_layout).setVisibility(View.GONE);
            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
        } else {
            premium_layout.setVisibility(View.VISIBLE);
            findViewById(R.id.login_layout).setVisibility(View.GONE);
        }


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
                    Toast.makeText(DashboardActivity.this, "Create account to become a premium user", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DashboardActivity.this, CreateAccountActivity.class));
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
                        Toast.makeText(DashboardActivity.this, "Create account to become a premium user", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(DashboardActivity.this, CreateAccountActivity.class));
                    } else {
                        GetSubPurchases();
                    }

                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        logout_dailogue = findViewById(R.id.logout_dailogue);
        login_txt = findViewById(R.id.login_txt_dashboard);
        cancel = findViewById(R.id.cancel);
        yes = findViewById(R.id.yes);
        if (Stash.getBoolean(Constants.IS_LOGGED_IN, false)) {
            login_txt.setText("Logout");
        } else {
            login_txt.setText("Login");
        }
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout_dailogue.setVisibility(View.GONE);
            }
        });
        /*yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                login_txt.setText("Login");
                logout_dailogue.setVisibility(View.GONE);
            }
        });
*/
    }

    void establishConnection() {
        billingClient = BillingClient.newBuilder(this)
                .enablePendingPurchases()
                .setListener((billingResult, purchases) -> {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
                        for (Purchase purchase : purchases) {
                            verifySubPurchase(purchase);
                        }
                    }
                }).build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    Log.d("TAG", "Connection Established");
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
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
                            Stash.put(Constants.IS_PREMIUM, true);

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
                                                Toast.makeText(DashboardActivity.this, "Premium Restored", Toast.LENGTH_SHORT).show();
                                                Stash.put(Constants.IS_PREMIUM, true);

                                                Log.d("TAG", "Product id " + PRODUCT_PREMIUM + " will restore here");
                                            }
                                        }
                                    } else {
                                        Toast.makeText(DashboardActivity.this, "Nothing found at restore", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    public void tours(View view) {
        startActivity(new Intent(this, ToursActivity.class));
    }

    public void stay(View view) {
        startActivity(new Intent(this, StayActivity.class));
    }


    private void getAllImagesOnce() {
        File localDir = new File(getFilesDir(), "cached_images");
        if (!localDir.exists()) localDir.mkdirs();

        boolean anyImageMissing = false;
        for (String imageName : firebaseImageNames) {
            File localFile = new File(localDir, imageName);
            if (!localFile.exists()) {
                anyImageMissing = true;
                break; // we only need to know if at least one is missing
            }
        }

        if (anyImageMissing) {

            FirebaseStorage storage = FirebaseStorage.getInstance();
            downloadImagesWithProgress(localDir, storage);
        }
    }

    private void downloadImagesWithProgress(File localDir, FirebaseStorage storage) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading App Data");
        progressDialog.setMessage("0% downloaded");
        progressDialog.setCancelable(false);
        progressDialog.show();

        int[] currentProgress = {0};
        int totalImages = firebaseImageNames.length;

        for (String imageName : firebaseImageNames) {
            File localFile = new File(localDir, imageName);
            if (!localFile.exists()) {
                StorageReference ref = storage.getReference().child("images/" + imageName);
                ref.getFile(localFile)
                        .addOnSuccessListener(taskSnapshot -> {
                            Log.d("ImageDownload", "Downloaded: " + imageName);
                            currentProgress[0]++;
                            int percentage = (currentProgress[0] * 100) / totalImages;
                            progressDialog.setMessage(percentage + "% downloaded");

                            if (currentProgress[0] == totalImages) {
                                progressDialog.dismiss();
                                Toast.makeText(this, "All images downloaded successfully!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(e -> {
                            Log.e("ImageDownload", "Failed to download " + imageName, e);
                            Toast.makeText(this, "Failed to download: " + imageName, Toast.LENGTH_SHORT).show();
                        });
            } else {
                Log.d("ImageDownload", "Already exists: " + imageName);
                currentProgress[0]++;
                int percentage = (currentProgress[0] * 100) / totalImages;
                progressDialog.setMessage(percentage + "% downloaded");

                if (currentProgress[0] == totalImages) {
                    progressDialog.dismiss();
                    Toast.makeText(this, "All images downloaded successfully!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}