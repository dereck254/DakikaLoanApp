package com.example.dakikaloan

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    lateinit var textView_Welcome: TextView
    lateinit var textView_Login: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView_Welcome = findViewById(R.id.textViewWelcome)
        textView_Login = findViewById(R.id.textViewLogin)

        textView_Welcome.apply {
            text = "Welcome"
        }

        textView_Login.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
