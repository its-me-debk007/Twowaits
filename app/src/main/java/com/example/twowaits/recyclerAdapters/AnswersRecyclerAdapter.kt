package com.example.twowaits.recyclerAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.borjabravo.readmoretextview.ReadMoreTextView
import com.example.twowaits.CompanionObjects
import com.example.twowaits.R
import com.example.twowaits.apiCalls.dashboardApiCalls.Answer
import com.example.twowaits.apiCalls.dashboardApiCalls.QnAResponseItem

class AnswersRecyclerAdapter(private val answers: List<Answer>, private val listener: AnswerItemClicked) :
    RecyclerView.Adapter<AnswersRecyclerAdapter.AnswersViewHolder>() {
    inner class AnswersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val answerer: TextView = itemView.findViewById(R.id.Answerer)
        val answer: ReadMoreTextView = itemView.findViewById(R.id.Answer)
        val answerDetails: TextView = itemView.findViewById(R.id.AnswerDetails)
        val answererProfilePic: ImageView = itemView.findViewById(R.id.AnswererProfilePic)
        val likesCount: TextView = itemView.findViewById(R.id.LikeCount)
        val commentsCount: TextView = itemView.findViewById(R.id.CommentCount)
        val likeBtn: ToggleButton = itemView.findViewById(R.id.Like)
        val commentBtn: ImageView = itemView.findViewById(R.id.Comment)

        init {
            likeBtn.setOnClickListener {
                listener.likeBtnClicked(answers[adapterPosition].answer_id)
//                likesCount.text = (answers[adapterPosition].likes + 1).toString()
            }
            commentBtn.setOnClickListener {
                listener.commentBtnClicked(answers[adapterPosition].answer_id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswersViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.answers, parent, false)
        return AnswersViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnswersViewHolder, position: Int) {
        val monthNumber = answers[position].answered.subSequence(5, 7)
        val day = answers[position].answered.subSequence(8, 10)
        val hours = answers[position].answered.subSequence(11, 13)
        val minutes = answers[position].answered.subSequence(14, 16)

        holder.apply {
            answerDetails.text = CompanionObjects.properTimeFormat(
                monthNumber.toString(),
                day.toString(),
                hours.toString(),
                minutes.toString()
            )
            likesCount.text = answers[position].likes.toString()
            commentsCount.text = answers[position].comment.size.toString()
            answer.text = answers[position].answer
            try {
                answerer.text = answers[position].author_id.student.name
                answererProfilePic.load(answers[position].author_id.student.profile_pic) {
                    transformations(CircleCropTransformation())
                }
            } catch (e: Exception) {
                answerer.text = "Anonymous"
                answererProfilePic.setImageResource(R.drawable.profile_pic_default)
            }
        }
    }

    override fun getItemCount(): Int {
        return answers.size
    }
}
interface AnswerItemClicked {
    fun likeBtnClicked(question_id: Int)
    fun commentBtnClicked(question_id: Int)
}