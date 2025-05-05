package com.moutamid.sqlapp.activities.Beaches;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.moutamid.sqlapp.R;
import com.moutamid.sqlapp.model.BeachItem;

import java.io.File;

public class BeachesActivity extends AppCompatActivity {
    BeachItem[] beachItems;
    String fullPath;

    // Latitude and Longitude Arrays for Beach Locations
    double[] itemLatitudesEast = {-20.1928215, -20.2668829, -20.1272669, -20.1043210}; // Beaches East latitudes
    double[] itemLongitudesEast = {57.7750124, 57.8057047, 57.7569250, 57.7257182}; // Beaches East longitudes

    double[] itemLatitudesNorth = {-19.9853937, -20.0078407, -20.0164764, -19.9938413, -20.0348409}; // Beaches North latitudes
    double[] itemLongitudesNorth = {57.6205649, 57.6697449, 57.5568158, 57.5910988, 57.5449564}; // Beaches North longitudes

    double[] itemLatitudesSouth = {-20.5044881, -20.4441615, -20.5243435, -20.4549788, -20.502551, -20.5092946}; // Beaches South latitudes
    double[] itemLongitudesSouth = {57.3993671, 57.7166979, 57.5323138, 57.6992368, 57.4534908, 57.4658383}; // Beaches South longitudes

    double[] itemLatitudesWest = {-20.2993385, -20.3547236, -20.4529630, -20.3262782}; // Beaches West latitudes
    double[] itemLongitudesWest = {57.3636901, 57.3614249, 57.3125745, 57.3778870}; // Beaches West longitudes

    @Override
protected void onCreate(Bundle savedInstanceState) {
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beaches);
        ImageView coin_de_mire_10, le_morne_brabant, gris_gris_4, ile_aux_cerfs_island_2, belle_mare_1;
        File cacheDir = new File(getFilesDir(), "cached_images");
         fullPath = cacheDir.getAbsolutePath();
        coin_de_mire_10 = findViewById(R.id.coin_de_mire_10);
        belle_mare_1 = findViewById(R.id.belle_mare_1);
        le_morne_brabant = findViewById(R.id.le_morne_brabant);
        gris_gris_4 = findViewById(R.id.gris_gris_4);
        ile_aux_cerfs_island_2 = findViewById(R.id.ile_aux_cerfs_island_2);
        Glide.with(this).load(new File(fullPath + "/coin_de_mire_10.jpg")).into(coin_de_mire_10);
        Glide.with(this).load(new File(fullPath + "/le_morne_brabant.jpg")).into(le_morne_brabant);
        Glide.with(this).load(new File(fullPath + "/gris_gris_4.jpg")).into(gris_gris_4);
        Glide.with(this).load(new File(fullPath + "/ile_aux_cerfs_island_2.jpg")).into(ile_aux_cerfs_island_2);
        Glide.with(this).load(new File(fullPath + "/belle_mare_1.jpg")).into(belle_mare_1);

    }

    public void BackPress(View view) {
        onBackPressed();
    }

    public void beaches_in_east(View view) {
        String title_header = "Beaches in the East";
        String title = "East";
        String[] itemTexts = {
                "District of Flacq ",
                "District of Flacq",
                "District of Flacq",
                "District of Riviere du Rempart"
        };
        String[] itemName = {
                "Belle Mare Beach",
                "Ile aux Cerfs Beach",
                "Poste Lafayette Beach",
                "Roche Noire Beach"
        };
        String[] itemName1 = {
                "Belle Mare",
                "Ile aux Cerfs",
                "Poste Lafayette",
                "Roche Noire"
        };
        String[] itemImages = {
                fullPath + "/" + "belle_mare_1" + ".jpg",
                fullPath + "/" + "ile_aux_cerfs_mauritius_1" + ".jpg",
                fullPath + "/" + "poste_lafayette_1" + ".jpg",
                fullPath + "/" + "roche_noire_2" + ".jpg"
        };
        Intent intent = new Intent(BeachesActivity.this, BeachesTypeActivity.class);
        intent.putExtra("itemHeader", title_header);
        intent.putExtra("itemTitle", title);
        intent.putExtra("itemTexts", itemTexts);
        intent.putExtra("itemName", itemName);
        intent.putExtra("itemName1", itemName1);
        intent.putExtra("itemImages", itemImages);
        intent.putExtra("itemLatitudes", itemLatitudesEast); // Add latitudes
        intent.putExtra("itemLongitudes", itemLongitudesEast); // Add longitudes
        startActivity(intent);
    }
//
    public void beaches_in_west(View view) {
        String title_header = "Beaches in the West";
        String title = "West";
        String[] itemTexts = {"Flic en Flac", "La Preneuse", "Le Morne Brabant", "Tamarin"};
        String[] itemName = {"Flic en Flac Beach", "La Preneuse Beach", "Le Morne Beach", "Tamarin Bay Beach"};
        String[] itemName1 = {"District of Black River", "District of Black River", "District of Black River", "District of Black River"};
        String[] itemImages = {fullPath + "/" + "flic_en_flac_3" + ".jpg", fullPath + "/" + "la_preneuse_4" + ".jpg", fullPath + "/" + "le_morne_beach_2" + ".jpg", fullPath + "/" + "tamarin_3" + ".jpg"};
        Intent intent = new Intent(BeachesActivity.this, BeachesTypeActivity.class);
        intent.putExtra("itemHeader", title_header);
        intent.putExtra("itemTitle", title);
        intent.putExtra("itemTexts", itemTexts);
        intent.putExtra("itemName", itemName);
        intent.putExtra("itemName1", itemName1);
        intent.putExtra("itemImages", itemImages);
        intent.putExtra("itemLatitudes", itemLatitudesWest); // Add latitudes
        intent.putExtra("itemLongitudes", itemLongitudesWest); // Add longitudes
        startActivity(intent);

    }
//
    public void beaches_in_south(View view) {
        String title_header = "Beaches in the South";
        String title = "South";
        String[] itemName = {
                "Bel Ombre Beach",
                "Blue Bay Beach",
                "Gris Gris Beach",
                "La Cambuse Beach",
                "Riviere des Galets Beach",
                "St Felix Beach"};
        String[] itemName1 = {
                "St Martin",
                "Blue Bay",
                "Souillac",
                "La Cambuse Beach",
                "Riviere des Galets Beach",
                "St Felix Beach"
        };
        String[] itemTexts = {
                "District of Savanne",
                "District of Grand Port",
                "District of Savanne",
                "District of Plaine Magnien",
                "District of Savanne",
                "District of Savanne"
        };
        String[] itemImages = {
                fullPath + "/" + "bel_ombre_17" + ".jpg",
                fullPath + "/" + "blue_bay" + ".jpg",
                fullPath + "/" + "gris_gris_1" + ".jpg",
                fullPath + "/" + "la_cambuse_2" + ".jpg",
                fullPath + "/" + "riviere_des_galets_1" + ".jpg",
                fullPath + "/" + "st_felix_1" + ".jpg"
        };
        Intent intent = new Intent(BeachesActivity.this, BeachesTypeActivity.class);
        intent.putExtra("itemHeader", title_header);
        intent.putExtra("itemTitle", title);
        intent.putExtra("itemTexts", itemTexts);
        intent.putExtra("itemName", itemName);
        intent.putExtra("itemName1", itemName1);
        intent.putExtra("itemImages", itemImages);
        intent.putExtra("itemLatitudes", itemLatitudesSouth); // Add latitudes
        intent.putExtra("itemLongitudes", itemLongitudesSouth); // Add longitudes
        startActivity(intent);

    }

    public void beaches_in_north(View view) {
        String title_header = "Beaches in the North";
        String title = "North";

        String[] itemTexts = {
                "Cap Malheureux",
                "Grand Gaube",
                "Mont Choisy",
                "Grand Baie",
                "Trou aux Biches"
        };
        String[] itemName = {
                "Cap Malheureux Beach",
                "Grand Gaube Beach",
                "Mont Choisy Beach",
                "Pereybere Beach",
                "Trou aux Biches Beach"
        };
        String[] itemName1 = {
                "District of Riviere du Rempart",
                "District of Riviere du Rempart",
                "District of Pamplemousses",
                "District of Riviere du Rempart",
                "District of Pamplemousses"
        };
        String[] itemImages = {
                fullPath + "/" + "cap_malheureux_6" + ".jpg",
                fullPath + "/" + "grand_gaube_1" + ".jpg",
                fullPath + "/" + "mont_choisy_2" + ".jpg",
                fullPath + "/" + "pereybere_beach_1" + ".jpg",
                fullPath + "/" + "trou_aux_biches_1" + ".jpg"
        };
        Intent intent = new Intent(BeachesActivity.this, BeachesTypeActivity.class);
        intent.putExtra("itemHeader", title_header);
        intent.putExtra("itemTitle", title);
        intent.putExtra("itemTexts", itemTexts);
        intent.putExtra("itemName", itemName);
        intent.putExtra("itemName1", itemName1);
        intent.putExtra("itemImages", itemImages);
        intent.putExtra("itemLatitudes", itemLatitudesNorth); // Add latitudes
        intent.putExtra("itemLongitudes", itemLongitudesNorth); // Add longitudes
        startActivity(intent);


    }
}