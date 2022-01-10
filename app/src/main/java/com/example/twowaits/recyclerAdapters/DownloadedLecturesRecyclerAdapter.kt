package com.example.twowaits.recyclerAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.twowaits.R

class DownloadedLecturesRecyclerAdapter: RecyclerView.Adapter<DownloadedLecturesRecyclerAdapter.DownloadedLecturesViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DownloadedLecturesRecyclerAdapter.DownloadedLecturesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.wishlist_card_view, parent, false)
        return DownloadedLecturesRecyclerAdapter.DownloadedLecturesViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: DownloadedLecturesRecyclerAdapter.DownloadedLecturesViewHolder,
        position: Int
    ) {

    }

    override fun getItemCount(): Int {
        return 3
    }

    class DownloadedLecturesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){  //Nested Class

    }
}