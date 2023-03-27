package com.example.buy_sellnow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.buy_sellnow.Activity.AddProduct
import com.example.buy_sellnow.fragments.Chat
import com.example.buy_sellnow.fragments.Favorite
import com.example.buy_sellnow.fragments.Home
import com.example.buy_sellnow.fragments.Profile
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        defaultPage();
        // Creando una variable para los botones de menu navegacion
        val bottomNav: BottomNavigationView = findViewById(R.id.bottomNavigationView);
        val addProduct: FloatingActionButton = findViewById(R.id.addProduct);
        addProduct.setOnClickListener {
            var redrige = Intent(this, AddProduct::class.java);
            startActivity(redrige);
        }
        bottomNav.setOnItemSelectedListener { item: MenuItem ->
            // condicione como Switch
            when (item.itemId) {
                R.id.home -> {// Frgmento Home
                    loadFragment(Home())
                    true
                }
                R.id.favorite -> {// fragmento List
                    loadFragment(Favorite())
                    true
                }
                R.id.vacio -> {// fragmento List
                    true
                }
                // Frgmento de perfil donde tiene boton eliminar todo
                R.id.profile -> {
                    loadFragment(Profile())
                    true
                }// Este fragmento es de Formulario
                R.id.chat -> {
                    loadFragment(Chat())
                    true
                }// La opcion por defecto
                else -> {false}
            }
        }
    }

    // Funcion fragmento por defecto
    private fun defaultPage(){
        loadFragment(Home());
    }
    // Funcion que recibe el fragmento y lo inicializa
    public fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container,fragment);
        transaction.addToBackStack(null);
        transaction.commit()
    }
}