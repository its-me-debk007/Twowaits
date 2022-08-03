package com.example.twowaits.recyclerAdapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.twowaits.databinding.NotesListItemBinding

class DownloadedNotesRecyclerAdapter(
    private val fileNames: List<String>,
    private val listener: DownloadedNoteClicked
) : RecyclerView.Adapter<DownloadedNotesRecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DownloadedNotesRecyclerAdapter.ViewHolder = ViewHolder(
        NotesListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(
        holder: DownloadedNotesRecyclerAdapter.ViewHolder,
        position: Int
    ) {
        holder.binding.apply {
            constraintLayout.layoutParams.apply {
                width = ViewGroup.LayoutParams.MATCH_PARENT
                constraintLayout.layoutParams = this
            }

            cardView.layoutParams.apply {
                width = ViewGroup.LayoutParams.MATCH_PARENT
                cardView.layoutParams = this
            }
            Log.e("FILE_NAME", fileNames[position])
            val (noteName, noteDetails) = fileNames[position].split('?')
            name.text = noteName
            details.text = noteDetails
            moreDetails.text = "(Downloaded)"
        }
    }

    override fun getItemCount(): Int = fileNames.size

    inner class ViewHolder(val binding: NotesListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                bookmark.visibility = View.INVISIBLE
                cardView.setOnClickListener {
                    listener.onDownloadedNoteClicked(fileNames[adapterPosition])
                }
                cardView.setOnLongClickListener {
//                    listener.onLongPress()
                    true
                }
            }
        }
    }
}

interface DownloadedNoteClicked {
    fun onDownloadedNoteClicked(downloadedNoteName: String)
//    fun onLongPress()
}