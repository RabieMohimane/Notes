package com.rabie.notes.screens

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rabie.notes.R
import com.rabie.notes.data.models.Note
import com.rabie.notes.data.models.SpannableNote
import com.rabie.notes.screens.home.NotesAdapter
import kotlinx.android.synthetic.main.activity_notes_of_user.*

class NotesOfUserActivity : AppCompatActivity() {
    var total: Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes_of_user)
        val name=intent.getStringExtra("name")
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("notes")
        val query = myRef.orderByChild("userName").equalTo(name)
        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                total=0.0
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.e("datasnapshot", p0.toString())
                var items = ArrayList<SpannableNote>()
                p0.children.forEach {
                    val value = it.getValue(SpannableNote::class.java)
                    if (value != null) {
                        Log.e("read value", "Value is: " + value!!.toString())
                        value.id=it.key.toString()
                        items.add(value)
                     //   total += value.price
                    }

                }
                tvTotal.text = total.toString()

                val noteItemAdapter = NotesAdapter(items)
                val layoutManager = LinearLayoutManager(this@NotesOfUserActivity)
                layoutManager.orientation = RecyclerView.VERTICAL
                recyclerView.layoutManager = layoutManager
                recyclerView.adapter = noteItemAdapter

            }

        })
    }

    override fun onNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
