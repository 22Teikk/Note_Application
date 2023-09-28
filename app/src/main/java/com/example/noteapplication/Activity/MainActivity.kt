package com.example.noteapplication.Activity

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.noteapplication.Adapter.NoteAdapter
import com.example.noteapplication.Database.NoteDatabase
import com.example.noteapplication.Listener.NotesListener
import com.example.noteapplication.Model.Note
import com.example.noteapplication.R
import com.example.noteapplication.Utilities.Converters
import com.example.noteapplication.ViewModel.NoteViewModel
import com.example.noteapplication.databinding.ActivityMainBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.util.Locale

class MainActivity : AppCompatActivity(), NotesListener {
    lateinit var viewModel: NoteViewModel
    private lateinit var database: NoteDatabase
    private lateinit var binding: ActivityMainBinding
    lateinit var noteAdapter: NoteAdapter
    private lateinit var noteList: ArrayList<Note>

    val arlImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val imageView = result.data?.data as Uri
                if (imageView != null) {
                    try {
                        var inputStream = contentResolver.openInputStream(imageView)
                        var bitmap = BitmapFactory.decodeStream(inputStream)
                        val intent = Intent(this, AddNoteActivity::class.java).apply {
                            this.putExtra("fromQuickAction", true)
                            this.putExtra("type", "image")
                            this.putExtra("image", Converters.bitmapToString(bitmap))
                        }
                        arl.launch(intent)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }

    val arl =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val note = result.data?.getSerializableExtra("note") as Note
                note?.let {
                    viewModel.insertNote(note)
                }
            }
        }

    val arlUpdate =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[NoteViewModel::class.java]
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

        binding.imageAddImage.setOnClickListener {
            val permission: Array<String> =
                arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, permission, 123)
            } else selectImage()
        }

        binding.inputSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })

        binding.imageAddWebLink.setOnClickListener {
            showDialogURL()
        }
    }

    private fun showDialogURL() {
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.add_url, null)
        val inputURL = dialogView.findViewById<EditText>(R.id.inputURL)
        val textAdd = dialogView.findViewById<TextView>(R.id.textAdd)
        val textCancel = dialogView.findViewById<TextView>(R.id.textCancel)
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        val alertDialog = builder.create()
        textAdd.setOnClickListener {
            if (inputURL.text.isEmpty())
                Toast.makeText(this, "Please entering URL", Toast.LENGTH_LONG).show()
            else if (!Patterns.WEB_URL.matcher(inputURL.text).matches())
                Toast.makeText(this, "URL Invalid", Toast.LENGTH_LONG).show()
            else {
                var intent = Intent(this, AddNoteActivity::class.java).apply {
                    this.putExtra("fromQuickAction", true)
                    this.putExtra("type", "uri")
                    this.putExtra("uri", inputURL.text.toString())
                    Log.d("TEMPTEST", inputURL.text.toString())
                }
                arl.launch(intent)
                alertDialog.dismiss()
            }
        }
        textCancel.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    private fun filterList(newText: String?) {
        if (newText != null) {
            val filterList = ArrayList<Note>()
            for (i in noteList) {
                if (i.title.lowercase(Locale.ROOT).contains(newText)) {
                    filterList.add(i)
                }
            }
            if (filterList.isEmpty()) {
                noteAdapter.updateList(emptyList())
            } else
                noteAdapter.updateList(filterList)
        }
    }

    private fun initUI() {
        noteAdapter = NoteAdapter(this)
        binding.noteRecyclerView.setHasFixedSize(true)
        binding.noteRecyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.noteRecyclerView.adapter = noteAdapter
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        arlImage.launch(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 123 && grantResults.isNotEmpty())
            selectImage()
        else
            Toast.makeText(this, "Permission Denied!", Toast.LENGTH_LONG).show()
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