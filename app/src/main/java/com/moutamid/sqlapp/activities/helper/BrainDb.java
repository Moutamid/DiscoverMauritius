package com.moutamid.sqlapp.activities.helper;

import com.moutamid.sqlapp.model.BrainDataModel;

import java.util.ArrayList;

public class BrainDb {
    public ArrayList<BrainDataModel> eastBrainList() {
        ArrayList<BrainDataModel> dataList = new ArrayList<>();

        /*dataList.add(new BrainDataModel(
                "La Vallée de Ferney\n",
                fullPath + "/" + "" + ".jpg"ferney_1,
                "",
                "",
                "La Vallée de Ferney is a 200-hectare forest and wildlife reserve in the Grand Port District of the Bambou Mountains, north of Mahébourg. It’s managed by the La Vallée de Ferney Conservation Trust, which was established in 2006 in partnership with the Mauritian Wildlife Foundation (MWF) and local authorities. The primary focus is on restoring indigenous forests and protecting the unique biodiversity of the area." +
                        "</br></br>" +
                        getImage("ferney_3")+
                        "</br></br>" +
                        "The valley became a focal point in 2004 when plans for a new highway threatened its local plant and animal life. Surveys revealed new and thought-to-be-extinct species, which led to local efforts to reroute the highway and protect the reserve. With its rich volcanic soil and humid climate, La Vallée de Ferney is one of the last untouched nature refuges on the island.\n" +
                        "</br>" +
                        getImage("ferney_4") +
                        "</br></br>" +
                        "Visitors to La Vallée de Ferney can enjoy hiking trails through native forests, guided tours, and a stone museum that highlights the history of the area. The reserve also features gardens showcasing useful plants, a restaurant, and a visitor’s center. You’ll even find giant tortoises and a nursery for endangered plants. The valley is home to many rare species, including the Mauritius kestrel and the Mauritian flying fox.\n" +
                        getImage("ferney_conservation_park_1") +
                        "</br></br>" +
                        "Since less than 2% of Mauritius' original ecosystems remain, future projects at La Vallée de Ferney aim to expand the propagation of native plants, remove invasive species, and reintroduce endangered birds like the pink pigeon and echo parakeet. The reserve also collaborates with La Vallée de l'Est, another conservation area, to protect an additional 70 hectares of highland forest.\n"
        ));

        dataList.add(new BrainDataModel(
                "Vieux Grand Port Heritage Site",
                fullPath + "/" + "" + ".jpg"frederik_hendrik_museum_1,
                "",
                "",
                "The Vieux Grand Port Heritage Site is located on the southeast coast of Mauritius, about 4 kilometers north of Mahebourg and Pointe d’Esny. This historic spot, set at the foot of Lion Mountain, 7 meters above sea level, offers stunning views of the bay and holds a rich history.\n" +
                        "</br></br>" +
                        getImage("frederik_hendrik_museum_5") +
                        "</br></br>" +
                        "<b>Interesting Facts About Vieux Grand Port Heritage Site:</b></br><ul>" +
                        "\n</br>" +
                        "<li>It was home to the first Dutch East India Company (VOC) fort in the eastern hemisphere.\n</li>" +
                        "</br>" +
                        "<li>It was the first Dutch fort built to defend the island from the sea.\n</li>" +
                        "</br>" +
                        "<li>It saw the first slave uprising in Mauritius, during which the slaves set fire to the wooden fort.\n</li>" +
                        "</br>" +
                        "<li>The rebellion led to a change in architecture, as the Dutch started using stone to build forts for greater safety.\n</li></ul>" +
                        "</br></br>" +
                        "In 1658, Dutch commander Cornelius Gooyer and his crew of 25 men built a small wooden fort shaped like a four-pointed star. Life in Mauritius was difficult, with attacks from slaves, damage from cyclones, and crops destroyed by rodents. After a fire in 1694, the Dutch rebuilt the fort using stone.\n" +
                        "</br></br>" +
                        getImage("frederik_hendrik_museum_4")+
                        "</br></br>" +
                        "The Dutch eventually left Mauritius in 1710, destroying the site before their departure, leaving only a stone lodge behind. The stone jetty in the lagoon beneath the fort is still visible. The French used the stone lodge as their administrative center after conquering Mauritius, and they converted it into a military outpost to protect Vieux Grand Port Bay. In 1806, this outpost was moved to Mahebourg.\n" +
                        "</br></br>" +
                        "Today, remnants of French structures, such as a powder house, prison, bakery, and workshop, sit on top of the old Dutch ruins. In 1998, the site was restored to celebrate the 400th anniversary of the first Dutch landing in Mauritius. The renovation was inaugurated by a descendant of Maurits van Nassau, after whom the island was named.\n" +
                        "</br></br>" +
                        "Adjacent to the fort, the Frederik Hendrik Museum was opened in 1999. It houses artifacts uncovered during archaeological excavations, with a permanent exhibition that includes old maps, military items, pottery, cooking utensils, coins, and more.\n" +
                        "</br></br>" +
                        getImage("frederik_hendrik_museum_2") +
                        "</br></br>" +
                        "<b>Visitor Information</b>" +
                        "</br></br>" +
                        "<b>Address:</b> Royal Road, Old Grand Port, Mauritius (a 15-minute drive from the airport)." +
                        "</br></br>" +
                        "<b>Visiting Hours:</b>" +
                        "</br>" +
                        "Monday, Tuesday, Thursday, Friday, Saturday: 9:00 AM – 4:00 PM\n" +
                        "</br>" +
                        "Wednesday: 11:00 AM – 4:00 PM\n" +
                        "</br>" +
                        "Sunday: 9:00 AM – 12:00 PM\n" +
                        "</br>" +
                        "Closed on public holidays.\n" +
                        "</br>" +
                        "</b>Admission: Free\n</b>" +
                        "</br>" +
                        "<b>Nearby Attractions:</b> Ile aux Aigrettes Nature Reserve and the Mahebourg Museum.\n"
        ));*/

        return dataList;
    }

    private String getImage(String name){
        return "<img src=\"file:///android_res/drawable/" + name + ".jpg\" alt=\"Image description\" style=\"width: 40px; height: 40px;\">";
    }

}
