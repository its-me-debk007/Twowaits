package com.example.twowaits.homePages

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.twowaits.R
import com.example.twowaits.databinding.HomePageBinding
import com.example.twowaits.recyclerAdapters.homePageRecyclerAdapters.*

class HomePage : Fragment() {
    private var _binding: HomePageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HomePageBinding.inflate(inflater, container, false)

        binding.TopNotesRecyclerView.adapter = TopNotesRecyclerAdapter()
        binding.TopNotesRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        binding.TopLecturesRecyclerView.adapter = TopLecturesRecyclerAdapter()
        binding.TopLecturesRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        binding.QuizzesRecyclerView.adapter = QuizzesRecyclerAdapter()
        binding.QuizzesRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        binding.QnARecyclerView.adapter = QnARecyclerAdapter()
        binding.QnARecyclerView.layoutManager = LinearLayoutManager(context)

        binding.StudentSuggestionRecyclerView.adapter = StudentsSuggestionRecyclerAdapter()
        binding.StudentSuggestionRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun exitConfirmation() {
        AlertDialog.Builder(context)
            .setTitle("Exit")
            .setMessage("Are you sure you want to exit?")
            .setIcon(R.drawable.exit_warning)
            .setPositiveButton("Yes", ){
                    _, _ -> activity?.finish()
            }
            .setNegativeButton("No"){
                    _, _ ->
            }
            .create()
            .show()
    }
}