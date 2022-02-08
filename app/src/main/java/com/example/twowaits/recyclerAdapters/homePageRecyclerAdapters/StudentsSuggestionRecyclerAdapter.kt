package com.example.twowaits.recyclerAdapters.homePageRecyclerAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.twowaits.R

class StudentsSuggestionRecyclerAdapter:
    RecyclerView.Adapter<StudentsSuggestionRecyclerAdapter.StudentSuggestionViewHolder>() {

    class StudentSuggestionViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val profilePicOfStudent: ImageView = itemView.findViewById(R.id.ProfilePic)
        val nameOfStudent: TextView = itemView.findViewById(R.id.NameOfStudent)
        val studentDetails: TextView = itemView.findViewById(R.id.StudentDetails)
        val studentCollege: TextView = itemView.findViewById(R.id.StudentCollege)
        val messageButton: Button = itemView.findViewById(R.id.MessageButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentSuggestionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.student_suggestion, parent, false)
        return StudentSuggestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentSuggestionViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 5
    }
}