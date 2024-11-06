package com.example.uts_pam

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.uts_pam.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var db: DatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        db = DatabaseHelper(this)

        if(isFirstRun()){
            db.seedDatabase()
            Toast.makeText(this, "Admin Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
        }

        binding.btnlogin.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            perFormLogin(email, password)
        }
    }

    private fun perFormLogin(email: String, password: String) {
        val isSuccess = db.login(email, password)

        if(isSuccess){
            Toast.makeText(this, "Login Berhasil", Toast.LENGTH_SHORT).show()
            val next = Intent(this, ListBookActivity::class.java)
            startActivity(next)
            finish()
        } else {
            Toast.makeText(this, "Email atau Password Salah", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isFirstRun() : Boolean {
        val preferences: SharedPreferences = getSharedPreferences("Apppreferences", MODE_PRIVATE)
        val isFirstRun = preferences.getBoolean("isFirstRun", true)
        if(isFirstRun){
            preferences.edit().putBoolean("isFirstRun", false).apply()
        }
        return isFirstRun
    }
}