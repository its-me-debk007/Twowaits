package com.example.twowaits.homePages

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.twowaits.utils.Utils
import com.example.twowaits.NoteLectureActivity
import com.example.twowaits.R
import com.example.twowaits.databinding.BookmarkedNotesBinding
import com.example.twowaits.recyclerAdapters.homePageRecyclerAdapters.NotesClicked
import com.example.twowaits.recyclerAdapters.homePageRecyclerAdapters.RecentNotesRecyclerAdapter
import com.example.twowaits.viewmodels.questionsAnswersViewModel.QuestionsAnswersViewModel
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class BookmarkedNotes : Fragment(R.layout.bookmarked_notes), NotesClicked {
    private lateinit var binding: BookmarkedNotesBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = BookmarkedNotesBinding.bind(view)
        val viewModel = ViewModelProvider(this)[QuestionsAnswersViewModel::class.java]
        viewModel.getBookmarkedNotes()
        viewModel.bookmarkedNotesData.observe(viewLifecycleOwner) {
            if (it.isEmpty()) noItems()
            binding.BookmarkedNotesRecyclerView.adapter =
                RecentNotesRecyclerAdapter("BOOKMARK", it.toMutableList(), this)
            binding.BookmarkedNotesRecyclerView.layoutManager =
                object : LinearLayoutManager(context) {
                    override fun canScrollVertically(): Boolean = false
                }
        }
        viewModel.errorBookmarkedNotesData.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onNotesClicked(pdfUri: String, noteName: String) {
        Utils.PDF_URI = pdfUri
        val intent = Intent(context, NoteLectureActivity::class.java)
        intent.apply {
            intent.putExtra("PREVIOUS PAGE", "BOOKMARK")
            intent.putExtra("PAGE TYPE", "NOTE")
            intent.putExtra("NOTE NAME", noteName)
        }
        startActivity(intent)
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
    }

    override fun noItems() {
        binding.BookmarkedNotesRecyclerView.visibility = View.GONE
        binding.emptyAnimation.visibility = View.VISIBLE
        binding.text.visibility = View.VISIBLE
    }
}