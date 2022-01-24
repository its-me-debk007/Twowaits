package com.example.twowaits.recyclerAdapters.homePageRecyclerAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.twowaits.R
import com.example.twowaits.apiCalls.dashboardApiCalls.RecentQuizzesResponse

class RecentLecturesRecyclerAdapter(
    private val size: Int
) : RecyclerView.Adapter<RecentLecturesRecyclerAdapter.TopLecturesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopLecturesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.wishlist_card_view, parent, false)
        return TopLecturesViewHolder(view)
    }

    override fun onBindViewHolder(holder: TopLecturesViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 5
    }

    class TopLecturesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameOfLecture: TextView = itemView.findViewById<TextView>(R.id.NameOfLecture)
        val lectureDetails: TextView = itemView.findViewById(R.id.LectureDetails)
        val creator: TextView = itemView.findViewById(R.id.Creator)
        val lectureImg: ImageView = itemView.findViewById(R.id.LectureImg)
        val bookmark: ImageView = itemView.findViewById(R.id.Bookmark)
    }
}