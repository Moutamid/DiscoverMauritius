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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
import com.moutamid.sqlapp.activities.Explore.ExploreDetailsActivity;
import com.moutamid.sqlapp.helper.Constants;
import com.moutamid.sqlapp.model.BeacModel;
import com.moutamid.sqlapp.model.DatabaseHelper;
import com.moutamid.sqlapp.offlinemap.MapActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class ExploreAdapter extends RecyclerView.Adapter<ExploreAdapter.ViewHolder> {
    String name;
    String fullPath;
    ArrayList<String> images;
    private Context context;
    private String[] itemName;
    private double[] itemLatitudes;
    private double[] itemLongitudes;

    public ExploreAdapter(Context context, ArrayList<String> images, String[] itemName, double[] itemLatitudes, double[] itemLongitudes, String name) {
        this.context = context;
        this.images = images;
        this.itemName = itemName;
        this.itemLatitudes = itemLatitudes;
        this.itemLongitudes = itemLongitudes;
        this.name = name;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.explore_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int p) {
//        holder.imageView.setImageResource(itemImages[holder.getAdapterPosition()]);
        File cacheDir = new File(context.getFilesDir(), "cached_images");
        fullPath = cacheDir.getAbsolutePath();
        String image = images.get(holder.getAdapterPosition());
        Glide.with(context)
                .load(new File(image))
                .into(holder.imageView);
        fetchWeatherData(itemLatitudes[holder.getAdapterPosition()], itemLongitudes[holder.getAdapterPosition()], holder);
        holder.ratingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signInAnonymously()
                        .addOnSuccessListener(authResult -> {
                            String userId = authResult.getUser().getUid();
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ratings").child(holder.textView.getText().toString()).child(userId);
                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (!snapshot.exists()) {
                                        showRatingDialog(holder.textView.getText().toString());
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
        loadRating(itemName[holder.getAdapterPosition()], holder.ratingBar, holder);

//        Bitmap bitmap = BitmapFactory.decodeFile(images.get(0).getAbsolutePath());
//        holder.imageView.setImageBitmap(bitmap);
        holder.textView.setText(itemName[holder.getAdapterPosition()]);
        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper;
        databaseHelper = new DatabaseHelper(context);
        List<BeacModel> beacModels = databaseHelper.getAllBeacModels();
        boolean isDataAvailable = false;
        for (BeacModel model : beacModels) {
            Log.d("model", model.text1 + " test");
            if (model.title.equals(holder.textView.getText().toString())) {
                isDataAvailable = true;
                break;
            }
        }
        if (isDataAvailable) {
            holder.add.setVisibility(View.GONE);
            holder.remove.setVisibility(View.VISIBLE);
        } else {
            holder.add.setVisibility(View.VISIBLE);
            holder.remove.setVisibility(View.GONE);
        }
        holder.map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Stash.getBoolean(Constants.IS_PREMIUM, false)) {

                    Stash.put("map_lat", itemLatitudes[holder.getAdapterPosition()]);
                    Stash.put("map_lng", itemLongitudes[holder.getAdapterPosition()]);
                    Stash.put("map_name", itemName[holder.getAdapterPosition()]);
//                    Stash.put("map_img", itemImages[holder.getAdapterPosition()]);
                    Intent intent = new Intent(context, MapActivity.class);
                    intent.putExtra("map_lat", itemLatitudes[holder.getAdapterPosition()]);
                    intent.putExtra("map_lng", itemLongitudes[holder.getAdapterPosition()]);
                    context.startActivity(intent);

                } else {
                    ExploreDetailsActivity.premium_layout.setVisibility(View.VISIBLE);
                }

            }
        });
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String deleteName = holder.textView.getText().toString();
                SQLiteDatabase db = databaseHelper.getWritableDatabase();
                db.delete(TABLE_NAME, DatabaseHelper.COLUMN_TITLE + "=?", new String[]{deleteName});
                db.close();
                holder.remove.setVisibility(View.GONE);
                holder.add.setVisibility(View.VISIBLE);
                notifyDataSetChanged();
            }
        });

        holder.add.setOnClickListener(v -> {
            holder.add.setVisibility(View.GONE);
            holder.remove.setVisibility(View.VISIBLE);
            triggerModel(holder, false);
        });

        holder.main_lyt.setOnClickListener(v -> {
            triggerModel(holder, true);
        });

    }

    @Override
    public int getItemCount() {
        return itemName.length;
    }

    public void triggerModel(ViewHolder holder, boolean isOpened) {
        Intent intent = null;
        switch (name) {
            case "Central":
                if (holder.getAdapterPosition() == 0) {
                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "bagatelle_mall_1" + ".jpg";
                    model.text1 = "Bagatelle Mall is the go-to spot for shopping, dining, and entertainment in Mauritius. Opened in September 2011 and located in the Moka area, it offers over 150 stores with a mix of international favorites like Mango, Zara, and Lacoste, along with well-loved local brands. The mall has something for everyone, including a hypermarket, a food court, a cinema, and a children’s play area.\n" + "<br><br>" + "When it comes to food, Bagatelle Mall doesn’t disappoint. You’ll find everything from sit-down restaurants and cozy cafes to quick fast-food options. The food court is a big draw, featuring vendors that offer all kinds of cuisines, whether you’re grabbing a quick snack or settling in for a relaxed meal. Loved by both locals and tourists, Bagatelle Mall has become a must-visit spot in Mauritius.\n" + "<br><br>" + "Getting to the mall is easy, too. It’s right off the M1 highway, about 10 minutes south of Port Louis. There’s plenty of parking if you’re driving, but public transportation and taxis are also available with bus stops close by.\n";
                    model.title1 = "";
                    model.image1 = "";
                    model.text2 = "";
                    model.title2 = "";
                    model.text3 = "";
                    model.image2 = "";
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
                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];

                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 1) {

                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "bois_cheri_1" + ".jpg";

                    model.text1 = "Established in 1892, the Bois Chéri Plantation is the largest tea producer in Mauritius, covering more than 250 hectares. The plantation has a factory and museum where guests can learn about the origins and manufacturing of tea.\n" + "<br><br>" + "During your visit, you can join a guided tour that takes you through the factory and museum, as well as the tea fields, and enjoy a tea-tasting session. The guides will explain the history of Bois Chéri and share the importance of tea production in Mauritius.\n";
                    model.title1 = "";
                    model.image1 = fullPath + "/" + "bois_cheri_8" + ".jpg";
                    model.text2 = "For tea lovers and nature enthusiasts, this tour is a must. The plantation’s peaceful surroundings, with its green landscape and nearby crater lake, create the perfect atmosphere for relaxation. The scenic drive around the plantation also offers plenty of photo opportunities, with stunning views of the lake, unique trees, and the sprawling tea fields.\n";
                    model.title2 = "";
                    model.text3 = "";
                    model.image2 = fullPath + "/" + "bois_cheri_6" + ".jpg";
                    model.text4 = "";
                    model.title3 = "The Factory and Museum:\n";
                    model.text5 = "The museum showcases the rich history of tea in Mauritius, featuring old machines used in tea production, including an old locomotive. On the factory tour, you’ll see the tea-making process firsthand. Bois Chéri produces both green and black teas, with their signature being a popular vanilla tea.\n";
                    model.title4 = "";
                    model.text6 = "";
                    model.title5 = "";
                    model.text7 = "";
                    model.title6 = "";
                    model.text8 = "";
                    model.title7 = "";
                    model.image3 = fullPath + "/" + "bois_cheri_7" + ".jpg";
                    model.text9 = "After the tour, head to the factory café near the restaurant for a tea-tasting experience. You’ll be able to try a variety of teas and pair them with treats like waffles or pancakes.\n" + "<br><br>" + "The Bois Chéri restaurant is just a short walk away, offering breathtaking views of the south coast. The restaurant serves unique dishes with a local twist, such as shrimp with green tea and chicken with exotic tea. Whether you’re with friends or family, the tour, tea tasting, and meal make for a delightful and relaxing experience.\n";
                    model.title8 = "";
                    model.text10 = "";
                    model.image4 = fullPath + "/" + "bois_cheri_4" + ".jpg";
//
                    model.text11 = "<br><b><span style=\"color:black; font-weight:bold;\">Visitor Information:</span></b>\n" + "<br><br>" + "<b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;• Visit Hours:</b>" + "<br>" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;• Monday to Friday: 9:00 AM – 2:00 PM\n" + "<br>" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;• Saturday: 9:00 AM – 11:00 AM\n" + "<br>" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;• Closed on Sundays and public holidays\n" + "<br>" + "<b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;• Bois Chéri Tea Factory Operating Hours:</b>" + "<br>" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;• Saturday: 9:00 AM – 02:00 PM\n" + "<br>" + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;• Saturday: 9:00 AM – 11:00 AM\n";

                    model.image5 = "";
                    model.text12 = "";

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 2) {

                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "eureka_house" + ".jpg";

                    model.text1 = "Eureka House is a beautifully preserved Creole mansion that offers a fascinating look into Mauritius's colonial past. Built in the 1830s, this grand house now functions as a museum, taking visitors on a journey through the island's plantation history. The manor’s architecture is designed to handle the tropical climate, with 109 doors allowing for excellent airflow, keeping the interior refreshingly cool during hot summers.\n";
                    model.title1 = "A Step Back in Time\n";
                    model.image1 = fullPath + "/" + "eureka_house_3" + ".jpg";
                    model.text2 = "Inside, the rooms are decorated with a remarkable collection of period furniture, imported by the French East India Company. ";
                    model.title2 = "";
                    model.text3 = "";
                    model.image2 = fullPath + "/" + "eureka_house_2" + ".jpg";
                    model.text4 = "Highlights include antique maps, an old-fashioned shower contraption that was considered a luxury in its time, and a weathered piano with decaying keys, adding to the historical charm of the house.\n";
                    model.title3 = "The Grounds and Waterfall\n";
                    model.text5 = "Beyond the main house, the courtyard features beautifully landscaped grounds, with stone cottages that were once used as servants' quarters and kitchens. A short 15-minute walk from the back of the estate leads to the Ravin Waterfall, a picturesque spot that adds to the property’s allure.\n" + "<br><br>" + "The house gained its current name in 1856, when Eugène Le Clézio purchased it at auction, leaving a legacy that is still celebrated today.\n";
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

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 3) {

                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "grand_bassin_1" + ".jpg";

                    model.text1 = "Grand Bassin, also known as Ganga Talao, is a sacred lake located in the quiet mountain area of Savanne, sitting about 550 meters above sea level. In 1898, Pandit Giri Gossayne, a native of Triolet village, made the first known visit to Ganga Talao. Since then, Hindus have come to regard this place as one of Mauritius' most sacred sites.\n" + "<br><br>" + "On the bank of the lake stands the Shiv Mandir, a temple dedicated to Lord Shiva. Every year, during the Maha Shivaratri festival, around half a million Hindus in Mauritius make a pilgrimage to the lake, with many walking barefoot from their homes, carrying Kanvars (decorated structures used to carry holy water and offerings).\n";
                    model.title1 = "";
                    model.image1 = fullPath + "/" + "grand_bassin_2" + ".jpg";
                    model.text2 = "The lake is symbolically linked to the holy Ganges River in India by its name, Ganga Talao, which means \"Lake of Ganga.\"\n";
                    model.title2 = "History";
                    model.text3 = "In 1866, Pandit Sanjibonlal returned to Mauritius after completing his first labor contract in India and transformed Grand Bassin into a pilgrimage site. He converted a building into a temple and brought a Shivalingam (symbol of Shiva) from India.\n" + "<br><br>" + "In 1897, priest Jhummon Giri Gosagne Napal had a dream where he saw that the waters of Grand Bassin were linked to the holy Ganges. The lake, previously called \"Pari Talao,\" was declared a sacred lake in 1998. In 1972, water from the Ganges River was mixed with the lake’s water, and the name Ganga Talao became official.\n";
                    model.image2 = "";
                    model.text4 = "";
                    model.title3 = "Events";
                    model.text5 = "During Maha Shivaratri, devotees walk to Grand Bassin, and volunteers along the route provide food and drinks to the pilgrims. A 33-meter-tall statue of Lord Shiva, known as Mangal Mahadev, stands near the lake and was inaugurated in 2007. The area also hosts grand celebrations for Durga Pooja and Navaratri, while Shivaratri is a national holiday in Mauritius, celebrated with great devotion by the Hindu community.\n";
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

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 4) {

                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "gymkhana_2" + ".jpg";
                    model.text1 = "Founded by the British Royal Navy in 1844, Gymkhana Golf Course is one of the oldest in the world, ranking as the fourth oldest globally and the oldest in the southern hemisphere. Though it’s smaller than many modern courses, this historic spot in Vacoas is challenging, requiring golfers to use precision and technique.\n" + "<br><br>" + "Surrounded by towering, century-old trees and colorful blooms like bougainvilleas and Gabonese tulips, Gymkhana feels more like a lush garden. It’s an 18-hole course filled with history and charm, making it a special place for golf lovers.\n" + "<br><br>" + "Every golfer who plays here gets a certificate, marking their experience on a course that’s been around since 1844. While it may be smaller than other Mauritian courses, it certainly isn’t lacking in challenge.\n" + "<br><br>" + "Once exclusive to local members, Gymkhana now welcomes golfers from around the world. You can play by paying a small green fee, and there’s even a temporary membership option available, so everyone gets a chance to enjoy this iconic course.\n";
                    model.title1 = "";
                    model.image1 = "";
                    model.text2 = "";
                    model.title2 = "";
                    model.text3 = "";
                    model.image2 = "";
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
                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 5) {

                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "le_pouce_1" + ".jpg";

                    model.text1 = "Le Pouce is the third tallest mountain in Mauritius, standing at 812 meters (2,664 feet). It’s only a bit shorter than Piton de la Petite Rivière Noire (828 meters) and Pieter Both (820 meters). The mountain gets its name from its unique thumb-shaped peak. This dormant basalt volcano, which is a part of the Moka mountain range, provides breathtaking views of the capital city, Le Morne, and Coin de Mire from its summit.\n";
                    model.title1 = "";
                    model.image1 = fullPath + "/" + "le_pouce_2" + ".jpg"; // Set the value for image1
                    model.text2 = "Le Pouce used to be called the \"Mountain of Immortals\" because of a plant that still grows on its slopes today. Climbing this mountain gives you breathtaking panoramic views and a chance to see rare and exotic plants along the way. Fun fact: Charles Darwin himself climbed Le Pouce on May 2, 1836!\n";
                    model.title2 = "Trails";
                    model.text3 = "";
                    model.image2 = fullPath + "/" + "le_pouce_3" + ".jpg"; // Set the value for image2
                    model.text4 = "There are two trails to reach the top of Le Pouce, one starting from Port Louis (north side) and the other from Moka (south side).\n" + "<ul>" + "<li><b>Southern Route:</b> This is the more popular trail, starting at a higher point, making it shorter and easier for most hikers. The trail winds through grassy areas and sugarcane fields before leading up the mountainside. The final stretch is steeper, requiring a bit of hands-on climbing.\n</li>" + "</ul>" + "\t\t<b>• Trail Type:</b> Out and Back" + "<br>" + "\t\t<b>• Start Point:</b> Moka Trailhead" + "<br>" + "\t\t<b>• Distance:</b> 1.4 miles (one way)" + "<br>" + "\t\t<b>• Duration:</b> 2 hours (one way)" + "<br>" + "\t\t<b>• Elevation Gain:</b> 1,165 feet" + "<ul>" + "<li><b>Northern Route:</b> This longer trail from Port Louis offers better views and more shade but is quite a bit tougher. The path is marked with blue and white stones to guide you along.\n</li>" + "</ul>" + "\t\t<b>• Trail Type:</b> Out and Back" + "<br>" + "\t\t<b>• Start Point:</b> Tranquebar Trailhead" + "<br>" + "\t\t<b>• Distance:</b> 3.9 miles (one way)" + "<br>" + "\t\t<b>• Duration:</b> 6 hours (one way)" + "<br>" + "\t\t<b>• Elevation Gain:</b> 2,690 feet" + "<br><br>" + "Both trails share a steep final section at the summit, where caution is needed, especially if the weather turns bad.\n";
                    model.title3 = "Practical Tips";
                    model.text5 = "What to Bring: Water, snacks, proper clothing and shoes, sunscreen, insect repellent, and a first-aid kit.\n" + "<ul>" + "<li>Pack out all your trash—don't leave anything behind.\n</li>" + "<li>Stick to the trails and don’t separate from your group.\n</li>" + "<li>Avoid feeding wild animals.\n</li>" + "<li>Be careful with plants—leave them as they are.\n</li>" + "</ul>";
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

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 6) {

                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "pieter_both_1" + ".jpg";

                    model.text1 = "Pieter Both is one of Mauritius’ most famous landmarks, often featured on postcards for its striking appearance. Thousands of nature lovers visit this mountain for the incredible views and hiking experiences it offers. An interesting tidbit is that the world’s rarest palm, Hyophorbe amaricaulis, once covered this mountain but has since nearly disappeared, making it a rare sight today.\n" + "<br><br>" + "Standing at 820 meters (2,690 feet), Pieter Both is only slightly shorter than the island’s tallest mountain. It is a part of the Moka Range and gets its name from Pieter Both, a Governor-General of the Dutch East Indies. The mountain is easily recognized thanks to the giant boulder perched on its summit, which resembles a human head.\n";
                    model.title1 = "";
                    model.image1 = fullPath + "/" + "pieter_both_2" + ".jpg"; // Set the value for image1
                    model.text2 = "<b><span style=\"color:black; font-weight:bold;\">Legend of Pieter Both</span></b>" + "<br><br>" + "According to local legend, a milkman from the nearby village of Crève Coeur once took a shortcut over the mountain and, tired from his journey, fell asleep. He awoke to the enchanting sound of fairies singing. When they realized they were being watched, they cursed the milkman, saying he’d turn to stone if he told anyone what he’d seen. Unable to keep the secret, he shared the story, and the next time he visited the mountain, the fairies turned him into stone. His \"head\" is said to be the large boulder at the top of the mountain.\n";
                    model.title2 = "Hiking Pieter Both\n";
                    model.text3 = "The Pieter Both hike is popular among hikers of all skill levels, but it’s recommended to bring safety gear like helmets and ropes, especially for the more challenging sections. Guided hikes are available through local agencies for those who want some extra help.\n";
                    model.image2 = fullPath + "/" + "pieter_both_3" + ".jpg"; // Set the value for image2
                    model.text4 = "La Laura village, at the base of the mountain, is where the hike begins. The first 600–650 meters take about an hour and are manageable for anyone in decent shape. As you get higher, though, the hike becomes more difficult, especially when you reach the neck of the mountain. You’ll need good rock-climbing skills to make it to the summit, though there are old iron rungs placed in the rocks that help a little. For the last section, which involves climbing a nine-meter boulder, expect the steepest and most challenging part of the journey.\n";
                    model.title3 = "Safety Tips\n";
                    model.text5 = "<ul>" + "<li>Bring water, a hat, sunglasses, and sunscreen if you’re hiking in the summer.</li>" + "<li>Be cautious after rainy weather, as the trails can get slippery.</li>" + "</ul>" + "Also, don’t forget to look out for the Hyophorbe amaricaulis palm, which is now the rarest palm in the world.\n";
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

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);
                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 7) {

                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "tamarin_falls_1" + ".jpg";

                    model.text1 = "Tamarin Falls, also known as the Seven Cascades, is a stunning waterfall located in the southwestern part of Mauritius, near the village of Henrietta. The falls feature seven tiers of cascades, making it one of the island’s most picturesque spots.\n";
                    model.title1 = "";
                    model.image1 = fullPath + "/" + "tamarin_falls_2" + ".jpg"; // Set the value for image1
                    model.text2 = "The main waterfall stands at an impressive 293 meters (961 feet), making it one of the highest on the island. Surrounded by lush, diverse plant life, Tamarin Falls is a paradise for nature and animal lovers. You can spot exotic plants and birds as you explore the area.\n" + "<br><br>" + "Getting to the falls isn’t easy, though—it takes nearly a full day of hiking to reach this natural wonder. The trail winds through the forest, and at one point, crosses an open basalt rock area that lets you know you're getting close. Once you arrive, you can cool off in the basin or continue climbing to the top. Along the way, you’ll pass small pools before reaching a breathtaking view of the lush green canyon below.\n";
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

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 8) {

                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "trou_aux_cerfs_4" + ".jpg";

                    model.text1 = "Trou aux Cerfs is a dormant volcanic crater surrounded by thick forest, filled with native plants and towering pine trees. Though it hasn’t erupted in about 700,000 years, scientists believe it could potentially become active again one day.\n" + "<br><br>" + "One of the best things about Trou aux Cerfs is the amazing 360-degree view. The town of Curepipe and the surrounding mountains, including Rempart Mountain, Trois Mamelles, and the Port Louis-Moka range, are visible from the summit. It’s also a popular spot for locals, especially joggers who gather around 5 a.m. every morning. There’s a gazebo where you can sit, relax, and enjoy the peaceful surroundings.\n" + "<br><br>" + "Since it’s located at a higher elevation and surrounded by trees, the area can get a little chilly, so it’s a good idea to bring a light sweater or shawl. You can access Trou aux Cerfs from three main roads: La Hausse de la Louviere, Edgar Huges Road, and Crater Lane, and there’s parking available nearby.\n";
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

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                }
                break;
            case "South":
                if (holder.getAdapterPosition() == 0) {
                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "gris_gris_coastal_4" + ".jpg";

                    model.text1 = "Gris Gris Beach stands out in Mauritius for its lack of a coral reef barrier, creating a one-of-a-kind experience. Unlike the island’s usual calm, reef-protected beaches, the ocean here fiercely crashes against the shoreline, offering visitors a dramatic and captivating scene. Most beaches in Mauritius are shielded by coral reefs, which create shallow, calm waters ideal for swimming, but Gris Gris is a wild exception.\n";
                    model.title1 = "";
                    model.image1 = "";
                    model.text2 = "Anybody visiting the island should make time to visit Gris Gris. Located at the southernmost point of Mauritius, just a few kilometers southeast of Souillac, this beach offers breathtaking views of towering cliffs that drop down to the turbulent ocean. The waves crashing into the rocks create striking white foam and a powerful, resounding sound. The beach's beauty is enhanced by its history, which dates back to the French colonial period.\n";
                    model.title2 = "";
                    model.text3 = "";
                    model.image2 = fullPath + "/" + "gris_gris_3" + ".jpg";
                    model.text4 = "The name \"Gris Gris\" adds a layer of intrigue to the beach. As you enter, you’ll notice a sign explaining the name’s origins. According to local tradition, \"Gris Gris\" refers to an African amulet associated with the coastline’s wild nature. But another story offers a twist—it’s said that a French cartographer visiting the area in 1753 named it after his puppy, Gris Gris.";
                    model.title3 = "";
                    model.text5 = "";
                    model.title4 = "";
                    model.text6 = "";
                    model.title5 = "";
                    model.text7 = "";
                    model.title6 = "";
                    model.text8 = "";
                    model.title7 = "";
                    model.image3 = fullPath + "/" + "gris_gris_1" + ".jpg";
                    model.text9 = "The main beach area features a large grassy field overlooking the cliffs and crashing waves. Benches and a pavilion provide places to relax and take in the stunning scenery. To the left, a concrete staircase leads down to the beach itself. Swimming here, however, is strongly discouraged due to the dangerous waves that can quickly overpower even the strongest swimmers. Instead, it’s best to enjoy a peaceful walk along the beach, where you’ll find a small cave at the far end.\n";
                    model.title8 = "Caves at Gris Gris\n";
                    model.text10 = "";
                    model.image4 = fullPath + "/" + "grisgris_2" + ".jpg";
                    model.text11 = "Aside from the cave on the far left of the beach, there are two more hidden caves to discover at Gris Gris. These are harder to reach and require descending a cliff and walking through the water. Caution is key—water levels can rise unexpectedly, and the currents are often strong. For a safer option, head to the cliff’s edge across from the parking lot. From there, you can descend about halfway down to catch a glimpse of the caves on your right. Keep in mind, though, that entering the caves can be risky if the water starts to rise.\n" + "<br><br>" + "Gris Gris is closely tied to the nearby village of Souillac, which relies on tourism for much of its income. " + "<br><br>" + "Established 200 years ago as a port for ships traveling between Europe and India, Souillac has a rich history worth exploring. While you’re in the area, consider visiting Rochester Falls, just outside the village. It’s famous for its rectangular rock formations, making it a great addition to your day in southern Mauritius.\n" + "<br><br>" + "<b><span style=\"color: #000000; font-weight: 1400; text-shadow: 1px 1px 0 #000, -1px -1px 0 #000, 1px -1px 0 #000, -1px 1px 0 #000;\">\n" + "    La Roche qui Pleure\n" + "</span></b>\n\n";
                    model.image5 = fullPath + "/" + "la_roche_qui_pleure" + ".jpg";
                    model.text12 = "\n" + "La Roche qui Pleure, meaning \"The Weeping Rock,\" gets its name from the way water trickles down its face, giving the cliffs the appearance of crying. Some visitors even claim to see the weathered features of the Mauritian poet Robert Edward Hart in the cliffs’ eroded surfaces.\n" + "<br><br>" + "Like Gris Gris, La Roche qui Pleure is free from coral reefs, leaving its shores fully exposed to the ocean’s might. This lack of a natural barrier results in larger, more spectacular waves, offering a striking contrast to the calm, shallow lagoons found elsewhere on the island. Strong winds and seasonal changes also shape the landscape, giving this spot its unique and rugged charm.\n";

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 1) {

                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "rochester_falls_1" + ".jpg";

                    model.text1 = "Rochester Falls may not be the largest or most famous waterfall in Mauritius, but it’s definitely worth a visit if you’re in the area. To get there, just follow the handmade signs that lead you from the main road through Souillac. The path can be a little tricky, with a rough, stony track, but it’s easy to follow. Local vendors may offer to guide you to a parking spot, and it's polite to offer them a small tip. After a short five-minute walk from your car, you’ll find yourself at the waterfall, where the water cascades from the sugarcane fields above.\n" + "<br><br>" + "Located in southern Mauritius, Rochester Falls is known for its unique volcanic rock formations. The water flows through these square-shaped rocks, creating a visually stunning effect that sets this waterfall apart from others on the island. It’s a peaceful spot, popular with both locals and tourists, offering a serene pond for swimming. Surrounded by a wild, lush forest, it’s a perfect escape from the everyday hustle, making it an ideal spot for a relaxing day with friends.\n";
                    model.title1 = "A few tips:";
                    model.image1 = "";
                    model.text2 = "<ul>" + "<li>It’s best to avoid visiting during or after heavy rainfall.</li>" + "<li>Be cautious if you plan to cliff jump, as the rocks can get slippery.</li>" + "</ul>";
                    model.title2 = "";
                    model.text3 = "";
                    model.image2 = "";
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

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 2) {
                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "bel_ombre_1" + ".jpg";

                    model.text1 = "Bel Ombre is more than just a beautiful coastal spot; it’s a natural paradise where the sounds of chirping birds and the sight of lush greenery create a peaceful escape. With views of towering mountains, hidden waterfalls, and sprawling forests, Bel Ombre Nature Reserve is a protected area overseen by the Mauritian Wildlife Foundation. It’s like stepping into a real-life Garden of Eden, with a focus on sustainability and environmental care. Spanning 1,300 hectares within the Domaine de Bel Ombre, the reserve invites visitors to explore through activities like quad biking, trekking, Segway rides, and 4x4 tours.\n";
                    model.title1 = "";
                    model.image1 = fullPath + "/" + "bel_ombre_11" + ".jpg";
                    model.text2 = "Nestled between the clear blue waters of the Indian Ocean and the rich, green endemic forests of the South, Bel Ombre is a treasure trove of biodiversity. The reserve’s mountainous landscape is home to a variety of wildlife, including roaming stags and tropical birds. It also offers impressive waterfalls and untouched natural beauty. A range of activities, from adventurous to relaxing, allows visitors to discover the wonders of this special place.\n";
                    model.title2 = "Activities at Bel Ombre Nature Reserve:\n";
                    model.text3 = "<b>• 4x4 Excursion:</b> Take a ride through the reserve’s diverse flora and fauna, stopping at the picturesque l'Example waterfall along the way.\n";
                    model.image2 = fullPath + "/" + "bel_ombre_6" + ".jpg";

                    model.text4 = "<b>• Quad & Buggy Adventure:</b>\n Get your adrenaline pumping with a thrilling ride through the natural landscape, including a refreshing stop for a swim at a waterfall.\n";
                    model.title3 = "";
                    model.text5 = "";
                    model.title4 = "";
                    model.text6 = "";
                    model.title5 = "";
                    model.text7 = "";
                    model.title6 = "";
                    model.text8 = "";
                    model.title7 = "";
                    model.image3 = fullPath + "/" + "bel_ombre_9" + ".jpg";
                    model.text9 = "<b>• Kids Discovery Tour:</b> Perfect for young explorers, this tour includes a drive through sugarcane fields and a freshwater fishing trip.\n" + "<br>" + "                                <b>• Trekking Exploration:</b>\n Enjoy a scenic nature walk starting at the Old Chimney, passing through the river reserve, and ending with a swim in a natural rock pool at the base of a waterfall.\n";
                    model.title8 = "";
                    model.text10 = "";
                    model.image4 = fullPath + "/" + "bel_ombre_12" + ".jpg";


                    model.text11 = "   <b>• Segway Ride</b> Explore the stunning scenery of Domaine de Bel Ombre during a 45-minute Segway ride. The route includes stops at key landmarks like Frederica Welcome Center, Heritage Golf Club, C Beach Club, Heritage Le Telfair Golf and Spa Resort, Place du Moulin, and Le Château de Bel Ombre before returning to the starting point.\n";
                    model.image5 = "";
                    model.text12 = "";

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 3) {

                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "valle_des_couleurs" + ".jpg";

                    model.text1 = "\n" + "In the southern part of Mauritius lies La Vallée des Couleurs, a stunning 450-acre nature reserve that offers visitors a peaceful retreat and a chance to connect with nature. Located in Mare Anguilles, Chamouny, the park is open daily from 9 a.m. to 5:30 p.m., offering plenty of activities to make your visit unforgettable.\n" + "<br><br>" + "At La Vallée des Couleurs, you can explore the charming animal farm, enjoy delicious food with a view of grazing deer and a nearby waterfall, and take part in a variety of activities like trekking, luge karting, Nepalese bridge rides, quad bike tours, and ziplining. The park’s standout feature is the fascinating 23-colored earth, a unique natural phenomenon resulting from volcanic eruptions millions of years ago.\n";
                    model.title1 = "";
                    model.image1 = fullPath + "/" + "valle_des_couleurs_9" + ".jpg";
                    model.text2 = "Since its opening on July 4, 1998, the park has grown from a simple nature trail destination into a hub of adventure and entertainment. The 23-colored earth, surrounded by lush greenery and vibrant plant life, is one of the park's main attractions. Visitors can also take in panoramic views of the sea while exploring the diverse flora and fauna.\n";
                    model.title2 = "";
                    model.text3 = "";
                    model.image2 = fullPath + "/" + "valle_des_couleurs_2" + ".jpg";
                    model.text4 = "";
                    model.title3 = "Popular Activities at La Vallée des Couleurs:\n";
                    model.text5 = "<ul>" + "<li><b>Trekking:</b> Enjoy a scenic walk through the park's natural beauty.</li>" + "</ul>";
                    model.title4 = "";
                    model.text6 = "";
                    model.title5 = "";
                    model.text7 = "";
                    model.title6 = "";
                    model.text8 = "";
                    model.title7 = "";
                    model.image3 = fullPath + "/" + "valle_des_couleurs_17" + ".jpg";
                    model.text9 = "<ul>" + "<li><b>Mountain Luge Karting:</b> Zoom down a 700-meter track for a thrilling ride.</li>" + "<li><b>Nepalese Bridge Walk:</b> Cross the 350-meter footbridge for an exhilarating experience with sweeping views of the park.</li>" + "<li><b>Ziplining:</b> Fly over the park on seven zip lines, including the longest one, which stretches over 1.5 kilometers.</li>" + "<li><b>Quad Biking & Buggy Rides:</b> Explore the tropical forest and the 23-colored earth while interacting with wildlife.</li>" + "<li><b>Animal Farm:</b> Visit the park’s resident tortoises, chickens, and deer.</li>" + "</ul>";
                    model.title8 = "";
                    model.text10 = "";
                    model.image4 = fullPath + "/" + "valle_des_couleurs_12" + ".jpg";
                    model.text11 = "For those who prefer a more leisurely visit, many of the park’s major attractions are within a 15-minute walk. You can also choose to explore faster by renting a quad bike or buggy. One of the park's highlights is the 23-colored earth, which can be seen during the quad bike tour. This remarkable site is rich in minerals left behind by ancient volcanic eruptions.\n" + "\n<br><br>" + "<b><span style=\"color:black; font-weight:bold;\">Sensory Experiences at La Vallée des Couleurs:</span></b>";
                    model.image5 = fullPath + "/" + "valle_des_couleurs_5" + ".jpg";
                    model.text12 = "La Vallée des Couleurs truly engages all the senses. The vibrant 23-colored land is a visual feast, while the sounds of birds and waterfalls, the scent of wet tree trunks, and the taste of local cuisine create a fully immersive experience. Ziplining across the park gives you a bird’s-eye view of the breathtaking landscape, while trekking offers a two-hour journey through nature, ending at a waterfall.\n" + "<br><br>" + "<b><span style=\"color:black; font-weight:bold;\">Food and Dining:</span></b>";

                    model.image6 = fullPath + "/" + "vallee_des_couleurs_1" + ".jpg";
                    model.text13 = "La Vallée des Couleurs has several restaurants offering a range of dishes, including an Indian restaurant serving a mix of Subcontinental and French cuisine. The dining areas are set around the waterfalls, allowing visitors to enjoy their meals in a serene and beautiful setting.\n" + "<br><br>" + "Whether you're seeking adventure or relaxation, La Vallée des Couleurs offers a unique blend of wildlife, natural beauty, and exciting activities, making it a great destination for visitors of all ages.\n";

                    model.image7 = "";
                    model.text14 = "";
                    model.image8 = "";
                    model.text15 = "";

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 4) {

                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "blue_bay" + ".jpg";

                    model.text1 = "Located on the southeastern coast of Mauritius, Blue Bay was designated a national park in 1997 and later recognized as a wetland of international importance under the Ramsar Convention in 2008. The marine park is famous for its coral garden, which is home to a wide variety of corals and marine life. Its close proximity to the coastline, combined with calm and shallow waters, makes Blue Bay an ideal spot for snorkeling and exploring its rich biodiversity.\n";
                    model.title1 = "";
                    model.image1 = fullPath + "/" + "blue_bay_4" + ".jpg";
                    model.text2 = "When you arrive at Blue Bay Beach, you’ll likely encounter local vendors selling handmade jewelry and people offering glass-bottom boat trips or snorkeling tours. More than fifteen operators are licensed to run businesses within the Blue Bay Marine Park.\n" + "<br><br>" + "One of the park’s main attractions is an ancient brain coral that’s over 1,000 years old and has a diameter of 5 meters, making it a must-see for visitors. The coral garden near Mahebourg, a small village in the southeast, spans a large area and features incredible biodiversity. The brain coral is a popular tourist attraction, and glass-bottom boat rides offer a tranquil way to view the undersea environment.\n";
                    model.title2 = "";
                    model.text3 = "";
                    model.image2 = fullPath + "/" + "blue_bay_2" + ".jpg";
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
                    model.text9 = "Coastguards patrol the area regularly to make sure the park's rules are followed and to safeguard marine life.\n";
                    model.title8 = "Protected Marine Area Features:\n";
                    model.text10 = "<b>Incredible Marine Biodiversity:</b> Blue Bay Marine Park is home to various ecosystems, including coral reefs, seagrass meadows, mangroves, and macroalgae beds. The park supports over 38 species of coral, 72 species of fish, 31 species of algae, 2 mangrove species, and 4 types of seagrass plants.\n";
                    model.image4 = fullPath + "/" + "blue_bay_3" + ".jpg";
                    model.text11 = "<ul>" + "<li><b>Unique Status in Mauritius:</b> Blue Bay Marine Park is the only one in Mauritius to be classified under the Wildlife and National Parks Act of 1993. It became a protected zone in 2000 under the Fisheries and Marine Act and was later recognized as a Ramsar site in 2008. Mooring buoys are used to prevent boat anchors from damaging the coral.</li>" + "</ul>";
                    model.image5 = fullPath + "/" + "blue_bay_8" + ".jpg";
                    model.text12 = "<ul>" + "<li><b>Activities for Everyone:</b> Glass-bottom boat trips and snorkeling are popular activities that allow non-swimmers to enjoy the marine environment without diving in. Snorkelers can also relax under the casuarina trees on Coco Island, which is visible from the beach. Operators offer convenient drop-off and pick-up services for snorkelers.\n</li>" + "</ul>";

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 5) {

                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "mahebourg" + ".jpg";

                    model.text1 = "Situated in the heart of a traditional Mauritian village, the newly developed Mahebourg Waterfront is an ideal spot for a relaxing walk and a glimpse into the island’s history. Visitors can explore the naval museum and learn about the famous battles that took place in the region. The Bataille de le Passe memorial honors the fighters who lost their lives in this historic conflict.\n";
                    model.title1 = "";
                    model.image1 = fullPath + "/" + "mahebourg_5" + ".jpg";
                    model.text2 = "Located just behind the bus station, the iconic Sir Gaetan Duval Esplanade is a key feature of the Mahebourg Waterfront. The area is perfect for those who enjoy seaside strolls, offering a peaceful environment with stunning sea views.\n";
                    model.title2 = "";
                    model.text3 = "";
                    model.image2 = fullPath + "/" + "mahebourg_waterfront_1" + ".jpg";
                    model.text4 = "As you explore the waterfront, you’ll discover the rich history of the Grand Port battle, a significant event that has left a lasting mark on Mauritius' heritage. Don’t miss the picturesque view of 'Mouchoir Rouge' island, which can be seen from the quay.\n";
                    model.title3 = "";
                    model.text5 = "";
                    model.title4 = "";
                    model.text6 = "";
                    model.title5 = "";
                    model.text7 = "";
                    model.title6 = "";
                    model.text8 = "";
                    model.title7 = "";
                    model.image3 = fullPath + "/" + "mahebourg_3" + ".jpg";
                    model.text9 = "Across from the Mahebourg Waterfront, you’ll find a lively local market filled with street food, local delicacies, and fresh produce. It’s a terrific place to sample authentic Mauritian flavors and do a little shopping. While the waterfront is a popular weekend destination for locals—especially picnickers and those looking to unwind—you might want to visit during the week for a quieter experience.\n";
                    model.title8 = "";
                    model.text10 = "";
                    model.image4 = "";
                    model.text11 = "";
                    model.image5 = "";
                    model.text12 = "";

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 6) {

                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "mahebourg_museum_2" + ".jpg";

                    model.text1 = "Tucked away in the scenic southeast of Mauritius, the National History Museum in Mahebourg offers a fascinating glimpse into the island’s past. Admire rare discoveries like an almost whole dodo skeleton, naval artifacts, and in-depth exhibits that shed light on colonial life. Housed in an old-world mansion designated a National Heritage Site, the museum is as charming as it is historic.\n" + "<br><br>" + "Known as Gheude Castle, the mansion was built in the late 18th century and originally belonged to the de Robillard family. Its original owner was Grand Port district commander Jean de Robillard. Over the years, the house passed through the hands of several prominent French settlers before the Mauritian government purchased it in 1947. It was later transformed into a Naval and Historical Museum, and a special Dutch section was opened by a descendant of Maurits Van Nassau, the man after whom Mauritius was named in 1598.\n";
                    model.title1 = "";
                    model.image1 = fullPath + "/" + "mahebourg_museum_5" + ".jpg";
                    model.text2 = "The museum is surrounded by 12 acres of lush gardens, complete with reproductions of traditional village houses. Inside, you’ll find a diverse collection that includes artifacts salvaged from naval battles, old maps, coins, furniture, and, of course, the famous dodo skeleton. One highlight is the weaponry used by the corsair Robert Surcouf, as well as the bell recovered from the wreck of the Saint Geran, a ship that sank off the East Coast in the 18th century.\n";
                    model.title2 = "";
                    model.text3 = "";
                    model.image2 = fullPath + "/" + "mahebourg_6" + ".jpg";
                    model.text4 = "Just outside the museum, a cannon from the British frigate La Magicienne stands proudly, facing the entrance from the main road to Mahebourg. Inside, on the first floor, you can see a four-poster bed once owned by Mahe de Labourdonnais, Mauritius' first French governor. The museum also displays palanquins once carried by slaves, giving visitors a glimpse into the island’s regal past.\n";
                    model.title3 = "";
                    model.text5 = "";
                    model.title4 = "";
                    model.text6 = "";
                    model.title5 = "";
                    model.text7 = "";
                    model.title6 = "";
                    model.text8 = "";
                    model.title7 = "";
                    model.image3 = fullPath + "/" + "mahebourg_museum_4" + ".jpg";
                    model.text9 = "One interesting historical detail is tied to a naval battle that took place in August 1810 at Vieux Grand Port, not far from the museum. Both British and French commanders—Sir Nesbit Willoughby and Baron Victor Duperre—were injured in the battle and were treated in the same wing of what is now the museum. This battle remains the only French naval victory against the British, and it is commemorated on the Arc de Triomphe in Paris.\n" + "<br><br>" + "The Mahebourg Museum is just a 10-minute drive from the airport, making it a convenient stop for anyone visiting the southeast of the island. The museum is open from Monday to Saturday, 9:00 AM to 4:00 PM, and on Sundays from 9:00 AM to 12:00 PM. Admission is free, though visitors should note that interior photography is not allowed, ensuring a fully immersive experience as you explore the rich history housed within this delightful museum.\n";
                    model.title8 = "";
                    model.text10 = "";
                    model.image4 = "";
                    model.text11 = "";
                    model.image5 = "";
                    model.text12 = "";

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 7) {
                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "ile_aux_fouquets" + ".jpg";

                    model.text1 = "Île aux Fouquets, also called Île au Phare or Lighthouse Island, is a small coral island off the southeast coast of Mauritius. It was declared an Islet National Park on June 5, 2004. Situated about five kilometers from the mainland, near Ilot Vacoas and Île de la Passe, Île aux Fouquets sits in the only opening in the coral reef that allows large vessels to pass through. Covering an area of 2.49 hectares, the island gets its name from a species of tern called \"fouquet,\" which local fishermen named after observing the bird, as noted by German zoologist Karl August Möbius.\n" + "<br><br>" + "Around 1694, a group of Huguenot refugees led by the Frenchman François Leguat lived on Île aux Fouquets for several years. They had originally tried to establish a Protestant colony called Eden on Rodrigues Island, but after giving up on the project, they sailed to Mauritius. They traveled in a handmade barge without an anchor or compass and eventually arrived safely. However, they soon found themselves in conflict with the Dutch governor and were exiled to the treeless Île aux Fouquets. Years later, they were taken to Java.\n" + "<br><br>" + "Île aux Fouquets is also famous for its connection to the 1810 Battle of Grand Port, where the French scored their only naval victory over the British. More than fifty years later, in 1864, a lighthouse was built on the island. Though the structure is now in ruins, it remains a protected historic site. The island offers stunning views of the surrounding islands, the coastline, and the striking Lion Mountain in the distance.\n";
                    model.title1 = "Île aux Fouquets Lighthouse\n";
                    model.image1 = fullPath + "/" + "ile_aux_fouqets_3" + ".jpg";
                    model.text2 = "Built in 1864, this lighthouse once guided ships toward the harbor at Mahébourg. Mahébourg was initially the main French settlement and served as an important port until the early 20th century when the British shifted port activities to Port Louis on the other side of the island. As a result, the lighthouse was eventually abandoned and fell into disrepair.\n" + "<br><br>" + "Located alongside Île de la Passe and Île aux Vacoas, Île aux Fouquets (also called Île au Phare) is the second island in this chain. It is hillier than the others, with the remnants of the lighthouse located at the top. Although no restoration efforts have been made, the ruins provide shade and shelter, making it a popular picnic spot for locals and fishermen. A colony of tropical birds has also made the steep hillsides behind the lighthouse their home.\n" + "<br><br>" + "Accessible only by boat, Île aux Fouquets offers a distant yet captivating view from the mainland.\n";
                    model.title2 = "";
                    model.text3 = "";
                    model.image2 = "";
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

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 8) {
                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "ile_aux_aigrettes_1" + ".jpg";

                    model.text1 = "Just off the southeastern coast of Mauritius, Île aux Aigrettes serves as both a nature reserve and a hub for scientific research. The island spans 27 hectares (67 acres) and is the largest islet in the Grand Port Bay, sitting 850 meters (2,790 feet) from the mainland and about a kilometer from the coastal town of Mahebourg. Unlike the volcanic terrain of most of Mauritius, Île aux Aigrettes is made of coral limestone and is relatively flat.\n";
                    model.title1 = "";
                    model.image1 = fullPath + "/" + "ile_aux_aigrettes_4" + ".jpg";
                    model.text2 = "Île aux Aigrettes is home to the last remaining fragment of the “Mauritius Dry Coastal Forest,” a unique ecosystem that once thrived on the island. As a result, the reserve is a sanctuary for several rare and endangered species of plants and animals.\n";
                    model.title2 = "";
                    model.text3 = "";
                    model.image2 = fullPath + "/" + "pink_pigeon" + ".jpg";
                    model.text4 = "Over the centuries, the island’s original vegetation and wildlife were severely impacted by logging and the introduction of invasive species, a fate shared by much of Mauritius. This led to the extinction of the dodo and the native giant tortoise, along with many plant species. However, a few species survived. In 1965, Île aux Aigrettes was designated a nature reserve, and efforts to restore the native vegetation and protect the remaining wildlife began. Several species that had disappeared from the island but still survived elsewhere in Mauritius were reintroduced to Île aux Aigrettes as part of this restoration effort.\n";
                    model.title3 = "";
                    model.text5 = "";
                    model.title4 = "";
                    model.text6 = "";
                    model.title5 = "";
                    model.text7 = "";
                    model.title6 = "";
                    model.text8 = "";
                    model.title7 = "";
                    model.image3 = fullPath + "/" + "ile_aux_aigrettes_3" + ".jpg";
                    model.text9 = "The island’s reptile population includes the Telfairs Skink, a slow-moving, large lizard, as well as several species of brightly colored day geckos. The island is also home to non-native Aldabra giant tortoises, which play a crucial ecological role. By eating and dispersing seeds, they help the forest regenerate, filling the gap left by the extinction of the native Mauritian tortoises.\n";
                    model.title8 = "";
                    model.text10 = "";
                    model.image4 = fullPath + "/" + "ile_aux_aigrettes_2" + ".jpg";
                    model.text11 = "Among the rare plant species on the island is the endemic ebony tree, Diospyros egrettarum, which thrives on Île aux Aigrettes and gives the island part of its name.\n";
                    model.image5 = "";
                    model.text12 = "";

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                }
                break;
            case "East":
                if (holder.getAdapterPosition() == 0) {
                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "ferney_1" + ".jpg";

                    model.text1 = "La Vallée de Ferney is a 200-hectare forest and wildlife reserve in the Grand Port District of the Bambou Mountains, north of Mahébourg. It’s managed by the La Vallée de Ferney Conservation Trust, which was established in 2006 in partnership with the Mauritian Wildlife Foundation (MWF) and local authorities. The primary focus is on restoring indigenous forests and protecting the unique biodiversity of the area.\n";
                    model.title1 = "";
                    model.image1 = fullPath + "/" + "ferney_3" + ".jpg";
                    model.text2 = "The valley became a focal point in 2004 when plans for a new highway threatened its local plant and animal life. Surveys revealed new and thought-to-be-extinct species, which led to local efforts to reroute the highway and protect the reserve. With its rich volcanic soil and humid climate, La Vallée de Ferney is one of the last untouched nature refuges on the island.\n";
                    model.title2 = "";
                    model.text3 = "";
                    model.image2 = fullPath + "/" + "ferney_4" + ".jpg";
                    model.text4 = "Visitors to La Vallée de Ferney can enjoy hiking trails through native forests, guided tours, and a stone museum that highlights the history of the area. The reserve also features gardens showcasing useful plants, a restaurant, and a visitor’s center. You’ll even find giant tortoises and a nursery for endangered plants. The valley is home to many rare species, including the Mauritius kestrel and the Mauritian flying fox.\n";
                    model.title3 = "";
                    model.text5 = "";
                    model.title4 = "";
                    model.text6 = "";
                    model.title5 = "";
                    model.text7 = "";
                    model.title6 = "";
                    model.text8 = "";
                    model.title7 = "";
                    model.image3 = fullPath + "/" + "ferney_conservation_park_1" + ".jpg";
                    model.text9 = "Since less than 2% of Mauritius' original ecosystems remain, future projects at La Vallée de Ferney aim to expand the propagation of native plants, remove invasive species, and reintroduce endangered birds like the pink pigeon and echo parakeet. The reserve also collaborates with La Vallée de l'Est, another conservation area, to protect an additional 70 hectares of highland forest.\n";
                    model.title8 = "";
                    model.text10 = "";
                    model.image4 = "";
                    model.text11 = "";
                    model.image5 = "";
                    model.text12 = "";

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 1) {

                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "frederik_hendrik_museum_1" + ".jpg";

                    model.text1 = "The Vieux Grand Port Heritage Site is located on the southeast coast of Mauritius, about 4 kilometers north of Mahebourg and Pointe d’Esny. This historic spot, set at the foot of Lion Mountain, 7 meters above sea level, offers stunning views of the bay and holds a rich history.\n";
                    model.title1 = "";
                    model.image1 = fullPath + "/" + "frederik_hendrik_museum_5" + ".jpg";
                    model.text2 = "";
                    model.title2 = "Interesting Facts About Vieux Grand Port Heritage Site:\n";
                    model.text3 = "It was home to the first Dutch East India Company (VOC) fort in the eastern hemisphere.\n" + "<ul>" + "<li>It was the first Dutch fort built to defend the island from the sea.\n</li>" + "<li>It saw the first slave uprising in Mauritius, during which the slaves set fire to the wooden fort.\n</li>" + "<li>The rebellion led to a change in architecture, as the Dutch started using stone to build forts for greater safety.\n</li>" + "</ul>" + "In 1658, Dutch commander Cornelius Gooyer and his crew of 25 men built a small wooden fort shaped like a four-pointed star. Life in Mauritius was difficult, with attacks from slaves, damage from cyclones, and crops destroyed by rodents. After a fire in 1694, the Dutch rebuilt the fort using stone.\n";
                    model.image2 = fullPath + "/" + "frederik_hendrik_museum_4" + ".jpg";
                    model.text4 = "The Dutch eventually left Mauritius in 1710, destroying the site before their departure, leaving only a stone lodge behind. The stone jetty in the lagoon beneath the fort is still visible. The French used the stone lodge as their administrative center after conquering Mauritius, and they converted it into a military outpost to protect Vieux Grand Port Bay. In 1806, this outpost was moved to Mahebourg.\n" + "<br><br>" + "Today, remnants of French structures, such as a powder house, prison, bakery, and workshop, sit on top of the old Dutch ruins. In 1998, the site was restored to celebrate the 400th anniversary of the first Dutch landing in Mauritius. The renovation was inaugurated by a descendant of Maurits van Nassau, after whom the island was named.\n" + "<br><br>" + "Adjacent to the fort, the Frederik Hendrik Museum was opened in 1999. It houses artifacts uncovered during archaeological excavations, with a permanent exhibition that includes old maps, military items, pottery, cooking utensils, coins, and more.\n";
                    model.title3 = "";
                    model.text5 = "";
                    model.title4 = "";
                    model.text6 = "";
                    model.title5 = "";
                    model.text7 = "";
                    model.title6 = "";
                    model.text8 = "";
                    model.title7 = "";
                    model.image3 = fullPath + "/" + "frederik_hendrik_museum_2" + ".jpg";
                    model.text9 = "Before visiting the site, make sure to check out the monument commemorating the first Dutch landing on September 20, 1598, led by Admiral Wybrand Warwick. From there, you can admire the beautiful Lion Mountain.\n";
                    model.title8 = "<b>Visitor Information:\n</b>";
                    model.text10 = "<b>Address:</b> Royal Road, Old Grand Port, Mauritius (a 15-minute drive from the airport).\n" + "<br><br>" + "<b>Visiting Hours:\n</b>" + "<br><br><ul>" + "<li>Monday, Tuesday, Thursday, Friday, Saturday: 9:00 AM – 4:00 PM\n</li>" + "<li>Wednesday: 11:00 AM – 4:00 PM\n</li>" + "<li>Sunday: 9:00 AM – 12:00 PM\n</li>" + "<li>Closed on public holidays.\n</li>" + "</ul>" + "<b>Admission:</b> Free\n" + "<br><br>" + "<b>Nearby Attractions:</b> Ile aux Aigrettes Nature Reserve and the Mahebourg Museum.\n";
                    model.image4 = "";
                    model.text11 = "";
                    model.image5 = "";
                    model.text12 = "";

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 2) {

                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "belle_mare_1" + ".jpg";
                    model.text1 = "Belle Mare Beach, located near Mahebourg on the southeastern coast of Mauritius, stretches between the villages of Belle Mare and Pointe de Flacq. Sheltered by a coral reef, this crescent-shaped beach is known for its calm, transparent waters and fine, dazzling white sand. The lagoon here is a stunning shade of turquoise, offering a breathtaking view of unspoiled nature. Development is minimal, with only a few hotels and luxury villas scattered along the coast.";
                    model.title1 = "";
                    model.image1 = fullPath + "/" + "belle_mare_3" + ".jpg";
                    model.text2 = "Loved by both tourists and locals, Belle Mare Beach has retained its authentic charm, unlike other rapidly developing coastal areas such as Grand Baie, Flic en Flac, and Black River. The beach is perfect for swimming and diving, especially on weekdays when the atmosphere is quieter and more relaxed. Nature lovers will appreciate the long stretches of sandy shoreline, ideal for sunbathing and peaceful walks.\n";
                    model.title2 = "";
                    model.text3 = "Belle Mare Beach is best visited in the morning, from 7:00 to 11:00, or in the afternoon, from 1:00 to 5:00, as these times tend to be less crowded. There are plenty of activities to enjoy, including pedalo rides, windsurfing, catamaran trips, horseback riding, parasailing, scuba diving, speed boat rides, and kayaking.\n";
                    model.image2 = "";
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
                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 3) {

                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "poste_lafayette_1" + ".jpg";
                    model.text1 = "Poste Lafayette Beach, an often-overlooked gem on the eastern coast of Mauritius, offers an escape into unspoiled nature. Though its waters can be turbulent, this beach provides a rare opportunity for peace and tranquility, making it a favorite among nature enthusiasts. The area is rich with marine biodiversity, supported by massive coral reefs and lush mangroves, offering an authentic and rustic charm.";
                    model.title1 = "";
                    model.image1 = fullPath + "/" + "poste_lafayette_2" + ".jpg";
                    model.text2 = "The southern part of the beach is perfect for a quiet picnic, providing a peaceful retreat for visitors. It’s also a great starting point for a scenic trek along the rocky trails to the north of Poste Lafayette, where you can enjoy picturesque green landscapes.";
                    model.title2 = "";
                    model.text3 = "";
                    model.image2 = fullPath + "/" + "poste_lafayette_3" + ".jpg";
                    model.text4 = "Picnicking under the trees, with stormy waves in the background due to the nearby reefs, is a unique experience here. Due to the beach's year-round exposure to trade winds, Poste Lafayette is a great place for fans of kite and windsurfing.";
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
                    model.text9 = "Although swimming can be challenging due to the rough sea, the shallow waters are perfect for sunbathing, and kids can enjoy building sandcastles along the shore. The eastern coast also boasts a variety of hotels and restaurants, including Jalsa Beach Hotel & Spa, Radisson Blu Poste Lafayette Resort & Spa, and the luxurious Hôtel Constance Le Prince Maurice.\n" + "For dining, Poste Lafayette offers both gourmet and traditional options. La Maison D’Été is a must-visit, serving Italian cuisine with fresh seafood, while Seabell is a local favorite for Mauritian dishes. The nearby mini market, owned by Seabell's owners, is convenient for grabbing snacks or essentials.\n";
                    model.title8 = "";
                    model.text10 = "";
                    model.image4 = "";
                    model.text11 = "";
                    model.image5 = "";
                    model.text12 = "";
                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 4) {

                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "roche_noire_2" + ".jpg";
                    model.text1 = "Located just a few kilometers from Poste Lafayette on the northeast coast, Roche Noire Beach is ranked among the top 10 beaches in Mauritius. The beach is known for its striking landscape, featuring black lava rocks and a darker sea where gentle waves meet the coral reef. Roche Noire Beach is a genuinely unique location, featuring immaculate sand and breathtaking views of the sunrise and sunset. The refreshing winds make it especially pleasant during the hot summer months, and it’s one of the most photographed beaches according to tourist reviews.";
                    model.title1 = "";
                    model.image1 = fullPath + "/" + "roche_noire_3" + ".jpg";
                    model.text2 = "Roche Noire Beach is a haven for water lovers and nature enthusiasts alike. Activities such as swimming, snorkeling, and kayaking allow visitors to explore the colorful coral reefs and marine life. The peaceful atmosphere makes it ideal for leisurely walks along the shore or relaxing while sunbathing.\n" + "<br><br>" + "Parking is easily accessible near the beach, with both free and paid options available. One of the beach’s standout features is its volcanic rock formations, which add to its natural beauty and give Roche Noire a distinct, picturesque charm.<br><br>";
                    model.title2 = "";
                    model.text3 = "";
                    model.image2 = "";
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
                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 5) {

                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "ile_aux_cerfs_mauritius_1" + ".jpg";
                    model.text1 = "Located on the eastern coast of Mauritius, just south of Trou d’Eau Douce, Ile aux Cerfs is part of a stunning duo of islands, offering a true paradise experience across 87 hectares. Known for its breathtaking lagoon and pristine beaches, it’s a favorite among water sports enthusiasts and is considered one of the most beautiful beaches in Mauritius.";
                    model.title1 = "What to Explore and Experience on Ile aux Cerfs";
                    model.image1 = fullPath + "/" + "ile_aux_cerfs_7_1" + ".jpg";
                    model.text2 = "Ile aux Cerfs is famous for its turquoise lagoon, which is perfect for a wide variety of water sports. One of the highlights is parasailing, offering unforgettable views of the lagoon and the surrounding landscape. Other popular activities include speedboat rides, water skiing, and paddleboarding. For snorkeling, a boat trip is required to reach areas with coral, as the water near the beach is shallow and mostly sandy.";
                    model.title2 = "";
                    model.text3 = "";
                    model.image2 = fullPath + "/" + "ile_aux_cerfs_5_1" + ".jpg";
                    model.text4 = "While the island is a hub for water activities, it’s also ideal for sunbathers. With limited shade available on the beach, visitors are advised to bring plenty of sunscreen—preferably eco-friendly options. Most tourists stay near the pier where the shuttle drops them off, but if you want to escape the crowds, a short walk along the coast will lead you to quieter beaches, especially on weekdays.";
                    model.title3 = "How to Reach Ile aux Cerfs ";
                    model.text5 = "";
                    model.title4 = "";
                    model.text6 = "";
                    model.title5 = "";
                    model.text7 = "";
                    model.title6 = "";
                    model.text8 = "";
                    model.title7 = "";
                    model.image3 = fullPath + "/" + "ile_aux_cerfs_7" + ".jpg";
                    model.text9 = "The island is open to the public from 9:00 AM to 6:00 PM. You can catch a shuttle from the village of Trou d’Eau Douce, which departs every half hour from the Pointe Maurice pier to the Masala pier on Ile aux Cerfs, starting at 9:30 AM. Another option is to take a scenic catamaran ride, with many providers offering trips to the island that may also include a detour to the nearby waterfalls in the Grande Rivière Sud Est nature reserve.";
                    model.title8 = "Best Time to Visit Ile aux Cerfs:";
                    model.text10 = "";
                    model.image4 = fullPath + "/" + "ile_aux_cerfs_6" + ".jpg";
                    model.text11 = "Weekdays are the best time to visit if you want to avoid the larger crowds that flock to the island on weekends. October and November are considered the ideal months for a trip to Ile aux Cerfs, but the entire period from June to November provides pleasant weather for a visit." + "<br><br>" + "<b><span style=\"color:black; font-weight:bold;\">Accommodation:</span></b><br>" + "For those seeking luxury, the 5-star Shangri-La’s Le Touessrok Resort & Spa offers an opulent stay on Ile aux Cerfs, providing the perfect setting for a grand celebration or special event." + "<br><br>" + "<b><span style=\"color:black; font-weight:bold;\">Dining Options:</span></b>";
                    model.image5 = fullPath + "/" + "ile_aux_cerfs_3" + ".jpg";
                    model.text12 = "The island has several dining options, including a beachside restaurant where you can enjoy a meal with a view. There are also two bars offering refreshing drinks and cocktails at reasonable prices. If you prefer, you can bring your own food and drinks for a picnic on the beach.<br>";
                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                }
                break;
            case "West":

                if (holder.getAdapterPosition() == 0) {
                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "albion_lighthouse" + ".jpg";
                    model.text1 = "Situated atop a cliff with a commanding view of the ocean, the Cave Point lighthouse safely directs ships into Port Louis harbor. With its striking red and white stripes, this iconic lighthouse has been standing since 1910 and is well-maintained. Visitors must ascend a 98-step wooden and cast-iron staircase to reach the top of the lighthouse.\n" + "<br><br>" + "The sweeping views of the coastline, which stretches from Pointe aux Sables to Flic en Flac, make the climb worthwhile. If heights aren’t your thing, a walk along the trail to the lighthouse still offers breathtaking views of the shore. Taking a moment to pause and listen to the waves crash against the cliffs enhances the experience.\n" + "<br><br>" + "The area is popular with photographers, and beneath the cliffs, there's a cave home to birds and bats. Adventurous cliff divers often jump into the sea from the rocks, and you might even see fishermen at work. As the sun sets, the sky comes alive with vibrant colors, creating a beautiful scene.\n" + "<br><br>" + "It’s best to avoid visiting the lighthouse on windy days.<br>";
                    model.title1 = "";
                    model.image1 = "";
                    model.text2 = "";
                    model.title2 = "";
                    model.text3 = "";
                    model.image2 = "";
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
                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 1) {

                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "flic_en_flac_3" + ".jpg";

                    model.text1 = "After Grand Baie, Flic en Flac, on the island's west coast, has grown to be Mauritius's second-most popular tourist destination. Over the past two centuries, this area has transformed from a small fishing village into a vibrant town, attracting both international tourists and local Mauritians.\n";
                    model.title1 = "";
                    model.image1 = fullPath + "/" + "flic_en_flac_1" + ".jpg";
                    model.text2 = "Flic en Flac offers an ideal holiday experience with its stunning 8 km beach, lined with white sand and crystal-clear blue lagoons. Numerous restaurants, hotels, and shops cater to visitors, making it a perfect spot for watersports, afternoon strolls, sunbathing, or simply relaxing under the shade of <b>Casuarina Trees.</b> The lagoon, protected by a coral reef, provides safe swimming conditions and various watersport activities.\n" + "<br><br>" + "During the day, Flic en Flac is bustling with activity, offering local street food like <b>DhalPuri</b>, a delicious flatbread filled with curry, and <b>fried noodles with Mauritian meatballs.</b> On weekends, the beach comes alive with activity as locals congregate for picnics, singing, and dancing to the beat of Sega music, creating a joyous atmosphere. atmosphere. Thanks to its many restaurants and nightclubs, Flic en Flac is also a favorite nighttime destination for both locals and visitors.\n" + "<br><br>" + "Despite its beauty, it's important to note that the beach has corals and sea urchins, so caution is advised when walking barefoot.\n";
                    model.title2 = "Location and Accessibility\n";
                    model.text3 = "Flic en Flac is situated in the <b>Black River district,</b> approximately 15 km south of Port Louis. It lies between the villages of <b>Albion</b> to the north and <b>Tamarin</b> and <b>Black River</b> to the south. The town is easily accessible by a well-established bus route connecting it to major cities like Port Louis, <b>Quatre Bornes,</b> and <b>Curepipe,</b> making transportation convenient for visitors.\n";
                    model.image2 = "";
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

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 2) {
                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "casela" + ".jpg";

                    model.text1 = "Casela World of Adventures, the most visited attraction in Mauritius, is nestled amidst sugarcane fields on the island’s western coast, with the stunning <b>Rempart Mountain</b> serving as its backdrop. Originally founded as a bird sanctuary in 1979, Casela has grown into a sprawling nature park offering a wide range of thrilling activities and unforgettable animal encounters.\n";
                    model.title1 = "";
                    model.image1 = fullPath + "/" + "botanical_garden_5" + ".jpg";
                    model.text2 = "";
                    model.title2 = "Exciting Activities at Casela\n";
                    model.text3 = "Casela provides something for everyone, from adrenaline-pumping adventures like ziplining, quad biking, and the exhilarating <b>Canyon Swing,</b> to more family-friendly options like the <b>African safari,</b> where visitors can see big cats, giraffes, rhinos, and zebras. There are four themed worlds to choose from, so you can customize your visit:\n" + "<br><br><ul>" + "<li><b>Mountain Kingdom:</b> Features activities like the Zig Zag Racer, Canyon Swing, and Zip and Splash Tour.\n</li>" + "<li><b>Big Cats Kingdom:</b> Offers unique experiences such as walking with lions and interacting with cheetahs and caracals.\n</li>" + "<li><b>Safari Kingdom:</b> Includes quad biking, giraffe feeding, Segway tours, and e-bike safaris.\n</li>" + "<li><b>Middle Kingdom:</b> Provides opportunities to interact with and feed tortoises.\n</li>" + "</ul>" + "<b><span style=\"color:black; font-weight:bold;\">Action-Packed Mountain Adventures</span></b>";
                    model.image2 = fullPath + "/" + "casela_6" + ".jpg";
                    model.text4 = "<ul>" + "<li><b>Ziplining:</b> Soar above the park on zip lines, taking in breathtaking aerial views.\n</li>" + "<li><b>Canyon Swing:</b> Experience the thrill of freefalling from a 45-meter platform.\n</li>" + "<li><b>Mountain Climbing:</b> Challenge yourself on the Via Ferrata Canyon Tour through sugarcane fields and scenic plateaus.\n</li>" + "</ul>" + "<b><span style=\"color:black; font-weight:bold;\">Safari and Animal Encounters</span></b>";
                    model.title3 = "";
                    model.text5 = "";
                    model.title4 = "";
                    model.text6 = "";
                    model.title5 = "";
                    model.text7 = "";
                    model.title6 = "";
                    model.text8 = "";
                    model.title7 = "";
                    model.image3 = fullPath + "/" + "casela_4" + ".jpg";
                    model.text9 = "<ul><li><b>Walk with Lions:</b> Stroll alongside lions with expert guides ensuring your safety.\n</li>" + "<li><b>Big Cat Interactions:</b> Get up close with lions, cheetahs, and caracals.\n</li>" + "<li><b>Drive-Thru Safari:</b> Take a 45-minute drive to observe lions in their natural habitat.\n</li>" + "<li><b>E-Bike Safari & Segway Tour:</b> Explore the Yemen Nature Reserve on eco-friendly bikes or Segways.\n</li>" + "<li><b>Camel Riding:</b> Enjoy a camel ride through the scenic park.\n</li>" + "</ul>\n" + "<b><span style=\"color:black; font-weight:bold;\">Family Activities\n</span></b>";
                    model.title8 = "";
                    model.text10 = "";
                    model.image4 = fullPath + "/" + "casela_3" + ".jpg";
                    model.text11 = "<ul><li><b>Zookeeper for a Day:</b> Kids can experience life as a zookeeper, participating in various activities.\n</li>" + "<li><b>Petting Farm & Pony Ride:</b> Ideal for children to interact with farm animals.\n</li>" + "<li><b>Giraffe, Ostrich, & Tortoise Feeding:</b> Engage in up-close encounters with these majestic animals.\n</li>" + "</ul>" + "<b><span style=\"color:black; font-weight:bold;\">Dining and Shopping</span></b>" + "<br><br>" + "Visitors can unwind at the <b>Casela Restaurant,</b> which overlooks <b>Tamarin Bay</b> and offers a variety of cuisines, including Mauritian, European, and Asian dishes. The park also features multiple food outlets to satisfy diverse tastes. Before leaving, stop by the <b>gift shops</b> to pick up souvenirs, from African crafts to locally made jewelry and textiles.\n" + "<br><br>" + "<b><span style=\"color:black; font-weight:bold;\">Conservation and History\n</span></b>" + "<br><br>" + "Since its inception, Casela has remained committed to conservation efforts. In February 2015, the park gained international recognition when <b>Her Royal Highness Princess Stephanie of Monaco</b> became its patron, further highlighting the park’s dedication to protecting endangered species. Today, Casela continues to expand its offerings while staying true to its mission of conservation, providing a memorable experience for visitors of all ages.\n";
                    model.image5 = "";
                    model.text12 = "";

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 3) {

                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "tamarin_3" + ".jpg";

                    model.text1 = "Tamarin Bay Beach is a gorgeous beach location on Mauritius' west coast, situated just past the <b>Black River</b> village. Known for its natural beauty and ideal surfing conditions, it attracts both locals and tourists.\n";
                    model.title1 = "Surfing History and Recognition";
                    model.image1 = fullPath + "/" + "tamarin_2" + ".jpg";
                    model.text2 = "This beach, formerly known as <b>Santosha Bay,</b> was kept a secret by surfers who considered it a hidden treasure. The name <b>Santosha</b> can still be faintly seen painted on a few buildings. Tamarin Bay gained international fame with the release of the 1974 surf documentary <b>\"Forgotten Island of Santosha\"</b> by Larry and Roger Yates, which brought global attention to its remarkable surf breaks. The bay features two renowned surfing spots: <b>‘Dal’</b> on the southern side and <b>‘Black Stone’</b> to the north.";
                    model.title2 = "Dolphins and Wildlife\n";
                    model.text3 = "Tamarin Bay is a haven for dolphins, especially <b>Spinner</b> and <b>Bottlenose dolphins,</b> which are frequently spotted in the early morning hours before heading out to the open sea. Many boat companies offer morning trips for tourists to observe and swim with these playful creatures, making it a top destination for wildlife enthusiasts as well.\n" + "<br><br>" + "<b><span style=\"color:black; font-weight:bold;\">Surfing Hub\n</span></b>" + "<br><br>" + "Since the 1970s, Tamarin Bay has been a major surfing hub, introduced primarily by Australians living on the island’s west coast. The area’s strong waves and ideal conditions continue to draw surf enthusiasts from around the world, and surfing here is often considered a special privilege.\n" + "<br><br>" + "<b><span style=\"color:black; font-weight:bold;\">Authentic and Laid-Back Atmosphere\n</span></b>" + "<br><br>" + "Despite its fame as a surfing hotspot, Tamarin Bay retains its authentic charm. Local families frequent the beach for leisurely strolls or relaxed afternoons, creating a welcoming and laid-back environment. Its vibrant yet genuine atmosphere makes it appealing to both tourists and locals.\n" + "<br><br>" + "<b><span style=\"color:black; font-weight:bold;\">Swimming Caution\n</span></b>" + "<br><br>" + "While Tamarin Bay is excellent for surfing, swimming is not recommended for children or inexperienced swimmers. The strong currents and large waves can be unpredictable, making it less safe for casual swimming.\n" + "<br><br>" + "<b><span style=\"color:black; font-weight:bold;\">Lively Beach Culture\n</span></b>" + "<br><br>" + "Tamarin Bay can become quite busy, especially on weekends and holidays, offering a lively and energetic beach scene. Whether you're enjoying the surf, relaxing by the water, or taking in the local culture, the beach offers something for everyone.\n" + "<br><br>" + "<b><span style=\"color:black; font-weight:bold;\">Best Times to Visit\n</span></b>" + "<br><br>" + "The best time to visit Tamarin Bay is either early in the morning, between 8:00 and 11:00, or in the afternoon, from 13:00 to 16:00. These times offer the most favorable conditions for beach activities, with fewer crowds and enjoyable weather.\n" + "\n<br><br>" + "<b><span style=\"color:black; font-weight:bold;\">Activities at Tamarin Bay\n</span></b>";
                    model.image2 = fullPath + "/" + "tamarin_1" + ".jpg";
                    model.text4 = "In addition to surfing, Tamarin Bay offers a variety of water activities, including:\n" + "<ul>" + "<li>Stand-up paddleboarding\n</li>" + "<li>Bodyboarding\n</li>" + "<li>Catamaran tours\n</li>" + "<li>Swimming with dolphins\n</li>" + "<li>Speed boat trips\n</li>" + "<li>Kayaking\n</li>" + "</ul>" + "Tamarin Bay Beach is a dynamic coastal destination that combines the thrill of surfing with the beauty of nature. Its authentic atmosphere, paired with a range of water activities and wildlife experiences, makes it an ideal spot for anyone looking to enjoy Mauritius’s vibrant beach culture.\n";
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

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 4) {

                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "la_preneuse_4" + ".jpg";

                    model.text1 = "<b>La Preneuse Beach,</b> located on the west coast of Mauritius, offers a serene setting with pristine, white sand that slopes gently into the shallow turquoise waters of the Indian Ocean. Though the sea is mostly calm, occasional strong currents can occur. The beach is spacious, providing ample room for vacationers, and offers breathtaking panoramic views, including the striking sight of <b>Mount Le Morne</b> set against the lagoon.\n" + "<br><br>" + "This beach destination is ideal for those seeking relaxation, as it is surrounded by various hotels catering to different comfort levels. Nearby, you'll find cafes, shops, and souvenir stores offering local goods. One of the notable landmarks here is the <b>Martello Tower,</b> a structure built by the British in the 1830s as a defense against potential French attacks. It now serves as a museum that provides insight into the region's history. Additionally, the <b>Black River</b> flows along the beach, and yachts and boats dot the horizon, offering sea excursions for tourists.";
                    model.title1 = "";
                    model.image1 = "";
                    model.text2 = "";
                    model.title2 = "";
                    model.text3 = "";
                    model.image2 = "";
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

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 5) {

                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "martello_tower_4" + ".jpg";

                    model.text1 = "Enhance your visit to <b>La Preneuse Beach</b> by exploring the <b>Martello Tower Heritage Museum.</b> The tower itself is a relic of Mauritius’s colonial history and offers a fascinating look into the British defense strategies during the 19th century.\n";
                    model.title1 = "Origins of the Martello Towers\n";
                    model.image1 = fullPath + "/" + "martello_tower_3" + ".jpg";
                    model.text2 = "The <b>Martello Towers</b> were inspired by a British encounter with a French-held tower in <b>Mortella Bay,</b> Corsica, during a battle in 1794. Impressed by the tower’s defensive strength, the British began building similar fortifications around their empire. These towers, typically conical with thick walls, can still be found in former colonies and along England’s shores.";
                    model.title2 = "Martello Tower in Mauritius\n";
                    model.text3 = "";
                    model.image2 = fullPath + "/" + "matello_tower_1" + ".jpg";
                    model.text4 = "The Martello Tower at La Preneuse was one of five buildings erected along Mauritius's west coast by the British in the early 1800s as a defense against possible French attacks. The towers were built primarily from <b>basalt rock,</b> using labor that included royal engineers, skilled Indian stonecutters, carpenters, and enslaved workers. Though the towers were outfitted with cannons capable of 360-degree rotation, they were never used in combat.\n";
                    model.title3 = "The Tower Museum\n";
                    model.text5 = "";
                    model.title4 = "";
                    model.text6 = "";
                    model.title5 = "";
                    model.text7 = "";
                    model.title6 = "";
                    model.text8 = "";
                    model.title7 = "";
                    model.image3 = fullPath + "/" + "martello_tower_2" + ".jpg";
                    model.text9 = "In 1992, the <b>Friends of the Environment</b> initiated the restoration of the La Preneuse Tower, transforming it into a heritage museum with government and institutional support. The museum opened officially in 2000, marking the tower as part of Mauritius’s <b>National Heritage Site.</b>";
                    model.title8 = "Inside the Museum\n";
                    model.text10 = "Visitors to the <b>Martello Tower Museum</b> can explore its different floors, which include:\n" + "<br><br><ul>" + "<li><b>Basement:</b> Used for rainwater storage.</li>" + "<li><b>Ground Floor:</b> Functioned as a storeroom and gunpowder armory.</li>" + "<li><b>First Floor:</b> Reserved for the Chief Officer and soldier accommodations.</li>" + "<li><b>Flat Roof:</b> Where cannons were installed for defense.</li>" + "</ul>" + "The museum features a collection of historical memorabilia, including <b>muskets,</b> the officer’s uniform, and personal belongings like cooking utensils and a fireplace, offering a glimpse into life during the era.\n" + "<br><br>" + "<b><span style=\"color:black; font-weight:bold;\">Opening Hours and Tours\n</span></b>" + "<br><br><ul>" + "<li><b>Tuesday to Saturday:</b> 09h30 to 17h00\n</li>" + "<li><b>Sunday:</b> 09h30 to 13h00\n</li>" + "<li><b>Closed:</b> Mondays, 1 May, 25 December, 1 & 2 January, 1 February, and 12 March.\n</li>" + "</ul>" + "Guided tours are available every half hour with no advance booking required. Visitors can simply arrive and pay the entrance fee, and a guide will lead them through the museum.\n" + "<br><br>" + "The <b>Martello Tower Heritage Museum</b> offers an immersive excursion into Mauritius's colonial past, making it an essential stop for history enthusiasts.\n";
                    model.image4 = "";
                    model.text11 = "";
                    model.image5 = "";
                    model.text12 = "";

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 6) {

                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "le_morne_1" + ".jpg";

                    model.text1 = "<b>Le Morne Brabant,</b> a UNESCO World Heritage Site since 2008, is a mountain located on the southwestern tip of Mauritius, jutting into the Indian Ocean. This culturally significant site served as a refuge for runaway slaves, known as <b>maroons,</b> from the 18th century until the early 1900s.";
                    model.title1 = "The Mountain's Natural Defense\n";
                    model.image1 = "";
                    model.text2 = "The mountain, with its vertical cliffs, steep slopes, and deep ravines, provided a natural fortress for the maroons seeking refuge from their pursuers. At its summit, a relatively flat plateau offered sanctuary to those who successfully navigated the dangerous ascent. The <b>V-Gap,</b> a wide gorge, served as the crucial entry point to the plateau, making access difficult and treacherous for both the maroons and those who sought to capture them.\n" + "<br><br>" + "Archaeological findings in the caves on Le Morne revealed ashy deposits from fires and the remains of a 300-year-old sheep, further confirming that maroons inhabited the summit, utilizing the resources they could secure to survive.\n";
                    model.title2 = "Unique Flora and Ecosystem\n";
                    model.text3 = "";
                    model.image2 = fullPath + "/" + "brabant_6" + ".jpg";
                    model.text4 = "Le Morne Brabant's ecosystem is fragile yet diverse, home to numerous endemic plant species. Of the 311 species of flowering plants in Mauritius, 73 are found exclusively on this mountain. One notable species is <b>L’Immortelle Du Morne</b> (Helichrysum Mauritianum), which thrives only on Le Morne.\n";
                    model.title3 = "The International Slave Route Monument\n";
                    model.text5 = "";
                    model.title4 = "";
                    model.text6 = "";
                    model.title5 = "";
                    model.text7 = "";
                    model.title6 = "";
                    model.text8 = "";
                    model.title7 = "";
                    model.image3 = fullPath + "/" + "brabant_8" + ".jpg";
                    model.text9 = "The International Slave Route Monument, a potent representation of the effects of slavery around the world, is located at the base of the mountain. The monument, featuring a central structure surrounded by nine smaller stone sculptures, represents the origins and destinations of enslaved people.\n";
                    model.title8 = "";
                    model.text10 = "";
                    model.image4 = fullPath + "/" + "brabant_4" + ".jpg";
                    model.text11 = "A striking sculpture by Haitian artist <b>Fritz Laratte</b> embodies the theme of liberation from slavery, depicting a slave whose hands are freed from chains through prayer, symbolizing the resilience and hope of the maroons.\n" + "<br><br>" + "<b><span style=\"color:black; font-weight:bold;\">Historical and Cultural Elements of Le Morne\n</span></b>";
                    model.image5 = fullPath + "/" + "brabant_7" + ".jpg";
                    model.text12 = "The cultural landscape of Le Morne extends beyond the mountain itself. <b>Trou-Chenille village,</b> with its rich history and heritage, includes five traditional huts preserved as part of an open-air museum. These huts portray daily life in the region during the 19th and 20th centuries, showcasing the simple yet meaningful existence of the local people.\n" + "<br><br>" + "<b><span style=\"color:black; font-weight:bold;\">Archaeological Discoveries</span></b>";

                    model.image6 = fullPath + "/" + "brabant_10" + ".jpg";
                    model.text13 = "Archaeological evidence has unearthed the remnants of a 19th-20th century settlement called <b>Macaque,</b> likely linked to Malagasy and Mozambican families. An abandoned cemetery, discovered at the foot of Le Morne, is believed to have been used by individuals from these regions. These findings provide insight into the multi-ethnic composition of the maroons and their descendants.\n" + "<br><br>" + "<b><span style=\"color:black; font-weight:bold;\">Cultural Traditions and Community\n</span></b>" + "<ul>" + "\t<li><b>Stella Maris Chapel:</b> The first Catholic chapel in Le Morne, originally built in <b>1891,</b> was moved to <b>L’Embrasure</b> during World War I and rebuilt in <b>1987</b> after being destroyed by a cyclone.\n" + "\t<li><b>Sega Nights:</b> A weekly gathering where locals shared stories and performed Sega music beneath the ancient <b>Sega Tree</b> (Banyan Tree), using traditional instruments.\n" + "\t<li><b>Fishing:</b> Fishing has been a key part of local culture since the 18th century. Villagers employ traditional methods like <b>Seine fishing</b> and <b>Kazie</b> (basket trap) fishing, which continue to be passed down through generations.\n" + "</ul>" + "<b><span style=\"color:black; font-weight:bold;\">Historical Landmarks and Daily Life</span></b>";
                    model.image7 = fullPath + "/" + "brabant_11" + ".jpg";
                    model.text14 = "<ul>" + "\t<li><b>Limekiln:</b> Constructed by the Cambier family, this <b>20th-century limekiln</b> involved villagers in the production of quick lime by burning corals and shells.\n</li>" + "\t<li><b>Grilled Coffee:</b> The distinct aroma of grilled coffee is a source of local pride. Beans from <b>Chamarel</b> are roasted in a cast iron pot over a fire and then ground using a mortar and pestle.\n</li>" + "\t<li><b>Ilot Fourneau:</b> Villagers would travel by boat to this nearby island to collect fresh water from a spring. Historical records show that <b>Ilot Fourneau</b> was used as a British military post during the 18th and 19th centuries.\n</li>" + "</ul>" + "Le Morne Brabant stands as a deeply symbolic and culturally rich landscape, representing the history of resistance, survival, and liberation. It remains a sacred site for many, particularly the <b>Rastafarian community,</b> who view the mountain as a place of spiritual connection and meditation.\n";
                    model.image8 = "";
                    model.text15 = "";

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 7) {

                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "maconde_1" + ".jpg";

                    model.text1 = "Maconde Viewpoint is perched along the southern coast of Baie du Cap, a quiet village known for its rugged beauty and untouched coastlines. The viewpoint sits atop a small rocky cliff on a curved section of the coastal road. From here, you'll see an incredible landscape with rich red earth, lush green forests, rows of palm trees, and the sparkling blue waters of the Indian Ocean. It's a view that truly captures the heart.\n";
                    model.title1 = "";
                    model.image1 = fullPath + "/" + "maconde_2" + ".jpg";
                    model.text2 = "There's an interesting story behind the name \"Maconde.\" Some say it dates back to the days of slavery when runaway slaves from Mozambique’s Makonde tribe found safety in this area. Others believe it's named after Governor Jean Baptiste Henri Conde, who built an outlook on the cliff.\n" + "<br><br>" + "Getting to this area wasn’t always easy. The first road was only built in the 1920s, and the rough terrain and low-lying coast made construction tough. Recent updates have improved safety, but the drive along the winding basalt cliffs, with the sound of waves crashing against the rocks, is still as mesmerizing as ever. It’s a favorite spot for people who love watching the powerful ocean swells.\n" + "<br><br>" + "To reach the viewpoint itself, you'll need to climb a narrow set of stairs. At the top, you’ll be rewarded with stunning views of the ocean, the coastal village nearby, and the sight of local fishermen along the shore.\n";
                    model.title2 = "";
                    model.text3 = "";
                    model.image2 = "";
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

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 8) {

                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "chamarel_2" + ".jpg";

                    model.text1 = "The Seven Coloured Earths are a must-see geological wonder located in the Chamarel plain of the Rivière Noire District in southwest Mauritius. This natural phenomenon features small sand dunes that show off seven different colors—red, brown, violet, green, blue, purple, and yellow. What makes this spot so unique is the way the colors naturally settle in separate layers, forming striped patterns across the hills. Over time, the rain has carved these colorful dunes into fascinating shapes that resemble the texture of meringue.\n" + "<br><br>" + "These sands come from volcanic rock that broke down into clay, eventually turning into soil through a process called hydrolysis. The red and anthracite shades come from iron, while aluminum gives the sands their blue and purplish tones. Scientists believe the colors are linked to how the volcanic rock cooled at different rates, but they still don’t fully understand how the layers form so consistently.\n" + "<br><br>" + "The name \"Seven Coloured Earths\" isn’t the official title, but a simple description of what you'll see. Sometimes it’s called \"Chamarel Seven Coloured Earths\" or \"Terres des Sept Couleurs\" in French, but whatever the name, the sight is unforgettable.\n" + "<br><br>" + "This fascinating phenomenon can even be recreated on a small scale. If you mix the differently colored sands together, they'll separate over time, forming layers just like in nature.\n" + "<br><br>" + "Since the 1960s, the Seven Coloured Earths have become a top tourist attraction in Mauritius. Today, the dunes are protected by a wooden fence, and visitors can no longer walk on them. Instead, you can take in the breathtaking view from observation posts along the fence. And if you want a little souvenir, local shops sell little test tubes filled with colored sand.\n";
                    model.title1 = "";
                    model.image1 = "";
                    model.text2 = "";
                    model.title2 = "";
                    model.text3 = "";
                    model.image2 = "";
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

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 9) {

                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "chamarel_1" + ".jpg";

                    model.text1 = "Chamarel Waterfall, the tallest single-drop waterfall in Mauritius, stands at about 100 meters high and is a sight to behold. Surrounded by lush greenery, this natural wonder is powered by three streams that merge and flow into the Saint Denis River, creating a powerful cascade. At its peak, the waterfall flows at over 40,000 cubic meters per minute.\n" + "<br><br>" + "As you drive along the 3-kilometer road through the Seven-Coloured Earth Geopark, you'll pass the viewpoint for Chamarel Waterfall. You can reach the ideal location to witness this magnificent waterfall by taking a brief hike through dense forest.\n" + "<br><br>" + "The waterfall pours over a basalt cliff into an oval pool below, then winds through a 6-kilometer canyon lined with tropical forests before eventually reaching Baie du Cap. It's a living reminder of Mauritius' volcanic past, with the landscape shaped by lava flows from two different periods. The bottom layer of basalt is 10 to 8 million years old, while the top layer dates from 3.5 to 1.7 million years ago.\n" + "<br><br>" + "For the adventurous, there's a three-hour trek through the hidden valley leading to the base of the waterfall. Once there, you can take a dip in the cool waters and feel the refreshing spray of the waterfall above.\n" + "<br><br>" + "The combination of the sights and sounds creates a true tropical rainforest experience, leaving you with memories of this amazing natural beauty.\n";
                    model.title1 = "";
                    model.image1 = "";
                    model.text2 = "";
                    model.title2 = "";
                    model.text3 = "";
                    model.image2 = "";
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

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 10) {
                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "black_river_georges_2" + ".jpg";

                    model.text1 = "Black River Gorges National Park is located in the hilly southwestern part of Mauritius. Established on June 15, 1994, and managed by the National Parks and Conservation Service, the park covers an area of 67.54 square kilometers. It features different habitats like humid upland forests, drier lowland forests, and marshy heathlands. Visitors can explore two information centers, picnic areas, and 60 kilometers of hiking trails. There are also four research stations focused on conservation efforts, run by the National Parks and Conservation Service along with the Mauritian Wildlife Foundation.\n" + "<br><br>" + "The park was created to protect what remains of the island's rainforest. Over time, non-native plants like Chinese guava and privet, along with animals such as the rusa deer and wild pigs, have damaged parts of the forest. To protect native species, some areas of the park have been fenced off, and efforts are being made to control the invasive species.\n";
                    model.title1 = "";
                    model.image1 = fullPath + "/" + "fody" + ".jpg";
                    model.text2 = "Despite these challenges, the park is home to many unique plants and animals. Among them are the Mauritian flying fox and all of the island's endemic bird species, including the Mauritius kestrel, pink pigeon, Mauritius parakeet, and Mauritius cuckooshrike. Black River Gorges National Park is recognized as an Important Bird Area by BirdLife International, playing a key role in preserving Mauritius' special wildlife.\n";
                    model.title2 = "";
                    model.text3 = "";
                    model.image2 = "";
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
                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 11) {

                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "le_morne_beach_2" + ".jpg";

                    model.text1 = "Le Morne Beach, located on a peninsula along the western coast of Mauritius, sits at the foot of the UNESCO-listed Le Morne Brabant mountain. With easy access from the coastal road, this public beach stretches for miles with soft white sand and shady casuarina trees. The crystal-clear water provides excellent visibility, making it a perfect spot for scuba diving.\n";
                    model.title1 = "";
                    model.image1 = fullPath + "/" + "kite_surfing" + ".jpg";
                    model.text2 = "Le Morne Beach, renowned for being one of the best places in the world to kite surf, has an amazing view of the famous mountain in the distance. The beach is also a favorite among windsurfers and kite surfers due to the ideal conditions that last throughout much of the year. Le Morne Beach hosts several competitions, including the Kiteival, which draws participants from around the globe.\n" + "<br><br>" + "Though great for swimming, Le Morne Beach can get quite crowded on weekends and holidays. Nudism is not allowed, but topless sunbathing is accepted. For a quieter visit, it's best to go early in the morning from 8:00 to 11:00 or in the afternoon between 1:00 and 5:00.\n";
                    model.title2 = "";
                    model.text3 = "";
                    model.image2 = fullPath + "/" + "paddle_boarding" + ".jpg";
                    model.text4 = "There’s no shortage of activities for beachgoers here, including pedalo rides, windsurfing, kite surfing, scuba diving, stand-up paddleboarding, and kayaking. At Le Morne Beach, there's something for everyone to enjoy.\n";
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

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 12) {

                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "rhumerie_de_chamarel_1" + ".jpg";

                    model.text1 = "Set in the fertile valley of southwestern Mauritius, the Rhumerie de Chamarel is a must-visit destination. Surrounded by lush sugarcane fields that blend with pineapple groves and tropical fruit, this rum distillery offers a warm and authentic experience.\n" + "<br><br>" + "Visitors to Rhumerie de Chamarel can take a thorough tour of the distillery, taste some of the best rums, and dine at the distillery's own restaurant, L'Alchimiste. The design of the distillery emphasizes its connection with nature, using natural materials like wood, stone, and water to create a peaceful atmosphere that reflects the beauty of the surrounding landscape.\n" + "<br><br>" + "<b><span style=\"color:black; font-weight:bold;\">Guided Tour of Rhumerie de Chamarel\n</span></b>" + "<br><br>" + "The guided tour at Rhumerie de Chamarel takes you behind the scenes of the rum-making process, led by knowledgeable guides. You’ll learn about how the sugarcane is carefully selected and cultivated on the property. The distillery uses special fermentation techniques to enhance the flavors and aromas of the rum. The tour lasts about 30 to 40 minutes and is offered in both English and French.\n" + "<br><br>" + "<b><span style=\"color:black; font-weight:bold;\">Rum Tasting\n</span></b>";
                    model.title1 = "";
                    model.image1 = fullPath + "/" + "rhumerie_de_chamarel_3" + ".jpg";
                    model.text2 = "The rum tasting is, of course, the highlight of the trip. At the end of the tour, you’ll get the chance to try a variety of agricultural rums made from pure cane juice, rather than molasses. This unique process gives the rum its distinctive flavors. The tasting features White Rum, Coeur de Chauffe, Chamarel Liquors, Exotic-Flavored Rums, and the distillery’s Old Rum.\n";
                    model.title2 = "About the Rum at Rhumerie de Chamarel\n";
                    model.text3 = "";
                    model.image2 = fullPath + "/" + "rhumerie_2" + ".jpg";
                    model.text4 = "Rhumerie de Chamarel focuses on producing high-quality, eco-friendly agricultural rum. From the cultivation of the cane to the fermentation and distillation process, everything is done with care. Unlike traditional rum made from molasses, their rum is distilled from fresh, fermented cane juice, creating a unique taste.\n" + "<br><br>" + "The distillery is also committed to sustainability. They recycle bagasse (the fibrous material left after the cane is processed) to generate energy, purify fumes from the distillation process, use ashes as fertilizer, and even reuse steam for watering the gardens.\n" + "<br><br>" + "<b><span style=\"color:black; font-weight:bold;\">Additional Experiences at Rhumerie de Chamarel\n</span></b>" + "<br><br><ul>" + "<li><b>The Sugar Cane Spirit Shop:</b> Here, you can buy local products like the distillery's rum, Mauritian crafts, jewelry, and even an exclusive clothing line designed by Rhumerie de Chamarel.\n</li>" + "<li><b>Restaurant L’Alchimiste:</b> Enjoy dishes made from local ingredients in a beautiful setting surrounded by tropical plants, overlooking the sugarcane fields and mountains. Finer wines from France and abroad complement the menu's unusual dishes, which include palm hearts and meat from deer or wild pigs.\n</li>" + "</ul>" + "<b><span style=\"color:black; font-weight:bold;\">Visitor Information\n</span></b>" + "<br><br><ul>" + "<li>The distillery is open Monday through Saturday, from 9:30 a.m. to 5:30 p.m.\n</li>" + "<li>The guided tour and rum tasting last about 30 to 40 minutes.\n</li>" + "<li>Children under 12 must be accompanied by their parents, but they are not allowed to participate in the rum tasting.\n</li>" + "</ul>";
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

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 13) {
                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "curious_corner_2" + ".jpg";

                    model.text1 = "Curious Corner, the only science museum in Chamarel, is located in the southwestern part of Mauritius. It’s a place for curious minds of all ages, offering a fun and challenging environment filled with puzzles and brain teasers. For kids, it’s a great spot to spark curiosity, boost their thinking skills, and encourage learning. But it’s not just for children—couples, families, and parents also enjoy the interactive exhibits, which make it a perfect place for socializing and sharing fun experiences.\n" + "<br><br>" + "Curious Corner has become a favorite for hosting birthdays and exchanging thoughtful gifts. Families love coming here for the unique and exciting atmosphere, making memories that last long after the visit. While most tourists come to Mauritius for the beaches and blue lagoons, Curious Corner offers something completely different—a fun, quirky adventure.\n";
                    model.title1 = "";
                    model.image1 = fullPath + "/" + "curious_corner_4" + ".jpg";
                    model.text2 = "Sitting right across from the entrance to Chamarel's Seven-Coloured Earth, Curious Corner was designed with one goal: to surprise and delight visitors. Covering 5,000 square meters, it’s packed with optical illusions and over 40 exhibits to explore. A typical visit lasts around an hour and a half, giving you plenty of time to enjoy everything it has to offer.\n";
                    model.title2 = "Activities at Curious Corner\n";
                    model.text3 = "<ul>" + "<li><b>Upside-Down Room:</b> Step into a room where everything is flipped, and it feels like you're standing upside down, with the ground above and the sky below. It’s perfect for snapping some one-of-a-kind photos.\n</li>" + "<li><b>Staircase Music Room:</b> As you walk up the staircase, each step produces a musical note, making it a fun, interactive way to move through the space.\n</li>" + "<li><b>Light-Tapping Room:</b> Tap the lights to earn points in this playful room that’s sure to bring lots of laughs.\n</li>" + "<li><b>Ames Room:</b> Named after Adelbert Ames, this room uses a distorted trapezoid shape to play tricks on your eyes, making people look bigger or smaller depending on where they stand.\n</li>" + "</ul>";
                    model.image2 = fullPath + "/" + "curious_corner_5" + ".jpg";
                    model.text4 = "<ul>" + "<li><b>Mirror Maze:</b> Find your way through a maze of 200 mirrors that create tricky illusions and test your ability to navigate.\n</li>" + "</ul>";
                    model.title3 = "";
                    model.text5 = "";
                    model.title4 = "";
                    model.text6 = "";
                    model.title5 = "";
                    model.text7 = "";
                    model.title6 = "";
                    model.text8 = "";
                    model.title7 = "";
                    model.image3 = fullPath + "/" + "curious_corner_3" + ".jpg";
                    model.text9 = "<ul>" + "<li><b>Laser Music Room:</b> Show off your musical talent by playing tunes using laser beams—an experience that’s as fun as it sounds!\n</li>" + "<b>Corner Café:</b> Take a break in the café, which is set in a lovely garden, and grab a bite. The menu includes ice cream, pizzas, burgers, snacks, and smoothies.\n" + "<li><b>Gift Shop ('Puzzles and Things'):</b> Pick up a souvenir from the shop, where you’ll find items like local rum, Mauritian crafts, jewelry, and an exclusive clothing line.\n</li>" + "</ul>" + "<b><span style=\"color:black; font-size:60px;\">Extra Features</span></b>";
                    model.title8 = "";
                    model.text10 = "<ul>" + "<li><b>Special Guides:</b> The staff at Curious Corner are always ready to help and provide more information, making your visit even more enjoyable.\n</li>" + "<li><b>Group Visits:</b> Group discounts are available, so you can bring friends, family, or larger groups and have a fun day out together.\n</li>" + "</ul>" + "<b><span style=\"color:black; font-weight:bold;\">Operating Information\n</span></b>" + "<br><br>" + "<ul>" + "<li><b>Opening Hours:</b> Curious Corner is open daily from 9 AM to 5 PM.\n</li>" + "</ul>";
                    model.image4 = "";
                    model.text11 = "";
                    model.image5 = "";
                    model.text12 = "";

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                }
                break;
            case "North":
                if (holder.getAdapterPosition() == 0) {
                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "red_roof_church" + ".jpg";

                    model.text1 = "Located on the northern coast of Mauritius, Notre-Dame Auxiliatrice is a small church known for its bright red roof. It overlooks the lagoon in Cap-Malheureux and is a well-known symbol of the island. Visitors can enjoy stunning views of Coin de Mire, a small island just under ten kilometers off the northern coast of Mauritius.\n" + "<br><br>" + "The chapel has a simple yet beautiful design that captures the spirit of Mauritius. It is a popular stop on many of the tours offered by Mauritius Resorts. With its charming appearance, the chapel welcomes visitors year-round. The peaceful white walls inside create a calming atmosphere, while the impressive carved stone altar adds to its charm.\n";
                    model.title1 = "Notre Dame Auxiliatrice";
                    model.image1 = "";
                    model.text2 = "This renowned church, reminiscent of a scene painted on a postcard, beckons exploration. Visitors are captivated by its magnificent basalt altar and Holy Water font. The picturesque landscape, adorned with a majestic Banyan tree casting a cooling shade, further enhances its charm. It is one of the most frequently photographed spots on the island, drawing interest from ardent photographers and couples looking for a romantic backdrop for their wedding.\n";
                    model.title2 = "Embark on a Journey Through a Unique Setting";
                    model.text3 = "";
                    model.image2 = fullPath + "/" + "cap_malheureux_1" + ".jpg";
                    model.text4 = "Cap Malheureux, along with its iconic chapel, is best explored towards the end of the day. The sunset bathes the surroundings in an exceptional glow, reminiscent of the enchanting scenes from classic Hollywood romances. The natural beauty of the location is preserved and abundant, offering a chance to witness the renowned green ray, a phenomenon the island proudly boasts about. Optimal times to visit are between October and December, when the radiant flowers adorn the island, creating a captivating spectacle.\n";
                    model.title3 = "Coin De Mire";
                    model.text5 = "";
                    model.title4 = "";
                    model.text6 = "";
                    model.title5 = "";
                    model.text7 = "";
                    model.title6 = "";
                    model.text8 = "";
                    model.title7 = "";
                    model.image3 = fullPath + "/" + "cap_malheureux_2" + ".jpg";
                    model.text9 = "A visit to Cap Malheureux isn’t complete without exploring nearby attractions like the marine cemetery or the Tamil temple, both of which offer a glimpse into the rich cultural and historical significance of the area. The peacefulness of Cap Malheureux makes it feel like a sanctuary, far from the hustle and bustle of the more tourist-heavy spots on the island. It's a place where you can truly relax, letting the calm surroundings recharge your spirits.\n" + "<br><br>" + "The views from this coastal village are stunning, especially with the iconic Coin de Mire (Gunner’s Quoin) island in the background, creating a postcard-worthy scene. The contrast of the bright turquoise waters against the rugged island silhouette is unforgettable and perfect for photos. It’s a favorite spot for those who appreciate nature’s beauty and want to take in the serene coastal atmosphere.\n" + "<br><br>" + "Whether you’re wandering through the quiet village, admiring the famous red-roofed Notre Dame Auxiliatrice church, or simply enjoying the breathtaking ocean views, Cap Malheureux offers a chance to slow down and connect with the natural beauty of Mauritius.\n";
                    model.title8 = "A Mauritius Destination That You Must Visit";
                    model.text10 = "The enchanting chapel at Cap Malheureux is a highlight you shouldn't miss during your stay in Mauritius. Designed by Max Boullé and built by Raoul Lolliot in 1938, it was consecrated by Vicar General Bishop Richard Lee on August 7, 1938.";
                    model.image4 = fullPath + "/" + "red_church_inside" + ".jpg";
                    model.text11 = "Originally envisioned by Abbé Albert Glorieux, a Belgian missionary and parish priest of Saint-Michel of Grand Gaube, Notre-Dame Auxiliatrice was intended for wedding celebrations. Today, it continues to attract visitors for Sunday masses and welcomes guests throughout the day.\n";
                    model.image5 = "";
                    model.text12 = "<b><span style=\"color:black; font-weight:bold;\">Cap Malheureux</span></b>" + "<br><br>" + "Cap Malheureux is a village situated in the Rivière du Rempart District of Mauritius. The name <b>\"Cap Malheureux,\"</b> which means <b>\"Cape of Bad Luck,\"</b> was given by the French during their rule over the island from 1715 to 1810.\n" + "<br><br>" + "The island was a significant point of interest for many explorers, including the British. In 1810, the British decided to take control of the island to stop Mauritian corsairs from attacking their fleets, especially those led by the famous Surcouf.\n" + "<br><br>" + "British naval power had cut off the island's supply lines with France. After an unsuccessful attempt to invade Grand Port in the south—where the French defeated the Royal Navy during the Napoleonic Wars—the British surprised the French by attacking from the north, where their defenses were weaker.\n" + "<br><br>" + "As a result, the French were defeated, and the name \"Cap Malheureux\" was chosen to remember their loss to the British, who successfully took control of the island from that strategic location.\n";

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 1) {
                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "pamplemousse_garden" + ".jpg";

                    model.text1 = "The Sir Seewoosagur Ramgoolam Botanic Garden, also known as the Pamplemousses Botanic Garden, is a major tourist attraction located in Pamplemousses, near Port Louis, Mauritius. It is the oldest botanical garden in the Southern Hemisphere. Established in 1770 by Pierre Poivre, the garden spans about 37 hectares (91 acres) and is famous for its large pond filled with giant water lilies (Victoria amazonica).\n" + "<br><br>" + "Over the years, the garden has had several names that reflect its changing status and ownership. Some of these names include 'Jardin de Mon Plaisir,' 'Jardin des Plantes,' 'Le Jardin National de l’Ile de France,' 'Jardin Royal,' and 'Jardin Botanique des Pamplemousses.' On September 17, 1988, it was officially named the \"Sir Seewoosagur Ramgoolam Botanic Garden\" to honor the first prime minister of Mauritius.\n";
                    model.title1 = "";
                    model.image1 = fullPath + "/" + "botanical_garden_1" + ".jpg";
                    model.text2 = "In addition to the stunning giant water lilies, the garden is home to a variety of spices, ebonies, sugar canes, and 85 different types of palm trees from regions including Central America, Asia, Africa, and the Indian Ocean islands. Many notable people, such as Princess Margaret, Indira Gandhi, and François Mitterrand, have planted trees in the garden.\n" + "<br><br>" + "Located about seven miles northeast of Port Louis, the garden has a rich history that dates back to 1729 when it was set aside for colonist P. Barmont. It changed hands several times, with different owners contributing to its growth and development. Today, the garden covers approximately 25,110 hectares (62,040 acres), with part of the area serving as an experimental station.\n";
                    model.title2 = "The History of the Royal Botanic Gardens";
                    model.text3 = "";
                    model.image2 = fullPath + "/" + "botanical_garden_3" + ".jpg";
                    model.text4 = "The Royal Botanic Gardens began as an initiative by Mahé de La Bourdonnais, the first French Governor of Mauritius, in 1735. Originally set up as a vegetable garden for his home and the growing town of Port Louis, it transformed into a botanical paradise. Under Pierre Poivre's leadership in 1768, the garden became a center for acclimatizing plants from other countries and growing important crops like cassava.";
                    model.title3 = "";
                    model.text5 = "";
                    model.title4 = "";
                    model.text6 = "";
                    model.title5 = "";
                    model.text7 = "";
                    model.title6 = "";
                    model.text8 = "";
                    model.title7 = "";
                    model.image3 = fullPath + "/" + "botanical_garden_2" + ".jpg";
                    model.text9 = "Over the years, the garden faced times of neglect and challenges, but directors like James Duncan worked hard to restore and improve it. In 1866, during a malaria outbreak, the garden played an important role by growing Eucalyptus trees to help fight the disease.\n" + "<br><br>" + "In 1913, the Department of Agriculture took over the garden, managing its growth and upkeep. Notably, after the death of Seewoosagur Ramgoolam in 1985, part of the garden was dedicated to a crematorium, marking the first time someone was cremated on its grounds.\n" + "<br><br>" + "Today, the Sir Seewoosagur Ramgoolam Botanic Garden is a symbol of Mauritius's botanical heritage, inviting visitors to explore its beautiful landscapes and rich history.\n";
                    model.title8 = "Chateau de Mon Plaisir";
                    model.text10 = "";
                    model.image4 = fullPath + "/" + "botanical_garden_4" + ".jpg";
                    model.text11 = "Until 1839, the Chateau de Mon Plaisir was a simple building with a flat roof and circular verandahs. The current single-story structure, built by the English in the mid-19th century, is now a National Monument, which means it is legally protected. Visitors can enjoy a lovely view of the Moka Range and the Peak of Pieter Both from the Chateau, making it a beautiful spot to take in the scenery.\n";
                    model.image5 = "";
                    model.text12 = "";

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


//                        Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
                } else if (holder.getAdapterPosition() == 2) {
                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "sugar_museum_pamplemousses" + ".jpg";

                    model.text1 = "Just a short distance from the Pamplemousse Botanical Garden, L’Aventure du Sucre Museum is located in a beautifully renovated sugar mill surrounded by picturesque bougainvillea, coconut trees, and lush greenery. The sugar industry has played a vital role in the island’s history since the Dutch introduced sugar cane to Mauritius.\n";
                    model.title1 = "";
                    model.image1 = fullPath + "/" + "laventure_du_sucre" + ".jpg";
                    model.text2 = "At L'Aventure du Sucre, you’ll journey through 250 years of sugar production, from the Dutch era to the French and English colonists, while learning how sugar shaped the island’s colonial past. Housed in a former operational sugar factory, the museum provides an immersive experience, allowing visitors to witness the intricate stages of the sugar-making process.\n";
                    model.title2 = "Tasting and Shopping\n";
                    model.text3 = "End your tour with a sweet treat by sampling different types of sugar and other products made during the island’s golden colonial period. The <b>Village Boutik</b> offers a selection of local products and souvenirs, while <b>Restaurant Le Fangourin</b> invites visitors to savor the flavors of Mauritian cuisine amidst the serene surroundings of the sugar estate.\n";
                    model.image2 = "";
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

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 3) {
                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "baie_de_larsenal_2" + ".jpg";

                    model.text1 = "Discover the <b>Balaclava Ruins,</b> which are located near <b>Turtle Bay</b> on the gorgeous <b>Maritim Resort and Spa</b> grounds. This site offers a unique glimpse into Mauritius’s rich heritage, with remnants of a fort perched gracefully over Riviere Citron on the scenic Northwest coast.\n";
                    model.title1 = "Historical Significance of Turtle Bay\n";
                    model.image1 = "";
                    model.text2 = "Turtle Bay, originally named <b>Ebony Bay</b> by the Dutch, was a crucial stop for ships traveling from Europe to the East in the 17th century. The abundance of ebony trees, which are perfect for ship repairs, led to its designation. Over time, the bay earned its current name due to the numerous turtles inhabiting the area.\n";
                    model.title2 = "The Balaclava Ruins\n";
                    model.text3 = "";
                    model.image2 = fullPath + "/" + "baie_de_larsenal" + ".jpg";
                    model.text4 = "In the 18th century, under <b>Governor Mahé de Labourdonnais</b>, the French administration moved from Mahebourg to Port Louis. Balaclava's iron was essential for building the new capital's harbor and for sustaining military activities in India. This area once housed an iron foundry, a naval arsenal, and a gunpowder factory. A tragic explosion in 1774 destroyed parts of the site, but some structures, including the flour mill and ironworks, remained intact.\n" + "<br><br>" + "Later in 1864, the estate was sold to Mr. Wiehe, who built the famous Mon Desir mansion and established a school. The estate became a popular retreat for wealthy Mauritians and also housed a modern rum distillery.\n";
                    model.title3 = "Balaclava Ruins Today\n";
                    model.text5 = "Today, the ruins are preserved within the <b>Maritim Hotel</b> estate, managed by a German hotel chain and a Mauritian family. The <b>Chateau Mon Desir</b> restaurant now stands on the original Mon Desir site, offering a panoramic view of the ruins and Turtle Bay. The site, now an open-air museum, is occasionally used for film and fashion shoots, so visitors are advised to contact Maritim Resort for permission to explore the area.\n";
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

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 4) {
                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "grand_baie_1" + ".jpg";

                    model.text1 = "Grand Bay, also known as Grand Baie, is a coastal village and a popular tourist spot located in the Rivière du Rempart district at the northern tip of Mauritius. In the 17th century, the Dutch called it De Bogt Zonder Eyndt, meaning \"Bay Without End,\" a name that reflects the beauty of the area. Today, Grand Bay is the most popular vacation destination in Mauritius.\n" + "<br><br>" + "The village is famous for its stunning emerald waters and lively atmosphere, both during the day and at night. Visitors can enjoy various water activities like safe swimming, sailing, windsurfing, and water skiing. Grand Bay is also the starting point for deep-sea fishing trips and boat tours to the nearby northern islands of Mauritius, such as Gunners' Quoin, Flat Island, Round Island, and Serpent Island.\n" + "<br><br>" + "In and around Grand Bay, you'll find a wide range of fashion and craft shops, hotels, and restaurants. The village blends contemporary shopping malls and stores carrying international brands with classic local businesses, some of which have been operating for close to fifty years. This makes it an excellent place to shop for clothing, jewelry, textiles, and souvenirs.\n";
                    model.title1 = "Nightlife";
                    model.image1 = "";
                    model.text2 = "Grand Bay is famous for its lively nightlife, featuring some of the best bars and nightclubs on the island. Popular spots include Banana Café, Zanzibar, Les Enfants Terribles, and the well-known Buddha Bar. Night owls can enjoy the vibrant atmosphere, with parties starting around midnight and lasting into the early morning.";
                    model.title2 = "Location";
                    model.text3 = "Grand Bay is located in the Rivière du Rempart district on the northwest side of Mauritius. It is about 25 kilometers north of Port Louis, the capital of Mauritius. The village sits between Pereybere to the east and Trou aux Biches to the west. Driving to Port Louis takes around 30 minutes while getting to the airport takes about 1 hour and 30 minutes.\n" + "<br><br>" + "Grand Bay has a good bus system, making it easy to travel to important places in Mauritius, including Port Louis, Triolet, Goodlands, and Grand Gaube.\n" + "<br><br>\n" + "<b><span style=\"color:black; font-weight:bold;\">Climate</span></b>" + "<br><br>" + "Grand Bay enjoys a pleasant climate throughout the year, featuring warm summers and mild winters. It is a wonderful place to travel any time of year because of its protected beaches and lagoons.\n" + "<br><br>\n" + "<b><span style=\"color:black; font-weight:bold;\">Activities</span></b>";
                    model.image2 = fullPath + "/" + "parasailing" + ".jpg";
                    model.text4 = "Visitors to Grand Bay can enjoy many activities beyond just sunbathing. You can go safe swimming, sailing, windsurfing, parasailing, and participate in various water sports. The area is perfect for learning about underwater marine life because of attractions like the Underwater Sea Walk, submarine excursions, and the Underwater Scooter. Merely a few kilometers from the coast, divers can discover several favorable diving spots amidst the coral reefs.\n";
                    model.title3 = "Hotels";
                    model.text5 = "The hospitality scene in Mauritius, especially in Grand Bay, has changed a lot over the years. Once a hidden gem for only a few travelers, Mauritius is now a popular vacation spot with many hotel options, guesthouses, private apartments, and villas. Grand Bay offers something for everyone, from luxury resorts to cozy rental rooms.\n" + "<br><br>" + "Some well-known hotels in Grand Bay include Veranda Grand Bay, Le Mauricia Hotel, 20 Sud, Ocean Villas, Royal Palm, Ventura Hotel, and Merville Beach Resort.\n";
                    model.title4 = "Grand Bay Beaches\n";
                    model.text6 = "";
                    model.title5 = "";
                    model.text7 = "";
                    model.title6 = "";
                    model.text8 = "";
                    model.title7 = "";
                    model.image3 = fullPath + "/" + "grand_baie_3" + ".jpg";
                    model.text9 = "La Cuvette Beach and Grand Bay Public Beach are two of Grand Bay's well-known beaches. La Cuvette, located near the Royal Palm Hotel, offers a cozy atmosphere with calm waters, perfect for swimming, and is free of rocks and corals. In contrast, the Grand Bay Public Beach is in the center of Grand Bay and provides stunning views, but the swimming area is limited due to the busy boat traffic in the bay.\n";
                    model.title8 = "";
                    model.text10 = "";
                    model.image4 = fullPath + "/" + "grand_baie_1" + ".jpg";
                    model.text11 = "The northern part of Mauritius, including Grand Bay, is known for its flat landscape, called the \"Northern Plains.\" This region is famous for its beautiful lagoons and white sandy beaches, showcasing a mix of vibrant colors and diverse scenery. The lagoons are filled with soft sand and crystal-clear water, creating a stunning view as they shift from green to blue, framed by coral reefs and the deep blue ocean. \n";
                    model.image5 = fullPath + "/" + "catamaran_cruise_west_coast_2" + ".jpg";
                    model.text12 = "For underwater exploration, visitors can take glass-bottom boat trips to see marine life or enjoy a unique experience in a two-person submarine, diving beneath the surface alongside tropical fish.\n";

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 5) {
                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "port_louis_3" + ".jpg";

                    model.text1 = "Le Caudan Waterfront is a vibrant commercial hub in Port Louis, Mauritius, featuring an array of amenities such as shops, banks, casinos, cinemas, restaurants, a marina, and the luxurious Le Labourdonnais hotel.\n";
                    model.title1 = "Historical Roots\n";
                    model.image1 = "";
                    model.text2 = "The name \"Le Caudan Waterfront\" honors Jean Dominique Michel de Caudan, who arrived in Mauritius in 1726 and established a saltpan near what is now the Robert Edward Hart Garden. This peninsula, formed around a fossil coral islet, has evolved over 250 years, once housing a powder magazine, observatories, and warehouses, with the sugar industry shaping much of its history until the Bulk Sugar Terminal opened in 1980.\n";
                    model.title2 = "Historically Significant Spots\n";
                    model.text3 = "Notable historical locations in Le Caudan include the Blue Penny Museum, which is situated in the Food Court, and the first meteorological observatory in the Indian Ocean, which was formerly a docks office. Different areas within the complex are named to reflect the island's colonial history:\n" + "<br><br><ul>" + "<li><b>Barkly Wharf:</b> Named after Sir Henry Barkly, Governor of Mauritius (1863-1870).\n</li>" + "<li><b>Le Pavillon Wing:</b> Linked to Pavillon Street from an old map of Port Louis.\n</li>" + "<li><b>Dias Pier:</b> Honors Diogo Dias, the navigator believed to have first charted the Mascarene Islands.\n</li>" + "</ul>" + "Le Caudan Waterfront combines modernity with rich historical significance, making it a must-visit destination.\n";
                    model.image2 = "";
                    model.text4 = "";
                    model.title3 = "Modern Day Attractions\n";
                    model.text5 = "";
                    model.title4 = "";
                    model.text6 = "";
                    model.title5 = "";
                    model.text7 = "";
                    model.title6 = "";
                    model.text8 = "";
                    model.title7 = "";
                    model.image3 = fullPath + "/" + "port_louis_13" + ".jpg";
                    model.text9 = "Le Caudan, known for being the first major shopping development in Mauritius, remains an iconic destination for both locals and tourists. The mall boasts the latest fashion trends, a vibrant Craft Market, specialty shops with unique local products, cinemas, a marina, a bookstore, restaurants, cafes, and even a museum housing two of the world's rarest stamps.";
                    model.title8 = "Modern Appeal\n";
                    model.text10 = "";
                    model.image4 = fullPath + "/" + "port_louis_6" + ".jpg";
                    model.text11 = "Adapting to contemporary trends, Le Caudan continues to thrive as a modern waterfront mall. Visitors can enjoy tax-free shopping, while witnessing bustling port activities as container and cruise ships come and go, enhancing the lively atmosphere. Whether you're seeking souvenirs or simply soaking in the vibrant surroundings, Le Caudan offers a memorable experience.\n";
                    model.image5 = "";
                    model.text12 = "";

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 6) {
                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "port_louis_4" + ".jpg";

                    model.text1 = "\n" + "The Port Louis Central Market, also known as the Bazaar of Port Louis, is the heart of the city’s shopping scene, buzzing with energy and life. The market is a hub for both locals and tourists, offering a lively, authentic experience. At the center, you’ll find rows of vibrant stalls overflowing with fresh fruits and vegetables, creating a colorful display that’s as much a feast for the eyes as it is for the taste buds. The aroma of fresh produce and the friendly chatter of vendors add to the market’s charm, making it a must-see when exploring Port Louis.";
                    model.title1 = "Craft Market\n";
                    model.image1 = "";
                    model.text2 = "Head up to the first floor, and you’ll discover the Craft Market, a treasure trove of locally made souvenirs, spices, and handicrafts. From intricate woven baskets to handcrafted jewelry, there’s something for every taste and budget. It’s the perfect place to find a unique gift or a meaningful memento to take home with you. The variety of items on display reflects Mauritius' rich cultural blend, and the affordable prices make it easy to indulge. Whether you're browsing or buying, the Craft Market is an experience not to be missed.\n";
                    model.title2 = "Street Food Delights at Central Market\n";
                    model.text3 = "";
                    model.image2 = fullPath + "/" + "port_louis_5" + ".jpg";
                    model.text4 = "No visit to the Central Market is complete without sampling some of the local street food. Just steps away from the main market area, you’ll find stalls offering mouthwatering treats that are a favorite among locals. Be sure to try dhall puri, a soft flatbread filled with a mix of split chickpeas and spices, or cool off with alouda, a sweet and refreshing drink made with milk, basil seeds, and jelly.\n" + "<br><br>" + "For those who love snacks, the market has plenty to offer. Try the crispy pastry filled with spiced potatoes, known as samosas, or the fritters called bajas and gato piment, which are made with yellow split peas and chickpea flour and have a hint of chile. These delicious bites are perfect for a quick snack as you continue exploring.\n" + "<br><br>" + "Open throughout the week, the Port Louis Central Market is a lively gathering spot that gives you a true glimpse into Mauritian life. You can shop, eat, or just take in the atmosphere—either way, you will remember this experience long after you leave. This is the place to be if you want to fully immerse yourself in the local culture!\n";
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

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 7) {
                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "port_louis_9" + ".jpg";

                    model.text1 = "The Government House in Port Louis is a historic gem, tracing its origins back to the French colonial era. Construction began under Governors Nicolas de Maupin and Mahé de La Bourdonnais, who used it as both a residence and an administrative hub.\n" + "<br><br>" + "Originally, the structure featured wooden walls covered with palm leaves, but it underwent significant renovations, including an expansion by La Bourdonnais in 1738. The building was later known as Hôtel du Gouvernement and has seen various modifications through the French and British colonial periods.\n" + "<br><br>" + "Nearby, a statue of Queen Victoria stands as a reminder of the building's rich history, although it remains closed to the public. Despite this, its enduring presence continues to capture the essence of Port Louis's colonial past.\n";
                    model.title1 = "";
                    model.image1 = "";
                    model.text2 = "";
                    model.title2 = "";
                    model.text3 = "";
                    model.image2 = "";
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

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 8) {
                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "place_des_armes" + ".jpg";

                    model.text1 = "Nestled in the vibrant capital of Mauritius, Port Louis, Place d'Armes is a significant spot that captures the island's history and culture. Located between the former government hotel and the picturesque waterfront, this bustling square features statues honoring the founding fathers of Mauritius, making it a symbol of national pride.\n" + "<br><br>\n" + "As a central hub, Place d'Armes is where major north-south traffic converges, serving as a key access point for both locals and visitors. Its lively atmosphere and historical significance make it a must-visit destination when exploring the capital. Whether you’re taking a leisurely stroll or simply soaking in the sights, Place d'Armes offers a glimpse into the heart of Mauritian life.\n";
                    model.title1 = "";
                    model.image1 = fullPath + "/" + "place_darmes_2" + ".jpg";
                    model.text2 = "At the entrance of Place d'Armes stands a statue of Mahé de La Bourdonnais, a testament to his influential role in Mauritian history. This iconic square is lined with towering palm trees and features a three-lane avenue that has welcomed dignitaries from around the globe.";
                    model.title2 = "";
                    model.text3 = "";
                    model.image2 = fullPath + "/" + "port_louis_8" + ".jpg";
                    model.text4 = "Place d'Armes, Port Louis's oldest neighborhood, is home to numerous banks, businesses, and consulates. While the historical buildings once defining the area have transitioned to more modern architecture, the square remains a vital and vibrant center, reflecting the dynamic blend of the old and new in Mauritius.\n";
                    model.title3 = "Starting Your Journey at Place d'Armes\n";
                    model.text5 = "Place d'Armes serves as the perfect launching pad for your exploration of Port Louis. From this central square, you can easily access a variety of significant landmarks:\n" + "<br><br><ul>" + "<li><b>Government Hotel:</b> The historic site that once housed colonial dignitaries.\n</li>" + "<li><b>The Oldest Theater in the Indian Ocean:</b> A cultural gem reflecting the island's artistic heritage.\n</li>" + "<li><b>Vieux Conseil Street and Georges Guibert Street:</b> Stroll along these charming cobbled roads for a taste of the city’s history.\n</li>" + "<li><b>St Louis Cathedral:</b> This stunning Catholic church showcases beautiful architecture and serene grounds.\n</li>" + "<li><b>St James Cathedral:</b> A prominent Protestant church, adding to the area's religious diversity.\n</li>" + "<li><b>Central Market:</b> Immerse yourself in local life, with vibrant stalls and delicious street food.\n</li>" + "<li><b>Museums:</b> Several nearby museums offer insights into Mauritius's rich history and culture.\n</li>" + "</ul>" + "Each of these attractions contributes to the vibrant tapestry of Port Louis, making Place d'Armes a must-visit spot for anyone looking to experience the capital's unique charm.\n";
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

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 9) {
                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "blue_penny_museum_2" + ".jpg";

                    model.text1 = "The Blue Penny Museum, inaugurated in November 2001, stands as a significant cultural institution dedicated to showcasing the rich history and art of Mauritius. Here are some key highlights:\n";
                    model.title1 = "";
                    model.image1 = "";
                    model.text2 = "<b>Historic Stamps:</b> The museum's name derives from the famous 1847 Blue Penny and Red Penny stamps, which are considered some of the rarest and most valuable stamps in the world. In 1993, a consortium of Mauritian enterprises, led by The Mauritius Commercial Bank, purchased these stamps for an astounding $2 million. This marked a momentous occasion as the stamps were repatriated to Mauritius after nearly 150 years of absence.\n" + "<br><br>" + "<b>Cultural Significance:</b> The Blue Penny Museum was established to preserve and promote the island's artistic and historical heritage. It serves not only as a display space for these iconic stamps but also as a hub for exploring various aspects of Mauritian culture, including its diverse influences and unique identity.\n" + "<br><br>" + "<b>Conservation Practices:</b> Given the extreme rarity and immense value of the Blue Penny stamps, meticulous conservation efforts are in place. The originals are illuminated only temporarily to minimize the risk of damage from prolonged exposure to light. For most visitors, replicas are displayed, ensuring that the original stamps remain safe for future generations to appreciate.\n" + "<br><br>" + "<b>Founder - The Mauritius Commercial Bank:</b> The Blue Penny Museum was established by The Mauritius Commercial Bank, a prominent financial institution in Mauritius known for its longstanding service and community engagement. \n";
                    model.title2 = "";
                    model.text3 = "";
                    model.image2 = fullPath + "/" + "blue_penny_museum_1" + ".jpg";
                    model.text4 = "The bank is proving through this project that it is dedicated to the preservation of the island's rich cultural legacy in addition to its financial success. The bank's goal in opening the museum is to increase pride and knowledge of Mauritius's historical significance among locals and visitors alike, particularly in light of the island's unique philatelic treasures.\n";
                    model.title3 = "Additional Artifacts\n";
                    model.text5 = "In addition to the renowned Blue Penny stamps, the museum features the original statue of Paul and Virginia, crafted by Prosper d'Épinay in 1881. This piece enhances the museum's cultural and artistic collection, drawing interest from both locals and tourists eager to explore Mauritius's history.\n";
                    model.title4 = "Museum Structure\n";
                    model.text6 = "The museum spans two floors, each with unique exhibits:\n" + "<br><br>" + "<b><span style=\"color:black; font-weight:bold;\">Ground Floor\n</span></b>" + "<br><br><ul>" + "<b>Souvenir Shop:</b> Visitors are greeted by a souvenir shop, offering mementos related to the museum and Mauritius.\n" + "<br><br>" + "<b>Temporary Exhibition Room:</b> This space hosts rotating exhibits, ensuring diverse and dynamic displays that change over time.\n" + "</ul>" + "<br><br>" + "<b><span style=\"color:black; font-weight:bold;\">First Floor\n</span></b>" + "<br><br><ul>" + "<li><b>The Age of Discovery:</b> This exhibition narrates the stories of pioneers who sailed to the Mascarene Islands, featuring ancient maps that document their journeys.\n</li>" + "<li><b>The Island Builders:</b> This room covers three critical periods in Mauritius's history: the Dutch, French, and British eras, providing a comprehensive overview of the island's evolution.\n</li>" + "<li><b>Port Louis:</b> Focused on the origins and history of Port Louis, this exhibition offers insights into the city's development.\n</li>" + "<li><b>The Postal Adventure:</b> Dedicated to Mauritius's postal history, this room displays two stamps from the Blue Penny collection, highlighting the island's philatelic heritage.\n</li>" + "<li><b>Engraved Memory:</b> Honoring Joseph Osmond Barnard, the first engraver of stamps in Mauritius, this room serves as a tribute to his contributions to the field.\n</li>" + "</ul>" + "Overall, the Blue Penny Museum is thoughtfully organized to provide visitors with a captivating journey through the history, art, and unique cultural heritage of Mauritius.\n";
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

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 10) {
                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "church_pl" + ".jpg";

                    model.text1 = "The St. Louis Cathedral, or 'Katedral Sin Lwi,' is a significant landmark in Port Louis, close to Champ de Mars, and stands as one of the oldest churches in Mauritius. It has undergone multiple demolitions and reconstructions over the years and currently houses the bishop of the Port Louis diocese.\n" + "<br><br>" + "Constructed initially as a makeshift wooden chapel between 1752 and 1756, it was used during the war and eventually destroyed by cyclones. Mahe de Labourdonnais chose the site, and by 1756, the cathedral was completed. But in April 1773, a cyclone destroyed it entirely. A new church opened in 1782 but was soon closed due to structural problems.  Sir Robert Farquhar oversaw its reconstruction in 1814, but it faced another demolition in 1928, leading to the current structure being rebuilt in 1933 under Mgr James Leen. This version features a larger design and historic elements like the facade and stone towers.\n";
                    model.title1 = "";
                    model.image1 = fullPath + "/" + "text1" + ".jpg";
                    model.text2 = "Inside, visitors can find relics from its past, including statues, paintings, and period furniture. The altar, still in use, adds to its historical significance. In 2007, the cathedral underwent extensive renovations to celebrate its 160th anniversary, restoring its prominence in the city.\n" + "<br><br>" + "The cathedral is also the burial site for several bishops, a common tradition in the Catholic Church. Six bishops, including Mgrs Hankinson, Meurin, O’Neil, Bilsborrow, Leen, and the most recent, Cardinal Margéot, who was interred in July 2009, rest beneath the choir tiling, marking its rich ecclesiastical history.\n";
                    model.title2 = "";
                    model.text3 = "";
                    model.image2 = "";
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

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 11) {
                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "natural_history_musuem" + ".jpg";

                    model.text1 = "The Natural History Museum in Port Louis is housed on the ground floor of the Mauritius Institute Building, a structure recognized as a National Heritage site in the heart of the city. Built between 1880 and 1884, the Mauritius Institute Building partially emulates the Colombo Museum of African Modern Art in Sri Lanka, designed by British architect M. Mann.\n" + "<br><br>" + "With a rich history, the Natural History Museum is the oldest museum in Mauritius and among the first in southern Africa. The concept for a museum in Mauritius emerged in 1826 when naturalists Julien Desjardins and Louis Bouton proposed it to Governor Sir Lowry Cole. This led to the inauguration of the Desjardins Museum of African Modern Art on October 14, 1842, within a wing of the old Royal College in Port Louis, where it remained for 42 years.\n" + "<br><br>" + "In 1880, the Mauritius Institute was established under Governor Sir George Ferguson Bowen. The collection from the Desjardins Museum was relocated to the newly constructed Mauritius Institute Building in 1885, focusing primarily on marine fauna and birds from the Mascarene Islands. This collection formed the foundation of the present-day Natural History Museum.\n" + "<br><br>" + "Dedicated to the systematic study and documentation of the fauna and flora of Mauritius and the Mascarene Islands, the museum has become a center for information exchange across various natural history fields in the region. In 2000, it was designated a National Museum, reinforcing its role in preserving and promoting the country's natural heritage. The museum's primary attraction is the most complete dodo skeleton, discovered in 1904 by barber Etienne Thirioux, underscoring its significance in understanding Mauritius's unique biodiversity.\n";
                    model.title1 = "Key Features of the Natural History Museum:\n";
                    model.image1 = "";
                    model.text2 = "<ul>" + "<li><b>Exhibits and Displays:</b> The museum showcases various aspects of natural history, including geology, paleontology, botany, and zoology, with fossils, indigenous plant specimens, and information about unique ecosystems in Mauritius.</li>" + "<li><b>Endemic Species:</b> Mauritius is famous for its unique flora and fauna. The museum emphasizes endemic species found nowhere else in the world, providing insights into rare plants, birds, and wildlife.</li>" + "<li><b>Educational Content:</b> Serving an educational purpose, the museum offers valuable information about the island's geological history, species evolution, and conservation importance.</li>" + "<li><b>Research and Conservation:</b> The museum engages in research and conservation efforts, collaborating with scientists and conservationists to enhance understanding and preservation of the island's natural environment.\n</li>" + "<li><b>Interactive Displays:</b> Incorporating modern features, the museum offers interactive displays, multimedia presentations, and engaging activities to enrich the visitor experience for all ages.\n</li>" + "</ul>";
                    model.title2 = "";
                    model.text3 = "";
                    model.image2 = "";
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

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 12) {
                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "aapravasi_ghat_1" + ".jpg";

                    model.text1 = "The Aapravasi Ghat is a historical site located in Port Louis, the capital city of Mauritius. It holds great significance in the history of the island and is recognized as a UNESCO World Heritage Site. The history of the Aapravasi Ghat is closely tied to the indentured labor system that shaped the social and economic landscape of Mauritius.\n" + "<br><br>\n" + "Here is a brief history of the Aapravasi Ghat:\n" + "\n<br><br>" + "<b>Indentured Labor System:</b> In the 19th century, after the abolition of slavery, the British turned to the system of indentured labor to meet the demand for cheap and abundant workforce for their colonies. Indentured laborers were recruited from various parts of India, as well as China and Africa, to work on plantations in Mauritius and other British colonies.\n" + "\n<br><br>" + "<b>Establishment of Aapravasi Ghat:</b> The Aapravasi Ghat was established in 1849 as the first site for the reception of indentured laborers in Mauritius. The name \"Aapravasi Ghat\" translates to \"immigration depot\" in Hindi. The location served as a processing center where indentured laborers arriving by ship were registered, housed temporarily, and assigned to various plantations across the island.\n" + "\n<br><br>" + "<b>Housing:</b> At the Aapravasi Ghat, indentured laborers had to endure substandard housing. They were often crowded into cramped barracks, and the site became a place where many experienced suffering, sickness, and death. The conditions were challenging, and the laborers endured a difficult transition from their home countries to the unfamiliar environment of Mauritius.\n" + "\n<br><br>" + "<b>End of Indenture:</b> The indenture system continued until the early 20th century when it was officially abolished. The Aapravasi Ghat continued to be used for processing immigrants until 1920. The location underwent many changes over the years that served various functions after the indenture system was discontinued.\n" + "\n<br><br>" + "<b>UNESCO World Heritage Site:</b> In 2006, the Aapravasi Ghat was designated as a UNESCO World Heritage Site in recognition of its historical importance in the global migration of indentured laborers and its impact on the multicultural identity of Mauritius.\n";
                    model.title1 = "";
                    model.image1 = fullPath + "/" + "aapravasi_ghat_2" + ".jpg";
                    model.text2 = "Today, the Aapravasi Ghat stands as a poignant symbol of the struggles and resilience of the indentured laborers who played a crucial role in shaping Mauritius's cultural and social fabric. \n" + "<br><br>\n" + "This UNESCO World Heritage site is open to visitors, offering them a chance to explore its rich history and gain insights into this critical period of the island's past. The exhibits and memorials here provide a deeper understanding of the lives and contributions of these laborers, making it a significant destination for those interested in Mauritian heritage.\n";
                    model.title2 = "";
                    model.text3 = "";
                    model.image2 = "";
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

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 13) {
                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "post_office" + ".jpg";

                    model.text1 = "The present-day museum building was originally constructed as the General Post Office between 1865 and 1870, strategically located by the harbor next to the customs building in the center of the town. Construction began in January 1865 under the supervision of Surveyor General Morrison, reaching three-quarters completion by 1868, with a total expense of £10,000 to £11,000 and around 80 workers involved. It officially opened its doors in December 1870.\n" + "<br><br>" + "Architecturally, the building exemplifies Victorian design typical of public colonial structures from Queen Victoria's era, resembling similar edifices found in India, Sri Lanka, South Africa, Trinidad, and Guyana. It replaced the former General Post Office on Government Street, which had been operational since 1847. The Central Telegraph Office moved into its premises in April 1877, and it has functioned as Mauritius' principal post office ever since it opened.\n" + "<br><br>" + "The Postmaster General resided there, making it the hub for mail from 33 rural post offices established on the island between the 1870s and 1890s. In 1958, the building was recognized as a listed structure, following a recommendation from the Ancient Monuments Board. This designation was reaffirmed under the Mauritian National Monuments Act of 1985 and further confirmed by the National Heritage Fund Act of 2003.\n" + "<br><br>" + "Established in 2001, the postal museum within the building showcases the history of postal services and telecommunications in Mauritius. While the famous Red and Blue \"Post Office\" stamps are not displayed here, they can be found at the nearby Blue Penny Museum.\n";
                    model.title1 = "";
                    model.image1 = "";
                    model.text2 = "";
                    model.title2 = "";
                    model.text3 = "";
                    model.image2 = "";
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

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 14) {
                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "citadelle" + ".jpg";

                    model.text1 = "Fort Adelaide, constructed between 1834 and 1835 under Colonel Thomas Cunningham, stands as a symbol of the early years of British rule in Mauritius. The fort's construction was challenging due to labor shortages following the abolition of slavery. To overcome this, the British government initially relied on apprentices, liberated slaves, prisoners, and soldiers. However, by 1837, the lack of skilled labor led to the recruitment of craftsmen, including stonemasons and masons from India.\n" + "<br><br>" + "The fort's main objectives were to quell any local uprisings and protect the port from any possible invasions. It was named in honor of Queen Adelaide, wife of King William IV, and the entrance still bears her initials and crown. Although it was never used for military or policing purposes, Fort Adelaide remains one of the few surviving structures from the transitional period between the abolition of slavery and the arrival of indentured laborers.\n" + "<br><br>" + "Unlike other forts from the British era, such as Fort William and Fort Victoria, which have deteriorated over time, Fort Adelaide remains intact. Built from black basalt stones, the fort is a testament to the craftsmanship of the era. Completed in 1840, it marks the beginning of Indian immigration and the end of slavery, symbolized by the people involved in its construction and the historical context of its creation.\n";
                    model.title1 = "Historical Context\n";
                    model.image1 = "";
                    model.text2 = "In the 1830s, tensions were high in Mauritius, which had just been placed under British control. The French Revolution of 1830 exacerbated the unhappiness of the people of France over the loss of authority and the abolition of the slave trade. With a population comprising whites, people of color, and former slaves, British authorities feared both foreign attacks and uprisings. Fort Adelaide was strategically built on a hill overlooking Port Louis to safeguard the city, the port, and the surrounding mountains.\n" + "<br><br>" + "However, by the time the fort was completed, the political situation on the island had stabilized, and its original military purpose became redundant. Instead, it was repurposed for ceremonial activities, such as signaling the arrival of important figures or daily cannon fire.\n";
                    model.title2 = "A Cultural Hub\n";
                    model.text3 = "Over time, Fort Adelaide transitioned from a military installation to a cultural center, hosting concerts, shows, and other events. Today, it remains a prominent landmark in Mauritius, not only for its architectural and historical significance but also for its role as a venue for cultural initiatives. During a pivotal juncture in the evolution of Mauritius, Fort Adelaide serves as a reminder of the island's complicated past.\n";
                    model.image2 = "";
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

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 15) {
                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "port_louis_photography_museum" + ".jpg";

                    model.text1 = "Founded in 1966 by Mr. Tristan Bréville and his wife, the Bréville Photography Museum began as a private collection at their home in Rose-Hill. It later moved to Quatre Bornes before finding its permanent location in 1993 within a historic building donated by the municipality. With support from the Association Internationale des Maires Francophones (AIMF), the building was restored. The museum's official inauguration took place on July 1, 1993, attended by sixty-four French mayors, French Minister of Culture Mr. Jacques Toubon, and Mr. Jean-Luc Monterrosso, director of the European House of Photography.\n" + "<br><br>" + "Mauritius embraced photography in February 1840, just four months after the island acquired the daguerreotype patent from Louis Daguerre in France. This early adoption made Mauritius one of the world's pioneers in photography.\n";
                    model.title1 = "A Rich Collection of Photographic History\n";
                    model.image1 = fullPath + "/" + "photography_musuem_3" + ".jpg";
                    model.text2 = "The museum houses an impressive array of documents that capture the history of photography in Mauritius. These include portraits, landscapes, urban and rural scenes, significant events, and moments from the island’s early days. The collection also highlights factories, fishing activities, the first automobiles and buses (from 1930), the Mauritian railway, colonial residences, historical buildings, and the island’s flora and fauna.";
                    model.title2 = "Extensive Camera Collection and Research Center\n";
                    model.text3 = "";
                    model.image2 = fullPath + "/" + "port_louis_photography_museum_inside" + ".jpg";
                    model.text4 = "The museum's collection includes over 1,000 cameras, some dating back to the earliest days of photography, including a lens made by Charles Chevalier for Daguerre in 1839. It also functions as an iconographic research center, featuring a specialized library and collections such as Nadar business cards. The museum holds 400,000 acetate negatives, 5,000 glass plates, 28 daguerreotypes, 10 autochromes by the Lumière brothers, and 200,000 prints depicting the history and landscapes of Mauritius. Additionally, the museum possesses 9,000 vintage postcards, over twenty-five hours of films about Mauritius from 1939, archival newspapers from the late 19th century to 1945 related to photography, as well as materials on the history of cinematography on the island dating back to 1897.\n" + "<br><br>" + "This collection makes the Bréville Photography Museum a vital resource for anyone interested in the visual and cultural history of Mauritius.\n";
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

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 16) {
                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "marie_reine_de_la_paix_3" + ".jpg";

                    model.text1 = "Marie Reine de la Paix, or Mary Queen of Peace, is a Catholic church and pilgrimage site that holds great spiritual significance in Mauritius. The chapel centerpiece is a stunning 3-meter-tall statue of the Virgin Mary, gracefully holding a globe. Crafted from Carrara marble by Italian sculptor Ferdinando Palla, the statue was unveiled on the Feast of the Assumption in 1940. The monument was dedicated to Mary Queen of Peace as a gesture of gratitude for protecting Mauritius during World War I.\n";
                    model.title1 = "A Popular Pilgrimage Site";
                    model.image1 = fullPath + "/" + "marie_reine_de_la_paix_5" + ".jpg";
                    model.text2 = "Marie Reine de la Paix is an important destination for Catholic pilgrims, particularly during religious celebrations like the Feast of the Assumption. Thousands of devotees gather here to honor the Blessed Virgin Mary.  The church gained further recognition in 1989, when Pope John Paul II visited during his 44th international journey, cementing its place as a site of international religious significance.\n";
                    model.title2 = "A Serene Escape with Panoramic Views\n";
                    model.text3 = "";
                    model.image2 = fullPath + "/" + "marie_reine_de_la_paix_4" + ".jpg";
                    model.text4 = "\n" + "Located on the slopes of Signal Mountain in Port Louis, the church offers more than a place of worship; it serves as a peaceful sanctuary with breathtaking views of the city below. There are seven terraces and eighty-two rock-carved steps leading up to the chapel. Every terrace has bright flowers growing up it. The surrounding gardens, meticulously maintained, provide an inviting and serene atmosphere, making it a perfect spot for quiet reflection and gatherings with friends and family.\n";
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

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 17) {
                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "jardin_de_la_compagnie_1" + ".jpg";

                    model.text1 = "The Jardin de la Compagnie is one of Port Louis's most charming public gardens, known for its towering banyan trees, statues, benches, and fountains. It is an inviting and peaceful place to visit during the day, providing a relaxing escape in the middle of the city. However, it’s best to avoid the park after dark, as it becomes a gathering spot for sex workers and drug addicts.";
                    model.title1 = "Origins of the Garden\n";
                    model.image1 = fullPath + "/" + "les_jardins_de_la_compagnie_2" + ".jpg";
                    model.text2 = "The Jardin de la Compagnie's origins can be traced to the French colonial era, which began in 1710 when the Dutch left Mauritius. After dealing with numerous difficulties, including cyclones and floods, the Dutch left the island, and the French took control in 1721. The garden was established by Mr. Durongouet-Le-Toullec, who arrived from Bourbon Island (now Réunion) and settled along the \"Ruisseau du Pouce,\" laying the foundation for what would later become the Company Garden.\n" + "<br><br>" + "Initially, the area was swampy and unfit for construction, but as governors worked on urban development, the garden became part of the city's landscape. During the smallpox epidemic, it also served as a temporary cemetery. It wasn’t until 1771, under Governor Desroches, that Port Louis began to take on the appearance of a town, and the cemetery was relocated to make way for the garden’s use as a public space for leisure and enjoyment.\n";
                    model.title2 = "";
                    model.text3 = "";
                    model.image2 = "";
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

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 18) {
                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "odysseo_1" + ".jpg";

                    model.text1 = "Dive into a captivating adventure at <b>Odysseo</b>, the largest oceanarium in the Mascarene Islands. This immersive experience is a dream for marine biology enthusiasts, offering a unique opportunity to explore the rich biodiversity of the Indian Ocean.\n";
                    model.title1 = "An Oceanic Adventure in Port Louis\n";
                    model.image1 = fullPath + "/" + "odysseo_2" + ".jpg";
                    model.text2 = "Located in the heart of <b>Port Louis,</b> near the harbor, Odysseo welcomes visitors with its ocean-themed exhibits featuring a diverse range of species, from colorful fish to majestic sharks. \n";
                    model.title2 = "";
                    model.text3 = "";
                    model.image2 = fullPath + "/" + "odysseo_3" + ".jpg";
                    model.text4 = "The experience offers two options: general admission to the oceanarium or a guided tour for a deeper exploration.\n";
                    model.title3 = "Highlights of the Odysseo Experience:\n";
                    model.text5 = "<ul>" + "<li><b>The Lagoon:</b> Explore the calm waters of the Mauritian lagoon, teeming with vibrant fish, seagrass beds, mangroves, and coral reefs.\n</li>" + "<li><b>Inland Forests:</b> Discover lakes, rivers, and forests, home to a variety of freshwater species, insects, and reptiles.\n</li>" + "<li><b>Coral Reefs:</b> Dive into the secrets of the coral reefs, uncovering the incredible biodiversity of the Indian Ocean.\n</li>" + "<li><b>Open Sea:</b> Walk through an immersive tunnel as stingrays, sharks, eels, and other marine creatures swim beside you.\n</li>" + "<li><b>Invertebrates:</b> Learn about the fascinating world of invertebrates, which make up 95% of Earth’s animal species.\n</li>" + "<li><b>Sub-Oceanic View:</b> Gain insights into sharks and their critical role in marine ecosystems, advocating for their protection.\n</li>" + "</ul>" + "After exploring Odysseo, be sure to visit the <b>gift shop,</b> where you can pick up souvenirs ranging from stuffed animals and educational games to books and marine-themed jewelry, bringing a piece of the oceanic experience home with you.\n" + "";
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

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 19) {
                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "chateau_de_labourdonnais" + ".jpg";

                    model.text1 = "A majestic lane lined with towering trees leads you to <b>Château Labourdonnais</b>, a stunning colonial mansion that once belonged to Christian Wiehe. Built in 1856 and completed a few years later, this mansion is often regarded as the most beautiful colonial house in Mauritius. \n";
                    model.title1 = "";
                    model.image1 = fullPath + "/" + "chateau_de_labourdonnais_4" + ".jpg";
                    model.text2 = "The grand estate features a spacious dining room, a vast pantry, an expansive living room, elegantly designed bedrooms, and a large exhibition hall, making it a captivating destination.\n";
                    model.title2 = "Gardens and Exploration\n";
                    model.text3 = "";
                    model.image2 = fullPath + "/" + "chateau_de_labourdonnais_2" + ".jpg";
                    model.text4 = "As you explore the grounds, you’ll be enchanted by the lush gardens, where century-old mango trees, spice trees, and exotic fruit trees abound. Adding to the charm, peaceful <b>Aldabra tortoises</b> graze around the gardens. You can also visit the <b>Rhumerie des Mascareignes</b>, established in 2006, to learn about traditional rum-making techniques, and enjoy a delightful tasting session that includes rum, fruit pastes, juices, and sorbets.\n" + "<br><br>" + "The visit concludes at the Tasting Bar, where you can sample products made from the estate’s orchards, such as jams, fruit jellies, and sorbets. Before you leave, don’t miss the opportunity to visit the boutique, which offers a range of local products like decorative items, rum, and spices. For a refined dining experience, visit <b>La Table du Château</b>, a restaurant offering Mauritian dishes made from fresh produce grown on the estate.\n";
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

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 20) {
                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "albion_lighthouse" + ".jpg";

                    model.text1 = "Situated atop a cliff with a commanding view of the ocean, the Cave Point lighthouse safely directs ships into Port Louis harbor. With its striking red and white stripes, this iconic lighthouse has been standing since 1910 and is well-maintained. Visitors must ascend a 98-step wooden and cast-iron staircase to reach the top of the lighthouse.\n" + "<br><br>" + "The sweeping views of the coastline, which stretches from Pointe aux Sables to Flic en Flac, make the climb worthwhile. If heights aren’t your thing, a walk along the trail to the lighthouse still offers breathtaking views of the shore. Taking a moment to pause and listen to the waves crash against the cliffs enhances the experience.\n" + "<br><br>" + "The area is popular with photographers, and beneath the cliffs, there's a cave home to birds and bats. Adventurous cliff divers often jump into the sea from the rocks, and you might even see fishermen at work. As the sun sets, the sky comes alive with vibrant colors, creating a beautiful scene.\n" + "<br><br>" + "It’s best to avoid visiting the lighthouse on windy days.\n";
                    model.title1 = "";
                    model.image1 = "";
                    model.text2 = "";
                    model.title2 = "";
                    model.text3 = "";
                    model.image2 = "";
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

                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 21) {
                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "albion_lighthouse" + ".jpg";
                    model.text1 = "";
                    model.title1 = "";
                    model.image1 = "";
                    model.text2 = "";
                    model.title2 = "";
                    model.text3 = "";
                    model.image2 = "";
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
                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 22) {
                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "albion_lighthouse" + ".jpg";
                    model.text1 = "";
                    model.title1 = "";
                    model.image1 = "";
                    model.text2 = "";
                    model.title2 = "";
                    model.text3 = "";
                    model.image2 = "";
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
                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                } else if (holder.getAdapterPosition() == 23) {
                    BeacModel model = new BeacModel();
                    model.title = holder.textView.getText().toString();
                    model.main_image = fullPath + "/" + "albion_lighthouse" + ".jpg";
                    model.text1 = "";
                    model.title1 = "";
                    model.image1 = "";
                    model.text2 = "";
                    model.title2 = "";
                    model.text3 = "";
                    model.image2 = "";
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
                    model.lat = itemLatitudes[holder.getAdapterPosition()];
                    model.lng = itemLongitudes[holder.getAdapterPosition()];
                    if (isOpened) {

                        Stash.put("model", model);
                        intent = new Intent(context, BeachDetails.class);
                        context.startActivity(intent);


                    } else {
                        com.moutamid.sqlapp.model.DatabaseHelper databaseHelper = new com.moutamid.sqlapp.model.DatabaseHelper(context);
                        databaseHelper.insertBeacModel(model);
                        holder.add.setVisibility(View.GONE);
                        holder.remove.setVisibility(View.VISIBLE);
                    }


                }
                break;
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

    private void loadRating(String placeName, MaterialRatingBar ratingBar, ViewHolder holder) {
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
                    holder.count.setText(average + "  (" + count + ")");
                    Log.d("Rating", "Average: " + average + ", Count: " + count);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Rating", "Error: " + error.getMessage());
            }
        });
    }

    private void fetchWeatherData(double lat, double lng, ViewHolder holder) {

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
                        Glide.with(context).load(iconUrl).into(holder.weather_icon);
                        holder.temp.setText(temp + " °C");
                        holder.condition.setText(mainWeather);
                        Log.d("Weather", "City: " + city + ", Temp: " + temp + "°C" + jsonObject.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            error.printStackTrace();
        });

        queue.add(stringRequest);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, add, remove, map, weather_icon;
        TextView textView, count, temp, condition;
        me.zhanghai.android.materialratingbar.MaterialRatingBar ratingBar;
        RelativeLayout main_lyt;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            main_lyt = itemView.findViewById(R.id.main_lyt);
            weather_icon = itemView.findViewById(R.id.weather_icon);
            textView = itemView.findViewById(R.id.title);
            add = itemView.findViewById(R.id.add);
            temp = itemView.findViewById(R.id.temp);
            condition = itemView.findViewById(R.id.condition);
            remove = itemView.findViewById(R.id.remove);
            map = itemView.findViewById(R.id.map);
            ratingBar = itemView.findViewById(R.id.ratingbar);
            count = itemView.findViewById(R.id.count);
        }
    }

}


