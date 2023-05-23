package com.example.dakikaloan


import android.content.Intent
import android.content.Context
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import android.view.View
import android.view.Gravity

class LoginActivity : AppCompatActivity() {

    lateinit var buttonLogin: Button
    lateinit var textViewRegister: TextView
    lateinit var editTextPhoneNumber: EditText
    lateinit var editTextPassword: EditText
    lateinit var textViewPhoneNumberError: TextView
    lateinit var textViewPasswordError: TextView
    lateinit var togglebutton: ToggleButton

    lateinit var registeredPhoneNumber: String
    lateinit var registeredPassword: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        buttonLogin = findViewById(R.id.buttonLogin)
        textViewRegister = findViewById(R.id.textViewRegister)
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber)
        editTextPassword = findViewById(R.id.editTextPassword)
        togglebutton= findViewById(R.id.showPasswordToggleButton)
        textViewPhoneNumberError = findViewById(R.id.textViewPhoneNumberError)
        textViewPasswordError = findViewById(R.id.textViewPasswordError)

        textViewRegister.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        togglebutton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                editTextPassword.transformationMethod = null
            } else {
                editTextPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }


        // Get the registered phone number and password from SharedPreferences or a database
        // Here, we'll use SharedPreferences for simplicity
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        registeredPhoneNumber = sharedPreferences.getString("phoneNumber", "") ?: ""
        registeredPassword = sharedPreferences.getString("password", "") ?: ""

        buttonLogin.setOnClickListener {
            val phoneNumber = editTextPhoneNumber.text.toString()
            val password = editTextPassword.text.toString()

            // Perform login validation using phone number and password
            if (isValidLogin(phoneNumber, password)) {
                // Successful login, navigate to the next activity
                // Replace NextActivity::class.java with your desired activity
                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                startActivity(intent)
                showToast("Login successful")
                finish() // Optional: Finish current activity to prevent going back to the login screen
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
                } else {
                    textViewPasswordError.visibility = View.GONE
                }
            }
        }

        textViewRegister.setOnClickListener {
            // Open the register activity
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
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
}

