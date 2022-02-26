package com.example.twowaits.recyclerAdapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.twowaits.apiCalls.dashboardApiCalls.quizApiCalls.QuestionXX
import com.example.twowaits.databinding.DetailedQuizResultBinding

class DetailedQuizResultRecyclerAdapter(val result: List<QuestionXX>) :
    RecyclerView.Adapter<DetailedQuizResultRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: DetailedQuizResultBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DetailedQuizResultBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            question.text = result[position].question_text
            option1.text = result[position].option[0].option
            option2.text = result[position].option[1].option
            if (result[position].option.size > 2) option3.text = result[position].option[2].option
            if (result[position].option.size > 3) option4.text = result[position].option[3].option

            for (i in result[position].option.indices) {
                val iterator = result[position].option[i]
                if (iterator.option_id == result[position].correct[0].correct) {
                    when (i) {
                        0 -> option1.setBackgroundColor(Color.parseColor("#00FF00"))
                        1 -> option2.setBackgroundColor(Color.parseColor("#00FF00"))
                        2 -> option3.setBackgroundColor(Color.parseColor("#00FF00"))
                        3 -> option4.setBackgroundColor(Color.parseColor("#00FF00"))
                    }
                }
                if (iterator.option_id.toString() == result[position].user_option) {
                    when (i) {
                        0 -> option1.setBackgroundColor(Color.parseColor("#0000FF"))
                        1 -> option2.setBackgroundColor(Color.parseColor("#0000FF"))
                        2 -> option3.setBackgroundColor(Color.parseColor("#0000FF"))
                        3 -> option4.setBackgroundColor(Color.parseColor("#0000FF"))
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return result.size
    }
}