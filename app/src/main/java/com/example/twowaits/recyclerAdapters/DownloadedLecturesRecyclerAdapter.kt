package com.example.twowaits.recyclerAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.twowaits.databinding.LectureListItemBinding

class DownloadedLecturesRecyclerAdapter(
    private val fileNames: List<String>,
    private val listener: DownloadedLectureClicked
) : RecyclerView.Adapter<DownloadedLecturesRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: LectureListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                bookmark.visibility = View.INVISIBLE
                wishlistCardView.setOnClickListener {
                    listener.onDownloadedLectureClicked(fileNames[adapterPosition])
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DownloadedLecturesRecyclerAdapter.ViewHolder {
        return ViewHolder(
            LectureListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(
        holder: DownloadedLecturesRecyclerAdapter.ViewHolder,
        position: Int
    ) {
        holder.binding.apply {
            nameOfLecture.text = fileNames[position]
            lectureDetails.text = ""
            creator.text = ""
        }
    }

    override fun getItemCount(): Int {
        return fileNames.size
    }
}

interface DownloadedLectureClicked {
    fun onDownloadedLectureClicked(downloadedLectureName: String)
}