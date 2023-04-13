package com.example.buy_sellnow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
class SignUp : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    companion object{
        lateinit var email: EditText
        lateinit var password: EditText
        lateinit var confirmPassword: EditText
        lateinit var signupRegistrarseBtn: Button
        lateinit var redrige: Intent
        var emailBol: Boolean = false
        var passwordBol: Boolean = false
        var confirmPasswordBol: Boolean = false
        var passwordMatchBol: Boolean = false
        lateinit var signUpEmailErMsg: TextView
        lateinit var signUpPasswordErMsg: TextView
        lateinit var signUpConfirmPasswordErMsg: TextView
        lateinit var signUpPasswordNotMatchErMsg: TextView


    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        /** Error text view massage informer**/
        signUpEmailErMsg =  findViewById(R.id.signUpEmailErMsg)
        signUpPasswordErMsg =  findViewById(R.id.signUpPasswordErMsg)
        signUpConfirmPasswordErMsg =  findViewById(R.id.signUpConfirmPasswordErMsg)
        signUpPasswordNotMatchErMsg =  findViewById(R.id.signUpPasswordNotMatchErMsg)
        redrige = Intent(this, Login::class.java)

        auth = FirebaseAuth.getInstance()

        email = findViewById(R.id.signupEmailInput)
        password = findViewById(R.id.signupPaswordInput)
        confirmPassword = findViewById(R.id.signupConfirmPasswordInput)
        signupRegistrarseBtn = findViewById(R.id.signupRegistrarseBtn)
        val submitBtn: Button = findViewById(R.id.signupLoginBtn)

        signupRegistrarseBtn.setOnClickListener {
            if (validateForm()){
                showErMsg()
                signUp(email.text.toString(), password.text.toString())
            }else{
                showErMsg()
            }
        }

        submitBtn.setOnClickListener {
            startActivity(redrige)
        }
    }

    private fun validateForm(): Boolean{
        emailBol = email.text.isNotEmpty()
        passwordBol = password.text.isNotEmpty() && password.text.length>5
        confirmPasswordBol =  confirmPassword.text.isNotEmpty() && confirmPassword.text.length>5
        if(passwordBol && confirmPasswordBol) passwordMatchBol = password.text.toString().equals(
            confirmPassword.text.toString())

        return (emailBol && passwordBol && confirmPasswordBol && passwordMatchBol)
    }

    private fun showErMsg(){
        signUpEmailErMsg.visibility = if(emailBol) View.GONE else View.VISIBLE
        signUpPasswordErMsg.visibility = if(passwordBol) View.GONE else View.VISIBLE
        signUpConfirmPasswordErMsg.visibility = if(confirmPasswordBol) View.GONE else View.VISIBLE

        signUpPasswordNotMatchErMsg.visibility = if(passwordMatchBol) View.GONE else View.VISIBLE
    }

    private fun signUp(email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(redrige)
                    val user = auth.currentUser
                } else {
                    Toast.makeText(this, task.exception?.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
    }
}