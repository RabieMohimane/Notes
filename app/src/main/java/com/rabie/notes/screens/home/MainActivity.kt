package com.rabie.notes.screens.home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rabie.notes.R
import com.rabie.notes.data.models.SpannableNote
import com.rabie.notes.screens.addnotespannable.AddNoteWithSpannabLe
import com.rabie.notes.viewModels.NoteViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    lateinit var mViewModel: NoteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.rabie.notes.R.layout.activity_main)
        var total: Double = 0.0
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        setAppStatusListener()
        mViewModel = ViewModelProviders.of(this).get(NoteViewModel::class.java)
        mViewModel.kepDBSynced()
        mViewModel.addNoteEventListener().observe(this, Observer {
            total = 0.0
            it.forEach{it1->
                total += it1.price
            }
            tvTotal.text = total.toString()
            val noteItemAdapter = NotesAdapter(it)
            val layoutManager = LinearLayoutManager(this@MainActivity)
            layoutManager.orientation = RecyclerView.VERTICAL
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = noteItemAdapter

        })


        btnAdd.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                startActivity(Intent(this@MainActivity, AddNoteWithSpannabLe::class.java))
            }

        })
    }


    fun setAppStatusListener() {
        val connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected")
        connectedRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val connected = snapshot.getValue(Boolean::class.java)!!
                if (connected) {
                    toolbar.subtitle = "you are connected"
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        toolbar.setSubtitleTextColor(resources.getColor(R.color.green, null))
                    } else {
                        toolbar.setSubtitleTextColor(resources.getColor(R.color.green))
                    }
                } else {
                    toolbar.subtitle = "you are offline"
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        toolbar.setSubtitleTextColor(resources.getColor(R.color.red, null))
                    } else {
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
