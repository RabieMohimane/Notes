package com.rabie.notes.viewModels
import android.content.DialogInterface
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.common.util.ArrayUtils.contains
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.rabie.notes.data.models.SpannableNote

class NoteViewModel : ViewModel() {
    val database = FirebaseDatabase.getInstance()
    val myRef = database.getReference("notes")

    fun kepDBSynced(){
        myRef.keepSynced(true)
    }
    fun addNoteEventListener():MutableLiveData<List<SpannableNote>>{
        val data=MutableLiveData<List<SpannableNote>>()
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var items = ArrayList<SpannableNote>()
                dataSnapshot.children.forEach {
                    val value = it.getValue(SpannableNote::class.java)
                    if (value != null) {
                        Log.e("read value", "Value is: " + value!!.toString())
                        value.id = it.key.toString()
                        items.add(value)
                    }

                }
                data.postValue(items)

            }

            })
        return data
    }

    fun addNewNote(spannaBleNote:SpannableNote):MutableLiveData<Boolean>{
      val data= MutableLiveData<Boolean>()
        myRef.push().setValue(spannaBleNote).addOnSuccessListener {

             data.postValue(true)

        }
        return data
    }
    fun getNamesForAutoComplete(str:String):MutableLiveData<ArrayList<String>>{
        val data=MutableLiveData<ArrayList<String>>()
        val query = myRef.orderByChild("name").startAt(str).limitToFirst(4)
        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                var items = ArrayList<String>()
                p0.children.forEach {
                    Log.e("autocomplete", it.toString())
                    val value = it.getValue(SpannableNote::class.java)

                    if (value != null) {
                        Log.e("read value", "Value is: " + value!!.toString())
                        if(!items.contains(value.name))
                            items.add(value.name)

                    }

                }
                data.postValue(items)

    }
    })
        return data
    }
    fun getUserNotes(name:String):MutableLiveData<List<SpannableNote>>{
        val data=MutableLiveData<List<SpannableNote>>()
        val query = myRef.orderByChild("name").equalTo(name)
        query.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                var items = ArrayList<SpannableNote>()
                p0.children.forEach {
                    val value = it.getValue(SpannableNote::class.java)
                    if (value != null) {
                        Log.e("read value", "Value is: " + value!!.toString())
                        value.id=it.key.toString()
                        items.add(value)

                    }

                }
                data.postValue(items)
            }

        })
        return  data
    }
    fun updateNote(oldNote:SpannableNote):MutableLiveData<Boolean>{
        val data= MutableLiveData<Boolean>()
        myRef.child(oldNote.id).setValue(oldNote).addOnSuccessListener {

            data.postValue(true)

        }
        return data
    }
}