package com.example.buy_sellnow.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.buy_sellnow.Adapters.RecycleViewList
import com.example.buy_sellnow.Login
import com.example.buy_sellnow.MainActivity
import com.example.buy_sellnow.Model.GridViewProduct
import com.example.buy_sellnow.R
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.OnCompleteListener


class Profile(): Fragment() {
    private lateinit var googleSignInClient : GoogleSignInClient;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_profile, container, false);
        var prom_list_view: RecyclerView = view.findViewById(R.id.prom_list_view);
        var productLis = ArrayList<GridViewProduct>();
        var logOutBtn: Button = view.findViewById(R.id.log_out);
        logOutBtn.setOnClickListener {
            signOut(view);
        }
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
            product.precio = product.precio + "€";
            if (product.productTitle.length > 13) {
                product.productTitle = product.productTitle.substring(0, 13);
            }
        }
        prom_list_view.layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false);
        val adapter : RecycleViewList = RecycleViewList(productLis, context);
        prom_list_view.adapter = adapter;
        return view;
    }

    private fun signOut(view: View) {
        googleSignInClient.signOut().addOnCompleteListener {
            startActivity(Intent(view.context, Login()::class.java));
        }
    }
}