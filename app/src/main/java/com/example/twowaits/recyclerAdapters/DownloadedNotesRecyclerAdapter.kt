package com.example.twowaits.recyclerAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.twowaits.R
import com.example.twowaits.homePages.DownloadedNotesDataClass

class DownloadedNotesRecyclerAdapter(
    private val downloadedNotes: List<DownloadedNotesDataClass>,
    private val listener: DownloadedNoteClicked
) : RecyclerView.Adapter<DownloadedNotesRecyclerAdapter.DownloadedNotesViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DownloadedNotesRecyclerAdapter.DownloadedNotesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.bookmarked_notes_card_view2, parent, false)
        return DownloadedNotesViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: DownloadedNotesRecyclerAdapter.DownloadedNotesViewHolder,
        position: Int
    ) {
        holder.apply {
            subjectName.text = downloadedNotes[position].name
            noteDetails.text = ""
            moreNoteDetails.text = ""
        }
    }

    override fun getItemCount(): Int {
        return downloadedNotes.size
    }

    inner class DownloadedNotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subjectName: TextView = itemView.findViewById(R.id.SubjectName)
        val noteDetails: TextView = itemView.findViewById(R.id.NoteDetails)
        val moreNoteDetails: TextView = itemView.findViewById(R.id.MoreNoteDetails)
        val notesImg: ImageView = itemView.findViewById(R.id.NotesImg)
        val bookmark: ToggleButton = itemView.findViewById(R.id.Bookmark)
        val cardView: CardView = itemView.findViewById(R.id.cardView)

        init {
            bookmark.visibility = View.INVISIBLE
            cardView.setOnClickListener {
                listener.onDownloadedNoteClicked(
                    "${downloadedNotes[absoluteAdapterPosition].name}.pdf"
                )
            }
        }
    }
}

interface DownloadedNoteClicked {
    fun onDownloadedNoteClicked(downloadedNoteName: String)
}