package com.example.noteapplication.Listener

import android.widget.LinearLayout
import com.example.noteapplication.Model.Note

interface NotesListener {
    fun onNoteClicked(note: Note)

    fun onNoteLongClicked(note: Note, layoutNote: LinearLayout)
}