package com.rabie.notes.data.models

data class Note(
    val userName: String,
    val title: String,
    var designation: String,
    var price: Double,
    var description: String
){
    override fun toString(): String {
        return "Note(userName=$userName, title='$title', designation='$designation', price=$price, description='$description')"
    }
}