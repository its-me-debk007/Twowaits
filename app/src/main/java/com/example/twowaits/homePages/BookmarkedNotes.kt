package com.example.twowaits.homePages

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.twowaits.Data
import com.example.twowaits.R
import com.example.twowaits.databinding.BookmarkedNotesBinding
import com.example.twowaits.recyclerAdapters.homePageRecyclerAdapters.NotesClicked
import com.example.twowaits.recyclerAdapters.homePageRecyclerAdapters.RecentNotesRecyclerAdapter
import com.example.twowaits.viewmodels.questionsAnswersViewModel.QuestionsAnswersViewModel
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class BookmarkedNotes: Fragment(), NotesClicked {
    private var _binding: BookmarkedNotesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BookmarkedNotesBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this)[QuestionsAnswersViewModel::class.java]
        viewModel.getBookmarkedNotes()
        viewModel.bookmarkedNotesData.observe(viewLifecycleOwner) {
            binding.BookmarkedNotesRecyclerView.adapter = RecentNotesRecyclerAdapter("BookmarkedNotes", it, this)
            binding.BookmarkedNotesRecyclerView.layoutManager =
                object : LinearLayoutManager(context) {
                    override fun canScrollVertically(): Boolean = false
                }
        }
        viewModel.errorBookmarkedNotesData.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onNotesClicked(pdfUri: Uri, noteName: String) {
        Data.NOTE_NAME = noteName
        Data.PDF_URI = pdfUri
        Data.PREVIOUS_PAGE = "BOOKMARK"
        findNavController().navigate(R.id.action_homePage_to_PDFViewer)
    }

    override fun onBookmarkNotesClicked(noteId: Int) {
        val viewModel = ViewModelProvider(this)[QuestionsAnswersViewModel::class.java]
        viewModel.bookmarkNote(BookmarkNoteBody(noteId))
        viewModel.bookmarkNoteData.observe(viewLifecycleOwner) {
            if (it != "success")
                Toast.makeText(
                    context,
                    "$it\nPlease try again after refreshing the page",
                    Toast.LENGTH_SHORT
                ).show()
        }
        viewModel.getBookmarkedNotes()
        viewModel.bookmarkedNotesData.observe(viewLifecycleOwner) {
            binding.BookmarkedNotesRecyclerView.adapter = RecentNotesRecyclerAdapter("BookmarkedNotes", it, this)
            Log.e("aaaa", "updated")
        }
    }
}