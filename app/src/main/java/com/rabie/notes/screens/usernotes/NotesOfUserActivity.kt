package com.rabie.notes.screens.usernotes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rabie.notes.R
import com.rabie.notes.screens.home.NotesAdapter
import com.rabie.notes.viewModels.NoteViewModel
import kotlinx.android.synthetic.main.activity_notes_of_user.*

class NotesOfUserActivity : AppCompatActivity() {
    var total: Double = 0.0
    lateinit var mViewModel: NoteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes_of_user)
        mViewModel = ViewModelProviders.of(this).get(NoteViewModel::class.java)
        val name = intent.getStringExtra("name")
        mViewModel.getUserNotes(name).observe(this, Observer {
            total = 0.0
            it.forEach { it1 ->
                total += it1.price
            }
            tvTotal.text = total.toString()
            val noteItemAdapter = NotesAdapter(it)
            val layoutManager = LinearLayoutManager(this@NotesOfUserActivity)
            layoutManager.orientation = RecyclerView.VERTICAL
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = noteItemAdapter
        })

    }

    override fun onNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
