package com.rabie.notes.screens.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.rabie.notes.R
import com.rabie.notes.data.models.Note
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Write a message to the database
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("notes")

        myRef.setValue("Hello, World!")
        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue(String::class.java)
                Log.e("read value", "Value is: " + value!!)
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.e("error", "Failed to read value.", error.toException())
            }
        })
        var items = ArrayList<Note>()
        val note1 = Note("Amine", "//Sortie 11 mai", "carburant", -120.0, "café in rest station mediouna")
        val note2 = Note("Yassir", "//Sortie 11 mai", "carburant", -120.0, "café in rest station mediouna")
        items.add(note1)
        items.add(note2)
        val noteItemAdapter = NotesAdapter(items)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = RecyclerView.VERTICAL
        recyclerView.layoutManager=layoutManager
        recyclerView.adapter = noteItemAdapter
    }
}
