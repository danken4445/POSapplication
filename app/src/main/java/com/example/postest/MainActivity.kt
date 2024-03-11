package com.example.postest

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var empIdEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var forgotPasswordTextView: TextView

    // Dummy employee ID and password for testing
    private val dummyEmployeeId = "12345"
    private val dummyPassword = "jetch"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI components
        empIdEditText = findViewById(R.id.empIdEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        forgotPasswordTextView = findViewById(R.id.forgotPasswordTextView)

        // Set click listener for login button
        loginButton.setOnClickListener {
            val enteredEmployeeId = empIdEditText.text.toString()
            val enteredPassword = passwordEditText.text.toString()

            // Check if entered employee ID and password match the dummy values
            if (enteredEmployeeId == dummyEmployeeId && enteredPassword == dummyPassword) {
                startActivity(Intent(this@MainActivity, ManagerActivity::class.java))
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                // Navigate to the next screen or perform desired actions
            } else {
                // Login failed
                Toast.makeText(this, "Invalid Employee ID or Password", Toast.LENGTH_SHORT).show()
            }
        }

        // Set click listener for forgot password text view
        forgotPasswordTextView.setOnClickListener {
            // Handle forgot password logic here
            Toast.makeText(this, "Forgot Password Clicked", Toast.LENGTH_SHORT).show()
        }
    }
}
