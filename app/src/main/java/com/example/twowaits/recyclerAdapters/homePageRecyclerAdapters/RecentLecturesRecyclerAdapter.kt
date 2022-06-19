package com.example.twowaits.recyclerAdapters.homePageRecyclerAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.example.twowaits.R
import com.example.twowaits.network.dashboardApiCalls.RecentLecturesResponse
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class RecentLecturesRecyclerAdapter(
    private val adapter: String, private val size: Int,
    private val lectures: MutableList<RecentLecturesResponse>,
    private val listener: LecturesClicked
) :
    RecyclerView.Adapter<RecentLecturesRecyclerAdapter.TopLecturesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopLecturesViewHolder {
        val view: View = if (adapter == "HOME")
            LayoutInflater.from(parent.context)
                .inflate(R.layout.wishlist_card_view, parent, false)
        else
            LayoutInflater.from(parent.context)
                .inflate(R.layout.wishlist_card_view2, parent, false)
        return TopLecturesViewHolder(view)
    }

    override fun onBindViewHolder(holder: TopLecturesViewHolder, position: Int) {
        holder.apply {
            if (absoluteAdapterPosition == lectures.size) {
                seeAll.visibility = View.VISIBLE
                wishlistCardView.visibility = View.INVISIBLE
            } else {
                nameOfLecture.isSelected = true
                lectureDetails.isSelected = true
                creator.isSelected = true
                nameOfLecture.text = lectures[position].title
                lectureDetails.text = lectures[position].description
                bookmark.isChecked = lectures[position].wishlisted_by_user == "True"
                try {
                    creator.text = "By " + lectures[position].author_id.student.name
                } catch (e: Exception) {
                    try {
                        creator.text = "By " + lectures[position].author_id.faculty.name
                    } catch (e: Exception) {
                        creator.text = "By Anonymous"
                    }
                }
                if (adapter == "WISHLIST") {
                    bookmark.setOnClickListener {
                        listener.onWishlistBtnClicked(lectures[absoluteAdapterPosition].id)
                        lectures.removeAt(absoluteAdapterPosition)
                        notifyItemRemoved(absoluteAdapterPosition)
                        if (lectures.size == 0) listener.noItems()
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return lectures.size + 1
    }

    inner class TopLecturesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameOfLecture: TextView = itemView.findViewById<TextView>(R.id.NameOfLecture)
        val lectureDetails: TextView = itemView.findViewById(R.id.LectureDetails)
        val creator: TextView = itemView.findViewById(R.id.Creator)
        val lectureImg: ImageView = itemView.findViewById(R.id.LectureImg)
        val bookmark: ToggleButton = itemView.findViewById(R.id.bookmark)
        val wishlistCardView: MaterialCardView = itemView.findViewById(R.id.wishlistCardView)
        val seeAll: MaterialButton = itemView.findViewById(R.id.seeAll)

        init {
            nameOfLecture.isSelected = true
            lectureDetails.isSelected = true
            creator.isSelected = true
            wishlistCardView.setOnClickListener {
                listener.onLectureClicked(
                    lectures[absoluteAdapterPosition].video_firebase,
                    lectures[absoluteAdapterPosition].title
                )
            }
            bookmark.setOnClickListener {
                listener.onWishlistBtnClicked(lectures[absoluteAdapterPosition].id)
            }
        }
    }
}

interface LecturesClicked {
    fun onLectureClicked(videoUri: String, lectureName: String)
    fun onWishlistBtnClicked(lectureId: Int)
    fun noItems()
}