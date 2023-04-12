package com.example.buy_sellnow.Activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.example.buy_sellnow.BuildConfig
import com.example.buy_sellnow.Connexions.FireBaseConexion
import com.example.buy_sellnow.MainActivity
import com.example.buy_sellnow.Model.User
import com.example.buy_sellnow.Model.tempUser
import com.example.buy_sellnow.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.File
import java.io.FileOutputStream
import java.util.*

class EditUserDetail : AppCompatActivity() {
    lateinit var profileSaveBtn: Button
    private lateinit var preferences: SharedPreferences;
    lateinit var imgPickBtn: Button
    lateinit var pickLoction: Button
    lateinit var userImagePicker: ImageView
    lateinit var editTextPersonName: EditText
    lateinit var editTextPersonLastName: EditText
    lateinit var mapProfileView: MapView
    lateinit var editDireccion: TextView
    var imageUri: Uri? = null
    var imgUrl = ""
    lateinit var profilemediaSelecterCons: ConstraintLayout
    lateinit var profileCameraBtn: Button
    lateinit var profileGallaryBtn: Button

    lateinit var location: LatLng
    lateinit var direccion: String

    companion object {
        private lateinit var map: GoogleMap
        private const val REQUEST_CODE = 1
        private const val CAMERA_PERMISSION_REQUEST_CODE = 1001;
        private const val EXTERNAL_PERMISSION_REQUEST_CODE = 1002;
        private var CAMERA_EXTERNAL = 0; //1 CAM - 2 EXTERNAL
    }

    /** Variable que para que startactivity with result o sea estara en la espera de resultado de lo lacalizacion **/
    val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            if (data != null) {
                val lat = data.getDoubleExtra("LATITUDE", 0.0)
                val lng = data.getDoubleExtra("LONGITUDE", 0.0)
                location = LatLng(lat, lng)
                direccion = data.getStringExtra("DIRECCION").toString()
                /*map.addMarker(MarkerOptions().position(location).title("Selected Location"))
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
                mapProfileView.visibility = View.GONE
                mapProfileView.onResume() // Agregue esta línea para actualizar la vista del mapa*/
                Log.d("Location", "entra")
                editDireccion.setText(direccion)

            }
            Log.d("Location", "No entra")

        } else {
            Log.d("Location", "ni entra")
        }
    }
    var pickMultipleMedia = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            imageUri = uri
            userImagePicker.setImageURI(uri)
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user_detail)
        /*mapProfileView = findViewById(R.id.mapProfileView)
        mapProfileView.onCreate(savedInstanceState)
        mapProfileView.getMapAsync(this)*/

        preferences = getSharedPreferences(getString(R.string.sesionPref), Context.MODE_PRIVATE);

        profileSaveBtn = findViewById(R.id.profileSaveBtn)
        pickLoction = findViewById(R.id.pickLoction)
        editDireccion = findViewById(R.id.editDireccion)
        userImagePicker = findViewById(R.id.userImagePicker)
        editTextPersonName = findViewById(R.id.editTextPersonName)
        editTextPersonLastName = findViewById(R.id.editTextPersonLastName)
        profilemediaSelecterCons = findViewById(R.id.profilemediaSelecterCons)
        profileCameraBtn = findViewById(R.id.profileCameraBtn)
        profileGallaryBtn = findViewById(R.id.profileGallaryBtn)
        imgPickBtn = findViewById(R.id.imgPickBtn)

        val userId = preferences.getString("userId", null).toString()
        if(userId!=null){
            val conexion: FireBaseConexion = FireBaseConexion()
            conexion.getUserById(userId){
                if(it != null){
                    editTextPersonName.setText(it.firstName)
                    editTextPersonLastName.setText(it.lastName)
                    editDireccion.setText(it.direccion)
                    Glide.with(this).load(it.userImage)
                        .into(userImagePicker)
                    imgUrl = it.userImage
                    location = LatLng(it.latitude, it.longitude)
                }
            }
        }

        profileCameraBtn.setOnClickListener {
            profilemediaSelecterCons.visibility = View.GONE
            if (!isCameraPermissionGranted()) {
                // Farem una petició de permisos
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_REQUEST_CODE
                )
            } else {
                // Sinó farem l'intent de mostrar la càmera
                cameraResult.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE));
            }
        }
        profileGallaryBtn.setOnClickListener {
            profilemediaSelecterCons.visibility = View.GONE
            if (isExternalPermissionGranted()) {
                pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly));
            } else {
                // Farem una petició de permisos
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    EXTERNAL_PERMISSION_REQUEST_CODE
                )
            }
        }

        pickLoction.setOnClickListener {
            var redrige = Intent(this, Map::class.java);
            activityResultLauncher.launch(redrige);
        }

        imgPickBtn.setOnClickListener {
            profilemediaSelecterCons.visibility = View.VISIBLE
        }

        profileSaveBtn.setOnClickListener {
            if (::direccion.isInitialized && ::location.isInitialized && editTextPersonName.text.isNotEmpty() && editTextPersonLastName.text.isNotEmpty()) {
                val conexion: FireBaseConexion = FireBaseConexion()
                val userId = preferences.getString("userId", null).toString()
                val userToken = preferences.getString("tokenNotify", null).toString()
                conexion.updateUserDetaill(
                    tempUser(
                        userId,
                        imgUrl,
                        editTextPersonName.text.toString(),
                        editTextPersonLastName.text.toString(),
                        userToken,
                        direccion,
                        location.latitude,
                        location.longitude
                    ),
                    imageUri
                )
                val redrige = Intent(this, MainActivity::class.java);
                startActivity(redrige)
            } else {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Falta completar campos")
                builder.setMessage("Porfavor rellene todos los campos necesarios")
                builder.setNegativeButton("Ok") { dialog, which ->
                    // No hacer nada
                }
                val dialog = builder.create()
                dialog.show()
            }
        }
    }

    private fun bitmapToUri(bitmap: Bitmap): Uri {
        // Obtener el directorio de la imagen en el almacenamiento externo de la aplicación
        val imagesDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        // Crear un archivo temporal para guardar la imagen
        val file = File(imagesDirectory, "${UUID.randomUUID()}.jpg")

        // Comprimir la imagen y guardarla en el archivo temporal
        val stream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream.flush()
        stream.close()

        // Devolver la Uri del archivo temporal
        return Uri.fromFile(file)
    }

    // Resposta de la càmera
    private val cameraResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data;
                val imageBitmap = intent?.extras?.get("data") as Bitmap;
                //imageUris.add(intent)
                if (imageBitmap != null) {
                    //Toast.makeText(this, "anterea", Toast.LENGTH_SHORT).show()
                    //imageUris.add(imageBitmap)
                    imageUri =  bitmapToUri(imageBitmap)
                    userImagePicker.setImageURI(imageUri)
                }
                //imageView.setImageBitmap(imageBitmap);
                CAMERA_EXTERNAL = 1;
            }
        }

    // Permisos camera photo
    private fun isCameraPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    //Permisos external storage
    private fun isExternalPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Resposta a l'acció de l'usuari en validar o no els permisos
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with opening camera
            } else {
                // Permission denied, handle accordingly
            }
        }/* else if (requestCode == EXTERNAL_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with opening camera
            } else {
                // Permission denied, handle accordingly
            }
        }*/
    }
/*
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val homeLatLng = LatLng(41.4161515,2.1983615)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLatLng, 15f))
    }

    override fun onResume() {
        super.onResume()
        mapProfileView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapProfileView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapProfileView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapProfileView.onLowMemory()
    }*/
}