package com.moutamid.sqlapp.activities.offlinemap;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fxn.stash.Stash;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.moutamid.sqlapp.R;
import com.moutamid.sqlapp.offlinemap.DurationCalculator;
import com.moutamid.sqlapp.offlinemap.MapActivity;
import com.moutamid.sqlapp.offlinemap.MyAdapter;

import java.util.ArrayList;
import java.util.List;

public class DirectionBottomSheetDialog extends BottomSheetDialogFragment {

    private static final String TAG = "DirectionBottomSheet";

    private ImageView activeImage1, inactiveImage1;
    private ImageView activeImage2, inactiveImage2;
    private ImageView activeImage3, inactiveImage3;
    private ImageView activeImage4, inactiveImage4;
    public static TextView time, distance, destination_name;
    TextView go;
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    private List<String> data;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
    ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.direction_bottom_sheet, container, false);

        ImageView close_button = v.findViewById(R.id.close_button);
        go = v.findViewById(R.id.go);
        destination_name = v.findViewById(R.id.final_name);
        destination_name.setText(Stash.getString("map_name"));
        distance = v.findViewById(R.id.distance);
        time = v.findViewById(R.id.time);

        activeImage1 = v.findViewById(R.id.active_image1);
        inactiveImage1 = v.findViewById(R.id.inactive_image1);

        activeImage2 = v.findViewById(R.id.active_image2);
        inactiveImage2 = v.findViewById(R.id.inactive_image2);

        activeImage3 = v.findViewById(R.id.active_image3);
        inactiveImage3 = v.findViewById(R.id.inactive_image3);

        activeImage4 = v.findViewById(R.id.active_image4);
        inactiveImage4 = v.findViewById(R.id.inactive_image4);

        // Set click listeners
        setClickListener(inactiveImage1, activeImage1, 1);
        setClickListener(inactiveImage2, activeImage2, 2);
        setClickListener(inactiveImage3, activeImage3, 3);
        setClickListener(inactiveImage4, activeImage4, 4);

        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        time.setText(MapActivity.formattedDistance + " km");
        distance.setText(DurationCalculator.formatDuration(MapActivity.bikingDuration) + "");
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MapActivity.addOverlayLayers(MapActivity.b.mapView.getLayerManager().getLayers());
                dismiss();
            }
        });
        data = new ArrayList<>();
        data.add("My Location");
        data.add(Stash.getString("map_name"));

        recyclerView = v.findViewById(R.id.recyclerView);
        myAdapter = new MyAdapter(getContext(), data);
        recyclerView.setAdapter(myAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                myAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // Not used
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return v;
    }

    private void setClickListener(final ImageView inactiveImage, final ImageView activeImage, final int imageNumber) {
        inactiveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide all active images
                hideAllActiveImages();
                // Show the clicked active image and hide its corresponding inactive image
                activeImage.setVisibility(View.VISIBLE);
                inactiveImage.setVisibility(View.GONE);
                // Log which image is active
                Log.d(TAG, "Active Image: " + imageNumber);
                if (imageNumber == 1) {
                    distance.setText(DurationCalculator.formatDuration(MapActivity.drivingDuration) + "");
                } else if (imageNumber == 2) {
                    distance.setText(DurationCalculator.formatDuration(MapActivity.walkingDuration) + "");
                } else if (imageNumber == 3) {
                    distance.setText(DurationCalculator.formatDuration(MapActivity.tramDuration) + "");
                } else if (imageNumber == 4) {
                    distance.setText(DurationCalculator.formatDuration(MapActivity.bikingDuration) + "");
                }
            }
        });
    }

    private void hideAllActiveImages() {
        activeImage1.setVisibility(View.GONE);
        inactiveImage1.setVisibility(View.VISIBLE);

        activeImage2.setVisibility(View.GONE);
        inactiveImage2.setVisibility(View.VISIBLE);

        activeImage3.setVisibility(View.GONE);
        inactiveImage3.setVisibility(View.VISIBLE);

        activeImage4.setVisibility(View.GONE);
        inactiveImage4.setVisibility(View.VISIBLE);
    }
}
