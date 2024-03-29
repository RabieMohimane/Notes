package com.rabie.notes.screens.home

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.text.Spannable
import android.text.method.LinkMovementMethod
import android.text.style.BackgroundColorSpan
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
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
import com.rabie.notes.data.models.SpannableNote
import com.rabie.notes.screens.usernotes.NotesOfUserActivity
import com.rabie.notes.screens.addnotespannable.AddNoteWithSpannabLe
import java.util.*
import java.util.regex.Pattern


class NotesAdapter : RecyclerView.Adapter<NotesAdapter.ItemViewHolder> {

    var notes: List<SpannableNote>

    var lastName: String = ""

    constructor(notes: List<SpannableNote>) : super() {
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

        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                // We display a Toast. You could do anything you want here.
                Toast.makeText(holder.itemView.context, "Clicked", Toast.LENGTH_SHORT).show()
               Log.e("clickable span","clickable span")
                val intent = Intent(holder.itemView.context, NotesOfUserActivity::class.java)
                intent.putExtra("name", note.name)
                holder.itemView.context.startActivity(intent)
            }
        }
        val span = Spannable.Factory.getInstance().newSpannable(note.note)
        val pattern = Pattern.compile("(?i)(?<!\\\\S)@[a-z]{1,}(?!\\\\S)")
        val matcher = pattern.matcher(span)
        while (matcher.find()) {
            val start = matcher.start()
            val end = matcher.end()

            span!!.setSpan(
                BackgroundColorSpan(getRandomColor()),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            span.setSpan(
                clickableSpan,
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            span.setSpan(
                StyleSpan(Typeface.BOLD),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

        }

        val pattern2 = Pattern.compile("(?i)(?<!\\\\S)//[a-z]{1,}(?!\\\\S)")
        val matcher2 = pattern2.matcher(span)
        while (matcher2.find()) {
            val start = matcher2.start()
            val end = matcher2.end()

            span!!.setSpan(
                ForegroundColorSpan(Color.GRAY),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )


        }
        val pattern3 = Pattern.compile("-?[0-9]{0,10}")
        val matcher3 = pattern3.matcher(span)
        var pri = 0.0
        while (matcher3.find()) {
            val start = matcher3.start()
            val end = matcher3.end()

            try {
                pri = span!!.substring(start, end).toDouble()
            } catch (e: NumberFormatException) {

            }
            if (pri < 0)
                span!!.setSpan(
                    ForegroundColorSpan(Color.RED),
                    start,
                    end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            else
                span!!.setSpan(
                    ForegroundColorSpan(Color.GREEN),
                    start,
                    end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
        }
        holder.tvNote.text = span
        holder.tvNote.setMovementMethod(LinkMovementMethod.getInstance())
        holder.tvNote.isClickable=true
        holder.ivEdite.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                Toast.makeText(p0!!.context, "edit clicked", Toast.LENGTH_SHORT).show()
                val intent = Intent(holder.itemView.context, AddNoteWithSpannabLe::class.java)
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
        holder.tvUserName.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {

            }

        })


    }


    class ItemViewHolder : RecyclerView.ViewHolder {
        var tvUserName: TextView
        var tvNote: TextView

        var ivEdite: ImageView
        var ivDelete: ImageView


        constructor(itemView: View) : super(itemView) {
            tvUserName = itemView.findViewById(R.id.tvName)
            tvNote = itemView.findViewById<TextView>(R.id.tvNote)
            ivEdite = itemView.findViewById<ImageView>(R.id.ivEdit)
            ivDelete = itemView.findViewById<ImageView>(R.id.ivDelete)
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
        return Color.argb(90, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }
}