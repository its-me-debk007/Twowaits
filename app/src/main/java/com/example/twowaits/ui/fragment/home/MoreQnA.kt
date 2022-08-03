package com.example.twowaits.ui.fragment.home

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.twowaits.R
import com.example.twowaits.databinding.MoreQNABinding
import com.example.twowaits.model.CreateAnswerBody
import com.example.twowaits.model.CreateCommentBody
import com.example.twowaits.model.LikeAnswerBody
import com.example.twowaits.network.dashboardApiCalls.Answer
import com.example.twowaits.recyclerAdapters.ItemClicked
import com.example.twowaits.recyclerAdapters.QuestionsAnswersRecyclerAdapter
import com.example.twowaits.sealedClass.Response
import com.example.twowaits.util.hideKeyboard
import com.example.twowaits.viewModel.HomePageViewModel
import com.example.twowaits.viewModel.questionsAnswersViewModel.QuestionsAnswersViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class MoreQnA : Fragment(R.layout.more_q_n_a), ItemClicked {
    private lateinit var binding: MoreQNABinding
    private var isCommentIconClicked = false
    private lateinit var adapter: QuestionsAnswersRecyclerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MoreQNABinding.bind(view)
        binding.swipeToRefresh.setColorSchemeColors(Color.parseColor("#804D37"))
        val viewModel = ViewModelProvider(this)[HomePageViewModel::class.java]
        viewModel.getQnA()
        viewModel.getQnALiveData.observe(viewLifecycleOwner) {
            if (it is Response.Success) {
                if (it.data!!.isEmpty()) noItems()
                adapter = QuestionsAnswersRecyclerAdapter(
                    "MORE_QnA", it.data.toMutableList(),
                    this, requireContext()
                )
                binding.moreQnA.adapter = adapter
                binding.moreQnA.layoutManager = object : LinearLayoutManager(context) {
                    override fun canScrollVertically(): Boolean = false
                }
            } else Log.e("dddd", it.errorMessage!!)
        }

        binding.swipeToRefresh.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                activity?.supportFragmentManager?.beginTransaction()?.detach(this)?.attach(this)
                    ?.commit()
            }, 440)
            binding.swipeToRefresh.isRefreshing = false
        }
    }

    override fun onQuestionClicked(question: String) {

    }

    override fun likeBtnClicked(question_id: Int) {
        val viewModel = ViewModelProvider(this)[QuestionsAnswersViewModel::class.java]
        viewModel.likeAnswer(LikeAnswerBody(question_id))
        viewModel.errorLikeAnswerLiveData.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    override fun commentBtnClicked(): Boolean {
        isCommentIconClicked = !isCommentIconClicked
        return isCommentIconClicked
    }

    override fun bookmarkBtnClicked(question_id: Int) {

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
            setCancelable(false)
            background = ColorDrawable(Color.TRANSPARENT)
        }
        val dialog = builder.show()
        customView.findViewById<ImageView>(R.id.ic_cancel).setOnClickListener { dialog.dismiss() }

        customView.findViewById<MaterialTextView>(R.id.answer).text = answer
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
        binding.moreQnA.visibility = View.GONE
        binding.emptyAnimation.visibility = View.VISIBLE
        binding.text.visibility = View.VISIBLE
    }

    private fun updateRecyclerView(position: Int) {
        val homePageViewModel = ViewModelProvider(this)[HomePageViewModel::class.java]
        homePageViewModel.getQnA()
        homePageViewModel.getQnALiveData.observe(viewLifecycleOwner) {
            if (it is Response.Success) {
                adapter = QuestionsAnswersRecyclerAdapter(
                    "MORE_QnA",
                    it.data!!.toMutableList(), this, requireContext()
                )
                binding.moreQnA.adapter = adapter
                adapter.notifyItemInserted(position)
            }
        }
    }
}