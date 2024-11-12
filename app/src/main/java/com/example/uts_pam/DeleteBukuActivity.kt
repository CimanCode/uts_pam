package com.example.uts_pam

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DeleteBukuActivity : AppCompatActivity() {

    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delete_buku)

        db = DatabaseHelper(this)

        val bookId = intent.getIntExtra("idbuku", -1)
        if (bookId != -1) {
            db.deleteBukuById(bookId)  // Asumsi bahwa deleteBook adalah fungsi untuk menghapus buku dari database
            Toast.makeText(this, "Buku berhasil dihapus", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "ID buku tidak valid", Toast.LENGTH_SHORT).show()
        }

        finish()  // Kembali ke aktivitas sebelumnya setelah penghapusan
    }
}
