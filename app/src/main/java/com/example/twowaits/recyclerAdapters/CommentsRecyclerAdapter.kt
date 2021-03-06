package com.example.twowaits.recyclerAdapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.twowaits.utils.Utils
import com.example.twowaits.R
import com.example.twowaits.network.dashboardApiCalls.Comment
import com.example.twowaits.databinding.CommentsBinding

class CommentsRecyclerAdapter(private val comments: List<Comment>, private val context: Context) :
    RecyclerView.Adapter<CommentsRecyclerAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: CommentsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(CommentsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            commentedAt.text = "Commented at ${Utils().formatTime(comments[position].commented)}"
            try {
                commentorName.text = comments[position].author_id.student.name
                Glide.with(context).load(comments[position].author_id.student.profile_pic_firebase)
                    .into(commentorProfilePic)
            } catch (e: Exception) {
                try {
                    commentorName.text = comments[position].author_id.faculty.name
                    Glide.with(context).load(comments[position].author_id.faculty.profile_pic_firebase)
                        .into(commentorProfilePic)
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