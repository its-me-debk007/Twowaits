package com.example.twowaits.recyclerAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.example.twowaits.R

class QuizOptionsRecyclerAdapter(private val options: List<String>):
    RecyclerView.Adapter<QuizOptionsRecyclerAdapter.QuizOptionsViewHolder>() {
    class QuizOptionsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val radioButton: RadioButton = itemView.findViewById(R.id.radioButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizOptionsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.radio_buttons, parent, false)
        return QuizOptionsViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuizOptionsViewHolder, position: Int) {
        holder.apply {
            radioButton.text = options[position]
        }
    }

    override fun getItemCount(): Int {
        return options.size
    }
}