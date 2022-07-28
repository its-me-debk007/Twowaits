package com.example.twowaits.recyclerAdapters.homePageRecyclerAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.twowaits.R
import com.example.twowaits.databinding.BookmarkedNotesItemBinding
import com.example.twowaits.network.dashboardApiCalls.RecentLecturesResponse

class RecentLecturesRecyclerAdapter(
    private val adapter: String, private val lectures: MutableList<RecentLecturesResponse>,
    private val listener: LecturesClicked
) : RecyclerView.Adapter<RecentLecturesRecyclerAdapter.TopLecturesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopLecturesViewHolder =
        TopLecturesViewHolder(
            BookmarkedNotesItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: TopLecturesViewHolder, position: Int) {
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
            img.setImageResource(R.drawable.ic_lecture_placeholder)
            if (holder.absoluteAdapterPosition == lectures.size) {
                if (adapter == "HOME") {
                    seeAll.visibility = View.VISIBLE
                    cardView.visibility = View.GONE
                } else cardView.visibility = View.GONE
            } else {
                name.text = lectures[position].title
                details.text = lectures[position].description
                bookmark.isChecked = lectures[position].wishlisted_by_user == "True"
                try {
                    moreDetails.text = "By " + lectures[position].author_id.student.name
                } catch (e: Exception) {
                    try {
                        moreDetails.text = "By " + lectures[position].author_id.faculty.name
                    } catch (e: Exception) {
                        moreDetails.text = "By Anonymous"
                    }
                }
                if (adapter == "WISHLIST") {
                    bookmark.setOnClickListener {
                        listener.onWishlistBtnClicked(lectures[holder.absoluteAdapterPosition].id)
                        lectures.removeAt(holder.absoluteAdapterPosition)
                        notifyItemRemoved(holder.absoluteAdapterPosition)
                        if (lectures.size == 0) listener.noItems()
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = lectures.size + 1

    inner class TopLecturesViewHolder(val binding: BookmarkedNotesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        //        val nameOfLecture: TextView = itemView.findViewById(R.id.name)
//        val lectureDetails: TextView = itemView.findViewById(R.id.details)
//        val creator: TextView = itemView.findViewById(R.id.moreDetails)
//        val lectureImg: ImageView = itemView.findViewById(R.id.img)
//        val bookmark: ToggleButton = itemView.findViewById(R.id.bookmark)
//        val wishlistCardView: MaterialCardView = itemView.findViewById(R.id.cardView)
//        val seeAll: MaterialButton = itemView.findViewById(R.id.seeAll)
//        val constraintLayout: ConstraintLayout = itemView.findViewById(R.id.constraintLayout)
        init {
            binding.apply {
                name.isSelected = true
                details.isSelected = true
                moreDetails.isSelected = true
                cardView.setOnClickListener {
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
}

interface LecturesClicked {
    fun onLectureClicked(videoUri: String, lectureName: String)
    fun onWishlistBtnClicked(lectureId: Int)
    fun noItems()
}