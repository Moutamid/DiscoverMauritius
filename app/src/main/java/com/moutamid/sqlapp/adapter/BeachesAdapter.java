package com.moutamid.sqlapp.adapter;

import static com.moutamid.sqlapp.model.DatabaseHelper.TABLE_NAME;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

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
import com.moutamid.sqlapp.activities.Beaches.BeachDetails;
import com.moutamid.sqlapp.activities.Beaches.BeachesTypeActivity;
import com.moutamid.sqlapp.helper.Constants;
import com.moutamid.sqlapp.model.BeacModel;
import com.moutamid.sqlapp.model.DatabaseHelper;
import com.moutamid.sqlapp.offlinemap.MapActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class BeachesAdapter extends BaseAdapter {
    ImageView  weather_icon;
    TextView count_txt, temp_txt, condition;
    me.zhanghai.android.materialratingbar.MaterialRatingBar ratingBar;

    private Context context;
    private String[] itemTexts;
    private String[] itemName;
    private String[] itemDetails;
    private String[] itemImages;
    private double[] latitudes; // Added latitude array
    private double[] longitudes; // Added longitude array
    String fullPath;

    public BeachesAdapter(Context context, String[] itemName, String[] itemDetails, String[] itemTexts, String[] itemImages, double[] latitudes, double[] longitudes) {
        this.context = context;
        this.itemTexts = itemTexts;
        this.itemImages = itemImages;
        this.itemDetails = itemDetails;
        this.itemName = itemName;
        this.latitudes = latitudes; // Initialize latitude array
        this.longitudes = longitudes; // Initialize longitude array
    }

    @Override
    public int getCount() {
        return itemTexts.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.beaches_east_layout, parent, false);
        File cacheDir = new File(context.getFilesDir(), "cached_images");
        fullPath = cacheDir.getAbsolutePath();
        ImageView imageView = itemView.findViewById(R.id.imageView);
        RelativeLayout main_lyt = itemView.findViewById(R.id.lyt);
        TextView textView = itemView.findViewById(R.id.textView);
        TextView textView1 = itemView.findViewById(R.id.textView1);
        TextView textView2 = itemView.findViewById(R.id.textView2);
        ImageView add = itemView.findViewById(R.id.add);
        ImageView remove = itemView.findViewById(R.id.remove);
        ImageView map = itemView.findViewById(R.id.map);
        weather_icon = itemView.findViewById(R.id.weather_icon);
        temp_txt = itemView.findViewById(R.id.temp);
        condition = itemView.findViewById(R.id.condition);
        ratingBar = itemView.findViewById(R.id.ratingbar);
        count_txt = itemView.findViewById(R.id.count);

        String image = itemImages[position];
        Glide.with(context)
                .load(new File(image))
                .into(imageView);
        textView.setText(itemName[position]);
        textView1.setText(itemTexts[position]);
        textView2.setText(itemDetails[position]);
        fetchWeatherData(latitudes[position], longitudes[position]);
        ratingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signInAnonymously()
                        .addOnSuccessListener(authResult -> {
                            String userId = authResult.getUser().getUid();
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ratings").child(textView.getText().toString()).child(userId);
                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (!snapshot.exists()) {
                                        showRatingDialog(textView.getText().toString());
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
        loadRating(itemName[position], ratingBar);
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Stash.getBoolean(Constants.IS_PREMIUM, false)) {

                    Stash.put("map_lat", latitudes[position]);
                    Stash.put("map_lng", longitudes[position]);
                    Stash.put("map_name", itemName[position]);
                    Stash.put("map_img", itemImages[position]);
                    Intent intent = new Intent(context, MapActivity.class);
                    intent.putExtra("map_lat", latitudes[position]);
                    intent.putExtra("map_lng", longitudes[position]);
                    context.startActivity(intent);

                } else {
                    BeachesTypeActivity.premium_layout.setVisibility(View.VISIBLE);
                }
            }
        });
        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper;
        databaseHelper = new DatabaseHelper(context);
        List<BeacModel> beacModels = databaseHelper.getAllBeacModels();
        boolean isDataAvailable = false;
        for (BeacModel model : beacModels) {
            if (model.title.equals(textView.getText().toString())) {
                isDataAvailable = true;
                break;
            }
        }
        if (isDataAvailable) {
            add.setVisibility(View.GONE);
            remove.setVisibility(View.VISIBLE);
        } else {
            add.setVisibility(View.VISIBLE);
            remove.setVisibility(View.GONE);
        }
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Stash.getBoolean(Constants.IS_PREMIUM, false)) {
                    String deleteName = textView.getText().toString();
                    SQLiteDatabase db = databaseHelper.getWritableDatabase();
                    db.delete(TABLE_NAME, DatabaseHelper.COLUMN_TITLE + "=?", new String[]{deleteName});
                    db.close();
                    remove.setVisibility(View.GONE);
                    add.setVisibility(View.VISIBLE);
                    notifyDataSetChanged();
                } else {
                    BeachesTypeActivity.premium_layout.setVisibility(View.VISIBLE);
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                triggerModel(position, textView, add, remove, true);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                triggerModel(position, textView, add, remove, false);
            }
        });

        return itemView;
    }

    private void triggerModel(int position, TextView textView, ImageView add, ImageView remove, boolean isOpened) {
        Intent intent = null;
        if (Stash.getString("type").equals("West")) {
            if (position == 0) {
                BeacModel model = new BeacModel();
                model.title = textView.getText().toString();
                model.main_image = fullPath + "/" + "flic_en_flac_3" + ".jpg";

                model.text1 = "After Grand Baie, Flic en Flac, on the island's west coast, has grown to be Mauritius's second-most popular tourist destination. Over the past two centuries, this area has transformed from a small fishing village into a vibrant town, attracting both international tourists and local Mauritians";
                model.title1 = "";
                model.image1 = fullPath + "/" + "flic_en_flac_1" + ".jpg";
                model.text2 = "Flic en Flac offers an ideal holiday experience with its stunning 8 km beach, lined with white sand and crystal-clear blue lagoons. Numerous restaurants, hotels, and shops cater to visitors, making it a perfect spot for watersports, afternoon strolls, sunbathing, or simply relaxing under the shade of <b>Casuarina Trees.</b> The lagoon, protected by a coral reef, provides safe swimming conditions and various watersport activities.";
                model.title2 = "";
                model.text3 = "During the day, Flic en Flac is bustling with activity, offering local street food like <b>DhalPuri,</b> a delicious flatbread filled with curry, and <b>fried noodles with Mauritian meatballs.</b> On weekends, the beach comes alive with activity as locals congregate for picnics, singing, and dancing to the beat of Sega music, creating a joyous atmosphere. atmosphere. Thanks to its many restaurants and nightclubs, Flic en Flac is also a favorite nighttime destination for both locals and visitors.";

                model.image2 = "";
                model.text4 = "Despite its beauty, it's important to note that the beach has corals and sea urchins, so caution is advised when walking barefoot.";
                model.title3 = "Location and Accessibility";
                model.text5 = "Flic en Flac is situated in the <b>Black River district,</b> approximately 15 km south of Port Louis. It lies between the villages of <b>Albion</b> to the north and <b>Tamarin</b> and <b>Black River</b> to the south. The town is easily accessible by a well-established bus route connecting it to major cities like Port Louis, <b>Quatre Bornes,</b> and <b>Curepipe,</b> making transportation convenient for visitors.";
                model.title4 = "";
                model.text6 = "";
                model.title5 = "";
                model.text7 = "";
                model.title6 = "";
                model.text8 = "";
                model.title7 = "";

                model.image3 = "";

                model.text9 = "";
                model.title8 = "";
                model.text10 = "";

                model.image4 = "";

                model.text11 = "";

                model.image5 = "";

                model.text12 = "";
                model.lat = latitudes[position];
                model.lng = longitudes[position];

                if (isOpened) {
                    Stash.put("model", model);
                    intent = new Intent(context, BeachDetails.class);
                    context.startActivity(intent);

                } else {
                    com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                    databaseHelper.insertBeacModel(model);
                    add.setVisibility(View.GONE);
                    remove.setVisibility(View.VISIBLE);
                }


            } else if (position == 1) {
                BeacModel model = new BeacModel();
                model.title = textView.getText().toString();

                model.main_image = fullPath + "/" + "la_preneuse_4" + ".jpg"; // Set the value for main_image

                model.text1 = "<b>La Preneuse Beach,</b> located on the west coast of Mauritius, offers a serene setting with pristine, white sand that slopes gently into the shallow turquoise waters of the Indian Ocean. Though the sea is mostly calm, occasional strong currents can occur. The beach is spacious, providing ample room for vacationers, and offers breathtaking panoramic views, including the striking sight of <b>Mount Le Morne</b> set against the lagoon.";
                model.title1 = "";

                model.image1 = ""; // Set the value for image1

                model.text2 = "This beach destination is ideal for those seeking relaxation, as it is surrounded by various hotels catering to different comfort levels. Nearby, you'll find cafes, shops, and souvenir stores offering local goods. One of the notable landmarks here is the <b>Martello Tower,</b> a structure built by the British in the 1830s as a defense against potential French attacks. It now serves as a museum that provides insight into the region's history. Additionally, the <b>Black River</b> flows along the beach, and yachts and boats dot the horizon, offering sea excursions for tourists.";
                model.title2 = "";
                model.text3 = "";

                model.image2 = ""; // Set the value for image2

                model.text4 = "";
                model.title3 = "";
                model.text5 = "";
                model.title4 = "";
                model.text6 = "";
                model.title5 = "";
                model.text7 = "";
                model.title6 = "";
                model.text8 = "";
                model.title7 = "";

                model.image3 = "";

                model.text9 = "";
                model.title8 = "";
                model.text10 = "";

                model.image4 = "";

                model.text11 = "";

                model.image5 = "";

                model.text12 = "";
                model.lat = latitudes[position];
                model.lng = longitudes[position];
                if (isOpened) {
                    Stash.put("model", model);
                    intent = new Intent(context, BeachDetails.class);
                    context.startActivity(intent);


                } else {
                    com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                    databaseHelper.insertBeacModel(model);
                    add.setVisibility(View.GONE);
                    remove.setVisibility(View.VISIBLE);
                }


            } else if (position == 2) {
                BeacModel model = new BeacModel();
                model.title = textView.getText().toString();

                model.main_image = fullPath + "/" + "le_morne_beach_2" + ".jpg"; // Set the value for main_image

                model.text1 = "Le Morne Beach, located on a peninsula along the western coast of Mauritius, sits at the foot of the UNESCO-listed Le Morne Brabant mountain. With easy access from the coastal road, this public beach stretches for miles with soft white sand and shady casuarina trees. The crystal-clear water provides excellent visibility, making it a perfect spot for scuba diving.";
                model.title1 = "";

                model.image1 = fullPath + "/" + "kite_surfing" + ".jpg"; // Set the value for image1

                model.text2 = "Le Morne Beach, renowned for being one of the best places in the world to kite surf, has an amazing view of the famous mountain in the distance. The beach is also a favorite among windsurfers and kite surfers due to the ideal conditions that last throughout much of the year. Le Morne Beach hosts several competitions, including the Kiteival, which draws participants from around the globe.";
                model.title2 = "";
                model.text3 = "Though great for swimming, Le Morne Beach can get quite crowded on weekends and holidays. Nudism is not allowed, but topless sunbathing is accepted. For a quieter visit, it's best to go early in the morning from 8:00 to 11:00 or in the afternoon between 1:00 and 5:00.";

                model.image2 = fullPath + "/" + "paddle_boarding" + ".jpg"; // Set the value for image2

                model.text4 = "There’s no shortage of activities for beachgoers here, including pedalo rides, windsurfing, kite surfing, scuba diving, stand-up paddleboarding, and kayaking. At Le Morne Beach, there's something for everyone to enjoy.";
                model.title3 = "";
                model.text5 = "";
                model.title4 = "";
                model.text6 = "";
                model.title5 = "";
                model.text7 = "";
                model.title6 = "";
                model.text8 = "";
                model.title7 = "";

                model.image3 = "";

                model.text9 = "";
                model.title8 = "";
                model.text10 = "";

                model.image4 = "";

                model.text11 = "";

                model.image5 = "";

                model.text12 = "";
                model.lat = latitudes[position];
                model.lng = longitudes[position];
                if (isOpened) {
                    Stash.put("model", model);
                    intent = new Intent(context, BeachDetails.class);
                    context.startActivity(intent);


                } else {
                    com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                    databaseHelper.insertBeacModel(model);
                    add.setVisibility(View.GONE);
                    remove.setVisibility(View.VISIBLE);
                }


            } else if (position == 3) {
//                        Toast.makeText(context, "cicked", Toast.LENGTH_SHORT).show();
                BeacModel model = new BeacModel();
                model.title = textView.getText().toString();
                model.main_image = fullPath + "/" + "tamarin_3" + ".jpg"; // Set the value for main_image

                model.text1 = "Tamarin Bay Beach is a gorgeous beach location on Mauritius' west coast, situated just past the <b>Black River</b> village. Known for its natural beauty and ideal surfing conditions, it attracts both locals and tourists.";
                model.title1 = "Surfing History and Recognition";
                model.image1 = fullPath + "/" + "tamarin_2" + ".jpg"; // Set the value for image1
                model.text2 = "This beach, formerly known as <b>Santosha Bay,</b> was kept a secret by surfers who considered it a hidden treasure. The name <b>Santosha</b> can still be faintly seen painted on a few buildings. Tamarin Bay gained international fame with the release of the 1974 surf documentary <b>\"Forgotten Island of Santosha\"</b> by Larry and Roger Yates, which brought global attention to its remarkable surf breaks. The bay features two renowned surfing spots: <b>‘Dal’</b> on the southern side and <b>‘Black Stone’</b> to the north.\n";
                model.title2 = "Dolphins and Wildlife";
                model.text3 = "Tamarin Bay is a haven for dolphins, especially <b>Spinner</b> and <b>Bottlenose dolphins,</b> which are frequently spotted in the early morning hours before heading out to the open sea. Many boat companies offer morning trips for tourists to observe and swim with these playful creatures, making it a top destination for wildlife enthusiasts as well.\n";
                model.image2 = ""; // Set the value for image2
                model.text4 = "";
                model.title3 = "Surfing Hub\n";
                model.text5 = "Since the 1970s, Tamarin Bay has been a major surfing hub, introduced primarily by Australians living on the island’s west coast. The area’s strong waves and ideal conditions continue to draw surf enthusiasts from around the world, and surfing here is often considered a special privilege.";
                model.title4 = "Authentic and Laid-Back Atmosphere\n";
                model.text6 = "Despite its fame as a surfing hotspot, Tamarin Bay retains its authentic charm. Local families frequent the beach for leisurely strolls or relaxed afternoons, creating a welcoming and laid-back environment. Its vibrant yet genuine atmosphere makes it appealing to both tourists and locals.\n";
                model.title5 = "Swimming Caution\n";
                model.text7 = "While Tamarin Bay is excellent for surfing, swimming is not recommended for children or inexperienced swimmers. The strong currents and large waves can be unpredictable, making it less safe for casual swimming.";
                model.title6 = "Lively Beach Culture\n";
                model.text8 = "Tamarin Bay can become quite busy, especially on weekends and holidays, offering a lively and energetic beach scene. Whether you're enjoying the surf, relaxing by the water, or taking in the local culture, the beach offers something for everyone.";
                model.title7 = "Best Times to Visit";

                model.image3 = "";

                model.text9 = "The best time to visit Tamarin Bay is either early in the morning, between 8:00 and 11:00, or in the afternoon, from 13:00 to 16:00. These times offer the most favorable conditions for beach activities, with fewer crowds and enjoyable weather.";
                model.title8 = "Activities at Tamarin Bay";
                model.text10 = "";

                model.image4 = fullPath + "/" + "tamarin_1" + ".jpg";

                model.text11 = "In addition to surfing, Tamarin Bay offers a variety of water activities, including:" + "<ul>" + "<li>Stand-up paddleboarding\n</li>" + "<li>Bodyboarding\n</li>" + "<li>Catamaran tours\n</li>" + "<li>Swimming with dolphins\n</li>" + "<li>Speed boat trips\n</li>" + "<li>Kayaking\n</li>" + "</ul>" + "Tamarin Bay Beach is a dynamic coastal destination that combines the thrill of surfing with the beauty of nature. Its authentic atmosphere, paired with a range of water activities and wildlife experiences, makes it an ideal spot for anyone looking to enjoy Mauritius’s vibrant beach culture.\n";

                model.image5 = "";

                model.text12 = "";
                model.lat = latitudes[position];
                model.lng = longitudes[position];
                if (isOpened) {
                    Stash.put("model", model);
                    intent = new Intent(context, BeachDetails.class);
                    context.startActivity(intent);


                } else {
                    com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                    databaseHelper.insertBeacModel(model);
                    add.setVisibility(View.GONE);
                    remove.setVisibility(View.VISIBLE);
                }


            }

        }
        else if (Stash.getString("type").equals("East")) {
            if (position == 0) {
                BeacModel model = new BeacModel();
                model.title = textView.getText().toString();
                model.main_image = fullPath + "/" + "belle_mare_1" + ".jpg"; // Set the value for main_image

                model.text1 = "Belle Mare Beach, located near Mahebourg on the southeastern coast of Mauritius, stretches between the villages of Belle Mare and Pointe de Flacq. Sheltered by a coral reef, this crescent-shaped beach is known for its calm, transparent waters and fine, dazzling white sand. The lagoon here is a stunning shade of turquoise, offering a breathtaking view of unspoiled nature. Development is minimal, with only a few hotels and luxury villas scattered along the coast.";
                model.title1 = "";
                model.image1 = fullPath + "/" + "belle_mare_3" + ".jpg"; // Set the value for image1
                model.text2 = "Loved by both tourists and locals, Belle Mare Beach has retained its authentic charm, unlike other rapidly developing coastal areas such as Grand Baie, Flic en Flac, and Black River. The beach is perfect for swimming and diving, especially on weekdays when the atmosphere is quieter and more relaxed. Nature lovers will appreciate the long stretches of sandy shoreline, ideal for sunbathing and peaceful walks.";
                model.title2 = "";
                model.text3 = "Belle Mare Beach is best visited in the morning, from 7:00 to 11:00, or in the afternoon, from 1:00 to 5:00, as these times tend to be less crowded. There are plenty of activities to enjoy, including pedalo rides, windsurfing, catamaran trips, horseback riding, parasailing, scuba diving, speed boat rides, and kayaking.";
                model.image2 = ""; // Set the value for image2
                model.text4 = "";
                model.title3 = "";
                model.text5 = "";
                model.title4 = "";
                model.text6 = "";
                model.title5 = "";
                model.text7 = "";
                model.title6 = "";
                model.text8 = "";
                model.title7 = "";
                model.image3 = "";
                model.text9 = "";
                model.title8 = "";
                model.text10 = "";
                model.image4 = "";
                model.text11 = "";
                model.image5 = "";
                model.text12 = "";

                model.lat = latitudes[position];
                model.lng = longitudes[position];
                if (isOpened) {
                    Stash.put("model", model);
                    intent = new Intent(context, BeachDetails.class);
                    context.startActivity(intent);


                } else {
                    com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                    databaseHelper.insertBeacModel(model);
                    add.setVisibility(View.GONE);
                    remove.setVisibility(View.VISIBLE);
                }


            } else if (position == 1) {
                BeacModel model = new BeacModel();
                model.title = textView.getText().toString();

                model.main_image = fullPath + "/" + "ile_aux_cerfs_mauritius_1" + ".jpg"; // Set the value for main_image

                model.text1 = "Located on the eastern coast of Mauritius, just south of Trou d’Eau Douce, Ile aux Cerfs is part of a stunning duo of islands, offering a true paradise experience across 87 hectares. Known for its breathtaking lagoon and pristine beaches, it’s a favorite among water sports enthusiasts and is considered one of the most beautiful beaches in Mauritius.\n";
                model.title1 = "What to Explore and Experience on Ile aux Cerfs:\n";
                model.image1 = fullPath + "/" + "ile_aux_cerfs_7" + ".jpg"; // Set the value for image1
                model.text2 = "Ile aux Cerfs is famous for its turquoise lagoon, which is perfect for a wide variety of water sports. One of the highlights is parasailing, offering unforgettable views of the lagoon and the surrounding landscape. Other popular activities include speedboat rides, water skiing, and paddleboarding. For snorkeling, a boat trip is required to reach areas with coral, as the water near the beach is shallow and mostly sandy.";
                model.title2 = "";
                model.text3 = "";
                model.image2 = fullPath + "/" + "ile_aux_cerf_5" + ".jpg"; // Set the value for image2
                model.text4 = "While the island is a hub for water activities, it’s also ideal for sunbathers. With limited shade available on the beach, visitors are advised to bring plenty of sunscreen—preferably eco-friendly options. Most tourists stay near the pier where the shuttle drops them off, but if you want to escape the crowds, a short walk along the coast will lead you to quieter beaches, especially on weekdays.\n";
                model.title3 = "How to Reach Ile aux Cerfs:\n";
                model.text5 = "";
                model.title4 = "";
                model.text6 = "";
                model.title5 = "";
                model.text7 = "";
                model.title6 = "";
                model.text8 = "";
                model.title7 = "";
                model.image3 = fullPath + "/" + "ile_aux_cerfs_7" + ".jpg";
                model.text9 = "The island is open to the public from 9:00 AM to 6:00 PM. You can catch a shuttle from the village of Trou d’Eau Douce, which departs every half hour from the Pointe Maurice pier to the Masala pier on Ile aux Cerfs, starting at 9:30 AM. Another option is to take a scenic catamaran ride, with many providers offering trips to the island that may also include a detour to the nearby waterfalls in the Grande Rivière Sud Est nature reserve.\n";
                model.title8 = "Best Time to Visit Ile aux Cerfs:\n";
                model.text10 = "";
                model.image4 = fullPath + "/" + "ile_aux_cerfs_6" + ".jpg";
                model.text11 = "Weekdays are the best time to visit if you want to avoid the larger crowds that flock to the island on weekends. October and November are considered the ideal months for a trip to Ile aux Cerfs, but the entire period from June to November provides pleasant weather for a visit.\n" + "<br><br>" + "<b><span style=\"color:black; font-weight:bold;\">Accommodation:</span></b>" + "<br><br>" + "For those seeking luxury, the 5-star Shangri-La’s Le Touessrok Resort & Spa offers an opulent stay on Ile aux Cerfs, providing the perfect setting for a grand celebration or special event.\n" + "<br><br>\n" + "\n" + "\n" + "<b><span style=\"color:black; font-weight:bold;\">Dining Options:</span></b>";
                model.image5 = fullPath + "/" + "ile_aux_cerfs_3" + ".jpg";
                model.text12 = "The island has several dining options, including a beachside restaurant where you can enjoy a meal with a view. There are also two bars offering refreshing drinks and cocktails at reasonable prices. If you prefer, you can bring your own food and drinks for a picnic on the beach.\n";

                model.lat = latitudes[position];
                model.lng = longitudes[position];
                if (isOpened) {
                    Stash.put("model", model);
                    intent = new Intent(context, BeachDetails.class);
                    context.startActivity(intent);


                } else {
                    com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                    databaseHelper.insertBeacModel(model);
                    add.setVisibility(View.GONE);
                    remove.setVisibility(View.VISIBLE);
                }


            } else if (position == 2) {
                BeacModel model = new BeacModel();
                model.title = textView.getText().toString();

                model.main_image = fullPath + "/" + "poste_lafayette_1" + ".jpg"; // Set the value for main_image

                model.text1 = "Poste Lafayette Beach, an often-overlooked gem on the eastern coast of Mauritius, offers an escape into unspoiled nature. Though its waters can be turbulent, this beach provides a rare opportunity for peace and tranquility, making it a favorite among nature enthusiasts. The area is rich with marine biodiversity, supported by massive coral reefs and lush mangroves, offering an authentic and rustic charm.\n";
                model.title1 = "";
                model.image1 = fullPath + "/" + "poste_lafayette_2" + ".jpg"; // Set the value for image1
                model.text2 = "The southern part of the beach is perfect for a quiet picnic, providing a peaceful retreat for visitors. It’s also a great starting point for a scenic trek along the rocky trails to the north of Poste Lafayette, where you can enjoy picturesque green landscapes.\n";
                model.title2 = "";
                model.text3 = "";
                model.image2 = fullPath + "/" + "poste_lafayette_3" + ".jpg"; // Set the value for image2
                model.text4 = "Picnicking under the trees, with stormy waves in the background due to the nearby reefs, is a unique experience here. Due to the beach's year-round exposure to trade winds, Poste Lafayette is a great place for fans of kite and windsurfing.\n";
                model.title3 = "";
                model.text5 = "";
                model.title4 = "";
                model.text6 = "";
                model.title5 = "";
                model.text7 = "";
                model.title6 = "";
                model.text8 = "";
                model.title7 = "";
                model.image3 = fullPath + "/" + "poste_lafayette_4" + ".jpg";
                model.text9 = "Although swimming can be challenging due to the rough sea, the shallow waters are perfect for sunbathing, and kids can enjoy building sandcastles along the shore. The eastern coast also boasts a variety of hotels and restaurants, including Jalsa Beach Hotel & Spa, Radisson Blu Poste Lafayette Resort & Spa, and the luxurious Hôtel Constance Le Prince Maurice.\n" + "<br><br>" + "For dining, Poste Lafayette offers both gourmet and traditional options. La Maison D’Été is a must-visit, serving Italian cuisine with fresh seafood, while Seabell is a local favorite for Mauritian dishes. The nearby mini market, owned by Seabell's owners, is convenient for grabbing snacks or essentials.\n";
                model.title8 = "";
                model.text10 = "";
                model.image4 = "";
                model.text11 = "";
                model.image5 = "";
                model.text12 = "";

                model.lat = latitudes[position];
                model.lng = longitudes[position];
                if (isOpened) {
                    Stash.put("model", model);
                    intent = new Intent(context, BeachDetails.class);
                    context.startActivity(intent);


                } else {
                    com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                    databaseHelper.insertBeacModel(model);
                    add.setVisibility(View.GONE);
                    remove.setVisibility(View.VISIBLE);
                }


            } else if (position == 3) {

                BeacModel model = new BeacModel();
                model.title = textView.getText().toString();

                model.main_image = fullPath + "/" + "roche_noire_2" + ".jpg"; // Set the value for main_image

                model.text1 = "Located just a few kilometers from Poste Lafayette on the northeast coast, Roche Noire Beach is ranked among the top 10 beaches in Mauritius. The beach is known for its striking landscape, featuring black lava rocks and a darker sea where gentle waves meet the coral reef. Roche Noire Beach is a genuinely unique location, featuring immaculate sand and breathtaking views of the sunrise and sunset. The refreshing winds make it especially pleasant during the hot summer months, and it’s one of the most photographed beaches according to tourist reviews.\n";
                model.title1 = "";
                model.image1 = fullPath + "/" + "roche_noire_3" + ".jpg"; // Set the value for image1
                model.text2 = "Roche Noire Beach is a haven for water lovers and nature enthusiasts alike. Activities such as swimming, snorkeling, and kayaking allow visitors to explore the colorful coral reefs and marine life. The peaceful atmosphere makes it ideal for leisurely walks along the shore or relaxing while sunbathing.\n" + "<br><br>" + "Parking is easily accessible near the beach, with both free and paid options available. One of the beach’s standout features is its volcanic rock formations, which add to its natural beauty and give Roche Noire a distinct, picturesque charm.\n";
                model.title2 = "";
                model.text3 = "";
                model.image2 = ""; // Set the value for image2
                model.text4 = "";
                model.title3 = "";
                model.text5 = "";
                model.title4 = "";
                model.text6 = "";
                model.title5 = "";
                model.text7 = "";
                model.title6 = "";
                model.text8 = "";
                model.title7 = "";
                model.image3 = "";
                model.text9 = "";
                model.title8 = "";
                model.text10 = "";
                model.image4 = "";
                model.text11 = "";
                model.image5 = "";
                model.text12 = "";

                model.lat = latitudes[position];
                model.lng = longitudes[position];
                if (isOpened) {
                    Stash.put("model", model);
                    intent = new Intent(context, BeachDetails.class);
                    context.startActivity(intent);


                } else {
                    com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                    databaseHelper.insertBeacModel(model);
                    add.setVisibility(View.GONE);
                    remove.setVisibility(View.VISIBLE);
                }


            }
        }
        else if (Stash.getString("type").equals("South")) {
            if (position == 0) {
                BeacModel model = new BeacModel();
                model.title = textView.getText().toString();

                model.main_image = fullPath + "/" + "bel_ombre_17" + ".jpg"; // Set the value for main_image

                model.text1 = "Located in the southwestern part of Mauritius, Bel Ombre Beach welcomes visitors with its soft golden sands and tranquil atmosphere. This serene beach stretches along the turquoise waters, surrounded by lush casuarina trees that offer shade and add to the natural beauty of the area. The gentle southern breeze carries the refreshing scent of the ocean, creating a peaceful and rejuvenating environment.\n";
                model.title1 = "";
                model.image1 = fullPath + "/" + "bel_ombre_18" + ".jpg"; // Set the value for image1
                model.text2 = "The beach’s pristine white sand is lined with coconut trees, offering a perfect mix of nature and luxury, with a few opulent hotels in the background. Bel Ombre Beach is truly a haven of calm and beauty, ideal for those seeking a quiet escape. The best times to visit are early in the morning between 7:00 and 11:00 or later in the afternoon from 1:00 to 5:00, when the beach is at its most peaceful.\n";
                model.title2 = "";
                model.text3 = "";
                model.image2 = fullPath + "/" + "bel_ombre_16" + ".jpg"; // Set the value for image2
                model.text4 = "Visitors can enjoy a range of activities at Bel Ombre Beach, including pedalo rides, scuba diving, speed boat tours, and kayaking, making it a great spot for both relaxation and adventure.\n";
                model.title3 = "";
                model.text5 = "";
                model.title4 = "";
                model.text6 = "";
                model.title5 = "";
                model.text7 = "";
                model.title6 = "";
                model.text8 = "";
                model.title7 = "";
                model.image3 = "";
                model.text9 = "";
                model.title8 = "";
                model.text10 = "";
                model.image4 = "";
                model.text11 = "";
                model.image5 = "";
                model.text12 = "";

                model.lat = latitudes[position];
                model.lng = longitudes[position];
                if (isOpened) {
                    Stash.put("model", model);
                    intent = new Intent(context, BeachDetails.class);
                    context.startActivity(intent);


                } else {
                    com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                    databaseHelper.insertBeacModel(model);
                    add.setVisibility(View.GONE);
                    remove.setVisibility(View.VISIBLE);
                }


            } else if (position == 1) {
                BeacModel model = new BeacModel();
                model.title = textView.getText().toString();

                model.main_image = fullPath + "/" + "blue_bay" + ".jpg"; // Set the value for main_image

                model.text1 = "Located on the southeastern coast of Mauritius, <b>Blue Bay</b> was designated a national park in 1997 and later recognized as a wetland of international importance under the Ramsar Convention in 2008. The marine park is famous for its coral garden, which is home to a wide variety of corals and marine life. Its close proximity to the coastline, combined with calm and shallow waters, makes Blue Bay an ideal spot for snorkeling and exploring its rich biodiversity.";

                model.title1 = "";

                model.image1 = fullPath + "/" + "blue_bay_4" + ".jpg"; // Set the value for image1

                model.text2 = "When you arrive at <b>Blue Bay Beach,</b> you’ll likely encounter local vendors selling handmade jewelry and people offering glass-bottom boat trips or snorkeling tours. More than fifteen operators are licensed to run businesses within the Blue Bay Marine Park.\n" + "<br><br>" + "One of the park’s main attractions is an ancient brain coral that’s over 1,000 years old and has a diameter of 5 meters, making it a must-see for visitors. The coral garden near Mahebourg, a small village in the southeast, spans a large area and features incredible biodiversity. The brain coral is a popular tourist attraction, and glass-bottom boat rides offer a tranquil way to view the undersea environment.\n";
                model.title2 = "";
                model.text3 = "";

                model.image2 = fullPath + "/" + "blue_bay_2" + ".jpg"; // Set the value for image2

                model.text4 = "Many of the operators here are descendants of fishermen who transitioned into tourism. Glass-bottom boat trips are accessible to everyone and offer a relaxing way to appreciate the beauty of the marine park. For those who want to get closer, snorkeling is a great option, providing an up-close view of the vibrant coral and fish in the clear, warm waters.\n" + "<br><br>" + "Covering 353 hectares, Blue Bay Marine Park is a popular destination for both locals and tourists. It plays an important role in supporting local households through tourism while also maintaining a balance between economic activity and environmental conservation. The park has installed permanent mooring buoys to protect coral from damage by boat anchors and to regulate zones for fishing, boat traffic, swimming, and waterskiing. These efforts help preserve the park's biodiversity, promote research, and educate the public about marine conservation.\n";
                model.title3 = "";
                model.text5 = "";
                model.title4 = "";
                model.text6 = "";
                model.title5 = "";
                model.text7 = "";
                model.title6 = "";
                model.text8 = "";
                model.title7 = "";

                model.image3 = fullPath + "/" + "blue_bay_6" + ".jpg";

                model.text9 = "Coastguards patrol the area regularly to make sure the park's rules are followed and to safeguard marine life.";
                model.title8 = "Protected Marine Area Features:\n";
                model.text10 = "<ul>" + "\t<li><b>Incredible Marine Biodiversity:</b> Blue Bay Marine Park is home to various ecosystems, including coral reefs, seagrass meadows, mangroves, and macroalgae beds. The park supports over 38 species of coral, 72 species of fish, 31 species of algae, 2 mangrove species, and 4 types of seagrass plants.</li>" + "</ul>";
                model.image4 = fullPath + "/" + "blue_bay_3" + ".jpg";

                model.text11 = "<ul>" + "\t<li><b>Unique Status in Mauritius:</b> Blue Bay Marine Park is the only one in Mauritius to be classified under the Wildlife and National Parks Act of 1993. It became a protected zone in 2000 under the Fisheries and Marine Act and was later recognized as a Ramsar site in 2008. Mooring buoys are used to prevent boat anchors from damaging the coral.</li>" + "</ul>";
                model.image5 = fullPath + "/" + "blue_bay_8" + ".jpg";

                model.text12 = "<ul>" + "\t<li><b>Activities for Everyone:</b> Glass-bottom boat trips and snorkeling are popular activities that allow non-swimmers to enjoy the marine environment without diving in. Snorkelers can also relax under the casuarina trees on Coco Island, which is visible from the beach. Operators offer convenient drop-off and pick-up services for snorkelers.</li>" + "</ul>";

                model.lat = latitudes[position];
                model.lng = longitudes[position];
                if (isOpened) {
                    Stash.put("model", model);
                    intent = new Intent(context, BeachDetails.class);
                    context.startActivity(intent);


                } else {
                    com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                    databaseHelper.insertBeacModel(model);
                    add.setVisibility(View.GONE);
                    remove.setVisibility(View.VISIBLE);
                }


            } else if (position == 2) {
                BeacModel model = new BeacModel();
                model.title = textView.getText().toString();

                model.main_image = fullPath + "/" + "gris_gris_1" + ".jpg"; // Set the value for main_image

                model.text1 = "The primary beach area features an expansive green field with a commanding view of dramatic cliffs and enormous waves. There are benches and a pavilion available for relaxation while taking in the scenery.\n" + "To the left, a concrete staircase descends to the beach, but swimming is strongly discouraged due to the high danger level. The powerful waves can swiftly overwhelm swimmers. Instead, enjoy a leisurely stroll along the beach, heading toward a small cave at the far end.\n";

                model.title1 = "Secret Caves at Gris Gris";

                model.image1 = fullPath + "/" + "grisgris_2" + ".jpg"; // Set the value for image1

                model.text2 = "In addition to the cave on the far left side of the beach, two other hidden caves can be discovered at Gris Gris. These are more challenging to reach, involving a descent down a cliff and walking through the water.\n" + "Caution is advised against going all the way down, as water levels can fluctuate unpredictably, and the current is often too strong.\n" + "<br><br>" + "For those eager to explore the secret caves at Gris Gris, head towards the cliff's edge directly across from the parking lot. Upon reaching the spot, descend only about halfway to catch a glimpse of the caves on your right.\n" + "<br><br>" + "It's important to bear in mind that entering the caves could pose risks if the water level rises!\n" + "Gris Gris beach is intricately connected to the village of Souillac, which relies heavily on tourism for its revenue. " + "<br><br>" + "Established 200 years ago as a port for ships sailing from Europe to India, Souillac has a rich history worth exploring. Plan your day strategically to make the most of your visit to the southern part of Mauritius, and consider including a visit to Rochester Falls, just outside the village, renowned for its distinctive rectangular-sided rocks.<br>";
                model.title2 = "";
                model.text3 = "";

                model.image2 = ""; // Set the value for image2

                model.text4 = "";
                model.title3 = "";
                model.text5 = "";
                model.title4 = "";
                model.text6 = "";
                model.title5 = "";
                model.text7 = "";
                model.title6 = "";
                model.text8 = "";
                model.title7 = "";

                model.image3 = "";

                model.text9 = "";
                model.title8 = "";
                model.text10 = "";

                model.image4 = "";

                model.text11 = "";

                model.image5 = "";

                model.text12 = "";
                model.lat = latitudes[position];
                model.lng = longitudes[position];
                if (isOpened) {
                    Stash.put("model", model);
                    intent = new Intent(context, BeachDetails.class);
                    context.startActivity(intent);


                } else {
                    com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                    databaseHelper.insertBeacModel(model);
                    add.setVisibility(View.GONE);
                    remove.setVisibility(View.VISIBLE);
                }


            } else if (position == 3) {
                BeacModel model = new BeacModel();
                model.title = textView.getText().toString();

                model.main_image = fullPath + "/" + "la_cambuse_2" + ".jpg"; // Set the value for main_image

                model.text1 = "La Cambuse Beach, located on the southeastern coast of Mauritius, is just a 10-minute drive from the international airport. This beautiful, unspoiled beach offers a peaceful retreat with its wide stretch of sand, natural shade from the surrounding trees, and picnic benches, making it a perfect spot for a scenic lunch. The panoramic views of the lagoon, along with the wild, crashing waves along the rugged coastline, add to the beach’s raw, natural charm.\n";
                model.title1 = "";
                model.image1 = fullPath + "/" + "la_cambuse_3" + ".jpg"; // Set the value for image1
                model.text2 = "While the beach is inviting, swimmers should be cautious, as the currents here can be quite strong. For those looking to explore, consider bringing hiking shoes to navigate the rocky terrain. A nearby trail leads to Le Bouchon and continues to Pont Naturel, a natural rock bridge, and Le Souffleur, known for its dramatic and rugged landscape. This hike offers an adventure-filled day, allowing you to experience the natural beauty and untamed scenery of the area.\n";
                model.title2 = "";
                model.text3 = "";
                model.image2 = ""; // Set the value for image2
                model.text4 = "";
                model.title3 = "";
                model.text5 = "";
                model.title4 = "";
                model.text6 = "";
                model.title5 = "";
                model.text7 = "";
                model.title6 = "";
                model.text8 = "";
                model.title7 = "";
                model.image3 = "";
                model.text9 = "";
                model.title8 = "";
                model.text10 = "";
                model.image4 = "";
                model.text11 = "";
                model.image5 = "";
                model.text12 = "";

                model.lat = latitudes[position];
                model.lng = longitudes[position];
                if (isOpened) {
                    Stash.put("model", model);
                    intent = new Intent(context, BeachDetails.class);
                    context.startActivity(intent);


                } else {
                    com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                    databaseHelper.insertBeacModel(model);
                    add.setVisibility(View.GONE);
                    remove.setVisibility(View.VISIBLE);
                }


            } else if (position == 4) {
                BeacModel model = new BeacModel();
                model.title = textView.getText().toString();

                model.main_image = fullPath + "/" + "riviere_des_galets_1" + ".jpg"; // Set the value for main_image

                model.text1 = "Situated between Souillac and Bel Ombre, Rivière des Galets Beach stands out for its unique shoreline covered with countless small pebbles, rather than the usual sand. This beach, easily reached from Ilot Sancho via the southern coastal road, has a unique charm that makes it stand out from other beaches on the island.\n";
                model.title1 = "";
                model.image1 = fullPath + "/" + "riviere_des_galets_4" + ".jpg"; // Set the value for image1
                model.text2 = "A popular stop for tourists exploring southern Mauritius, Rivière des Galets Beach is a great place to relax and take in the sight of powerful waves crashing against the shore. The beach’s pebble-strewn coastline, polished smooth by the action of the waves, creates a striking contrast to the usual sandy beaches found elsewhere on the island.\n" + "<br><br>" + "The beach’s unusual landscape also makes it a favorite spot for photographers and newlyweds seeking a picturesque backdrop for photo shoots. Rivière des Galets offers a refreshing change from the typical beaches in Mauritius, giving visitors a chance to experience the island’s more rugged and unique southern coastline.\n";
                model.title2 = "";
                model.text3 = "";
                model.image2 = ""; // Set the value for image2
                model.text4 = "";
                model.title3 = "";
                model.text5 = "";
                model.title4 = "";
                model.text6 = "";
                model.title5 = "";
                model.text7 = "";
                model.title6 = "";
                model.text8 = "";
                model.title7 = "";
                model.image3 = "";
                model.text9 = "";
                model.title8 = "";
                model.text10 = "";
                model.image4 = "";
                model.text11 = "";
                model.image5 = "";
                model.text12 = "";

                model.lat = latitudes[position];
                model.lng = longitudes[position];
                if (isOpened) {
                    Stash.put("model", model);
                    intent = new Intent(context, BeachDetails.class);
                    context.startActivity(intent);


                } else {
                    com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                    databaseHelper.insertBeacModel(model);
                    add.setVisibility(View.GONE);
                    remove.setVisibility(View.VISIBLE);
                }


            } else if (position == 5) {
                BeacModel model = new BeacModel();
                model.title = textView.getText().toString();

                model.main_image = fullPath + "/" + "st_felix_1" + ".jpg"; // Set the value for main_image

                model.text1 = "Saint Félix Beach, nestled between Souillac and Bel Ombre at the southernmost tip of Mauritius, is a breathtaking stretch of coastline. Known for its turquoise waters and pristine white sand, it’s often considered the most beautiful beach in the southern region of the island.\n" + "<br><br>" + "Despite its stunning beauty, Saint Félix Beach remains somewhat off the radar, with limited online information or imagery available. The beach is divided into two sections, and the area facing Morne is especially enchanting, offering a secluded escape from the more crowded tourist beaches.\n" + "<br><br>" + "Saint Félix is the ideal location for those looking for a quiet place to enjoy the sun or for a fun-filled day at the beach with friends. The beach is surrounded by lush greenery, which contrasts beautifully with the white sand and the turquoise sea, creating a mesmerizing array of blue hues.\n" + "<br><br>" + "However, swimming is not recommended at Saint Félix due to strong offshore currents and the presence of coral. The serene atmosphere, especially during weekdays, makes it an ideal spot for unwinding and enjoying nature’s beauty. Even though the sand is flawless, guests should exercise caution when entering the water because the region is home to extremely poisonous stonefish that mix in with the rocks and coral on the seafloor.\n" + "<br><br>" + "Popular activities at Saint Félix include deep-sea fishing and speedboat trips, offering a more adventurous way to experience the natural beauty of this hidden gem.\n";
                model.title1 = "";
                model.image1 = ""; // Set the value for image1
                model.text2 = "";
                model.title2 = "";
                model.text3 = "";
                model.image2 = ""; // Set the value for image2
                model.text4 = "";
                model.title3 = "";
                model.text5 = "";
                model.title4 = "";
                model.text6 = "";
                model.title5 = "";
                model.text7 = "";
                model.title6 = "";
                model.text8 = "";
                model.title7 = "";
                model.image3 = "";
                model.text9 = "";
                model.title8 = "";
                model.text10 = "";
                model.image4 = "";
                model.text11 = "";
                model.image5 = "";
                model.text12 = "";

                model.lat = latitudes[position];
                model.lng = longitudes[position];
                if (isOpened) {
                    Stash.put("model", model);
                    intent = new Intent(context, BeachDetails.class);
                    context.startActivity(intent);


                } else {
                    com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                    databaseHelper.insertBeacModel(model);
                    add.setVisibility(View.GONE);
                    remove.setVisibility(View.VISIBLE);
                }


            }
        }
        else if (Stash.getString("type").equals("North")) {

            if (position == 0) {
                BeacModel model = new BeacModel();
                model.title = textView.getText().toString();

                model.main_image = fullPath + "/" + "cap_malheureux_6" + ".jpg"; // Set the value for main_image

                model.text1 = "Cap Malheureux Beach, located at the northernmost tip of Mauritius, is a charming spot just behind the famous Notre Dame Auxiliatrice Chapel, known for its striking red roof and picturesque facade. Although swimming is excellent in the calm waters, Cap Malheureux is best known for being the launchpad for boat and catamaran trips to Flat Island and its lighthouse, as well as the Round Island wildlife sanctuary.\n";
                model.title1 = "";
                model.image1 = fullPath + "/" + "cap_malheureux_7" + ".jpg"; // Set the value for image1
                model.text2 = "The beach, dotted with basaltic rocks, provides a peaceful place to relax and enjoy the view of the lagoon. It also offers stunning panoramic views of nearby northern islets, including Coin de Mire (Gunner’s Quoin), Ile Plate (Flat Island), Ile Ronde (Round Island), Ile aux Serpents (Serpent Islet), and Ilôt Gabriel.\n" + "<br><br>" + "Cap Malheureux is a hotspot for water sports, especially kiteboarding and windsurfing, which are particularly popular in the summer months when the winds pick up. Surfing is another favorite activity here, suitable for surfers of all skill levels. Kids will also love exploring the rock pools scattered along the beach.\n";
                model.title2 = "";
                model.text3 = "";
                model.image2 = fullPath + "/" + "cap_malheureux_9" + ".jpg"; // Set the value for image2
                model.text4 = "Though the beach is typically quiet and serene, it tends to get busy with locals on weekends, especially after services at the nearby chapel. To avoid crowds, it’s best to visit on weekdays and arrive early to secure a parking spot.\n";
                model.title3 = "";
                model.text5 = "";
                model.title4 = "";
                model.text6 = "";
                model.title5 = "";
                model.text7 = "";
                model.title6 = "";
                model.text8 = "";
                model.title7 = "";
                model.image3 = "";
                model.text9 = "";
                model.title8 = "";
                model.text10 = "";
                model.image4 = "";
                model.text11 = "";
                model.image5 = "";
                model.text12 = "";

                model.lat = latitudes[position];
                model.lng = longitudes[position];
                if (isOpened) {
                    Stash.put("model", model);
                    intent = new Intent(context, BeachDetails.class);
                    context.startActivity(intent);


                } else {
                    com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                    databaseHelper.insertBeacModel(model);
                    add.setVisibility(View.GONE);
                    remove.setVisibility(View.VISIBLE);
                }


            } else if (position == 1) {
                BeacModel model = new BeacModel();
                model.title = textView.getText().toString();

                model.main_image = fullPath + "/" + "grand_gaube_1" + ".jpg"; // Set the value for main_image

                model.text1 = "Grand Gaube Beach, located in the northern part of Mauritius, stands apart from its more popular neighbors like Pereybere, Trou aux Biches, and Mont Choisy. Its rugged, rocky landscape and coral-filled seabed make it unsuitable for swimming and water sports, giving it a much more secluded and untouched feel.\n" + "<br><br>" + "Unlike other beaches in the area, Grand Gaube has seen little development, and despite efforts by the Ministry of Tourism to clean up and beautify the beach, it hasn’t gained the same level of attention as other northern beaches. From a tourist perspective, Grand Gaube offers limited attractions compared to its more famous counterparts, making it a quiet retreat rather than a tourist hotspot.\n" + "<br><br>" + "However, Grand Gaube is still an excellent spot for those seeking fresh air and solitude. For visitors looking for more action, nearby beaches like Butte à l'Herbe and Anse La Raie are great alternatives for swimming or kitesurfing.\n" + "<br><br>" + "For guests staying at Lux Grand Gaube, there are plenty of water activities to enjoy, including:\n" + "<ul>" + "<li>Pedalo\n</li>" + "<li>Windsurfing\n</li>" + "<li>Catamaran Rides\n</li>" + "<li>Parasailing\n</li>" + "<li>Scuba Diving\n</li>" + "<li>Deep-Sea Fishing\n</li>" + "<li>Speed Boat Trips\n</li>" + "<li>Kayaking\n</li>" + "</ul>";
                model.title1 = "";
                model.image1 = ""; // Set the value for image1
                model.text2 = "";
                model.title2 = "";
                model.text3 = "";
                model.image2 = ""; // Set the value for image2
                model.text4 = "";
                model.title3 = "";
                model.text5 = "";
                model.title4 = "";
                model.text6 = "";
                model.title5 = "";
                model.text7 = "";
                model.title6 = "";
                model.text8 = "";
                model.title7 = "";
                model.image3 = "";
                model.text9 = "";
                model.title8 = "";
                model.text10 = "";
                model.image4 = "";
                model.text11 = "";
                model.image5 = "";
                model.text12 = "";

                model.lat = latitudes[position];
                model.lng = longitudes[position];
                if (isOpened) {
                    Stash.put("model", model);
                    intent = new Intent(context, BeachDetails.class);
                    context.startActivity(intent);


                } else {
                    com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                    databaseHelper.insertBeacModel(model);
                    add.setVisibility(View.GONE);
                    remove.setVisibility(View.VISIBLE);
                }


            } else if (position == 2) {
                BeacModel model = new BeacModel();
                model.title = textView.getText().toString();
                model.main_image = fullPath + "/" + "mont_choisy_2" + ".jpg"; // Set the value for main_image

                model.text1 = "Often regarded as one of the most stunning beaches in the nation, Mont Choisy Beach is situated in the Pamplemousses district on Mauritius' northwest coast. Known for its peaceful atmosphere and long stretch of powdery white sand, it offers a tranquil escape from the more commercialized beaches. Shaped like a crescent, this serene beach is the longest in Mauritius, making it perfect for those seeking relaxation away from the crowds.\n";
                model.title1 = "Best Time to Visit Mont Choisy Beach:\n";
                model.image1 = ""; // Set the value for image1
                model.text2 = "Mont Choisy Beach is a great destination year-round, but the best time to visit is between September and November when the weather is pleasant, and rainfall is minimal. The warmest months, from January to March, are ideal for enjoying the sea, while the busiest period falls between November and January. If you're looking for fewer crowds and lower prices, June to August is a quieter time to visit.\n";
                model.title2 = "Things to Do at Mont Choisy Beach:\n";
                model.text3 = "<ul>" + "<li><b>Water Sports & Diving:</b> Mont Choisy is a hotspot for water activities, offering everything from kitesurfing and parasailing to water skiing and glass-bottom boat tours. High safety standards are offered by the dive centers in the area for reef, drift, wreck, and deep-sea diving.\n</li>" + "<li><b>Photography:</b> The breathtaking blue waters, crescent-shaped coastline, and stunning sunrise and sunset views make Mont Choisy a perfect place for capturing beautiful photos.\n</li>" + "<li><b>Unwind:</b> The peaceful environment at Mont Choisy allows visitors to relax, swim, and enjoy snacks from local vendors under the shade of casuarina trees.\n</li>" + "<li><b>Beach Soccer or Golf:</b> The soft sand is ideal for beach soccer, and there’s a designated playground for the sport. Golf enthusiasts can also enjoy the nearby Mont Choisy Le Golf course, an 18-hole course surrounded by landscaped greens.\n</li>" + "<li><b>Horseback Riding:</b> Horse Riding Delights offers the chance to ride along scenic trails, where you can spot deer, giant tortoises, and other animals.\n</li>" + "</ul>";
                model.image2 = ""; // Set the value for image2
                model.text4 = "";
                model.title3 = "Best Places to Stay Near Mont Choisy Beach:\n";
                model.text5 = "<ul>" + "<li><b>Mont Choisy Beach Villas:</b> These beachfront villas are conveniently close to the beach and offer spacious accommodations with stunning views of the Indian Ocean.\n</li>" + "<li><b>Casuarina Resort & Spa:</b> This 3-star resort features elegant rooms and bungalows with beautiful views of Trou aux Biches Bay, along with complimentary water activities.\n</li>" + "<li><b>Coral Azur Hotel Mont Choisy:</b> A great option for luxury at a reasonable price, this hotel offers an ocean-view pool and well-equipped rooms.\n</li>" + "<li><b>Mont Choisy Beach R.:</b> A resort with a beachfront pool, apartment-style accommodations, and kitchenettes for a comfortable stay.\n</li>" + "<li><b>Choisy Les Bains:</b> This modern complex offers apartments with a children’s play area and a lovely garden, just a short walk from the beach.</li>" + "</ul>" + "<b><span style=\"color:black; font-weight:bold;\">How to Get to Mont Choisy Beach:</span></b>";
                model.title4 = "";
                model.text6 = "Mont Choisy Beach is located close to Port Louis and can be reached by car in about 20 minutes. You can also take the Express Bus from Immigration Square, which takes around 35–40 minutes.\n";
                model.title5 = "";
                model.text7 = "";
                model.title6 = "";
                model.text8 = "";
                model.title7 = "";
                model.image3 = "";
                model.text9 = "";
                model.title8 = "";
                model.text10 = "";
                model.image4 = "";
                model.text11 = "";
                model.image5 = "";
                model.text12 = "";

                model.lat = latitudes[position];
                model.lng = longitudes[position];
                if (isOpened) {
                    Stash.put("model", model);
                    intent = new Intent(context, BeachDetails.class);
                    context.startActivity(intent);


                } else {
                    com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                    databaseHelper.insertBeacModel(model);
                    add.setVisibility(View.GONE);
                    remove.setVisibility(View.VISIBLE);
                }


            } else if (position == 3) {
                BeacModel model = new BeacModel();
                model.title = textView.getText().toString();
                model.main_image = fullPath + "/" + "pereybere_beach_1" + ".jpg"; // Set the value for main_image

                model.text1 = "Though only 150 meters long, Pereybere Beach is known as the best family-friendly beach in Mauritius. Its calm, shallow waters make it one of the safest beaches for swimming, making it a top choice for families with young children.\n" + "<br><br>" + "The beach is lined with clean white sand, and the turquoise water is surrounded by palm trees, creating a picture-perfect setting. Beach shacks along the shore offer a variety of food options, and you can rent deck chairs to relax while keeping an eye on your kids in the water.\n";
                model.title1 = "";
                model.image1 = fullPath + "/" + "pereybere_beach_5" + ".jpg"; // Set the value for image1
                model.text2 = "Pereybere Beach is perfect for simple, fun activities like collecting seashells, building sandcastles, or exploring the shallow water with your children. The crystal-clear waters also provide a great opportunity to see colorful fish up close. If you’re feeling adventurous, you can rent pedal boats to explore the surrounding areas of the Indian Ocean.\n";
                model.title2 = "";
                model.text3 = "";
                model.image2 = fullPath + "/" + "pereybere_beach_3" + ".jpg"; // Set the value for image2
                model.text4 = "For those looking for more excitement, the beach offers a range of water sports, including windsurfing, parasailing, and kayaking. Deep-sea fishing and catamaran rides are also popular here, with dolphin cruises offering a particularly fun experience for children.\n";
                model.title3 = "";
                model.text5 = "";
                model.title4 = "";
                model.text6 = "";
                model.title5 = "";
                model.text7 = "";
                model.title6 = "";
                model.text8 = "";
                model.title7 = "";
                model.image3 = fullPath + "/" + "pereybere_beach_6" + ".jpg";
                model.text9 = "As the day comes to a close, Pereybere Beach is known for its breathtaking sunsets, making it a must-see. Arriving early is recommended, especially during the week, as the beach can get crowded, especially on weekends.\n";
                model.title8 = "";
                model.text10 = "";
                model.image4 = fullPath + "/" + "pereybere_beach_4" + ".jpg";
                model.text11 = "A unique experience in Mauritius is the underwater sea walk, where you can explore marine life while wearing a solar-powered helmet. Suitable for kids as young as seven, this adventure is supervised by trained professionals to ensure safety.\n" + "<br><br>" + "The beach area is also lined with a variety of restaurants, offering everything from street food to fine dining. For those who prefer quieter beaches, Pereybere is just five minutes from Grand Baie, which has additional public beaches like La Cuvette and Grand Baie Public Beach.\n" + "<br><br>" + "<b><span style=\"color:black; font-weight:bold;\">Nearby Attractions:</span></b>" + "<ul>" + "<li><b>Grand Baie:</b> A lively town offering casinos, shopping, and vibrant nightlife.\n</li>" + "<li><b>Accommodations:</b> Popular options near Pereybere Beach include Hibiscus Beach Resort & Spa, Pereybere Hotel and Spa, and Le Beachclub, each providing different experiences and amenities.\n</li>" + "</ul>" + "<b><span style=\"color:black; font-weight:bold;\">Getting There:</span></b>" + "<br><br>" + "From Port Louis, you can take the Express Cap Malheureux Bus from Immigration Square, and it takes about an hour to reach Pereybere Beach. Renting a car is another option, giving you flexibility and convenience for the journey.\n";
                model.image5 = "";
                model.text12 = "";

                model.lat = latitudes[position];
                model.lng = longitudes[position];
                if (isOpened) {
                    Stash.put("model", model);
                    intent = new Intent(context, BeachDetails.class);
                    context.startActivity(intent);


                } else {
                    com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                    databaseHelper.insertBeacModel(model);
                    add.setVisibility(View.GONE);
                    remove.setVisibility(View.VISIBLE);
                }


            } else if (position == 4) {
                BeacModel model = new BeacModel();
                model.title = textView.getText().toString();

                model.main_image = fullPath + "/" + "trou_aux_biches_1" + ".jpg"; // Set the value for main_image

                model.text1 = "Trou-aux-Biches is a small town on the northern coast of Mauritius. It is situated in the Pamplemousses district. It is famous for its public beach, which is recognized as one of the most beautiful beaches on the island. It was awarded the title of World's Leading Beach Destination at the 2011 World Travel Award. The area has many tourist resorts and boutique hotels, including the Trou aux Biches Resort & Spa. You can easily reach Trou-aux-Biches from Port Louis by taking the M2 highway. It is about a mile west from where the M2 ends, and public buses run between the town and the airport.";
                model.title1 = "";

                model.image1 = ""; // Set the value for image1

                model.text2 = "Originally a fishing village in the 19th century, Trou-aux-Biches has transformed into a lovely town. Its name comes from the time of French colonial rule and was found on the Lislet-Geoffroy map from 1807. Today, the town features a two-kilometer-long white sand beach lined with casuarina trees. You can also find snorkeling reefs, boutique shops, a supermarket, and various facilities along the B38 road, making it a great place for families to visit.";
                model.title2 = "";
                model.text3 = "";

                model.image2 = fullPath + "/" + "trou_aux_biches_2" + ".jpg"; // Set the value for image2

                model.text4 = "Tourism in Mauritius started in 1952, but the first major hotel was built in Trou-aux-Biches in 1971 after the island gained independence in 1968. The town keeps a cozy village feel, setting it apart from the busier Grand Baie nearby. Inland from Trou-aux-Biches is the Maheswarnath Mandir, the largest Hindu temple on the island, which was established in 1888.";
                model.title3 = "";
                model.text5 = "Visitors to Trou-aux-Biches can enjoy the beach, especially for watching the beautiful sunsets.";
                model.title4 = "";
                model.text6 = "";
                model.title5 = "";
                model.text7 = "";
                model.title6 = "";
                model.text8 = "";
                model.title7 = "";

                model.image3 = "";

                model.text9 = "";
                model.title8 = "";
                model.text10 = "";

                model.image4 = "";

                model.text11 = "";

                model.image5 = "";

                model.text12 = "";
                model.lat = latitudes[position];
                model.lng = longitudes[position];
                if (isOpened) {
                    Stash.put("model", model);
                    intent = new Intent(context, BeachDetails.class);
                    context.startActivity(intent);


                } else {
                    com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                    databaseHelper.insertBeacModel(model);
                    add.setVisibility(View.GONE);
                    remove.setVisibility(View.VISIBLE);
                }


            }
        }
    }
    private void showRatingDialog(String placeName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_rating, null);
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
                    Toast.makeText(context, "You have reached to maximum limit", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(context, "Thanks for rating!", Toast.LENGTH_SHORT).show();
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
                    count_txt.setText(average + "  (" + count + ")");
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
        RequestQueue queue = Volley.newRequestQueue(context);
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
                        Glide.with(context).load(iconUrl).into(weather_icon);
                        temp_txt.setText(temp + " °C");
                        condition.setText(mainWeather);
                        Log.d("Weather", "City: " + city + ", Temp: " + temp + "°C" + jsonObject.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            error.printStackTrace();
        });

        queue.add(stringRequest);
    }

}
