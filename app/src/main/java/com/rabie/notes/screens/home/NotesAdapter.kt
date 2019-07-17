package com.rabie.notes.screens.home

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.rabie.notes.R
import com.rabie.notes.data.models.Note
import com.rabie.notes.screens.addnote.AddNoteActivity
import android.graphics.Color
import java.util.*


class NotesAdapter : RecyclerView.Adapter<NotesAdapter.ItemViewHolder> {

    var notes: List<Note>

    var lastName: String = ""

    constructor(notes: List<Note>) : super() {
        Log.e("NotesAdapter", "NotesAdapter")
        this.notes = notes

    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(p0.getContext())
        val layout = layoutInflater.inflate(com.rabie.notes.R.layout.recycler_item, p0, false) as ViewGroup
        return ItemViewHolder(layout)
    }

    override fun getItemCount(): Int {
        Log.e("NotesAdapter size", "${notes.size}")
        return notes.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, p1: Int) {
        Log.e("onBindViewHolder", p1.toString())
        val note = notes.get(p1)
        holder.tvUserName.text = note.userName
        if(note.userName.equals(lastName)){
            holder.tvUserName.visibility=View.GONE
        }else{
            holder.tvUserName.visibility=View.VISIBLE
        }
        holder.tvUserName.setTextColor(getRandomColor())
        lastName=note.userName
        if (!note.title.isEmpty()) {
            holder.tvTitle.text = note.title
        } else {
            holder.tvTitle.visibility = View.GONE
        }
        holder.tvDesignation.text = note.designation
        setPriceText(holder.tvPrice, note.price)
        holder.tvDescription.text = note.description
        holder.ivEdite.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                Toast.makeText(p0!!.context, "edit clicked", Toast.LENGTH_SHORT).show()
                val intent = Intent(holder.itemView.context, AddNoteActivity::class.java)
                intent.putExtra("note", note)
                holder.itemView.context.startActivity(intent)
            }

        })

        holder.ivDelete.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                AlertDialog.Builder(holder.itemView.context)
                    .setTitle("Notes")
                    .setMessage("Are you sure , you want to delete this note?")
                    .setPositiveButton("Delete", DialogInterface.OnClickListener { dialogInterface, i ->
                        val database = FirebaseDatabase.getInstance()
                        val myRef = database.getReference("notes")
                        myRef.child(note.id).removeValue()
                        Toast.makeText(p0!!.context, "delete clicked", Toast.LENGTH_SHORT).show()
                    })
                    .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, i ->
                        dialogInterface.dismiss()
                    })
                    .create().show()

            }

        })


    }


    class ItemViewHolder : RecyclerView.ViewHolder {
        var tvUserName: TextView
        var tvTitle: TextView
        var tvDesignation: TextView
        var tvDescription: TextView
        var tvPrice: TextView
        var ivEdite: ImageView
        var ivDelete: ImageView


        constructor(itemView: View) : super(itemView) {
            tvUserName = itemView.findViewById(com.rabie.notes.R.id.tvName)
            tvTitle = itemView.findViewById<TextView>(com.rabie.notes.R.id.tvTitle)
            tvDesignation = itemView.findViewById<TextView>(com.rabie.notes.R.id.tvDesignation)
            tvDescription = itemView.findViewById<TextView>(com.rabie.notes.R.id.tvDescription)
            tvPrice = itemView.findViewById<TextView>(com.rabie.notes.R.id.tvPrice)
            ivEdite = itemView.findViewById<ImageView>(com.rabie.notes.R.id.ivEdit)
            ivDelete = itemView.findViewById<ImageView>(com.rabie.notes.R.id.ivDelete)
        }
    }

    fun setPriceText(tvPrice: TextView, price: Double) {
        tvPrice.text = " ${price.toString()} dh "
        if (price >= 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                tvPrice.setTextColor(tvPrice.context.resources.getColor(com.rabie.notes.R.color.green, null))
            else
                tvPrice.setTextColor(tvPrice.context.resources.getColor(com.rabie.notes.R.color.green))

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                tvPrice.setTextColor(tvPrice.context.resources.getColor(com.rabie.notes.R.color.red, null))
            else
                tvPrice.setTextColor(tvPrice.context.resources.getColor(com.rabie.notes.R.color.red))
        }
    }
    fun getRandomColor(): Int {
        val rnd = Random()
        return Color.argb(240, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }
}