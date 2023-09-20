package com.example.noteapplication.Repository

import androidx.lifecycle.LiveData
import com.example.noteapplication.Dao.NoteDao
import com.example.noteapplication.Model.Note

class NoteRepository(private val noteDao: NoteDao) {
    fun getAllNote(): LiveData<List<Note>> = noteDao.getAllNote()

    suspend fun insertNote(note: Note) = noteDao.insertNote(note)

    suspend fun deleteNote(note: Note) = noteDao.deleteNote(note)

    suspend fun updateNote(note: Note) = noteDao.updateNote(note)
}