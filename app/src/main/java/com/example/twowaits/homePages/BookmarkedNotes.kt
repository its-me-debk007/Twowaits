package com.example.twowaits.homePages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.twowaits.databinding.BookmarkedNotesBinding
import com.example.twowaits.databinding.YourQuestionsBinding
import com.example.twowaits.recyclerAdapters.BookmarkedNotesRecyclerAdapter
import com.example.twowaits.recyclerAdapters.YourQuestionsRecyclerAdapter
import com.example.twowaits.repository.dashboardRepositories.QnARepository

class BookmarkedNotes: Fragment() {
    private var _binding: BookmarkedNotesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BookmarkedNotesBinding.inflate(inflater, container, false)

//        val repository = QnARepository()
//        repository.getQnA()
//        repository.q_n_aMutableLiveData.observe(viewLifecycleOwner, {
            binding.BookmarkedNotesRecyclerView.adapter = BookmarkedNotesRecyclerAdapter(7)
            binding.BookmarkedNotesRecyclerView.layoutManager = LinearLayoutManager(container?.context)
//        })
//        repository.errorMutableLiveData.observe(viewLifecycleOwner, {
//            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
//        })

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}