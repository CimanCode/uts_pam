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
import com.example.uts_pam.databinding.ActivityUpdateBukuBinding
import java.io.ByteArrayOutputStream

class UpdateBukuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateBukuBinding
    private lateinit var db: DatabaseHelper
    private var idBuku: Int = 0
    private var imageData : ByteArray? = null

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

        //event to pick image form galeri or file explorer
        binding.buttonSelectImage.setOnClickListener { openImagePicker() }
        //event to apply function update data buku
        binding.editbukubutton.setOnClickListener { updateDataToDatabase() }

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
                binding.editimageView.setImageBitmap(bitmap)
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

    private fun updateDataToDatabase() {
        val judul = binding.editjudulbuku.text.toString()
        val penulis = binding.editpenulisbuku.text.toString()
        val deskripsi = binding.editdeskripsibuku.text.toString()
        if(judul.isNotEmpty() && penulis.isNotEmpty() && deskripsi.isNotEmpty()){
            val id = db.updateDataBuku(idBuku, imageData, judul, penulis, deskripsi)
            finish()
            Toast.makeText(this, "Data Bukur Berhasil Diupdate : $id", Toast.LENGTH_SHORT).show()
            binding.editjudulbuku.text.clear()
            binding.editpenulisbuku.text.clear()
            binding.editdeskripsibuku.text.clear()
        }  else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }
}