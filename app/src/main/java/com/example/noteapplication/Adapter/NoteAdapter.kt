package com.example.noteapplication.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapplication.Model.Note
import com.example.noteapplication.R

class NoteAdapter: RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    inner class NoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val textTitle = itemView.findViewById<TextView>(R.id.textTitle)
        val textSubTitle = itemView.findViewById<TextView>(R.id.textSubTitle)
        val textDateTime = itemView.findViewById<TextView>(R.id.textDateTime)
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
    }

    fun updateList(newList: List<Note>) {
        listNote.clear()
        listNote.addAll(newList)
        notifyDataSetChanged()
    }
}