package com.example.twowaits.recyclerAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.twowaits.databinding.BookmarkedNotesCardView2Binding

class DownloadedNotesRecyclerAdapter(
    private val fileNames: List<String>,
    private val listener: DownloadedNoteClicked
) : RecyclerView.Adapter<DownloadedNotesRecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DownloadedNotesRecyclerAdapter.ViewHolder {
        return ViewHolder(
            BookmarkedNotesCardView2Binding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(
        holder: DownloadedNotesRecyclerAdapter.ViewHolder,
        position: Int
    ) {
        holder.binding.apply {
            subjectName.text = fileNames[position]
            noteDetails.text = ""
            moreNoteDetails.text = ""
        }
    }

    override fun getItemCount(): Int {
        return fileNames.size
    }

    inner class ViewHolder(val binding: BookmarkedNotesCardView2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                bookmark.visibility = View.INVISIBLE
                cardView.setOnClickListener {
                    listener.onDownloadedNoteClicked(fileNames[absoluteAdapterPosition])
                }
                cardView.setOnLongClickListener {
                    listener.onLongPress()
                    true
                }
            }
        }
    }
}

interface DownloadedNoteClicked {
    fun onDownloadedNoteClicked(downloadedNoteName: String)
    fun onLongPress()
}