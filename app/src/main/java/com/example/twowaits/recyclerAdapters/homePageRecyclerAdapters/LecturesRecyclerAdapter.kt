package com.example.twowaits.recyclerAdapters.homePageRecyclerAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.twowaits.R
import com.example.twowaits.databinding.NotesListItemBinding
import com.example.twowaits.network.dashboardApiCalls.RecentLecturesResponse

class RecentLecturesRecyclerAdapter(
    private val adapter: String, private val lectures: MutableList<RecentLecturesResponse>,
    private val listener: LecturesClicked
) : RecyclerView.Adapter<RecentLecturesRecyclerAdapter.TopLecturesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopLecturesViewHolder =
        TopLecturesViewHolder(
            NotesListItemBinding.inflate(
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
            if (holder.adapterPosition == lectures.size) {
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
                        listener.onWishlistBtnClicked(lectures[holder.adapterPosition].id)
                        lectures.removeAt(holder.adapterPosition)
                        notifyItemRemoved(holder.adapterPosition)
                        if (lectures.size == 0) listener.noItems()
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int = lectures.size + 1

    inner class TopLecturesViewHolder(val binding: NotesListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                name.isSelected = true
                details.isSelected = true
                moreDetails.isSelected = true
                cardView.setOnClickListener {
                    listener.onLectureClicked(
                        lectures[adapterPosition].video_firebase,
                        lectures[adapterPosition].title
                    )
                }
                bookmark.setOnClickListener {
                    listener.onWishlistBtnClicked(lectures[adapterPosition].id)
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