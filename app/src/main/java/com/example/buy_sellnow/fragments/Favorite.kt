package com.example.buy_sellnow.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.VIEW_MODEL_STORE_OWNER_KEY
import com.example.buy_sellnow.Adapters.ProductGridViewAdapter
import com.example.buy_sellnow.Connexions.FireBaseConexion
import com.example.buy_sellnow.Model.Product
import com.example.buy_sellnow.R
import com.google.firebase.auth.FirebaseAuth


class Favorite : Fragment() {
    val conexion: FireBaseConexion = FireBaseConexion()
    val userId = FirebaseAuth.getInstance().currentUser!!.uid
    lateinit var fav_grid_view: GridView
    lateinit var nonFavProductMsg: TextView
    val productos: ArrayList<Product> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fav_grid_view = view.findViewById(R.id.fav_grid_view)
        nonFavProductMsg = view.findViewById(R.id.nonFavProductMsg)
    }

    override fun onResume() {
        super.onResume()
        val productGridViewAdapter = ProductGridViewAdapter(productos, requireContext(), false)
        conexion.getAllFavoritesByUserId(userId) { ids ->
            productos.clear()
            if (ids != null) {
                nonFavProductMsg.visibility = if(ids.isEmpty()) View.VISIBLE else View.GONE
                if(ids.isEmpty()){
                    productGridViewAdapter.notifyDataSetChanged()
                }
                conexion.getProductsByIds(ids) {
                    if (it != null) {
                        productos.addAll(it)
                        productGridViewAdapter.notifyDataSetChanged()
                    }
                }
                fav_grid_view.adapter = productGridViewAdapter
            }
        }
    }
}
