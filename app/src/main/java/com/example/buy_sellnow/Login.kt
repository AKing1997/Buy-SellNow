package com.example.buy_sellnow

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn.*
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth;
    private lateinit var redrige: Intent;
    private lateinit var googleSignInClient : GoogleSignInClient;
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = getSignedInAccountFromIntent(result.data);
            if (task.isSuccessful) {
                val account: GoogleSignInAccount? = task.result
                if (account != null) {
                    updateUI(account)
                } else {
                    Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
        @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        val googleBtn: Button = findViewById(R.id.loginGoogleBtn);
        val email: EditText = findViewById(R.id.loginEmailInput);
        val password: EditText = findViewById(R.id.loginPaaswordInput);
        val submitBtn: Button = findViewById(R.id.LoginEntrarBtn);
        val signUpRedirect: Button = findViewById(R.id.signUpRedirect);
        auth = FirebaseAuth.getInstance();
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build();
        googleSignInClient = getClient(this,gso);
            googleBtn.setOnClickListener {
                singInGoogle();
            }
        submitBtn.setOnClickListener {
            redrige = Intent(this, MainActivity::class.java);
            startActivity(redrige);
        }
        signUpRedirect.setOnClickListener {
            redrige = Intent(this, SignUp::class.java);
            startActivity(redrige);
        }

    }

    fun singInGoogle(){
        val signInIntent = googleSignInClient.signInIntent;
        launcher.launch(signInIntent);
    }

    private fun updateUI(account: GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener{
            if(it.isSuccessful) {
                redrige = Intent(this, MainActivity::class.java);

                startActivity(redrige);
            }else{
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    override fun onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        val account = getLastSignedInAccount(this);
        if(account!=null) {
            updateUI(account);
        }
    }

}