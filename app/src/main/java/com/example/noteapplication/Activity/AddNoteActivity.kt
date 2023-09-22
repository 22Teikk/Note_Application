package com.example.noteapplication.Activity

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.noteapplication.Model.Note
import com.example.noteapplication.R
import com.example.noteapplication.Utilities.Converters
import com.example.noteapplication.ViewModel.NoteViewModel
import com.example.noteapplication.databinding.ActivityAddNoteBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.text.SimpleDateFormat
import java.util.Date


class AddNoteActivity : AppCompatActivity() {
    private lateinit var layoutOptionColor :LinearLayout
    private var oldID: Int = -1
    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var noteViewModel: NoteViewModel
    var selectedColor: String = "#333333"
    var selectedImagePath: String = ""
    private var selectedUri: String = ""
    private var isUpdate = false
    private lateinit var oldNote: Note
    val arl = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->        if (result.resultCode == RESULT_OK) {
            val imageView = result.data?.data as Uri
            if (imageView != null) {
                try {
                    var inputStream = contentResolver.openInputStream(imageView)
                    var bitmap = BitmapFactory.decodeStream(inputStream)
                    selectedImagePath = Converters.bitmapToString(bitmap)
                    binding.imageNote.setImageBitmap(bitmap)
                    binding.imageNote.visibility = View.VISIBLE

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        layoutOptionColor = findViewById<LinearLayout>(R.id.layoutOptionNote)
        try {
            isUpdate = true
            oldNote = intent.getSerializableExtra("note") as Note
            oldID = oldNote.id
            binding.inputNoteTitle.setText(oldNote.title)
            binding.textDateTime.setText(oldNote.dateTime)
            binding.inputNoteSubTitle.setText(oldNote.subTitle)
            binding.inputNote.setText(oldNote.noteText)
            if (oldNote.imagePath != "") {
                selectedImagePath = oldNote.imagePath
                binding.imageNote.setImageBitmap(Converters.stringToBitmap(oldNote.imagePath))
                binding.imageNote.visibility = View.VISIBLE
            }
            if (oldNote.webLink != null) {
                selectedUri = oldNote.webLink
                binding.textWebURL.setText(oldNote.webLink)
                binding.layoutWebURL.visibility = View.VISIBLE
            }
            if (oldNote.color != "#333333") {
                selectedColor = oldNote.color
                when (oldNote.color) {
                    "#E79797" -> {
                        layoutOptionColor.findViewById<ImageView>(R.id.imageColor1).setImageResource(R.drawable.baseline_done_24)
                    }
                    "#D3C292" -> {
                        layoutOptionColor.findViewById<ImageView>(R.id.imageColor2).setImageResource(R.drawable.baseline_done_24)
                    }
                    "#B6F892" -> {
                        layoutOptionColor.findViewById<ImageView>(R.id.imageColor3).setImageResource(R.drawable.baseline_done_24)
                    }
                    "#5EC9C8" -> {
                        layoutOptionColor.findViewById<ImageView>(R.id.imageColor4).setImageResource(R.drawable.baseline_done_24)
                    }
                    "#1594B8" -> {
                        layoutOptionColor.findViewById<ImageView>(R.id.imageColor5).setImageResource(R.drawable.baseline_done_24)
                    }
                    "#602587" -> {
                        layoutOptionColor.findViewById<ImageView>(R.id.imageColor6).setImageResource(R.drawable.baseline_done_24)
                    }
                }
            }
            setViewSubtitleColor()
        }catch (e: Exception) {
            e.printStackTrace()
        }

        if (isUpdate == false) {
            val formatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm a")
            binding.textDateTime.text = formatter.format(Date())
            selectedColor = "#333333"
            selectedImagePath = ""
            selectedUri = ""
        }
        intiOptionColor()
        setViewSubtitleColor()
        binding.imageBack.setOnClickListener {
            onBackPressed()
        }

        binding.imageSave.setOnClickListener {
            if (binding.inputNoteTitle.text.isEmpty())
                Toast.makeText(this, "Note title can't be empty!", Toast.LENGTH_LONG).show()
            else {
                val note: Note
                if (isUpdate) {
                    note = Note(oldID,
                        binding.inputNoteTitle.text.toString(),
                        binding.textDateTime.text.toString(),
                        binding.inputNoteSubTitle.text.toString(),
                        binding.inputNote.text.toString())
                }else {
                    note = Note(0,
                        binding.inputNoteTitle.text.toString(),
                        binding.textDateTime.text.toString(),
                        binding.inputNoteSubTitle.text.toString(),
                        binding.inputNote.text.toString())
                }
                note.color = selectedColor
                if (selectedImagePath != null)
                    note.imagePath = selectedImagePath

                if (binding.layoutWebURL.visibility == View.VISIBLE)
                    note.webLink = selectedUri
                val intent = Intent()
                intent.putExtra("note", note)
                setResult(RESULT_OK, intent)
                finish()
            }
        }

    }


    private fun intiOptionColor() {
        val bottomSheetBehavior = BottomSheetBehavior.from(layoutOptionColor)
        layoutOptionColor.findViewById<TextView>(R.id.textOption).setOnClickListener {
            if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }else bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        layoutOptionColor.findViewById<View>(R.id.viewColor1).setOnClickListener {
            selectedColor = "#E79797"
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor1).setImageResource(R.drawable.baseline_done_24)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor2).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor3).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor4).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor5).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor6).setImageResource(0)
            setViewSubtitleColor()
        }
        layoutOptionColor.findViewById<View>(R.id.viewColor2).setOnClickListener {
            selectedColor = "#D3C292"
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor2).setImageResource(R.drawable.baseline_done_24)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor1).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor3).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor4).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor5).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor6).setImageResource(0)
            setViewSubtitleColor()
        }
        layoutOptionColor.findViewById<View>(R.id.viewColor3).setOnClickListener {
            selectedColor = "#B6F892"
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor3).setImageResource(R.drawable.baseline_done_24)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor2).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor1).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor4).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor5).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor6).setImageResource(0)
            setViewSubtitleColor()
        }
        layoutOptionColor.findViewById<View>(R.id.viewColor4).setOnClickListener {
            selectedColor = "#5EC9C8"
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor4).setImageResource(R.drawable.baseline_done_24)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor2).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor3).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor1).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor5).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor6).setImageResource(0)
            setViewSubtitleColor()
        }
        layoutOptionColor.findViewById<View>(R.id.viewColor5).setOnClickListener {
            selectedColor = "#1594B8"
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor5).setImageResource(R.drawable.baseline_done_24)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor2).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor3).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor4).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor1).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor6).setImageResource(0)
            setViewSubtitleColor()
        }
        layoutOptionColor.findViewById<View>(R.id.viewColor6).setOnClickListener {
            selectedColor = "#602587"
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor6).setImageResource(R.drawable.baseline_done_24)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor2).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor3).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor4).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor5).setImageResource(0)
            layoutOptionColor.findViewById<ImageView>(R.id.imageColor1).setImageResource(0)
            setViewSubtitleColor()
        }



        //Add Image
        layoutOptionColor.findViewById<LinearLayout>(R.id.layoutAddImage).setOnClickListener {
            val permission: Array<String> = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            if (ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permission, 123)
            }else selectImage()
        }

        //Add URL
        layoutOptionColor.findViewById<LinearLayout>(R.id.layoutAddURLView).setOnClickListener {
            bottomSheetBehavior.state = (BottomSheetBehavior.STATE_COLLAPSED)
            showDialogURL()
        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        arl.launch(intent)
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

    //Set background note
    private fun setViewSubtitleColor() {
        val gradientDrawable: GradientDrawable = findViewById<View>(R.id.viewSubTitle).background as GradientDrawable
        gradientDrawable.setColor(Color.parseColor(selectedColor))
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
            else if(!Patterns.WEB_URL.matcher(inputURL.text).matches())
                Toast.makeText(this, "URL Invalid", Toast.LENGTH_LONG).show()
            else {
                binding.textWebURL.text = inputURL.text
                selectedUri = inputURL.text.toString()
                binding.layoutWebURL.visibility = View.VISIBLE
                alertDialog.dismiss()
            }
        }
        textCancel.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

}
