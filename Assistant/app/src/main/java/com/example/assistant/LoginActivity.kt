package com.example.assistant

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {
    private lateinit var emailTextView: EditText
    private lateinit var passwordTextView: EditText
    private lateinit var button: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        // creating instance of FirebaseAuth
        auth = FirebaseAuth.getInstance()

        emailTextView = findViewById(R.id.email_edittext)
        passwordTextView = findViewById(R.id.password_edittext)
        button = findViewById(R.id.login_button)

        button.setOnClickListener {
            loginUserAccount()
        }
    }

    private fun loginUserAccount() {
        val email = emailTextView.text.toString()
        val password = passwordTextView.text.toString()

        // Validations for input email and password
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Enter credentials", Toast.LENGTH_LONG).show()
        } else {
            // login existing user
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // login successful
                    Toast.makeText(this, "Login successful!", Toast.LENGTH_LONG).show()
                    startActivity(Intent(this, MenuActivity::class.java))
                } else {
                    // login failed
                    Toast.makeText(this, "Login failed!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}