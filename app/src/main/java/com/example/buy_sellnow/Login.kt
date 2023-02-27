package com.example.buy_sellnow

import android.annotation.SuppressLint
import android.content.Intent
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
        val signUpRedirect: Button = findViewById(R.id.signUpRedirect);
        submitBtn.setOnClickListener {
            val redrige = Intent(this, MainActivity::class.java);
            startActivity(redrige);
        }
        signUpRedirect.setOnClickListener {
            val redrigee = Intent(this, SignUp::class.java);
            startActivity(redrigee);
        }

    }

}