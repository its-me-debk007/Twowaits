package com.example.twowaits.recyclerAdapters.homePageRecyclerAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.example.twowaits.R
import com.example.twowaits.network.dashboardApiCalls.RecentNotesResponse
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class RecentNotesRecyclerAdapter(
    private val adapter: String,
    private val notes: MutableList<RecentNotesResponse>,
    private val listener: NotesClicked
) :
    RecyclerView.Adapter<RecentNotesRecyclerAdapter.RecentNotesViewHolder>() {

    inner class RecentNotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subjectName: TextView = itemView.findViewById(R.id.subjectName)
        val noteDetails: TextView = itemView.findViewById(R.id.noteDetails)
        val moreNoteDetails: TextView = itemView.findViewById(R.id.moreNoteDetails)
        val bookmark: ToggleButton = itemView.findViewById(R.id.bookmark)
        val cardView: MaterialCardView = itemView.findViewById(R.id.cardView)
        val seeAll: MaterialButton = itemView.findViewById(R.id.seeAll)

        init {
            cardView.setOnClickListener {
                listener.onNotesClicked(
                    notes[absoluteAdapterPosition].file_obj_firebase,
                    notes[absoluteAdapterPosition].title
                )
            }
            bookmark.setOnClickListener {
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
            if (absoluteAdapterPosition == notes.size) {
                if (adapter == "HOME") {
                    seeAll.visibility = View.VISIBLE
                    cardView.visibility = View.GONE
                }
            } else {
                subjectName.text = notes[position].title
                bookmark.isChecked = notes[position].bookmarked_by_user == "True"
                if (adapter == "BOOKMARK") {
                    bookmark.setOnClickListener {
                        listener.onBookmarkNotesClicked(notes[absoluteAdapterPosition].id)
                        notes.removeAt(absoluteAdapterPosition)
                        notifyItemRemoved(absoluteAdapterPosition)
                        if (notes.size == 0) listener.noItems()
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
    }

    override fun getItemCount() = notes.size + 1
}

interface NotesClicked {
    fun onNotesClicked(pdfUri: String, noteName: String)
    fun onBookmarkNotesClicked(noteId: Int)
    fun noItems()
}