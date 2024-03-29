package com.example.buy_sellnow.Activity

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickMultipleVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.buy_sellnow.Adapters.ImageRecycleView
import com.example.buy_sellnow.Connexions.FireBaseConexion
import com.example.buy_sellnow.Model.Enum.ProductStatus
import com.example.buy_sellnow.Model.Enum.Status
import com.example.buy_sellnow.Model.Product
import com.example.buy_sellnow.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.FileNotFoundException
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class AddProduct : AppCompatActivity() {
    companion object {
        private lateinit var preferences: SharedPreferences
        var imageUris: ArrayList<Uri> = ArrayList()
        private const val CAMERA_PERMISSION_REQUEST_CODE = 1001
        private const val EXTERNAL_PERMISSION_REQUEST_CODE = 1002
        private var CAMERA_EXTERNAL = 0 //1 CAM - 2 EXTERNAL
        lateinit var adapter: ImageRecycleView
        lateinit var recycleView: RecyclerView
        var maxImage = 5
        lateinit var productStatusSpinner: Spinner
        lateinit var addProductDeliverySpinner: Spinner
        lateinit var addProductCategoriSpinner: Spinner

        lateinit var addProductCameraBtn: Button
        lateinit var addProductGallaryBtn: Button
        lateinit var addProductGallaryPick: Button

        /** Layouts etc*/
        lateinit var mediaSelecterCons: ConstraintLayout
        lateinit var addProductToolbar: MaterialToolbar
        lateinit var addProductBottomNav: BottomNavigationView
        lateinit var addProductImageViewConst: ConstraintLayout
        lateinit var addProductDetailPickConst: ConstraintLayout

        /** Variables para spinner item selected */
        var productStatusSpinnerSelected: Int = 0
        var addProductDeliverySpinnerSelected: Int = 0
        lateinit var addProductCategoriSpinnerSelected: String
        var categoriaId: Int = 0

        /** Form input variables */
        lateinit var editTextAddProductBrand: EditText
        lateinit var editTextAddProductName: EditText
        lateinit var editTextAddProductDescription: EditText
        lateinit var editTextAddProductWeight: EditText
        lateinit var editTextAddProductPrice: EditText

        /** text view variables error message */
        lateinit var editTextAddProductImageErMsg: TextView
        lateinit var editTextAddProductBrandErMsg: TextView
        lateinit var editTextAddProductNameErMsg: TextView
        lateinit var editTextAddProductDescriptionErMsg: TextView
        lateinit var editTextAddProductWeightErMsg: TextView
        lateinit var editTextAddProductPriceErMsg: TextView
        lateinit var productStatusSpinnerErMsg: TextView
        lateinit var addProductDeliverySpinnerErMsg: TextView
        lateinit var addProductCategoriSpinnerErMsg: TextView

        /** Validation variable boolean for form*/
        var imageMinimamBol: Boolean = false
        var editTextAddProductNameBol: Boolean = false
        var editTextAddProductBrandBol: Boolean = false
        var editTextAddProductDescriptionBol: Boolean = false
        var editTextAddProductWeightBol: Boolean = false
        var editTextAddProductPriceBol: Boolean = false
        var productStatusSpinnerSelectedBol: Boolean = false
        var addProductDeliverySpinnerSelectedBol: Boolean = false
        var addProductCategoriSpinnerSelectedBol: Boolean = false
    }
    var pickMultipleMedia = registerForActivityResult(PickMultipleVisualMedia(maxImage)) { uris ->
        if (uris.isNotEmpty()) {
            /*for (uri in uris){
                val bitmap = uriToBitmap(uri, contentResolver)
                bitmap?.let { imageUris.add(it) }
            }*/
            imageUris.addAll(uris)
            adapterImage()
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }


    private fun validateForm(): Boolean {
        imageMinimamBol = imageUris.size>0
        editTextAddProductNameBol = editTextAddProductName.text.isNotEmpty()
        editTextAddProductBrandBol = editTextAddProductBrand.text.isNotEmpty()
        editTextAddProductDescriptionBol = editTextAddProductDescription.text.isNotEmpty()
        editTextAddProductWeightBol = editTextAddProductWeight.text.isNotEmpty()
        editTextAddProductPriceBol = editTextAddProductPrice.text.isNotEmpty()
        productStatusSpinnerSelectedBol = (productStatusSpinnerSelected>0)
        addProductDeliverySpinnerSelectedBol = (addProductDeliverySpinnerSelected>0)
        addProductCategoriSpinnerSelectedBol = !(addProductCategoriSpinnerSelected.contains('!'))
        return (imageMinimamBol &&
                editTextAddProductBrandBol &&
                editTextAddProductNameBol &&
                editTextAddProductDescriptionBol &&
                editTextAddProductWeightBol &&
                editTextAddProductPriceBol &&
                productStatusSpinnerSelectedBol &&
                addProductDeliverySpinnerSelectedBol &&
                addProductCategoriSpinnerSelectedBol)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
        preferences = getSharedPreferences(getString(R.string.sesionPref), Context.MODE_PRIVATE)
        productStatusSpinner = findViewById(R.id.productStatusSpinner)
        addProductDeliverySpinner = findViewById(R.id.addProductDeliverySpinner)
        addProductCategoriSpinner = findViewById(R.id.addProductCategoriSpinner)
        addProductToolbar = findViewById(R.id.addProductToolbar)
        mediaSelecterCons = findViewById(R.id.mediaSelecterCons)
        addProductCameraBtn = findViewById(R.id.addProductCameraBtn)
        addProductGallaryBtn = findViewById(R.id.addProductGallaryBtn)
        editTextAddProductImageErMsg = findViewById(R.id.editTextAddProductImageErMsg)
        addProductGallaryPick = findViewById(R.id.addProductGallaryBtnPick)
        addProductBottomNav = findViewById(R.id.addProductBottomNav)
        addProductImageViewConst = findViewById(R.id.addProductImageViewConst)
        addProductDetailPickConst = findViewById(R.id.addProductDetailPickConst)
        val addNewProduct: Button = findViewById(R.id.addNewProduct)
        /** Variales de Error Message  **/
        editTextAddProductBrandErMsg = findViewById(R.id.editTextAddProductBrandErMsg)
        editTextAddProductNameErMsg = findViewById(R.id.editTextAddProductNameErMsg)
        editTextAddProductDescriptionErMsg = findViewById(R.id.editTextAddProductDescriptionErMsg)
        editTextAddProductWeightErMsg = findViewById(R.id.editTextAddProductWeightErMsg)
        editTextAddProductPriceErMsg = findViewById(R.id.editTextAddProductPriceErMsg)
        productStatusSpinnerErMsg = findViewById(R.id.productStatusSpinnerErMsg)
        addProductDeliverySpinnerErMsg = findViewById(R.id.addProductDeliverySpinnerErMsg)
        addProductCategoriSpinnerErMsg = findViewById(R.id.addProductCategoriSpinnerErMsg)

        /** Variables de inputs **/
        editTextAddProductBrand = findViewById(R.id.editTextAddProductBrand)
        editTextAddProductName = findViewById(R.id.editTextAddProductName)
        editTextAddProductDescription = findViewById(R.id.editTextAddProductDescription)
        editTextAddProductWeight = findViewById(R.id.editTextAddProductWeight)
        editTextAddProductPrice = findViewById(R.id.editTextAddProductPrice)

        addNewProduct.setOnClickListener {
            if (!validateForm()) {
                showErrorMsg()
            }else{
                showErrorMsg()
                var conexion: FireBaseConexion =  FireBaseConexion()

                val date = LocalDateTime.now()
                val userId = preferences.getString("userId", null)
                var product = Product(
                    UUID.randomUUID().toString(),
                    editTextAddProductName.text.toString(),
                    addProductCategoriSpinnerSelected,
                    "",
                    editTextAddProductDescription.text.toString(),
                    editTextAddProductPrice.text.toString(),
                    editTextAddProductWeight.text.toString(),
                    "", date.toString(),
                    "",
                    ArrayList<String>(),
                    "",
                    editTextAddProductBrand.text.toString(),
                    "0",
                    "",
                    Status.Activeted,

                    (if(1== productStatusSpinnerSelected) ProductStatus.Nuevo else
                        if(2== productStatusSpinnerSelected) ProductStatus.BuenEstado else
                            if(3== productStatusSpinnerSelected) ProductStatus.Bien else
                                if(4== productStatusSpinnerSelected) ProductStatus.Mejorar else TODO()) as ProductStatus,
                    addProductDeliverySpinnerSelected==1,
                    userId!!,
                    productStatusSpinnerSelected,
                    categoriaId
                )


                conexion.createProduct(product, imageUris)
                resetForm()
                onBackPressed()
            }
        }


        recycleView = findViewById(R.id.addProductImageView)
        recycleView.setOnClickListener {

        }

        addProductGallaryPick.setOnClickListener {
            maxImage = (5 - imageUris.size)
            mediaSelecterCons.visibility = View.VISIBLE
        }

        addProductCameraBtn.setOnClickListener {
            mediaSelecterCons.visibility = View.GONE
            if (!isCameraPermissionGranted()) {
                // Farem una petició de permisos
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.CAMERA),
                    CAMERA_PERMISSION_REQUEST_CODE
                )
            } else {
                // Sinó farem l'intent de mostrar la càmera
                cameraResult.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
            }
        }
        //selecionarImagen.setOnClickListener {
        // mediaSelecterCons.visibility = View.VISIBLE
        // }

        addProductGallaryBtn.setOnClickListener {
            if (isExternalPermissionGranted()) {
                pickMultipleMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly ))
            } else {
                // Farem una petició de permisos
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    EXTERNAL_PERMISSION_REQUEST_CODE
                )
            }
            mediaSelecterCons.visibility = View.GONE
        }
        addProductToolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        getSpinnerItemSelected(
            productStatusSpinner,
            resources.getStringArray(R.array.status_product_list)
        )
        getSpinnerItemSelected(
            addProductDeliverySpinner,
            resources.getStringArray(R.array.delivery_list)
        )
        getSpinnerItemSelected(
            addProductCategoriSpinner,
            resources.getStringArray(R.array.category_array_list)
        )
    }

    private fun resetForm(){
        editTextAddProductName.text.clear()
        editTextAddProductDescription.text.clear()
        editTextAddProductPrice.text.clear()
        editTextAddProductWeight.text.clear()
        editTextAddProductBrand.text.clear()
        imageUris.clear()
    }

    private fun showErrorMsg() {
        editTextAddProductImageErMsg.visibility = if (imageMinimamBol) View.GONE else View.VISIBLE
        editTextAddProductBrandErMsg.visibility = if (editTextAddProductBrandBol) View.GONE else View.VISIBLE
        editTextAddProductNameErMsg.visibility = if (editTextAddProductNameBol) View.GONE else View.VISIBLE
        editTextAddProductDescriptionErMsg.visibility = if (editTextAddProductDescriptionBol) View.GONE else View.VISIBLE
        editTextAddProductWeightErMsg.visibility = if (editTextAddProductWeightBol) View.GONE else View.VISIBLE
        editTextAddProductPriceErMsg.visibility = if (editTextAddProductPriceBol) View.GONE else View.VISIBLE
        productStatusSpinnerErMsg.visibility = if (productStatusSpinnerSelectedBol) View.GONE else View.VISIBLE
        addProductDeliverySpinnerErMsg.visibility = if (addProductDeliverySpinnerSelectedBol) View.GONE else View.VISIBLE
        addProductCategoriSpinnerErMsg.visibility = if (addProductCategoriSpinnerSelectedBol) View.GONE else View.VISIBLE
    }

    private fun adapterImage() {
        recycleView.visibility = if(imageUris.size>0) View.VISIBLE else View.GONE
        recycleView.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        adapter = ImageRecycleView(imageUris, this, true)
        recycleView.adapter = adapter
    }

    private fun getSpinnerItemSelected(s: Spinner, array: Array<String>) {
        if (s != null) {
            val aA = ArrayAdapter(this, android.R.layout.simple_spinner_item, array)
            aA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            s.adapter = aA
            s.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    val item = parent!!.getItemAtPosition(position).toString()
                    val selectedSpinnerId = parent?.id ?: return
                    when (selectedSpinnerId) {
                        R.id.productStatusSpinner -> {productStatusSpinnerSelected = position}
                        R.id.addProductDeliverySpinner -> {addProductDeliverySpinnerSelected = position}
                        R.id.addProductCategoriSpinner -> {addProductCategoriSpinnerSelected = item
                            categoriaId=position
                        }
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }
        }
    }
    fun uriToBitmap(uri: Uri, contentResolver: ContentResolver): Bitmap? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    /** Resposta de la càmera */
    private val cameraResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                val imageBitmap = intent?.extras?.get("data") as Bitmap
                //imageUris.add(intent)
                if (imageBitmap != null) {
                    Toast.makeText(this, "anterea",Toast.LENGTH_SHORT).show()
                    //imageUris.add(imageBitmap)
                    adapterImage()
                }
                //imageView.setImageBitmap(imageBitmap)
                CAMERA_EXTERNAL = 1
            }
        }

    /** Permisos camera photo */
    private fun isCameraPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    /** Permisos external storage */
    private fun isExternalPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    /** Resposta a l'acció de l'usuari en validar o no els permisos */
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
        } else if (requestCode == EXTERNAL_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickMultipleMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly ))
            } else {
                // Permission denied, handle accordingly
            }
        }
    }
}