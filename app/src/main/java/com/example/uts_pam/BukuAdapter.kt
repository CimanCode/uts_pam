package com.example.uts_pam

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.AlertDialog

class BukuAdapter (private var buku: List<Buku>, context: Context) :
RecyclerView.Adapter<BukuAdapter.BukuViewHolder> ()
{
    private val db: DatabaseHelper = DatabaseHelper(context)
    class BukuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val moreoptions: ImageView = itemView.findViewById(R.id.moreoptions)
        val judulTextView : TextView = itemView.findViewById(R.id.judulTextView)
        val deskripsiTextView : TextView = itemView.findViewById(R.id.deskripsiTextView)
        val penulisTextView : TextView = itemView.findViewById(R.id.penulisTextView)
        val bukuImageView : ImageView = itemView.findViewById(R.id.imageViewBook)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BukuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_data_buku, parent, false)
        return  BukuViewHolder(view)
    }

    override fun onBindViewHolder(holder: BukuViewHolder, position: Int) {
        val book = buku[position]
        holder.judulTextView.text = book.judulBuku
        holder.penulisTextView.text = book.penulisBuku
        holder.deskripsiTextView.text = book.deskripsi

        book.gambarBuku?.let {
            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
            holder.bukuImageView.setImageBitmap(bitmap)
        }

        holder.moreoptions.setOnClickListener { view ->
            val popup = PopupMenu(view.context, view, 0, 0, R.style.CustomPopUp)
            popup.inflate(R.menu.menu_more_options)

            // Tampilkan ikon pada PopupMenu (opsional)
            try {
                val fields = popup.javaClass.declaredFields
                for (field in fields) {
                    if ("mPopup" == field.name) {
                        field.isAccessible = true
                        val menuPopupHelper = field.get(popup)
                        val classPopupHelper = Class.forName(menuPopupHelper.javaClass.name)
                        val setForceIcons = classPopupHelper.getMethod("setForceShowIcon", Boolean::class.java)
                        setForceIcons.invoke(menuPopupHelper, true)
                        break
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_edit -> {
                        val intent = Intent(holder.itemView.context, UpdateBukuActivity::class.java).apply {
                            putExtra("idbuku", book.id)
                        }
                        holder.itemView.context.startActivity(intent)
                        Toast.makeText(view.context, "Edit ${book.judulBuku}", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.action_delete -> {
                        // Tampilkan dialog konfirmasi sebelum menghapus
                        AlertDialog.Builder(view.context)
                            .setMessage("Apakah anda yakin ingin menghapus buku ini?")
                            .setPositiveButton("Ya") { dialog, _ ->
                                // Jika dikonfirmasi, lanjutkan ke aktivitas penghapusan
                                val intent = Intent(holder.itemView.context, DeleteBukuActivity::class.java).apply {
                                    putExtra("idbuku", book.id)
                                }
                                holder.itemView.context.startActivity(intent)
                                Toast.makeText(view.context, "Delete ${book.judulBuku}", Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            }
                            .setNegativeButton("Tidak") { dialog, _ ->
                                dialog.dismiss()  // Batalkan dialog
                            }
                            .show()
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }

    }

    override fun getItemCount(): Int = buku.size

    @SuppressLint("NotifyDataSetChanged")
    fun refreshData(newbuku: List<Buku>){
        buku = newbuku
        notifyDataSetChanged()
    }

}