package com.example.twowaits.recyclerAdapters.homePageRecyclerAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.twowaits.databinding.QuizListItemBinding
import com.example.twowaits.network.dashboardApiCalls.RecentQuizzesResponse

class QuizzesRecyclerAdapter(
    private val size: Int,
    private val quizzes: List<RecentQuizzesResponse>,
    private val listener: QuizClicked
) : RecyclerView.Adapter<QuizzesRecyclerAdapter.QuizzesViewHolder>() {

    inner class QuizzesViewHolder(val binding: QuizListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.cardView.setOnClickListener {
                listener.onQuizClicked(quizzes[adapterPosition].quiz_id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizzesViewHolder =
        QuizzesViewHolder(
            QuizListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: QuizzesViewHolder, position: Int) {
        holder.binding.apply {
            if (holder.adapterPosition == quizzes.size) {
                seeAll.visibility = View.VISIBLE
                cardView.visibility = View.INVISIBLE
            } else {
                name.text = quizzes[position].title
                noOfQuestions.text = quizzes[position].no_of_question.toString()
                val nameOfTeacher = when (quizzes[position].author_id.faculty.gender) {
                    "M" -> "${quizzes[position].author_id.faculty.name} Sir"
                    "F" -> "${quizzes[position].author_id.faculty.name} Ma'am"
                    else -> "${quizzes[position].author_id.faculty.name} Faculty"
                }
                quizCreator.text = "Quiz by $nameOfTeacher"
            }
        }
    }

    override fun getItemCount(): Int = size + 1
}

interface QuizClicked {
    fun onQuizClicked(quiz_id: Int)
}
