package com.rabie.notes.screens.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rabie.notes.R
import com.rabie.notes.data.models.Note

class NotesAdapter : RecyclerView.Adapter<NotesAdapter.ItemViewHolder> {

    var notes: List<Note>


    constructor(notes: List<Note>) : super() {
        Log.e("NotesAdapter","NotesAdapter")
        this.notes = notes

    }


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(p0.getContext())
        val layout = layoutInflater.inflate(R.layout.recycler_item, p0, false) as ViewGroup
        return ItemViewHolder(layout)
    }

    override fun getItemCount(): Int {
        Log.e("NotesAdapter size","${notes.size}")
        return notes.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, p1: Int) {
        Log.e("onBindViewHolder", p1.toString())
        holder.tvUserName.text = "${holder.itemView.context.getText(R.string.at)}${notes.get(p1).userName} "
        var items = ArrayList<Note>()
        val note1 = Note("Ahmed", "//Sortie 11 mai", "carburant", -120.0, "café in rest station mediouna")
        items.add(note1)
        for (i in 1..8) {
            val note = Note("Ahmed", "", "cafe", 12.0, "café in rest station mediouna")
            items.add(note)
        }
        val noteItemAdapter = NoteItemAdapter(items)
        val layoutManager = LinearLayoutManager(holder.itemView.context)
        layoutManager.orientation = RecyclerView.VERTICAL
        holder.recyclerView.layoutManager=layoutManager
        holder.recyclerView.isNestedScrollingEnabled=false
        holder.recyclerView.adapter = noteItemAdapter

    }


    class ItemViewHolder : RecyclerView.ViewHolder {
        var tvUserName: TextView
        var recyclerView: RecyclerView


        constructor(itemView: View) : super(itemView) {
            tvUserName = itemView.findViewById(R.id.tvName)
            recyclerView = itemView.findViewById(R.id.recyclerView)
        }
    }
}