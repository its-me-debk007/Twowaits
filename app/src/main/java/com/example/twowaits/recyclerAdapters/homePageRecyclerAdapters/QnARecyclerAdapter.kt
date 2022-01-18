package com.example.twowaits.recyclerAdapters.homePageRecyclerAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.borjabravo.readmoretextview.ReadMoreTextView
import com.example.twowaits.R

class QnARecyclerAdapter: RecyclerView.Adapter<QnARecyclerAdapter.QnAViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QnAViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.q_n_a, parent, false)
        return QnAViewHolder(view)
    }

    override fun onBindViewHolder(holder: QnAViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 3
    }

    class QnAViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val answerer: TextView = itemView.findViewById(R.id.Answerer)
        val question: TextView = itemView.findViewById(R.id.Question)
        val answer: ReadMoreTextView = itemView.findViewById(R.id.Answer)
        val answerDetails: TextView = itemView.findViewById(R.id.AnswerDetails)
        val answererProfilePic: ImageView = itemView.findViewById(R.id.AnswererProfilePic)
        val likesCount: TextView = itemView.findViewById(R.id.LikeCount)
        val commentsCount: TextView = itemView.findViewById(R.id.CommentCount)
        val likeBtn: ToggleButton = itemView.findViewById(R.id.Like)
        val commentBtn: ImageView = itemView.findViewById(R.id.Comment)
    }
}