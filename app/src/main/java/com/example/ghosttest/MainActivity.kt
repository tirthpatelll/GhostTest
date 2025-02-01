package com.example.ghosttest
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.ghosttest.R.layout.activity_main
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
<<<<<<< HEAD
=======
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
>>>>>>> faaa15239eaeb64e7401e433bfbfb08fad584a61
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.geojson.Point
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.viewport.viewport

class MainActivity : AppCompatActivity() {

    private lateinit var mapView: MapView
<<<<<<< HEAD
    private val locationPermissionRequestCode = 1
=======
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
>>>>>>> faaa15239eaeb64e7401e433bfbfb08fad584a61

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_main)

        mapView = findViewById(R.id.mapView)

        // Check permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
<<<<<<< HEAD
                locationPermissionRequestCode
=======
                LOCATION_PERMISSION_REQUEST_CODE
>>>>>>> faaa15239eaeb64e7401e433bfbfb08fad584a61
            )
        } else {
            initializeMap()
        }
    }

    private fun initializeMap() {
        with(mapView) {
            location.locationPuck = createDefault2DPuck(withBearing = true)
            location.enabled = true
            location.puckBearing = PuckBearing.COURSE
            location.puckBearingEnabled = true
            viewport.transitionTo(
                targetState = viewport.makeFollowPuckViewportState(),
                transition = viewport.makeImmediateViewportTransition()
            )
        }
            // Set initial camera position
            val cameraOptions = CameraOptions.Builder()
                .zoom(12.0)
                .center(Point.fromLngLat(-81.0912, 32.0809))  // Centered over the US
                .pitch(0.0)
                .bearing(0.0)
                .build()

<<<<<<< HEAD
            mapView.mapboxMap.setCamera(cameraOptions)
=======
            mapView.getMapboxMap().setCamera(cameraOptions)
>>>>>>> faaa15239eaeb64e7401e433bfbfb08fad584a61
        }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
<<<<<<< HEAD
        if (requestCode == locationPermissionRequestCode) {
=======
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
>>>>>>> faaa15239eaeb64e7401e433bfbfb08fad584a61
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeMap()
            } else {
                Toast.makeText(this, "Location permission is required to display the puck", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
