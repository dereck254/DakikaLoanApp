package com.example.dakikaloan

import android.content.Context
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import android.widget.ToggleButton

class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var editTextPhoneNumber: EditText
    lateinit var editTextIDNumber: EditText
    lateinit var editTextNewPassword: EditText
    lateinit var editTextConfirmNewPassword: EditText
    lateinit var buttonResetPassword: Button
    lateinit var buttonLoginNow: Button
    lateinit var textViewPhoneNumberError: TextView
    lateinit var textViewIDNumberError: TextView
    lateinit var textViewNewPasswordError: TextView
    lateinit var textViewConfirmNewPasswordError: TextView
    lateinit var toggleButton: ToggleButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber)
        editTextIDNumber = findViewById(R.id.editTextIDNumber)
        editTextNewPassword = findViewById(R.id.editTextNewPassword)
        editTextConfirmNewPassword = findViewById(R.id.editTextConfirmNewPassword)
        buttonResetPassword = findViewById(R.id.buttonResetPassword)
        buttonLoginNow = findViewById(R.id.buttonLoginNow)
        textViewPhoneNumberError = findViewById(R.id.textViewPhoneNumberError)
        textViewIDNumberError = findViewById(R.id.textViewIDNumberError)
        textViewNewPasswordError = findViewById(R.id.textViewNewPasswordError)
        textViewConfirmNewPasswordError = findViewById(R.id.textViewConfirmNewPasswordError)
        toggleButton = findViewById(R.id.showNewPasswordToggleButton)

        toggleButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                editTextNewPassword.transformationMethod = null
                editTextConfirmNewPassword.transformationMethod = null
            } else {
                editTextNewPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                editTextConfirmNewPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        buttonResetPassword.setOnClickListener {
            val phoneNumber = editTextPhoneNumber.text.toString()
            val idNumber = editTextIDNumber.text.toString()
            val newPassword = editTextNewPassword.text.toString()
            val confirmNewPassword = editTextConfirmNewPassword.text.toString()

            // Clear error messages
            clearErrorMessages()

            // Perform validation
            var hasError = false

            // Validate phone number
            if (!isValidPhoneNumber(phoneNumber)) {
                textViewPhoneNumberError.visibility = View.VISIBLE
                textViewPhoneNumberError.text = "Invalid phone number"
                hasError = true
            }

            // Validate ID number
            if (!isValidIDNumber(idNumber)) {
                textViewIDNumberError.visibility = View.VISIBLE
                textViewIDNumberError.text = "ID number not registered."
                hasError = true
            }

            // Validate new password
            if (!isValidPassword(newPassword)) {
                textViewNewPasswordError.visibility = View.VISIBLE
                textViewNewPasswordError.text = "Password should have Uppercase, lowercase, special character and a numeric"
                hasError = true
            }

            // Validate confirm new password
            if (!isValidConfirmPassword(newPassword, confirmNewPassword)) {
                textViewConfirmNewPasswordError.visibility = View.VISIBLE
                textViewConfirmNewPasswordError.text = "Passwords do not match"
                hasError = true
            }

            if (hasError) {
                return@setOnClickListener
            }

            // Check if the phone number and ID number match the registered values
            if (!isPhoneNumberAndIDNumberMatch(phoneNumber, idNumber)) {
                Toast.makeText(this, "Incorrect phone number or ID number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Password reset successful
            Toast.makeText(this, "Password reset successful", Toast.LENGTH_SHORT).show()

            // Show the Login Now button
            buttonLoginNow.visibility = View.VISIBLE
        }

        buttonLoginNow.setOnClickListener {
            // Open the login activity
            finish() // Close the Forgot Password activity
        }
    }

    private fun clearErrorMessages() {
        textViewPhoneNumberError.visibility = View.GONE
        textViewIDNumberError.visibility = View.GONE
        textViewNewPasswordError.visibility = View.GONE
        textViewConfirmNewPasswordError.visibility = View.GONE
    }

    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        // Implement your phone number validation logic here
        return phoneNumber.length == 10 && phoneNumber.startsWith("0")
    }

    private fun isValidIDNumber(idNumber: String): Boolean {
        // Implement your ID number validation logic here
        // For example, check the length or format of the ID number
        return idNumber.length == 8
    }

    private fun isValidPassword(password: String): Boolean {
        // Implement your password validation logic here
        // For example, check the password length or required characters
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+\$).{6,}\$"
        return password.matches(passwordPattern.toRegex())
    }

    private fun isValidConfirmPassword(password: String, confirmPassword: String): Boolean {
        // Implement your confirm password validation logic here
        return password == confirmPassword
    }

    private fun isPhoneNumberAndIDNumberMatch(phoneNumber: String, idNumber: String): Boolean {
        // Retrieve the registered phone number and ID number from SharedPreferences or a database
        // Here, we'll use SharedPreferences for simplicity
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val registeredPhoneNumber = sharedPreferences.getString("phoneNumber", null)
        val registeredIDNumber = sharedPreferences.getString("idNumber", null)

        return phoneNumber == registeredPhoneNumber && idNumber == registeredIDNumber
    }
}
