package com.example.uts_pam

import java.sql.Blob

data class Buku(
    val id: Int,
    val judulBuku: String,
    val penulisBuku: String,
    val gambarBuku: Blob,
    val deskripsi: String
)
