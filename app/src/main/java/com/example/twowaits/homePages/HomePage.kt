package com.example.twowaits.homePages

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.example.twowaits.Data
import com.example.twowaits.R
import com.example.twowaits.apiCalls.dashboardApiCalls.AddToWishlistBody
import com.example.twowaits.apiCalls.dashboardApiCalls.Answer
import com.example.twowaits.databinding.CreateAnswerBinding
import com.example.twowaits.databinding.HomePageBinding
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
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent.setEventListener
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener


@DelicateCoroutinesApi
class HomePage: Fragment(), ItemClicked, QuizClicked, NotesClicked, LecturesClicked {
    private var _binding: HomePageBinding? = null
    private val binding get() = _binding!!
    private var isClicked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomePageBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this)[HomePageViewModel::class.java]
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        viewModel.recentLectures()
        viewModel.recentLecturesLiveData.observe(viewLifecycleOwner) {
            binding.TopLecturesRecyclerView.adapter =
                RecentLecturesRecyclerAdapter("HomePage", it.size, it, this)
            binding.TopLecturesRecyclerView.layoutManager = object:
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false) {
                override fun canScrollVertically(): Boolean = false
                }
        }
        viewModel.errorRecentLecturesLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.recentNotes()
        viewModel.recentNotesLiveData.observe(viewLifecycleOwner) {
            binding.recentNotesRecyclerView.adapter =
                RecentNotesRecyclerAdapter("HomePage", it, this)
            binding.recentNotesRecyclerView.layoutManager = object:
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false) {
                override fun canScrollVertically(): Boolean  = false
                }
        }
        viewModel.errorRecentNotesLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.recentQuizzes()
        viewModel.recentQuizzesLiveData.observe(viewLifecycleOwner) {
            binding.QuizzesRecyclerView.adapter = QuizzesRecyclerAdapter(it.size, it, this)
            binding.QuizzesRecyclerView.layoutManager = object:
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false){
                override fun canScrollVertically(): Boolean = false
                }
        }
        viewModel.errorRecentQuizzesLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.getQnA()
        viewModel.getQnALiveData.observe(viewLifecycleOwner) {
            binding.QnARecyclerView.adapter = QuestionsAnswersRecyclerAdapter(it.size, it, this)
            binding.QnARecyclerView.layoutManager = object : LinearLayoutManager(context) {
                override fun canScrollVertically(): Boolean = false
            }
        }
        viewModel.errorGetQnALiveData.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

        binding.QnA.setOnClickListener {
            findNavController().navigate(R.id.action_homePage_to_moreQnA2)
        }
        binding.arrow.setOnClickListener {
            findNavController().navigate(R.id.action_homePage_to_moreQnA2)
        }

        binding.StudentSuggestionRecyclerView.adapter = StudentsSuggestionRecyclerAdapter()
        binding.StudentSuggestionRecyclerView.layoutManager = object:
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false) {
            override fun canScrollVertically(): Boolean = false
            }

        binding.swipeToRefresh.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                Data.isSearchBarActiveLiveData.postValue(false)
                findNavController().navigate(R.id.action_homePage_self)
            }, 440)
        }
        setEventListener(requireActivity(),
            KeyboardVisibilityEventListener {
                if (!it) bottomNavigationView?.visibility = View.VISIBLE
            })
        Data.isSearchBarActiveLiveData.observe(viewLifecycleOwner) {
            if (it) {
                binding.searchQ.error = null
                binding.textInputLayout.visibility = View.VISIBLE
                binding.searchButton.visibility = View.VISIBLE
                binding.searchQ.requestFocus()
                showKeyboard(binding.searchQ)
                bottomNavigationView?.visibility = View.GONE
            } else {
                binding.textInputLayout.visibility = View.GONE
                binding.searchButton.visibility = View.GONE
                hideKeyboard(requireView())
                bottomNavigationView?.visibility = View.VISIBLE
            }
        }

        binding.searchButton.setOnClickListener {
            if (binding.searchQ.text.toString().trim().isEmpty()) {
                binding.searchQ.error = "Please enter a question in order to search"
                return@setOnClickListener
            }
            Data.Q_SEARCHED = binding.searchQ.text.toString().trim()
            findNavController().navigate(R.id.action_homePage_to_showSearchQuestions)
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showKeyboard(view: View) {
        val imm = activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideKeyboard(view: View) {
        val imm = activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

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
        viewModel.errorBookmarkQuestionLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    override fun shareBtnClicked(question: String, answersList: List<Answer>) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        var answerFormat = ""
        for (i in answersList.indices) {
            answerFormat += "A${i + 1}) ${answersList[i].answer}"
            answerFormat += if (answersList[i].comment.isNotEmpty()) "\n" + "\t\tComments:\n"
            else "\n\n"
            var commentFormat = ""
            for (j in answersList[i].comment.indices) {
                commentFormat += "\t\t${j + 1}) ${answersList[i].comment[j].comment}"
                commentFormat += if (j == answersList[i].comment.size - 1) "\n\n"
                else "\n"
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
            if (dialog.findViewById<TextInputEditText>(R.id.answerOfQ).text.toString().trim()
                    .isEmpty()
            ) {
                dialog.findViewById<TextInputLayout>(R.id.questionLayout).helperText =
                    "Please enter your answer first"
                return@setOnClickListener
            }
            dialog.findViewById<TextInputLayout>(R.id.questionLayout).helperText = ""
            viewModel.createAnswer(
                CreateAnswerBody(
                    dialog.findViewById<TextInputEditText>(R.id.answerOfQ).text.toString().trim(),
                    question_id
                )
            )
            dialog.findViewById<LottieAnimationView>(R.id.ProgressBar).visibility = View.VISIBLE
            viewModel.createAnswerData.observe(viewLifecycleOwner) {
                if (it == "success") {
                    Toast.makeText(context, "Added your answer successfully", Toast.LENGTH_SHORT)
                        .show()
                    dialog.cancel()
                } else {
                    dialog.findViewById<LottieAnimationView>(R.id.ProgressBar).visibility =
                        View.GONE
                    Toast.makeText(context, "Please try again!\n$it", Toast.LENGTH_SHORT).show()
                }
            }
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
            if (dialog.findViewById<TextInputEditText>(R.id.answerOfQ).text.toString().trim()
                    .isEmpty()
            ) {
                dialog.findViewById<TextInputLayout>(R.id.questionLayout).helperText =
                    "Please enter your comment first"
                return@setOnClickListener
            }
            dialog.findViewById<TextInputLayout>(R.id.questionLayout).helperText = ""
            viewModel.createComment(
                CreateCommentBody(
                    answer_id,
                    dialog.findViewById<TextInputEditText>(R.id.answerOfQ).text.toString().trim()
                )
            )
            dialog.findViewById<LottieAnimationView>(R.id.ProgressBar).visibility = View.VISIBLE
            viewModel.createCommentData.observe(viewLifecycleOwner) {
                if (it == "success") {
                    Toast.makeText(context, "Added your comment successfully", Toast.LENGTH_SHORT)
                        .show()
                    dialog.cancel()
                } else {
                    dialog.findViewById<LottieAnimationView>(R.id.ProgressBar).visibility =
                        View.GONE
                    Toast.makeText(context, "Please try again!\n$it", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun likeBtnClicked(question_id: Int) {
        val viewModel = ViewModelProvider(this)[QuestionsAnswersViewModel::class.java]
        viewModel.likeAnswer(LikeAnswerBody(question_id))
        viewModel.errorLikeAnswerLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onQuizClicked(quiz_id: Int) {
        findNavController().navigate(R.id.action_homePage_to_quiz)
        Data.QUIZ_ID = quiz_id
    }

    override fun onNotesClicked(pdfUri: Uri, noteName: String) {
        Data.NOTE_NAME = noteName
        Data.PDF_URI = pdfUri
        Data.PREVIOUS_PAGE = "HOME"
        findNavController().navigate(R.id.action_homePage_to_PDFViewer)
    }

    override fun onBookmarkNotesClicked(note_id: Int) {
        val viewModel = ViewModelProvider(this)[QuestionsAnswersViewModel::class.java]
        viewModel.bookmarkNote(BookmarkNoteBody(note_id))
        viewModel.bookmarkNoteData.observe(viewLifecycleOwner) {
            if (it != "success")
                Toast.makeText(
                    context,
                    it,
                    Toast.LENGTH_SHORT
                ).show()
        }
    }

    override fun onLectureClicked(videoUri: Uri, lectureName: String) {
        Data.VIDEO_URI = videoUri
        Data.PREV_PAGE_FOR_PLAYER = "HOME"
        Data.LECTURE_NAME = lectureName
        findNavController().navigate(R.id.action_homePage_to_videoPlayer2)
    }

    override fun onWishlistBtnClicked(lecture_id: Int) {
        val viewModel = ViewModelProvider(this)[QuestionsAnswersViewModel::class.java]
        viewModel.addToWishlist(AddToWishlistBody(lecture_id))
        viewModel.addToWishlistData.observe(viewLifecycleOwner) {
            if (it != "success")
                Toast.makeText(
                    context,
                    it,
                    Toast.LENGTH_SHORT
                ).show()
        }
    }
}