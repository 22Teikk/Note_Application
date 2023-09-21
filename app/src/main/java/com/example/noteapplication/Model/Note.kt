package com.example.noteapplication.Model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "note_table")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var title: String,
    val dateTime: String,
    val subTitle: String,
    val noteText: String,
): Serializable {
    var imagePath: String = ""
    var color: String = ""
    var webLink: String = ""
}
