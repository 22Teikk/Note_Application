package com.example.noteapplication.Adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapplication.Listener.NotesListener
import com.example.noteapplication.Model.Note
import com.example.noteapplication.R
import com.example.noteapplication.Utilities.Converters
import com.makeramen.roundedimageview.RoundedImageView
import java.util.Timer
import java.util.TimerTask

class NoteAdapter(
    val notesListener: NotesListener
): RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    inner class NoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val textTitle = itemView.findViewById<TextView>(R.id.textTitle)
        val textSubTitle = itemView.findViewById<TextView>(R.id.textSubTitle)
        val textDateTime = itemView.findViewById<TextView>(R.id.textDateTime)
        val layoutNote = itemView.findViewById<LinearLayout>(R.id.layoutNote)
        val imageNoteItem = itemView.findViewById<RoundedImageView>(R.id.imageNoteItem)
    }

    var listNote = ArrayList<Note>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false))
    }

    override fun getItemCount(): Int {
        return listNote.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = listNote[position]
        holder.textTitle.setText(currentNote.title)
        holder.textSubTitle.setText(currentNote.subTitle)
        holder.textDateTime.setText(currentNote.dateTime)
        var gradientDrawable: GradientDrawable = holder.layoutNote.background as GradientDrawable
        gradientDrawable.setColor(Color.parseColor(currentNote.color))

        if (currentNote.imagePath != null) {
            var bitmap = Converters.stringToBitmap(currentNote.imagePath)
            holder.imageNoteItem.setImageBitmap(bitmap)
            holder.imageNoteItem.visibility = View.VISIBLE
        }else
            holder.imageNoteItem.visibility = View.GONE

        holder.layoutNote.setOnClickListener {
            notesListener.onNoteClicked(currentNote)
        }

        holder.layoutNote.setOnLongClickListener {
            notesListener.onNoteLongClicked(currentNote, holder.layoutNote)
            true
        }
    }

    fun updateList(newList: List<Note>) {
        listNote.clear()
        listNote.addAll(newList)
        notifyDataSetChanged()
    }

    public fun filter(query: String) {
        var filterList = listNote
        filterList.clear()
        if (query.isEmpty()) {
            filterList.addAll(listNote)
        } else {
            for (item in listNote) {
                if (item.title.toLowerCase().contains(query.toLowerCase())) {
                    filterList.add(item)
                }
            }
        }
        notifyDataSetChanged()
    }
}