package com.example.twowaits.ui.fragment

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.Image
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.twowaits.NoteLectureActivity
import com.example.twowaits.R
import com.example.twowaits.databinding.FragmentHomeBinding
import com.example.twowaits.homePages.questionsAnswers.BookmarkQuestionBody
import com.example.twowaits.homePages.questionsAnswers.CreateAnswerBody
import com.example.twowaits.homePages.questionsAnswers.CreateCommentBody
import com.example.twowaits.homePages.questionsAnswers.LikeAnswerBody
import com.example.twowaits.model.BookmarkNoteBody
import com.example.twowaits.network.dashboardApiCalls.AddToWishlistBody
import com.example.twowaits.network.dashboardApiCalls.Answer
import com.example.twowaits.recyclerAdapters.ItemClicked
import com.example.twowaits.recyclerAdapters.QuestionsAnswersRecyclerAdapter
import com.example.twowaits.recyclerAdapters.homePageRecyclerAdapters.*
import com.example.twowaits.sealedClass.Response
import com.example.twowaits.ui.activity.home.AskActivity
import com.example.twowaits.ui.activity.home.QuizActivity
import com.example.twowaits.util.Utils
import com.example.twowaits.util.hideKeyboard
import com.example.twowaits.viewModel.HomePageViewModel
import com.example.twowaits.viewModel.questionsAnswersViewModel.QuestionsAnswersViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import com.google.android.material.transition.platform.MaterialFadeThrough
import kotlinx.coroutines.DelicateCoroutinesApi
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent.setEventListener
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener

@DelicateCoroutinesApi
class HomeFragment : Fragment(R.layout.fragment_home), ItemClicked, QuizClicked, NotesClicked,
    LecturesClicked {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: QuestionsAnswersRecyclerAdapter
    private val viewModel by lazy { ViewModelProvider(this)[HomePageViewModel::class.java] }
    private val bottomNavigationView by lazy {
        activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
    }
    private var isCommentIconClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialFadeThrough()
        enterTransition = MaterialFadeThrough()
        reenterTransition = MaterialFadeThrough()
        returnTransition = MaterialFadeThrough()

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                exitConfirmation()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)
        binding.swipeToRefresh.setColorSchemeColors(Color.parseColor("#804D37"))
        activity?.invalidateOptionsMenu()

        viewModel.recentLectures()
        viewModel.recentLecturesLiveData.observe(viewLifecycleOwner) {
            if (it is Response.Success) {
                binding.TopLecturesRecyclerView.adapter =
                    RecentLecturesRecyclerAdapter("HOME", it.data!!.toMutableList(), this)
                binding.TopLecturesRecyclerView.layoutManager = object :
                    LinearLayoutManager(context, HORIZONTAL, false) {
                    override fun canScrollVertically(): Boolean = false
                }
            } else Log.e("dddd", it.errorMessage!!)
        }

        viewModel.recentNotes()
        viewModel.recentNotesLiveData.observe(viewLifecycleOwner) {
            if (it is Response.Success) {
                binding.recentNotesRecyclerView.adapter =
                    RecentNotesRecyclerAdapter("HOME", it.data!!.toMutableList(), this)
                binding.recentNotesRecyclerView.layoutManager = object :
                    LinearLayoutManager(context, HORIZONTAL, false) {
                    override fun canScrollVertically(): Boolean = false
                }
            } else Log.e("dddd", it.errorMessage!!)
        }

        viewModel.recentQuizzes()
        viewModel.recentQuizzesLiveData.observe(viewLifecycleOwner) {
            if (it is Response.Success) {
                binding.QuizzesRecyclerView.adapter =
                    QuizzesRecyclerAdapter(it.data!!.size, it.data, this)
                binding.QuizzesRecyclerView.layoutManager = object :
                    LinearLayoutManager(context, HORIZONTAL, false) {
                    override fun canScrollVertically(): Boolean = false
                }
            } else Log.e("dddd", it.errorMessage!!)
        }

        viewModel.getQnA()
        viewModel.getQnALiveData.observe(viewLifecycleOwner) {
            if (it is Response.Success) {
                if (it.data!!.isEmpty()) noItems()
                adapter = QuestionsAnswersRecyclerAdapter(
                    "HOME", it.data.toMutableList(),
                    this@HomeFragment, requireContext()
                )
                binding.QnARecyclerView.adapter = adapter
                binding.QnARecyclerView.layoutManager = object : LinearLayoutManager(context) {
                    override fun canScrollVertically(): Boolean = false
                }
            } else Log.e("dddd", it.errorMessage!!)
        }

        binding.QnA.setOnClickListener { goToMoreQnA() }
        binding.arrow.setOnClickListener { goToMoreQnA() }

        binding.swipeToRefresh.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.action_homePage_self)
            }, 440)
        }
        setEventListener(requireActivity(),
            KeyboardVisibilityEventListener {
                if (!it) bottomNavigationView?.visibility = View.VISIBLE
            })
    }

    private fun goToMoreQnA() {
        val intent = Intent(context, AskActivity::class.java)
        intent.putExtra("askActivityFragment", "MoreQnA")
        startActivity(intent)
    }

    private fun exitConfirmation() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.exit)
            .setMessage(R.string.exitConfirmation)
            .setIcon(R.drawable.exit_warning)
            .setPositiveButton("YES") { _, _ ->
                activity?.finishAffinity()
            }
            .setNegativeButton("NO") { _, _ -> }
            .setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.exit_dialog))
            .show()
    }

    override fun onQuestionClicked(question: String) {

    }

    override fun commentBtnClicked(): Boolean {
        isCommentIconClicked = !isCommentIconClicked
        return isCommentIconClicked
    }

    override fun bookmarkBtnClicked(question_id: Int) {
        val viewModel = ViewModelProvider(this)[QuestionsAnswersViewModel::class.java]
        viewModel.bookmarkQuestion(BookmarkQuestionBody(question_id))
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
        val format = "Q) $question\n\n$answerFormat".trim()
        shareIntent.putExtra(Intent.EXTRA_TEXT, format)
        val chooser = Intent.createChooser(shareIntent, "Share this QnA using...")
        startActivity(chooser)
    }

    override fun addAnswerClicked(question: String, question_id: Int, position: Int) {
        val customView = layoutInflater.inflate(R.layout.create_answer, null)
        val progressBar = customView.findViewById<CircularProgressIndicator>(R.id.progressBar)
        val textInputLayout = customView.findViewById<TextInputLayout>(R.id.questionLayout)
        val btn = customView.findViewById<Button>(R.id.btn)
        val customViewModel = ViewModelProvider(this)[QuestionsAnswersViewModel::class.java]
        val builder = MaterialAlertDialogBuilder(requireContext()).apply {
            setView(customView)
            background = ColorDrawable(Color.TRANSPARENT)
            setCancelable(false)
        }
        val dialog = builder.show()
        customView.findViewById<ImageView>(R.id.ic_cancel).setOnClickListener { dialog.dismiss() }

        customView.findViewById<MaterialTextView>(R.id.question).text = question

        btn.setOnClickListener {
            if (customView.findViewById<TextInputEditText>(R.id.answer).text.toString().trim()
                    .isEmpty()
            ) {
                textInputLayout.helperText = "Enter your answer"
                return@setOnClickListener
            }
            textInputLayout.helperText = null
            customViewModel.createAnswer(
                CreateAnswerBody(
                    customView.findViewById<TextInputEditText>(R.id.answer)
                        .text.toString().trim(), question_id
                )
            )
            progressBar.show()
            btn.isEnabled = false
            btn.text = null
            requireView().hideKeyboard(activity)
            customViewModel.createAnswerData.observe(viewLifecycleOwner) {
                if (it.data == "success") {
                    Toast.makeText(
                        context, "Added your answer successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    dialog.dismiss()
                    updateRecyclerView(position)
                }
                progressBar.hide()
                btn.text = "Answer"
                btn.isEnabled = true
                Toast.makeText(context, it.errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun addCommentClicked(answer: String, answer_id: Int, position: Int) {
        val customView = layoutInflater.inflate(R.layout.create_comment, null)
        val progressBar = customView.findViewById<CircularProgressIndicator>(R.id.progressBar)
        val textInputLayout = customView.findViewById<TextInputLayout>(R.id.commentLayout)
        val btn = customView.findViewById<Button>(R.id.btn)
        val customViewModel = ViewModelProvider(this)[QuestionsAnswersViewModel::class.java]
        val builder = MaterialAlertDialogBuilder(requireContext()).apply {
            setView(customView)
            background = ColorDrawable(Color.TRANSPARENT)
        }
        val dialog = builder.show()

        customView.findViewById<MaterialTextView>(R.id.question).text = answer
        btn.setOnClickListener {
            if (customView.findViewById<TextInputEditText>(R.id.comment).text.toString().trim()
                    .isEmpty()
            ) {
                textInputLayout.helperText = "Enter your comment"
                return@setOnClickListener
            }
            textInputLayout.helperText = null
            customViewModel.createComment(
                CreateCommentBody(
                    answer_id,
                    customView.findViewById<TextInputEditText>(R.id.comment).text.toString().trim()
                )
            )
            progressBar.show()
            btn.isEnabled = false
            btn.text = null
            requireView().hideKeyboard(activity)
            customViewModel.createCommentData.observe(viewLifecycleOwner) {
                if (it.data == "success") {
                    Toast.makeText(context, "Added your comment successfully", Toast.LENGTH_SHORT)
                        .show()
                    dialog.dismiss()
                    updateRecyclerView(position)
                } else {
                    progressBar.hide()
                    btn.isEnabled = true
                    btn.text = "Comment"
                    Toast.makeText(context, it.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun noItems() {
//        binding.QnARecyclerView.visibility = View.GONE
//        binding.emptyAnimation.visibility = View.VISIBLE
//        binding.text.visibility = View.VISIBLE
    }

    private fun updateRecyclerView(position: Int) {
        val homePageViewModel = ViewModelProvider(this)[HomePageViewModel::class.java]
        homePageViewModel.getQnA()
        homePageViewModel.getQnALiveData.observe(viewLifecycleOwner) {
            if (it is Response.Success) {
                adapter = QuestionsAnswersRecyclerAdapter(
                    "HOME",
                    it.data!!.toMutableList(), this, requireContext()
                )
                binding.QnARecyclerView.adapter = adapter
                adapter.notifyItemInserted(position)
            } else Log.e("dddd", it.errorMessage!!)
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
        val intent = Intent(context, QuizActivity::class.java)
        intent.putExtra("Quiz ID", quiz_id)
        startActivity(intent)
    }

    override fun onNotesClicked(pdfUri: String, noteName: String) {
        Utils.PDF_URI = pdfUri
        val intent = Intent(context, NoteLectureActivity::class.java)
        intent.apply {
            putExtra("PREVIOUS PAGE", "HOME")
            putExtra("PAGE TYPE", "NOTE")
            putExtra("NOTE NAME", noteName)
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
                    it,
                    Toast.LENGTH_SHORT
                ).show()
        }
    }

    override fun onLectureClicked(videoUri: String, lectureName: String) {
        Utils.VIDEO_URI = videoUri
        val intent = Intent(context, NoteLectureActivity::class.java).apply {
            putExtra("PREVIOUS PAGE", "HOME")
            putExtra("PAGE TYPE", "LECTURE")
            putExtra("LECTURE NAME", lectureName)
        }
        startActivity(intent)
    }

    override fun onWishlistBtnClicked(lectureId: Int) {
        val viewModel = ViewModelProvider(this)[QuestionsAnswersViewModel::class.java]
        viewModel.addToWishlist(AddToWishlistBody(lectureId))
        viewModel.addToWishlistData.observe(viewLifecycleOwner) {
            if (it != "success")
                Toast.makeText(
                    context,
                    it,
                    Toast.LENGTH_SHORT
                ).show()
        }
    }

//    override fun onPrepareOptionsMenu(menu: Menu) {
//        menu.findItem(R.id.SearchIcon).isVisible = true
//        super.onPrepareOptionsMenu(menu)
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == R.id.SearchIcon) {
//            val bundle = ActivityOptions.makeSceneTransitionAnimation(activity).toBundle()
//            val intent = Intent(context, AskActivity::class.java).apply {
//                putExtra("askActivityFragment", "ShowSearchQ")
//            }
//            startActivity(intent, bundle)
//        }
//        return super.onOptionsItemSelected(item)
//    }
}