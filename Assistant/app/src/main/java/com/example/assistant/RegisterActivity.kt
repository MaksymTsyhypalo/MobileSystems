package com.example.assistant

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth


class RegisterActivity : AppCompatActivity() {

    private lateinit var emailTextView: EditText
    private lateinit var passwordTextView: EditText
    private lateinit var button: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_page)

        // creating FirebaseAuth instance
        auth = FirebaseAuth.getInstance()

        emailTextView = findViewById(R.id.email_edittext)
        passwordTextView = findViewById(R.id.password_edittext)
        button = findViewById(R.id.register_button)

        button.setOnClickListener {
            registerNewUser()
        }
    }

    private fun registerNewUser() {
        // Take the value of two edit texts in Strings
        val email = emailTextView.text.toString()
        val password = passwordTextView.text.toString()

        // Validations for input email and password
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "please enter credentials", Toast.LENGTH_LONG).show()
        } else {
            // create new user or register new user
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Registration successful!", Toast.LENGTH_LONG).show()
                    // if the user created intent to login activity
                    startActivity(Intent(this, MenuActivity::class.java))
                } else {
                    // Registration failed
                    Toast.makeText(this, "Registration failed!!" + " Please try again later", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}