package com.example.twowaits.recyclerAdapters.homePageRecyclerAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.twowaits.databinding.NotesListItemBinding
import com.example.twowaits.network.dashboardApiCalls.RecentNotesResponse

class NotesRecyclerAdapter(
    private val adapter: String,
    private val notes: MutableList<RecentNotesResponse>,
    private val listener: NotesClicked
) : RecyclerView.Adapter<NotesRecyclerAdapter.RecentNotesViewHolder>() {

    inner class RecentNotesViewHolder(val binding: NotesListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                cardView.setOnClickListener {
                    listener.onNotesClicked(
                        notes[adapterPosition].file_obj_firebase,
                        notes[adapterPosition].title + '?' +
                                notes[adapterPosition].description
                    )
                }
                bookmark.setOnClickListener {
                    listener.onBookmarkNotesClicked(notes[adapterPosition].id)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentNotesViewHolder =
        RecentNotesViewHolder(
            NotesListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: RecentNotesViewHolder, position: Int) {
        holder.binding.apply {
            if (adapter != "HOME") {
                constraintLayout.layoutParams.apply {
                    width = ViewGroup.LayoutParams.MATCH_PARENT
                    constraintLayout.layoutParams = this
                }

                cardView.layoutParams.apply {
                    width = ViewGroup.LayoutParams.MATCH_PARENT
                    cardView.layoutParams = this
                }
            }

            if (holder.adapterPosition == notes.size) {
                if (adapter == "HOME") {
                    seeAll.visibility = View.VISIBLE
                    cardView.visibility = View.GONE
                } else cardView.visibility = View.GONE
            } else {
                name.text = notes[position].title
                bookmark.isChecked = notes[position].bookmarked_by_user == "True"
                if (adapter == "BOOKMARK") {
                    bookmark.setOnClickListener {
                        listener.onBookmarkNotesClicked(notes[holder.adapterPosition].id)
                        notes.removeAt(holder.adapterPosition)
                        notifyItemRemoved(holder.adapterPosition)
                        if (notes.size == 0) listener.noItems()
                    }
                }
                details.text = notes[position].description
                try {
                    moreDetails.text = "By " + notes[position].author_id.student.name
                } catch (e: Exception) {
                    try {
                        moreDetails.text = "By " + notes[position].author_id.faculty.name
                    } catch (e: Exception) {
                        moreDetails.text = "By Anonymous"
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