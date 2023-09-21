package com.example.noteapplication.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteapplication.Adapter.NoteAdapter
import com.example.noteapplication.Database.NoteDatabase
import com.example.noteapplication.Model.Note
import com.example.noteapplication.ViewModel.NoteViewModel
import com.example.noteapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: NoteViewModel
    private lateinit var database: NoteDatabase
    private lateinit var binding: ActivityMainBinding
    lateinit var noteAdapter: NoteAdapter

    val arl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val note = result.data?.getSerializableExtra("note") as Note
            note?.let {
                viewModel.insertNote(note)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application))[NoteViewModel::class.java]
        viewModel.readAllData.observe(this) { list ->
            list?.let {
                noteAdapter.updateList(list)
            }
        }
        database = NoteDatabase.getDatabase(this)
        initUI()

        binding.addNoteMain.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            arl.launch(intent)
        }
    }

    private fun initUI() {
        noteAdapter = NoteAdapter()
        binding.noteRecyclerView.setHasFixedSize(true)
        binding.noteRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.noteRecyclerView.adapter = noteAdapter
    }
}