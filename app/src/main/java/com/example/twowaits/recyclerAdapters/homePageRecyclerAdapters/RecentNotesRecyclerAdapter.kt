package com.example.twowaits.recyclerAdapters.homePageRecyclerAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.twowaits.databinding.BookmarkedNotesItemBinding
import com.example.twowaits.network.dashboardApiCalls.RecentNotesResponse

class RecentNotesRecyclerAdapter(
    private val adapter: String,
    private val notes: MutableList<RecentNotesResponse>,
    private val listener: NotesClicked
) : RecyclerView.Adapter<RecentNotesRecyclerAdapter.RecentNotesViewHolder>() {

    inner class RecentNotesViewHolder(val binding: BookmarkedNotesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentNotesViewHolder =
        RecentNotesViewHolder(
            BookmarkedNotesItemBinding.inflate(
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

            if (holder.absoluteAdapterPosition == notes.size) {
                if (adapter == "HOME") {
                    seeAll.visibility = View.VISIBLE
                    cardView.visibility = View.GONE
                } else cardView.visibility = View.GONE
            } else {
                name.text = notes[position].title
                bookmark.isChecked = notes[position].bookmarked_by_user == "True"
                if (adapter == "BOOKMARK") {
                    bookmark.setOnClickListener {
                        listener.onBookmarkNotesClicked(notes[holder.absoluteAdapterPosition].id)
                        notes.removeAt(holder.absoluteAdapterPosition)
                        notifyItemRemoved(holder.absoluteAdapterPosition)
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