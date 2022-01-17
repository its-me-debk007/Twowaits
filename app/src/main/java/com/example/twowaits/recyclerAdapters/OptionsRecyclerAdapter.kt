package com.example.twowaits.recyclerAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.twowaits.R
import com.example.twowaits.homePages.quiz.OptionsDataClass

class OptionsRecyclerAdapter : ListAdapter<OptionsDataClass, OptionsRecyclerAdapter.ViewHolder>(DiffUtil()) {
//class OptionsRecyclerAdapter (private val optionsSerialNo: List<String>, private val options: List<String>): RecyclerView.Adapter<OptionsRecyclerAdapter.ViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.options, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.optionSerialNo.text = getItem(position).optionSerialNo
        holder.option.text = getItem(position).option
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val optionSerialNo: TextView = itemView.findViewById(R.id.OptionSerialNo)
        val option: TextView = itemView.findViewById(R.id.Option)
    }

    class DiffUtil: androidx.recyclerview.widget.DiffUtil.ItemCallback<OptionsDataClass>(){
        override fun areItemsTheSame(
            oldItem: OptionsDataClass,
            newItem: OptionsDataClass
        ): Boolean {
            return oldItem.optionSerialNo == newItem.optionSerialNo
        }

        override fun areContentsTheSame(
            oldItem: OptionsDataClass,
            newItem: OptionsDataClass
        ): Boolean {
            return oldItem == newItem
        }
    }

//    override fun getItemCount(): Int {
//        return options.size
//    }

}