package com.example.noteapplication.Activity

import android.content.Intent
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.drawable.GradientDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.noteapplication.Model.Note
import com.example.noteapplication.R
import com.example.noteapplication.ViewModel.NoteViewModel
import com.example.noteapplication.databinding.ActivityAddNoteBinding
import com.example.noteapplication.databinding.OptionNoteBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.text.SimpleDateFormat
import java.util.Date

class AddNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var selectedColor: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        noteViewModel = ViewModelProvider(this).get(NoteViewModel::class.java)

        val formatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm a")
        binding.textDateTime.text = formatter.format(Date())
        selectedColor = "#333333"
        intiOptionColor()
        setViewSubtitleColor()
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
                note.color = selectedColor
                val intent = Intent()
                intent.putExtra("note", note)
                setResult(RESULT_OK, intent)
                finish()
            }
        }

    }


    private fun intiOptionColor() {
        val layoutOptionColor = findViewById<LinearLayout>(R.id.layoutOptionNote)
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
    }

    private fun setViewSubtitleColor() {
        val gradientDrawable: GradientDrawable = findViewById<View>(R.id.viewSubTitle).background as GradientDrawable
        gradientDrawable.setColor(Color.parseColor(selectedColor))
    }
}
