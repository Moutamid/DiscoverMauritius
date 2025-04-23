package com.moutamid.sqlapp.activities;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.moutamid.sqlapp.R;

public class TravelTipsActivity extends AppCompatActivity {
    TextView tips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_tips);
        tips=findViewById(R.id.tips);
        tips.setText(Html.fromHtml("<h2>Costs</h2>" +
                "While not an inexpensive travel location, Mauritius does have options for all price ranges. Although you can save money by taking the bus, staying in modest lodging, and purchasing products from local shops and markets, most tourists visiting Mauritius end up spending more. Going out to lunch is a more economical choice because hotel restaurants frequently serve food at Western pricing. You will also need to budget for meals, transportation, and day trips.<br><br>" +
                "A fifteen percent value added tax (VAT) is either added to hotel and restaurant bills or is charged extra. Duty-free purchases require a passport and a return ticket; these must be made at the airport and paid for with a credit card or foreign currency.<br><br>" +
                "<h2>Crime and personal safety</h2>" +
                "With a low crime incidence and few incidents of attacks on tourists, Mauritius is a generally safe travel destination. Due to the traditional and conservative nature of Mauritius culture, ladies should use caution when traveling alone. They should never let strangers into their hotel, cover up in public, stay away from remote beaches and poorly lighted areas at night, and cover up when they are out and about.<br><br>" +
                "Avoid leaving valuables exposed in public including rental cars and be on the lookout for pickpockets in crowded, popular tourist destinations like Grand Baie, since petty crime has regrettably escalated in recent years. There have been instances of theft from hotels, so store valuables in safes or at reception (along with a receipt). Additionally, self-catering units have been broken into; choose a location with reasonable security.<br><br>" +
                "Make sure boats are licensed for tourism. Keep in mind that there is little regulation of tourism providers outside of hotels, and that unlicensed boat tours and watersports operators may use equipment that are not compliant to safety standards. The most frequent cause of holiday mishaps is accidents; avoid motorized water vehicles, which are becoming more and more common by sticking to swimming areas that are roped off. Cyclones are a natural hazard that can affect the island from January to March, therefore always follow instructions.<br><br>" +
                "<h2>Driving</h2>" +
                "You must be at least 21 years old (the minimum age varies depending on the type of car) and have owned a driver's license for at least a year in order to rent a car. Child seats and seatbelts are mandatory.<br><br>" +
                "<h2>Speed Limits</h2>" +
                "Speed limits in Mauritius are:<br><br>" +
                "Town and city: 40 km/h<br><br>" +
                "Open Roads: 80 km/h<br><br>" +
                "Motorways: 110 km/h<br><br>" +
                "<h2>Traffic restrictions</h2>" +
                "Driving on the left side of the road is the norm in Mauritius. Keep a safe distance from other vehicles and drive defensively. Using a hand-held mobile phone while operating a vehicle is prohibited.<br><br>" +
                "<h2>Petrol</h2>" +
                "Mauritius has many gas stations. Few stations are open around-the-clock, so make sure you refill in time.<br><br>" +
                "<h2>Tolls</h2>" +
                "There are currently no road tolls in Mauritius<br><br>" +
                "<h2>Parking</h2>" +
                "Parking is free nearly everywhere in Mauritius.<br><br>" +
                "<h2>Electricity</h2>" +
                "The island uses 50 Hz, 220 volts. Plugs with two round pins in the continental form and three pins in the British style can sometimes be found in the same building.<br><br>" +
                "<h2>Entry requirements</h2>" +
                "Citizens from the majority of Commonwealth and European countries do not need a visa to visit Mauritius for a period of up to ninety days. However, it is essential to possess a passport that remains valid for at least six months, along with proof of a return or onward ticket and accommodation details. In certain instances, the desk clerk may request evidence of sufficient funds for the duration of your stay. Additionally, passengers aged 18 and above are allowed to bring in limited quantities of certain items, including tobacco, spirits, wine or beer, as well as eau de toilette and perfume. It is important to note that severe penalties are imposed for drug trafficking offenses.<br><br>" +
                "Embassies and consulates in Mauritius</h2>" +
                "Australia Mauritius Embassy, 2 Beale Crescent, Canberra, ACT 2600 (+61 2 6281 1203, canberrahc@mail.gov.mu).<br><br>" +
                "South Africa Mauritius High Commission, 1163 Pretorius St, Hatfield 0083, Pretoria (+27 12 342 1283, mhcpta@mweb.co.za).<br><br>" +
                "UK Mauritius High Commission, 32-33 Elvaston Place, London SW7 5NW (+44 20 7581 0294, foreign.govmu.org).<br><br>" +
                "US Mauritius Embassy, 1709 N St NW, Washington, DC 20036 (+1202 244 1491, mauritius.embassy@prodigy.net).<br><br>" +
                "<h2>Gay and lesbian travelers</h2>" +
                "Mauritius is not a gay-friendly destination. The bulk of the population lives in conservative communities where homosexuality is not socially accepted, and sodomy is prohibited for both sexes. In the many hotels and resorts that are owned and operated by foreigners, European attitudes are prevalent. Gay-friendly hotels even welcome same-sex honeymoons, but it is wise to keep your distance elsewhere as public shows of affection could offend. Go online if you want to meet members of the gay community in Mauritius.<br><br>" +
                "<h2>Health</h2><br><br>" +
                "Mauritius does not have malaria, and there are no mandatory vaccinations for visitors. However, mosquito-borne illnesses like dengue fever and Chikungunya are prevalent. It is advisable to use insect repellent containing DEET generously, especially during the summer months (November to April), and to cover up, even during the daytime in wooded areas. Additionally, it is important not to underestimate the sun's intensity: consider packing or purchasing a hat upon arrival, use sunscreen with a high sun protection factor (SPF), and keep a water bottle handy for hydration.<br><br>" +
                "While the island's water is chemically treated and safe to drink, most foreigners choose to boil their water or use bottled water. On Rodrigues, you should absolutely choose bottled water as the water isn't treated there. Hotels typically have acceptable standards for food hygiene, and most visitors gladly snack at small restaurants and street vendors without experiencing any stomach issues.<br><br>" +
                "Mauritius does nothave any venomous reptiles or hazardous animals. However, sea urchins, stonefish, and lionfish, which inhabit Mauritian waters, can pose risks, so wearing protective footwear while swimming is advised. While occasional groups of stray dogs may be intimidating, the risk of rabies is low.<br><br>" +
                "The quality of healthcare on the island is generally adequate for addressing most medical issues. However, visitors are encouraged to consult a doctor (often trainedin France or the UK)at an affordable private clinic. Both public and private hospitals are accessible on the island, with well-equipped pharmacies in most towns. Healthcare options are more limited in Rodrigues.<br><br>" +
                "<h2>Insurance</h2>" +
                "Before traveling, it is important to obtain an insurance policy to protect against theft, loss, illness, or injury. While Mauritius offers adequate everyday medical services, it is advisable to have comprehensive travel and health insurance, which includes emergency evacuation coverage, in case of unforeseen circumstances. Additionally, if you plan to participate in adventure activities, such as horse riding, jet skiing, mountain climbing, diving, or trekking, be sure to review the policy's terms and conditions, as some policies may exclude coverage for such activities deemed \"dangerous.\"<br><br>" +
                "<h2>Internet</h2>" +
                "Mauritius markets itself as a \"cyber island,\" providing high-speed internet access at hotels, resorts, most guesthouses, as well as restaurants and bars in the main tourist center. However, there may be a fee for access, or it may only be available in specific areas. While internet cafes are not common due to the lack of a prominent travelers' scene, they can usually be found in shopping centers. Alternatively, internet access at post offices in tourist areas may offer greater convenience.<br><br>" +
                "For travelers seeking unlimited Wi-Fi while exploring Mauritius, consider purchasing a Skyroam Solis. This device operates in over 130 countries at a flat daily rate, with payment on a pay-as-you-go basis. It allows connectivity for up to five devices simultaneously, with prices starting from as little as €5 per day.<br><br>" +
                "<h2>Laundry</h2>" +
                "Hotels offer laundry services, but the prices are nearly the same as dry cleaning costs back home.  Laundromats are a considerably more affordable choice and may be found in the tourist hotspots of Flic enFlac and Grand Baie.<br><br>" +
                "<h2>Mail</h2>" +
                "The postal service in Mauritius is fast and dependable. It typically takes about a week for mail to reach the UK and Europe from Mauritius, and approximately ten days to reach Australia, South Africa, and the US. Larger hotels and resorts offer mailing services for postcards and letters, and there are around one hundred post offices located across the island, located in most towns, villages, and at the airport.<br><br>" +
                "<h2>Maps</h2>" +
                "The Mauritius Tourism Promotion Authority (MTPA) provides a free map - \"Port Louis & Mauritius,\" which highlights the main hotels and tourist attractions. Additionally, the forestry department offers a map of the Black River Gorges National Park, although it may not be detailed enough for navigating trails. For more comprehensive information, it is recommended to purchase one of the following maps before your trip: \"Mauritius, Réunion, Rodrigues\" by Reise-Know-How Verlag, \"Mauritius\" by InstitutGéographique National (IGN), or \"Mauritius & Réunion\" by International Travel Maps (ITMB). Street maps of central plateau towns can be found in the Mauritius Yellow Pages; however, there are none available for tourist towns like Flic enFlac or even Grand Baie. Some resourceful guesthouses have created their own maps to address this limitation.<br><br>" +
                "<h2>Money</h2>" +
                "The Mauritian rupee (MUR or Rs) is the unit of currency, and it is divided into 100 cents. Larger establishments allow for the option of paying in euros. Hotel rates are published in both rupees and euros. There are currency exchange shops in tourist destinations like Flic enFlac and Grand Baie, but their exchange rate differences are little. Banks and ATMs can be found in most towns and retail centers.<br><br>" +
                "Hotels, restaurants, rental car agencies, and tourist destinations all accept Visa and MasterCard credit cards. Exchange rates are set by the Central Bank of Mauritius.<br><br>" +
                "<h2>Opening hours and public holidays</h2>" +
                "Business hours in the main cities of Mauritius typically run from Monday to Friday, starting at 9am and ending at 5pm. Banks operate from Monday to Thursday, from 9am to 3pm, and on Fridays from 9am to 5pm. Post offices are open from Monday to Friday, from 8am to 4pm, and on Saturdays from 8am to 11.45am. Shops generally open from Monday to Saturday, between 9.30am and 7.30pm, although some, particularly those in the capital, may extend their hours until noon on Sundays. Shops in central plateau towns close for a half-day on Thursdays. Markets typically open early and close around 4pm. Museums open their doors from Monday to Sunday, with one designated closing day per week, typically either Tuesday or Wednesday. Tourist attractions' operating hours vary, although most are open daily. Restaurants typically serve lunch from noon to 3pm and dinner from 7pm to 10pm, with many closing their doors on Sundays.<br><br>" +
                "<h2>Phones</h2>" +
                "The international dialing code for Mauritius is 230, followed by a seven-digit number for landlines. Recently, the prefix 5 has been added to mobile numbers, which may not always be reflected on business cards. If a number is not working, try adding the prefix. All phones in Mauritius have International Direct Dialing (IDD) and calling abroad costs about MUR15 per minute. LUX Resorts provide telephone boxes offering free calls but calling home from other hotels and resorts can be up to ten times more expensive. Mobile phone coverage is generally good in Mauritius though not as reliable in Rodrigues, and GSM phones can be set to roaming. Considering that Mauritian mobile charges are among the lowest in the world, it is advisable to unlock GSM phones and purchase a local SIM card from either Emtel, located at Air Mauritius Building, President John Kennedy St, Port Louis (5498 9898, emtel.com), or Orange, situated on the 9th floor of the Mauritius Telecom Tower, Edith Cavell Street (orange.mu).<br><br>" +
                "<h2>Public holidays</h2>" +
                "There are 14 public holidays in Mauritius. A few of these are based upon lunar or religious calendars and therefore change from year to year.<br><br>" +
                "<h2>January 1 & 2</h2>" +
                "New Year<br><br>" +
                "<h2>January/February</h2>" +
                "ThaipoosamCavadee<br><br>" +
                "<h2>January/February</h2>" +
                "Chinese Spring Festival<br><br>" +
                "<h2>February 1</h2>" +
                "Abolition of Slavery<br><br>" +
                "<h2>February/March</h2>" +
                "Maha Shivaratree<br><br>" +
                "<h2>March 12</h2>" +
                "National Day<br><br>" +
                "<h2>March/April</h2>" +
                "Ougadi<br><br>" +
                "<h2>May 1</h2>" +
                "Labour Day<br><br>" +
                "<h2>July</h2>" +
                "Eid al-Fitr<br><br>" +
                "<h2>August 15</h2>" +
                "Assumption of the Virgin<br><br>" +
                "<h2>August/September</h2>" +
                "Ganesh Chaturthi<br><br>" +
                "<h2>October/November</h2>" +
                "Divali<br><br>" +
                "<h2>November 2</h2>" +
                "Arrival of Indentured Labourers<br><br>" +
                "<h2>December</h2>" +
                "Christmas<br><br>" +
                "<h2>Taxi</h2>" +
                "Taxis are readily available throughout Mauritius, making them easily accessible for travelers as they can be found on every corner of the island. There are two types of taxis in Mauritius: tourist taxis and shared taxis.<br><br>" +
                "Tourist taxis primarily operate within cities and are overseen by the local authorities or hotels. These taxis are painted yellow for easy identification. Shared taxis, on the other hand, are cheaper and run nearly around the clock along popular routes, departing only when the vehicle reaches full capacity.<br><br>" +
                "Not all taxis in Mauritius have meters, and even if some taxis do, it is advisable to agree on a fare before entering. The cost of journeys within Mauritius is usually reasonable and is determined by factors such as the itinerary, distance covered, and duration of the trip.<br><br>" +
                "Here are the standard taxi rates in Mauritius:<br><br>" +
                "Initial Fare: $1.60 (€1.40)<br><br>" +
                "Per Kilometer Fare: $1.70 (€1.50)<br><br>" +
                "Luggage supplement or Supplement for the 5th passenger: $0.20 (€0.18)<br><br>" +
                "If you require transportation to a shop, hotel, restaurant, or attraction, you have several options in Mauritius. You can visit a taxi stand, call for a taxi, or hail one on the street. Taxis are readily available throughout the island, including at shopping centers, bus stations, and outside hotels. To arrange a taxi service in Mauritius, you can call (+230) 5955 03 05. Alternatively, you can book a taxi online through any taxi company offering services within Mauritius.<br><br>" +
                "In Mauritius, you can go to a taxi stand, call a taxi, or flag a cab if you need to get to a store, hotel, restaurant, or attraction. On the island, taxis are ubiquitous and may be seen outside of hotels, at bus stops, and in retail centers. The Mauritius cab service's number is (+230) 5955 03 05. As an alternative, you can reserve a cab online with any Mauritius-based taxi business.<br><br>" +
                "You can reserve your Mauritius cab in advance to secure on-time pickups for a predetermined fee to your location before you travel to Mauritius. <br><br>" +
                "<h2>Mauritius Taxi Tips</h2>" +
                "•\tThe destinations of some taxi drivers are printed on their cars. This is something to watch out for while flagging a cab.<br><br>" +
                "•\tFeel free to negotiate the fare, regardless of whether the taxi has a meter.<br><br>" +
                "•\tIf you need additional information on where to find a shared taxi, you can inquire with the staff at your accommodation.<br><br>" +
                "•\tYou can track your journey using a navigation app on your phone to ensure the taxi driver follows the correct route.<br><br>" +
                "<h2>Wheelchair Accessible Taxis in Mauritius</h2>" +
                "While not all taxis in Mauritius are wheelchair-accessible, several taxi companies do have wheelchair-accessible cars available.You may contact Wheelchair Accessible Holiday Taxis at 203 585 4040. They are a trustworthy taxi company that offers wheelchair accessible taxis.<br><br>" +
                "<h2>Mauritius Taxi Safety and Complaints</h2>" +
                "Transportation in Mauritius is typically safe, although some taxi drivers may attempt to overcharge passengers. First-time visitors are at a higher risk of being scammed, but you can prevent this by negotiating the fare in advance and refusing to pay for services not rendered. Before boarding, it's advisable to obtain the driver's name, license plate number, and identification details. In the event of any issues, you can report them to the local authorities or the taxi company employing the driver.<br><br>" +
                "<h2>Time</h2>" +
                "Mauritius operates on a time zone that is four hours ahead of Greenwich Mean Time (GMT) and does not observe daylight saving time.<br><br>" +
                "<h2>Tourist information</h2>" +
                "The primary source of visitor information on the island is the Mauritius Tourism Promotion Authority (MTPA; 210 1545, tourism-mauritius.mu), which has information offices at Trou d'Eau Douce, Port Louis, and SSR International Airport. They provide free island maps, booklets about island attractions, and flyers that you may pick up from their offices. When visiting Mauritius on their own, independent travelers typically rely on their lodging provider for information and activity arrangements.<br><br>" +
                "<h2>Travelers with disabilities</h2>" +
                "Wheelchair ramps are installed at major tourist sites in Mauritius in an effort to accommodate disabled visitors, and the people of Mauritius are hospitable and eager to assist. While hotels and resorts have made some efforts to accommodate travelers with disabilities, they are not mandated to provide specialized rooms. Typically, this translates to offering rooms on the ground floor, occasionally with steps leading to them. Hotels that are adapted for disabled guests include Shangri-La Le Touessrok Resort & Spa and Le Preskil Beach Resort. However, public transportation poses challenges as buses lack ramps, and city sidewalks are often poorly maintained. Additionally, there is no availability of mobility equipment for rent on the island.<br><br>" +
                "<h2>Tipping</h2>" +
                "In restaurants, it is customary to leave a minimum tip of ten percent, unless it has already been included in your bill; however, smaller local restaurants typically do not expect tips. At hotels, the only expected tip is for porter service; nevertheless, as tips to hotel employees have decreased recently, attempt to tip for excellent service wherever possible. Although taxi drivers do not explicitly ask for tips, those affiliated with hotels are accustomed to receiving them.<br><br>" +
                "<h2>Wedding Bells</h2>" +
                "The ideal location for weddings in the Indian Ocean is without a doubt Mauritius. Not surprisingly, considering the island's natural beauty and the champagne breakfasts, gourmet dinners, couples spa treatments, and seaside horseback rides available, it was chosen as one of the top five places to get married in the world and as the \"Indian Ocean's leading honeymoon destination\" at the 2016 World Travel Awards. The locations vary from private island Île des Deux Cocos in the style of Robinson Crusoe, to hotel beaches and gardens, catamarans, and even underwater in a Blue Safari submarine. To participate in a civil ceremony, couples must arrange for a marriage license ahead of time and be on the island three days in advance. Religious ceremonies require a notice of fifteen days.<br><br>" +
                "<br><br>"));
    }

    public void BackPress(View view) {
        onBackPressed();
    }
}