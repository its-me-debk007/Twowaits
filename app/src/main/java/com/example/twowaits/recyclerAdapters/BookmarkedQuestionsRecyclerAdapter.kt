package com.example.twowaits.recyclerAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.twowaits.R

class BookmarkedQuestionsRecyclerAdapter: RecyclerView.Adapter<BookmarkedQuestionsRecyclerAdapter.BookmarkedQuestionsViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookmarkedQuestionsRecyclerAdapter.BookmarkedQuestionsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.q_n_a, parent, false)
        return BookmarkedQuestionsRecyclerAdapter.BookmarkedQuestionsViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: BookmarkedQuestionsRecyclerAdapter.BookmarkedQuestionsViewHolder,
        position: Int
    ) {

    }

    override fun getItemCount(): Int {
        return 2
    }

    class BookmarkedQuestionsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){  //Nested Class
        val answerer: TextView = itemView.findViewById<TextView>(R.id.Answerer)
    }
}