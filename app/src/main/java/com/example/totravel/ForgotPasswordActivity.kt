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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var resetPasswordButton: Button
    private lateinit var backToLoginTextView: TextView
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        // Initialize the database
        db = DatabaseBuilder.getInstance(this)

        emailEditText = findViewById(R.id.editTextEmail)
        resetPasswordButton = findViewById(R.id.buttonResetPassword)
        backToLoginTextView = findViewById(R.id.textViewBackToLogin)

        resetPasswordButton.setOnClickListener {
            val email = emailEditText.text.toString()

            if (email.isNotEmpty()) {
                lifecycleScope.launch {
                    val userExists = checkUserExists(email)
                    withContext(Dispatchers.Main) {
                        if (userExists) {
                            // Add logic to send a reset password email or instructions
                            Toast.makeText(this@ForgotPasswordActivity, "Password reset instructions sent!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@ForgotPasswordActivity, "Email not found", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            }
        }

        backToLoginTextView.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private suspend fun checkUserExists(email: String): Boolean {
        return withContext(Dispatchers.IO) {
            db.userDao().getUserByUsername(email) != null
        }
    }
}
