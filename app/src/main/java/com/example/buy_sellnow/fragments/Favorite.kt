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
import com.example.buy_sellnow.R


class Favorite : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_favorite, container, false);
        var fav_grid_view: GridView = view.findViewById(R.id.fav_grid_view);
        var productLis = ArrayList<GridViewProduct>();
        val product1 = GridViewProduct(
            "P001",
            "Reloj de pulsera para mujer",
            "https://example.com/images/product1.jpg",
            true,
            "39.99"
        )

        val product2 = GridViewProduct(
            "P002",
            "Zapatillas deportivas para hombre",
            "https://example.com/images/product2.jpg",
            false,
            "69.99"
        )

        val product3 = GridViewProduct(
            "P003",
            "Laptop de alta gama",
            "https://example.com/images/product3.jpg",
            true,
            "1299.99"
        )

        val product4 = GridViewProduct(
            "P004",
            "Set de cuchillos de cocina",
            "https://example.com/images/product4.jpg",
            true,
            "79.99"
        )

        val product5 = GridViewProduct(
            "P005",
            "Libro de cocina vegetariana",
            "https://example.com/images/product5.jpg",
            false,
            "29.99"
        )

        val product6 = GridViewProduct(
            "P006",
            "Mochila de senderismo",
            "https://example.com/images/product6.jpg",
            true,
            "89.99"
        )
        productLis.add(product1);
        productLis.add(product2);
        productLis.add(product3);
        productLis.add(product4);
        productLis.add(product5);
        productLis.add(product6);
        for (product in productLis) {
            product.precio = product.precio + "???";
            if (product.productTitle.length > 30) {
                product.productTitle = product.productTitle.substring(0, 30);
            }
        }
        val productGridViewAdapter = ProductGridViewAdapter(productLis, view.context);
        fav_grid_view.adapter = productGridViewAdapter;
        fav_grid_view.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            // inside on click method we are simply displaying
            // a toast message with course name.
            Toast.makeText(
                view.context, productLis[position].productTitle + " selected",
                Toast.LENGTH_SHORT
            ).show()
        }
        return view;
    }
}