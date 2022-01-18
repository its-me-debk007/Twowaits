package com.example.twowaits.recyclerAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.twowaits.R

class WishlistRecyclerAdapter (private val size: Int): RecyclerView.Adapter<WishlistRecyclerAdapter.WishlistViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WishlistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.wishlist_card_view, parent, false)
        return WishlistViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: WishlistViewHolder,
        position: Int
    ) {
    }

    override fun getItemCount(): Int {
        return size
    }

    class WishlistViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val nameOfLecture: TextView = itemView.findViewById<TextView>(R.id.NameOfLecture)
        val lectureDetails: TextView = itemView.findViewById(R.id.LectureDetails)
        val creator: TextView = itemView.findViewById(R.id.Creator)
        val lectureImg: ImageView = itemView.findViewById(R.id.LectureImg)
        val bookmark: ImageView = itemView.findViewById(R.id.Bookmark)
    }
}