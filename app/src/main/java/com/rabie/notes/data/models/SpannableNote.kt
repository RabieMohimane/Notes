package com.rabie.notes.data.models

import android.os.Parcel
import android.os.Parcelable

data class SpannableNote(val note:String="",val name:String="",var price: Double=0.0) :Parcelable{
    var id:String=""

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble()
    ) {
        id = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(note)
        parcel.writeString(name)
        parcel.writeDouble(price)
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "SpannableNote(note='$note', name='$name', price=$price, id='$id')"
    }

    companion object CREATOR : Parcelable.Creator<SpannableNote> {
        override fun createFromParcel(parcel: Parcel): SpannableNote {
            return SpannableNote(parcel)
        }

        override fun newArray(size: Int): Array<SpannableNote?> {
            return arrayOfNulls(size)
        }
    }


}