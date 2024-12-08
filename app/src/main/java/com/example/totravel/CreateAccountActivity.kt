package com.example.totravel

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.totravel.database.AppDatabase
import com.example.totravel.database.DatabaseBuilder
import com.example.totravel.database.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateAccountActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var createAccountButton: Button
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        // Initialize the database
        db = DatabaseBuilder.getInstance(this)

        emailEditText = findViewById(R.id.editTextEmail)
        passwordEditText = findViewById(R.id.editTextPassword)
        createAccountButton = findViewById(R.id.buttonCreateAccount)

        createAccountButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                lifecycleScope.launch {
                    try {
                        val userExists = checkUserExists(email)
                        withContext(Dispatchers.Main) {
                            if (userExists) {
                                Toast.makeText(this@CreateAccountActivity, "Email already registered", Toast.LENGTH_SHORT).show()
                            } else {
                                val user = User(username = email, password = password)
                                insertUser(user)
                                Toast.makeText(this@CreateAccountActivity, "Account Created!", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("CreateAccountActivity", "Error creating account", e)
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@CreateAccountActivity, "Error creating account", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun checkUserExists(email: String): Boolean {
        return withContext(Dispatchers.IO) {
            db.userDao().getUserByUsername(email) != null
        }
    }

    private suspend fun insertUser(user: User) {
        withContext(Dispatchers.IO) {
            db.userDao().insert(user)
        }
    }
}
