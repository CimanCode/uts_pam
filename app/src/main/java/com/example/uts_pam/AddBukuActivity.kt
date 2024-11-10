package com.example.uts_pam

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.uts_pam.databinding.ActivityAddBukuBinding
import java.io.ByteArrayOutputStream

class AddBukuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBukuBinding
    private lateinit var db: DatabaseHelper
    private var imageData : ByteArray? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddBukuBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        db = DatabaseHelper(this)
        binding.closeIcon.setOnClickListener {
            val intent = Intent(this, ListBookActivity::class.java)
            startActivity(intent)
        }
        binding.buttonSelectImage.setOnClickListener { openImagePicker() }
        binding.tambahbukubutton.setOnClickListener { saveDataToDatabase() }
    }

    // Image picker launcher
    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val uri = data?.data
            uri?.let {
                val inputStream = contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                binding.imageView.setImageBitmap(bitmap)
                imageData = bitmapToByteArray(bitmap)
            } ?: Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        imagePickerLauncher.launch(intent)
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    private fun saveDataToDatabase() {
        val judul = binding.judulbuku.text.toString()
        val penulis = binding.penulisbuku.text.toString()
        val deskripsi = binding.deskripsibuku.text.toString()
        if(judul.isNotEmpty() && penulis.isNotEmpty() && deskripsi.isNotEmpty()){
            val id = db.insertDataBuku(imageData, judul, penulis, deskripsi)
            finish()
            Toast.makeText(this, "Data Bukur Berhasil Disimpan : $id", Toast.LENGTH_SHORT).show()
            binding.judulbuku.text.clear()
            binding.penulisbuku.text.clear()
            binding.deskripsibuku.text.clear()
        }  else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }

}