package com.example.buy_sellnow

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class Login : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val email: EditText = findViewById(R.id.loginEmailInput);
        val password: EditText = findViewById(R.id.loginPaaswordInput);
        val submitBtn: Button = findViewById(R.id.LoginEntrarBtn);



    }
}