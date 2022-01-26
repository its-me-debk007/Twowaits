package com.example.twowaits.recyclerAdapters.homePageRecyclerAdapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.twowaits.R
import com.example.twowaits.apiCalls.dashboardApiCalls.RecentNotesResponse
import com.example.twowaits.recyclerAdapters.ItemClicked

class RecentNotesRecyclerAdapter(private val size: Int, private val notes: List<RecentNotesResponse>, private val listener: NotesClicked):
    RecyclerView.Adapter<RecentNotesRecyclerAdapter.RecentNotesViewHolder>() {

    inner class RecentNotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subjectName: TextView = itemView.findViewById(R.id.SubjectName)
        val noteDetails: TextView = itemView.findViewById(R.id.NoteDetails)
        val moreNoteDetails: TextView = itemView.findViewById(R.id.MoreNoteDetails)
        val notesImg: ImageView = itemView.findViewById(R.id.NotesImg)
        val bookmark: ToggleButton = itemView.findViewById(R.id.Bookmark)
        init {
            itemView.setOnClickListener {
                listener.onNotesClicked(notes[adapterPosition].id)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentNotesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.bookmarked_notes_card_view, parent, false)
        return RecentNotesViewHolder(view)
    }
    override fun onBindViewHolder(holder: RecentNotesViewHolder, position: Int) {
        holder.apply {
            subjectName.text = notes[position].title
            bookmark.isChecked = notes[position].bookmarked_by_user == "True"
            noteDetails.text = notes[position].description
            try {
                moreNoteDetails.text = notes[position].author_id.student.name
            } catch (e: Exception) {
                try {
                    moreNoteDetails.text = notes[position].author_id.faculty.name

                } catch (e: Exception) {
                    moreNoteDetails.text = "Anonymous"
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }
}

interface NotesClicked {
    fun onNotesClicked(note_id: Int)
}