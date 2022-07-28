package com.example.twowaits.recyclerAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.twowaits.R
import com.example.twowaits.network.dashboardApiCalls.Answer
import com.example.twowaits.databinding.AnswersBinding
import com.example.twowaits.util.formatTime

class AnswersRecyclerAdapter(
    private val answers: List<Answer>,
    private val listener: AnswerItemClicked,
    private val context: Context
) : RecyclerView.Adapter<AnswersRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: AnswersBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                likeBtn.setOnClickListener {
                    listener.likeBtnClicked(answers[absoluteAdapterPosition].answer_id)
                    if (likeBtn.isChecked) likesCount.text =
                        (likesCount.text.toString().toInt() + 1).toString()
                    else likesCount.text = (likesCount.text.toString().toInt() - 1).toString()
                }
                commentIcon.setOnClickListener {
                    if (listener.commentBtnClicked()) {
                        commentsRecyclerView.visibility = View.VISIBLE
                        addComment.visibility = View.VISIBLE
                    } else {
                        commentsRecyclerView.visibility = View.GONE
                        addComment.visibility = View.GONE
                    }
                }
                addComment.setOnClickListener {
                    listener.addCommentClicked(
                        answers[absoluteAdapterPosition].answer,
                        answers[absoluteAdapterPosition].answer_id, absoluteAdapterPosition
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AnswersBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            answerDetails.text = "Answered at ${formatTime(answers[position].answered)}"
            likesCount.text = answers[position].likes.toString()
            commentsCount.text = answers[position].comment.size.toString()
            answer.text = answers[position].answer
            likeBtn.isChecked = answers[position].liked_by_user == "True"
            commentsRecyclerView.adapter = CommentsRecyclerAdapter(answers[position].comment, context)
            commentsRecyclerView.isNestedScrollingEnabled = false
            commentsRecyclerView.isNestedScrollingEnabled = false
            try {
                answerer.text = answers[position].author_id.student.name
                Glide.with(context).load(answers[position].author_id.student.profile_pic_firebase)
                    .into(answererProfilePic)
            } catch (e: Exception) {
                try {
                    answerer.text = answers[position].author_id.faculty.name
                    Glide.with(context).load(answers[position].author_id.faculty.profile_pic_firebase)
                        .into(answererProfilePic)
                } catch (e: Exception) {
                    answerer.text = "Anonymous"
                    answererProfilePic.setImageResource(R.drawable.profile_pic_default)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return answers.size
    }
}

interface AnswerItemClicked {
    fun likeBtnClicked(question_id: Int)
    fun commentBtnClicked(): Boolean
    fun addCommentClicked(answer: String, answer_id: Int, position: Int)
}