package com.example.uts_pam

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.uts_pam.databinding.ActivityUpdateBukuBinding
import java.io.ByteArrayOutputStream

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

        binding.tambahbukubutton.setOnClickListener {
            updateBuku()
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

    private fun updateBuku() {
        val judul = binding.editjudulbuku.text.toString()
        val penulis = binding.editpenulisbuku.text.toString()
        val deskripsi = binding.editdeskripsibuku.text.toString()

        val imageView = binding.editimageView
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = imageView.drawingCache
        val stream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val image = stream.toByteArray()

        val result = db.updateDataBuku(idBuku, image, judul, penulis, deskripsi)

        if (result > 0) {
            Toast.makeText(this, "Data buku berhasil diperbarui", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, ListBookActivity::class.java))
            finish()
        } else {
            Toast.makeText(this, "Gagal memperbarui data buku", Toast.LENGTH_SHORT).show()
        }
    }

}