package com.example.twowaits.recyclerAdapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.cardview.widget.CardView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.twowaits.R
import com.example.twowaits.apiCalls.dashboardApiCalls.WishlistResponse

class WishlistRecyclerAdapter(
    private val lectures: List<WishlistResponse>,
    private val listener: WishlistLectureClicked
) : RecyclerView.Adapter<WishlistRecyclerAdapter.WishlistViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WishlistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.wishlist_card_view2, parent, false)
        return WishlistViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: WishlistViewHolder,
        position: Int
    ) {
        holder.apply {
            nameOfLecture.text = lectures[position].title
            lectureDetails.text = lectures[position].description
            creator.text = ""
        }
    }

    override fun getItemCount(): Int {
        return lectures.size
    }

    inner class WishlistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameOfLecture: TextView = itemView.findViewById(R.id.NameOfLecture)
        val lectureDetails: TextView = itemView.findViewById(R.id.LectureDetails)
        val creator: TextView = itemView.findViewById(R.id.Creator)
        val lectureImg: ImageView = itemView.findViewById(R.id.LectureImg)
        val bookmark: ToggleButton = itemView.findViewById(R.id.Bookmark)
        val wishlistCardView: CardView = itemView.findViewById(R.id.wishlistCardView)

        init {
            wishlistCardView.setOnClickListener {
                listener.onLectureClicked(
                    lectures[absoluteAdapterPosition].video_firebase.toUri(),
                    lectures[absoluteAdapterPosition].title
                )
            }
            bookmark.setOnClickListener {
                listener.onWishlistBtnClicked(lectures[absoluteAdapterPosition].id)
            }
        }
    }
}

interface WishlistLectureClicked {
    fun onLectureClicked(videoUri: Uri, lectureName: String)
    fun onWishlistBtnClicked(lecture_id: Int)
}