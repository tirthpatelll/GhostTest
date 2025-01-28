package com.example.ghosttest;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.MapboxMap;
import com.mapbox.maps.Style;

public class MainActivity extends AppCompatActivity {
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize MapView
        mapView = findViewById(R.id.mapView);

        // Access MapboxMap instance and configure the map
        MapboxMap mapboxMap = mapView.getMapboxMap();
        mapboxMap.loadStyleUri(Style.MAPBOX_STREETS, style -> {
            // Set initial camera position
            CameraOptions cameraOptions = new CameraOptions.Builder()
                    .zoom(12.0)
                    .center(Point.fromLngLat(-81.0912, 32.0809)) // Centered over the US
                    .pitch(0.0)
                    .bearing(0.0)
                    .build();

            mapboxMap.setCamera(cameraOptions);
        });
    }
}
