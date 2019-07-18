package com.rabie.notes.screens.home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rabie.notes.R
import com.rabie.notes.data.models.Note
import com.rabie.notes.screens.addnote.AddNoteActivity
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.database.DatabaseReference




class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.rabie.notes.R.layout.activity_main)
        var total: Double = 0.0
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        setAppStatusListener()
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("notes")
        myRef.keepSynced(true)
        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                total=0.0
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.e("datasnapshot", dataSnapshot.toString())
                var items = ArrayList<Note>()
                dataSnapshot.children.forEach {
                    val value = it.getValue(Note::class.java)
                    if (value != null) {
                        Log.e("read value", "Value is: " + value!!.toString())
                       value.id=it.key.toString()
                        items.add(value)
                        total += value.price
                    }

                }
                tvTotal.text = total.toString()
                /* val note1 = Note("Amine", "//Sortie 11 mai", "carburant", -120.0, "café in rest station mediouna")
                 val note2 = Note("Yassir", "//Sortie 11 mai", "carburant", -120.0, "café in rest station mediouna")
                 items.add(note1)
                 items.add(note2)*/
                val noteItemAdapter = NotesAdapter(items)
                val layoutManager = LinearLayoutManager(this@MainActivity)
                layoutManager.orientation = RecyclerView.VERTICAL
                recyclerView.layoutManager = layoutManager
                recyclerView.adapter = noteItemAdapter


            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.e("error", "Failed to read value.", error.toException())
            }
        })

        btnAdd.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                startActivity(Intent(this@MainActivity, AddNoteActivity::class.java))
            }

        })
    }


     fun setAppStatusListener(){
         val connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected")
         connectedRef.addValueEventListener(object : ValueEventListener {
             override fun onDataChange(snapshot: DataSnapshot) {
                 val connected = snapshot.getValue(Boolean::class.java)!!
                 if (connected) {
                    toolbar.subtitle="you are connected"
                     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                         toolbar.setSubtitleTextColor(resources.getColor(R.color.green,null))
                     }else{
                         toolbar.setSubtitleTextColor(resources.getColor(R.color.green))
                     }
                 } else {
                     toolbar.subtitle="you are offline"
                     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                         toolbar.setSubtitleTextColor(resources.getColor(R.color.red,null))
                     }else{
                         toolbar.setSubtitleTextColor(resources.getColor(R.color.red))
                     }
                 }
             }

             override fun onCancelled(error: DatabaseError) {
                 System.err.println("Listener was cancelled")
             }
         })
     }
}
