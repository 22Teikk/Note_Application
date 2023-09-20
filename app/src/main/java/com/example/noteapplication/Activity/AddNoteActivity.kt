package com.example.noteapplication.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.noteapplication.Model.Note
import com.example.noteapplication.ViewModel.NoteViewModel
import com.example.noteapplication.databinding.ActivityAddNoteBinding
import java.text.SimpleDateFormat
import java.util.Date

class AddNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var noteViewModel: NoteViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        val formatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm a")
        binding.textDateTime.text = formatter.format(Date())
        binding.imageBack.setOnClickListener {
            onBackPressed()
        }

        binding.imageSave.setOnClickListener {
            if (binding.inputNoteTitle.text.isEmpty())
                Toast.makeText(this, "Note title can't be empty!", Toast.LENGTH_LONG).show()
            else {
                val note: Note = Note(0,
                    binding.inputNoteTitle.text.toString(),
                    binding.textDateTime.text.toString(),
                    binding.inputNoteSubTitle.text.toString(),
                    binding.inputNote.text.toString())
                note.color
                noteViewModel.insertNote(note)
                finish()
            }

        }
    }

}