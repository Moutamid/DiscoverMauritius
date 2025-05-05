package com.moutamid.sqlapp.activities.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.moutamid.sqlapp.R;
import com.moutamid.sqlapp.activities.Iteneraries.ItenerariesDetails;
import com.moutamid.sqlapp.activities.Iteneraries.ItinerariesActivity;
import com.moutamid.sqlapp.helper.Constants;
import com.moutamid.sqlapp.model.BeacModel;
import com.moutamid.sqlapp.offlinemap.DistanceCalculator;
import com.moutamid.sqlapp.offlinemap.DurationCalculator;
import com.moutamid.sqlapp.offlinemap.MapActivity;

import java.io.File;

public class ItenerariesAdapter extends BaseAdapter {
    String fullPath;
    private Context context;
    private String[] itemTexts;
    private String[] itemName;
    private String[] itemDetails;
    private String[] itemImages;
    private double[] latitudes;
    private double[] longitudes;
    private double totalDistance = 0.0;
    private double totalDuration = 0.0;

    public ItenerariesAdapter(Context context, String[] itemName, String[] itemDetails, String[] itemTexts, String[] itemImages, double[] latitudes, double[] longitudes) {
        this.context = context;
        this.itemTexts = itemTexts;
        this.itemImages = itemImages;
        this.itemDetails = itemDetails;
        this.itemName = itemName;
        this.latitudes = latitudes;
        this.longitudes = longitudes;
        calculateTotalDistanceAndDuration();
    }

    private void calculateTotalDistanceAndDuration() {
        totalDistance = 0.0;
        totalDuration = 0.0;
        try {
            for (int i = 1; i < itemTexts.length; i++) {
                double distance = DistanceCalculator.calculateDistance(
                        latitudes[i - 1], longitudes[i - 1], latitudes[i], longitudes[i]);
                double duration = DurationCalculator.calculateDrivingDuration(distance);
                totalDistance += distance;
                totalDuration += duration;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ItinerariesActivity.distance.setText(String.format("%.1f km", totalDistance));
        ItinerariesActivity.time.setText(DurationCalculator.formatDuration(totalDuration));
        ItinerariesActivity.total_stop.setText(itemTexts.length + " stops");

        Log.d("MyAdapter", "Total Distance: " + String.format("%.1f km", totalDistance));
        Log.d("MyAdapter", "Total Duration: " + DurationCalculator.formatDuration(totalDuration));
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
        View itemView = inflater.inflate(R.layout.iteneraies_layout, parent, false);
        ImageView imageView = itemView.findViewById(R.id.imageView);
        TextView textView = itemView.findViewById(R.id.textView);
        TextView textView1 = itemView.findViewById(R.id.textView1);
        TextView textView2 = itemView.findViewById(R.id.textView2);
        TextView textView3 = itemView.findViewById(R.id.textView3);
        TextView number = itemView.findViewById(R.id.number);
        ImageView map = itemView.findViewById(R.id.map);
        textView.setText(itemName[position]);
        textView1.setText(itemTexts[position]);
        textView2.setText(itemDetails[position]);
        int i = position + 1;
        File cacheDir = new File(context.getFilesDir(), "cached_images");
        fullPath = cacheDir.getAbsolutePath();
        String image = itemImages[position];
        Glide.with(context)
                .load(new File(image))
                .into(imageView);
        number.setText("" + i);

        if (position == 0) {
            textView3.setText("Start\nHere");
        } else {
            double distance = 0.0;
            double duration = 0.0;
            try {
                distance = DistanceCalculator.calculateDistance(
                        latitudes[position - 1], longitudes[position - 1], latitudes[position], longitudes[position]);
                duration = DurationCalculator.calculateDrivingDuration(distance);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String formattedDuration = DurationCalculator.formatDuration(duration);
            textView3.setText(formattedDuration + "\n" + String.format("%.1f km", distance));
        }
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Stash.getBoolean(Constants.IS_PREMIUM, false) == true) {
                    Stash.put("map_lat", latitudes[position]);
                    Stash.put("map_lng", longitudes[position]);
                    Stash.put("map_name", itemName[position]);
                    Stash.put("map_img", itemImages[position]);
                    Intent intent = new Intent(context, MapActivity.class);
                    intent.putExtra("map_lat", latitudes[position]);
                    intent.putExtra("map_lng", longitudes[position]);
                    context.startActivity(intent);
                } else {
                    ItinerariesActivity.premium_layout.setVisibility(View.VISIBLE);
                }
            }
        });
        if (position == 0) {
            textView3.setText("Start here");
        }
        itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                if (Stash.getString("day").equals("day1")) {
                    if (position == 0) {
//                        Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "port_louis_3" + ".jpg";
                        model.text1 = "Le Caudan Waterfront is a vibrant commercial hub in Port Louis, Mauritius, featuring an array of amenities such as shops, banks, casinos, cinemas, restaurants, a marina, and the luxurious Le Labourdonnais hotel.\n";
                        model.title1 = "Historical Roots\n";
                        model.image1 = "";
                        model.text2 = "The name \"Le Caudan Waterfront\" honors Jean Dominique Michel de Caudan, who arrived in Mauritius in 1726 and established a saltpan near what is now the Robert Edward Hart Garden. This peninsula, formed around a fossil coral islet, has evolved over 250 years, once housing a powder magazine, observatories, and warehouses, with the sugar industry shaping much of its history until the Bulk Sugar Terminal opened in 1980.\n";
                        model.title2 = "Historically Significant Spots\n";
                        model.text3 = "Notable historical locations in Le Caudan include the Blue Penny Museum, which is situated in the Food Court, and the first meteorological observatory in the Indian Ocean, which was formerly a docks office. Different areas within the complex are named to reflect the island's colonial history:\n" +
                                "<br><br><ul>" +
                                "<li><b>Barkly Wharf:</b> Named after Sir Henry Barkly, Governor of Mauritius (1863-1870).\n</li>" +
                                "<li><b>Le Pavillon Wing:</b> Linked to Pavillon Street from an old map of Port Louis.\n</li>" +
                                "<li><b>Dias Pier:</b> Honors Diogo Dias, the navigator believed to have first charted the Mascarene Islands.\n</li>" +
                                "</ul>" +
                                "Le Caudan Waterfront combines modernity with rich historical significance, making it a must-visit destination.\n";
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
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 1) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "aapravasi_ghat_1" + ".jpg";
                        model.text1 = "The Aapravasi Ghat is a historical site located in Port Louis, the capital city of Mauritius. It holds great significance in the history of the island and is recognized as a UNESCO World Heritage Site. The history of the Aapravasi Ghat is closely tied to the indentured labor system that shaped the social and economic landscape of Mauritius.\n" +
                                "<br><br>\n" +
                                "Here is a brief history of the Aapravasi Ghat:\n" +
                                "\n<br><br>" +
                                "<b>Indentured Labor System:</b> In the 19th century, after the abolition of slavery, the British turned to the system of indentured labor to meet the demand for cheap and abundant workforce for their colonies. Indentured laborers were recruited from various parts of India, as well as China and Africa, to work on plantations in Mauritius and other British colonies.\n" +
                                "\n<br><br>" +
                                "<b>Establishment of Aapravasi Ghat:</b> The Aapravasi Ghat was established in 1849 as the first site for the reception of indentured laborers in Mauritius. The name \"Aapravasi Ghat\" translates to \"immigration depot\" in Hindi. The location served as a processing center where indentured laborers arriving by ship were registered, housed temporarily, and assigned to various plantations across the island.\n" +
                                "\n<br><br>" +
                                "<b>Housing:</b> At the Aapravasi Ghat, indentured laborers had to endure substandard housing. They were often crowded into cramped barracks, and the site became a place where many experienced suffering, sickness, and death. The conditions were challenging, and the laborers endured a difficult transition from their home countries to the unfamiliar environment of Mauritius.\n" +
                                "\n<br><br>" +
                                "<b>End of Indenture:</b> The indenture system continued until the early 20th century when it was officially abolished. The Aapravasi Ghat continued to be used for processing immigrants until 1920. The location underwent many changes over the years that served various functions after the indenture system was discontinued.\n" +
                                "\n<br><br>" +
                                "<b>UNESCO World Heritage Site:</b> In 2006, the Aapravasi Ghat was designated as a UNESCO World Heritage Site in recognition of its historical importance in the global migration of indentured laborers and its impact on the multicultural identity of Mauritius.\n";
                        model.title1 = "";
                        model.image1 = fullPath + "/" + "aapravasi_ghat_2" + ".jpg";
                        model.text2 = "Today, the Aapravasi Ghat stands as a poignant symbol of the struggles and resilience of the indentured laborers who played a crucial role in shaping Mauritius's cultural and social fabric. \n" +
                                "<br><br>\n" +
                                "This UNESCO World Heritage site is open to visitors, offering them a chance to explore its rich history and gain insights into this critical period of the island's past. The exhibits and memorials here provide a deeper understanding of the lives and contributions of these laborers, making it a significant destination for those interested in Mauritian heritage.\n";
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
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 2) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "port_louis_4" + ".jpg";
                        model.text1 = "\n" + "The Port Louis Central Market, also known as the Bazaar of Port Louis, is the heart of the city’s shopping scene, buzzing with energy and life. The market is a hub for both locals and tourists, offering a lively, authentic experience. At the center, you’ll find rows of vibrant stalls overflowing with fresh fruits and vegetables, creating a colorful display that’s as much a feast for the eyes as it is for the taste buds. The aroma of fresh produce and the friendly chatter of vendors add to the market’s charm, making it a must-see when exploring Port Louis.";
                        model.title1 = "Craft Market\n";
                        model.image1 = "";
                        model.text2 = "Head up to the first floor, and you’ll discover the Craft Market, a treasure trove of locally made souvenirs, spices, and handicrafts. From intricate woven baskets to handcrafted jewelry, there’s something for every taste and budget. It’s the perfect place to find a unique gift or a meaningful memento to take home with you. The variety of items on display reflects Mauritius' rich cultural blend, and the affordable prices make it easy to indulge. Whether you're browsing or buying, the Craft Market is an experience not to be missed.\n";
                        model.title2 = "Street Food Delights at Central Market\n";
                        model.text3 = "";
                        model.image2 = fullPath + "/" + "port_louis_5" + ".jpg";
                        model.text4 = "No visit to the Central Market is complete without sampling some of the local street food. Just steps away from the main market area, you’ll find stalls offering mouthwatering treats that are a favorite among locals. Be sure to try dhall puri, a soft flatbread filled with a mix of split chickpeas and spices, or cool off with alouda, a sweet and refreshing drink made with milk, basil seeds, and jelly.\n" +
                                "<br><br>" +
                                "For those who love snacks, the market has plenty to offer. Try the crispy pastry filled with spiced potatoes, known as samosas, or the fritters called bajas and gato piment, which are made with yellow split peas and chickpea flour and have a hint of chile. These delicious bites are perfect for a quick snack as you continue exploring.\n" +
                                "<br><br>" +
                                "Open throughout the week, the Port Louis Central Market is a lively gathering spot that gives you a true glimpse into Mauritian life. You can shop, eat, or just take in the atmosphere—either way, you will remember this experience long after you leave. This is the place to be if you want to fully immerse yourself in the local culture!\n";
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
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 3) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
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
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 4) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "citadelle" + ".jpg";
                        model.text1 = "Fort Adelaide, constructed between 1834 and 1835 under Colonel Thomas Cunningham, stands as a symbol of the early years of British rule in Mauritius. The fort's construction was challenging due to labor shortages following the abolition of slavery. To overcome this, the British government initially relied on apprentices, liberated slaves, prisoners, and soldiers. However, by 1837, the lack of skilled labor led to the recruitment of craftsmen, including stonemasons and masons from India.\n" +
                                "<br><br>" +
                                "The fort's main objectives were to quell any local uprisings and protect the port from any possible invasions. It was named in honor of Queen Adelaide, wife of King William IV, and the entrance still bears her initials and crown. Although it was never used for military or policing purposes, Fort Adelaide remains one of the few surviving structures from the transitional period between the abolition of slavery and the arrival of indentured laborers.\n" +
                                "<br><br>" +
                                "Unlike other forts from the British era, such as Fort William and Fort Victoria, which have deteriorated over time, Fort Adelaide remains intact. Built from black basalt stones, the fort is a testament to the craftsmanship of the era. Completed in 1840, it marks the beginning of Indian immigration and the end of slavery, symbolized by the people involved in its construction and the historical context of its creation.\n";
                        model.title1 = "Historical Context\n";
                        model.image1 = "";
                        model.text2 = "In the 1830s, tensions were high in Mauritius, which had just been placed under British control. The French Revolution of 1830 exacerbated the unhappiness of the people of France over the loss of authority and the abolition of the slave trade. With a population comprising whites, people of color, and former slaves, British authorities feared both foreign attacks and uprisings. Fort Adelaide was strategically built on a hill overlooking Port Louis to safeguard the city, the port, and the surrounding mountains.\n" +
                                "<br><br>" +
                                "However, by the time the fort was completed, the political situation on the island had stabilized, and its original military purpose became redundant. Instead, it was repurposed for ceremonial activities, such as signaling the arrival of important figures or daily cannon fire.\n";
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
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 5) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "le_morne_beach_2" + ".jpg";
                        model.text1 = "Le Morne Beach, located on a peninsula along the western coast of Mauritius, sits at the foot of the UNESCO-listed Le Morne Brabant mountain. With easy access from the coastal road, this public beach stretches for miles with soft white sand and shady casuarina trees. The crystal-clear water provides excellent visibility, making it a perfect spot for scuba diving.\n";
                        model.title1 = "";
                        model.image1 = fullPath + "/" + "kite_surfing" + ".jpg";
                        model.text2 = "Le Morne Beach, renowned for being one of the best places in the world to kite surf, has an amazing view of the famous mountain in the distance. The beach is also a favorite among windsurfers and kite surfers due to the ideal conditions that last throughout much of the year. Le Morne Beach hosts several competitions, including the Kiteival, which draws participants from around the globe.\n" +
                                "<br><br>" +
                                "Though great for swimming, Le Morne Beach can get quite crowded on weekends and holidays. Nudism is not allowed, but topless sunbathing is accepted. For a quieter visit, it's best to go early in the morning from 8:00 to 11:00 or in the afternoon between 1:00 and 5:00.\n";
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
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 6) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "brabant_4" + ".jpg";
                        model.text1 = "<b>Le Morne Brabant,</b> a UNESCO World Heritage Site since 2008, is a mountain located on the southwestern tip of Mauritius, jutting into the Indian Ocean. This culturally significant site served as a refuge for runaway slaves, known as <b>maroons,</b> from the 18th century until the early 1900s.";
                        model.title1 = "The Mountain's Natural Defense\n";
                        model.image1 = "";
                        model.text2 = "The mountain, with its vertical cliffs, steep slopes, and deep ravines, provided a natural fortress for the maroons seeking refuge from their pursuers. At its summit, a relatively flat plateau offered sanctuary to those who successfully navigated the dangerous ascent. The <b>V-Gap,</b> a wide gorge, served as the crucial entry point to the plateau, making access difficult and treacherous for both the maroons and those who sought to capture them.\n" +
                                "<br><br>" +
                                "Archaeological findings in the caves on Le Morne revealed ashy deposits from fires and the remains of a 300-year-old sheep, further confirming that maroons inhabited the summit, utilizing the resources they could secure to survive.\n";
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
                        model.text11 = "A striking sculpture by Haitian artist <b>Fritz Laratte</b> embodies the theme of liberation from slavery, depicting a slave whose hands are freed from chains through prayer, symbolizing the resilience and hope of the maroons.\n" +
                                "<br><br>" +
                                "<b><span style=\"color:black; font-weight:bold;\">Historical and Cultural Elements of Le Morne\n</span></b>";
                        model.image5 = fullPath + "/" + "brabant_7" + ".jpg";
                        model.text12 = "The cultural landscape of Le Morne extends beyond the mountain itself. <b>Trou-Chenille village,</b> with its rich history and heritage, includes five traditional huts preserved as part of an open-air museum. These huts portray daily life in the region during the 19th and 20th centuries, showcasing the simple yet meaningful existence of the local people.\n" +
                                "<br><br>" +
                                "<b><span style=\"color:black; font-weight:bold;\">Archaeological Discoveries</span></b>";

                        model.image6 = fullPath + "/" + "brabant_10" + ".jpg";
                        model.text13 = "Archaeological evidence has unearthed the remnants of a 19th-20th century settlement called <b>Macaque,</b> likely linked to Malagasy and Mozambican families. An abandoned cemetery, discovered at the foot of Le Morne, is believed to have been used by individuals from these regions. These findings provide insight into the multi-ethnic composition of the maroons and their descendants.\n" +
                                "<br><br>" +
                                "<b><span style=\"color:black; font-weight:bold;\">Cultural Traditions and Community\n</span></b>" +
                                "<ul>" +
                                "\t<li><b>Stella Maris Chapel:</b> The first Catholic chapel in Le Morne, originally built in <b>1891,</b> was moved to <b>L’Embrasure</b> during World War I and rebuilt in <b>1987</b> after being destroyed by a cyclone.\n" +
                                "\t<li><b>Sega Nights:</b> A weekly gathering where locals shared stories and performed Sega music beneath the ancient <b>Sega Tree</b> (Banyan Tree), using traditional instruments.\n" +
                                "\t<li><b>Fishing:</b> Fishing has been a key part of local culture since the 18th century. Villagers employ traditional methods like <b>Seine fishing</b> and <b>Kazie</b> (basket trap) fishing, which continue to be passed down through generations.\n" +
                                "</ul>" +
                                "<b><span style=\"color:black; font-weight:bold;\">Historical Landmarks and Daily Life</span></b>";
                        model.image7 = fullPath + "/" + "brabant_11" + ".jpg";
                        model.text14 = "<ul>" +
                                "\t<li><b>Limekiln:</b> Constructed by the Cambier family, this <b>20th-century limekiln</b> involved villagers in the production of quick lime by burning corals and shells.\n</li>" +
                                "\t<li><b>Grilled Coffee:</b> The distinct aroma of grilled coffee is a source of local pride. Beans from <b>Chamarel</b> are roasted in a cast iron pot over a fire and then ground using a mortar and pestle.\n</li>" +
                                "\t<li><b>Ilot Fourneau:</b> Villagers would travel by boat to this nearby island to collect fresh water from a spring. Historical records show that <b>Ilot Fourneau</b> was used as a British military post during the 18th and 19th centuries.\n</li>" +
                                "</ul>" +
                                "Le Morne Brabant stands as a deeply symbolic and culturally rich landscape, representing the history of resistance, survival, and liberation. It remains a sacred site for many, particularly the <b>Rastafarian community,</b> who view the mountain as a place of spiritual connection and meditation.\n";
                        model.image8 = "";
                        model.text15 = "";

                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 7) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "maconde_1" + ".jpg";
                        model.text1 = "Maconde Viewpoint is perched along the southern coast of Baie du Cap, a quiet village known for its rugged beauty and untouched coastlines. The viewpoint sits atop a small rocky cliff on a curved section of the coastal road. From here, you'll see an incredible landscape with rich red earth, lush green forests, rows of palm trees, and the sparkling blue waters of the Indian Ocean. It's a view that truly captures the heart.\n";
                        model.title1 = "";
                        model.image1 = fullPath + "/" + "maconde_2" + ".jpg";
                        model.text2 = "There's an interesting story behind the name \"Maconde.\" Some say it dates back to the days of slavery when runaway slaves from Mozambique’s Makonde tribe found safety in this area. Others believe it's named after Governor Jean Baptiste Henri Conde, who built an outlook on the cliff.\n" +
                                "<br><br>" +
                                "Getting to this area wasn’t always easy. The first road was only built in the 1920s, and the rough terrain and low-lying coast made construction tough. Recent updates have improved safety, but the drive along the winding basalt cliffs, with the sound of waves crashing against the rocks, is still as mesmerizing as ever. It’s a favorite spot for people who love watching the powerful ocean swells.\n" +
                                "<br><br>" +
                                "To reach the viewpoint itself, you'll need to climb a narrow set of stairs. At the top, you’ll be rewarded with stunning views of the ocean, the coastal village nearby, and the sight of local fishermen along the shore.\n";
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
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 8) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "chamarel_2" + ".jpg";
                        model.text1 = "The Seven Coloured Earths are a must-see geological wonder located in the Chamarel plain of the Rivière Noire District in southwest Mauritius. This natural phenomenon features small sand dunes that show off seven different colors—red, brown, violet, green, blue, purple, and yellow. What makes this spot so unique is the way the colors naturally settle in separate layers, forming striped patterns across the hills. Over time, the rain has carved these colorful dunes into fascinating shapes that resemble the texture of meringue.\n" +
                                "<br><br>" +
                                "These sands come from volcanic rock that broke down into clay, eventually turning into soil through a process called hydrolysis. The red and anthracite shades come from iron, while aluminum gives the sands their blue and purplish tones. Scientists believe the colors are linked to how the volcanic rock cooled at different rates, but they still don’t fully understand how the layers form so consistently.\n" +
                                "<br><br>" +
                                "The name \"Seven Coloured Earths\" isn’t the official title, but a simple description of what you'll see. Sometimes it’s called \"Chamarel Seven Coloured Earths\" or \"Terres des Sept Couleurs\" in French, but whatever the name, the sight is unforgettable.\n" +
                                "<br><br>" +
                                "This fascinating phenomenon can even be recreated on a small scale. If you mix the differently colored sands together, they'll separate over time, forming layers just like in nature.\n" +
                                "<br><br>" +
                                "Since the 1960s, the Seven Coloured Earths have become a top tourist attraction in Mauritius. Today, the dunes are protected by a wooden fence, and visitors can no longer walk on them. Instead, you can take in the breathtaking view from observation posts along the fence. And if you want a little souvenir, local shops sell little test tubes filled with colored sand.\n";
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
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 9) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "chamarel_1" + ".jpg";
                        model.text1 = "Chamarel Waterfall, the tallest single-drop waterfall in Mauritius, stands at about 100 meters high and is a sight to behold. Surrounded by lush greenery, this natural wonder is powered by three streams that merge and flow into the Saint Denis River, creating a powerful cascade. At its peak, the waterfall flows at over 40,000 cubic meters per minute.\n" +
                                "<br><br>" +
                                "As you drive along the 3-kilometer road through the Seven-Coloured Earth Geopark, you'll pass the viewpoint for Chamarel Waterfall. You can reach the ideal location to witness this magnificent waterfall by taking a brief hike through dense forest.\n" +
                                "<br><br>" +
                                "The waterfall pours over a basalt cliff into an oval pool below, then winds through a 6-kilometer canyon lined with tropical forests before eventually reaching Baie du Cap. It's a living reminder of Mauritius' volcanic past, with the landscape shaped by lava flows from two different periods. The bottom layer of basalt is 10 to 8 million years old, while the top layer dates from 3.5 to 1.7 million years ago.\n" +
                                "<br><br>" +
                                "For the adventurous, there's a three-hour trek through the hidden valley leading to the base of the waterfall. Once there, you can take a dip in the cool waters and feel the refreshing spray of the waterfall above.\n" +
                                "<br><br>" +
                                "The combination of the sights and sounds creates a true tropical rainforest experience, leaving you with memories of this amazing natural beauty.\n";
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
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 10) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "black_river_georges_2" + ".jpg";
                        model.text1 = "Black River Gorges National Park is located in the hilly southwestern part of Mauritius. Established on June 15, 1994, and managed by the National Parks and Conservation Service, the park covers an area of 67.54 square kilometers. It features different habitats like humid upland forests, drier lowland forests, and marshy heathlands. Visitors can explore two information centers, picnic areas, and 60 kilometers of hiking trails. There are also four research stations focused on conservation efforts, run by the National Parks and Conservation Service along with the Mauritian Wildlife Foundation.\n" +
                                "<br><br>" +
                                "The park was created to protect what remains of the island's rainforest. Over time, non-native plants like Chinese guava and privet, along with animals such as the rusa deer and wild pigs, have damaged parts of the forest. To protect native species, some areas of the park have been fenced off, and efforts are being made to control the invasive species.\n";
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
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 11) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "grand_bassin_1" + ".jpg";
                        model.text1 = "Grand Bassin, also known as Ganga Talao, is a sacred lake located in the quiet mountain area of Savanne, sitting about 550 meters above sea level. In 1898, Pandit Giri Gossayne, a native of Triolet village, made the first known visit to Ganga Talao. Since then, Hindus have come to regard this place as one of Mauritius' most sacred sites.\n" +
                                "<br><br>" +
                                "On the bank of the lake stands the Shiv Mandir, a temple dedicated to Lord Shiva. Every year, during the Maha Shivaratri festival, around half a million Hindus in Mauritius make a pilgrimage to the lake, with many walking barefoot from their homes, carrying Kanvars (decorated structures used to carry holy water and offerings).\n";
                        model.title1 = "";
                        model.image1 = fullPath + "/" + "grand_bassin_2" + ".jpg";
                        model.text2 = "The lake is symbolically linked to the holy Ganges River in India by its name, Ganga Talao, which means \"Lake of Ganga.\"\n";
                        model.title2 = "History";
                        model.text3 = "In 1866, Pandit Sanjibonlal returned to Mauritius after completing his first labor contract in India and transformed Grand Bassin into a pilgrimage site. He converted a building into a temple and brought a Shivalingam (symbol of Shiva) from India.\n" +
                                "<br><br>" +
                                "In 1897, priest Jhummon Giri Gosagne Napal had a dream where he saw that the waters of Grand Bassin were linked to the holy Ganges. The lake, previously called \"Pari Talao,\" was declared a sacred lake in 1998. In 1972, water from the Ganges River was mixed with the lake’s water, and the name Ganga Talao became official.\n";
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
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 12) {

                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "gris_gris_1" + ".jpg";
                        model.text1 = "The primary beach area features an expansive green field with a commanding view of dramatic cliffs and enormous waves. There are benches and a pavilion available for relaxation while taking in the scenery.\n" +
                                "To the left, a concrete staircase descends to the beach, but swimming is strongly discouraged due to the high danger level. The powerful waves can swiftly overwhelm swimmers. Instead, enjoy a leisurely stroll along the beach, heading toward a small cave at the far end.\n";
                        model.title1 = "Secret Caves at Gris Gris";
                        model.image1 = fullPath + "/" + "grisgris_2" + ".jpg";
                        model.text2 = "In addition to the cave on the far left side of the beach, two other hidden caves can be discovered at Gris Gris. These are more challenging to reach, involving a descent down a cliff and walking through the water.\n" +
                                "Caution is advised against going all the way down, as water levels can fluctuate unpredictably, and the current is often too strong.\n" +
                                "<br><br>" +
                                "For those eager to explore the secret caves at Gris Gris, head towards the cliff's edge directly across from the parking lot. Upon reaching the spot, descend only about halfway to catch a glimpse of the caves on your right.\n" +
                                "<br><br>" +
                                "It's important to bear in mind that entering the caves could pose risks if the water level rises!\n" +
                                "Gris Gris beach is intricately connected to the village of Souillac, which relies heavily on tourism for its revenue. " +
                                "<br><br>" +
                                "Established 200 years ago as a port for ships sailing from Europe to India, Souillac has a rich history worth exploring. Plan your day strategically to make the most of your visit to the southern part of Mauritius, and consider including a visit to Rochester Falls, just outside the village, renowned for its distinctive rectangular-sided rocks.\n" + "The name \"Gris Gris\" adds an intriguing dimension to the experience. Upon entering the beach, a large sign displays the history behind the name. According to local tradition, \"Gris Gris\" is linked to the African amulet known as the “Gris Gris” and its association with the tumultuous coastline. However, the story takes an unexpected turn, suggesting that Gris Gris might have been the name of a puppy belonging to a French cartographer who visited the coast in 1753.<br>";

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
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    }
                }
                else if (Stash.getString("day").equals("day21")) {
                    if (position == 0) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "pamplemousse_garden" + ".jpg";
                        model.text1 = "The Sir Seewoosagur Ramgoolam Botanic Garden, also known as the Pamplemousses Botanic Garden, is a major tourist attraction located in Pamplemousses, near Port Louis, Mauritius. It is the oldest botanical garden in the Southern Hemisphere. Established in 1770 by Pierre Poivre, the garden spans about 37 hectares (91 acres) and is famous for its large pond filled with giant water lilies (Victoria amazonica).\n" +
                                "<br><br>" +
                                "Over the years, the garden has had several names that reflect its changing status and ownership. Some of these names include 'Jardin de Mon Plaisir,' 'Jardin des Plantes,' 'Le Jardin National de l’Ile de France,' 'Jardin Royal,' and 'Jardin Botanique des Pamplemousses.' On September 17, 1988, it was officially named the \"Sir Seewoosagur Ramgoolam Botanic Garden\" to honor the first prime minister of Mauritius.\n";
                        model.title1 = "";
                        model.image1 = fullPath + "/" + "botanical_garden_1" + ".jpg";
                        model.text2 = "In addition to the stunning giant water lilies, the garden is home to a variety of spices, ebonies, sugar canes, and 85 different types of palm trees from regions including Central America, Asia, Africa, and the Indian Ocean islands. Many notable people, such as Princess Margaret, Indira Gandhi, and François Mitterrand, have planted trees in the garden.\n" +
                                "<br><br>" +
                                "Located about seven miles northeast of Port Louis, the garden has a rich history that dates back to 1729 when it was set aside for colonist P. Barmont. It changed hands several times, with different owners contributing to its growth and development. Today, the garden covers approximately 25,110 hectares (62,040 acres), with part of the area serving as an experimental station.\n";
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
                        model.text9 = "Over the years, the garden faced times of neglect and challenges, but directors like James Duncan worked hard to restore and improve it. In 1866, during a malaria outbreak, the garden played an important role by growing Eucalyptus trees to help fight the disease.\n" +
                                "<br><br>" +
                                "In 1913, the Department of Agriculture took over the garden, managing its growth and upkeep. Notably, after the death of Seewoosagur Ramgoolam in 1985, part of the garden was dedicated to a crematorium, marking the first time someone was cremated on its grounds.\n" +
                                "<br><br>" +
                                "Today, the Sir Seewoosagur Ramgoolam Botanic Garden is a symbol of Mauritius's botanical heritage, inviting visitors to explore its beautiful landscapes and rich history.\n";

                        model.title8 = "";
                        model.text10 = "";
                        model.image4 = "";
                        model.text11 = "";
                        model.image5 = "";
                        model.text12 = "";
                        /*model.title8 = "Chateau de Mon Plaisir";
                        model.text10 = "";
                        model.image4 = fullPath + "/" + "botanical_garden_4" + ".jpg";
                        model.text11 = "Until 1839, the Chateau de Mon Plaisir was a simple building with a flat roof and circular verandahs. The current single-story structure, built by the English in the mid-19th century, is now a National Monument, which means it is legally protected. Visitors can enjoy a lovely view of the Moka Range and the Peak of Pieter Both from the Chateau, making it a beautiful spot to take in the scenery.\n";
                        model.image5 = "";
                        model.text12 = "";*/

                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 1) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
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
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 2) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "port_louis_3" + ".jpg";
                        model.text1 = "Le Caudan Waterfront is a vibrant commercial hub in Port Louis, Mauritius, featuring an array of amenities such as shops, banks, casinos, cinemas, restaurants, a marina, and the luxurious Le Labourdonnais hotel.\n";
                        model.title1 = "Historical Roots\n";
                        model.image1 = "";
                        model.text2 = "The name \"Le Caudan Waterfront\" honors Jean Dominique Michel de Caudan, who arrived in Mauritius in 1726 and established a saltpan near what is now the Robert Edward Hart Garden. This peninsula, formed around a fossil coral islet, has evolved over 250 years, once housing a powder magazine, observatories, and warehouses, with the sugar industry shaping much of its history until the Bulk Sugar Terminal opened in 1980.\n";
                        model.title2 = "Historically Significant Spots\n";
                        model.text3 = "Notable historical locations in Le Caudan include the Blue Penny Museum, which is situated in the Food Court, and the first meteorological observatory in the Indian Ocean, which was formerly a docks office. Different areas within the complex are named to reflect the island's colonial history:\n" +
                                "<br><br><ul>" +
                                "<li><b>Barkly Wharf:</b> Named after Sir Henry Barkly, Governor of Mauritius (1863-1870).\n</li>" +
                                "<li><b>Le Pavillon Wing:</b> Linked to Pavillon Street from an old map of Port Louis.\n</li>" +
                                "<li><b>Dias Pier:</b> Honors Diogo Dias, the navigator believed to have first charted the Mascarene Islands.\n</li>" +
                                "</ul>" +
                                "Le Caudan Waterfront combines modernity with rich historical significance, making it a must-visit destination.\n";
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
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 3) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "aapravasi_ghat_1" + ".jpg";
                        model.text1 = "The Aapravasi Ghat is a historical site located in Port Louis, the capital city of Mauritius. It holds great significance in the history of the island and is recognized as a UNESCO World Heritage Site. The history of the Aapravasi Ghat is closely tied to the indentured labor system that shaped the social and economic landscape of Mauritius.\n" +
                                "<br><br>\n" +
                                "Here is a brief history of the Aapravasi Ghat:\n" +
                                "\n<br><br>" +
                                "<b>Indentured Labor System:</b> In the 19th century, after the abolition of slavery, the British turned to the system of indentured labor to meet the demand for cheap and abundant workforce for their colonies. Indentured laborers were recruited from various parts of India, as well as China and Africa, to work on plantations in Mauritius and other British colonies.\n" +
                                "\n<br><br>" +
                                "<b>Establishment of Aapravasi Ghat:</b> The Aapravasi Ghat was established in 1849 as the first site for the reception of indentured laborers in Mauritius. The name \"Aapravasi Ghat\" translates to \"immigration depot\" in Hindi. The location served as a processing center where indentured laborers arriving by ship were registered, housed temporarily, and assigned to various plantations across the island.\n" +
                                "\n<br><br>" +
                                "<b>Housing:</b> At the Aapravasi Ghat, indentured laborers had to endure substandard housing. They were often crowded into cramped barracks, and the site became a place where many experienced suffering, sickness, and death. The conditions were challenging, and the laborers endured a difficult transition from their home countries to the unfamiliar environment of Mauritius.\n" +
                                "\n<br><br>" +
                                "<b>End of Indenture:</b> The indenture system continued until the early 20th century when it was officially abolished. The Aapravasi Ghat continued to be used for processing immigrants until 1920. The location underwent many changes over the years that served various functions after the indenture system was discontinued.\n" +
                                "\n<br><br>" +
                                "<b>UNESCO World Heritage Site:</b> In 2006, the Aapravasi Ghat was designated as a UNESCO World Heritage Site in recognition of its historical importance in the global migration of indentured laborers and its impact on the multicultural identity of Mauritius.\n";
                        model.title1 = "";
                        model.image1 = fullPath + "/" + "aapravasi_ghat_2" + ".jpg";
                        model.text2 = "Today, the Aapravasi Ghat stands as a poignant symbol of the struggles and resilience of the indentured laborers who played a crucial role in shaping Mauritius's cultural and social fabric. \n" +
                                "<br><br>\n" +
                                "This UNESCO World Heritage site is open to visitors, offering them a chance to explore its rich history and gain insights into this critical period of the island's past. The exhibits and memorials here provide a deeper understanding of the lives and contributions of these laborers, making it a significant destination for those interested in Mauritian heritage.\n";
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
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 4) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "blue_penny_museum_2" + ".jpg";
                        model.text1 = "The Blue Penny Museum, inaugurated in November 2001, stands as a significant cultural institution dedicated to showcasing the rich history and art of Mauritius. Here are some key highlights:\n";
                        model.title1 = "";
                        model.image1 = "";
                        model.text2 = "<b>Historic Stamps:</b> The museum's name derives from the famous 1847 Blue Penny and Red Penny stamps, which are considered some of the rarest and most valuable stamps in the world. In 1993, a consortium of Mauritian enterprises, led by The Mauritius Commercial Bank, purchased these stamps for an astounding $2 million. This marked a momentous occasion as the stamps were repatriated to Mauritius after nearly 150 years of absence.\n" +
                                "<br><br>" +
                                "<b>Cultural Significance:</b> The Blue Penny Museum was established to preserve and promote the island's artistic and historical heritage. It serves not only as a display space for these iconic stamps but also as a hub for exploring various aspects of Mauritian culture, including its diverse influences and unique identity.\n" +
                                "<br><br>" +
                                "<b>Conservation Practices:</b> Given the extreme rarity and immense value of the Blue Penny stamps, meticulous conservation efforts are in place. The originals are illuminated only temporarily to minimize the risk of damage from prolonged exposure to light. For most visitors, replicas are displayed, ensuring that the original stamps remain safe for future generations to appreciate.\n" +
                                "<br><br>" +
                                "<b>Founder - The Mauritius Commercial Bank:</b> The Blue Penny Museum was established by The Mauritius Commercial Bank, a prominent financial institution in Mauritius known for its longstanding service and community engagement. \n";
                        model.title2 = "";
                        model.text3 = "";
                        model.image2 = fullPath + "/" + "blue_penny_museum_1" + ".jpg";
                        model.text4 = "The bank is proving through this project that it is dedicated to the preservation of the island's rich cultural legacy in addition to its financial success. The bank's goal in opening the museum is to increase pride and knowledge of Mauritius's historical significance among locals and visitors alike, particularly in light of the island's unique philatelic treasures.\n";
                        model.title3 = "Additional Artifacts\n";
                        model.text5 = "In addition to the renowned Blue Penny stamps, the museum features the original statue of Paul and Virginia, crafted by Prosper d'Épinay in 1881. This piece enhances the museum's cultural and artistic collection, drawing interest from both locals and tourists eager to explore Mauritius's history.\n";
                        model.title4 = "Museum Structure\n";
                        model.text6 = "The museum spans two floors, each with unique exhibits:\n" +
                                "<br><br>" +
                                "<b><span style=\"color:black; font-weight:bold;\">Ground Floor\n</span></b>" +
                                "<br><br><ul>" +
                                "<b>Souvenir Shop:</b> Visitors are greeted by a souvenir shop, offering mementos related to the museum and Mauritius.\n" +
                                "<br><br>" +
                                "<b>Temporary Exhibition Room:</b> This space hosts rotating exhibits, ensuring diverse and dynamic displays that change over time.\n" +
                                "</ul>" +
                                "<br><br>" +
                                "<b><span style=\"color:black; font-weight:bold;\">First Floor\n</span></b>" +
                                "<br><br><ul>" +
                                "<li><b>The Age of Discovery:</b> This exhibition narrates the stories of pioneers who sailed to the Mascarene Islands, featuring ancient maps that document their journeys.\n</li>" +
                                "<li><b>The Island Builders:</b> This room covers three critical periods in Mauritius's history: the Dutch, French, and British eras, providing a comprehensive overview of the island's evolution.\n</li>" +
                                "<li><b>Port Louis:</b> Focused on the origins and history of Port Louis, this exhibition offers insights into the city's development.\n</li>" +
                                "<li><b>The Postal Adventure:</b> Dedicated to Mauritius's postal history, this room displays two stamps from the Blue Penny collection, highlighting the island's philatelic heritage.\n</li>" +
                                "<li><b>Engraved Memory:</b> Honoring Joseph Osmond Barnard, the first engraver of stamps in Mauritius, this room serves as a tribute to his contributions to the field.\n</li>" +
                                "</ul>" +
                                "Overall, the Blue Penny Museum is thoughtfully organized to provide visitors with a captivating journey through the history, art, and unique cultural heritage of Mauritius.\n";
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
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 5) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "black_river_georges_2" + ".jpg";
                        model.text1 = "Black River Gorges National Park is located in the hilly southwestern part of Mauritius. Established on June 15, 1994, and managed by the National Parks and Conservation Service, the park covers an area of 67.54 square kilometers. It features different habitats like humid upland forests, drier lowland forests, and marshy heathlands. Visitors can explore two information centers, picnic areas, and 60 kilometers of hiking trails. There are also four research stations focused on conservation efforts, run by the National Parks and Conservation Service along with the Mauritian Wildlife Foundation.\n" +
                                "<br><br>" +
                                "The park was created to protect what remains of the island's rainforest. Over time, non-native plants like Chinese guava and privet, along with animals such as the rusa deer and wild pigs, have damaged parts of the forest. To protect native species, some areas of the park have been fenced off, and efforts are being made to control the invasive species.\n";
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
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 6) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "chamarel_2" + ".jpg";
                        model.text1 = "The Seven Coloured Earths are a must-see geological wonder located in the Chamarel plain of the Rivière Noire District in southwest Mauritius. This natural phenomenon features small sand dunes that show off seven different colors—red, brown, violet, green, blue, purple, and yellow. What makes this spot so unique is the way the colors naturally settle in separate layers, forming striped patterns across the hills. Over time, the rain has carved these colorful dunes into fascinating shapes that resemble the texture of meringue.\n" +
                                "<br><br>" +
                                "These sands come from volcanic rock that broke down into clay, eventually turning into soil through a process called hydrolysis. The red and anthracite shades come from iron, while aluminum gives the sands their blue and purplish tones. Scientists believe the colors are linked to how the volcanic rock cooled at different rates, but they still don’t fully understand how the layers form so consistently.\n" +
                                "<br><br>" +
                                "The name \"Seven Coloured Earths\" isn’t the official title, but a simple description of what you'll see. Sometimes it’s called \"Chamarel Seven Coloured Earths\" or \"Terres des Sept Couleurs\" in French, but whatever the name, the sight is unforgettable.\n" +
                                "<br><br>" +
                                "This fascinating phenomenon can even be recreated on a small scale. If you mix the differently colored sands together, they'll separate over time, forming layers just like in nature.\n" +
                                "<br><br>" +
                                "Since the 1960s, the Seven Coloured Earths have become a top tourist attraction in Mauritius. Today, the dunes are protected by a wooden fence, and visitors can no longer walk on them. Instead, you can take in the breathtaking view from observation posts along the fence. And if you want a little souvenir, local shops sell little test tubes filled with colored sand.\n";
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
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    }

                }
                else if (Stash.getString("day").equals("day22")) {
                    if (position == 0) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "trou_aux_cerfs_4" + ".jpg";
                        model.text1 = "Trou aux Cerfs is a dormant volcanic crater surrounded by thick forest, filled with native plants and towering pine trees. Though it hasn’t erupted in about 700,000 years, scientists believe it could potentially become active again one day.\n" +
                                "<br><br>" +
                                "One of the best things about Trou aux Cerfs is the amazing 360-degree view. The town of Curepipe and the surrounding mountains, including Rempart Mountain, Trois Mamelles, and the Port Louis-Moka range, are visible from the summit. It’s also a popular spot for locals, especially joggers who gather around 5 a.m. every morning. There’s a gazebo where you can sit, relax, and enjoy the peaceful surroundings.\n" +
                                "<br><br>" +
                                "Since it’s located at a higher elevation and surrounded by trees, the area can get a little chilly, so it’s a good idea to bring a light sweater or shawl. You can access Trou aux Cerfs from three main roads: La Hausse de la Louviere, Edgar Huges Road, and Crater Lane, and there’s parking available nearby.\n";
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
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 1) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "grand_bassin_1" + ".jpg";
                        model.text1 = "Grand Bassin, also known as Ganga Talao, is a sacred lake located in the quiet mountain area of Savanne, sitting about 550 meters above sea level. In 1898, Pandit Giri Gossayne, a native of Triolet village, made the first known visit to Ganga Talao. Since then, Hindus have come to regard this place as one of Mauritius' most sacred sites.\n" +
                                "<br><br>" +
                                "On the bank of the lake stands the Shiv Mandir, a temple dedicated to Lord Shiva. Every year, during the Maha Shivaratri festival, around half a million Hindus in Mauritius make a pilgrimage to the lake, with many walking barefoot from their homes, carrying Kanvars (decorated structures used to carry holy water and offerings).\n";
                        model.title1 = "";
                        model.image1 = fullPath + "/" + "grand_bassin_2" + ".jpg";
                        model.text2 = "The lake is symbolically linked to the holy Ganges River in India by its name, Ganga Talao, which means \"Lake of Ganga.\"\n";
                        model.title2 = "History";
                        model.text3 = "In 1866, Pandit Sanjibonlal returned to Mauritius after completing his first labor contract in India and transformed Grand Bassin into a pilgrimage site. He converted a building into a temple and brought a Shivalingam (symbol of Shiva) from India.\n" +
                                "<br><br>" +
                                "In 1897, priest Jhummon Giri Gosagne Napal had a dream where he saw that the waters of Grand Bassin were linked to the holy Ganges. The lake, previously called \"Pari Talao,\" was declared a sacred lake in 1998. In 1972, water from the Ganges River was mixed with the lake’s water, and the name Ganga Talao became official.\n";
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
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 2) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "bois_cheri_1" + ".jpg";
                        model.text1 = "Established in 1892, the Bois Chéri Plantation is the largest tea producer in Mauritius, covering more than 250 hectares. The plantation has a factory and museum where guests can learn about the origins and manufacturing of tea.\n" +
                                "<br><br>" +
                                "During your visit, you can join a guided tour that takes you through the factory and museum, as well as the tea fields, and enjoy a tea-tasting session. The guides will explain the history of Bois Chéri and share the importance of tea production in Mauritius.\n";
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
                        model.text9 = "After the tour, head to the factory café near the restaurant for a tea-tasting experience. You’ll be able to try a variety of teas and pair them with treats like waffles or pancakes.\n" +
                                "<br><br>" +
                                "The Bois Chéri restaurant is just a short walk away, offering breathtaking views of the south coast. The restaurant serves unique dishes with a local twist, such as shrimp with green tea and chicken with exotic tea. Whether you’re with friends or family, the tour, tea tasting, and meal make for a delightful and relaxing experience.\n";
                        model.title8 = "";
                        model.text10 = "";
                        model.image4 = fullPath + "/" + "bois_cheri_4" + ".jpg";
//
                        model.text11 = "<br><b><span style=\"color:black; font-weight:bold;\">Visitor Information:</span></b>\n" +
                                "<br><br>" +
                                "<b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;• Visit Hours:</b>" +
                                "<br>" +
                                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;• Monday to Friday: 9:00 AM – 2:00 PM\n" +
                                "<br>" +
                                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;• Saturday: 9:00 AM – 11:00 AM\n" +
                                "<br>" +
                                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;• Closed on Sundays and public holidays\n" +
                                "<br>" +
                                "<b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;• Bois Chéri Tea Factory Operating Hours:</b>" +
                                "<br>" +
                                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;• Saturday: 9:00 AM – 02:00 PM\n" +
                                "<br>" +
                                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;• Saturday: 9:00 AM – 11:00 AM\n";

                        model.image5 = "";
                        model.text12 = "";
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    }
//                    TODO change
                    else if (position == 3) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "la_vanilla_1" + ".jpg";

                        model.text1 = "La Vanille Nature Park lets you experience the native plants and animals of Mauritius and nearby islands. As you walk through the park, you’ll see Nile crocodiles, tenrecs, iguanas, and turtles from Madagascar and the Seychelles, all under the shade of giant bamboo and palm trees.\n" +
                                "<br><br>" +
                                "The park also features a fascinating collection of fossils, including the extinct dodo and Madagascar elephant bird, which once roamed the area. There’s even an insectarium with over 23,000 species from around the globe, offering a chance to learn more about the insect world.\n";
                        model.title1 = "";
                        model.image1 = fullPath + "/" + "la_vanille_4" + ".jpg";
                        model.text2 = "One of the main attractions at La Vanille is the Crocodile Park, which houses more than 2,000 Nile crocodiles in different stages of growth. Visitors can even hold baby crocodiles for a photo! You can also catch a free crocodile feeding show every day at 11:30 a.m. It's a great spot to learn about this ancient reptile and see how it has adapted through time.\n";
                        model.title2 = "";
                        model.text3 = "";
                        model.image2 = fullPath + "/" + "la_vanille_9" + ".jpg";
                        model.text4 = "Thanks to a unique breeding program, La Vanille is home to the world's largest artificial habitat for Aldabra tortoises, with over 1,000 of them. You can get up close, feed them, and enjoy their gentle company.\n" +
                                "<br><br>" +
                                "The park is a fantastic place to connect with nature, with well-kept paths and serene surroundings. The forest is alive with unique wildlife, and you might even spot giant tortoises or friendly deer on your walk. For those who love photography, the park offers countless opportunities to capture amazing shots of the animals and landscape.\n";
                        model.title3 = "";
                        model.text5 = "";
                        model.title4 = "";
                        model.text6 = "";
                        model.title5 = "";
                        model.text7 = "";
                        model.title6 = "";
                        model.text8 = "";
                        model.title7 = "";
                        model.image3 = fullPath + "/" + "la_vanille_6" + ".jpg";
                        model.text9 = "La Vanille Nature Park also runs programs that help preserve nature, including planting over 350,000 native trees and breeding endangered animals. After your visit, stop by the Crocodile Park Shop for handcrafted souvenirs and books about wildlife.\n" +
                                "<br><br>" +
                                "To wrap up your day, relax with a meal at the Crocodile Park Restaurant, nestled in the forest. The restaurant offers meals for both kids and adults, with one standout dish—crocodile meat, served at Le Crocodile Affamé. Those who try it often rave about how good it is!\n";
                        model.title8 = "";
                        model.text10 = "";
                        model.image4 = fullPath + "/" + "la_vanilla_8" + ".jpg";
                        model.text11 = "Opening hours: Every day from 8:30 a.m. to 5:00 p.m.\n";
                        model.image5 = "";
                        model.text12 = "";

                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);

                    } else if (position == 4) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "gris_gris_1" + ".jpg";
                        model.text1 = "The primary beach area features an expansive green field with a commanding view of dramatic cliffs and enormous waves. There are benches and a pavilion available for relaxation while taking in the scenery.<br>" +
                                "To the left, a concrete staircase descends to the beach, but swimming is strongly discouraged due to the high danger level. The powerful waves can swiftly overwhelm swimmers. Instead, enjoy a leisurely stroll along the beach, heading toward a small cave at the far end.";
                        model.title1 = "Caves at Gris Gris";
                        model.image1 = fullPath + "/" + "grisgris_2" + ".jpg";
                        model.text2 = "In addition to the cave on the far left side of the beach, two other hidden caves can be discovered at Gris Gris. These are more challenging to reach, involving a descent down a cliff and walking through the water.<br>" +
                                "Caution is advised against going all the way down, as water levels can fluctuate unpredictably, and the current is often too strong." +
                                "<br><br>" +
                                "For those eager to explore the secret caves at Gris Gris, head towards the cliff's edge directly across from the parking lot. Upon reaching the spot, descend only about halfway to catch a glimpse of the caves on your right." +
                                "<br><br>" +
                                "It's important to bear in mind that entering the caves could pose risks if the water level rises!<br>" +
                                "Gris Gris beach is intricately connected to the village of Souillac, which relies heavily on tourism for its revenue." +
                                "<br><br>" +
                                "Established 200 years ago as a port for ships sailing from Europe to India, Souillac has a rich history worth exploring. Plan your day strategically to make the most of your visit to the southern part of Mauritius, and consider including a visit to Rochester Falls, just outside the village, renowned for its distinctive rectangular-sided rocks.";

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
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 5) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "black_river_georges_2" + ".jpg";
                        model.text1 = "Black River Gorges National Park is located in the hilly southwestern part of Mauritius. Established on June 15, 1994, and managed by the National Parks and Conservation Service, the park covers an area of 67.54 square kilometers. It features different habitats like humid upland forests, drier lowland forests, and marshy heathlands. Visitors can explore two information centers, picnic areas, and 60 kilometers of hiking trails. There are also four research stations focused on conservation efforts, run by the National Parks and Conservation Service along with the Mauritian Wildlife Foundation.\n" +
                                "<br><br>" +
                                "The park was created to protect what remains of the island's rainforest. Over time, non-native plants like Chinese guava and privet, along with animals such as the rusa deer and wild pigs, have damaged parts of the forest. To protect native species, some areas of the park have been fenced off, and efforts are being made to control the invasive species.\n";
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
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 6) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "chamarel_2" + ".jpg";
                        model.text1 = "The Seven Coloured Earths are a must-see geological wonder located in the Chamarel plain of the Rivière Noire District in southwest Mauritius. This natural phenomenon features small sand dunes that show off seven different colors—red, brown, violet, green, blue, purple, and yellow. What makes this spot so unique is the way the colors naturally settle in separate layers, forming striped patterns across the hills. Over time, the rain has carved these colorful dunes into fascinating shapes that resemble the texture of meringue.\n" +
                                "<br><br>" +
                                "These sands come from volcanic rock that broke down into clay, eventually turning into soil through a process called hydrolysis. The red and anthracite shades come from iron, while aluminum gives the sands their blue and purplish tones. Scientists believe the colors are linked to how the volcanic rock cooled at different rates, but they still don’t fully understand how the layers form so consistently.\n" +
                                "<br><br>" +
                                "The name \"Seven Coloured Earths\" isn’t the official title, but a simple description of what you'll see. Sometimes it’s called \"Chamarel Seven Coloured Earths\" or \"Terres des Sept Couleurs\" in French, but whatever the name, the sight is unforgettable.\n" +
                                "<br><br>" +
                                "This fascinating phenomenon can even be recreated on a small scale. If you mix the differently colored sands together, they'll separate over time, forming layers just like in nature.\n" +
                                "<br><br>" +
                                "Since the 1960s, the Seven Coloured Earths have become a top tourist attraction in Mauritius. Today, the dunes are protected by a wooden fence, and visitors can no longer walk on them. Instead, you can take in the breathtaking view from observation posts along the fence. And if you want a little souvenir, local shops sell little test tubes filled with colored sand.\n";
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
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    }

                } else if (Stash.getString("day").equals("day31")) {
                    if (position == 0) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "pamplemousse_garden" + ".jpg";
                        model.text1 = "The Sir Seewoosagur Ramgoolam Botanic Garden, also known as the Pamplemousses Botanic Garden, is a major tourist attraction located in Pamplemousses, near Port Louis, Mauritius. It is the oldest botanical garden in the Southern Hemisphere. Established in 1770 by Pierre Poivre, the garden spans about 37 hectares (91 acres) and is famous for its large pond filled with giant water lilies (Victoria amazonica).\n" +
                                "<br><br>" +
                                "Over the years, the garden has had several names that reflect its changing status and ownership. Some of these names include 'Jardin de Mon Plaisir,' 'Jardin des Plantes,' 'Le Jardin National de l’Ile de France,' 'Jardin Royal,' and 'Jardin Botanique des Pamplemousses.' On September 17, 1988, it was officially named the \"Sir Seewoosagur Ramgoolam Botanic Garden\" to honor the first prime minister of Mauritius.\n";
                        model.title1 = "";
                        model.image1 = fullPath + "/" + "botanical_garden_1" + ".jpg";
                        model.text2 = "In addition to the stunning giant water lilies, the garden is home to a variety of spices, ebonies, sugar canes, and 85 different types of palm trees from regions including Central America, Asia, Africa, and the Indian Ocean islands. Many notable people, such as Princess Margaret, Indira Gandhi, and François Mitterrand, have planted trees in the garden.\n" +
                                "<br><br>" +
                                "Located about seven miles northeast of Port Louis, the garden has a rich history that dates back to 1729 when it was set aside for colonist P. Barmont. It changed hands several times, with different owners contributing to its growth and development. Today, the garden covers approximately 25,110 hectares (62,040 acres), with part of the area serving as an experimental station.\n";
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
                        model.text9 = "Over the years, the garden faced times of neglect and challenges, but directors like James Duncan worked hard to restore and improve it. In 1866, during a malaria outbreak, the garden played an important role by growing Eucalyptus trees to help fight the disease.\n" +
                                "<br><br>" +
                                "In 1913, the Department of Agriculture took over the garden, managing its growth and upkeep. Notably, after the death of Seewoosagur Ramgoolam in 1985, part of the garden was dedicated to a crematorium, marking the first time someone was cremated on its grounds.\n" +
                                "<br><br>" +
                                "Today, the Sir Seewoosagur Ramgoolam Botanic Garden is a symbol of Mauritius's botanical heritage, inviting visitors to explore its beautiful landscapes and rich history.\n";
                        model.title8 = "Chateau de Mon Plaisir";
                        model.text10 = "";
                        model.image4 = fullPath + "/" + "botanical_garden_4" + ".jpg";
                        model.text11 = "Until 1839, the Chateau de Mon Plaisir was a simple building with a flat roof and circular verandahs. The current single-story structure, built by the English in the mid-19th century, is now a National Monument, which means it is legally protected. Visitors can enjoy a lovely view of the Moka Range and the Peak of Pieter Both from the Chateau, making it a beautiful spot to take in the scenery.\n";
                        model.image5 = "";
                        model.text12 = "";
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 1) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
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
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 2) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "chateau_de_labourdonnais" + ".jpg";
                        model.text1 = "A majestic lane lined with towering trees leads you to <b>Château Labourdonnais</b>, a stunning colonial mansion that once belonged to Christian Wiehe. Built in 1856 and completed a few years later, this mansion is often regarded as the most beautiful colonial house in Mauritius. \n";
                        model.title1 = "";
                        model.image1 = fullPath + "/" + "chateau_de_labourdonnais_4" + ".jpg";
                        model.text2 = "The grand estate features a spacious dining room, a vast pantry, an expansive living room, elegantly designed bedrooms, and a large exhibition hall, making it a captivating destination.\n";
                        model.title2 = "Gardens and Exploration\n";
                        model.text3 = "";
                        model.image2 = fullPath + "/" + "chateau_de_labourdonnais_2" + ".jpg";
                        model.text4 = "As you explore the grounds, you’ll be enchanted by the lush gardens, where century-old mango trees, spice trees, and exotic fruit trees abound. Adding to the charm, peaceful <b>Aldabra tortoises</b> graze around the gardens. You can also visit the <b>Rhumerie des Mascareignes</b>, established in 2006, to learn about traditional rum-making techniques, and enjoy a delightful tasting session that includes rum, fruit pastes, juices, and sorbets.\n" +
                                "<br><br>" +
                                "The visit concludes at the Tasting Bar, where you can sample products made from the estate’s orchards, such as jams, fruit jellies, and sorbets. Before you leave, don’t miss the opportunity to visit the boutique, which offers a range of local products like decorative items, rum, and spices. For a refined dining experience, visit <b>La Table du Château</b>, a restaurant offering Mauritian dishes made from fresh produce grown on the estate.\n";
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
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 3) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "port_louis_3" + ".jpg";
                        model.text1 = "Le Caudan Waterfront is a vibrant commercial hub in Port Louis, Mauritius, featuring an array of amenities such as shops, banks, casinos, cinemas, restaurants, a marina, and the luxurious Le Labourdonnais hotel.\n";
                        model.title1 = "Historical Roots\n";
                        model.image1 = "";
                        model.text2 = "The name \"Le Caudan Waterfront\" honors Jean Dominique Michel de Caudan, who arrived in Mauritius in 1726 and established a saltpan near what is now the Robert Edward Hart Garden. This peninsula, formed around a fossil coral islet, has evolved over 250 years, once housing a powder magazine, observatories, and warehouses, with the sugar industry shaping much of its history until the Bulk Sugar Terminal opened in 1980.\n";
                        model.title2 = "Historically Significant Spots\n";
                        model.text3 = "Notable historical locations in Le Caudan include the Blue Penny Museum, which is situated in the Food Court, and the first meteorological observatory in the Indian Ocean, which was formerly a docks office. Different areas within the complex are named to reflect the island's colonial history:\n" +
                                "<br><br><ul>" +
                                "<li><b>Barkly Wharf:</b> Named after Sir Henry Barkly, Governor of Mauritius (1863-1870).\n</li>" +
                                "<li><b>Le Pavillon Wing:</b> Linked to Pavillon Street from an old map of Port Louis.\n</li>" +
                                "<li><b>Dias Pier:</b> Honors Diogo Dias, the navigator believed to have first charted the Mascarene Islands.\n</li>" +
                                "</ul>" +
                                "Le Caudan Waterfront combines modernity with rich historical significance, making it a must-visit destination.\n";
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
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
//                        Toast.makeText(context, "data is not added", Toast.LENGTH_SHORT).show();
                    } else if (position == 4) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "aapravasi_ghat_1" + ".jpg";
                        model.text1 = "The Aapravasi Ghat is a historical site located in Port Louis, the capital city of Mauritius. It holds great significance in the history of the island and is recognized as a UNESCO World Heritage Site. The history of the Aapravasi Ghat is closely tied to the indentured labor system that shaped the social and economic landscape of Mauritius.\n" +
                                "<br><br>\n" +
                                "Here is a brief history of the Aapravasi Ghat:\n" +
                                "\n<br><br>" +
                                "<b>Indentured Labor System:</b> In the 19th century, after the abolition of slavery, the British turned to the system of indentured labor to meet the demand for cheap and abundant workforce for their colonies. Indentured laborers were recruited from various parts of India, as well as China and Africa, to work on plantations in Mauritius and other British colonies.\n" +
                                "\n<br><br>" +
                                "<b>Establishment of Aapravasi Ghat:</b> The Aapravasi Ghat was established in 1849 as the first site for the reception of indentured laborers in Mauritius. The name \"Aapravasi Ghat\" translates to \"immigration depot\" in Hindi. The location served as a processing center where indentured laborers arriving by ship were registered, housed temporarily, and assigned to various plantations across the island.\n" +
                                "\n<br><br>" +
                                "<b>Housing:</b> At the Aapravasi Ghat, indentured laborers had to endure substandard housing. They were often crowded into cramped barracks, and the site became a place where many experienced suffering, sickness, and death. The conditions were challenging, and the laborers endured a difficult transition from their home countries to the unfamiliar environment of Mauritius.\n" +
                                "\n<br><br>" +
                                "<b>End of Indenture:</b> The indenture system continued until the early 20th century when it was officially abolished. The Aapravasi Ghat continued to be used for processing immigrants until 1920. The location underwent many changes over the years that served various functions after the indenture system was discontinued.\n" +
                                "\n<br><br>" +
                                "<b>UNESCO World Heritage Site:</b> In 2006, the Aapravasi Ghat was designated as a UNESCO World Heritage Site in recognition of its historical importance in the global migration of indentured laborers and its impact on the multicultural identity of Mauritius.\n";
                        model.title1 = "";
                        model.image1 = fullPath + "/" + "aapravasi_ghat_2" + ".jpg";
                        model.text2 = "Today, the Aapravasi Ghat stands as a poignant symbol of the struggles and resilience of the indentured laborers who played a crucial role in shaping Mauritius's cultural and social fabric. \n" +
                                "<br><br>\n" +
                                "This UNESCO World Heritage site is open to visitors, offering them a chance to explore its rich history and gain insights into this critical period of the island's past. The exhibits and memorials here provide a deeper understanding of the lives and contributions of these laborers, making it a significant destination for those interested in Mauritian heritage.\n";
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
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 5) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "blue_penny_museum_2" + ".jpg";
                        model.text1 = "The Blue Penny Museum, inaugurated in November 2001, stands as a significant cultural institution dedicated to showcasing the rich history and art of Mauritius. Here are some key highlights:\n";
                        model.title1 = "";
                        model.image1 = "";
                        model.text2 = "<b>Historic Stamps:</b> The museum's name derives from the famous 1847 Blue Penny and Red Penny stamps, which are considered some of the rarest and most valuable stamps in the world. In 1993, a consortium of Mauritian enterprises, led by The Mauritius Commercial Bank, purchased these stamps for an astounding $2 million. This marked a momentous occasion as the stamps were repatriated to Mauritius after nearly 150 years of absence.\n" +
                                "<br><br>" +
                                "<b>Cultural Significance:</b> The Blue Penny Museum was established to preserve and promote the island's artistic and historical heritage. It serves not only as a display space for these iconic stamps but also as a hub for exploring various aspects of Mauritian culture, including its diverse influences and unique identity.\n" +
                                "<br><br>" +
                                "<b>Conservation Practices:</b> Given the extreme rarity and immense value of the Blue Penny stamps, meticulous conservation efforts are in place. The originals are illuminated only temporarily to minimize the risk of damage from prolonged exposure to light. For most visitors, replicas are displayed, ensuring that the original stamps remain safe for future generations to appreciate.\n" +
                                "<br><br>" +
                                "<b>Founder - The Mauritius Commercial Bank:</b> The Blue Penny Museum was established by The Mauritius Commercial Bank, a prominent financial institution in Mauritius known for its longstanding service and community engagement. \n";
                        model.title2 = "";
                        model.text3 = "";
                        model.image2 = fullPath + "/" + "blue_penny_museum_1" + ".jpg";
                        model.text4 = "The bank is proving through this project that it is dedicated to the preservation of the island's rich cultural legacy in addition to its financial success. The bank's goal in opening the museum is to increase pride and knowledge of Mauritius's historical significance among locals and visitors alike, particularly in light of the island's unique philatelic treasures.\n";
                        model.title3 = "Additional Artifacts\n";
                        model.text5 = "In addition to the renowned Blue Penny stamps, the museum features the original statue of Paul and Virginia, crafted by Prosper d'Épinay in 1881. This piece enhances the museum's cultural and artistic collection, drawing interest from both locals and tourists eager to explore Mauritius's history.\n";
                        model.title4 = "Museum Structure\n";
                        model.text6 = "The museum spans two floors, each with unique exhibits:\n" +
                                "<br><br>" +
                                "<b><span style=\"color:black; font-weight:bold;\">Ground Floor\n</span></b>" +
                                "<br><br><ul>" +
                                "<b>Souvenir Shop:</b> Visitors are greeted by a souvenir shop, offering mementos related to the museum and Mauritius.\n" +
                                "<br><br>" +
                                "<b>Temporary Exhibition Room:</b> This space hosts rotating exhibits, ensuring diverse and dynamic displays that change over time.\n" +
                                "</ul>" +
                                "<br><br>" +
                                "<b><span style=\"color:black; font-weight:bold;\">First Floor\n</span></b>" +
                                "<br><br><ul>" +
                                "<li><b>The Age of Discovery:</b> This exhibition narrates the stories of pioneers who sailed to the Mascarene Islands, featuring ancient maps that document their journeys.\n</li>" +
                                "<li><b>The Island Builders:</b> This room covers three critical periods in Mauritius's history: the Dutch, French, and British eras, providing a comprehensive overview of the island's evolution.\n</li>" +
                                "<li><b>Port Louis:</b> Focused on the origins and history of Port Louis, this exhibition offers insights into the city's development.\n</li>" +
                                "<li><b>The Postal Adventure:</b> Dedicated to Mauritius's postal history, this room displays two stamps from the Blue Penny collection, highlighting the island's philatelic heritage.\n</li>" +
                                "<li><b>Engraved Memory:</b> Honoring Joseph Osmond Barnard, the first engraver of stamps in Mauritius, this room serves as a tribute to his contributions to the field.\n</li>" +
                                "</ul>" +
                                "Overall, the Blue Penny Museum is thoughtfully organized to provide visitors with a captivating journey through the history, art, and unique cultural heritage of Mauritius.\n";
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
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    }

                }
                else if (Stash.getString("day").equals("day32")) {
                    if (position == 0) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "bois_cheri_1" + ".jpg";
                        model.text1 = "Established in 1892, the Bois Chéri Plantation is the largest tea producer in Mauritius, covering more than 250 hectares. The plantation has a factory and museum where guests can learn about the origins and manufacturing of tea.\n" +
                                "<br><br>" +
                                "During your visit, you can join a guided tour that takes you through the factory and museum, as well as the tea fields, and enjoy a tea-tasting session. The guides will explain the history of Bois Chéri and share the importance of tea production in Mauritius.\n";
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
                        model.text9 = "After the tour, head to the factory café near the restaurant for a tea-tasting experience. You’ll be able to try a variety of teas and pair them with treats like waffles or pancakes.\n" +
                                "<br><br>" +
                                "The Bois Chéri restaurant is just a short walk away, offering breathtaking views of the south coast. The restaurant serves unique dishes with a local twist, such as shrimp with green tea and chicken with exotic tea. Whether you’re with friends or family, the tour, tea tasting, and meal make for a delightful and relaxing experience.\n";
                        model.title8 = "";
                        model.text10 = "";
                        model.image4 = fullPath + "/" + "bois_cheri_4" + ".jpg";
//
                        model.text11 = "<br><b><span style=\"color:black; font-weight:bold;\">Visitor Information:</span></b>\n" +
                                "<br><br>" +
                                "<b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;• Visit Hours:</b>" +
                                "<br>" +
                                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;• Monday to Friday: 9:00 AM – 2:00 PM\n" +
                                "<br>" +
                                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;• Saturday: 9:00 AM – 11:00 AM\n" +
                                "<br>" +
                                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;• Closed on Sundays and public holidays\n" +
                                "<br>" +
                                "<b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;• Bois Chéri Tea Factory Operating Hours:</b>" +
                                "<br>" +
                                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;• Saturday: 9:00 AM – 02:00 PM\n" +
                                "<br>" +
                                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;• Saturday: 9:00 AM – 11:00 AM\n";

                        model.image5 = "";
                        model.text12 = "";
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 1) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "la_vanilla_1" + ".jpg";
                        model.text1 = "La Vanille Nature Park lets you experience the native plants and animals of Mauritius and nearby islands. As you walk through the park, you’ll see Nile crocodiles, tenrecs, iguanas, and turtles from Madagascar and the Seychelles, all under the shade of giant bamboo and palm trees.\n" +
                                "<br><br>" +
                                "The park also features a fascinating collection of fossils, including the extinct dodo and Madagascar elephant bird, which once roamed the area. There’s even an insectarium with over 23,000 species from around the globe, offering a chance to learn more about the insect world.\n";
                        model.title1 = "";
                        model.image1 = fullPath + "/" + "la_vanille_4" + ".jpg";
                        model.text2 = "One of the main attractions at La Vanille is the Crocodile Park, which houses more than 2,000 Nile crocodiles in different stages of growth. Visitors can even hold baby crocodiles for a photo! You can also catch a free crocodile feeding show every day at 11:30 a.m. It's a great spot to learn about this ancient reptile and see how it has adapted through time.\n";
                        model.title2 = "";
                        model.text3 = "";
                        model.image2 = fullPath + "/" + "la_vanille_9" + ".jpg";
                        model.text4 = "Thanks to a unique breeding program, La Vanille is home to the world's largest artificial habitat for Aldabra tortoises, with over 1,000 of them. You can get up close, feed them, and enjoy their gentle company.\n" +
                                "<br><br>" +
                                "The park is a fantastic place to connect with nature, with well-kept paths and serene surroundings. The forest is alive with unique wildlife, and you might even spot giant tortoises or friendly deer on your walk. For those who love photography, the park offers countless opportunities to capture amazing shots of the animals and landscape.\n";
                        model.title3 = "";
                        model.text5 = "";
                        model.title4 = "";
                        model.text6 = "";
                        model.title5 = "";
                        model.text7 = "";
                        model.title6 = "";
                        model.text8 = "";
                        model.title7 = "";
                        model.image3 = fullPath + "/" + "la_vanille_6" + ".jpg";
                        model.text9 = "La Vanille Nature Park also runs programs that help preserve nature, including planting over 350,000 native trees and breeding endangered animals. After your visit, stop by the Crocodile Park Shop for handcrafted souvenirs and books about wildlife.\n" +
                                "<br><br>" +
                                "To wrap up your day, relax with a meal at the Crocodile Park Restaurant, nestled in the forest. The restaurant offers meals for both kids and adults, with one standout dish—crocodile meat, served at Le Crocodile Affamé. Those who try it often rave about how good it is!\n";
                        model.title8 = "";
                        model.text10 = "";
                        model.image4 = fullPath + "/" + "la_vanilla_8" + ".jpg";
                        model.text11 = "Opening hours: Every day from 8:30 a.m. to 5:00 p.m.\n";
                        model.image5 = "";
                        model.text12 = "";
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                        //                        Toast.makeText(context, "images are no available", Toast.LENGTH_SHORT).show();
                    } else if (position == 2) {

                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "gris_gris_1" + ".jpg";
                        model.text1 = "The primary beach area features an expansive green field with a commanding view of dramatic cliffs and enormous waves. There are benches and a pavilion available for relaxation while taking in the scenery.<br>" +
                                "To the left, a concrete staircase descends to the beach, but swimming is strongly discouraged due to the high danger level. The powerful waves can swiftly overwhelm swimmers. Instead, enjoy a leisurely stroll along the beach, heading toward a small cave at the far end.";
                        model.title1 = "Caves at Gris Gris";
                        model.image1 = fullPath + "/" + "grisgris_2" + ".jpg";
                        model.text2 = "In addition to the cave on the far left side of the beach, two other hidden caves can be discovered at Gris Gris. These are more challenging to reach, involving a descent down a cliff and walking through the water.<br>" +
                                "Caution is advised against going all the way down, as water levels can fluctuate unpredictably, and the current is often too strong." +
                                "<br><br>" +
                                "For those eager to explore the secret caves at Gris Gris, head towards the cliff's edge directly across from the parking lot. Upon reaching the spot, descend only about halfway to catch a glimpse of the caves on your right." +
                                "<br><br>" +
                                "It's important to bear in mind that entering the caves could pose risks if the water level rises!<br>" +
                                "Gris Gris beach is intricately connected to the village of Souillac, which relies heavily on tourism for its revenue. " +
                                "<br><br>" +
                                "Established 200 years ago as a port for ships sailing from Europe to India, Souillac has a rich history worth exploring. Plan your day strategically to make the most of your visit to the southern part of Mauritius, and consider including a visit to Rochester Falls, just outside the village, renowned for its distinctive rectangular-sided rocks.<br>";
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
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    }
                }
                else if (Stash.getString("day").equals("day33")) {
                    if (position == 0) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "tamarin_3" + ".jpg";
                        model.text1 = "Tamarin Bay Beach is a gorgeous beach location on Mauritius' west coast, situated just past the <b>Black River</b> village. Known for its natural beauty and ideal surfing conditions, it attracts both locals and tourists.\n";
                        model.title1 = "Surfing History and Recognition";
                        model.image1 = fullPath + "/" + "tamarin_2" + ".jpg";
                        model.text2 = "This beach, formerly known as <b>Santosha Bay,</b> was kept a secret by surfers who considered it a hidden treasure. The name <b>Santosha</b> can still be faintly seen painted on a few buildings. Tamarin Bay gained international fame with the release of the 1974 surf documentary <b>\"Forgotten Island of Santosha\"</b> by Larry and Roger Yates, which brought global attention to its remarkable surf breaks. The bay features two renowned surfing spots: <b>‘Dal’</b> on the southern side and <b>‘Black Stone’</b> to the north.";
                        model.title2 = "Dolphins and Wildlife\n";
                        model.text3 = "Tamarin Bay is a haven for dolphins, especially <b>Spinner</b> and <b>Bottlenose dolphins,</b> which are frequently spotted in the early morning hours before heading out to the open sea. Many boat companies offer morning trips for tourists to observe and swim with these playful creatures, making it a top destination for wildlife enthusiasts as well.\n" +
                                "<br><br>" +
                                "<b><span style=\"color:black; font-weight:bold;\">Surfing Hub\n</span></b>" +
                                "<br><br>" +
                                "Since the 1970s, Tamarin Bay has been a major surfing hub, introduced primarily by Australians living on the island’s west coast. The area’s strong waves and ideal conditions continue to draw surf enthusiasts from around the world, and surfing here is often considered a special privilege.\n" +
                                "<br><br>" +
                                "<b><span style=\"color:black; font-weight:bold;\">Authentic and Laid-Back Atmosphere\n</span></b>" +
                                "<br><br>" +
                                "Despite its fame as a surfing hotspot, Tamarin Bay retains its authentic charm. Local families frequent the beach for leisurely strolls or relaxed afternoons, creating a welcoming and laid-back environment. Its vibrant yet genuine atmosphere makes it appealing to both tourists and locals.\n" +
                                "<br><br>" +
                                "<b><span style=\"color:black; font-weight:bold;\">Swimming Caution\n</span></b>" +
                                "<br><br>" +
                                "While Tamarin Bay is excellent for surfing, swimming is not recommended for children or inexperienced swimmers. The strong currents and large waves can be unpredictable, making it less safe for casual swimming.\n" +
                                "<br><br>" +
                                "<b><span style=\"color:black; font-weight:bold;\">Lively Beach Culture\n</span></b>" +
                                "<br><br>" +
                                "Tamarin Bay can become quite busy, especially on weekends and holidays, offering a lively and energetic beach scene. Whether you're enjoying the surf, relaxing by the water, or taking in the local culture, the beach offers something for everyone.\n" +
                                "<br><br>" +
                                "<b><span style=\"color:black; font-weight:bold;\">Best Times to Visit\n</span></b>" +
                                "<br><br>" +
                                "The best time to visit Tamarin Bay is either early in the morning, between 8:00 and 11:00, or in the afternoon, from 13:00 to 16:00. These times offer the most favorable conditions for beach activities, with fewer crowds and enjoyable weather.\n" +
                                "\n<br><br>" +
                                "<b><span style=\"color:black; font-weight:bold;\">Activities at Tamarin Bay\n</span></b>";
                        model.image2 = fullPath + "/" + "tamarin_1" + ".jpg";
                        model.text4 = "In addition to surfing, Tamarin Bay offers a variety of water activities, including:\n" +
                                "<ul>" +
                                "<li>Stand-up paddleboarding\n</li>" +
                                "<li>Bodyboarding\n</li>" +
                                "<li>Catamaran tours\n</li>" +
                                "<li>Swimming with dolphins\n</li>" +
                                "<li>Speed boat trips\n</li>" +
                                "<li>Kayaking\n</li>" +
                                "</ul>" +
                                "Tamarin Bay Beach is a dynamic coastal destination that combines the thrill of surfing with the beauty of nature. Its authentic atmosphere, paired with a range of water activities and wildlife experiences, makes it an ideal spot for anyone looking to enjoy Mauritius’s vibrant beach culture.\n";
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
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 1) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "trou_aux_cerfs_4" + ".jpg";
                        model.text1 = "Trou aux Cerfs is a dormant volcanic crater surrounded by thick forest, filled with native plants and towering pine trees. Though it hasn’t erupted in about 700,000 years, scientists believe it could potentially become active again one day.\n" +
                                "<br><br>" +
                                "One of the best things about Trou aux Cerfs is the amazing 360-degree view. The town of Curepipe and the surrounding mountains, including Rempart Mountain, Trois Mamelles, and the Port Louis-Moka range, are visible from the summit. It’s also a popular spot for locals, especially joggers who gather around 5 a.m. every morning. There’s a gazebo where you can sit, relax, and enjoy the peaceful surroundings.\n" +
                                "<br><br>" +
                                "Since it’s located at a higher elevation and surrounded by trees, the area can get a little chilly, so it’s a good idea to bring a light sweater or shawl. You can access Trou aux Cerfs from three main roads: La Hausse de la Louviere, Edgar Huges Road, and Crater Lane, and there’s parking available nearby.\n";
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
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 2) {

                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "grand_bassin_1" + ".jpg";
                        model.text1 = "Grand Bassin, also known as Ganga Talao, is a sacred lake located in the quiet mountain area of Savanne, sitting about 550 meters above sea level. In 1898, Pandit Giri Gossayne, a native of Triolet village, made the first known visit to Ganga Talao. Since then, Hindus have come to regard this place as one of Mauritius' most sacred sites.\n" +
                                "<br><br>" +
                                "On the bank of the lake stands the Shiv Mandir, a temple dedicated to Lord Shiva. Every year, during the Maha Shivaratri festival, around half a million Hindus in Mauritius make a pilgrimage to the lake, with many walking barefoot from their homes, carrying Kanvars (decorated structures used to carry holy water and offerings).\n";
                        model.title1 = "";
                        model.image1 = fullPath + "/" + "grand_bassin_2" + ".jpg";
                        model.text2 = "The lake is symbolically linked to the holy Ganges River in India by its name, Ganga Talao, which means \"Lake of Ganga.\"\n";
                        model.title2 = "History";
                        model.text3 = "In 1866, Pandit Sanjibonlal returned to Mauritius after completing his first labor contract in India and transformed Grand Bassin into a pilgrimage site. He converted a building into a temple and brought a Shivalingam (symbol of Shiva) from India.\n" +
                                "<br><br>" +
                                "In 1897, priest Jhummon Giri Gosagne Napal had a dream where he saw that the waters of Grand Bassin were linked to the holy Ganges. The lake, previously called \"Pari Talao,\" was declared a sacred lake in 1998. In 1972, water from the Ganges River was mixed with the lake’s water, and the name Ganga Talao became official.\n";
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
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 3) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "black_river_georges_2" + ".jpg";
                        model.text1 = "Black River Gorges National Park is located in the hilly southwestern part of Mauritius. Established on June 15, 1994, and managed by the National Parks and Conservation Service, the park covers an area of 67.54 square kilometers. It features different habitats like humid upland forests, drier lowland forests, and marshy heathlands. Visitors can explore two information centers, picnic areas, and 60 kilometers of hiking trails. There are also four research stations focused on conservation efforts, run by the National Parks and Conservation Service along with the Mauritian Wildlife Foundation.\n" +
                                "<br><br>" +
                                "The park was created to protect what remains of the island's rainforest. Over time, non-native plants like Chinese guava and privet, along with animals such as the rusa deer and wild pigs, have damaged parts of the forest. To protect native species, some areas of the park have been fenced off, and efforts are being made to control the invasive species.\n";
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
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 4) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "chamarel_2" + ".jpg";
                        model.text1 = "The Seven Coloured Earths are a must-see geological wonder located in the Chamarel plain of the Rivière Noire District in southwest Mauritius. This natural phenomenon features small sand dunes that show off seven different colors—red, brown, violet, green, blue, purple, and yellow. What makes this spot so unique is the way the colors naturally settle in separate layers, forming striped patterns across the hills. Over time, the rain has carved these colorful dunes into fascinating shapes that resemble the texture of meringue.\n" +
                                "<br><br>" +
                                "These sands come from volcanic rock that broke down into clay, eventually turning into soil through a process called hydrolysis. The red and anthracite shades come from iron, while aluminum gives the sands their blue and purplish tones. Scientists believe the colors are linked to how the volcanic rock cooled at different rates, but they still don’t fully understand how the layers form so consistently.\n" +
                                "<br><br>" +
                                "The name \"Seven Coloured Earths\" isn’t the official title, but a simple description of what you'll see. Sometimes it’s called \"Chamarel Seven Coloured Earths\" or \"Terres des Sept Couleurs\" in French, but whatever the name, the sight is unforgettable.\n" +
                                "<br><br>" +
                                "This fascinating phenomenon can even be recreated on a small scale. If you mix the differently colored sands together, they'll separate over time, forming layers just like in nature.\n" +
                                "<br><br>" +
                                "Since the 1960s, the Seven Coloured Earths have become a top tourist attraction in Mauritius. Today, the dunes are protected by a wooden fence, and visitors can no longer walk on them. Instead, you can take in the breathtaking view from observation posts along the fence. And if you want a little souvenir, local shops sell little test tubes filled with colored sand.\n";
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
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    }
                }
                else if (Stash.getString("day").equals("day41")) {
                    if (position == 0) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "pamplemousse_garden" + ".jpg";
                        model.text1 = "The Sir Seewoosagur Ramgoolam Botanic Garden, also known as the Pamplemousses Botanic Garden, is a major tourist attraction located in Pamplemousses, near Port Louis, Mauritius. It is the oldest botanical garden in the Southern Hemisphere. Established in 1770 by Pierre Poivre, the garden spans about 37 hectares (91 acres) and is famous for its large pond filled with giant water lilies (Victoria amazonica).\n" +
                                "<br><br>" +
                                "Over the years, the garden has had several names that reflect its changing status and ownership. Some of these names include 'Jardin de Mon Plaisir,' 'Jardin des Plantes,' 'Le Jardin National de l’Ile de France,' 'Jardin Royal,' and 'Jardin Botanique des Pamplemousses.' On September 17, 1988, it was officially named the \"Sir Seewoosagur Ramgoolam Botanic Garden\" to honor the first prime minister of Mauritius.\n";
                        model.title1 = "";
                        model.image1 = fullPath + "/" + "botanical_garden_1" + ".jpg";
                        model.text2 = "In addition to the stunning giant water lilies, the garden is home to a variety of spices, ebonies, sugar canes, and 85 different types of palm trees from regions including Central America, Asia, Africa, and the Indian Ocean islands. Many notable people, such as Princess Margaret, Indira Gandhi, and François Mitterrand, have planted trees in the garden.\n" +
                                "<br><br>" +
                                "Located about seven miles northeast of Port Louis, the garden has a rich history that dates back to 1729 when it was set aside for colonist P. Barmont. It changed hands several times, with different owners contributing to its growth and development. Today, the garden covers approximately 25,110 hectares (62,040 acres), with part of the area serving as an experimental station.\n";
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
                        model.text9 = "Over the years, the garden faced times of neglect and challenges, but directors like James Duncan worked hard to restore and improve it. In 1866, during a malaria outbreak, the garden played an important role by growing Eucalyptus trees to help fight the disease.\n" +
                                "<br><br>" +
                                "In 1913, the Department of Agriculture took over the garden, managing its growth and upkeep. Notably, after the death of Seewoosagur Ramgoolam in 1985, part of the garden was dedicated to a crematorium, marking the first time someone was cremated on its grounds.\n" +
                                "<br><br>" +
                                "Today, the Sir Seewoosagur Ramgoolam Botanic Garden is a symbol of Mauritius's botanical heritage, inviting visitors to explore its beautiful landscapes and rich history.\n";
                        model.title8 = "Chateau de Mon Plaisir";
                        model.text10 = "";
                        model.image4 = fullPath + "/" + "botanical_garden_4" + ".jpg";
                        model.text11 = "Until 1839, the Chateau de Mon Plaisir was a simple building with a flat roof and circular verandahs. The current single-story structure, built by the English in the mid-19th century, is now a National Monument, which means it is legally protected. Visitors can enjoy a lovely view of the Moka Range and the Peak of Pieter Both from the Chateau, making it a beautiful spot to take in the scenery.\n";
                        model.image5 = "";
                        model.text12 = "";
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 1) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
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
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 2) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "chateau_de_labourdonnais" + ".jpg";
                        model.text1 = "A majestic lane lined with towering trees leads you to <b>Château Labourdonnais</b>, a stunning colonial mansion that once belonged to Christian Wiehe. Built in 1856 and completed a few years later, this mansion is often regarded as the most beautiful colonial house in Mauritius. \n";
                        model.title1 = "";
                        model.image1 = fullPath + "/" + "chateau_de_labourdonnais_4" + ".jpg";
                        model.text2 = "The grand estate features a spacious dining room, a vast pantry, an expansive living room, elegantly designed bedrooms, and a large exhibition hall, making it a captivating destination.\n";
                        model.title2 = "Gardens and Exploration\n";
                        model.text3 = "";
                        model.image2 = fullPath + "/" + "chateau_de_labourdonnais_2" + ".jpg";
                        model.text4 = "As you explore the grounds, you’ll be enchanted by the lush gardens, where century-old mango trees, spice trees, and exotic fruit trees abound. Adding to the charm, peaceful <b>Aldabra tortoises</b> graze around the gardens. You can also visit the <b>Rhumerie des Mascareignes</b>, established in 2006, to learn about traditional rum-making techniques, and enjoy a delightful tasting session that includes rum, fruit pastes, juices, and sorbets.\n" +
                                "<br><br>" +
                                "The visit concludes at the Tasting Bar, where you can sample products made from the estate’s orchards, such as jams, fruit jellies, and sorbets. Before you leave, don’t miss the opportunity to visit the boutique, which offers a range of local products like decorative items, rum, and spices. For a refined dining experience, visit <b>La Table du Château</b>, a restaurant offering Mauritian dishes made from fresh produce grown on the estate.\n";
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
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 3) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "port_louis_3" + ".jpg";
                        model.text1 = "Le Caudan Waterfront is a vibrant commercial hub in Port Louis, Mauritius, featuring an array of amenities such as shops, banks, casinos, cinemas, restaurants, a marina, and the luxurious Le Labourdonnais hotel.\n";
                        model.title1 = "Historical Roots\n";
                        model.image1 = "";
                        model.text2 = "The name \"Le Caudan Waterfront\" honors Jean Dominique Michel de Caudan, who arrived in Mauritius in 1726 and established a saltpan near what is now the Robert Edward Hart Garden. This peninsula, formed around a fossil coral islet, has evolved over 250 years, once housing a powder magazine, observatories, and warehouses, with the sugar industry shaping much of its history until the Bulk Sugar Terminal opened in 1980.\n";
                        model.title2 = "Historically Significant Spots\n";
                        model.text3 = "Notable historical locations in Le Caudan include the Blue Penny Museum, which is situated in the Food Court, and the first meteorological observatory in the Indian Ocean, which was formerly a docks office. Different areas within the complex are named to reflect the island's colonial history:\n" +
                                "<br><br><ul>" +
                                "<li><b>Barkly Wharf:</b> Named after Sir Henry Barkly, Governor of Mauritius (1863-1870).\n</li>" +
                                "<li><b>Le Pavillon Wing:</b> Linked to Pavillon Street from an old map of Port Louis.\n</li>" +
                                "<li><b>Dias Pier:</b> Honors Diogo Dias, the navigator believed to have first charted the Mascarene Islands.\n</li>" +
                                "</ul>" +
                                "Le Caudan Waterfront combines modernity with rich historical significance, making it a must-visit destination.\n";
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
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 4) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "aapravasi_ghat_1" + ".jpg";
                        model.text1 = "The Aapravasi Ghat is a historical site located in Port Louis, the capital city of Mauritius. It holds great significance in the history of the island and is recognized as a UNESCO World Heritage Site. The history of the Aapravasi Ghat is closely tied to the indentured labor system that shaped the social and economic landscape of Mauritius.\n" +
                                "<br><br>\n" +
                                "Here is a brief history of the Aapravasi Ghat:\n" +
                                "\n<br><br>" +
                                "<b>Indentured Labor System:</b> In the 19th century, after the abolition of slavery, the British turned to the system of indentured labor to meet the demand for cheap and abundant workforce for their colonies. Indentured laborers were recruited from various parts of India, as well as China and Africa, to work on plantations in Mauritius and other British colonies.\n" +
                                "\n<br><br>" +
                                "<b>Establishment of Aapravasi Ghat:</b> The Aapravasi Ghat was established in 1849 as the first site for the reception of indentured laborers in Mauritius. The name \"Aapravasi Ghat\" translates to \"immigration depot\" in Hindi. The location served as a processing center where indentured laborers arriving by ship were registered, housed temporarily, and assigned to various plantations across the island.\n" +
                                "\n<br><br>" +
                                "<b>Housing:</b> At the Aapravasi Ghat, indentured laborers had to endure substandard housing. They were often crowded into cramped barracks, and the site became a place where many experienced suffering, sickness, and death. The conditions were challenging, and the laborers endured a difficult transition from their home countries to the unfamiliar environment of Mauritius.\n" +
                                "\n<br><br>" +
                                "<b>End of Indenture:</b> The indenture system continued until the early 20th century when it was officially abolished. The Aapravasi Ghat continued to be used for processing immigrants until 1920. The location underwent many changes over the years that served various functions after the indenture system was discontinued.\n" +
                                "\n<br><br>" +
                                "<b>UNESCO World Heritage Site:</b> In 2006, the Aapravasi Ghat was designated as a UNESCO World Heritage Site in recognition of its historical importance in the global migration of indentured laborers and its impact on the multicultural identity of Mauritius.\n";
                        model.title1 = "";
                        model.image1 = fullPath + "/" + "aapravasi_ghat_2" + ".jpg";
                        model.text2 = "Today, the Aapravasi Ghat stands as a poignant symbol of the struggles and resilience of the indentured laborers who played a crucial role in shaping Mauritius's cultural and social fabric. \n" +
                                "<br><br>\n" +
                                "This UNESCO World Heritage site is open to visitors, offering them a chance to explore its rich history and gain insights into this critical period of the island's past. The exhibits and memorials here provide a deeper understanding of the lives and contributions of these laborers, making it a significant destination for those interested in Mauritian heritage.\n";
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
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 5) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "blue_penny_museum_2" + ".jpg";
                        model.text1 = "The Blue Penny Museum, inaugurated in November 2001, stands as a significant cultural institution dedicated to showcasing the rich history and art of Mauritius. Here are some key highlights:\n";
                        model.title1 = "";
                        model.image1 = "";
                        model.text2 = "<b>Historic Stamps:</b> The museum's name derives from the famous 1847 Blue Penny and Red Penny stamps, which are considered some of the rarest and most valuable stamps in the world. In 1993, a consortium of Mauritian enterprises, led by The Mauritius Commercial Bank, purchased these stamps for an astounding $2 million. This marked a momentous occasion as the stamps were repatriated to Mauritius after nearly 150 years of absence.\n" +
                                "<br><br>" +
                                "<b>Cultural Significance:</b> The Blue Penny Museum was established to preserve and promote the island's artistic and historical heritage. It serves not only as a display space for these iconic stamps but also as a hub for exploring various aspects of Mauritian culture, including its diverse influences and unique identity.\n" +
                                "<br><br>" +
                                "<b>Conservation Practices:</b> Given the extreme rarity and immense value of the Blue Penny stamps, meticulous conservation efforts are in place. The originals are illuminated only temporarily to minimize the risk of damage from prolonged exposure to light. For most visitors, replicas are displayed, ensuring that the original stamps remain safe for future generations to appreciate.\n" +
                                "<br><br>" +
                                "<b>Founder - The Mauritius Commercial Bank:</b> The Blue Penny Museum was established by The Mauritius Commercial Bank, a prominent financial institution in Mauritius known for its longstanding service and community engagement. \n";
                        model.title2 = "";
                        model.text3 = "";
                        model.image2 = fullPath + "/" + "blue_penny_museum_1" + ".jpg";
                        model.text4 = "The bank is proving through this project that it is dedicated to the preservation of the island's rich cultural legacy in addition to its financial success. The bank's goal in opening the museum is to increase pride and knowledge of Mauritius's historical significance among locals and visitors alike, particularly in light of the island's unique philatelic treasures.\n";
                        model.title3 = "Additional Artifacts\n";
                        model.text5 = "In addition to the renowned Blue Penny stamps, the museum features the original statue of Paul and Virginia, crafted by Prosper d'Épinay in 1881. This piece enhances the museum's cultural and artistic collection, drawing interest from both locals and tourists eager to explore Mauritius's history.\n";
                        model.title4 = "Museum Structure\n";
                        model.text6 = "The museum spans two floors, each with unique exhibits:\n" +
                                "<br><br>" +
                                "<b><span style=\"color:black; font-weight:bold;\">Ground Floor\n</span></b>" +
                                "<br><br><ul>" +
                                "<b>Souvenir Shop:</b> Visitors are greeted by a souvenir shop, offering mementos related to the museum and Mauritius.\n" +
                                "<br><br>" +
                                "<b>Temporary Exhibition Room:</b> This space hosts rotating exhibits, ensuring diverse and dynamic displays that change over time.\n" +
                                "</ul>" +
                                "<br><br>" +
                                "<b><span style=\"color:black; font-weight:bold;\">First Floor\n</span></b>" +
                                "<br><br><ul>" +
                                "<li><b>The Age of Discovery:</b> This exhibition narrates the stories of pioneers who sailed to the Mascarene Islands, featuring ancient maps that document their journeys.\n</li>" +
                                "<li><b>The Island Builders:</b> This room covers three critical periods in Mauritius's history: the Dutch, French, and British eras, providing a comprehensive overview of the island's evolution.\n</li>" +
                                "<li><b>Port Louis:</b> Focused on the origins and history of Port Louis, this exhibition offers insights into the city's development.\n</li>" +
                                "<li><b>The Postal Adventure:</b> Dedicated to Mauritius's postal history, this room displays two stamps from the Blue Penny collection, highlighting the island's philatelic heritage.\n</li>" +
                                "<li><b>Engraved Memory:</b> Honoring Joseph Osmond Barnard, the first engraver of stamps in Mauritius, this room serves as a tribute to his contributions to the field.\n</li>" +
                                "</ul>" +
                                "Overall, the Blue Penny Museum is thoughtfully organized to provide visitors with a captivating journey through the history, art, and unique cultural heritage of Mauritius.\n";
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
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    }

                }
                else if (Stash.getString("day").equals("day42")) {
                    if (position == 0) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
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
                        model.text11 = "Weekdays are the best time to visit if you want to avoid the larger crowds that flock to the island on weekends. October and November are considered the ideal months for a trip to Ile aux Cerfs, but the entire period from June to November provides pleasant weather for a visit." +
                                "<br><br>" +
                                "<b><span style=\"color:black; font-weight:bold;\">Accommodation:</span></b><br>" +
                                "For those seeking luxury, the 5-star Shangri-La’s Le Touessrok Resort & Spa offers an opulent stay on Ile aux Cerfs, providing the perfect setting for a grand celebration or special event."
                                + "<br><br>"
                                + "<b><span style=\"color:black; font-weight:bold;\">Dining Options:</span></b>";
                        model.image5 = fullPath + "/" + "ile_aux_cerfs_3" + ".jpg";
                        model.text12 = "The island has several dining options, including a beachside restaurant where you can enjoy a meal with a view. There are also two bars offering refreshing drinks and cocktails at reasonable prices. If you prefer, you can bring your own food and drinks for a picnic on the beach.<br>";

                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    }

                }
                else if (Stash.getString("day").equals("day43")) {
                    if (position == 0) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "casela" + ".jpg";
                        model.text1 = "Casela World of Adventures, the most visited attraction in Mauritius, is nestled amidst sugarcane fields on the island’s western coast, with the stunning <b>Rempart Mountain</b> serving as its backdrop. Originally founded as a bird sanctuary in 1979, Casela has grown into a sprawling nature park offering a wide range of thrilling activities and unforgettable animal encounters.\n";
                        model.title1 = "";
                        model.image1 = fullPath + "/" + "botanical_garden_5" + ".jpg";
                        model.text2 = "";
                        model.title2 = "Exciting Activities at Casela\n";
                        model.text3 = "Casela provides something for everyone, from adrenaline-pumping adventures like ziplining, quad biking, and the exhilarating <b>Canyon Swing,</b> to more family-friendly options like the <b>African safari,</b> where visitors can see big cats, giraffes, rhinos, and zebras. There are four themed worlds to choose from, so you can customize your visit:\n" +
                                "<br><br><ul>" +
                                "<li><b>Mountain Kingdom:</b> Features activities like the Zig Zag Racer, Canyon Swing, and Zip and Splash Tour.\n</li>" +
                                "<li><b>Big Cats Kingdom:</b> Offers unique experiences such as walking with lions and interacting with cheetahs and caracals.\n</li>" +
                                "<li><b>Safari Kingdom:</b> Includes quad biking, giraffe feeding, Segway tours, and e-bike safaris.\n</li>" +
                                "<li><b>Middle Kingdom:</b> Provides opportunities to interact with and feed tortoises.\n</li>" +
                                "</ul>" +
                                "<b><span style=\"color:black; font-weight:bold;\">Action-Packed Mountain Adventures</span></b>";
                        model.image2 = fullPath + "/" + "casela_6" + ".jpg";
                        model.text4 = "<ul>" +
                                "<li><b>Ziplining:</b> Soar above the park on zip lines, taking in breathtaking aerial views.\n</li>" +
                                "<li><b>Canyon Swing:</b> Experience the thrill of freefalling from a 45-meter platform.\n</li>" +
                                "<li><b>Mountain Climbing:</b> Challenge yourself on the Via Ferrata Canyon Tour through sugarcane fields and scenic plateaus.\n</li>" +
                                "</ul>" +
                                "<b><span style=\"color:black; font-weight:bold;\">Safari and Animal Encounters</span></b>";
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
                        model.text9 = "<ul><li><b>Walk with Lions:</b> Stroll alongside lions with expert guides ensuring your safety.\n</li>" +
                                "<li><b>Big Cat Interactions:</b> Get up close with lions, cheetahs, and caracals.\n</li>" +
                                "<li><b>Drive-Thru Safari:</b> Take a 45-minute drive to observe lions in their natural habitat.\n</li>" +
                                "<li><b>E-Bike Safari & Segway Tour:</b> Explore the Yemen Nature Reserve on eco-friendly bikes or Segways.\n</li>" +
                                "<li><b>Camel Riding:</b> Enjoy a camel ride through the scenic park.\n</li>" +
                                "</ul>\n" +
                                "<b><span style=\"color:black; font-weight:bold;\">Family Activities\n</span></b>";
                        model.title8 = "";
                        model.text10 = "";
                        model.image4 = fullPath + "/" + "casela_3" + ".jpg";
                        model.text11 = "<ul><li><b>Zookeeper for a Day:</b> Kids can experience life as a zookeeper, participating in various activities.\n</li>" +
                                "<li><b>Petting Farm & Pony Ride:</b> Ideal for children to interact with farm animals.\n</li>" +
                                "<li><b>Giraffe, Ostrich, & Tortoise Feeding:</b> Engage in up-close encounters with these majestic animals.\n</li>" +
                                "</ul>" +
                                "<b><span style=\"color:black; font-weight:bold;\">Dining and Shopping</span></b>" +
                                "<br><br>" +
                                "Visitors can unwind at the <b>Casela Restaurant,</b> which overlooks <b>Tamarin Bay</b> and offers a variety of cuisines, including Mauritian, European, and Asian dishes. The park also features multiple food outlets to satisfy diverse tastes. Before leaving, stop by the <b>gift shops</b> to pick up souvenirs, from African crafts to locally made jewelry and textiles.\n" +
                                "<br><br>" +
                                "<b><span style=\"color:black; font-weight:bold;\">Conservation and History\n</span></b>" +
                                "<br><br>" +
                                "Since its inception, Casela has remained committed to conservation efforts. In February 2015, the park gained international recognition when <b>Her Royal Highness Princess Stephanie of Monaco</b> became its patron, further highlighting the park’s dedication to protecting endangered species. Today, Casela continues to expand its offerings while staying true to its mission of conservation, providing a memorable experience for visitors of all ages.\n";
                        model.image5 = "";
                        model.text12 = "";

                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 1) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "trou_aux_cerfs_4" + ".jpg";
                        model.text1 = "Trou aux Cerfs is a dormant volcanic crater surrounded by thick forest, filled with native plants and towering pine trees. Though it hasn’t erupted in about 700,000 years, scientists believe it could potentially become active again one day.\n" +
                                "<br><br>" +
                                "One of the best things about Trou aux Cerfs is the amazing 360-degree view. The town of Curepipe and the surrounding mountains, including Rempart Mountain, Trois Mamelles, and the Port Louis-Moka range, are visible from the summit. It’s also a popular spot for locals, especially joggers who gather around 5 a.m. every morning. There’s a gazebo where you can sit, relax, and enjoy the peaceful surroundings.\n" +
                                "<br><br>" +
                                "Since it’s located at a higher elevation and surrounded by trees, the area can get a little chilly, so it’s a good idea to bring a light sweater or shawl. You can access Trou aux Cerfs from three main roads: La Hausse de la Louviere, Edgar Huges Road, and Crater Lane, and there’s parking available nearby.\n";
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
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 2) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "black_river_georges_2" + ".jpg";
                        model.text1 = "Black River Gorges National Park is located in the hilly southwestern part of Mauritius. Established on June 15, 1994, and managed by the National Parks and Conservation Service, the park covers an area of 67.54 square kilometers. It features different habitats like humid upland forests, drier lowland forests, and marshy heathlands. Visitors can explore two information centers, picnic areas, and 60 kilometers of hiking trails. There are also four research stations focused on conservation efforts, run by the National Parks and Conservation Service along with the Mauritian Wildlife Foundation.\n" +
                                "<br><br>" +
                                "The park was created to protect what remains of the island's rainforest. Over time, non-native plants like Chinese guava and privet, along with animals such as the rusa deer and wild pigs, have damaged parts of the forest. To protect native species, some areas of the park have been fenced off, and efforts are being made to control the invasive species.\n";
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
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 3) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "chamarel_1" + ".jpg";
                        model.text1 = "Chamarel Waterfall, the tallest single-drop waterfall in Mauritius, stands at about 100 meters high and is a sight to behold. Surrounded by lush greenery, this natural wonder is powered by three streams that merge and flow into the Saint Denis River, creating a powerful cascade. At its peak, the waterfall flows at over 40,000 cubic meters per minute.\n" +
                                "<br><br>" +
                                "As you drive along the 3-kilometer road through the Seven-Coloured Earth Geopark, you'll pass the viewpoint for Chamarel Waterfall. You can reach the ideal location to witness this magnificent waterfall by taking a brief hike through dense forest.\n" +
                                "<br><br>" +
                                "The waterfall pours over a basalt cliff into an oval pool below, then winds through a 6-kilometer canyon lined with tropical forests before eventually reaching Baie du Cap. It's a living reminder of Mauritius' volcanic past, with the landscape shaped by lava flows from two different periods. The bottom layer of basalt is 10 to 8 million years old, while the top layer dates from 3.5 to 1.7 million years ago.\n" +
                                "<br><br>" +
                                "For the adventurous, there's a three-hour trek through the hidden valley leading to the base of the waterfall. Once there, you can take a dip in the cool waters and feel the refreshing spray of the waterfall above.\n" +
                                "<br><br>" +
                                "The combination of the sights and sounds creates a true tropical rainforest experience, leaving you with memories of this amazing natural beauty.\n";
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
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 4) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "chamarel_2" + ".jpg";
                        model.text1 = "The Seven Coloured Earths are a must-see geological wonder located in the Chamarel plain of the Rivière Noire District in southwest Mauritius. This natural phenomenon features small sand dunes that show off seven different colors—red, brown, violet, green, blue, purple, and yellow. What makes this spot so unique is the way the colors naturally settle in separate layers, forming striped patterns across the hills. Over time, the rain has carved these colorful dunes into fascinating shapes that resemble the texture of meringue.\n" +
                                "<br><br>" +
                                "These sands come from volcanic rock that broke down into clay, eventually turning into soil through a process called hydrolysis. The red and anthracite shades come from iron, while aluminum gives the sands their blue and purplish tones. Scientists believe the colors are linked to how the volcanic rock cooled at different rates, but they still don’t fully understand how the layers form so consistently.\n" +
                                "<br><br>" +
                                "The name \"Seven Coloured Earths\" isn’t the official title, but a simple description of what you'll see. Sometimes it’s called \"Chamarel Seven Coloured Earths\" or \"Terres des Sept Couleurs\" in French, but whatever the name, the sight is unforgettable.\n" +
                                "<br><br>" +
                                "This fascinating phenomenon can even be recreated on a small scale. If you mix the differently colored sands together, they'll separate over time, forming layers just like in nature.\n" +
                                "<br><br>" +
                                "Since the 1960s, the Seven Coloured Earths have become a top tourist attraction in Mauritius. Today, the dunes are protected by a wooden fence, and visitors can no longer walk on them. Instead, you can take in the breathtaking view from observation posts along the fence. And if you want a little souvenir, local shops sell little test tubes filled with colored sand.\n";
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
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 5) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "tamarin_3" + ".jpg";
                        model.text1 = "Tamarin Bay Beach is a gorgeous beach location on Mauritius' west coast, situated just past the <b>Black River</b> village. Known for its natural beauty and ideal surfing conditions, it attracts both locals and tourists.\n";
                        model.title1 = "Surfing History and Recognition";
                        model.image1 = fullPath + "/" + "tamarin_2" + ".jpg";
                        model.text2 = "This beach, formerly known as <b>Santosha Bay,</b> was kept a secret by surfers who considered it a hidden treasure. The name <b>Santosha</b> can still be faintly seen painted on a few buildings. Tamarin Bay gained international fame with the release of the 1974 surf documentary <b>\"Forgotten Island of Santosha\"</b> by Larry and Roger Yates, which brought global attention to its remarkable surf breaks. The bay features two renowned surfing spots: <b>‘Dal’</b> on the southern side and <b>‘Black Stone’</b> to the north.";
                        model.title2 = "Dolphins and Wildlife\n";
                        model.text3 = "Tamarin Bay is a haven for dolphins, especially <b>Spinner</b> and <b>Bottlenose dolphins,</b> which are frequently spotted in the early morning hours before heading out to the open sea. Many boat companies offer morning trips for tourists to observe and swim with these playful creatures, making it a top destination for wildlife enthusiasts as well.\n" +
                                "<br><br>" +
                                "<b><span style=\"color:black; font-weight:bold;\">Surfing Hub\n</span></b>" +
                                "<br><br>" +
                                "Since the 1970s, Tamarin Bay has been a major surfing hub, introduced primarily by Australians living on the island’s west coast. The area’s strong waves and ideal conditions continue to draw surf enthusiasts from around the world, and surfing here is often considered a special privilege.\n" +
                                "<br><br>" +
                                "<b><span style=\"color:black; font-weight:bold;\">Authentic and Laid-Back Atmosphere\n</span></b>" +
                                "<br><br>" +
                                "Despite its fame as a surfing hotspot, Tamarin Bay retains its authentic charm. Local families frequent the beach for leisurely strolls or relaxed afternoons, creating a welcoming and laid-back environment. Its vibrant yet genuine atmosphere makes it appealing to both tourists and locals.\n" +
                                "<br><br>" +
                                "<b><span style=\"color:black; font-weight:bold;\">Swimming Caution\n</span></b>" +
                                "<br><br>" +
                                "While Tamarin Bay is excellent for surfing, swimming is not recommended for children or inexperienced swimmers. The strong currents and large waves can be unpredictable, making it less safe for casual swimming.\n" +
                                "<br><br>" +
                                "<b><span style=\"color:black; font-weight:bold;\">Lively Beach Culture\n</span></b>" +
                                "<br><br>" +
                                "Tamarin Bay can become quite busy, especially on weekends and holidays, offering a lively and energetic beach scene. Whether you're enjoying the surf, relaxing by the water, or taking in the local culture, the beach offers something for everyone.\n" +
                                "<br><br>" +
                                "<b><span style=\"color:black; font-weight:bold;\">Best Times to Visit\n</span></b>" +
                                "<br><br>" +
                                "The best time to visit Tamarin Bay is either early in the morning, between 8:00 and 11:00, or in the afternoon, from 13:00 to 16:00. These times offer the most favorable conditions for beach activities, with fewer crowds and enjoyable weather.\n" +
                                "\n<br><br>" +
                                "<b><span style=\"color:black; font-weight:bold;\">Activities at Tamarin Bay\n</span></b>";
                        model.image2 = fullPath + "/" + "tamarin_1" + ".jpg";
                        model.text4 = "In addition to surfing, Tamarin Bay offers a variety of water activities, including:\n" +
                                "<ul>" +
                                "<li>Stand-up paddleboarding\n</li>" +
                                "<li>Bodyboarding\n</li>" +
                                "<li>Catamaran tours\n</li>" +
                                "<li>Swimming with dolphins\n</li>" +
                                "<li>Speed boat trips\n</li>" +
                                "<li>Kayaking\n</li>" +
                                "</ul>" +
                                "Tamarin Bay Beach is a dynamic coastal destination that combines the thrill of surfing with the beauty of nature. Its authentic atmosphere, paired with a range of water activities and wildlife experiences, makes it an ideal spot for anyone looking to enjoy Mauritius’s vibrant beach culture.\n";
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
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    }
                }
                else if (Stash.getString("day").equals("day44")) {
                    if (position == 0) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "eau_bleu_1" + ".jpg";

                        model.text1 = "Eau Bleu Waterfall is a stunning natural wonder, known for its bright turquoise ponds that make it a perfect spot for nature lovers and adventurers. The crystal-clear water and lush surroundings make it a must-see for anyone looking to explore Mauritius' hidden gems.\n" +
                                "<br><br>" +
                                "Located on the southeast side of the island, about 20 minutes from Curepipe and 45 minutes from Port Louis, Eau Bleu (also called Cascade Rama) gets its name from the striking blue color of the water. This unique color comes from underground springs that feed the river and ponds, especially in the summer, giving them that signature turquoise glow.\n";
                        model.title1 = "";
                        model.image1 = fullPath + "/" + "eau_bleue_5" + ".jpg";
                        model.text2 = "For the adventurous, Eau Bleu offers activities like canoeing and cliff jumping. The falls range from 8 to 15 meters in height, making it a great spot for swimming and diving into the cool waters. Just make sure you have basic swimming skills before diving in, and avoid swimming right after a big meal or drinking alcohol. The water level in the ponds can vary depending on the season.\n" +
                                "<br><br>" +
                                "If you’re planning to explore all five waterfalls, bring sunscreen, sunglasses, closed-toe shoes, and the right clothing for hiking. The trails can be steep or covered in roots, so you’ll need an average level of fitness to make your way around. During the rainy season, the area can get slippery, so be extra careful.\n" +
                                "<br><br>" +
                                "Eau Bleu’s rich greenery and peaceful atmosphere make it an ideal spot to unwind. You’ll feel refreshed just being there, surrounded by nature. Several companies also offer canoe trips and outdoor activities at Eau Bleu, perfect for adventurers of all levels.\n";
                        model.title2 = "";
                        model.text3 = "";
                        model.image2 = fullPath + "/" + "eau_bleu_4" + ".jpg";
                        model.text4 = "The best time to visit is during the rainy season (January to March) when the waterfalls are at their fullest. If you visit between April and August, the water level may be lower unless there’s been recent rain. From September to early December, some waterfalls might even dry up completely.\n";
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
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 1) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "pont_naturel_2" + ".jpg";

                        model.text1 = "Pont Naturel, located in the southern part of Mauritius, is a natural bridge made entirely of rock formations shaped by nature over time. The waves crash dramatically against the cliffs below, making it an ideal spot to take in the raw beauty of the ocean and surrounding landscape. The sound of the waves and the untouched scenery add to the peacefulness of this remote location.\n";
                        model.title1 = "";
                        model.image1 = fullPath + "/" + "pont_naturel_3" + ".jpg";
                        model.text2 = "If you're feeling brave, you can cross the bridge, but be extremely careful—the strong currents and slippery rocks can be dangerous if you slip. It's not recommended for children or anyone unsure on their feet. Despite the risk, it’s one of the most peaceful and scenic places on the island, making it a great spot for photos or simply appreciating the natural wonders of Mauritius. The stunning contrast between the deep blue sea and the rugged cliffs is truly unforgettable. Many visitors come just to sit and admire the view, making it a hidden gem for those looking to escape the usual tourist crowds.\n";
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

                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 2) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "le_souffleur_1" + ".jpg";
                        model.text1 = "A must-do activity in Mauritius is hiking to Le Souffleur. The area is known for its unique rock formations, powerful waves that crash against the cliffs, and huge sprays of water. There’s even a hidden beach that few people ever visit, adding to the adventure.\n" +
                                "<br><br>" +
                                "Le Souffleur is close to both Pont Naturel and Gris Gris beach, so it’s easy to plan a day trip to see all three in one go. Located near the village of L'Escalier in southern Mauritius, Le Souffleur isn’t accessible by bus, so you’ll need to rent a car. Be warned—using Google Maps alone might get you lost. Instead, search for \"Savannah Sugar Industry\" and follow the signs when you get close.\n" +
                                "<br><br>" +
                                "Le Souffleur is one of the island’s most unique natural wonders. If the sea is rough that day, you might witness the impressive blowhole effect, where waves hit the cliffs and send water shooting high into the air. It’s a sight you won’t want to miss if you’re visiting Mauritius!\n";
                        model.title1 = "";
                        model.image1 = fullPath + "/" + "le_souffleur_2" + ".jpg";
                        model.text2 = "Nearby, you’ll also find the secluded Savinia Beach, one of the least crowded beaches on the island. Before reaching Savinia, look out for two natural arches, which are sometimes confused with Pont Naturel. There’s also a small cove close by, but be cautious—the strong currents make swimming there unsafe.\n";
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
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 3) {

                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "gris_gris_1" + ".jpg";
                        model.text1 = "The primary beach area features an expansive green field with a commanding view of dramatic cliffs and enormous waves. There are benches and a pavilion available for relaxation while taking in the scenery.\n" +
                                "To the left, a concrete staircase descends to the beach, but swimming is strongly discouraged due to the high danger level. The powerful waves can swiftly overwhelm swimmers. Instead, enjoy a leisurely stroll along the beach, heading toward a small cave at the far end.\n";
                        model.title1 = "Secret Caves at Gris Gris";
                        model.image1 = fullPath + "/" + "grisgris_2" + ".jpg";
                        model.text2 = "In addition to the cave on the far left side of the beach, two other hidden caves can be discovered at Gris Gris. These are more challenging to reach, involving a descent down a cliff and walking through the water.\n" +
                                "Caution is advised against going all the way down, as water levels can fluctuate unpredictably, and the current is often too strong.\n" +
                                "<br><br>" +
                                "For those eager to explore the secret caves at Gris Gris, head towards the cliff's edge directly across from the parking lot. Upon reaching the spot, descend only about halfway to catch a glimpse of the caves on your right.\n" +
                                "<br><br>" +
                                "It's important to bear in mind that entering the caves could pose risks if the water level rises!\n" +
                                "Gris Gris beach is intricately connected to the village of Souillac, which relies heavily on tourism for its revenue. " +
                                "<br><br>" +
                                "Established 200 years ago as a port for ships sailing from Europe to India, Souillac has a rich history worth exploring. Plan your day strategically to make the most of your visit to the southern part of Mauritius, and consider including a visit to Rochester Falls, just outside the village, renowned for its distinctive rectangular-sided rocks.\n" + "The name \"Gris Gris\" adds an intriguing dimension to the experience. Upon entering the beach, a large sign displays the history behind the name. According to local tradition, \"Gris Gris\" is linked to the African amulet known as the “Gris Gris” and its association with the tumultuous coastline. However, the story takes an unexpected turn, suggesting that Gris Gris might have been the name of a puppy belonging to a French cartographer who visited the coast in 1753.<br>";
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
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 4) {

                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "la_roche_qui_pleure" + ".jpg";
                        model.text1 = "This mystical site derives its name from the visual impression it creates: as water trickles down its walls, the cliffs appear to shed tears. Even more astonishing, some claim to recognize the eroded features of the Mauritian poet Robert Edward Hart."
                                + "<br><br>"
                                + "From a geographical and climatic standpoint, unlike other parts of the island, La Roche qui Pleure lacks coral reefs. Consequently, its shores are more exposed to the assaults of the ocean. This absence of a natural barrier results in more powerful and spectacular waves, a stark contrast to the tranquil lagoons typically associated with the island. The region is influenced by strong winds and seasonal variations, shaping its unique landscape and marine dynamics.";
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
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 5) {

                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "rochester_falls_1" + ".jpg";
                        model.text1 = "Rochester Falls may not be the largest or most famous waterfall in Mauritius, but it’s definitely worth a visit if you’re in the area. To get there, just follow the handmade signs that lead you from the main road through Souillac. The path can be a little tricky, with a rough, stony track, but it’s easy to follow. Local vendors may offer to guide you to a parking spot, and it's polite to offer them a small tip. After a short five-minute walk from your car, you’ll find yourself at the waterfall, where the water cascades from the sugarcane fields above.\n" +
                                "<br><br>" +
                                "Located in southern Mauritius, Rochester Falls is known for its unique volcanic rock formations. The water flows through these square-shaped rocks, creating a visually stunning effect that sets this waterfall apart from others on the island. It’s a peaceful spot, popular with both locals and tourists, offering a serene pond for swimming. Surrounded by a wild, lush forest, it’s a perfect escape from the everyday hustle, making it an ideal spot for a relaxing day with friends.\n";
                        model.title1 = "A few tips:";
                        model.image1 = "";
                        model.text2 = "<ul>" +
                                "<li>It’s best to avoid visiting during or after heavy rainfall.</li>" +
                                "<li>Be cautious if you plan to cliff jump, as the rocks can get slippery.</li>" +
                                "</ul>";
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
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 6) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "maconde_1" + ".jpg";
                        model.text1 = "Maconde Viewpoint is perched along the southern coast of Baie du Cap, a quiet village known for its rugged beauty and untouched coastlines. The viewpoint sits atop a small rocky cliff on a curved section of the coastal road. From here, you'll see an incredible landscape with rich red earth, lush green forests, rows of palm trees, and the sparkling blue waters of the Indian Ocean. It's a view that truly captures the heart.\n";
                        model.title1 = "";
                        model.image1 = fullPath + "/" + "maconde_2" + ".jpg";
                        model.text2 = "There's an interesting story behind the name \"Maconde.\" Some say it dates back to the days of slavery when runaway slaves from Mozambique’s Makonde tribe found safety in this area. Others believe it's named after Governor Jean Baptiste Henri Conde, who built an outlook on the cliff.\n" +
                                "<br><br>" +
                                "Getting to this area wasn’t always easy. The first road was only built in the 1920s, and the rough terrain and low-lying coast made construction tough. Recent updates have improved safety, but the drive along the winding basalt cliffs, with the sound of waves crashing against the rocks, is still as mesmerizing as ever. It’s a favorite spot for people who love watching the powerful ocean swells.\n" +
                                "<br><br>" +
                                "To reach the viewpoint itself, you'll need to climb a narrow set of stairs. At the top, you’ll be rewarded with stunning views of the ocean, the coastal village nearby, and the sight of local fishermen along the shore.\n";
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
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 7) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "la_prairie_2" + ".jpg";
                        model.text1 = "As you drive along Mauritius' scenic southern coast, it’s hard to miss the charm of La Prairie Beach. The beach gets its name from the short grass that stretches all the way to the water, giving it a unique and peaceful vibe. The area is surrounded by unspoiled beauty, offering a perfect blend of grassy patches and sandy shores. La Prairie is a visual feast, with lots of shady spots where you can escape the crowd and a stunning lagoon that’s sure to captivate you. This small gem is situated in the Savanne region in the southwest, close to the village of Baie-du-Cap.\n";
                        model.title1 = "";
                        model.image1 = fullPath + "/" + "la_prairie_1" + ".jpg";
                        model.text2 = "Tucked between the picturesque village of Baie-du-Cap and the iconic Le Morne Brabant mountain, La Prairie offers breathtaking views of the famous peak. On clear days, the reflection of Le Morne in the water adds to the stunning scenery, making it a favorite for nature lovers and photographers alike. It’s usually quiet during the week, providing a peaceful retreat, though it can get busy on weekends and holidays when locals come to enjoy its natural charm.\n" +
                                "<br><br>" +
                                "While swimming isn’t recommended due to the strong currents, the calm atmosphere makes it an ideal spot for a picnic or a relaxing day of sunbathing. Families often come to enjoy the wide-open space, and it’s a great place for children to play safely on the grass while parents take in the view. Whether you're looking to unwind or simply admire Mauritius’ natural beauty, La Prairie is a must-visit.\n";
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
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    }
                }
                else if (Stash.getString("day").equals("day51")) {
                    if (position == 0) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "port_louis_3" + ".jpg";

                        model.text1 = "Le Caudan Waterfront is a vibrant commercial hub in Port Louis, Mauritius, featuring an array of amenities such as shops, banks, casinos, cinemas, restaurants, a marina, and the luxurious Le Labourdonnais hotel.\n";
                        model.title1 = "Historical Roots\n";
                        model.image1 = "";
                        model.text2 = "The name \"Le Caudan Waterfront\" honors Jean Dominique Michel de Caudan, who arrived in Mauritius in 1726 and established a saltpan near what is now the Robert Edward Hart Garden. This peninsula, formed around a fossil coral islet, has evolved over 250 years, once housing a powder magazine, observatories, and warehouses, with the sugar industry shaping much of its history until the Bulk Sugar Terminal opened in 1980.\n";
                        model.title2 = "Historically Significant Spots\n";
                        model.text3 = "Notable historical locations in Le Caudan include the Blue Penny Museum, which is situated in the Food Court, and the first meteorological observatory in the Indian Ocean, which was formerly a docks office. Different areas within the complex are named to reflect the island's colonial history:\n" +
                                "<br><br><ul>" +
                                "<li><b>Barkly Wharf:</b> Named after Sir Henry Barkly, Governor of Mauritius (1863-1870).\n</li>" +
                                "<li><b>Le Pavillon Wing:</b> Linked to Pavillon Street from an old map of Port Louis.\n</li>" +
                                "<li><b>Dias Pier:</b> Honors Diogo Dias, the navigator believed to have first charted the Mascarene Islands.\n</li>" +
                                "</ul>" +
                                "Le Caudan Waterfront combines modernity with rich historical significance, making it a must-visit destination.\n";
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

                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 1) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "aapravasi_ghat_1" + ".jpg";
                        model.text1 = "The Aapravasi Ghat is a historical site located in Port Louis, the capital city of Mauritius. It holds great significance in the history of the island and is recognized as a UNESCO World Heritage Site. The history of the Aapravasi Ghat is closely tied to the indentured labor system that shaped the social and economic landscape of Mauritius.\n" +
                                "<br><br>\n" +
                                "Here is a brief history of the Aapravasi Ghat:\n" +
                                "\n<br><br>" +
                                "<b>Indentured Labor System:</b> In the 19th century, after the abolition of slavery, the British turned to the system of indentured labor to meet the demand for cheap and abundant workforce for their colonies. Indentured laborers were recruited from various parts of India, as well as China and Africa, to work on plantations in Mauritius and other British colonies.\n" +
                                "\n<br><br>" +
                                "<b>Establishment of Aapravasi Ghat:</b> The Aapravasi Ghat was established in 1849 as the first site for the reception of indentured laborers in Mauritius. The name \"Aapravasi Ghat\" translates to \"immigration depot\" in Hindi. The location served as a processing center where indentured laborers arriving by ship were registered, housed temporarily, and assigned to various plantations across the island.\n" +
                                "\n<br><br>" +
                                "<b>Housing:</b> At the Aapravasi Ghat, indentured laborers had to endure substandard housing. They were often crowded into cramped barracks, and the site became a place where many experienced suffering, sickness, and death. The conditions were challenging, and the laborers endured a difficult transition from their home countries to the unfamiliar environment of Mauritius.\n" +
                                "\n<br><br>" +
                                "<b>End of Indenture:</b> The indenture system continued until the early 20th century when it was officially abolished. The Aapravasi Ghat continued to be used for processing immigrants until 1920. The location underwent many changes over the years that served various functions after the indenture system was discontinued.\n" +
                                "\n<br><br>" +
                                "<b>UNESCO World Heritage Site:</b> In 2006, the Aapravasi Ghat was designated as a UNESCO World Heritage Site in recognition of its historical importance in the global migration of indentured laborers and its impact on the multicultural identity of Mauritius.\n";
                        model.title1 = "";
                        model.image1 = fullPath + "/" + "aapravasi_ghat_2" + ".jpg";
                        model.text2 = "Today, the Aapravasi Ghat stands as a poignant symbol of the struggles and resilience of the indentured laborers who played a crucial role in shaping Mauritius's cultural and social fabric. \n" +
                                "<br><br>\n" +
                                "This UNESCO World Heritage site is open to visitors, offering them a chance to explore its rich history and gain insights into this critical period of the island's past. The exhibits and memorials here provide a deeper understanding of the lives and contributions of these laborers, making it a significant destination for those interested in Mauritian heritage.\n";
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
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 3) {

                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "pamplemousse_garden" + ".jpg";
                        model.text1 = "The Sir Seewoosagur Ramgoolam Botanic Garden, also known as the Pamplemousses Botanic Garden, is a major tourist attraction located in Pamplemousses, near Port Louis, Mauritius. It is the oldest botanical garden in the Southern Hemisphere. Established in 1770 by Pierre Poivre, the garden spans about 37 hectares (91 acres) and is famous for its large pond filled with giant water lilies (Victoria amazonica).\n" +
                                "<br><br>" +
                                "Over the years, the garden has had several names that reflect its changing status and ownership. Some of these names include 'Jardin de Mon Plaisir,' 'Jardin des Plantes,' 'Le Jardin National de l’Ile de France,' 'Jardin Royal,' and 'Jardin Botanique des Pamplemousses.' On September 17, 1988, it was officially named the \"Sir Seewoosagur Ramgoolam Botanic Garden\" to honor the first prime minister of Mauritius.\n";
                        model.title1 = "";
                        model.image1 = fullPath + "/" + "botanical_garden_1" + ".jpg";
                        model.text2 = "In addition to the stunning giant water lilies, the garden is home to a variety of spices, ebonies, sugar canes, and 85 different types of palm trees from regions including Central America, Asia, Africa, and the Indian Ocean islands. Many notable people, such as Princess Margaret, Indira Gandhi, and François Mitterrand, have planted trees in the garden.\n" +
                                "<br><br>" +
                                "Located about seven miles northeast of Port Louis, the garden has a rich history that dates back to 1729 when it was set aside for colonist P. Barmont. It changed hands several times, with different owners contributing to its growth and development. Today, the garden covers approximately 25,110 hectares (62,040 acres), with part of the area serving as an experimental station.\n";
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
                        model.text9 = "Over the years, the garden faced times of neglect and challenges, but directors like James Duncan worked hard to restore and improve it. In 1866, during a malaria outbreak, the garden played an important role by growing Eucalyptus trees to help fight the disease.\n" +
                                "<br><br>" +
                                "In 1913, the Department of Agriculture took over the garden, managing its growth and upkeep. Notably, after the death of Seewoosagur Ramgoolam in 1985, part of the garden was dedicated to a crematorium, marking the first time someone was cremated on its grounds.\n" +
                                "<br><br>" +
                                "Today, the Sir Seewoosagur Ramgoolam Botanic Garden is a symbol of Mauritius's botanical heritage, inviting visitors to explore its beautiful landscapes and rich history.\n";
                        model.title8 = "Chateau de Mon Plaisir";
                        model.text10 = "";
                        model.image4 = fullPath + "/" + "botanical_garden_4" + ".jpg";
                        model.text11 = "Until 1839, the Chateau de Mon Plaisir was a simple building with a flat roof and circular verandahs. The current single-story structure, built by the English in the mid-19th century, is now a National Monument, which means it is legally protected. Visitors can enjoy a lovely view of the Moka Range and the Peak of Pieter Both from the Chateau, making it a beautiful spot to take in the scenery.\n";
                        model.image5 = "";
                        model.text12 = "";
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 2) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "port_louis_4" + ".jpg";
                        model.text1 = "\n" + "The Port Louis Central Market, also known as the Bazaar of Port Louis, is the heart of the city’s shopping scene, buzzing with energy and life. The market is a hub for both locals and tourists, offering a lively, authentic experience. At the center, you’ll find rows of vibrant stalls overflowing with fresh fruits and vegetables, creating a colorful display that’s as much a feast for the eyes as it is for the taste buds. The aroma of fresh produce and the friendly chatter of vendors add to the market’s charm, making it a must-see when exploring Port Louis.";
                        model.title1 = "Craft Market\n";
                        model.image1 = "";
                        model.text2 = "Head up to the first floor, and you’ll discover the Craft Market, a treasure trove of locally made souvenirs, spices, and handicrafts. From intricate woven baskets to handcrafted jewelry, there’s something for every taste and budget. It’s the perfect place to find a unique gift or a meaningful memento to take home with you. The variety of items on display reflects Mauritius' rich cultural blend, and the affordable prices make it easy to indulge. Whether you're browsing or buying, the Craft Market is an experience not to be missed.\n";
                        model.title2 = "Street Food Delights at Central Market\n";
                        model.text3 = "";
                        model.image2 = fullPath + "/" + "port_louis_5" + ".jpg";
                        model.text4 = "No visit to the Central Market is complete without sampling some of the local street food. Just steps away from the main market area, you’ll find stalls offering mouthwatering treats that are a favorite among locals. Be sure to try dhall puri, a soft flatbread filled with a mix of split chickpeas and spices, or cool off with alouda, a sweet and refreshing drink made with milk, basil seeds, and jelly.\n" +
                                "<br><br>" +
                                "For those who love snacks, the market has plenty to offer. Try the crispy pastry filled with spiced potatoes, known as samosas, or the fritters called bajas and gato piment, which are made with yellow split peas and chickpea flour and have a hint of chile. These delicious bites are perfect for a quick snack as you continue exploring.\n" +
                                "<br><br>" +
                                "Open throughout the week, the Port Louis Central Market is a lively gathering spot that gives you a true glimpse into Mauritian life. You can shop, eat, or just take in the atmosphere—either way, you will remember this experience long after you leave. This is the place to be if you want to fully immerse yourself in the local culture!\n";
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
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 4) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "grand_baie_1" + ".jpg";
                        model.text1 = "Grand Bay, also known as Grand Baie, is a coastal village and a popular tourist spot located in the Rivière du Rempart district at the northern tip of Mauritius. In the 17th century, the Dutch called it De Bogt Zonder Eyndt, meaning \"Bay Without End,\" a name that reflects the beauty of the area. Today, Grand Bay is the most popular vacation destination in Mauritius.\n" +
                                "<br><br>" +
                                "The village is famous for its stunning emerald waters and lively atmosphere, both during the day and at night. Visitors can enjoy various water activities like safe swimming, sailing, windsurfing, and water skiing. Grand Bay is also the starting point for deep-sea fishing trips and boat tours to the nearby northern islands of Mauritius, such as Gunners' Quoin, Flat Island, Round Island, and Serpent Island.\n" +
                                "<br><br>" +
                                "In and around Grand Bay, you'll find a wide range of fashion and craft shops, hotels, and restaurants. The village blends contemporary shopping malls and stores carrying international brands with classic local businesses, some of which have been operating for close to fifty years. This makes it an excellent place to shop for clothing, jewelry, textiles, and souvenirs.\n";
                        model.title1 = "Nightlife";
                        model.image1 = "";
                        model.text2 = "Grand Bay is famous for its lively nightlife, featuring some of the best bars and nightclubs on the island. Popular spots include Banana Café, Zanzibar, Les Enfants Terribles, and the well-known Buddha Bar. Night owls can enjoy the vibrant atmosphere, with parties starting around midnight and lasting into the early morning.";
                        model.title2 = "Location";
                        model.text3 = "Grand Bay is located in the Rivière du Rempart district on the northwest side of Mauritius. It is about 25 kilometers north of Port Louis, the capital of Mauritius. The village sits between Pereybere to the east and Trou aux Biches to the west. Driving to Port Louis takes around 30 minutes while getting to the airport takes about 1 hour and 30 minutes.\n" +
                                "<br><br>" +
                                "Grand Bay has a good bus system, making it easy to travel to important places in Mauritius, including Port Louis, Triolet, Goodlands, and Grand Gaube.\n" +
                                "<br><br>\n" +
                                "<b><span style=\"color:black; font-weight:bold;\">Climate</span></b>" +
                                "<br><br>" +
                                "Grand Bay enjoys a pleasant climate throughout the year, featuring warm summers and mild winters. It is a wonderful place to travel any time of year because of its protected beaches and lagoons.\n" +
                                "<br><br>\n" +
                                "<b><span style=\"color:black; font-weight:bold;\">Activities</span></b>";
                        model.image2 = fullPath + "/" + "parasailing" + ".jpg";
                        model.text4 = "Visitors to Grand Bay can enjoy many activities beyond just sunbathing. You can go safe swimming, sailing, windsurfing, parasailing, and participate in various water sports. The area is perfect for learning about underwater marine life because of attractions like the Underwater Sea Walk, submarine excursions, and the Underwater Scooter. Merely a few kilometers from the coast, divers can discover several favorable diving spots amidst the coral reefs.\n";
                        model.title3 = "Hotels";
                        model.text5 = "The hospitality scene in Mauritius, especially in Grand Bay, has changed a lot over the years. Once a hidden gem for only a few travelers, Mauritius is now a popular vacation spot with many hotel options, guesthouses, private apartments, and villas. Grand Bay offers something for everyone, from luxury resorts to cozy rental rooms.\n" +
                                "<br><br>" +
                                "Some well-known hotels in Grand Bay include Veranda Grand Bay, Le Mauricia Hotel, 20 Sud, Ocean Villas, Royal Palm, Ventura Hotel, and Merville Beach Resort.\n";
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

                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    }
                }
                else if (Stash.getString("day").equals("day52")) {
                    if (position == 0) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "chamarel_2" + ".jpg";
                        model.text1 = "The Seven Coloured Earths are a must-see geological wonder located in the Chamarel plain of the Rivière Noire District in southwest Mauritius. This natural phenomenon features small sand dunes that show off seven different colors—red, brown, violet, green, blue, purple, and yellow. What makes this spot so unique is the way the colors naturally settle in separate layers, forming striped patterns across the hills. Over time, the rain has carved these colorful dunes into fascinating shapes that resemble the texture of meringue.\n" +
                                "<br><br>" +
                                "These sands come from volcanic rock that broke down into clay, eventually turning into soil through a process called hydrolysis. The red and anthracite shades come from iron, while aluminum gives the sands their blue and purplish tones. Scientists believe the colors are linked to how the volcanic rock cooled at different rates, but they still don’t fully understand how the layers form so consistently.\n" +
                                "<br><br>" +
                                "The name \"Seven Coloured Earths\" isn’t the official title, but a simple description of what you'll see. Sometimes it’s called \"Chamarel Seven Coloured Earths\" or \"Terres des Sept Couleurs\" in French, but whatever the name, the sight is unforgettable.\n" +
                                "<br><br>" +
                                "This fascinating phenomenon can even be recreated on a small scale. If you mix the differently colored sands together, they'll separate over time, forming layers just like in nature.\n" +
                                "<br><br>" +
                                "Since the 1960s, the Seven Coloured Earths have become a top tourist attraction in Mauritius. Today, the dunes are protected by a wooden fence, and visitors can no longer walk on them. Instead, you can take in the breathtaking view from observation posts along the fence. And if you want a little souvenir, local shops sell little test tubes filled with colored sand.\n";
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
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 1) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "chamarel_1" + ".jpg";
                        model.text1 = "Chamarel Waterfall, the tallest single-drop waterfall in Mauritius, stands at about 100 meters high and is a sight to behold. Surrounded by lush greenery, this natural wonder is powered by three streams that merge and flow into the Saint Denis River, creating a powerful cascade. At its peak, the waterfall flows at over 40,000 cubic meters per minute.\n" +
                                "<br><br>" +
                                "As you drive along the 3-kilometer road through the Seven-Coloured Earth Geopark, you'll pass the viewpoint for Chamarel Waterfall. You can reach the ideal location to witness this magnificent waterfall by taking a brief hike through dense forest.\n" +
                                "<br><br>" +
                                "The waterfall pours over a basalt cliff into an oval pool below, then winds through a 6-kilometer canyon lined with tropical forests before eventually reaching Baie du Cap. It's a living reminder of Mauritius' volcanic past, with the landscape shaped by lava flows from two different periods. The bottom layer of basalt is 10 to 8 million years old, while the top layer dates from 3.5 to 1.7 million years ago.\n" +
                                "<br><br>" +
                                "For the adventurous, there's a three-hour trek through the hidden valley leading to the base of the waterfall. Once there, you can take a dip in the cool waters and feel the refreshing spray of the waterfall above.\n" +
                                "<br><br>" +
                                "The combination of the sights and sounds creates a true tropical rainforest experience, leaving you with memories of this amazing natural beauty.\n";
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
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 2) {

                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "black_river_georges_2" + ".jpg";
                        model.text1 = "Black River Gorges National Park is located in the hilly southwestern part of Mauritius. Established on June 15, 1994, and managed by the National Parks and Conservation Service, the park covers an area of 67.54 square kilometers. It features different habitats like humid upland forests, drier lowland forests, and marshy heathlands. Visitors can explore two information centers, picnic areas, and 60 kilometers of hiking trails. There are also four research stations focused on conservation efforts, run by the National Parks and Conservation Service along with the Mauritian Wildlife Foundation.\n" +
                                "<br><br>" +
                                "The park was created to protect what remains of the island's rainforest. Over time, non-native plants like Chinese guava and privet, along with animals such as the rusa deer and wild pigs, have damaged parts of the forest. To protect native species, some areas of the park have been fenced off, and efforts are being made to control the invasive species.\n";
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
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 3) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "rhumerie_de_chamarel_1" + ".jpg";
                        model.text1 = "Set in the fertile valley of southwestern Mauritius, the Rhumerie de Chamarel is a must-visit destination. Surrounded by lush sugarcane fields that blend with pineapple groves and tropical fruit, this rum distillery offers a warm and authentic experience.\n" +
                                "<br><br>" +
                                "Visitors to Rhumerie de Chamarel can take a thorough tour of the distillery, taste some of the best rums, and dine at the distillery's own restaurant, L'Alchimiste. The design of the distillery emphasizes its connection with nature, using natural materials like wood, stone, and water to create a peaceful atmosphere that reflects the beauty of the surrounding landscape.\n" +
                                "<br><br>" +
                                "<b><span style=\"color:black; font-weight:bold;\">Guided Tour of Rhumerie de Chamarel\n</span></b>" +
                                "<br><br>" +
                                "The guided tour at Rhumerie de Chamarel takes you behind the scenes of the rum-making process, led by knowledgeable guides. You’ll learn about how the sugarcane is carefully selected and cultivated on the property. The distillery uses special fermentation techniques to enhance the flavors and aromas of the rum. The tour lasts about 30 to 40 minutes and is offered in both English and French.\n" +
                                "<br><br>" +
                                "<b><span style=\"color:black; font-weight:bold;\">Rum Tasting\n</span></b>";
                        model.title1 = "";
                        model.image1 = fullPath + "/" + "rhumerie_de_chamarel_3" + ".jpg";
                        model.text2 = "The rum tasting is, of course, the highlight of the trip. At the end of the tour, you’ll get the chance to try a variety of agricultural rums made from pure cane juice, rather than molasses. This unique process gives the rum its distinctive flavors. The tasting features White Rum, Coeur de Chauffe, Chamarel Liquors, Exotic-Flavored Rums, and the distillery’s Old Rum.\n";
                        model.title2 = "About the Rum at Rhumerie de Chamarel\n";
                        model.text3 = "";
                        model.image2 = fullPath + "/" + "rhumerie_2" + ".jpg";
                        model.text4 = "Rhumerie de Chamarel focuses on producing high-quality, eco-friendly agricultural rum. From the cultivation of the cane to the fermentation and distillation process, everything is done with care. Unlike traditional rum made from molasses, their rum is distilled from fresh, fermented cane juice, creating a unique taste.\n" +
                                "<br><br>" +
                                "The distillery is also committed to sustainability. They recycle bagasse (the fibrous material left after the cane is processed) to generate energy, purify fumes from the distillation process, use ashes as fertilizer, and even reuse steam for watering the gardens.\n" +
                                "<br><br>" +
                                "<b><span style=\"color:black; font-weight:bold;\">Additional Experiences at Rhumerie de Chamarel\n</span></b>" +
                                "<br><br><ul>" +
                                "<li><b>The Sugar Cane Spirit Shop:</b> Here, you can buy local products like the distillery's rum, Mauritian crafts, jewelry, and even an exclusive clothing line designed by Rhumerie de Chamarel.\n</li>" +
                                "<li><b>Restaurant L’Alchimiste:</b> Enjoy dishes made from local ingredients in a beautiful setting surrounded by tropical plants, overlooking the sugarcane fields and mountains. Finer wines from France and abroad complement the menu's unusual dishes, which include palm hearts and meat from deer or wild pigs.\n</li>" +
                                "</ul>" +
                                "<b><span style=\"color:black; font-weight:bold;\">Visitor Information\n</span></b>" +
                                "<br><br><ul>" +
                                "<li>The distillery is open Monday through Saturday, from 9:30 a.m. to 5:30 p.m.\n</li>" +
                                "<li>The guided tour and rum tasting last about 30 to 40 minutes.\n</li>" +
                                "<li>Children under 12 must be accompanied by their parents, but they are not allowed to participate in the rum tasting.\n</li>" +
                                "</ul>";
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
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 4) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "tamarin_3" + ".jpg";
                        model.text1 = "Tamarin Bay Beach is a gorgeous beach location on Mauritius' west coast, situated just past the <b>Black River</b> village. Known for its natural beauty and ideal surfing conditions, it attracts both locals and tourists.\n";
                        model.title1 = "Surfing History and Recognition";
                        model.image1 = fullPath + "/" + "tamarin_2" + ".jpg";
                        model.text2 = "This beach, formerly known as <b>Santosha Bay,</b> was kept a secret by surfers who considered it a hidden treasure. The name <b>Santosha</b> can still be faintly seen painted on a few buildings. Tamarin Bay gained international fame with the release of the 1974 surf documentary <b>\"Forgotten Island of Santosha\"</b> by Larry and Roger Yates, which brought global attention to its remarkable surf breaks. The bay features two renowned surfing spots: <b>‘Dal’</b> on the southern side and <b>‘Black Stone’</b> to the north.";
                        model.title2 = "Dolphins and Wildlife\n";
                        model.text3 = "Tamarin Bay is a haven for dolphins, especially <b>Spinner</b> and <b>Bottlenose dolphins,</b> which are frequently spotted in the early morning hours before heading out to the open sea. Many boat companies offer morning trips for tourists to observe and swim with these playful creatures, making it a top destination for wildlife enthusiasts as well.\n" +
                                "<br><br>" +
                                "<b><span style=\"color:black; font-weight:bold;\">Surfing Hub\n</span></b>" +
                                "<br><br>" +
                                "Since the 1970s, Tamarin Bay has been a major surfing hub, introduced primarily by Australians living on the island’s west coast. The area’s strong waves and ideal conditions continue to draw surf enthusiasts from around the world, and surfing here is often considered a special privilege.\n" +
                                "<br><br>" +
                                "<b><span style=\"color:black; font-weight:bold;\">Authentic and Laid-Back Atmosphere\n</span></b>" +
                                "<br><br>" +
                                "Despite its fame as a surfing hotspot, Tamarin Bay retains its authentic charm. Local families frequent the beach for leisurely strolls or relaxed afternoons, creating a welcoming and laid-back environment. Its vibrant yet genuine atmosphere makes it appealing to both tourists and locals.\n" +
                                "<br><br>" +
                                "<b><span style=\"color:black; font-weight:bold;\">Swimming Caution\n</span></b>" +
                                "<br><br>" +
                                "While Tamarin Bay is excellent for surfing, swimming is not recommended for children or inexperienced swimmers. The strong currents and large waves can be unpredictable, making it less safe for casual swimming.\n" +
                                "<br><br>" +
                                "<b><span style=\"color:black; font-weight:bold;\">Lively Beach Culture\n</span></b>" +
                                "<br><br>" +
                                "Tamarin Bay can become quite busy, especially on weekends and holidays, offering a lively and energetic beach scene. Whether you're enjoying the surf, relaxing by the water, or taking in the local culture, the beach offers something for everyone.\n" +
                                "<br><br>" +
                                "<b><span style=\"color:black; font-weight:bold;\">Best Times to Visit\n</span></b>" +
                                "<br><br>" +
                                "The best time to visit Tamarin Bay is either early in the morning, between 8:00 and 11:00, or in the afternoon, from 13:00 to 16:00. These times offer the most favorable conditions for beach activities, with fewer crowds and enjoyable weather.\n" +
                                "\n<br><br>" +
                                "<b><span style=\"color:black; font-weight:bold;\">Activities at Tamarin Bay\n</span></b>";
                        model.image2 = fullPath + "/" + "tamarin_1" + ".jpg";
                        model.text4 = "In addition to surfing, Tamarin Bay offers a variety of water activities, including:\n" +
                                "<ul>" +
                                "<li>Stand-up paddleboarding\n</li>" +
                                "<li>Bodyboarding\n</li>" +
                                "<li>Catamaran tours\n</li>" +
                                "<li>Swimming with dolphins\n</li>" +
                                "<li>Speed boat trips\n</li>" +
                                "<li>Kayaking\n</li>" +
                                "</ul>" +
                                "Tamarin Bay Beach is a dynamic coastal destination that combines the thrill of surfing with the beauty of nature. Its authentic atmosphere, paired with a range of water activities and wildlife experiences, makes it an ideal spot for anyone looking to enjoy Mauritius’s vibrant beach culture.\n";
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
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    }
                }
                else if (Stash.getString("day").equals("day53")) {
                    if (position == 0) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "casela" + ".jpg";
                        model.text1 = "Casela World of Adventures, the most visited attraction in Mauritius, is nestled amidst sugarcane fields on the island’s western coast, with the stunning <b>Rempart Mountain</b> serving as its backdrop. Originally founded as a bird sanctuary in 1979, Casela has grown into a sprawling nature park offering a wide range of thrilling activities and unforgettable animal encounters.\n";
                        model.title1 = "";
                        model.image1 = fullPath + "/" + "botanical_garden_5" + ".jpg";
                        model.text2 = "";
                        model.title2 = "Exciting Activities at Casela\n";
                        model.text3 = "Casela provides something for everyone, from adrenaline-pumping adventures like ziplining, quad biking, and the exhilarating <b>Canyon Swing,</b> to more family-friendly options like the <b>African safari,</b> where visitors can see big cats, giraffes, rhinos, and zebras. There are four themed worlds to choose from, so you can customize your visit:\n" +
                                "<br><br><ul>" +
                                "<li><b>Mountain Kingdom:</b> Features activities like the Zig Zag Racer, Canyon Swing, and Zip and Splash Tour.\n</li>" +
                                "<li><b>Big Cats Kingdom:</b> Offers unique experiences such as walking with lions and interacting with cheetahs and caracals.\n</li>" +
                                "<li><b>Safari Kingdom:</b> Includes quad biking, giraffe feeding, Segway tours, and e-bike safaris.\n</li>" +
                                "<li><b>Middle Kingdom:</b> Provides opportunities to interact with and feed tortoises.\n</li>" +
                                "</ul>" +
                                "<b><span style=\"color:black; font-weight:bold;\">Action-Packed Mountain Adventures</span></b>";
                        model.image2 = fullPath + "/" + "casela_6" + ".jpg";
                        model.text4 = "<ul>" +
                                "<li><b>Ziplining:</b> Soar above the park on zip lines, taking in breathtaking aerial views.\n</li>" +
                                "<li><b>Canyon Swing:</b> Experience the thrill of freefalling from a 45-meter platform.\n</li>" +
                                "<li><b>Mountain Climbing:</b> Challenge yourself on the Via Ferrata Canyon Tour through sugarcane fields and scenic plateaus.\n</li>" +
                                "</ul>" +
                                "<b><span style=\"color:black; font-weight:bold;\">Safari and Animal Encounters</span></b>";
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
                        model.text9 = "<ul><li><b>Walk with Lions:</b> Stroll alongside lions with expert guides ensuring your safety.\n</li>" +
                                "<li><b>Big Cat Interactions:</b> Get up close with lions, cheetahs, and caracals.\n</li>" +
                                "<li><b>Drive-Thru Safari:</b> Take a 45-minute drive to observe lions in their natural habitat.\n</li>" +
                                "<li><b>E-Bike Safari & Segway Tour:</b> Explore the Yemen Nature Reserve on eco-friendly bikes or Segways.\n</li>" +
                                "<li><b>Camel Riding:</b> Enjoy a camel ride through the scenic park.\n</li>" +
                                "</ul>\n" +
                                "<b><span style=\"color:black; font-weight:bold;\">Family Activities\n</span></b>";
                        model.title8 = "";
                        model.text10 = "";
                        model.image4 = fullPath + "/" + "casela_3" + ".jpg";
                        model.text11 = "<ul><li><b>Zookeeper for a Day:</b> Kids can experience life as a zookeeper, participating in various activities.\n</li>" +
                                "<li><b>Petting Farm & Pony Ride:</b> Ideal for children to interact with farm animals.\n</li>" +
                                "<li><b>Giraffe, Ostrich, & Tortoise Feeding:</b> Engage in up-close encounters with these majestic animals.\n</li>" +
                                "</ul>" +
                                "<b><span style=\"color:black; font-weight:bold;\">Dining and Shopping</span></b>" +
                                "<br><br>" +
                                "Visitors can unwind at the <b>Casela Restaurant,</b> which overlooks <b>Tamarin Bay</b> and offers a variety of cuisines, including Mauritian, European, and Asian dishes. The park also features multiple food outlets to satisfy diverse tastes. Before leaving, stop by the <b>gift shops</b> to pick up souvenirs, from African crafts to locally made jewelry and textiles.\n" +
                                "<br><br>" +
                                "<b><span style=\"color:black; font-weight:bold;\">Conservation and History\n</span></b>" +
                                "<br><br>" +
                                "Since its inception, Casela has remained committed to conservation efforts. In February 2015, the park gained international recognition when <b>Her Royal Highness Princess Stephanie of Monaco</b> became its patron, further highlighting the park’s dedication to protecting endangered species. Today, Casela continues to expand its offerings while staying true to its mission of conservation, providing a memorable experience for visitors of all ages.\n";
                        model.image5 = "";
                        model.text12 = "";
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 1) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "la_preneuse_4" + ".jpg";

                        model.text1 = "<b>La Preneuse Beach,</b> located on the west coast of Mauritius, offers a serene setting with pristine, white sand that slopes gently into the shallow turquoise waters of the Indian Ocean. Though the sea is mostly calm, occasional strong currents can occur. The beach is spacious, providing ample room for vacationers, and offers breathtaking panoramic views, including the striking sight of <b>Mount Le Morne</b> set against the lagoon.";
                        model.title1 = "";
                        model.image1 = "";
                        model.text2 = "This beach destination is ideal for those seeking relaxation, as it is surrounded by various hotels catering to different comfort levels. Nearby, you'll find cafes, shops, and souvenir stores offering local goods. One of the notable landmarks here is the <b>Martello Tower,</b> a structure built by the British in the 1830s as a defense against potential French attacks. It now serves as a museum that provides insight into the region's history. Additionally, the <b>Black River</b> flows along the beach, and yachts and boats dot the horizon, offering sea excursions for tourists.";
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
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 2) {

                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
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
                        model.text10 = "Visitors to the <b>Martello Tower Museum</b> can explore its different floors, which include:\n" +
                                "<br><br><ul>" +
                                "<li><b>Basement:</b> Used for rainwater storage.</li>" +
                                "<li><b>Ground Floor:</b> Functioned as a storeroom and gunpowder armory.</li>" +
                                "<li><b>First Floor:</b> Reserved for the Chief Officer and soldier accommodations.</li>" +
                                "<li><b>Flat Roof:</b> Where cannons were installed for defense.</li>" +
                                "</ul>" +
                                "The museum features a collection of historical memorabilia, including <b>muskets,</b> the officer’s uniform, and personal belongings like cooking utensils and a fireplace, offering a glimpse into life during the era.\n" +
                                "<br><br>" +
                                "<b><span style=\"color:black; font-weight:bold;\">Opening Hours and Tours\n</span></b>" +
                                "<br><br><ul>" +
                                "<li><b>Tuesday to Saturday:</b> 09h30 to 17h00\n</li>" +
                                "<li><b>Sunday:</b> 09h30 to 13h00\n</li>" +
                                "<li><b>Closed:</b> Mondays, 1 May, 25 December, 1 & 2 January, 1 February, and 12 March.\n</li>" +
                                "</ul>" +
                                "Guided tours are available every half hour with no advance booking required. Visitors can simply arrive and pay the entrance fee, and a guide will lead them through the museum.\n" +
                                "<br><br>" +
                                "The <b>Martello Tower Heritage Museum</b> offers an immersive excursion into Mauritius's colonial past, making it an essential stop for history enthusiasts.\n";
                        model.image4 = "";
                        model.text11 = "";
                        model.image5 = "";
                        model.text12 = "";

                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 3) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "le_morne_1" + ".jpg";

                        model.text1 = "<b>Le Morne Brabant,</b> a UNESCO World Heritage Site since 2008, is a mountain located on the southwestern tip of Mauritius, jutting into the Indian Ocean. This culturally significant site served as a refuge for runaway slaves, known as <b>maroons,</b> from the 18th century until the early 1900s.";
                        model.title1 = "The Mountain's Natural Defense\n";
                        model.image1 = "";
                        model.text2 = "The mountain, with its vertical cliffs, steep slopes, and deep ravines, provided a natural fortress for the maroons seeking refuge from their pursuers. At its summit, a relatively flat plateau offered sanctuary to those who successfully navigated the dangerous ascent. The <b>V-Gap,</b> a wide gorge, served as the crucial entry point to the plateau, making access difficult and treacherous for both the maroons and those who sought to capture them.\n" +
                                "<br><br>" +
                                "Archaeological findings in the caves on Le Morne revealed ashy deposits from fires and the remains of a 300-year-old sheep, further confirming that maroons inhabited the summit, utilizing the resources they could secure to survive.\n";
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
                        model.text11 = "A striking sculpture by Haitian artist <b>Fritz Laratte</b> embodies the theme of liberation from slavery, depicting a slave whose hands are freed from chains through prayer, symbolizing the resilience and hope of the maroons.\n" +
                                "<br><br>" +
                                "<b><span style=\"color:black; font-weight:bold;\">Historical and Cultural Elements of Le Morne\n</span></b>";
                        model.image5 = fullPath + "/" + "brabant_7" + ".jpg";
                        model.text12 = "The cultural landscape of Le Morne extends beyond the mountain itself. <b>Trou-Chenille village,</b> with its rich history and heritage, includes five traditional huts preserved as part of an open-air museum. These huts portray daily life in the region during the 19th and 20th centuries, showcasing the simple yet meaningful existence of the local people.\n" +
                                "<br><br>" +
                                "<b><span style=\"color:black; font-weight:bold;\">Archaeological Discoveries</span></b>";

                        model.image6 = fullPath + "/" + "brabant_10" + ".jpg";
                        model.text13 = "Archaeological evidence has unearthed the remnants of a 19th-20th century settlement called <b>Macaque,</b> likely linked to Malagasy and Mozambican families. An abandoned cemetery, discovered at the foot of Le Morne, is believed to have been used by individuals from these regions. These findings provide insight into the multi-ethnic composition of the maroons and their descendants.\n" +
                                "<br><br>" +
                                "<b><span style=\"color:black; font-weight:bold;\">Cultural Traditions and Community\n</span></b>" +
                                "<ul>" +
                                "\t<li><b>Stella Maris Chapel:</b> The first Catholic chapel in Le Morne, originally built in <b>1891,</b> was moved to <b>L’Embrasure</b> during World War I and rebuilt in <b>1987</b> after being destroyed by a cyclone.\n" +
                                "\t<li><b>Sega Nights:</b> A weekly gathering where locals shared stories and performed Sega music beneath the ancient <b>Sega Tree</b> (Banyan Tree), using traditional instruments.\n" +
                                "\t<li><b>Fishing:</b> Fishing has been a key part of local culture since the 18th century. Villagers employ traditional methods like <b>Seine fishing</b> and <b>Kazie</b> (basket trap) fishing, which continue to be passed down through generations.\n" +
                                "</ul>" +
                                "<b><span style=\"color:black; font-weight:bold;\">Historical Landmarks and Daily Life</span></b>";
                        model.image7 = fullPath + "/" + "brabant_11" + ".jpg";
                        model.text14 = "<ul>" +
                                "\t<li><b>Limekiln:</b> Constructed by the Cambier family, this <b>20th-century limekiln</b> involved villagers in the production of quick lime by burning corals and shells.\n</li>" +
                                "\t<li><b>Grilled Coffee:</b> The distinct aroma of grilled coffee is a source of local pride. Beans from <b>Chamarel</b> are roasted in a cast iron pot over a fire and then ground using a mortar and pestle.\n</li>" +
                                "\t<li><b>Ilot Fourneau:</b> Villagers would travel by boat to this nearby island to collect fresh water from a spring. Historical records show that <b>Ilot Fourneau</b> was used as a British military post during the 18th and 19th centuries.\n</li>" +
                                "</ul>" +
                                "Le Morne Brabant stands as a deeply symbolic and culturally rich landscape, representing the history of resistance, survival, and liberation. It remains a sacred site for many, particularly the <b>Rastafarian community,</b> who view the mountain as a place of spiritual connection and meditation.\n";
                        model.image8 = "";
                        model.text15 = "";

                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 4) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "flic_en_flac_3" + ".jpg";
                        model.text1 = "After Grand Baie, Flic en Flac, on the island's west coast, has grown to be Mauritius's second-most popular tourist destination. Over the past two centuries, this area has transformed from a small fishing village into a vibrant town, attracting both international tourists and local Mauritians.\n";
                        model.title1 = "";
                        model.image1 = fullPath + "/" + "flic_en_flac_1" + ".jpg";
                        model.text2 = "Flic en Flac offers an ideal holiday experience with its stunning 8 km beach, lined with white sand and crystal-clear blue lagoons. Numerous restaurants, hotels, and shops cater to visitors, making it a perfect spot for watersports, afternoon strolls, sunbathing, or simply relaxing under the shade of <b>Casuarina Trees.</b> The lagoon, protected by a coral reef, provides safe swimming conditions and various watersport activities.\n" +
                                "<br><br>" +
                                "During the day, Flic en Flac is bustling with activity, offering local street food like <b>DhalPuri</b>, a delicious flatbread filled with curry, and <b>fried noodles with Mauritian meatballs.</b> On weekends, the beach comes alive with activity as locals congregate for picnics, singing, and dancing to the beat of Sega music, creating a joyous atmosphere. atmosphere. Thanks to its many restaurants and nightclubs, Flic en Flac is also a favorite nighttime destination for both locals and visitors.\n" +
                                "<br><br>" +
                                "Despite its beauty, it's important to note that the beach has corals and sea urchins, so caution is advised when walking barefoot.\n";
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

                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    }
                }
                else if (Stash.getString("day").equals("day54")) {
                    if (position == 0) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "mahebourg" + ".jpg";
                        model.text1 = "Nestled in the heart of the traditional Mauritian Village, the newly developed Mahebourg Waterfront offers an idyllic setting for a leisurely stroll and a captivating journey into local Mauritian history. Take the opportunity to explore the naval museum and delve into the details of the epic battle that unfolded in this region. The Bataille de le Passe memorial stands as a poignant tribute to the brave fighters who sacrificed their lives in that historic battle.";
                        model.title1 = "";
                        model.image1 = fullPath + "/" + "mahebourg_5" + ".jpg";
                        model.text2 = "Conveniently situated behind the bus station, the iconic Sir Gaetan Duval Esplanade is a prominent feature of the Mahebourg Waterfront. This locale is perfect for enthusiasts of seaside walks, offering an enchanting experience amidst the beauty of nature and breathtaking sea views.";
                        model.title2 = "";
                        model.text3 = "";
                        model.image2 = fullPath + "/" + "mahebourg_waterfront_1" + ".jpg";
                        model.text4 = "Immerse yourself in the rich history of the Grand Port battle, a pivotal event that has left an indelible mark on our heritage. Don't overlook the captivating sight of the 'Mouchoir Rouge' island at the quay's end – a truly mesmerizing view.";
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
                        model.text9 = "Facing the Mahebourg Waterfront is the bustling local market of Mahebourg, a vibrant hub filled with delectable street food, local treats, and fresh vegetables. Take a detour to indulge in some essential shopping. While the Mahebourg Waterfront is a popular weekend destination for locals, attracting picnickers and those seeking casual relaxation, we recommend visiting during the week to avoid the crowds.";
                        model.title8 = "";
                        model.text10 = "";
                        model.image4 = "";
                        model.text11 = "";
                        model.image5 = "";
                        model.text12 = "";
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 1) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "mahebourg_museum_2" + ".jpg";
                        model.text1 = "Tucked away in the scenic southeast of Mauritius, the National History Museum in Mahebourg offers a fascinating glimpse into the island’s past. Admire rare discoveries like an almost whole dodo skeleton, naval artifacts, and in-depth exhibits that shed light on colonial life. Housed in an old-world mansion designated a National Heritage Site, the museum is as charming as it is historic.\n" +
                                "<br><br>" +
                                "Known as Gheude Castle, the mansion was built in the late 18th century and originally belonged to the de Robillard family. Its original owner was Grand Port district commander Jean de Robillard. Over the years, the house passed through the hands of several prominent French settlers before the Mauritian government purchased it in 1947. It was later transformed into a Naval and Historical Museum, and a special Dutch section was opened by a descendant of Maurits Van Nassau, the man after whom Mauritius was named in 1598.\n";
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
                        model.text9 = "One interesting historical detail is tied to a naval battle that took place in August 1810 at Vieux Grand Port, not far from the museum. Both British and French commanders—Sir Nesbit Willoughby and Baron Victor Duperre—were injured in the battle and were treated in the same wing of what is now the museum. This battle remains the only French naval victory against the British, and it is commemorated on the Arc de Triomphe in Paris.\n" +
                                "<br><br>" +
                                "The Mahebourg Museum is just a 10-minute drive from the airport, making it a convenient stop for anyone visiting the southeast of the island. The museum is open from Monday to Saturday, 9:00 AM to 4:00 PM, and on Sundays from 9:00 AM to 12:00 PM. Admission is free, though visitors should note that interior photography is not allowed, ensuring a fully immersive experience as you explore the rich history housed within this delightful museum.\n";
                        model.title8 = "";
                        model.text10 = "";
                        model.image4 = "";
                        model.text11 = "";
                        model.image5 = "";
                        model.text12 = "";
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 2) {

                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();

                        model.main_image = fullPath + "/" + "blue_bay" + ".jpg"; // Set the value for main_image

                        model.text1 = "Located on the southeastern coast of Mauritius, <b>Blue Bay</b> was designated a national park in 1997 and later recognized as a wetland of international importance under the Ramsar Convention in 2008. The marine park is famous for its coral garden, which is home to a wide variety of corals and marine life. Its close proximity to the coastline, combined with calm and shallow waters, makes Blue Bay an ideal spot for snorkeling and exploring its rich biodiversity.";

                        model.title1 = "";

                        model.image1 = fullPath + "/" + "blue_bay_4" + ".jpg"; // Set the value for image1

                        model.text2 = "When you arrive at <b>Blue Bay Beach,</b> you’ll likely encounter local vendors selling handmade jewelry and people offering glass-bottom boat trips or snorkeling tours. More than fifteen operators are licensed to run businesses within the Blue Bay Marine Park.\n" +
                                "<br><br>" +
                                "One of the park’s main attractions is an ancient brain coral that’s over 1,000 years old and has a diameter of 5 meters, making it a must-see for visitors. The coral garden near Mahebourg, a small village in the southeast, spans a large area and features incredible biodiversity. The brain coral is a popular tourist attraction, and glass-bottom boat rides offer a tranquil way to view the undersea environment.\n";
                        model.title2 = "";
                        model.text3 = "";

                        model.image2 = fullPath + "/" + "blue_bay_2" + ".jpg"; // Set the value for image2

                        model.text4 = "Many of the operators here are descendants of fishermen who transitioned into tourism. Glass-bottom boat trips are accessible to everyone and offer a relaxing way to appreciate the beauty of the marine park. For those who want to get closer, snorkeling is a great option, providing an up-close view of the vibrant coral and fish in the clear, warm waters.\n" +
                                "<br><br>" +
                                "Covering 353 hectares, Blue Bay Marine Park is a popular destination for both locals and tourists. It plays an important role in supporting local households through tourism while also maintaining a balance between economic activity and environmental conservation. The park has installed permanent mooring buoys to protect coral from damage by boat anchors and to regulate zones for fishing, boat traffic, swimming, and waterskiing. These efforts help preserve the park's biodiversity, promote research, and educate the public about marine conservation.\n";
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
                        model.text10 = "<ul>" +
                                "\t<li><b>Incredible Marine Biodiversity:</b> Blue Bay Marine Park is home to various ecosystems, including coral reefs, seagrass meadows, mangroves, and macroalgae beds. The park supports over 38 species of coral, 72 species of fish, 31 species of algae, 2 mangrove species, and 4 types of seagrass plants.</li>" +
                                "</ul>";
                        model.image4 = fullPath + "/" + "blue_bay_3" + ".jpg";

                        model.text11 = "<ul>" +
                                "\t<li><b>Unique Status in Mauritius:</b> Blue Bay Marine Park is the only one in Mauritius to be classified under the Wildlife and National Parks Act of 1993. It became a protected zone in 2000 under the Fisheries and Marine Act and was later recognized as a Ramsar site in 2008. Mooring buoys are used to prevent boat anchors from damaging the coral.</li>" +
                                "</ul>";
                        model.image5 = fullPath + "/" + "blue_bay_8" + ".jpg";

                        model.text12 = "<ul>" +
                                "\t<li><b>Activities for Everyone:</b> Glass-bottom boat trips and snorkeling are popular activities that allow non-swimmers to enjoy the marine environment without diving in. Snorkelers can also relax under the casuarina trees on Coco Island, which is visible from the beach. Operators offer convenient drop-off and pick-up services for snorkelers.</li>" +
                                "</ul>";
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 3) {

                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "gris_gris_1" + ".jpg";
                        model.text1 = "The primary beach area features an expansive green field with a commanding view of dramatic cliffs and enormous waves. There are benches and a pavilion available for relaxation while taking in the scenery.\n" +
                                "To the left, a concrete staircase descends to the beach, but swimming is strongly discouraged due to the high danger level. The powerful waves can swiftly overwhelm swimmers. Instead, enjoy a leisurely stroll along the beach, heading toward a small cave at the far end.\n";
                        model.title1 = "Secret Caves at Gris Gris";
                        model.image1 = fullPath + "/" + "grisgris_2" + ".jpg";
                        model.text2 = "In addition to the cave on the far left side of the beach, two other hidden caves can be discovered at Gris Gris. These are more challenging to reach, involving a descent down a cliff and walking through the water.\n" +
                                "Caution is advised against going all the way down, as water levels can fluctuate unpredictably, and the current is often too strong.\n" +
                                "<br><br>" +
                                "For those eager to explore the secret caves at Gris Gris, head towards the cliff's edge directly across from the parking lot. Upon reaching the spot, descend only about halfway to catch a glimpse of the caves on your right.\n" +
                                "<br><br>" +
                                "It's important to bear in mind that entering the caves could pose risks if the water level rises!\n" +
                                "Gris Gris beach is intricately connected to the village of Souillac, which relies heavily on tourism for its revenue. " +
                                "<br><br>" +
                                "Established 200 years ago as a port for ships sailing from Europe to India, Souillac has a rich history worth exploring. Plan your day strategically to make the most of your visit to the southern part of Mauritius, and consider including a visit to Rochester Falls, just outside the village, renowned for its distinctive rectangular-sided rocks.\n" + "The name \"Gris Gris\" adds an intriguing dimension to the experience. Upon entering the beach, a large sign displays the history behind the name. According to local tradition, \"Gris Gris\" is linked to the African amulet known as the “Gris Gris” and its association with the tumultuous coastline. However, the story takes an unexpected turn, suggesting that Gris Gris might have been the name of a puppy belonging to a French cartographer who visited the coast in 1753.<br>";
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
                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 4) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "la_roche_qui_pleure" + ".jpg";
                        model.text1 = "This mystical site derives its name from the visual impression it creates: as water trickles down its walls, the cliffs appear to shed tears. Even more astonishing, some claim to recognize the eroded features of the Mauritian poet Robert Edward Hart." +
                                "<br><br>" +
                                "From a geographical and climatic standpoint, unlike other parts of the island, La Roche qui Pleure lacks coral reefs. Consequently, its shores are more exposed to the assaults of the ocean. This absence of a natural barrier results in more powerful and spectacular waves, a stark contrast to the tranquil lagoons typically associated with the island. The region is influenced by strong winds and seasonal variations, shaping its unique landscape and marine dynamics.<br>";
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

                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 5) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "maconde_1" + ".jpg";
                        model.text1 = "Maconde Viewpoint is perched along the southern coast of Baie du Cap, a quiet village known for its rugged beauty and untouched coastlines. The viewpoint sits atop a small rocky cliff on a curved section of the coastal road. From here, you'll see an incredible landscape with rich red earth, lush green forests, rows of palm trees, and the sparkling blue waters of the Indian Ocean. It's a view that truly captures the heart.\n";
                        model.title1 = "";
                        model.image1 = fullPath + "/" + "maconde_2" + ".jpg";
                        model.text2 = "There's an interesting story behind the name \"Maconde.\" Some say it dates back to the days of slavery when runaway slaves from Mozambique’s Makonde tribe found safety in this area. Others believe it's named after Governor Jean Baptiste Henri Conde, who built an outlook on the cliff.\n" +
                                "<br><br>" +
                                "Getting to this area wasn’t always easy. The first road was only built in the 1920s, and the rough terrain and low-lying coast made construction tough. Recent updates have improved safety, but the drive along the winding basalt cliffs, with the sound of waves crashing against the rocks, is still as mesmerizing as ever. It’s a favorite spot for people who love watching the powerful ocean swells.\n" +
                                "<br><br>" +
                                "To reach the viewpoint itself, you'll need to climb a narrow set of stairs. At the top, you’ll be rewarded with stunning views of the ocean, the coastal village nearby, and the sight of local fishermen along the shore.\n";
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
//                        model.lat = latitudes[position];
//                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    } else if (position == 6) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
                        model.main_image = fullPath + "/" + "la_prairie_2" + ".jpg";
                        model.text1 = "As you drive along Mauritius' scenic southern coast, it’s hard to miss the charm of La Prairie Beach. The beach gets its name from the short grass that stretches all the way to the water, giving it a unique and peaceful vibe. The area is surrounded by unspoiled beauty, offering a perfect blend of grassy patches and sandy shores. La Prairie is a visual feast, with lots of shady spots where you can escape the crowd and a stunning lagoon that’s sure to captivate you. This small gem is situated in the Savanne region in the southwest, close to the village of Baie-du-Cap.\n";
                        model.title1 = "";
                        model.image1 = fullPath + "/" + "la_prairie_1" + ".jpg";
                        model.text2 = "Tucked between the picturesque village of Baie-du-Cap and the iconic Le Morne Brabant mountain, La Prairie offers breathtaking views of the famous peak. On clear days, the reflection of Le Morne in the water adds to the stunning scenery, making it a favorite for nature lovers and photographers alike. It’s usually quiet during the week, providing a peaceful retreat, though it can get busy on weekends and holidays when locals come to enjoy its natural charm.\n" +
                                "<br><br>" +
                                "While swimming isn’t recommended due to the strong currents, the calm atmosphere makes it an ideal spot for a picnic or a relaxing day of sunbathing. Families often come to enjoy the wide-open space, and it’s a great place for children to play safely on the grass while parents take in the view. Whether you're looking to unwind or simply admire Mauritius’ natural beauty, La Prairie is a must-visit.\n";
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
//                        model.lat = latitudes[position];
//                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    }
                } else if (Stash.getString("day").equals("day55")) {
                    if (position == 0) {
                        BeacModel model = new BeacModel();
                        model.title = textView.getText().toString();
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
                        model.text11 = "Weekdays are the best time to visit if you want to avoid the larger crowds that flock to the island on weekends. October and November are considered the ideal months for a trip to Ile aux Cerfs, but the entire period from June to November provides pleasant weather for a visit." +
                                "<br><br>" +
                                "<b><span style=\"color:black; font-weight:bold;\">Accommodation:</span></b><br>" +
                                "For those seeking luxury, the 5-star Shangri-La’s Le Touessrok Resort & Spa offers an opulent stay on Ile aux Cerfs, providing the perfect setting for a grand celebration or special event."
                                + "<br><br>"
                                + "<b><span style=\"color:black; font-weight:bold;\">Dining Options:</span></b>";
                        model.image5 = fullPath + "/" + "ile_aux_cerfs_3" + ".jpg";
                        model.text12 = "The island has several dining options, including a beachside restaurant where you can enjoy a meal with a view. There are also two bars offering refreshing drinks and cocktails at reasonable prices. If you prefer, you can bring your own food and drinks for a picnic on the beach.<br>";

                        model.lat = latitudes[position];
                        model.lng = longitudes[position];
                        Stash.put("model", model);

                        intent = new Intent(context, ItenerariesDetails.class);
                        context.startActivity(intent);
                    }

                }

            }
        });
        return itemView;
    }


}
