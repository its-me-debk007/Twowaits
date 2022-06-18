package com.example.twowaits.recyclerAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.twowaits.databinding.QNABinding
import com.example.twowaits.network.dashboardApiCalls.Answer
import com.example.twowaits.network.dashboardApiCalls.QnAResponseItem

class QuestionsAnswersRecyclerAdapter(
    private val adapter: String,
    private val questionsAndAnswers: MutableList<QnAResponseItem>,
    private val listener: ItemClicked,
    private val context: Context
) :
    RecyclerView.Adapter<QuestionsAnswersRecyclerAdapter.ViewHolder>(), AnswerItemClicked {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(QNABinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            question.text = questionsAndAnswers[position].question
            answersRecyclerView.adapter = AnswersRecyclerAdapter(questionsAndAnswers[position].answer,
                this@QuestionsAnswersRecyclerAdapter, context)
            answersRecyclerView.isNestedScrollingEnabled = false
            answersRecyclerView.isNestedScrollingEnabled = false
            bookmarkBtn.isChecked = questionsAndAnswers[position].bookmarked_by_user == "True"
        }
    }

    override fun getItemCount(): Int {
        return questionsAndAnswers.size
    }

    inner class ViewHolder(val binding: QNABinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                question.setOnClickListener {
                    listener.onQuestionClicked(question.text.toString())
                }

                bookmarkBtn.setOnClickListener {
                    listener.bookmarkBtnClicked(questionsAndAnswers[absoluteAdapterPosition].question_id)
                }

                shareBtn.setOnClickListener {
                    listener.shareBtnClicked(
                        questionsAndAnswers[absoluteAdapterPosition].question,
                        questionsAndAnswers[absoluteAdapterPosition].answer
                    )
                }

                addAnswer.setOnClickListener {
                    listener.addAnswerClicked(
                        questionsAndAnswers[absoluteAdapterPosition].question,
                        questionsAndAnswers[absoluteAdapterPosition].question_id,
                        absoluteAdapterPosition
                    )
                }
                if (adapter == "BOOKMARK") {
                    bookmarkBtn.setOnClickListener {
                        listener.bookmarkBtnClicked(questionsAndAnswers[absoluteAdapterPosition].question_id)
                        questionsAndAnswers.removeAt(absoluteAdapterPosition)
                        notifyItemRemoved(absoluteAdapterPosition)
                        if (questionsAndAnswers.size == 0) listener.noItems()
                    }
                }
            }
        }
    }

    override fun likeBtnClicked(question_id: Int) {
        listener.likeBtnClicked(question_id)
    }

    override fun commentBtnClicked(): Boolean {
        return listener.commentBtnClicked()
    }

    override fun addCommentClicked(answer: String, answer_id: Int, position: Int) {
        listener.addCommentClicked(answer, answer_id, position)
    }
}

interface ItemClicked {
    fun onQuestionClicked(question: String)
    fun likeBtnClicked(question_id: Int)
    fun commentBtnClicked(): Boolean
    fun bookmarkBtnClicked(question_id: Int)
    fun shareBtnClicked(question: String, answersList: List<Answer>)
    fun addAnswerClicked(question: String, question_id: Int, position: Int)
    fun addCommentClicked(answer: String, answer_id: Int, position: Int)
    fun noItems()
}
