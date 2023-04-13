package com.example.buy_sellnow.Activity

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.content.pm.PackageManager
import android.location.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.buy_sellnow.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class Map : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private lateinit var mapView: MapView
    private lateinit var map: GoogleMap
    private lateinit var searchEditText: EditText
    private lateinit var searchButton: ImageButton
    private lateinit var backSearchBtn: ImageButton
    private lateinit var btnGetLocation: ImageButton

    private lateinit var selectedLocation: LatLng
    private lateinit var direccion: String
    private val zoomLevel = 8f
    private lateinit var searchMapErMsg: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
        searchMapErMsg = findViewById(R.id.searchMapErMsg)
        btnGetLocation = findViewById(R.id.btnGetLocation)
        backSearchBtn = findViewById(R.id.backSearchBtn)
        // Inicializar elementos de la interfaz
        searchEditText = findViewById(R.id.searchEditText)
        searchButton = findViewById(R.id.searchButton)

        /* Boton que lleva en ultimo fragmento */
        backSearchBtn.setOnClickListener {
            if(::selectedLocation.isInitialized){
                val intent = Intent().apply {
                    putExtra("LATITUDE", selectedLocation.latitude)
                    putExtra("LONGITUDE", selectedLocation.longitude)
                    putExtra("DIRECCION", direccion)
                }
                setResult(Activity.RESULT_OK, intent)
                onBackPressed()
            }else{
                Toast.makeText(this, "Porfavor seleciciona una ubicacion", Toast.LENGTH_SHORT).show()
            }
        }

        /* Boton que recoje la ubicacion de usuario */
        btnGetLocation.setOnClickListener { getCurrentLocation() }

        // Configurar el botón de búsqueda
        searchButton.setOnClickListener {
            searchLocation()
        }
    }

    private fun searchLocation() {
        if(searchEditText.text.isNotEmpty()){
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(searchEditText.windowToken, 0)

            val location = searchEditText.text.toString()

            // Crear un objeto Geocoder para obtener las coordenadas de la ubicación
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses = geocoder.getFromLocationName(location, 1)

            // Si se encontró la ubicación, mostrarla en el mapa
            if (addresses!!.isNotEmpty()) {
                val address = addresses[0]
                val latLng = LatLng(address.latitude, address.longitude)
                selectedLocation = latLng
                setDireccion(latLng)
                map.addMarker(MarkerOptions().position(latLng).title(location))
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            } else {
                Toast.makeText(this, "Ubicación no encontrada", Toast.LENGTH_SHORT).show()
            }
        }else{
            searchMapErMsg.visibility = View.VISIBLE
            Handler().postDelayed({
                searchMapErMsg.visibility = View.GONE
            }, 3000)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val homeLatLng = LatLng(41.4161515,2.1983615)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLatLng, zoomLevel))
        map.setOnMapClickListener { latLng ->
            selectedLocation = latLng
            map.clear()
            map.addMarker(MarkerOptions().position(latLng))
        }
    }

    private fun getCurrentLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // El usuario ha otorgado permisos de ubicación
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    // La última ubicación conocida del usuario puede ser null, verificar
                    if (location != null) {
                        val latLng = LatLng(location.latitude, location.longitude)
                        selectedLocation = latLng
                        setDireccion(latLng)
                        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17f)
                        map.addMarker(MarkerOptions().position(latLng).title("You Location"))
                        map.moveCamera(cameraUpdate)
                    } else {
                        // Si la ubicación es null, mostrar un mensaje de error o solicitar la ubicación de nuevo
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("Ubicación no encontrada")
                        builder.setMessage("No se pudo obtener la ubicación actual. ¿Desea habilitar la ubicación en su dispositivo?")
                        builder.setPositiveButton("Sí") { dialog, which ->
                            // Abrir la configuración de ubicación del dispositivo
                            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                        }
                        builder.setNegativeButton("No") { dialog, which ->
                            // No hacer nada
                        }
                        val dialog = builder.create()
                        dialog.show()
                    }
                }
        } else {
            // Si el usuario no ha otorgado permisos de ubicación, solicitarlos
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    // Este método se ejecuta después de que el usuario concede o deniega los permisos de ubicación
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Si el usuario ha concedido los permisos de ubicación, obtener la ubicación actual del usuario
                getCurrentLocation()
            } else {
                // Si el usuario ha denegado los permisos de ubicación, mostrar un mensaje de error
                Toast.makeText(
                    this,
                    "No se pueden obtener los permisos de ubicación",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setDireccion(latLng: LatLng){
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses: List<Address> =
            geocoder.getFromLocation(latLng!!.latitude, latLng.longitude, 1)!!
        val address: Address = addresses[0]
        direccion = address.getAddressLine(0)
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}