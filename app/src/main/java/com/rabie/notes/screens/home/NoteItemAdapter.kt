package com.rabie.notes.screens.home

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.rabie.notes.R
import com.rabie.notes.data.models.Note

class NoteItemAdapter : RecyclerView.Adapter<NoteItemAdapter.ItemViewHolder> {

    var notes: List<Note>


    constructor(notes: List<Note>) : super() {
        this.notes = notes

    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(p0.getContext())
        val layout = layoutInflater.inflate(R.layout.note_item, p0, false) as ViewGroup
        return ItemViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, p1: Int) {
        Log.e("onBindViewHolder", p1.toString())
        val note = notes.get(p1)
        if (!note.title.isEmpty()) {
            holder.tvTitle.text = note.title
        } else {
            holder.tvTitle.visibility = View.GONE
        }
        holder.tvDesignation.text = note.designation
        setPriceText(holder.tvPrice, note.price)
        holder.tvDescription.text=note.description
        holder.ivEdite.setOnClickListener(object :View.OnClickListener{
            override fun onClick(p0: View?) {
                Toast.makeText(p0!!.context,"edit clicked",Toast.LENGTH_SHORT).show()
            }

        })

        holder.ivDelete.setOnClickListener(object :View.OnClickListener{
            override fun onClick(p0: View?) {
                Toast.makeText(p0!!.context,"delete clicked",Toast.LENGTH_SHORT).show()
            }

        })


    }

    fun setPriceText(tvPrice: TextView, price: Double) {
        tvPrice.text = price.toString()
        if (price >= 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                tvPrice.setTextColor(tvPrice.context.resources.getColor(R.color.green, null))
            else
                tvPrice.setTextColor(tvPrice.context.resources.getColor(R.color.green))

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                tvPrice.setTextColor(tvPrice.context.resources.getColor(R.color.red, null))
            else
                tvPrice.setTextColor(tvPrice.context.resources.getColor(R.color.red))
        }
    }

    class ItemViewHolder : RecyclerView.ViewHolder {
        var tvTitle: TextView
        var tvDesignation: TextView
        var tvDescription: TextView
        var tvPrice: TextView
        var ivEdite: ImageView
        var ivDelete: ImageView

        constructor(itemView: View) : super(itemView) {
            tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
            tvDesignation = itemView.findViewById<TextView>(R.id.tvDesignation)
            tvDescription = itemView.findViewById<TextView>(R.id.tvDescription)
            tvPrice = itemView.findViewById<TextView>(R.id.tvPrice)
            ivEdite = itemView.findViewById<ImageView>(R.id.ivEdit)
            ivDelete = itemView.findViewById<ImageView>(R.id.ivDelete)
        }
    }
}