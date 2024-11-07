package com.example.uts_pam

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.uts_pam.databinding.ActivityListBookBinding

class ListBookActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListBookBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityListBookBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.buttonadd.setOnClickListener {
            val next = Intent(this, AddBukuActivity::class.java)
            startActivity(next)
        }
    }
}