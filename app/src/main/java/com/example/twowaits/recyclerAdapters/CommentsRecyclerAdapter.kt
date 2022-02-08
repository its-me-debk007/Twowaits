package com.example.twowaits.recyclerAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.twowaits.Data
import com.example.twowaits.R
import com.example.twowaits.apiCalls.dashboardApiCalls.Comment

class CommentsRecyclerAdapter(private val comments: List<Comment>): RecyclerView.Adapter<CommentsRecyclerAdapter.CommentsViewHolder>() {
    inner class CommentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val commentorProfilePic: ImageView = itemView.findViewById(R.id.commentorProfilePic)
        val commentorName: TextView = itemView.findViewById(R.id.commentorName)
        val commentText: TextView = itemView.findViewById(R.id.commentText)
        val commentedAt: TextView = itemView.findViewById(R.id.commentedAt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comments, parent, false)
        return CommentsViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        val monthNumber = comments[position].commented.subSequence(5, 7)
        val day = comments[position].commented.subSequence(8, 10)
        val hours = comments[position].commented.subSequence(11, 13)
        val minutes = comments[position].commented.subSequence(14, 16)
        holder.apply {
            commentedAt.text = "Commented at ${Data.properTimeFormat(
                monthNumber.toString(),
                day.toString(),
                hours.toString(),
                minutes.toString())}"
            try {
                commentorName.text = comments[position].author_id.student.name
                commentorProfilePic.load(comments[position].author_id.student.profile_pic) {
                    transformations(CircleCropTransformation())
                }
            } catch (e: Exception) {
                try {
                    commentorName.text = comments[position].author_id.faculty.name
                    commentorProfilePic.load(comments[position].author_id.faculty.profile_pic) {
                        transformations(CircleCropTransformation())
                    }
                } catch (e: Exception) {
                    commentorName.text = "Anonymous"
                    commentorProfilePic.setImageResource(R.drawable.profile_pic_default)
                }
            }
            commentText.text = comments[position].comment
        }
    }

    override fun getItemCount(): Int {
        return comments.size
    }
}