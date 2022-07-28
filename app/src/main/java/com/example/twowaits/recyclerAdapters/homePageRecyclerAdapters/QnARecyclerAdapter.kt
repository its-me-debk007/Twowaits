package com.example.twowaits.recyclerAdapters.homePageRecyclerAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.twowaits.R

class QnARecyclerAdapter : RecyclerView.Adapter<QnARecyclerAdapter.QnAViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QnAViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.q_n_a, parent, false)
        return QnAViewHolder(view)
    }

    override fun onBindViewHolder(holder: QnAViewHolder, position: Int) {

    }

    override fun getItemCount(): Int = 5

    class QnAViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val question: TextView = itemView.findViewById(R.id.Question)
        val answersRecyclerView: RecyclerView = itemView.findViewById(R.id.answersRecyclerView)
    }
}