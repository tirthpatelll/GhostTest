package com.example.ghosttest
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.ghosttest.R.layout.activity_main
import com.example.ghosttest.ui.login.LoginActivity
import com.example.ghosttest.ui.settings.SettingsActivity
import com.example.ghosttest.ui.settings.UserProfileActivity
import com.google.firebase.auth.FirebaseAuth
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.geojson.Point
import com.mapbox.maps.plugin.PuckBearing
import com.mapbox.maps.plugin.locationcomponent.createDefault2DPuck
import com.mapbox.maps.plugin.viewport.viewport

class MainActivity : AppCompatActivity() {

    //set text on the TextView when ghost button is cliked


    private lateinit var mapView: MapView
    private val locationPermissionRequestCode = 1
    private val auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_main)

        mapView = findViewById(R.id.mapView)

        // Check permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionRequestCode)
        } else {
            initializeMap()
        }

        val settingsButton: Button = findViewById(R.id.btn_settings)
        settingsButton.setOnClickListener { view ->
            showPopupMenu(view)
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
            mapView.mapboxMap.setCamera(cameraOptions)
        }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeMap()
            } else {
                Toast.makeText(this, "Location permission is required to display the puck", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        val inflater: MenuInflater = popupMenu.menuInflater
        inflater.inflate(R.menu.settings_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.menu_profile -> {
                    // Handle profile menu item click
                    startActivity(Intent(this, UserProfileActivity::class.java))
                    true
                }
                R.id.menu_settings -> {
                    // Handle settings menu item click
                    startActivity(Intent(this, SettingsActivity::class.java))
                    true
                }
                R.id.menu_logout -> {
                    auth.signOut() // sign out the user
                    // Handle logout menu item click
                    startActivity(Intent(this, LoginActivity::class.java))
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }
}
