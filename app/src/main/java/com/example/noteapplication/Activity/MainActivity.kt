package com.example.noteapplication.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteapplication.Adapter.NoteAdapter
import com.example.noteapplication.Database.NoteDatabase
import com.example.noteapplication.Listener.NotesListener
import com.example.noteapplication.Model.Note
import com.example.noteapplication.R
import com.example.noteapplication.ViewModel.NoteViewModel
import com.example.noteapplication.databinding.ActivityMainBinding
import java.util.Locale

class MainActivity : AppCompatActivity(), NotesListener {
    lateinit var viewModel: NoteViewModel
    private lateinit var database: NoteDatabase
    private lateinit var binding: ActivityMainBinding
    lateinit var noteAdapter: NoteAdapter
    private lateinit var noteList: ArrayList<Note>

    val arl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val note = result.data?.getSerializableExtra("note") as Note
            note?.let {
                viewModel.insertNote(note)
            }
        }
    }

    val arlUpdate = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val note = result.data?.getSerializableExtra("note") as Note
            note?.let {
                viewModel.updateNote(note)
                noteAdapter.notifyDataSetChanged()
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
                noteList = it as ArrayList<Note>
                noteAdapter.updateList(list)
            }
        }
        database = NoteDatabase.getDatabase(this)
        initUI()

        binding.addNoteMain.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            arl.launch(intent)
        }

        binding.inputSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })
    }

    private fun filterList(newText: String?) {
        if (newText != null) {
            val filterList = ArrayList<Note>()
            for(i in noteList) {
                if(i.title.lowercase(Locale.ROOT).contains(newText)) {
                    filterList.add(i)
                }
            }
            if (filterList.isEmpty()) {
                Toast.makeText(this, "No data found", Toast.LENGTH_LONG).show()
                noteAdapter.updateList(emptyList())
            }
            else
                noteAdapter.updateList(filterList)
        }
    }

    private fun initUI() {
        noteAdapter = NoteAdapter(this)
        binding.noteRecyclerView.setHasFixedSize(true)
        binding.noteRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.noteRecyclerView.adapter = noteAdapter
    }

    override fun onNoteClicked(note: Note) {
        val intent = Intent(this, AddNoteActivity::class.java).apply {
            this.putExtra("isViewOrUpdate", true)
            this.putExtra("note", note)
        }
        arlUpdate.launch(intent)
    }

    override fun onNoteLongClicked(note: Note, layoutNote: LinearLayout) {
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.delete_note, null)
        val textDel = dialogView.findViewById<TextView>(R.id.textDelete)
        val textCancel = dialogView.findViewById<TextView>(R.id.textCancelDel)
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        val alertDialog = builder.create()
        textDel.setOnClickListener {
            viewModel.deleteNote(note)
            noteAdapter.notifyDataSetChanged()
            alertDialog.dismiss()
        }
        textCancel.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
    }
}