package com.example.twowaits.homePages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.twowaits.R
import com.example.twowaits.apiCalls.dashboardApiCalls.QnAResponseItem
import com.example.twowaits.databinding.ProfileBinding
import com.example.twowaits.databinding.YourQuestionsBinding
import com.example.twowaits.recyclerAdapters.ItemClicked
import com.example.twowaits.recyclerAdapters.YourQuestionsRecyclerAdapter
import com.example.twowaits.repository.dashboardRepositories.QnARepository
import com.example.twowaits.viewmodels.ProfileDetailsViewModel
import com.example.twowaits.viewmodels.YourQuestionsViewModel
import com.google.android.material.snackbar.Snackbar

class YourQuestions : Fragment(), ItemClicked{
    private var _binding: YourQuestionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = YourQuestionsBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this)[YourQuestionsViewModel::class.java]

        viewModel.getQnA()
        viewModel.q_n_aLiveData.observe(viewLifecycleOwner, {
            binding.YourQnARecyclerView.adapter = YourQuestionsRecyclerAdapter( it, this)
            binding.YourQnARecyclerView.layoutManager = LinearLayoutManager(container?.context)
        })
        viewModel.errorLiveData.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemClicked(item: QnAResponseItem) {
    }

    override fun onQuestionClicked(question: String) {
        Snackbar.make(binding.YourQnARecyclerView, question, Snackbar.LENGTH_SHORT).show()
    }
}