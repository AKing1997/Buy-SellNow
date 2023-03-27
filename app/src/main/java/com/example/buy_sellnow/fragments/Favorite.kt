package com.example.buy_sellnow.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.buy_sellnow.Model.GridViewProduct
import com.example.buy_sellnow.Adapters.ProductGridViewAdapter
import com.example.buy_sellnow.Model.Product
import com.example.buy_sellnow.R


class Favorite : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_favorite, container, false);
        var fav_grid_view: GridView = view.findViewById(R.id.fav_grid_view);
        var productLis = ArrayList<Product>();

        val productGridViewAdapter = ProductGridViewAdapter(productLis, view.context);
        fav_grid_view.adapter = productGridViewAdapter;
        fav_grid_view.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            // inside on click method we are simply displaying
            // a toast message with course name.
            Toast.makeText(
                view.context, productLis[position].tituloDeProducto + " selected",
                Toast.LENGTH_SHORT
            ).show()
        }
        return view;
    }
}