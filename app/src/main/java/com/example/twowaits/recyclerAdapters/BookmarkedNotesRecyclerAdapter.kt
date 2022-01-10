package com.example.twowaits.recyclerAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.twowaits.R

class BookmarkedNotesRecyclerAdapter (private val size: Int): RecyclerView.Adapter<BookmarkedNotesRecyclerAdapter.BookmarkedNotesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkedNotesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bookmarked_notes_card_view, parent, false)
        return  BookmarkedNotesViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookmarkedNotesViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return size
    }

    class BookmarkedNotesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){  //Nested Class
        val answerer: TextView = itemView.findViewById<TextView>(R.id.SubjectName)
    }
}