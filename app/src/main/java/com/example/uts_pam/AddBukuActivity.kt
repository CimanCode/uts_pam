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
import android.content.pm.PackageManager
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddBukuActivity : AppCompatActivity() {
    private val CAMERA_REQUEST_CODE = 1
    private lateinit var currentPhotoPath: String

    private lateinit var gambar_buku: ImageView
    private lateinit var db: DatabaseHelper

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
            checkCameraPermission()
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (cameraIntent.resolveActivity(packageManager) != null) {
                val photoFile = createImageFile()
                if (photoFile != null) {
                    val photoURI = FileProvider.getUriForFile(
                        this,
                        "${applicationContext.packageName}.provider",
                        photoFile
                    )
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
                } else {
                    Toast.makeText(this, "Gagal membuat file gambar", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btn_submit.setOnClickListener {
            // Logika menyimpan data
        }
    }

    private fun checkCameraPermission() {
        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_REQUEST_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Izin kamera diberikan", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Izin kamera ditolak", Toast.LENGTH_SHORT).show()
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

