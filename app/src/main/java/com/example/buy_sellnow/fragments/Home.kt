package com.example.buy_sellnow.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.buy_sellnow.Adapters.RecycleViewList
import com.example.buy_sellnow.Model.GridViewProduct
import com.example.buy_sellnow.Adapters.ProductGridViewAdapter
import com.example.buy_sellnow.Connexions.FireBaseConexion
import com.example.buy_sellnow.Model.Product
import com.example.buy_sellnow.R


class Home : Fragment() {
    var productLis: ArrayList<Product> = ArrayList();
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_home, container, false);
        var fav_grid_view_home: GridView = view.findViewById(R.id.fav_grid_view_home);
        var prom_list_view_home: RecyclerView = view.findViewById(R.id.prom_list_view_home);
        var conexion: FireBaseConexion =  FireBaseConexion();
        prom_list_view_home.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        conexion.getAllPublicacion { products ->
            productLis.clear();
            productLis.addAll(products)
            val productGridViewAdapter = ProductGridViewAdapter(productLis, view.context, false);
            productGridViewAdapter.notifyDataSetChanged();
            fav_grid_view_home.adapter = productGridViewAdapter;
        }


        return view;
    }
}