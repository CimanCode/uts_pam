package com.example.uts_pam

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.uts_pam.databinding.ActivityUpdateBukuBinding

class UpdateBukuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateBukuBinding
    private lateinit var db: DatabaseHelper
    private var idBuku: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityUpdateBukuBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        db = DatabaseHelper(this)
        binding.closeIcon.setOnClickListener {
            val intent = Intent(this, ListBookActivity::class.java)
            startActivity(intent)
        }

        idBuku = intent.getIntExtra("idbuku", -1)

        if(idBuku != -1){
            tampilkanDataBuku(idBuku)
        }

    }

    private fun tampilkanDataBuku(id: Int) {
        val buku = db.getBukuById(id)
        buku.let {
            binding.editjudulbuku.setText(it.judulBuku)
            binding.editpenulisbuku.setText(it.penulisBuku)
            binding.editdeskripsibuku.setText(it.deskripsi)

            val bitmap =
                it.gambarBuku?.let { it1 -> BitmapFactory.decodeByteArray(it.gambarBuku, 0, it1.size) }
            binding.editimageView.setImageBitmap(bitmap)
        }
    }
}