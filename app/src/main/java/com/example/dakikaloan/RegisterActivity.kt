package com.example.dakikaloan

import android.content.Intent
import android.content.Context
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import android.view.View

class RegisterActivity : AppCompatActivity() {

    lateinit var buttonRegister: Button
    lateinit var editTextFullName: EditText
    lateinit var editTextPhoneNumber: EditText
    lateinit var editTextIDNumber: EditText
    lateinit var editTextPassword: EditText
    lateinit var confirmpwd: EditText
    lateinit var togglebutton: ToggleButton
    lateinit var textlogin: TextView
    lateinit var textViewErrorMessage: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        buttonRegister = findViewById(R.id.buttonRegister)
        editTextFullName = findViewById(R.id.editTextFullName)
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber)
        editTextIDNumber = findViewById(R.id.editTextIDNumber)
        editTextPassword = findViewById(R.id.editTextPassword)
        confirmpwd = findViewById(R.id.ConfirmPassword)
        textlogin = findViewById(R.id.text_Login)
        textViewErrorMessage = findViewById(R.id.textViewErrorMessage)
        togglebutton = findViewById(R.id.showPasswordToggleButton)


        togglebutton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                editTextPassword.transformationMethod = null
                confirmpwd.transformationMethod = null
            } else {
                editTextPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                confirmpwd.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        textlogin.setOnClickListener {
            val reg = Intent(this, LoginActivity::class.java)
            startActivity(reg)
        }

        buttonRegister.setOnClickListener {
            val fullnames = editTextFullName.text.toString()
            val phoneNumber = editTextPhoneNumber.text.toString()
            val idNumber = editTextIDNumber.text.toString()
            val password = editTextPassword.text.toString()
            val confirmPassword = confirmpwd.text.toString()

            if (fullnames.isEmpty() || phoneNumber.isEmpty() || idNumber.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show()
            } else if (password != confirmPassword) {
                Toast.makeText(this, "Password mismatch", Toast.LENGTH_SHORT).show()
                textViewErrorMessage.visibility = View.VISIBLE
                textViewErrorMessage.text = "Password mismatch"
            } else if (!isPasswordValid(password)) {
                Toast.makeText(
                    this,
                    "Password must have at least 6 characters, one uppercase letter, one lowercase letter, one digit, and one special character",
                    Toast.LENGTH_SHORT
                ).show()
                textViewErrorMessage.visibility = View.VISIBLE
                textViewErrorMessage.text = "Invalid password"
            } else if (!isPhoneNumberValid(phoneNumber)) {
                Toast.makeText(this, "Invalid phone number", Toast.LENGTH_SHORT).show()
                textViewErrorMessage.visibility = View.VISIBLE
                textViewErrorMessage.text = "Invalid phone number"
            } else {
                // Check if the phone number or ID number is already registered
                if (isPhoneNumberRegistered(phoneNumber)) {
                    Toast.makeText(this, "Phone number already registered", Toast.LENGTH_SHORT).show()
                    textViewErrorMessage.visibility = View.VISIBLE
                    textViewErrorMessage.text = "Phone number already registered"
                } else if (isIDNumberRegistered(idNumber)) {
                    Toast.makeText(this, "ID number already registered", Toast.LENGTH_SHORT).show()
                    textViewErrorMessage.visibility = View.VISIBLE
                    textViewErrorMessage.text = "ID number already registered"
                } else {
                    // Store the registered phone number, ID number, and password in SharedPreferences or a database
                    // Here, we'll use SharedPreferences for simplicity
                    val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("phoneNumber", phoneNumber)
                    editor.putString("idNumber", idNumber)
                    editor.putString("password", password)
                    editor.apply()

                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish() // Optional: Finish current activity to prevent going back to the registration screen
                }
            }
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        val passwordRegex =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,}$".toRegex()
        return password.matches(passwordRegex)
    }
    Number: String): Boolean {
        // Retrieve the registered phone number
    private fun isPhoneNumberValid(phoneNumber: String): Boolean {
        val phoneNumberRegex = "^0\\d{9}$".toRegex()
        return phoneNumber.matches(phoneNumberRegex)
    }

    private fun isPhoneNumberRegistered(phones from SharedPreferences or a database
        // Here, we'll use SharedPreferences for simplicity
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val registeredPhoneNumber = sharedPreferences.getString("phoneNumber", null)

        return registeredPhoneNumber == phoneNumber
    }

    private fun isIDNumberRegistered(idNumber: String): Boolean {
        // Retrieve the registered ID numbers from SharedPreferences or a database
        // Here, we'll use SharedPreferences for simplicity
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val registeredIDNumber = sharedPreferences.getString("idNumber", null)

        return registeredIDNumber == idNumber
    }
}



