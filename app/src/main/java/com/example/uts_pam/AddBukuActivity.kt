package com.example.uts_pam

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddBukuActivity : AppCompatActivity() {
    private val CAMERA_REQUEST_CODE = 1
    private lateinit var currentPhotoPath: String

    private lateinit var gambar_buku: ImageView
    private lateinit var db: DatabaseHelper

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_buku)

        val judul_buku: EditText = findViewById(R.id.judul_buku)
        val penulis_buku: EditText = findViewById(R.id.penulis_buku)
        gambar_buku = findViewById(R.id.gambar_buku)
        val btn_pilih_buku: Button = findViewById(R.id.btn_pilih_buku)
        val deskripsi_buku: EditText = findViewById(R.id.deskripsi_buku)
        val btn_submit: Button = findViewById(R.id.btn_submit)

        db = DatabaseHelper(this)

        btn_pilih_buku.setOnClickListener {
            // Buka kamera
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (cameraIntent.resolveActivity(packageManager) != null) {
                val photoFile = createImageFile()
                val photoURI = FileProvider.getUriForFile(
                    this,
                    "${applicationContext.packageName}.provider",
                    photoFile
                )
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
            }
        }

        btn_submit.setOnClickListener {
            val judul = judul_buku.text.toString().trim()
            val penulis = penulis_buku.text.toString().trim()
            val gambar = currentPhotoPath // Path dari gambar yang diambil
            val deskripsi = deskripsi_buku.text.toString().trim()

            if (judul.isEmpty() || penulis.isEmpty() || deskripsi.isEmpty() || gambar.isEmpty()) {
                Toast.makeText(this, "Semua field wajib diisi!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val addBuku = Buku(judul, penulis, gambar, deskripsi)
            val isInserted = db.insertBook(addBuku)

            if (isInserted) {
                Toast.makeText(this, "Buku berhasil disimpan", Toast.LENGTH_SHORT).show()
                judul_buku.text.clear()
                penulis_buku.text.clear()
                deskripsi_buku.text.clear()
                gambar_buku.setImageResource(0) // Hapus gambar di ImageView
            } else {
                Toast.makeText(this, "Gagal menyimpan buku", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createImageFile(): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timestamp}_", ".jpg", storageDir).apply {
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            val bitmap = BitmapFactory.decodeFile(currentPhotoPath)
            gambar_buku.setImageBitmap(bitmap) // Tampilkan gambar di ImageView
        }
    }
}
