package com.example.dakikaloan

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    lateinit var buttonRegister: Button
    lateinit var editTextFullName: EditText
    lateinit var editTextPhoneNumber: EditText
    lateinit var editTextIDNumber: EditText
    lateinit var editTextPassword: EditText
    lateinit var confirmpwd: EditText
    lateinit var toggleButton: ToggleButton
    lateinit var textLogin: TextView

    lateinit var auth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        buttonRegister = findViewById(R.id.buttonRegister)
        editTextFullName = findViewById(R.id.editTextFullName)
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber)
        editTextIDNumber = findViewById(R.id.editTextIDNumber)
        editTextPassword = findViewById(R.id.editTextPassword)
        confirmpwd = findViewById(R.id.ConfirmPassword)
        toggleButton = findViewById(R.id.showPasswordToggleButton)
        textLogin = findViewById(R.id.text_Login)

        toggleButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                editTextPassword.transformationMethod = null
                confirmpwd.transformationMethod = null
            } else {
                editTextPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                confirmpwd.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        textLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        buttonRegister.setOnClickListener {
            val fullName = editTextFullName.text.toString()
            val phoneNumber = editTextPhoneNumber.text.toString()
            val idNumber = editTextIDNumber.text.toString()
            val password = editTextPassword.text.toString()
            val confirmPassword = confirmpwd.text.toString()

            if (fullName.isEmpty() || phoneNumber.isEmpty() || idNumber.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
            } else if (password != confirmPassword) {
                Toast.makeText(this, "Password mismatch", Toast.LENGTH_SHORT).show()
            } else if (!isPasswordValid(password)) {
                Toast.makeText(this, "Invalid password", Toast.LENGTH_SHORT).show()
            } else if (!isPhoneNumberValid(phoneNumber)) {
                Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT).show()
            } else if (!isIDNumberValid(idNumber)) {
                Toast.makeText(this, "Invalid ID number", Toast.LENGTH_SHORT).show()
            } else {
                // Register user using Firebase Authentication
                auth.createUserWithEmailAndPassword(phoneNumber, password)
                    .addOnCompleteListener { registrationTask ->
                        if (registrationTask.isSuccessful) {
                            val user = auth.currentUser
                            val userId = user?.uid

                            // Check if the phone number or ID number is already registered
                            if (userId != null) {
                                firestore.collection("users").whereEqualTo("phoneNumber", phoneNumber)
                                    .get()
                                    .addOnSuccessListener { result ->
                                        if (result.isEmpty) {
                                            // Store additional user details in Firestore
                                            val userDocument = firestore.collection("users").document(userId)
                                            val userData = hashMapOf(
                                                "fullName" to fullName,
                                                "phoneNumber" to phoneNumber,
                                                "idNumber" to idNumber
                                            )

                                            userDocument.set(userData)
                                                .addOnSuccessListener {
                                                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                                                    val intent = Intent(this, LoginActivity::class.java)
                                                    startActivity(intent)
                                                    finish()
                                                }
                                                .addOnFailureListener { e ->
                                                    Toast.makeText(this, "Failed to register: ${e.message}", Toast.LENGTH_SHORT).show()
                                                }
                                        } else {
                                            Toast.makeText(this, "Phone number already registered", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(this, "Failed to register: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        } else {
                            Toast.makeText(this, "Registration failed: ${registrationTask.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        val passwordRegex =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$".toRegex()
        return password.matches(passwordRegex)
    }

    private fun isPhoneNumberValid(phoneNumber: String): Boolean {
        val phoneNumberRegex = "^0\\d{9}$".toRegex()
        return phoneNumber.matches(phoneNumberRegex)
    }

    private fun isIDNumberValid(idNumber: String): Boolean {
        return idNumber.length == 8
    }
}
