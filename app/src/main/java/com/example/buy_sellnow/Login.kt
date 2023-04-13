package com.example.buy_sellnow

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.buy_sellnow.Model.Enum.sessionPlatform
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.messaging.FirebaseMessaging

class Login : AppCompatActivity() {
    private lateinit var preferences: SharedPreferences
    private lateinit var editPerferences: SharedPreferences.Editor
    private lateinit var auth: FirebaseAuth
    private lateinit var redrige: Intent
    private lateinit var googleSignInClient: GoogleSignInClient

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                if (task.isSuccessful) {
                    val account: GoogleSignInAccount? = task.result
                    if (account != null) {
                        updateUI(account)
                    } else {
                        Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val googleBtn: Button = findViewById(R.id.loginGoogleBtn)
        val email: EditText = findViewById(R.id.loginEmailInput)
        val password: EditText = findViewById(R.id.loginPaaswordInput)
        val submitBtn: Button = findViewById(R.id.LoginEntrarBtn)
        val signUpRedirect: Button = findViewById(R.id.signUpRedirect)
        redrige = Intent(this, MainActivity::class.java)
        preferences = getSharedPreferences(getString(R.string.sesionPref), Context.MODE_PRIVATE)
        editPerferences = preferences.edit()
        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        googleBtn.setOnClickListener {
            singInGoogle()
        }
        submitBtn.setOnClickListener {
            logIn(email.text.toString(), password.text.toString())
        }
        signUpRedirect.setOnClickListener {
            redrige = Intent(this, SignUp::class.java)
            startActivity(redrige)
        }

    }

    private fun logIn(email: String, password: String) {
        if(email.isEmpty() && password.isEmpty()) return
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(redrige)
                    val user = auth.currentUser
                    getToken()
                    editPerferences?.putString("userId",user?.uid)
                    editPerferences.putString("email", user?.email)
                    editPerferences.putString("sessionPlatform", sessionPlatform.EMAIL.name)
                    editPerferences.apply()
                    Toast.makeText(this, user?.email, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, task.exception?.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }

    private fun getToken(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            editPerferences.putString("tokenNotify", token)
            editPerferences.apply()
        })
    }

    fun singInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private fun updateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = auth.currentUser
                getToken()
                editPerferences?.putString("email", user?.email)
                editPerferences?.putString("userId", user?.uid)
                editPerferences?.putString("sessionPlatform", sessionPlatform.GOOGLE.name)
                editPerferences?.apply()
                startActivity(redrige)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            updateUI(account)
        }
    }

}