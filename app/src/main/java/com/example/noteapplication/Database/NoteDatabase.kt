package com.example.noteapplication.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.noteapplication.Dao.NoteDao
import com.example.noteapplication.Model.Note
import com.example.noteapplication.Utilities.DATABASE_NAME

@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteDatabase: RoomDatabase() {
    abstract fun getNoteDao(): NoteDao
    companion object {
        @Volatile
        private var INSTANCE: NoteDatabase ?= null
        fun getDatabase(context: Context): NoteDatabase {
            val temp = INSTANCE
            if (temp != null)
                return temp
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}