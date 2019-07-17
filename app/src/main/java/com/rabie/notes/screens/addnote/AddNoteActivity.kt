package com.rabie.notes.screens.addnote

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import com.google.firebase.database.*
import com.rabie.notes.data.models.Note
import kotlinx.android.synthetic.main.activity_add_note.*


class AddNoteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.rabie.notes.R.layout.activity_add_note)
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("notes")
        btnSave.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {

                val note = Note(
                    tvName.text.toString(),
                    tvTitle.text.toString(),
                    tvNoteDesignation.text.toString(),
                    tvPrice.text.toString().toDouble(),
                    tvComment.text.toString()
                )


               myRef.push().setValue(note).addOnSuccessListener {
                  if(!this@AddNoteActivity.isDestroyed)
                   AlertDialog.Builder(this@AddNoteActivity)
                       .setTitle("New Note")
                       .setMessage("Note saved Successfully !")
                       .setPositiveButton("Ok", DialogInterface.OnClickListener { dialogInterface, i ->
                           tvName.text!!.clear()
                           tvComment.text!!.clear()
                           tvNoteDesignation.text!!.clear()
                           tvPrice.text!!.clear()
                           tvTitle.text!!.clear()
                           tvName.requestFocus()

                       }).create().show()

               }






            }

        })
        tvName.threshold = 2
        (tvName as AppCompatAutoCompleteTextView).addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
               if(p0!!.trim().length>0)
                if (p0!!.trim().contains("@")) {
                    val query = myRef.orderByChild("userName").startAt(p0.toString()).limitToFirst(4)
                    query.addValueEventListener(object :ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {

                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            var items = ArrayList<String>()
                            p0.children.forEach {
                                Log.e("autocomplete", it.toString())
                                  val value = it.getValue(Note::class.java)

                                  if (value != null) {
                                      Log.e("read value", "Value is: " + value!!.toString())
                                      items.add(value.userName)

                                  }

                            }
                            val adapter =
                                ArrayAdapter<String>(this@AddNoteActivity, android.R.layout.select_dialog_item, items)
                            tvName.setAdapter(adapter)

                        }

                    })

                }
            }

        })
    }
}
