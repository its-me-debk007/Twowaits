package com.example.twowaits.recyclerAdapters.homePageRecyclerAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.twowaits.R

class QuizzesRecyclerAdapter: RecyclerView.Adapter<QuizzesRecyclerAdapter.QuizzesViewHolder>() {

    class QuizzesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val quizTopic: TextView = itemView.findViewById<TextView>(R.id.QuizTopic)
        val details: TextView = itemView.findViewById(R.id.Detail)
        val quizCreator: TextView = itemView.findViewById(R.id.QuizCreator)
        val quizImg: ImageView = itemView.findViewById(R.id.QuizImg)
        val noOfQuestions: TextView = itemView.findViewById(R.id.NoOfQuestions)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizzesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.quiz_card_view, parent, false)
        return QuizzesViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuizzesViewHolder, position: Int) {
    }

    override fun getItemCount(): Int {
        return 5
    }
}