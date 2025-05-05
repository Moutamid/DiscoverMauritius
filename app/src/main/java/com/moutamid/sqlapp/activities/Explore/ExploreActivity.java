package com.moutamid.sqlapp.activities.Explore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.moutamid.sqlapp.R;
import com.moutamid.sqlapp.activities.AboutMauritius.AboutActivity;
import com.moutamid.sqlapp.activities.Beaches.BeachesActivity;
import com.moutamid.sqlapp.activities.ContactUs.ContactUsActivity;
import com.moutamid.sqlapp.activities.DashboardActivity;
import com.moutamid.sqlapp.activities.Iteneraries.ItinerariesActivity;
import com.moutamid.sqlapp.activities.MyTripsActivity;
import com.moutamid.sqlapp.activities.Organizer.OrganizerActivity;
import com.moutamid.sqlapp.activities.TourshipActivity;
import com.moutamid.sqlapp.activities.TravelTipsActivity;
import com.moutamid.sqlapp.helper.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExploreActivity extends AppCompatActivity {
    List<String> imagesList = new ArrayList<>();
    String fullPath;
    ImageView ile_aux_cerf_5, port_louis_7, red_roof_church_2, bel_ombre_11,
            trou_aux_cerfs_1, belle_mare_1, flag_of_mauritius, mauritius_map_1;

    @Override
protected void onCreate(Bundle savedInstanceState) {
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        Utils.loginBtnMenuListener(this);
        File cacheDir = new File(getFilesDir(), "cached_images");
        fullPath = cacheDir.getAbsolutePath();
        ile_aux_cerf_5 = findViewById(R.id.ile_aux_cerf_5);
        port_louis_7 = findViewById(R.id.port_louis_7);
        red_roof_church_2 = findViewById(R.id.red_roof_church_2);
        bel_ombre_11 = findViewById(R.id.bel_ombre_11);
        trou_aux_cerfs_1 = findViewById(R.id.trou_aux_cerfs_1);
        belle_mare_1 = findViewById(R.id.belle_mare_1);
        flag_of_mauritius = findViewById(R.id.flag_of_mauritius);
        mauritius_map_1 = findViewById(R.id.mauritius_map_1);

// Load images using Glide
        Glide.with(this).load(new File(fullPath + "/ile_aux_cerf_5.jpg")).into(ile_aux_cerf_5);
        Glide.with(this).load(new File(fullPath + "/port_louis_7.jpg")).into(port_louis_7);
        Glide.with(this).load(new File(fullPath + "/red_roof_church_2.jpg")).into(red_roof_church_2);
        Glide.with(this).load(new File(fullPath + "/bel_ombre_11.jpg")).into(bel_ombre_11);
        Glide.with(this).load(new File(fullPath + "/trou_aux_cerfs_1.jpg")).into(trou_aux_cerfs_1);
        Glide.with(this).load(new File(fullPath + "/belle_mare_1.jpg")).into(belle_mare_1);
        Glide.with(this).load(new File(fullPath + "/mauritius_flag.jpg")).into(flag_of_mauritius);
        Glide.with(this).load(new File(fullPath + "/mauritius_map_1.jpg")).into(mauritius_map_1);


    }

    public void BackPress(View view) {
        onBackPressed();
    }

    public void beaches(View view) {
        startActivity(new Intent(ExploreActivity.this, BeachesActivity.class));
    }

    public void west(View view) {
        String[] itemName1 = {
                "Albion Lighthouse",//1
                "Flic en Flac",//2
                "Casela World of Adventures",//3
                "Tamarin Bay Beach",//4
                "La Preneuse Beach",//5
                "Martello Tower Heritage Museum",//6
                "Le Morne Brabant",//7
                "Maconde Viewpoint",//8
                "Seven Coloured Earths",//9
                "Chamarel Waterfalls",//10
                "Black River Gorges National Park",//11
                "Le Morne Beach",//12
                "Rhumerie de Chamarel",//13
                "Curious Corner"//14
        };


        String[] itemImage = {
                "albion_lighthouse",//1
                "flic_en_flac_3", //2
                "casela", //3
                "tamarin_3",//4
                "la_preneuse_4", //5
                "martello_tower_4",//6
                "le_morne_1",//7
                "maconde_1",//8
                "chamarel_2", //9
                "chamarel_1",//10
                "black_river_georges_2", //11
                "le_morne_beach_2",//12
                "rhumerie_de_chamarel_1",//13
                "curious_corner_2"//14
        };


        double[] itemLatitudes = {
                -20.1912902,//1
                -20.2993385,//2
                -20.2911619,//3
                -20.3262782,//4
                -20.3547236,//5
                -20.3546962, //6
                -20.4499767,//7
                -20.4911178,//8
                -20.4401637,//9
                -20.4432469,//10
                -20.4267316,//11
                -20.4529630, //12
                -20.4279001, //13
                -20.4387179//14
        };
        double[] itemLongitudes = {
                57.4114837,//1
                57.3636901,//2
                57.4040171,//3
                57.3778870,//4
                57.3614249,//5
                57.3619205,//6
                57.3165315,//7
                57.3711084,//8
                57.3733048,//9
                57.3857878,//10
                57.4512266,//11
                57.3125745,//12
                57.3963121,//13
                57.3902818//14
        };
        move_next("West", itemImage, itemName1, itemLatitudes, itemLongitudes);

    }

    public void east(View view) {
        String[] itemName1 = {
                "La Vallee de Ferney",//1
                "Vieux Grand Port Heritage Site",//2
                "Belle Mare Beach",//3
                "Poste Lafayette Beach",//4
                "Roche Noire Beach",//5
                "Ile aux Cerfs Beach"//6
        };
        String[] itemImages = {
              "ferney_1",//1
              "frederik_hendrik_museum_1",//2
              "belle_mare_1",//3
              "poste_lafayette_1",//4
              "roche_noire_2",//5
              "ile_aux_cerfs_mauritius_1"//6
        };

        double[] itemLatitudes = {
                -20.3646662,//1
                -20.3748935, //2
                -20.1928215, //3
                -20.1272669, //4
                -20.10417362842651,//5
                -20.2668829//6
        };
        double[] itemLongitudes = {
                57.7045690, //1
                57.7220704, //2
                57.7750124, //3
                57.7569250,//4
                57.725583766140815,//5
                57.8057047//6
        };

        move_next("East", itemImages, itemName1, itemLatitudes, itemLongitudes);

    }

    public void north(View view) {
        String[] itemName1 = {
                "Notre-Dame Chapel",//1
                "SSR Botanical Garden",//2
                "L’Aventure du Sucre Museum",//3
                "Baie de L’Arsenal Ruins",//4
                "Grand Bay",//6
                "Le Caudan Waterfront",//7
                "Port Louis Central Market",//8
                "Government House",//9
                "Place D’Armes",//10
                "Blue Penny Museum",//11
                "St Louis Cathedral",//12
                "Natural History Museum",//13
                "Aapravasi Ghat",//14
                "Mauritius Postal Museum",//15
                "Forte Adelaide",//16
                "Bréville Photography Museum",//17
                "Marie Reine de la Paix Chapel",//18
                "Jardin de la Compagnie",//19
                "Odysseo Oceanarium", //20
                "Chateau de Labourdonnais" //21
        };
        String[] itemImages = {
              "red_roof_church",//1
              "pamplemousse_garden",//2
              "sugar_museum_pamplemousses",//3
              "baie_de_larsenal_2",//4
              "grand_baie_1",//6
              "port_louis_3",//7
              "port_louis_4",//8
              "port_louis_9",//9
              "place_des_armes",//10
              "blue_penny_museum_2",//11
              "church_pl",//12
              "natural_history_musuem",//13
              "aapravasi_ghat_1",//14
              "post_office",//15
              "citadelle",//16
              "port_louis_photography_museum",//17
              "marie_reine_de_la_paix_3",//18
              "jardin_de_la_compagnie_1",//19
              "odysseo_1",//20
              "chateau_de_labourdonnais"//21
        };

        double[] itemLatitudes = {
                -19.9867007,//1
                -20.1042691,//2
                -20.0978896,//3
                -20.0838732,//4
                -20.0089233,//6
                -20.1608170, //7
                -20.1606798,//8
                -20.1632027,//9
                -20.1617266,//10
                -20.1609300,//11
                -20.1644918, //12
                -20.1632449, //13
                -20.1586888, //14
                -20.1600091,//15
                -20.1637132,//16
                -20.1641560,//17
                -20.1704784, //18
                -20.1637060, //19
                -20.1590542,//20
                -20.0736144//21
        };
        double[] itemLongitudes = {
                57.6222564,//1
                57.5799724, //2
                57.5743742,//3
                57.5138079,//4
                57.5812308,//6
                57.4980775, //7
                57.5029272,//8
                57.5034466, //9
                57.5020649,//10
                57.4974394,//11
                57.5065137,//12
                57.5024089,//13
                57.5029467,//14
                57.5017028,//15
                57.5103489, //16
                57.5035763,//17
                57.4962069,//18
                57.5020793,//19
                57.4948769,//20
                57.6176456//21
        };

        move_next("North", itemImages, itemName1, itemLatitudes, itemLongitudes);

    }

    public void south(View view) {
        String[] itemName1 = {
                "Gris Gris, La Roche qui Pleure",//1
                "Rochester Falls",//2
                "Bel Ombre Nature Reserve",//3
                "La Vallee des Couleurs Nature Park",//4
                "Blue Bay Beach",//5
                "Mahebourg Waterfront"//6
                , "Mahebourg Museum",//7
                "Ile aux Fouquets",//8
                "Ile aux Aigrettes Nature Reserve"//9
        };

        String[] itemImages = {
              "gris_gris_coastal_4",//1
              "rochester_falls_1",//2
              "bel_ombre_1",//3
              "valle_des_couleurs",//4
              "blue_bay",//5
              "mahebourg",//6
              "mahebourg_museum_2",//7
              "ile_aux_fouquets",//8
              "ile_aux_aigrettes_1"//9
        };

        double[] itemLatitudes = {
                -20.5245931,//1
                -20.502085954098924,//2
                -20.50063835115693, //3
                -20.45746296913571, //4
                -20.404968404325018,//6
                -20.41629431643517,//7
                -20.39569334633414, //8
                -20.3954058,//9
                -20.4214025//10
        };
        double[] itemLongitudes = {
                57.5190262, //1
                57.516743581499064,//2
                57.440675839170886,//3
                57.485128249964724,//4
                57.70975468333102,//6
                57.70333143609267,//7
                57.77745626059767,//8
                57.7563514,//9
                57.7319953//10
        };

        move_next("South", itemImages, itemName1, itemLatitudes, itemLongitudes);

    }

    public void central(View view) {
        String[] itemName1 = {
                "Bagatelle Mall",//1
                "Bois Cheri Tea Museum",//2
                "Eureka House",//3
                "Grand Bassin",//4
                "Gymkhana Golf Course",//5
                "Le Pouce Mountain"//6
                , "Pieter Both Mountain",//7
                "Tamarin Falls",//8
                "Trou aux Cerfs"//9
        };

         String[] itemImages = {
              "bagatelle_mall_1",//1
              "bois_cheri_1",//2
              "eureka_house",//3
              "grand_bassin_1",//4
              "gymkhana_2",//5
              "le_pouce_1",//6
              "pieter_both_1",//7
              "tamarin_falls_1",//8
              "trou_aux_cerfs_4"//9
        };

        double[] itemLatitudes = {
                -20.225102094813213, //1
                -20.42622255811708,//2
                -20.217910536364208, //3
                -20.416355080617052, //4
                -20.28877656491809,//5
                -20.207412596219665, //6
                -20.200602623937076, //7
                -20.343866449653184, //8
                -20.3171534//9
        };
        double[] itemLongitudes = {
                57.496785966145254, //1
                57.52565277219139,//2
                57.4978533683259,//3
                57.49306445953197,//4
                57.49956406615142,//5
                57.527254494980575,//6
                57.55494762566751,//7
                57.46647229498563,//8
                57.5014187//9
        };

        move_next("Central",itemImages, itemName1, itemLatitudes, itemLongitudes);

    }

    public void tourship_map(View view) {
        Intent intent = new Intent(ExploreActivity.this, TourshipActivity.class);
        startActivity(intent);
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
        startActivity(new Intent(ExploreActivity.this, ExploreActivity.class));
    }

    public void my_trips(View view) {
        startActivity(new Intent(ExploreActivity.this, MyTripsActivity.class));
    }

    public void iternties(View view) {
        startActivity(new Intent(ExploreActivity.this, ItinerariesActivity.class));
    }

    public void organier(View view) {
        startActivity(new Intent(ExploreActivity.this, OrganizerActivity.class));
    }

    public void contact_us(View view) {
        startActivity(new Intent(ExploreActivity.this, ContactUsActivity.class));
    }


    public void tips(View view) {
        startActivity(new Intent(ExploreActivity.this, TravelTipsActivity.class));

    }

    public void about(View view) {
//        startActivity(new Intent(ExploreActivity.this, AppInfoActivity.class));
        startActivity(new Intent(ExploreActivity.this, AboutActivity.class));

    }

    /*public void login(View view) {
        startActivity(new Intent(ExploreActivity.this, LoginActivity.class));

    }*/

    public void home(View view) {
        startActivity(new Intent(ExploreActivity.this, DashboardActivity.class));

    }

    public void move_next(String type, String[] itemImage, String[] itemName1, double[] itemLatitudes, double[] itemLongitudes) {
        Stash.clear("images");
        imagesList.clear();
        for (String name : itemImage) {
            imagesList.add(fullPath + "/" + name + ".jpg");
        }
        Stash.put("images", imagesList);
        Intent intent = new Intent(ExploreActivity.this, ExploreDetailsActivity.class);
        intent.putExtra("itemHeader", type);
        intent.putExtra("itemName1", itemName1);
        intent.putExtra("itemLatitudes", itemLatitudes);
        intent.putExtra("itemLongitudes", itemLongitudes);
        startActivity(intent);
    }
}
