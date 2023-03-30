package com.example.buy_sellnow.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.buy_sellnow.R
import com.example.buy_sellnow.databinding.ActivityMapsBinding
import com.google.android.gms.maps.GoogleMap.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        // Set the map type to Hybrid
        mMap.mapType = MAP_TYPE_HYBRID
        // Add a marker in Sydney and move the camera
        val barcelona = LatLng(2.15899, 41.38879)
        mMap.addMarker(MarkerOptions().position(barcelona).title("Marker in Barcelona"))
        // Move the camera to the map coordenates and zoom in closer
        mMap.moveCamera(CameraUpdateFactory.newLatLng(barcelona))
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15F))
        // Display Traffic
        mMap.isTrafficEnabled = true
    }
}