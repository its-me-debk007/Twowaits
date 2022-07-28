package com.example.twowaits.ui.fragment.home

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.twowaits.R
import com.example.twowaits.databinding.YourQuestionsBinding
import com.example.twowaits.homePages.questionsAnswers.BookmarkQuestionBody
import com.example.twowaits.homePages.questionsAnswers.CreateAnswerBody
import com.example.twowaits.homePages.questionsAnswers.CreateCommentBody
import com.example.twowaits.homePages.questionsAnswers.LikeAnswerBody
import com.example.twowaits.network.dashboardApiCalls.Answer
import com.example.twowaits.recyclerAdapters.ItemClicked
import com.example.twowaits.recyclerAdapters.QuestionsAnswersRecyclerAdapter
import com.example.twowaits.sealedClass.Response
import com.example.twowaits.viewModel.HomePageViewModel
import com.example.twowaits.viewModel.YourQuestionsViewModel
import com.example.twowaits.viewModel.questionsAnswersViewModel.QuestionsAnswersViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class YourQuestions : Fragment(), ItemClicked {
    private var _binding: YourQuestionsBinding? = null
    private val binding get() = _binding!!
    var isClicked = false
    private lateinit var adapter: QuestionsAnswersRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = YourQuestionsBinding.inflate(inflater, container, false)
        val viewModel = ViewModelProvider(this)[YourQuestionsViewModel::class.java]
        viewModel.getYourQnA()
        viewModel.q_n_aLiveData.observe(viewLifecycleOwner) {
            if (it.isEmpty()) noItems()
            adapter = QuestionsAnswersRecyclerAdapter(
                "YOUR_Q", it.toMutableList(), this, requireContext()
            )
            binding.YourQnARecyclerView.adapter = adapter
        }
        viewModel.errorLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onQuestionClicked(question: String) {
        Snackbar.make(binding.YourQnARecyclerView, question, Snackbar.LENGTH_SHORT).show()
    }

    override fun commentBtnClicked(): Boolean {
        isClicked = !isClicked
        return isClicked
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
            answerFormat += "A${i + 1}) ${answersList[i].answer_id}"
            if (i != answersList.size - 1) answerFormat += "\n\n"
        }
        val format = "Q) $question\n\n$answerFormat"
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
        }
        val dialog = builder.show()

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
        binding.YourQnARecyclerView.visibility = View.GONE
        binding.emptyAnimation.visibility = View.VISIBLE
        binding.text.visibility = View.VISIBLE
    }

    private fun updateRecyclerView(position: Int) {
        val homePageViewModel = ViewModelProvider(this)[HomePageViewModel::class.java]
        homePageViewModel.getQnA()
        homePageViewModel.getQnALiveData.observe(viewLifecycleOwner) {
            if (it is Response.Success) {
                adapter = QuestionsAnswersRecyclerAdapter(
                    "YOUR_Q",
                    it.data!!.toMutableList(), this, requireContext()
                )
                binding.YourQnARecyclerView.adapter = adapter
                adapter.notifyItemInserted(position)
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
}