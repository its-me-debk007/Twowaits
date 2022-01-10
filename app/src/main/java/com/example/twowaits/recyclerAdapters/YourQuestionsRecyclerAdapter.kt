package com.example.twowaits.recyclerAdapters

import android.util.Log
import android.view.LayoutInflater
import android.view.OnReceiveContentListener
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.borjabravo.readmoretextview.ReadMoreTextView
import com.example.twowaits.CompanionObjects
import com.example.twowaits.R
import com.example.twowaits.apiCalls.dashboardApiCalls.QnAResponseItem
import coil.load
import coil.transform.CircleCropTransformation
import java.lang.Exception

class YourQuestionsRecyclerAdapter (private val QnA: List<QnAResponseItem>, private val size: Int, private val listener: ItemClicked):
        RecyclerView.Adapter<YourQuestionsRecyclerAdapter.QnA_ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QnA_ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.q_n_a, parent, false)
        view.setOnClickListener {
            listener.onItemClicked(QnA[QnA_ViewHolder(view).adapterPosition])
        }
        return  QnA_ViewHolder(view)
    }

    override fun onBindViewHolder(holder: QnA_ViewHolder, position: Int) {
//        holder.question.text = questionsAndAnswers[position].question
//        holder.answer.text = questionsAndAnswers[position].answer[0].answer
//
//        try {
//            holder.answerer.text = questionsAndAnswers[position].answer[0].author_id.student.name
//            holder.answererProfilePic.load(questionsAndAnswers[position].answer[0].author_id.student.profile_pic) {
//                transformations(CircleCropTransformation())
//            }
//        } catch (e: Exception){
//            holder.answerer.text = "Anonymous"
//            holder.answererProfilePic.setImageResource(R.drawable.profile_pic_default)
//        }
//
//        val monthNumber = questionsAndAnswers[position].answer[0].answered.subSequence(5, 7)
//        val day = questionsAndAnswers[position].answer[0].answered.subSequence(8, 10)
//        val hours = questionsAndAnswers[position].answer[0].answered.subSequence(11, 13)
//        val minutes = questionsAndAnswers[position].answer[0].answered.subSequence(14, 16)
//
//        holder.answerDetails.text = CompanionObjects.properTimeFormat(monthNumber.toString(), day.toString(), hours.toString(), minutes.toString())
//        holder.likesCount.text = questionsAndAnswers[position].answer[0].likes.toString()
//        holder.commentsCount.text = questionsAndAnswers[position].answer[0].comment.size.toString()
    }

    override fun getItemCount(): Int {
        return size
    }

    inner class QnA_ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){  //Nested Class
//        val answerer: TextView = itemView.findViewById(R.id.Answerer)
//        val question: TextView = itemView.findViewById(R.id.Question)
//        val answer: ReadMoreTextView = itemView.findViewById(R.id.Answer)
//        val answerDetails: TextView = itemView.findViewById(R.id.AnswerDetails)
//        val answererProfilePic: ImageView = itemView.findViewById(R.id.AnswererProfilePic)
//        val likesCount: TextView = itemView.findViewById(R.id.LikeCount)
//        val commentsCount: TextView = itemView.findViewById(R.id.CommentCount)
//        val likeBtn: ToggleButton = itemView.findViewById(R.id.Like)
//        val commentBtn: ToggleButton = itemView.findViewById(R.id.Comment)
    }
}
interface ItemClicked {
    fun onItemClicked(item: QnAResponseItem)
}
