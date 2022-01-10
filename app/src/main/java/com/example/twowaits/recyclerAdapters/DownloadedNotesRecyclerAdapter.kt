package com.example.twowaits.recyclerAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.twowaits.R

class DownloadedNotesRecyclerAdapter: RecyclerView.Adapter<DownloadedNotesRecyclerAdapter.DownloadedNotesViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DownloadedNotesRecyclerAdapter.DownloadedNotesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bookmarked_notes_card_view, parent, false)
        return DownloadedNotesRecyclerAdapter.DownloadedNotesViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: DownloadedNotesRecyclerAdapter.DownloadedNotesViewHolder,
        position: Int
    ) {

    }

    override fun getItemCount(): Int {
        return 3
    }

    class DownloadedNotesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){  //Nested Class

    }
}