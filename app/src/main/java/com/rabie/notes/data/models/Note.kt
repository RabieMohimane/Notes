package com.rabie.notes.data.models

import android.os.Parcel
import android.os.Parcelable

data class Note(
    val userName: String="",
    val title: String="",
    var designation: String="",
    var price: Double=0.0,
    var description: String=""
):Parcelable{
    var id:String=""

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readString()
    ) {
        id = parcel.readString()
    }


    override fun toString(): String {
        return "Note(userName=$userName, title='$title', designation='$designation', price=$price, description='$description')"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userName)
        parcel.writeString(title)
        parcel.writeString(designation)
        parcel.writeDouble(price)
        parcel.writeString(description)
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Note> {
        override fun createFromParcel(parcel: Parcel): Note {
            return Note(parcel)
        }

        override fun newArray(size: Int): Array<Note?> {
            return arrayOfNulls(size)
        }
    }


}