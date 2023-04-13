package com.example.buy_sellnow.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.buy_sellnow.Adapters.ImageRecycleView
import com.example.buy_sellnow.Connexions.FireBaseConexion
import com.example.buy_sellnow.Model.Product
import com.example.buy_sellnow.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.appbar.MaterialToolbar

class ProductDetail: AppCompatActivity(), OnMapReadyCallback {
    lateinit var productDetail : MaterialToolbar
    private lateinit var preferences: SharedPreferences

    companion object{
        /** Map **/
        lateinit var productDetailMap: MapView
        private lateinit var map: GoogleMap
        lateinit var redrige: Intent

        /** Product Variables */
        lateinit var productUserId: String
        var producto: Product? = null

        /** User variables */
        lateinit var detail_total_sells: TextView
        lateinit var detail_username: TextView

        /** Product variables */
        lateinit var detail_title: TextView
        lateinit var detail_description: TextView
        lateinit var detail_priece: TextView
        lateinit var detail_profileImg: ImageView

        /** ACTTIVITY PRODUCT DETAIL VARIABLES **/
        lateinit var productDetailChatBtn: Button
        /** Image recycleView */
        lateinit var adapter: ImageRecycleView
        lateinit var productDetailRecycleView: RecyclerView

        val conexion: FireBaseConexion = FireBaseConexion()
        lateinit var dPFavBtn: ToggleButton
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        productDetailMap = findViewById(R.id.productDetailMap)
        productDetailMap.onCreate(savedInstanceState)
        productDetailMap.getMapAsync(this)

        preferences = getSharedPreferences(getString(R.string.sesionPref), Context.MODE_PRIVATE)
        val userId = preferences.getString("userId", null).toString()
        val productId = intent.getStringExtra("productId")
        dPFavBtn = findViewById(R.id.dPFavBtn)
        // Set initial state of button based on Firebase database
        conexion.getFavoriteByProductAndUserId(productId!!, userId) { isFavorite ->
            dPFavBtn.isChecked = isFavorite == true
        }


        // Toggle button state and update Firebase database when clicked
        dPFavBtn.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                conexion.addTFavorite(productId, userId)
            } else {
                conexion.rmFFavorite(productId, userId)
            }
        }
        detail_total_sells = findViewById(R.id.detail_total_sells)
        detail_username = findViewById(R.id.detail_username)
        productDetail = findViewById(R.id.productDetail)
        productDetailChatBtn = findViewById(R.id.productDetailChatBtn)
        detail_title = findViewById(R.id.detail_title)
        detail_description = findViewById(R.id.detail_description)
        detail_priece = findViewById(R.id.detail_priece)
        detail_profileImg = findViewById(R.id.detail_profileImg)

        productDetailChatBtn.setOnClickListener {
            if(producto!=null){
                redrige = Intent(this, ChatDetail::class.java)
                redrige.putExtra("PRODUCT_ID", productId)
                redrige.putExtra("USER_ID", userId)
                redrige.putExtra("PRODUCT_TITLE", producto!!.tituloDeProducto)
                redrige.putExtra("PRODUCT_IMG", producto!!.image[0])
                redrige.putExtra("PRODUCT_USER_ID", productUserId)
                startActivity(redrige)
            }
        }
        productDetailRecycleView = findViewById(R.id.productDetailRecycleView)
        productDetail.setNavigationOnClickListener {
            onBackPressed()
        }
        conexion.getProductById(productId!!) { product ->
            if(product.userId == userId){
                productDetailChatBtn.visibility = View.GONE
                dPFavBtn.visibility = View.GONE
            }

            var imageUrl: ArrayList<Uri> = ArrayList()
            for (prod in product.image){
                imageUrl.add(Uri.parse(prod))
            }
            producto = product
            productUserId = product.userId
            productDetail.setTitle(product.tituloDeProducto)
            productDetailRecycleView.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
            adapter = ImageRecycleView(imageUrl, this, true)
            productDetailRecycleView.adapter = adapter
            detail_title.setText(product.tituloDeProducto)
            detail_description.setText(product.description)
            detail_priece.setText(product.precio+" €")
            product.userId?.let {
                conexion.getUserById(it){
                    if(it != null){
                        detail_username.setText(it.firstName+" "+it.lastName)
                        Glide.with(this).load(it.userImage)
                            .into(detail_profileImg)
                        val location = LatLng(it.latitude, it.longitude)
                        val circleOptions = CircleOptions()
                            .center(location)
                            .radius(250.0)
                            .strokeWidth(1f)
                            .strokeColor(Color.BLUE)
                            .fillColor(Color.argb(50, 255, 0, 0))
                        map.addCircle(circleOptions)
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
                    }
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val homeLatLng = LatLng(41.4161515,2.1983615)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(homeLatLng, 15f))
    }

    override fun onResume() {
        super.onResume()
        productDetailMap.onResume()
    }

    override fun onPause() {
        super.onPause()
        productDetailMap.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        productDetailMap.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        productDetailMap.onLowMemory()
    }
}