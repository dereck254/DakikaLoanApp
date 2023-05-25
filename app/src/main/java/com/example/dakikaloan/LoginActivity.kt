package com.example.dakikaloan

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.view.Gravity
import android.widget.ToggleButton
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    lateinit var buttonLogin: Button
    lateinit var textViewRegister: TextView
    lateinit var textViewForgotPassword: TextView
    lateinit var editTextPhoneNumber: EditText
    lateinit var editTextPassword: EditText
    lateinit var textViewPhoneNumberError: TextView
    lateinit var textViewPasswordError: TextView
    lateinit var toggleButton: ToggleButton
    lateinit var auth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore

    lateinit var registeredPhoneNumber: String
    lateinit var registeredPassword: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        buttonLogin = findViewById(R.id.buttonLogin)
        textViewRegister = findViewById(R.id.textViewRegister)
        textViewForgotPassword = findViewById(R.id.textViewForgotPassword)
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber)
        editTextPassword = findViewById(R.id.editTextPassword)
        toggleButton = findViewById(R.id.showPasswordToggleButton)
        textViewPhoneNumberError = findViewById(R.id.textViewPhoneNumberError)
        textViewPasswordError = findViewById(R.id.textViewPasswordError)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        textViewRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        toggleButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                editTextPassword.transformationMethod = null
            } else {
                editTextPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        buttonLogin.setOnClickListener {
            val phoneNumber = editTextPhoneNumber.text.toString()
            val password = editTextPassword.text.toString()

            // Perform login validation using phone number and password
            if (isValidLogin(phoneNumber, password)) {
                auth.signInWithEmailAndPassword(phoneNumber, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putBoolean("isLoggedIn", true)
                            editor.apply()
                            // Successful login, navigate to the next activity
                            // Replace HomeActivity::class.java with your desired activity
                            val intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                            showToast("Login successful")
                            finish() // Optional: Finish current activity to prevent going back to the login screen
                        } else {
                            showToast("Invalid login credentials")
                        }
                    }
            } else {
                // Invalid login credentials, display error messages
                if (phoneNumber != registeredPhoneNumber) {
                    textViewPhoneNumberError.visibility = View.VISIBLE
                    textViewPhoneNumberError.text = "Invalid phone number"
                } else {
                    textViewPhoneNumberError.visibility = View.GONE
                }
                if (password != registeredPassword) {
                    textViewPasswordError.visibility = View.VISIBLE
                    textViewPasswordError.text = "Invalid password"
                    showForgotPasswordOption()
                } else {
                    textViewPasswordError.visibility = View.GONE
                    hideForgotPasswordOption()
                }
            }
        }

        textViewForgotPassword.setOnClickListener {
            // Handle the "Forgot Password" option click
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        fetchRegisteredUserData()
    }

    private fun fetchRegisteredUserData() {
        val userCollection = firestore.collection("users")
        val currentUser = auth.currentUser

        currentUser?.uid?.let { userId ->
            userCollection.document(userId)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        registeredPhoneNumber = documentSnapshot.getString("phoneNumber") ?: ""
                        registeredPassword = documentSnapshot.getString("password") ?: ""
                    }
                }
                .addOnFailureListener { exception ->
                    showToast("Failed to fetch user data from Firestore")
                }
        }
    }

    private fun isValidLogin(phoneNumber: String, password: String): Boolean {
        // Implement your login validation logic here
        // Compare the entered phone number and password with the registered values
        return phoneNumber == registeredPhoneNumber && password == registeredPassword
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).apply {
            setGravity(Gravity.CENTER, 0, 0)
            show()
        }
    }

    private fun showForgotPasswordOption() {
        textViewForgotPassword.visibility = View.VISIBLE
    }

    private fun hideForgotPasswordOption() {
        textViewForgotPassword.visibility = View.GONE
    }
}
