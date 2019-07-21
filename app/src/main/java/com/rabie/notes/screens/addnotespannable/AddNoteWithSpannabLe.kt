package com.rabie.notes.screens.addnotespannable

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.database.FirebaseDatabase
import com.rabie.notes.R
import com.rabie.notes.data.models.SpannableNote
import com.rabie.notes.viewModels.NoteViewModel
import kotlinx.android.synthetic.main.activity_add_note_with_spannab_le.*
import java.util.regex.Pattern


class AddNoteWithSpannabLe : AppCompatActivity() {
    var name = ""
    lateinit var mViewModel: NoteViewModel
    var price: Double = 0.0
    var noteToUpdate: SpannableNote? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note_with_spannab_le)
        mViewModel = ViewModelProviders.of(this).get(NoteViewModel::class.java)
        noteToUpdate = intent.getParcelableExtra<SpannableNote>("note")

        addNoteTextWatcher()
        if (noteToUpdate != null) {
            tvNote.setText(noteToUpdate!!.note)
        }
        btnSave.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                val spannableNote = SpannableNote(tvNote.text.toString(), name, price)
                if (!name.isEmpty()) {
                    if(noteToUpdate==null){
                        mViewModel.addNewNote(spannableNote).observe(this@AddNoteWithSpannabLe, Observer {
                            if (it) {

                            }
                        })
                    }else{
                        noteToUpdate!!.note=tvNote.text.toString()
                        mViewModel.updateNote(noteToUpdate!!).observe(this@AddNoteWithSpannabLe, Observer {
                            if (it) {
                                AlertDialog.Builder(this@AddNoteWithSpannabLe)
                                    .setTitle("Update Note")
                                    .setMessage("Note saved Successfully !")
                                    .setPositiveButton("Ok", DialogInterface.OnClickListener { dialogInterface, i ->
                                        finish()

                                    }).create().show()
                            }
                        })
                    }

                    AlertDialog.Builder(this@AddNoteWithSpannabLe)
                        .setTitle(" Note")
                        .setMessage("Note saved Successfully !")
                        .setPositiveButton("Ok", DialogInterface.OnClickListener { dialogInterface, i ->
                            finish()

                        }).create().show()
                } else {
                    tvNote.error = "you must tag a name !!"
                }

            }

        })
    }

    fun nameAutoComplete(str: String) {
        mViewModel.getNamesForAutoComplete(str).observe(this, Observer {

            val adapter =
                ArrayAdapter<String>(
                    this@AddNoteWithSpannabLe,
                    android.R.layout.select_dialog_item,
                    it
                )
            tvNote.setAdapter(adapter)
        })


    }

    fun addNoteTextWatcher() {
        tvNote.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

                val pattern = Pattern.compile("(?i)(?<!\\\\S)@[a-z]{1,}(?!\\\\S)")
                val matcher = pattern.matcher(p0)
                while (matcher.find()) {
                    val start = matcher.start()
                    val end = matcher.end()

                    p0!!.setSpan(
                        ForegroundColorSpan(resources.getColor(R.color.green)),
                        start,
                        end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    name = p0.substring(start, end)
                    noteToUpdate?.name=name
                    nameAutoComplete(name)


                }
                val pattern2 = Pattern.compile("(?i)(?<!\\\\S)//[a-z]{1,}(?!\\\\S)")
                val matcher2 = pattern2.matcher(p0)
                while (matcher2.find()) {
                    val start = matcher2.start()
                    val end = matcher2.end()

                    p0!!.setSpan(
                        ForegroundColorSpan(Color.GRAY),
                        start,
                        end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )


                }
                val pattern3 = Pattern.compile("-?[0-9]{0,10}")
                val matcher3 = pattern3.matcher(p0)
                while (matcher3.find()) {
                    val start = matcher3.start()
                    val end = matcher3.end()
                    try {
                        price = p0!!.substring(start, end).toDouble()
                    } catch (e: NumberFormatException) {

                    }
                    if (price < 0)
                        p0!!.setSpan(
                            ForegroundColorSpan(Color.RED),
                            start,
                            end,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    else
                        p0!!.setSpan(
                            ForegroundColorSpan(Color.GREEN),
                            start,
                            end,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )


                    Log.e("price", price.toString())

                }

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        })
    }
}
