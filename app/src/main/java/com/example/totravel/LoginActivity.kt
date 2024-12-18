package com.example.totravel

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.totravel.database.AppDatabase
import com.example.totravel.database.DatabaseBuilder
import com.example.totravel.database.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signInButton: Button
    private lateinit var createAccountTextView: TextView
    private lateinit var forgotPasswordTextView: TextView
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize the database
        db = DatabaseBuilder.getInstance(this)

        emailEditText = findViewById(R.id.editTextEmail)
        passwordEditText = findViewById(R.id.editTextPassword)
        signInButton = findViewById(R.id.buttonSignIn)
        createAccountTextView = findViewById(R.id.textViewCreateAccount)
        forgotPasswordTextView = findViewById(R.id.textViewForgotPassword)

        signInButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            lifecycleScope.launch {
                val user = login(email, password)
                withContext(Dispatchers.Main) {
                    if (user != null) {
                        // Login successful, navigate to MainActivity
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Login failed. Please check your credentials.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        createAccountTextView.setOnClickListener {
            startActivity(Intent(this, CreateAccountActivity::class.java))
        }

        forgotPasswordTextView.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }
    }

    private suspend fun login(username: String, password: String): User? {
        return withContext(Dispatchers.IO) {
            db.userDao().login(username, password)
        }
    }
}
