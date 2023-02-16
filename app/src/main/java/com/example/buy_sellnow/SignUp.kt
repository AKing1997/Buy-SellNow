package com.example.buy_sellnow

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class SignUp : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val email: EditText = findViewById(R.id.signupEmailInput);
        val password: EditText = findViewById(R.id.signupPaaswordInput);
        val confirmPassword: EditText = findViewById(R.id.signupConfirmPasswordInput);
        val submitBtn: Button = findViewById(R.id.signupLoginBtn);
    }
}