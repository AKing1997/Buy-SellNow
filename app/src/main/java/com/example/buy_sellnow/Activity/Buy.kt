package com.example.buy_sellnow.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridView
import com.example.buy_sellnow.Adapters.ProductGridViewAdapter
import com.example.buy_sellnow.Connexions.FireBaseConexion
import com.example.buy_sellnow.Model.Product
import com.example.buy_sellnow.R
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.auth.FirebaseAuth

class Buy : AppCompatActivity() {
    lateinit var buyToolbar: MaterialToolbar;
    var productLis: ArrayList<Product> = ArrayList();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy)

        buyToolbar = findViewById(R.id.buyToolbar)
        buyToolbar.setTitle("Compras")
        buyToolbar.setNavigationOnClickListener {
            onBackPressed();
        }

        var buy_grid_view_home: GridView = findViewById(R.id.buy_grid_view_home);
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        var conexion: FireBaseConexion =  FireBaseConexion();
        conexion.getAllProductsByUserId(userId) { products ->
            productLis.clear();
            productLis.addAll(products)
            val productGridViewAdapter = ProductGridViewAdapter(productLis, this, false);
            productGridViewAdapter.notifyDataSetChanged();
            buy_grid_view_home.adapter = productGridViewAdapter;
        }
    }
}