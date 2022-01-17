package com.example.twowaits.homePages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.twowaits.databinding.BookmarkedQuestionsBinding
import com.example.twowaits.databinding.YourQuestionsBinding
import com.example.twowaits.recyclerAdapters.YourQuestionsRecyclerAdapter
import com.example.twowaits.repository.dashboardRepositories.QnARepository

class BookmarkedQuestions: Fragment() {
    private var _binding: BookmarkedQuestionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BookmarkedQuestionsBinding.inflate(inflater, container, false)



        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}