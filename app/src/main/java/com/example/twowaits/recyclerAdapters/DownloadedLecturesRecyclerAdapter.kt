package com.example.twowaits.recyclerAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.twowaits.R
import com.example.twowaits.homePages.DownloadedNotesDataClass

class DownloadedLecturesRecyclerAdapter(
    private val downloadedLectures: List<DownloadedNotesDataClass>,
    private val listener: DownloadedLectureClicked
): RecyclerView.Adapter<DownloadedLecturesRecyclerAdapter.DownloadedLecturesViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DownloadedLecturesRecyclerAdapter.DownloadedLecturesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.wishlist_card_view2, parent, false)
        return DownloadedLecturesViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: DownloadedLecturesRecyclerAdapter.DownloadedLecturesViewHolder,
        position: Int
    ) {
        holder.apply {
            nameOfLecture.text = downloadedLectures[position].name
            lectureDetails.text = ""
            creator.text = ""
        }
    }

    override fun getItemCount(): Int {
        return downloadedLectures.size
    }

    inner class DownloadedLecturesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){  //Nested Class
        val nameOfLecture: TextView = itemView.findViewById<TextView>(R.id.NameOfLecture)
        val lectureDetails: TextView = itemView.findViewById(R.id.LectureDetails)
        val creator: TextView = itemView.findViewById(R.id.Creator)
        val lectureImg: ImageView = itemView.findViewById(R.id.LectureImg)
        val bookmark: ToggleButton = itemView.findViewById(R.id.Bookmark)
        val wishlistCardView: CardView = itemView.findViewById(R.id.wishlistCardView)
        init {
            bookmark.visibility = View.INVISIBLE
            wishlistCardView.setOnClickListener {
                listener.onDownloadedLectureClicked(
                    "${downloadedLectures[absoluteAdapterPosition].name}.mp4"
                )
            }
        }
    }
}

interface DownloadedLectureClicked {
    fun onDownloadedLectureClicked(downloadedLectureName: String)
}