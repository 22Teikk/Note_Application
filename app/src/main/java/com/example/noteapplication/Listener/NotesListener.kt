package com.example.noteapplication.Listener

import com.example.noteapplication.Model.Note

interface NotesListener {
    fun onNoteClicked(note: Note)
}