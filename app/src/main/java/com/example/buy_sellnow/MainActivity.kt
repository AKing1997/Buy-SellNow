package com.example.buy_sellnow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.buy_sellnow.Activity.AddProduct
import com.example.buy_sellnow.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        defaultPage();
        // Creando una variable para los botones de menu navegacion
        val bottomNav: BottomNavigationView = findViewById(R.id.bottomNavigationView);
        val addForm: Button = findViewById(R.id.addProduct)

        addForm.setOnClickListener {
            val redrige: Intent = Intent(this, AddProduct::class.java);
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