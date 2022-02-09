package com.example.twowaits.recyclerAdapters.homePageRecyclerAdapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.cardview.widget.CardView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.twowaits.R
import com.example.twowaits.apiCalls.dashboardApiCalls.RecentNotesResponse

class RecentNotesRecyclerAdapter(
    private val adapter: String,
    private val notes: MutableList<RecentNotesResponse>,
    private val listener: NotesClicked
) :
    RecyclerView.Adapter<RecentNotesRecyclerAdapter.RecentNotesViewHolder>() {

    inner class RecentNotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subjectName: TextView = itemView.findViewById(R.id.SubjectName)
        val noteDetails: TextView = itemView.findViewById(R.id.NoteDetails)
        val moreNoteDetails: TextView = itemView.findViewById(R.id.MoreNoteDetails)
        val notesImg: ImageView = itemView.findViewById(R.id.NotesImg)
        val bookmark: ToggleButton = itemView.findViewById(R.id.Bookmark)
        val cardView: CardView = itemView.findViewById(R.id.cardView)

        init {
            cardView.setOnClickListener {
                listener.onNotesClicked(notes[absoluteAdapterPosition].file_obj_firebase.toUri(),
                notes[absoluteAdapterPosition].title)
            }
            bookmark.setOnClickListener{
                listener.onBookmarkNotesClicked(notes[absoluteAdapterPosition].id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentNotesViewHolder {
        val view: View = if (adapter == "HOME")
            LayoutInflater.from(parent.context)
                .inflate(R.layout.bookmarked_notes_card_view, parent, false)
        else
            LayoutInflater.from(parent.context)
                .inflate(R.layout.bookmarked_notes_card_view2, parent, false)
        return RecentNotesViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecentNotesViewHolder, position: Int) {
        holder.apply {
            subjectName.text = notes[position].title
            bookmark.isChecked = notes[position].bookmarked_by_user == "True"
            if (adapter == "BOOKMARK") {
                bookmark.setOnClickListener {
                    listener.onBookmarkNotesClicked(notes[absoluteAdapterPosition].id)
                    notes.removeAt(absoluteAdapterPosition)
                    notifyDataSetChanged()
                }
            }
            noteDetails.text = notes[position].description
            try {
                moreNoteDetails.text = "By " + notes[position].author_id.student.name
            } catch (e: Exception) {
                try {
                    moreNoteDetails.text = "By " + notes[position].author_id.faculty.name
                } catch (e: Exception) {
                    moreNoteDetails.text = "By Anonymous"
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }
}

interface NotesClicked {
    fun onNotesClicked(pdfUri: Uri, noteName: String)
    fun onBookmarkNotesClicked(noteId: Int)
}