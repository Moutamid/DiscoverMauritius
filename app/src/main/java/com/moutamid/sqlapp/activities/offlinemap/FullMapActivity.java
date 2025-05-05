package com.moutamid.sqlapp.activities.offlinemap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.AppCompatActivity;
import com.moutamid.sqlapp.R;

import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.layer.Layers;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.overlay.Marker;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.rendertheme.InternalRenderTheme;
import org.mapsforge.map.view.MapView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class FullMapActivity extends AppCompatActivity {
    public static final String TAG = "MapActivity";
    private double[] latitudes;
    private double[] longitudes;
    Intent intent;
    MapView mapView;

    @Override
protected void onCreate(Bundle savedInstanceState) {
    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        AndroidGraphicFactory.createInstance(getApplicationContext()); // Ensure the factory is initialized
        setContentView(R.layout.activity_full_map);
        mapView = findViewById(R.id.mapView);
        if (mapView == null) {
            Log.e(TAG, "MapView is not initialized");
            return;
        }
        intent = getIntent();
        latitudes = intent.getDoubleArrayExtra("latitudes");
        longitudes = intent.getDoubleArrayExtra("longitudes");
        openMap();
    }

    private void openMap() {
        mapView.getMapScaleBar().setVisible(true);
//        mapView.setBuiltInZoomControls(true);

        TileCache cache = AndroidUtil.createTileCache(
                this,
                "mycache",
                mapView.getModel().displayModel.getTileSize(),
                1f,
                mapView.getModel().frameBufferModel.getOverdrawFactor()
        );

        try {
            File cacheDir = new File(getFilesDir(), "cached_images");
            String fullPath = cacheDir.getAbsolutePath();
            File mapFile = new File(fullPath + "/mauritius.map");
            org.mapsforge.map.reader.MapFile mapStore = new org.mapsforge.map.reader.MapFile(mapFile);
            TileRendererLayer renderLayer = new TileRendererLayer(
                    cache,
                    mapStore,
                    mapView.getModel().mapViewPosition,
                    AndroidGraphicFactory.INSTANCE
            );
            renderLayer.setXmlRenderTheme(InternalRenderTheme.DEFAULT);
            mapView.getLayerManager().getLayers().add(renderLayer);
            Log.d(TAG, "TileRendererLayer added successfully.");

            mapView.setCenter(new LatLong(-20.2886, 57.5736));
            mapView.setZoomLevel((byte) 10);

            if (latitudes != null && longitudes != null) {
                for (int i = 0; i < latitudes.length; i++) {
                    Log.d(TAG, "Lat: " + latitudes[i] + ", Lng: " + longitudes[i]);
                    LatLong markerLocation = new LatLong(latitudes[i], longitudes[i]);
                    addMarker(mapView.getLayerManager().getLayers(), markerLocation, R.drawable.dot);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error setting up map: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void copyMapFileToCache(int resourceId, File outputFile) {
        try (InputStream inputStream = getResources().openRawResource(resourceId);
             FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            Log.d(TAG, "Map file copied successfully to " + outputFile.getAbsolutePath());
        } catch (Exception e) {
            Log.e(TAG, "Error copying map file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addMarker(Layers layers, LatLong latLong, int resourceId) {
        Bitmap bitmap = AndroidGraphicFactory.convertToBitmap(getResources().getDrawable(resourceId));
        Marker marker = new Marker(latLong, bitmap, 0, 0);
        layers.add(marker);
    }

    public void back(View view) {
        onBackPressed();
    }
}
