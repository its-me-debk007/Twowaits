package com.example.twowaits.recyclerAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.example.twowaits.R
import com.example.twowaits.apiCalls.dashboardApiCalls.Answer
import com.example.twowaits.apiCalls.dashboardApiCalls.QnAResponseItem

class QuestionsAnswersRecyclerAdapter (
    private val adapter: String,
    private val questionsAndAnswers: MutableList<QnAResponseItem>,
    private val listener: ItemClicked):
        RecyclerView.Adapter<QuestionsAnswersRecyclerAdapter.QnA_ViewHolder>(), AnswerItemClicked {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QnA_ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.q_n_a, parent, false)
        return QnA_ViewHolder(view)
    }

    override fun onBindViewHolder(holder: QnA_ViewHolder, position: Int) {
        holder.apply {
            question.text = questionsAndAnswers[position].question
            answersRecyclerView.adapter = AnswersRecyclerAdapter(questionsAndAnswers[position].answer, this@QuestionsAnswersRecyclerAdapter)
            answersRecyclerView.isNestedScrollingEnabled = false
            answersRecyclerView.isNestedScrollingEnabled = false
            bookmarkBtn.isChecked = questionsAndAnswers[position].bookmarked_by_user == "True"
            if (adapter == "BOOKMARK") {
                bookmarkBtn.setOnClickListener {
                    listener.bookmarkBtnClicked(questionsAndAnswers[absoluteAdapterPosition].question_id)
                    questionsAndAnswers.removeAt(absoluteAdapterPosition)
                    notifyItemRemoved(absoluteAdapterPosition)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return questionsAndAnswers.size
    }

    inner class QnA_ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val question: TextView = itemView.findViewById(R.id.Question)
        val answersRecyclerView: RecyclerView = itemView.findViewById(R.id.answersRecyclerView)
        val bookmarkBtn: ToggleButton = itemView.findViewById(R.id.bookmarkBtn)
        val shareBtn: ImageView = itemView.findViewById(R.id.Share)
        val addAnswer: TextView = itemView.findViewById(R.id.addAnswer)
        init {
            question.setOnClickListener {
                listener.onQuestionClicked(question.text.toString())
            }
            bookmarkBtn.setOnClickListener {
                listener.bookmarkBtnClicked(questionsAndAnswers[absoluteAdapterPosition].question_id)
            }
            shareBtn.setOnClickListener {
                listener.shareBtnClicked(questionsAndAnswers[absoluteAdapterPosition].question,
                    questionsAndAnswers[absoluteAdapterPosition].answer)
            }
            addAnswer.setOnClickListener {
                listener.addAnswerClicked(questionsAndAnswers[absoluteAdapterPosition].question,
                    questionsAndAnswers[absoluteAdapterPosition].question_id, absoluteAdapterPosition)
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
}
