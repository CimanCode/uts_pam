package com.example.uts_pam

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uts_pam.databinding.ActivityListBookBinding

class ListBookActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListBookBinding
    private lateinit var db: DatabaseHelper
    private lateinit var bukuAdapter: BukuAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityListBookBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        binding.buttonadd.setOnClickListener {
            val next = Intent(this, AddBukuActivity::class.java)
            startActivity(next)
        }

        db = DatabaseHelper(this)
        bukuAdapter = BukuAdapter(db.getAllBuku(), this)
        binding.bukuRecycleView.layoutManager = LinearLayoutManager(this)
        binding.bukuRecycleView.adapter = bukuAdapter
    }

    override fun onResume() {
        super.onResume()
        bukuAdapter.refreshData(db.getAllBuku())
    }

}