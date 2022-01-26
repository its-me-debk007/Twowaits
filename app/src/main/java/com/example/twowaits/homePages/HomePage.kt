package com.example.twowaits.homePages

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.example.twowaits.CompanionObjects
import com.example.twowaits.R
import com.example.twowaits.apiCalls.dashboardApiCalls.Answer
import com.example.twowaits.databinding.CreateAnswerBinding
import com.example.twowaits.databinding.HomePageBinding
import com.example.twowaits.databinding.PleaseWaitDialogBinding
import com.example.twowaits.homePages.questionsAnswers.BookmarkQuestionBody
import com.example.twowaits.homePages.questionsAnswers.CreateAnswerBody
import com.example.twowaits.homePages.questionsAnswers.CreateCommentBody
import com.example.twowaits.homePages.questionsAnswers.LikeAnswerBody
import com.example.twowaits.recyclerAdapters.ItemClicked
import com.example.twowaits.recyclerAdapters.QuestionsAnswersRecyclerAdapter
import com.example.twowaits.recyclerAdapters.homePageRecyclerAdapters.*
import com.example.twowaits.viewmodels.HomePageViewModel
import com.example.twowaits.viewmodels.questionsAnswersViewModel.QuestionsAnswersViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class HomePage : Fragment(), ItemClicked, QuizClicked, NotesClicked {
    private var _binding: HomePageBinding? = null
    private val binding get() = _binding!!
    private var isClicked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomePageBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this)[HomePageViewModel::class.java]

        binding.TopLecturesRecyclerView.adapter = RecentLecturesRecyclerAdapter(4)
        binding.TopLecturesRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        viewModel.recentNotes()
        viewModel.recentNotesLiveData.observe(viewLifecycleOwner, {
            binding.recentNotesRecyclerView.adapter = RecentNotesRecyclerAdapter(it.size, it, this)
            binding.recentNotesRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        })
        viewModel.errorRecentNotesLiveData.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

        viewModel.recentQuizzes()
        viewModel.recentQuizzesLiveData.observe(viewLifecycleOwner, {
            binding.QuizzesRecyclerView.adapter = QuizzesRecyclerAdapter(it.size, it, this)
            binding.QuizzesRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        })
        viewModel.errorRecentQuizzesLiveData.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })

        viewModel.getQnA()
        viewModel.getQnALiveData.observe(viewLifecycleOwner, {
            binding.QnARecyclerView.adapter = QuestionsAnswersRecyclerAdapter(it.size, it, this)
            binding.QnARecyclerView.layoutManager = object : LinearLayoutManager(context) {
                override fun canScrollVertically(): Boolean = false
            }
        })
        viewModel.errorGetQnALiveData.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
//        binding.TopNotes.setOnClickListener {
//            findNavController().navigate(R.id.)
//        }
//        binding.notesArrow.setOnClickListener {
//            findNavController().navigate(R.id.)
//        }
        binding.QnA.setOnClickListener {
            findNavController().navigate(R.id.action_homePage_to_moreQnA2)
        }
        binding.arrow.setOnClickListener {
            findNavController().navigate(R.id.action_homePage_to_moreQnA2)
        }

        binding.StudentSuggestionRecyclerView.adapter = StudentsSuggestionRecyclerAdapter()
        binding.StudentSuggestionRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        binding.swipeToRefresh.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                CompanionObjects.isSearchBarActiveLiveData.postValue(false)
                findNavController().navigate(R.id.action_homePage_self)
            }, 440)
        }
        CompanionObjects.isSearchBarActiveLiveData.observe(viewLifecycleOwner, {
            if (it) {
                binding.searchQ.error = null
                binding.textInputLayout.visibility = View.VISIBLE
                binding.searchButton.visibility = View.VISIBLE
//                showSoftKeyboard(binding.SearchBar)
            } else {
                binding.textInputLayout.visibility = View.GONE
                binding.searchButton.visibility = View.GONE
            }
        })

        binding.searchButton.setOnClickListener {
            if (binding.searchQ.text.toString().trim().isEmpty()){
                binding.searchQ.error = "Please enter a question in order to search"
                return@setOnClickListener
            }
            CompanionObjects.Q_SEARCHED = binding.searchQ.text.toString().trim()
            findNavController().navigate(R.id.action_homePage_to_showSearchQuestions)
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

//    private fun showSoftKeyboard(view: View) {
//        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
//    }
//    private fun hideKeyboard(view: View) {
//        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.hideSoftInputFromWindow(view.windowToken, 0)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                exitConfirmation()
            }
        })
    }

    private fun exitConfirmation() {
        AlertDialog.Builder(context)
            .setTitle("Exit")
            .setMessage("Are you sure want to exit?")
            .setIcon(R.drawable.exit_warning)
            .setPositiveButton("Yes") { _, _ ->
                activity?.finish()
            }
            .setNegativeButton("No") { _, _ ->
            }
            .create()
            .show()
    }

    override fun onQuestionClicked(question: String) {

    }

    override fun commentBtnClicked(): Boolean {
        isClicked = !isClicked
        return isClicked
    }

    override fun bookmarkBtnClicked(question_id: Int) {
        val viewModel = ViewModelProvider(this)[QuestionsAnswersViewModel::class.java]
        viewModel.bookmarkQuestion(BookmarkQuestionBody(question_id))
//        viewModel.bookmarkQuestionLiveData.observe(viewLifecycleOwner, {

//        })
        viewModel.errorBookmarkQuestionLiveData.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
    }

    override fun shareBtnClicked(question: String, answersList: List<Answer>) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        var answerFormat = ""
        for (i in answersList.indices) {
            answerFormat += "A${i+1}) ${answersList[i].answer}"
            answerFormat += if (answersList[i].comment.isNotEmpty()) "\n" + "\t\tComments:\n"
            else "\n\n"
            var commentFormat = ""
            for (j in answersList[i].comment.indices){
                commentFormat += "\t\t${j+1}) ${answersList[i].comment[j].comment}"
                if (j == answersList[i].comment.size-1)  commentFormat += "\n\n"
                else commentFormat += "\n"
            }
            answerFormat += commentFormat
        }
        val format = "Q) $question\n\n$answerFormat"
        shareIntent.putExtra(Intent.EXTRA_TEXT, format)
        val chooser = Intent.createChooser(shareIntent, "Share this QnA using...")
        startActivity(chooser)
    }

    override fun addAnswerClicked(question: String, question_id: Int) {
        val viewModel = ViewModelProvider(this)[QuestionsAnswersViewModel::class.java]
        val dialog = Dialog(requireContext())
        dialog.setContentView(CreateAnswerBinding.inflate(layoutInflater).root)
        dialog.show()
        if (dialog.window != null)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        dialog.findViewById<TextView>(R.id.particularQuestion).text = question
        dialog.findViewById<Button>(R.id.answerButton).setOnClickListener {
            if (dialog.findViewById<TextInputEditText>(R.id.answerOfQ).text.toString().trim().isEmpty()){
                dialog.findViewById<TextInputLayout>(R.id.questionLayout).helperText = "Please enter your answer first"
                return@setOnClickListener
            }
            dialog.findViewById<TextInputLayout>(R.id.questionLayout).helperText = ""
            viewModel.createAnswer(CreateAnswerBody(dialog.findViewById<TextInputEditText>(R.id.answerOfQ).text.toString().trim(),
                question_id))
            dialog.findViewById<LottieAnimationView>(R.id.ProgressBar).visibility = View.VISIBLE
            viewModel.createAnswerData.observe(viewLifecycleOwner, {
                if (it == "success") {
                    Toast.makeText(context, "Added your answer successfully", Toast.LENGTH_SHORT).show()
                    dialog.cancel()
                } else {
                    dialog.findViewById<LottieAnimationView>(R.id.ProgressBar).visibility = View.GONE
                    Toast.makeText(context, "Please try again!\n$it", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun addCommentClicked(answer: String, answer_id: Int) {
        val viewModel = ViewModelProvider(this)[QuestionsAnswersViewModel::class.java]
        val dialog = Dialog(requireContext())
        dialog.setContentView(CreateAnswerBinding.inflate(layoutInflater).root)
        dialog.show()
        if (dialog.window != null)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        dialog.findViewById<TextView>(R.id.particularQuestion).text = answer
        dialog.findViewById<Button>(R.id.answerButton).setOnClickListener {
            if (dialog.findViewById<TextInputEditText>(R.id.answerOfQ).text.toString().trim().isEmpty()){
                dialog.findViewById<TextInputLayout>(R.id.questionLayout).helperText = "Please enter your comment first"
                return@setOnClickListener
            }
            dialog.findViewById<TextInputLayout>(R.id.questionLayout).helperText = ""
            viewModel.createComment(
                CreateCommentBody(answer_id, dialog.findViewById<TextInputEditText>(R.id.answerOfQ).text.toString().trim()))
            dialog.findViewById<LottieAnimationView>(R.id.ProgressBar).visibility = View.VISIBLE
            viewModel.createCommentData.observe(viewLifecycleOwner, {
                if (it == "success") {
                    Toast.makeText(context, "Added your comment successfully", Toast.LENGTH_SHORT).show()
                    dialog.cancel()
                } else {
                    dialog.findViewById<LottieAnimationView>(R.id.ProgressBar).visibility = View.GONE
                    Toast.makeText(context, "Please try again!\n$it", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun likeBtnClicked(question_id: Int) {
        val viewModel = ViewModelProvider(this)[QuestionsAnswersViewModel::class.java]
        viewModel.likeAnswer(LikeAnswerBody(question_id))
//        viewModel.likeAnswerLiveData.observe(viewLifecycleOwner, {

//        })
        viewModel.errorLikeAnswerLiveData.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onQuizClicked(quiz_id: Int) {
        findNavController().navigate(R.id.action_homePage_to_quiz)
        CompanionObjects.QUIZ_ID = quiz_id
    }

    override fun onNotesClicked(note_id: Int) {
        Log.d("FFFF","CLICKED")
    }

//    override fun onNotesClicked(pdfUri: Uri) {
//        Log.d("AAAA", "Clicked")
//        CompanionObjects.PDF_URI = pdfUri
//        findNavController().navigate(R.id.action_homePage_to_PDFViewer)
//    }

//    override fun onbookmarkNotesClicked(notes_id: Int) {
//        val viewModel = ViewModelProvider(this)[QuestionsAnswersViewModel::class.java]
//        viewModel.bookmarkNote(BookmarkNoteBody(notes_id))
//        viewModel.bookmarkNoteData.observe(viewLifecycleOwner, {
//            if (it != "success")
//            Toast.makeText(context, "$it\nPlease try again after refreshing the page", Toast.LENGTH_SHORT).show()
//        })
//    }
}