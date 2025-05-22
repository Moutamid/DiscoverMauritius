package com.moutamid.sqlapp.activities.AboutMauritius;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.moutamid.sqlapp.R;
import com.moutamid.sqlapp.model.BeacModel;

import java.io.File;

public class AboutActivity extends AppCompatActivity {
    String fullPath;
    ImageView etymology,mauritius_culture, mauritius_flag, mauritius_geography, mauritiue_conomy, mauritius_history, mauritius_demography, mauritius_wildlife, mauritius_environment;

    @Override
protected void onCreate(Bundle savedInstanceState) {
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        mauritius_flag = findViewById(R.id.mauritius_flag);
        etymology = findViewById(R.id.etymology);
        mauritius_history = findViewById(R.id.mauritius_history);
        mauritius_geography = findViewById(R.id.mauritius_geography);
        mauritius_wildlife = findViewById(R.id.mauritius_wildlife);
        mauritius_environment = findViewById(R.id.mauritius_environment);
        mauritius_demography = findViewById(R.id.mauritius_demography);
        mauritiue_conomy = findViewById(R.id.mauritiue_conomy);
        mauritius_culture = findViewById(R.id.mauritius_culture);
        File cacheDir = new File(getFilesDir(), "cached_images");
        fullPath = cacheDir.getAbsolutePath();
        Glide.with(this).load(new File(fullPath + "/" + "mauritius_flag" + ".jpg")).into(mauritius_flag);
        Glide.with(this).load(new File(fullPath + "/" + "etymology" + ".jpg")).into(etymology);
        Glide.with(this).load(new File(fullPath + "/" + "mauritius_history" + ".jpg")).into(mauritius_history);
        Glide.with(this).load(new File(fullPath + "/" + "mauritius_geography" + ".jpg")).into(mauritius_geography);
        Glide.with(this).load(new File(fullPath + "/" + "mauritius_wildlife" + ".jpg")).into(mauritius_wildlife);
        Glide.with(this).load(new File(fullPath + "/" + "mauritius_environment" + ".jpg")).into(mauritius_environment);
        Glide.with(this).load(new File(fullPath + "/" + "mauritius_demography" + ".jpg")).into(mauritius_demography);
        Glide.with(this).load(new File(fullPath + "/" + "mauritius_economy" + ".jpg")).into(mauritiue_conomy);
        Glide.with(this).load(new File(fullPath + "/" + "mauritius_culture" + ".jpg")).into(mauritius_culture);
        findViewById(R.id.icon).setOnClickListener(v -> finish());

    }


    public void introduction(View view) {
        BeacModel model = new BeacModel();
        model.title = "Introduction";

        model.main_image = fullPath + "/" + "mauritius_flag" + ".jpg"; // Set the value for main_image

        model.text1 = "Mauritius, officially the Republic of Mauritius, is an island country in the Indian Ocean, about 2,000 kilometres (1,100 nautical miles) off the southeastern coast of East Africa, east of Madagascar. It includes the main island (also called Mauritius), as well as Rodrigues, Agaléga, and St. Brandon (CargadosCarajos shoals). The islands of Mauritius including Agalega, St. Brandon and Rodrigues, along with nearby Réunion (a French overseas department), are part of the Mascarene Islands. The main island of Mauritius, where the population is concentrated, hosts the capital and largest city, Port Louis. The country spans 2,040 square kilometres (790 sq mi) and has an exclusive economic zone covering 2,300,000 square kilometres (670,000 square nautical miles).";
        model.title1 = "";

        model.image1 = ""; // Set the value for image1

        model.text2 = "Based on verbal accounts and the 1502 Cantino planisphere, Arab traders in the Indian Ocean may have been among the first to discover and name the uninhabited island, around 975 AD, as Dina ArobiIn 1507, Portuguese sailors blown off course from the Madagascar channel on their way to India visited the uninhabited island naming it Cirne ('cisne' or 'swan' in modern day Portuguese), Ilha do Cerne on 16th Century Portuguese maps. A Dutch fleet, under the command of Admiral Van Warwyck, landed at what is now the Grand Port District and took possession of the island in 1598, renaming the uninhabited islands after Maurice, Prince of Orange. A succession of short-lived Dutch attempts at permanent settlement took place over a century with the aim of exploiting the local ebony forests, establishing a consistent sugar and arrack production using cane plant cuttings imported from Java together with over three hundred Malagasy slaves, before abandoning their efforts.France took the uninhabited island in 1715, renaming the island \"Isle de France\". In 1810, the United Kingdom seized the island, and four years later, under the Treaty of Paris, France ceded Mauritius and its dependencies to the United Kingdom. The British colony of Mauritius included Rodrigues, Agaléga, St. Brandon, the Chagos Archipelago, and, until 1906, the Seychelles. Mauritius and France dispute sovereignty over the island of Tromelin, as the treaty failed to mention it specifically. Mauritius became the British Empire's main sugar-producing colony and remained a primarily sugar-dominated plantation-based colony until independence, in 1968.";
        model.title2 = "";
        model.text3 = "";

        model.image2 = fullPath + "/" + "mauritius_territory" + ".jpg"; // Set the value for image2

        model.text4 = "In 1965, the UK paid Mauritius and excised the Chagos Archipelago from Mauritian territory to create the British Indian Ocean Territory (BIOT). The local population was forcibly expelled and the largest island, Diego Garcia, was leased to the United States. Ruling on the sovereignty dispute between Mauritius and the UK, the International Tribunal for the Law of the Sea requested the return of the Chagos Islands to Mauritius but had no legal jurisdiction in the matter.\n" + "<br><br>Given its geographic location and colonial past, the people of Mauritius is diverse in ethnicity, culture, language and faith. It is the only country in Africa where Hinduism is the most practised religion. Indo-Mauritians make up the bulk of the population with significant Creole, Sino-Mauritian and Franco-Mauritian minorities. The island's government is closely modelled on the Westminster parliamentary system with Mauritius highly ranked for economic and political freedom being listed by the Economist's Democracy Index as the only country in Africa with full democracy. Mauritius is the only African country with a very high Human Development Index, and the World Bank classifies it as a high-income economy. It is amongst the most competitive and most developed economies in the African region. The government provides free universal healthcare, free education up through the tertiary level and free public transportation for students, senior citizens, and the disabled. Mauritius is consistently ranked as the most peaceful when compared to African countries.\n" + "<br><br>Along with the other Mascarene Islands and, especially the CargadosCarajos shoals, Mauritius is known for its biodiverse flora and fauna and its endemism. The main island is known for the demise of the dodo, which, along with several other avian species, became extinct soon after human settlement. Other endemic animals, such as the echo parakeet, the Mauritius kestrel and the pink pigeon, have survived and are subject to intensive and successful conservation efforts.\n";
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
        Stash.put("model", model);
        Intent intent = new Intent(AboutActivity.this, AboutDetailsActivity.class);
        startActivity(intent);
    }

    public void etymology(View view) {
        BeacModel model = new BeacModel();
        model.title = "Etymology";

        model.main_image = fullPath + "/" + "etymology" + ".jpg"; // Set the value for main_image

        model.text1 = "The first historical evidence of the existence of the island now known as Mauritius is on a 1502 map called the Cantino planisphere which was smuggled out of Portugal, for the Duke of Ferrara, by the Italian 'spy' Alberto Cantino. On this stolen Portuguese map, Mauritius bore the name Dina Arobi. In 1507, Portuguese sailors visited the uninhabited island. The island appears with the Portuguese names Cirne or Do-Cerne on early Portuguese maps, probably from the name of a ship in the 1507 expedition.";
        model.title1 = "";

        model.image1 = ""; // Set the value for image1

        model.text2 = "In 1598, a Dutch squadron under Admiral Wybrand van Warwyck landed at Grand Port and named the island Mauritius, in honour of Prince Maurice van Nassau, stadtholder of the Dutch Republic. Later the island became a French colony and was renamed Isle de France. On 3 December 1810, the French surrendered the island to the United Kingdom during the Napoleonic Wars. Under British rule, the island's name reverted to Mauritius. Mauritius is also commonly known as Maurice (and Île Maurice in French, Moris) in Mauritian Creole.";
        model.title2 = "";
        model.text3 = "";

        model.image2 = fullPath + "/" + "mauritius_territory" + ".jpg"; // Set the value for image2

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
        Stash.put("model", model);
        Intent intent = new Intent(AboutActivity.this, AboutDetailsActivity.class);
        startActivity(intent);
    }

    public void geoprhay(View view) {
        BeacModel model = new BeacModel();
        model.title = "Geography";

        model.main_image = fullPath + "/" + "mauritius_geography" + ".jpg"; // Set the value for main_image

        model.text1 = "The total land area of the country is 2,040 km2 (790 sq mi). It is the 170th largest nation in the world by size. The Republic of Mauritius comprises Mauritius Island and several outlying islands. The nation's exclusive economic zone covers about 2.3 million km2 (890,000 sq mi) of the Indian Ocean, including approximately 400,000 km2 (150,000 sq mi) jointly managed with the Seychelles.";
        model.title1 = "Mauritius Island";

        model.image1 = ""; // Set the value for image1

        model.text2 = "Mauritius is 2,000 km (1,200 mi) off the southeast coast of Africa, between latitudes 19°58.8'S and 20°31.7'S and longitudes 57°18.0'E and 57°46.5'E. It is 65 km (40 mi) long and 45 km (30 mi) wide. Its land area is 1,864.8 km2 (720.0 sq mi). The island is surrounded by more than 150 km (100 mi) of white sandy beaches, and the lagoons are protected from the open sea by the world's third-largest coral reef, which surrounds the island. Just off the Mauritian coast lie some 49 uninhabited islands and islets, several of which have been declared natural reserves for endangered species.\n" + "<br><br>Mauritius Island is relatively young geologically, having been created by volcanic activity some 8 million years ago. Together with Saint Brandon, Réunion, and Rodrigues, the island is part of the Mascarene Islands. These islands emerged as a result of gigantic underwater volcanic eruptions that happened thousands of kilometres to the east of the continental block made up of Africa and Madagascar. They are no longer volcanically active and the hotspot now rests under Réunion Island. Mauritius is encircled by a broken ring of mountain ranges, varying in height from 300 to 800 metres (1,000 to 2,600 ft) above sea level. \n";
        model.title2 = "";
        model.text3 = "";

        model.image2 = ""; // Set the value for image2

        model.text4 = "The land rises from coastal plains to a central plateau where it reaches a height of 670 m (2,200 ft); the highest peak is in the south-west, Piton de la Petite Rivière Noire at 828 metres (2,717 ft). Streams and rivers speckle the island, many formed in the cracks created by lava flows.";
        model.title3 = "Rodrigues Island";
        model.text5 = "The autonomous island of Rodrigues is located 560 km (350 mi) to the east of Mauritius, with an area 108 km2 (42 sq mi). Rodrigues is a volcanic island rising from a ridge along the edge of the Mascarene Plateau. The island is hilly with a central spine culminating in the highest peak, Mountain Limon at 398 m (1,306 ft). The island also has a coral reef and extensive limestone deposits. According to Statistics Mauritius, at 1 July 2019, the population of the island was estimated at 43,371.";
        model.title4 = "Chagos Archipelago";
        model.text6 = "The Chagos Archipelago is composed of atolls and islands, and is located approximately 2,200 kilometres north-east of the main island of Mauritius. Mauritius claims sovereignty over the archipelago, which forms the British Indian Ocean Territory and is de facto controlled by the United Kingdom.\n" + "<br><br>To the north of the Chagos Archipelago are Peros Banhos, the Salomon Islands and Nelsons Island; to the south-west are The Three Brothers, Eagle Islands, Egmont Islands and Danger Island. Diego Garcia is in the south-east of the archipelago. In 2016, the Chagossian population was estimated at 8,700 in Mauritius, including 483 natives; 350 Chagossians live in the Seychelles, including 75 natives, while 3,000, including 127 natives, live in the UK (the population having grown from the 1200 Chagossians who moved there).\n";
        model.title5 = "St. Brandon";
        model.text7 = "";
        model.title6 = "";
        model.text8 = "";
        model.text9 = "";
        model.title7 = "";

        model.image3 = fullPath + "/" + "st_brandon" + ".jpg";

        model.title8 = "";
        model.text10 = "<b>Agaléga Islands</b>\n" + "<br><br>The twin islands of Agaléga are located some 1,000 kilometres (600 miles) to the north of Mauritius. Its North Island is 12.5 km (7.8 mi) long and 1.5 km (0.93 mi) wide, while its South Island is 7 by 4.5 km (4.3 by 2.8 mi). The total area of both islands is 26 km2 (10 sq mi). According to Statistics Mauritius, at 1 July 2019, the population of Agaléga and St. Brandon was estimated at 274.\n" + "<b>Tromelin</b>\n" + "<br><br>Tromelin Island lies 430 km north-west of Mauritius. Mauritius claims sovereignty over Tromelin island, though it is registered as a part of France.\n" + "<br><br>The French took control of Mauritius in 1715, renaming it Isle de France. France officially ceded Mauritius including all its dependencies to Britain through the Treaty of Paris, signed on 30 May 1814 and in which Réunion was returned to France. The British Colony of Mauritius consisted of the main island of Mauritius along with its dependencies Rodrigues, Agaléga, St. Brandon, Tromelin (disputed) and the Chagos Archipelago, while the Seychelles became a separate colony in 1906. It is disputed whether the transfer of Isle de France (as Mauritius was previously known under French rule) and its dependencies to Britain in 1814 included Tromelin island. Article 8 of the Treaty of Paris stipulate the cession by France to Britain of Isle de France \"and its dependencies, namely Rodrigues and the Seychelles\". France considers that the sovereignty of Tromelin island was never transferred to Britain. Mauritius's claim is based on the fact that the transfer of Isle de France and its dependencies to Britain in 1814 was general in nature, that it was beyond those called out in the Treaty of Paris, and that all the dependencies of Isle de France were not specifically mentioned in the Treaty. Mauritius's claim is that since Tromelin was a dependency of Isle de France, it was 'de facto' transferred to Britain in 1814. The islands of Agaléga, St Brandon and the Chagos Archipelago were also not specifically mentioned in the Treaty of Paris but became part of the British Colony of Mauritius as they were dependencies of Isle de France at that time. In addition, the British authorities in Mauritius had been taking administrative measures with respect to Tromelin over the years; for instance, British officials granted four guano operating concessions on Tromelin island between 1901 and 1951. In 1959, British officials in Mauritius informed the World Meteorological Organization that it considered Tromelin to be part of its territory. A co-management treaty was reached by France and Mauritius in 2010 but has not been ratified. \n";

        model.image4 = "";

        model.text11 = "";
        model.image5 = "";

        model.text12 = "";
        Stash.put("model", model);
        Intent intent = new Intent(AboutActivity.this, AboutDetailsActivity.class);
        startActivity(intent);
    }

    public void environment_Climate(View view) {
        BeacModel model = new BeacModel();
        model.title = "Environment & Climate";

        model.main_image = fullPath + "/" + "mauritius_environment" + ".jpg"; // Set the value for main_image

        model.text1 = "The environment in Mauritius is typically tropical in the coastal regions with forests in the mountainous areas. Seasonal cyclones are destructive to its flora and fauna, although they recover quickly. Mauritius ranked second in an air quality index released by the World Health Organization in 2011. It had a 2019 Forest Landscape Integrity Index mean score of 5.46/10, ranking it 100th globally out of 172 countries.\n" + "<br><br>Situated near the Tropic of Capricorn, Mauritius has a tropical climate. There are 2 seasons: a warm humid summer from November to April, with a mean temperature of 24.7 °C (76.5 °F) and a relatively cool dry winter from June to September with a mean temperature of 20.4 °C (68.7 °F). The temperature difference between the seasons is only 4.3 °C (7.7 °F). The warmest months are January and February with average day maximum temperature reaching 29.2 °C (84.6 °F) and the coolest months are July and August with average overnight minimum temperatures of 16.4 °C (61.5 °F). Annual rainfall ranges from 900 mm (35 in) on the coast to 1,500 mm (59 in) on the central plateau. Although there is no marked rainy season, most of the rainfall occurs in the summer months. Sea temperature in the lagoon varies from 22–27 °C (72–81 °F). The central plateau is much cooler than the surrounding coastal areas and can experience as much as twice the rainfall. The prevailing trade winds keep the east side of the island cooler and bring more rain. Occasional tropical cyclones generally occur between January and March and tend to disrupt the weather for about three days, bringing heavy rain.\n" + "\n" + "<br><br>Prime Minister Pravind Jugnauth declared an environmental state of emergency after the 25 July 2020 MV Wakashio oil spill. France sent aircraft and specialists from Réunion and Greenpeace said that the leak threatened the survival of thousands of species, who are at \"risk of drowning in a sea of pollution\".\n";
        model.title1 = "Biodiversity";

        model.image1 = ""; // Set the value for image1

        model.text2 = "The country is home to some of the world's rarest plants and animals, but human habitation and the introduction of non-native species have threatened its indigenous flora and fauna.[125] Due to its volcanic origin, age, isolation, and unique terrain, Mauritius is home to a diversity of flora and fauna not usually found in such a small area. Before the Portuguese arrival in 1507, there were no terrestrial mammals on the island. This allowed the evolution of a number of flightless birds and large reptile species. The arrival of humans saw the introduction of invasive alien species, the rapid destruction of habitat and the loss of much of the endemic flora and fauna. In particular, the extinction of the flightless dodo bird, a species unique to Mauritius, has become a representative example of human-driven extinction. The dodo is prominently featured as a (heraldic) supporter of the national coat of arms of Mauritius.";
        model.title2 = "";
        model.text3 = "";
        model.image2 = fullPath + "/" + "dodo" + ".jpg"; // Set the value for image2

        model.text4 = "Less than 2% of the native forest now remains, concentrated in the Black River Gorges National Park in the south-west, the Bambous Mountain Range in the south-east, and the Moka-Port Louis Ranges in the north-west. There are some isolated mountains, Corps de Garde, Le Morne Brabant, and several offshore islands, with remnants of coastal and mainland diversity. Over 100 species of plants and animals have become extinct and many more are threatened. Conservation activities began in the 1980s with the implementation of programmes for the reproduction of threatened bird and plant species as well as habitat restoration in the national parks and nature reserves.\n" + "<br><br>In 2011, the Ministry of Environment & Sustainable Development issued the \"Mauritius Environment Outlook Report,\" which recommended that St Brandon be declared a Marine protected area. In the President's Report of the Mauritian Wildlife Foundation dated March 2016, St Brandon is declared an official MWF project to promote the conservation of the atoll. \n" + "<br><br>The Mauritian flying fox is the only remaining mammal endemic to the island,and has been severely threatened in recent years due to the government sanctioned culling introduced in November 2015 due to the belief that they were a threat to fruit plantations. Prior to 2015 the lack of severe cyclone had seen the fruit bat population increase and the status of the species was then changed by the IUCN from Endangered to Vulnerable in 2014. October 2018, saw the authorisation of the cull of 20% of the fruit bat population, amounting to 13,000 of the estimated 65,000 fruit bats remaining, although their status had already reverted to Endangered due to the previous years' culls.\n";
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
        Stash.put("model", model);
        Intent intent = new Intent(AboutActivity.this, AboutDetailsActivity.class);
        startActivity(intent);
    }

    public void government_Politics(View view) {
        BeacModel model = new BeacModel();
        model.title = "Government & Politics";

        model.main_image = fullPath + "/" + "coat_of_arms_of_mauritius" + ".png"; // Set the value for main_image

        model.text1 = "The politics of Mauritius take place in a framework of a parliamentary representative democratic republic, in which the President is the head of state and the Prime Minister is the head of government, assisted by a Council of Ministers. Mauritius has a multi-party system. Executive power is exercised by the Government. Legislative power is vested in both the Government and the National Assembly.\n" + "<br><br>The National Assembly is Mauritius's unicameral legislature, which was called the Legislative Assembly until 1992, when the country became a republic. It consists of 70 members, 62 elected for four-year terms in multi-member constituencies and eight additional members, known as \"best losers\", appointed by the Electoral Service Commission to ensure that ethnic and religious minorities are equitably represented. The UN Human Rights Committee (UNHRC), which monitors member states' compliance with the International Covenant on Political and Civil Rights (ICPCR), has criticised the country's Best Loser System following a complaint by a local youth and trade union movement.[143] The president is elected for a five-year term by the Parliament.\n" + "<br><br>The island of Mauritius is divided into 20 constituencies that return three members each. The island of Rodrigues is a single district that returns two members.\n" + "<br><br>After a general election, the Electoral Supervisory Commission may nominate up to eight additional members with a view to correct any imbalance in the representation of ethnic minorities in Parliament. This system of nominating members is commonly called the best loser system.\n" + "<br><br>The political party or party alliance that wins the majority of seats in Parliament forms the government. Its leader becomes the Prime Minister, who selects the Cabinet from elected members of the Assembly, except for the Attorney General of Mauritius, who may not be an elected member of the Assembly. The political party or alliance which has the second largest group of representatives forms the Official Opposition and its leader is normally nominated by the President of the Republic as the Leader of the Opposition. The Assembly elects a Speaker, a Deputy Speaker and a Deputy Chairman of Committees as some of its first tasks.\n" + "<br><br>Mauritius is a democracy with a government elected every five years. The most recent National Assembly Election was held on 7 November 2019 in all the 20 mainland constituencies, and in the constituency covering the island of Rodrigues. Elections have tended to be a contest between two major coalitions of parties.\n" + "<br><br>The 2018 Ibrahim Index of African Governance ranked Mauritius first in good governance.[144] According to the 2017 Democracy Index compiled by the Economist Intelligence Unit that measures the state of democracy in 167 countries, Mauritius ranks 16th worldwide and is the only African-related country with \"full democracy\".\n";
        model.title1 = "Administrative subdivisions";

        model.image1 = ""; // Set the value for image1

        model.text2 = "Mauritius has a single first-order administrative division, the Outer Islands of Mauritius (French: Îleséparses de Maurice), which consists of the islands of Mauritius and several outlying islands.\n" + "<br><br>Mauritius comprises the island of Mauritius and Outer Islands of Mauritius. The following are the island-groups in Mauritius:\n" + "<br><br>•\tIsland of Mauritius\n" + "<br>•\tRodrigues\n" + "<br>•\tSaint Brandon (CargadosCarajos)\n" + "<br>•\tAgaléga\n" + "<br><br>The island of Mauritius is subdivided into nine districts, which are the country's second-order administrative divisions.\n";
        model.title2 = "Military";
        model.text3 = "All military, police, and security functions in Mauritius are carried out by 10,000 active-duty personnel under the Commissioner of Police. The 8,000-member National Police Force is responsible for domestic law enforcement. The 1,400-member Special Mobile Force (SMF) and the 688-member National Coast Guard are the only two paramilitary units in Mauritius. Both units are composed of police officers on lengthy rotations to those services. Mauritius has also a special operations military known as 'GIPM' that would intervene in any terrorist attack or high risk operations.[149]";

        model.image2 = ""; // Set the value for image2

        model.text4 = "";
        model.title3 = "Foreign relations";
        model.text5 = "Mauritius has strong and friendly relations with various African, American, Asian, European and Oceanic countries. Considered part of Africa geographically, Mauritius has friendly relations with African states in the region, particularly South Africa, by far its largest continental trading partner. Mauritian investors are gradually entering African markets, notably Madagascar, Mozambique and Zimbabwe. The country's political heritage and dependence on Western markets have led to close ties with the European Union and its member states, particularly France. Relations with India are very strong for both historical and commercial reasons. Mauritius established diplomatic relations with China in April 1972 and was forced to defend this decision, along with naval contracts with the USSR in the same year. It has also been extending its Middle East outreach with the setting up of an embassy in Saudi Arabia[150] whose Ambassador also doubles as the country's ambassador to Bahrain.\n" + "<br><br>Mauritius is a member of the United Nations, the World Trade Organization, the African Union, the Commonwealth of Nations, La Francophonie, the Southern Africa Development Community, the Indian Ocean Commission, the Common Market for Eastern and Southern Africa, and the Indian Ocean Rim Association.\n";
        model.title4 = "Legal system";
        model.text6 = "Mauritius has a hybrid legal system derived from British common law and the French civil law. The Constitution of Mauritius established the separation of powers between the legislature, the executive and the judiciary and guaranteed the protection of the fundamental rights and freedoms of the individual. Mauritius has a single-structured judicial system consisting of two tiers, the Supreme Court and subordinate courts. The Supreme Court is composed of various divisions exercising jurisdiction such as the Master's Court, the Family Division, the Commercial Division (Bankruptcy), the Criminal Division, the Mediation Division, the Court of First Instance in civil and criminal proceedings, the Appellate jurisdiction: the Court of Civil Appeal and the Court of Criminal Appeal. Subordinate courts consist of the Intermediate Court, the Industrial Court, the District Courts, the Bail and Remand Court and the Court of Rodrigues. The Judicial Committee of the Privy Council is the final court of appeal of Mauritius. After the independence of Mauritius in 1968, Mauritius maintained the Privy Council as its highest court of appeal. Appeals to the Judicial Committee from decisions of the Court of Appeal or the Supreme Court may be as of right or with the leave of the Court, as set out in section 81 of the Constitution and section 70A of the Courts Act. The Judicial Committee may also grant special leave to appeal from the decision of any court in any civil or criminal matter as per section 81(5) of the Constitution.\n" + "<br><br>Mauritius is often described as Africa's most developed country. There are no legal restrictions against homosexuality in Mauritius.\n";
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
        Stash.put("model", model);
        Intent intent = new Intent(AboutActivity.this, AboutDetailsActivity.class);
        startActivity(intent);
    }

    public void demographics(View view) {
        BeacModel model = new BeacModel();
        model.title = "Demographics";

        model.main_image = fullPath + "/" + "mauritius_demography" + ".jpg"; // Set the value for main_image

        model.text1 = "Mauritius had a population of 1,235,260 (608,090 males, 627,170 females) according to the final results of the 2022 Census. The population on the island of Mauritius was 1,191,280 (586,590 males and 604,690 females), and that of Rodrigues Island was 43,650 (21,330 males and 22,320 females); Agalega island total population of 330 (170 males and 160 females). Mauritius has the second highest population density in Africa. According to the 2022 census, the average age of the population was 38 years. 2022 Census indicated that: The proportion of children aged below 15 years went down from 20.7% in 2011 to 15.4% in 2022; The share of persons aged 60 years and over has risen from 12.7% in 2011 to 18.7% in 2022. Subsequent to a Constitutional amendment in 1982, the census does not compile data on ethnic identities anymore but still does on religious affiliation. The 1972 census was the last one to measure ethnicity. Mauritius is a multiethnic society, drawn from Indian, African, Chinese and European (mostly French) origin.\n" + "<br><br>According to the Constitution of Mauritius, there are 4 distinct communities on the island for the purposes of representation in the National Assembly. Schedule I, Paragraph 3(4) of the Constitution states that The population of Mauritius shall be regarded as including a Hindu community, a Muslim community, and a Sino-Mauritian community, and every person who does not appear, from his way of life, to belong to one or other of those three communities shall be regarded as belonging to the General Population, which shall itself be regarded as a fourth community. Thus each ethnic group in Mauritius falls within one of the four main communities known as Hindus, General Population, Muslims and Sino-Mauritians.\n" + "<br><br>As per the above constitutional provision, the 1972 ethnic statistics are used to implement the Best Loser System, the method used in Mauritius since the 1950s to guarantee ethnic representation across the entire electorate in the National Assembly without organising the representation wholly by ethnicity.\n";
        model.title1 = "Religion";

        model.image1 = ""; // Set the value for image1

        model.text2 = "According to the 2011 census conducted by Statistics Mauritius, 48.5% of the Mauritian population follows Hinduism, followed by Christianity (32.7%), out of which 26.3% are Catholic, Islam (17.2%) and other religions (0.7%). 0.7% reported themselves as non-religious and 0.1% did not answer.[5] The constitution prohibits discrimination on religious grounds and provides for freedom to practice, change one's religion or not have any. The Roman Catholic Church, Church of England, Presbyterian Church of Mauritius, Seventh-day Adventists, Hindu Temples Associations and Muslim Mosques Organisations enjoy tax-exemptions and are allocated financial support based on their respective share of the population. Other religious groups can register and be tax-exempt but receive no financial support.[163] Public holidays of religious origins are the Hindu festivals of Maha Shivaratri, Ougadi, ThaipoosamCavadee, Ganesh Chaturthi, and Diwali; the Christian festivals of All Saints Day and Christmas; and the Muslim festival of Eid al-Fitr.[164] The state actively participates in their organisation with special committees presiding over the pilgrimage to Ganga Talao for Maha Shivaratri and the annual Catholic Procession to Jacques-Désiré Laval's resting place at Sainte-Croix.";
        model.title2 = "Languages";
        model.text3 = "The Mauritian constitution makes no mention of an official language. The Constitution only mentions that the official language of the National Assembly is English; however, any member can also address the chair in French. English and French are generally considered to be de facto national and common languages of Mauritius, as they are the languages of government administration, courts, and business. The constitution of Mauritius is written in English, while some laws, such as the Civil and Criminal codes, are in French. The Mauritian currency features the Latin, Tamil and Devanagari scripts.\n" + "<br><br>The Mauritian population is multilingual; while Mauritian Creole is the mother tongue of most Mauritians, most people are also fluent in English and French; they tend to switch languages according to the situation. French and English are favoured in educational and professional settings, while Asian languages are used mainly in music, religious and cultural activities. The media and literature are primarily in French.\n" + "<br><br>The Mauritian Creole language, which is French based with some additional influences, is spoken by the majority of the population as a native language. The Creole languages which are spoken in different islands of the country are more or less similar: Mauritian Creole, Rodriguan creole, Agalega creole and Chagossian creole are spoken by people from the islands of Mauritius, Rodrigues, Agaléga and Chagos. The following ancestral languages, also spoken in Mauritius, have received official recognition by acts of parliament: Bhojpuri, Chinese, Hindi, Marathi, Sanskrit, Tamil, Telugu and Urdu. Bhojpuri, once widely spoken as a mother tongue, has become less commonly spoken over the years. According to the 2022 census, Bhojpuri was spoken by 5.1% of the population compared to 12.1% in 2000.\n" + "<br><br>School students must learn English and French; they may also opt for an Asian language or Mauritian Creole. The medium of instruction varies from school to school but is usually English for public and government subsidised private schools and mainly French for paid private ones. O-Level and A-Level Exams are organised in public and government subsidised private schools in English by Cambridge International Examinations while paid private schools mostly follow the French Baccalaureate model.\n";

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
        Stash.put("model", model);
        Intent intent = new Intent(AboutActivity.this, AboutDetailsActivity.class);
        startActivity(intent);
    }

    public void economy(View view) {
        BeacModel model = new BeacModel();
        model.title = "Economy";

        model.main_image = fullPath + "/" + "mauritius_economy" + ".jpg"; // Set the value for main_image

        model.text1 = "Since independence from Britain in 1968, Mauritius has developed from a low-income, agriculture-based economy to a high-income diversified economy, based on tourism, textiles, sugar, and financial services. The economic history of Mauritius since independence has been called \"the Mauritian Miracle\" and the \"success of Africa\".\n" + "<br><br>In recent years, information and communication technology, seafood, hospitality and property development, healthcare, renewable energy, and education and training have emerged as important sectors, attracting substantial investment from both local and foreign investors.\n" + "<br><br>Mauritius has no exploitable fossil fuel reserves and so relies on petroleum products to meet most of its energy requirements. Local and renewable energy sources are biomass, hydro, solar and wind energy.\n" + "<br><br>Mauritius has one of the largest exclusive economic zones in the world, and in 2012 the government announced its intention to develop the marine economy.\n" + "<br><br>Mauritius is ranked high in terms of economic competitiveness, a friendly investment climate, good governance and a free economy. The Gross Domestic Product (PPP) was estimated at US$29.187 billion in 2018, and GDP (PPP) per capita was over US$22,909, the second highest in Africa.\n" + "<br><br>Mauritius has a high-income economy, according to the World Bank in 2019. The World Bank's 2019 Ease of Doing Business Index ranks Mauritius 13th worldwide out of 190 economies in terms of ease of doing business. According to the Mauritian Ministry of Foreign Affairs, the country's challenges are heavy reliance on a few industry sectors, high brain drain, scarcity of skilled labour, ageing population and inefficient public companies and para-statal bodies.\n" + "<br><br>Mauritius has built its success on a free market economy. According to the 2019 Economic Freedom of the World report, Mauritius is ranked as having the 9th most free economy in the world.\n";
        model.title1 = "Financial services";

        model.image1 = ""; // Set the value for image1

        model.text2 = "According to the Financial Services Commission, financial and insurance activities contributed to 11.1% of the country's GDP in 2018. Over the years, Mauritius has been positioning itself as the preferred hub for investment into Africa due its strategic location between Asia and Africa, hybrid regulatory framework, ease of doing business, investment protection treaties, non-double taxation treaties, highly qualified and multilingual workforce, political stability, low crime rate coupled with modern infrastructure and connectivity. It is home to a number of international banks, legal firms, corporate services, investment funds and private equity funds. Financial products and services include private banking, global business, insurance and reinsurance, limited companies, protected cell companies, trust and foundation, investment banking, global headquarter administration.";
        model.title2 = "";
        model.text3 = "Corporate tax rate ranges from 15% to 17% and individual tax rate ranges from 10% to 25%.While the country also offer incentives such as tax holidays and exemptions in some specific sectors to boost its competitiveness, the country is often tagged as a tax haven by the press due to individuals and companies who engaged in abusive practices in its financial sector. The country has built up a solid reputation by making use of best practices and adopting a strong legal and regulatory framework to demonstrate its compliance with international demands for greater transparency. In June 2015, Mauritius adhered to the multilateral Convention on Mutual Administrative Assistance in Tax Matters, and has an exchange information mechanism with 127 jurisdictions. Mauritius is a founding member of the Eastern and Southern Africa Anti Money Laundering Group and has been at the forefront in the fight against money laundering and other forms of financial crime. The country has adopted exchange of information on an automatic basis under the Common Reporting Standard and the Foreign Accounts Tax Compliance Act.";

        model.image2 = ""; // Set the value for image2

        model.text4 = "";
        model.title3 = "Tourism";
        model.text5 = "Mauritius is a major tourist destination, and the tourism sector is one of the main pillars of the Mauritian economy. The island nation enjoys a tropical climate with clear warm sea waters, beaches, tropical fauna and flora, complemented by a multi-ethnic and cultural population. The forecast of tourist arrivals for the year 2019 is maintained at 1,450,000, representing an increase of 3.6% over the figure of 1,399,408 in 2018.\n" + "<br><br>Mauritius currently has two UNESCO World Heritage Sites, namely, Aapravasi Ghat and Le Morne Cultural Landscape. Additionally, Black River Gorges National Park is currently in the UNESCO tentative list.\n";
        model.title4 = "Transport";
        model.text6 = "Since 2005 public buses, and later trains, in Mauritius have been free of charge for students, people with disabilities, and senior citizens. The Metro Express railway currently links all five cities and the University of Mauritius at Réduit with planned expansion to the east and south. ";
        model.title5 = "";
        model.text7 = "Former privately owned industrial railways have been abandoned since the 1960s. The harbour of Port Louis handles international trade as well as a cruise terminal. The Sir Seewoosagur Ramgoolam International Airport, the largest one in the Indian Ocean, is the main international airport and serves as the home operating base for the national airline Air Mauritius. The Plaine Corail Airport operates from Rodrigues ensuring air link with the main island of Mauritius and international flights with Réunion.";
        model.title6 = "Information and communications technology (ICT)";
        model.text8 = "The information and communications technology (ICT) sector has contributed to 5.7% of its GDP in 2016.\n" + "<br><br>Additionally, the African Network Information Centre (AFRINIC) – the regional Internet registry for Africa – is headquartered in Ebene.\n" + "<br><br>Mauritius is also connected to global Internet infrastructure via multiple optical fibre submarine communications cables, including the Lower Indian Ocean Network (LION) cable, the Mauritius–Rodrigues Submarine Cable, and the South Africa Far East (SAFE) cable.\n";
        model.title7 = "";

        model.image3 = "";

        model.text9 = "";
        model.title8 = "";
        model.text10 = "";

        model.image4 = "";

        model.text11 = "";

        model.image5 = "";

        model.text12 = "";
        Stash.put("model", model);
        Intent intent = new Intent(AboutActivity.this, AboutDetailsActivity.class);
        startActivity(intent);
    }

    public void culture(View view) {
        BeacModel model = new BeacModel();
        model.title = "Culture";

        model.main_image = fullPath + "/" + "mauritius_culture" + ".jpg"; // Set the value for main_image

        model.text1 = "";
        model.title1 = "Art";

        model.image1 = ""; // Set the value for image1

        model.text2 = "Prominent Mauritian painters include Henri Le Sidaner, Malcolm de Chazal, Raouf Oderuth and VacoBaissac while Gabrielle Wiehe is a prominent illustrator and graphic designer.\n" + "<br><br>The Mauritius \"Post Office\" stamps, the first stamps produced outside Great Britain, among the rarest postage stamps in the world, are widely considered \"the greatest item in all philately\".\n";
        model.title2 = "Architecture";
        model.text3 = "The distinctive architecture of Mauritius reflects the island nation's history as a colonial trade base connecting Europe with the East. Styles and forms introduced by Dutch, French, and British settlers from the seventeenth century onward, mixed with influences from India and East Africa, resulted in a unique hybrid architecture of international historic, social, and artistic significance. Mauritian structures present a variety of designs, materials, and decorative elements that are unique to the country and inform the historical context of the Indian Ocean and European colonialism.\n" + "Decades of political, social, and economic change have resulted in the routine destruction of Mauritian architectural heritage. Between 1960 and 1980, the historic homes of the island's high grounds, known locally as campagnes, disappeared at alarming rates. More recent years have witnessed the demolition of plantations, residences, and civic buildings as they have been cleared or drastically renovated for new developments to serve an expanding tourism industry. The capital city of Port Louis remained relatively unchanged until the mid-1990s, yet now reflects the irreversible damage that has been inflicted on its built heritage. Rising land values are pitted against the cultural value of historical structures in Mauritius, while the prohibitive costs of maintenance and the steady decline in traditional building skills make it harder to invest in preservation.\n" + "<br><br>The general populace historically lived in what are termed creole houses.\n";

        model.image2 = ""; // Set the value for image2

        model.text4 = "";
        model.title3 = "Literature";
        model.text5 = "Mauritius is remembered in literature mostly for the novel Paul et Virginie, a classic of French literature, by Bernardin de Saint-Pierre and for Alice's Adventures in Wonderland's Dodo\n" + "<br><br>Jean-Marie Le Clézio, Ananda Devi, Nathacha Appanah, Malcolm de Chazal, Eugénie Poujade, Marie-Thérèse Humbert, Shenaz Patel, Khal Torabully, Aqiil Gopee, Dev Virahsawmy, South-African born Lindsey Collen-Seegobin writing in English and French and Abhimanyu Unnuth writing in Hindi are some of the most prominent Mauritian writers. Le Clézio, who won the Nobel Prize for Literature in 2008, is of Mauritian heritage and holds dual French-Mauritian citizenship. " + "The island plays host to the Le Prince Maurice Prize. In keeping with the island's literary culture the prize alternates on a yearly basis between English-speaking and French-speaking writers.";
        model.title4 = "Music";
        model.text6 = "The major musical genres of Mauritius are Sega and its fusion genre, Seggae, Bhojpuri folk songs, Indian movie music especially Bollywood, and Classical music mainly Western classical music and Indian classical music.";
        model.title5 = "Cuisine";
        model.text7 = "Mauritian cuisine is a combination of Indian, Creole, French and Chinese, with many dishes unique to the island. Spices are also a major component of Mauritian cuisine. There is a local variant of Persian falooda, locally known as alouda, which is a cold beverage made with milk, basil seeds, and agar-agar jelly. Locally made French pastry and bread are sold in most localities. Popular hawker meals include a wrap called dhollpuri, rice-based biryani and gâteau piment.\n" + "<br><br>";
        model.title6 = "Holidays and festivals";
        model.text8 = "<br><br>The public holidays of Mauritius involve the blending of several cultures from Mauritius's history. There are Hindu festivals, Christian festivals, Chinese festivals, and Muslim festivals. There are 14 annual public holidays in Mauritius with New Year celebrated over two days if it falls on a weekend. All the public holidays related to religious festivals have dates that vary from year to year except for Christmas. Other festivals such as Holi, Raksha Bandhan, Durga Puja, Père Laval Pilgrimage also enrich the cultural landscape of Mauritius.\\n";
        model.title7 = "Sports";

        model.image3 = fullPath + "/" + "champ_de_aars_racecourse" + ".jpg";

        model.text9 = "The most popular sport in Mauritius is football and the national team is known as The Dodos or Club M. Other popular sports in Mauritius include cycling, table tennis, horse racing, badminton, volleyball, basketball, handball, boxing, judo, karate, taekwondo, weightlifting, bodybuilding and athletics. Water sports include swimming, sailing, scuba diving, windsurfing and kitesurfing." + "<br><br>Horseracing, which dates from 1812 when the Champ de Mars Racecourse was inaugurated, remains popular." + "<br><br>The country hosted the second (1985), fifth (2003) and tenth editions (2019) of the Indian Ocean Island Games. Mauritius won its first Olympic medal at the 2008 Summer Olympics in Beijing when boxer Bruno Julie won the bronze medal." + "<br><br>In golf, the former Mauritius Open and the current AfrAsia Bank Mauritius Open have been part of the European Tour." + "<br><br>The raids continued until 1810, when a Royal Navy expedition led by Commodore Josias Rowley, R.N., an Anglo-Irish aristocrat, was sent to capture the island. Despite winning the Battle of Grand Port against the British, the French could not prevent the British from landing at Cap Malheureux three months later. They formally surrendered the island on the fifth day of the invasion, 3 December 1810, on terms allowing settlers to keep their land and property and to use the French language and law of France in criminal and civil matters. Under British rule, the island's name reverted to Mauritius.";
        model.title8 = "British Mauritius (1810–1968)";
        model.text10 = "";

        model.image4 = "";

        model.text11 = "The British administration, which began with Sir Robert Farquhar as its first governor, oversaw rapid social and economic changes. However, it was tainted by the Ratsitatane episode. Ratsitatane, nephew of King Radama of Madagascar, was brought to Mauritius as a political prisoner. He managed to escape from prison and plotted a rebellion that would free the island's slaves. He was betrayed by his associate Laizaf and was caught by a group of militiamen and summarily executed.\n" + "<br><br>In 1832, d'Épinay launched the first Mauritian newspaper (Le Cernéen), which was not controlled by the government. In the same year, there was a move by the procureur-general to abolish slavery without compensation to the slave owners. This gave rise to discontent, and, to check an eventual rebellion, the government ordered all the inhabitants to surrender their arms. Furthermore, a stone fortress, Fort Adelaide, was built on a hill (now known as the Citadel hill) in the centre of Port Louis to quell any uprising.[43] Slavery was gradually abolished over several years after 1833, and the planters ultimately received two million pounds sterling in compensation for the loss of their slaves, who had been imported from Africa and Madagascar during the French occupation. The abolition of slavery had important effects on Mauritius's society, economy and population. The planters brought a large number of indentured labourers from India to work in the sugar cane fields. \n";

        model.image5 = "";

        model.text12 = "";
        Stash.put("model", model);
        Intent intent = new Intent(AboutActivity.this, AboutDetailsActivity.class);
        startActivity(intent);
    }

    public void history(View view) {
        Intent intent = new Intent(AboutActivity.this, HistoryAboutDetailsActivity.class);
        startActivity(intent);
    }

    public void wild_life(View view) {
        Intent intent = new Intent(AboutActivity.this, WildAboutDetailsActivity.class);
        startActivity(intent);
    }
}