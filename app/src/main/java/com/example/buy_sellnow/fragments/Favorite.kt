package com.example.buy_sellnow.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.fragment.app.Fragment
import com.example.buy_sellnow.Adapters.ProductGridViewAdapter
import com.example.buy_sellnow.Connexions.FireBaseConexion
import com.example.buy_sellnow.R
import com.google.firebase.auth.FirebaseAuth


class Favorite : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)
        val fav_grid_view: GridView = view.findViewById(R.id.fav_grid_view)
        val conexion: FireBaseConexion = FireBaseConexion()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        conexion.getAllFavoritesByUserId(userId){ids->
            if (ids != null) {
                conexion.getProductsByIds(ids) {
                    if (it != null) {
                        val productGridViewAdapter = ProductGridViewAdapter(it, view.context, false)
                        productGridViewAdapter.notifyDataSetChanged()
                        fav_grid_view.adapter = productGridViewAdapter
                    }
                }
            }
        }

        /* fav_grid_view.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
             // inside on click method we are simply displaying
             // a toast message with course name.
             Toast.makeText(
                 view.context, productLis[position].tituloDeProducto + " selected",
                 Toast.LENGTH_SHORT
             ).show()
         }*/
        return view
    }
}