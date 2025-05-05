package com.moutamid.sqlapp.activities.adapter;

import static com.moutamid.sqlapp.model.DatabaseHelper.TABLE_NAME;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fxn.stash.Stash;
import com.moutamid.sqlapp.R;
import com.moutamid.sqlapp.activities.Beaches.BeachDetails;
import com.moutamid.sqlapp.activities.Iteneraries.ItinerariesActivity;
import com.moutamid.sqlapp.activities.MyTripsActivity;
import com.moutamid.sqlapp.helper.Constants;
import com.moutamid.sqlapp.model.BeacModel;
import com.moutamid.sqlapp.model.DatabaseHelper;
import com.moutamid.sqlapp.offlinemap.DistanceCalculator;
import com.moutamid.sqlapp.offlinemap.DurationCalculator;
import com.moutamid.sqlapp.offlinemap.MapActivity;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<BeacModel> beacModels;
    private Context context;
    private OnStartDragListener dragListener;
    private double totalDistance = 0.0;
    private double totalDuration = 0.0;
    public MyAdapter(List<BeacModel> beacModels, Context context, OnStartDragListener dragListener) {
        this.beacModels = beacModels;
        this.context = context;
        this.dragListener = dragListener;
        calculateTotalDistanceAndDuration();
    }
    private void calculateTotalDistanceAndDuration() {
        totalDistance = 0.0;
        totalDuration = 0.0;
        for (int i = 1; i < beacModels.size(); i++)
        {
            BeacModel previousModel = beacModels.get(i - 1);
            BeacModel currentModel = beacModels.get(i);
            double distance = DistanceCalculator.calculateDistance(
            previousModel.lat, previousModel.lng, currentModel.lat, currentModel.lng);
            double duration = DurationCalculator.calculateDrivingDuration(distance);
            totalDistance += distance;
            totalDuration += duration;
        }
        MyTripsActivity.distance.setText(String.format("%.1f km", totalDistance));
        MyTripsActivity.time.setText(DurationCalculator.formatDuration(totalDuration));
        Log.d("MyAdapter", "Total Distance: " + String.format("%.1f km", totalDistance));
        Log.d("MyAdapter", "Total Duration: " + DurationCalculator.formatDuration(totalDuration));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BeacModel beacModel = beacModels.get(position);
        holder.textView.setText(beacModel.title);
        if (position == 0) {
            holder.textView3.setText("Start\nHere");
        } else {
            BeacModel previousModel = beacModels.get(position - 1);
            double distance = DistanceCalculator.calculateDistance(
            previousModel.lat, previousModel.lng, beacModel.lat, beacModel.lng);
            double duration = DurationCalculator.calculateDrivingDuration(distance);
            String formattedDuration = DurationCalculator.formatDuration(duration);
            holder.textView3.setText(formattedDuration + "\n" + String.format("%.1f km", distance));
        }
        holder.number.setText(String.valueOf(position + 1));
        MyTripsActivity.total_stop.setText(beacModels.size()+ " stops");
//        holder.imageView.setImageResource(beacModel.main_image);
        DatabaseHelper databaseHelper;
        databaseHelper = new DatabaseHelper(context);
        boolean isDataAvailable = false;
        for (BeacModel model : beacModels) {
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

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String deleteName = holder.textView.getText().toString();
                SQLiteDatabase db = databaseHelper.getWritableDatabase();
                db.delete(TABLE_NAME, DatabaseHelper.COLUMN_TITLE + "=?", new String[]{deleteName});
                db.close();
                beacModels.remove(holder.getAdapterPosition());
                holder.remove.setVisibility(View.GONE);
                holder.add.setVisibility(View.VISIBLE);
                calculateTotalDistanceAndDuration();
                notifyDataSetChanged();
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Stash.put("model", beacModel);
                Intent intent = new Intent(context, BeachDetails.class);
                intent.putExtra("is_trip", 0);
                context.startActivity(intent);
            }
        });
        holder.itemView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                if (dragEvent.getAction() == DragEvent.ACTION_DRAG_STARTED) {
                    // Notify the activity to start dragging
                    dragListener.onStartDrag(holder);
                    calculateTotalDistanceAndDuration();
                    return true;
                }
                return false;
            }
        });


        holder.map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            if (Stash.getBoolean(Constants.IS_PREMIUM, false)) {

                Stash.put("map_lat", beacModel.lat);
                Stash.put("map_lng", beacModel.lng);
                Stash.put("map_name", beacModel.title);
                Stash.put("map_img", beacModel.main_image);
                Intent intent = new Intent(context, MapActivity.class);
                intent.putExtra("map_lat", beacModel.lat);
                intent.putExtra("map_lng", beacModel.lng);
                context.startActivity(intent);
            } else {
                ItinerariesActivity.premium_layout.setVisibility(View.VISIBLE);
            }
            }
        });

        if (holder.textView.getText().toString().equals("Belle Mare Beach")) {
            holder.textView1.setText("Belle Mare");
            holder.textView2.setText("District of Flacq");
        } else if (holder.textView.getText().toString().equals("Ile aux Cerfs Beach")) {
            holder.textView1.setText("Ile aux Cerfs");
            holder.textView2.setText("District of Flacq");
        } else if (holder.textView.getText().toString().equals("Poste Lafayette Beach")) {
            holder.textView1.setText("Poste Lafayette");
            holder.textView2.setText("District of Flacq");
        } else if (holder.textView.getText().toString().equals("Roche Noire Beach")) {
            holder.textView1.setText("Roche Noire");
            holder.textView2.setText("District of Riviere du Rempart");
        } else if (holder.textView.getText().toString().equals("Cap Malheureux Beach")) {
            holder.textView1.setText("Cap Malheureux");
            holder.textView2.setText("District of Riviere du Rempart");
        } else if (holder.textView.getText().toString().equals("Grand Gaube Beach")) {
            holder.textView1.setText("Grand Gaube");
            holder.textView2.setText("District of Riviere du Rempart");
        } else if (holder.textView.getText().toString().equals("Mont Choisy Beach")) {
            holder.textView1.setText("Mont Choisy");
            holder.textView2.setText("District of Pamplemousses");
        } else if (holder.textView.getText().toString().equals("Pereybere Beach")) {
            holder.textView1.setText("Grand Baie");
            holder.textView2.setText("District of Riviere du Rempart");
        } else if (holder.textView.getText().toString().equals("Trou aux Biches Beach")) {
            holder.textView1.setText("Trou aux Biches");
            holder.textView2.setText("District of Pamplemousses");
        } else if (holder.textView.getText().toString().equals("Bel Ombre Beach")) {
            holder.textView1.setText("St Martin");
            holder.textView2.setText("District of Savanne");
        } else if (holder.textView.getText().toString().equals("Blue Bay Beach")) {
            holder.textView1.setText("Blue Bay");
            holder.textView2.setText("District of Grand Port");
        } else if (holder.textView.getText().toString().equals("Gris Gris Beach")) {
            holder.textView1.setText("Souillac");
            holder.textView2.setText("District of Savanne");
        } else if (holder.textView.getText().toString().equals("La Cambuse Beach")) {
            holder.textView1.setText("La Cambuse");
            holder.textView2.setText("District of Plaine Magnien");
        } else if (holder.textView.getText().toString().equals("Riviere des Galets Beach")) {
            holder.textView1.setText("Riviere des Galets");
            holder.textView2.setText("District of Savanne");
        } else if (holder.textView.getText().toString().equals("St Felix Beach")) {
            holder.textView1.setText("St Felix");
            holder.textView2.setText("District of Savanne");
        } else if (holder.textView.getText().toString().equals("Flic en Flac Beach")) {
            holder.textView1.setText("Flic en Flac");
            holder.textView2.setText("District of Black River");
        } else if (holder.textView.getText().toString().equals("La Preneuse Beach")) {
            holder.textView1.setText("La Preneuse");
            holder.textView2.setText("District of Black River");
        } else if (holder.textView.getText().toString().equals("Le Morne Beach")) {
            holder.textView1.setText("Le Morne Brabant");
            holder.textView2.setText("District of Black River");
        } else if (holder.textView.getText().toString().equals("Tamarin Bay Beach")) {
            holder.textView1.setText("Tamarin");
            holder.textView2.setText("District of Black River");
        } else if (holder.textView.getText().toString().equals("Bagatelle Mall")) {
            holder.textView1.setText("Bagatelle");
            holder.textView2.setText("Moka");
        } else if (holder.textView.getText().toString().equals("Bois Cheri Tea Museum")) {
            holder.textView1.setText("Bois Cheri Road");
            holder.textView2.setText("Bois Cheri");
        } else if (holder.textView.getText().toString().equals("Eureka House")) {
            holder.textView1.setText("Montagne Ory");
            holder.textView2.setText("Moka");
        } else if (holder.textView.getText().toString().equals("Grand Bassin")) {
            holder.textView1.setText("Grand Bassin Lake");
            holder.textView2.setText("District of Savanne");
        } else if (holder.textView.getText().toString().equals("Gymkhana Golf Course")) {
            holder.textView1.setText("Suffolk");
            holder.textView2.setText("Vacoas-Phoenix");
        } else if (holder.textView.getText().toString().equals("Le Pouce Mountain")) {
            holder.textView1.setText("Petit Verger");
            holder.textView2.setText("Saint Pierre");
        } else if (holder.textView.getText().toString().equals("Pieter Both Mountain")) {
            holder.textView1.setText("Pieter Both");
            holder.textView2.setText("District of Port Louis");
        } else if (holder.textView.getText().toString().equals("Tamarin Falls")) {
            holder.textView1.setText("Henrietta Branch Rd");
            holder.textView2.setText("District of Black River");
        } else if (holder.textView.getText().toString().equals("Trou aux Cerfs")) {
            holder.textView1.setText("Curepipe");
            holder.textView2.setText("District of Plaine Wilhems");
        } else if (holder.textView.getText().toString().equals("La Vallee de Ferney")) {
            holder.textView1.setText("La Vallee de Ferney");
            holder.textView2.setText("District of Vieux Grand Port");
        } else if (holder.textView.getText().toString().equals("Vieux Grand Port Heritage Site")) {
            holder.textView1.setText("Royal Road");
            holder.textView2.setText("Old Grand Port");
        } else if (holder.textView.getText().toString().equals("Notre-Dame Chapel")) {
            holder.textView1.setText("Cap Malheureux");
            holder.textView2.setText("District of Riviere du Rempart");
        } else if (holder.textView.getText().toString().equals("SSR Botanical Garden")) {
            holder.textView1.setText("Pamplemousses");
            holder.textView2.setText("District of Pamplemousses");
        } else if (holder.textView.getText().toString().equals("L’Aventure du Sucre Museum")) {
            holder.textView1.setText("21001 Beau Plan");
            holder.textView2.setText("Pamplemousses");
        } else if (holder.textView.getText().toString().equals("Baie de L’Arsenal Ruins")) {
            holder.textView1.setText("Baie aux Tortues");
            holder.textView2.setText("Terre Rouge");
        } else if (holder.textView.getText().toString().equals("Mauritius Aquarium")) {
            holder.textView1.setText("Coastal Road");
            holder.textView2.setText("Pointe aux Piments");
        } else if (holder.textView.getText().toString().equals("Grand Bay")) {
            holder.textView1.setText("Grand Bay");
            holder.textView2.setText("District of Riviere du Rempart");
        } else if (holder.textView.getText().toString().equals("Le Caudan Waterfront")) {
            holder.textView1.setText("Caudan Waterfront");
            holder.textView2.setText("Port Louis");
        } else if (holder.textView.getText().toString().equals("Port Louis Central Market")) {
            holder.textView1.setText("Port Louis Market");
            holder.textView2.setText("9 Corderie Street, Port Louis");
        } else if (holder.textView.getText().toString().equals("Government House")) {
            holder.textView1.setText("Government House");
            holder.textView2.setText("Intendance Street, Port Louis");
        } else if (holder.textView.getText().toString().equals("Place D’Armes")) {
            holder.textView1.setText("Place D’Armes");
            holder.textView2.setText("Intendance Street, Port Louis");
        } else if (holder.textView.getText().toString().equals("Blue Penny Museum")) {
            holder.textView1.setText("Blue Penny Museum");
            holder.textView2.setText("Caudan Waterfront, Port Louis");
        } else if (holder.textView.getText().toString().equals("St Louis Cathedral")) {
            holder.textView1.setText("St Louis Cathedral");
            holder.textView2.setText("Bourbon Street, Port Louis");
        } else if (holder.textView.getText().toString().equals("Natural History Museum")) {
            holder.textView1.setText("Natural History Museum");
            holder.textView2.setText("Poudriere Street, Port Louis");
        } else if (holder.textView.getText().toString().equals("Aapravasi Ghat")) {
            holder.textView1.setText("Aapravasi Ghat");
            holder.textView2.setText("Trou Fanfaron, Port Louis");
        } else if (holder.textView.getText().toString().equals("Mauritius Postal Museum")) {
            holder.textView1.setText("Mauritius Postal Museum");
            holder.textView2.setText("Trunk Road, Port Louis");
        } else if (holder.textView.getText().toString().equals("Forte Adelaide")) {
            holder.textView1.setText("Forte Adelaide");
            holder.textView2.setText("Sebastopol Street, Port Louis");
        } else if (holder.textView.getText().toString().equals("Photography Museum")) {
            holder.textView1.setText("Photography Museum");
            holder.textView2.setText("Rue du Vieux Conseil, Port Louis");
        } else if (holder.textView.getText().toString().equals("Marie Reine Chapel")) {
            holder.textView1.setText("Marie Reine Chapel");
            holder.textView2.setText("Port Louis, Port Louis");
        } else if (holder.textView.getText().toString().equals("Jardin de la Compagnie")) {
            holder.textView1.setText("Jardin de la Compagnie");
            holder.textView2.setText("La Chaussée, Port Louis");
        } else if (holder.textView.getText().toString().equals("Odysseo")) {
            holder.textView1.setText("Odysseo");
            holder.textView2.setText("Les Salines, Port Louis");
        } else if (holder.textView.getText().toString().equals("Chateau de Labourdonnais")) {
            holder.textView1.setText("Chateau de Labourdonnais");
            holder.textView2.setText("Domaine de Labourdonnais, Mapou");
        } else if (holder.textView.getText().toString().equals("Gris Gris")) {
            holder.textView1.setText("Gris Gris");
            holder.textView2.setText("Gris Gris, Souillac");
        } else if (holder.textView.getText().toString().equals("Rochester Falls")) {
            holder.textView1.setText("Rochester Falls");
            holder.textView2.setText("District of Savanne");
        } else if (holder.textView.getText().toString().equals("Bel Ombre Nature Res.")) {
            holder.textView1.setText("Bel Ombre Nature Res.");
            holder.textView2.setText("Bel Ombre, District of Souillac");
        } else if (holder.textView.getText().toString().equals("La Vallee de Couleurs")) {
            holder.textView1.setText("La Vallee de Couleurs");
            holder.textView2.setText("Chamouny, District of Savanne");
        } else if (holder.textView.getText().toString().equals("Blue Bay Beach")) {
            holder.textView1.setText("Blue Bay Beach");
            holder.textView2.setText("Blue Bay, District of Grand Port");
        } else if (holder.textView.getText().toString().equals("Mahebourg Waterfront")) {
            holder.textView1.setText("Mahebourg Waterfront");
            holder.textView2.setText("Mahebourg, District of Grand Port");
        } else if (holder.textView.getText().toString().equals("Mahebourg Museum")) {
            holder.textView1.setText("Mahebourg Museum");
            holder.textView2.setText("Royal Road, Mahebourg");
        } else if (holder.textView.getText().toString().equals("Ile aux Fouqets")) {
            holder.textView1.setText("Ile aux Fouqets");
            holder.textView2.setText("Ile aux Fouqets, District of Grand Port");
        } else if (holder.textView.getText().toString().equals("Ile aux Aigrettes")) {
            holder.textView1.setText("Ile aux Aigrettes");
            holder.textView2.setText("Ile aux Aigrettes, District of Grand Port");
        } else if (holder.textView.getText().toString().equals("Albion Lighthouse")) {
            holder.textView1.setText("Albion Light House");
            holder.textView2.setText("Albion, District of Black River");
        } else if (holder.textView.getText().toString().equals("Flic en Flac")) {
            holder.textView1.setText("Flic en Flac");
            holder.textView2.setText("Flic en Flac, District of Black River");
        } else if (holder.textView.getText().toString().equals("Casela World of Adventures")) {
            holder.textView1.setText("Casela Adventure Park");
            holder.textView2.setText("Royal Road, Cascavelle");
        } else if (holder.textView.getText().toString().equals("Tamarin Bay Beach")) {
            holder.textView1.setText("Tamarin Bay Beach");
            holder.textView2.setText("Tamarin, District of Black River");
        } else if (holder.textView.getText().toString().equals("La Preneuse beach")) {
            holder.textView1.setText("La Preneuse beach");
            holder.textView2.setText("Black River, District of Black River");
        } else if (holder.textView.getText().toString().equals("Martello Tower Heritage Museum")) {
            holder.textView1.setText("Martello Tower");
            holder.textView2.setText("La Preneuse, District of Black River");
        } else if (holder.textView.getText().toString().equals("Le Morne Brabant")) {
            holder.textView1.setText("Le Morne Brabant");
            holder.textView2.setText("Le Morne, District of Black River");
        } else if (holder.textView.getText().toString().equals("Maconde Viewpoint")) {
            holder.textView1.setText("Maconde Viewpoint");
            holder.textView2.setText("Baie du Cap, District of Savanne");
        } else if (holder.textView.getText().toString().equals("Seven Coloured Earths")) {
            holder.textView1.setText("Seven Coloured Earths");
            holder.textView2.setText("Chamarel, District of Black River");
        } else if (holder.textView.getText().toString().equals("Chamarel Waterfalls")) {
            holder.textView1.setText("Chamarel Waterfalls");
            holder.textView2.setText("Chamarel, District of Black River");
        } else if (holder.textView.getText().toString().equals("Black River Gorges")) {
            holder.textView1.setText("Black River Gorges");
            holder.textView2.setText("Black River Gorges, District of Black River");
        } else if (holder.textView.getText().toString().equals("Le Morne Beach")) {
            holder.textView1.setText("Le Morne Beach");
            holder.textView2.setText("Le Morne, District of Black River");
        } else if (holder.textView.getText().toString().equals("Rhumerie de Chamarel")) {
            holder.textView1.setText("Rhumerie de Chamarel");
            holder.textView2.setText("Chamarel, District of Black River");
        } else if (holder.textView.getText().toString().equals("Curious Corner")) {
            holder.textView1.setText("Curious Corners");
            holder.textView2.setText("Baie du Cap, Chamarel");
        } else {
            holder.textView1.setText("null");
            holder.textView2.setText("null");
        }


    }

    @Override
    public int getItemCount() {
        return beacModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView, textView1, textView2, number, textView3;
        public ImageView imageView, add, remove, map;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            number = itemView.findViewById(R.id.number);
            textView1 = itemView.findViewById(R.id.textView1);
            textView2 = itemView.findViewById(R.id.textView2);
            imageView = itemView.findViewById(R.id.imageView);
            add = itemView.findViewById(R.id.add);
            remove = itemView.findViewById(R.id.remove);
            textView3 = itemView.findViewById(R.id.textView3);
            map = itemView.findViewById(R.id.map);
        }
    }

    // Interface to handle drag start event
    public interface OnStartDragListener {
        void onStartDrag(ViewHolder holder);
    }
}
