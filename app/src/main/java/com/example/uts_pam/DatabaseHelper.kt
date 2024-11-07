package com.example.uts_pam

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DatabaseHelper (context: Context) : SQLiteOpenHelper (context,
    DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{
        private const val DATABASE_NAME = "perpustakan.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "user"
        private const val COLUMN_ID = "id"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PASSWORD = "password"
        private const val TABLE_BOOK = "books"
        private const val ID_BOOK = "id";
        private const val JUDUL_BOOK = "Judul"
        private const val PENULIS_BOOK = "Penulis"
        private const val GAMBAR_BOOK = "Gambar"
        private const val DESKRIPSI = "Deskripsi"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Query untuk tabel user
        val createUserTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_EMAIL TEXT NOT NULL,
                $COLUMN_PASSWORD TEXT NOT NULL
            )
        """.trimIndent()

        // Query untuk tabel books
        val createBooksTable = """
            CREATE TABLE $TABLE_BOOK (
                $ID_BOOK INTEGER PRIMARY KEY AUTOINCREMENT,
                $JUDUL_BOOK TEXT NOT NULL,
                $PENULIS_BOOK TEXT NOT NULL,
                $GAMBAR_BOOK TEXT,
                $DESKRIPSI TEXT
            )
        """.trimIndent()

        // Eksekusi query
        db?.execSQL(createUserTable)
        db?.execSQL(createBooksTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val QdropTable = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(QdropTable)
        onCreate(db)
    }

    @SuppressLint("Recycle")
    fun seedDatabase() {
        val db = writableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_EMAIL = ?", arrayOf("admin@gmail.com"))

        if(cursor.count == 0) {
            val values = ContentValues()
            values.put(COLUMN_EMAIL, "admin@gmail.com")
            values.put(COLUMN_PASSWORD, "admin123")
            db.insert(TABLE_NAME, null, values)
        }

        cursor.close()
    }


    fun login(email: String, password: String) : Boolean {
        val db = readableDatabase
        val selection = "$COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?"
        val selectionArgs = arrayOf(email, password)
        val cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null)

        val isLogin = cursor.count > 0
        cursor.close()
        db.close()
        return isLogin
    }

    fun insertBook(book: Buku): Boolean {
        val db = writableDatabase
        val dataBook = ContentValues().apply {
            put(JUDUL_BOOK, book.namaBuku)
            put(PENULIS_BOOK, book.penulisBuku)
            put(GAMBAR_BOOK, book.gambarBuku)
            put(DESKRIPSI, book.deskripsi)
        }

        val result = db.insert(TABLE_BOOK, null, dataBook)
        db.close()
        return result != -1L
    }


}