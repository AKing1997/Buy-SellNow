package com.example.buy_sellnow.Connexions

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.buy_sellnow.MainActivity
import com.example.buy_sellnow.R
import com.example.buy_sellnow.SignUp
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class GoogleAuth: AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var redrige: Intent;
    private lateinit var googleSignInClient : GoogleSignInClient;
    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data);
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

    fun SignGoogle(){
        auth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this,gso);
        singInGoogle();
    }

    private fun singInGoogle(){
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private fun updateUI(account: GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener{
            if(it.isSuccessful) {
                redrige = Intent(this, MainActivity::class.java);

                startActivity(redrige);
            }else{
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if(account!=null) {
            updateUI(account);
        }
    }
}