package com.example.twowaits.recyclerAdapters.homePageRecyclerAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.twowaits.R
import com.example.twowaits.network.dashboardApiCalls.RecentQuizzesResponse
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class QuizzesRecyclerAdapter(
    private val size: Int,
    private val quizzes: List<RecentQuizzesResponse>,
    private val listener: QuizClicked
) :
    RecyclerView.Adapter<QuizzesRecyclerAdapter.QuizzesViewHolder>() {

    inner class QuizzesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val quizTopic: TextView = itemView.findViewById(R.id.name)
        val details: TextView = itemView.findViewById(R.id.details)
        val quizCreator: TextView = itemView.findViewById(R.id.quizCreator)
        val quizImg: ImageView = itemView.findViewById(R.id.img)
        val noOfQuestions: TextView = itemView.findViewById(R.id.noOfQuestions)
        val seeAll: MaterialButton = itemView.findViewById(R.id.seeAll)
        val cardView: MaterialCardView = itemView.findViewById(R.id.cardView)

        init {
            itemView.setOnClickListener {
                listener.onQuizClicked(quizzes[absoluteAdapterPosition].quiz_id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizzesViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.quiz_list_item, parent, false)
        return QuizzesViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuizzesViewHolder, position: Int) {
        holder.apply {
            if (absoluteAdapterPosition == quizzes.size) {
                seeAll.visibility = View.VISIBLE
                cardView.visibility = View.INVISIBLE
            } else {
                quizTopic.isSelected = true
                quizCreator.isSelected = true
                quizTopic.text = quizzes[position].title
                noOfQuestions.text = quizzes[position].no_of_question.toString()
                if (quizzes[position].author_id.faculty != null) {
                    val nameOfTeacher = when (quizzes[position].author_id.faculty?.gender) {
                        "M" -> "${quizzes[position].author_id.faculty?.name} Sir"
                        "F" -> "${quizzes[position].author_id.faculty?.name} Ma'am"
                        else -> "${quizzes[position].author_id.faculty?.name} Faculty"
                    }
                    quizCreator.text = "Quiz by $nameOfTeacher"
                } else quizCreator.text = "Anonymous"
            }
        }
    }

    override fun getItemCount(): Int {
        return size + 1
    }
}

interface QuizClicked {
    fun onQuizClicked(quiz_id: Int)
}
