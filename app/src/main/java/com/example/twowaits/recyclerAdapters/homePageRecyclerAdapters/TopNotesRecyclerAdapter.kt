package com.example.twowaits.recyclerAdapters.homePageRecyclerAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.twowaits.R

class TopNotesRecyclerAdapter: RecyclerView.Adapter<TopNotesRecyclerAdapter.TopNotesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopNotesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bookmarked_notes_card_view, parent, false)
        return TopNotesViewHolder(view)
    }

    override fun onBindViewHolder(holder: TopNotesViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 5
    }

    class TopNotesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val subjectName: TextView = itemView.findViewById(R.id.SubjectName)
        val noteDetails: TextView = itemView.findViewById(R.id.NoteDetails)
        val moreNoteDetails: TextView = itemView.findViewById(R.id.MoreNoteDetails)
        val notesImg: ImageView = itemView.findViewById(R.id.NotesImg)
        val bookmark: ImageView = itemView.findViewById(R.id.Bookmark)
    }
}