package com.example.noteapplication.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.noteapplication.Database.NoteDatabase
import com.example.noteapplication.Model.Note
import com.example.noteapplication.Repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModel(application: Application): AndroidViewModel(application) {
    var readAllData: LiveData<List<Note>>
    private var noteRepository: NoteRepository
    init {
        val noteDao = NoteDatabase.getDatabase(application).getNoteDao()
        noteRepository = NoteRepository(noteDao)
        readAllData = noteRepository.getAllNote()
    }

    fun insertNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.insertNote(note)
        }
    }
    fun updateNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.updateNote(note)
        }
    }
    fun deleteNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.deleteNote(note)
        }
    }
}