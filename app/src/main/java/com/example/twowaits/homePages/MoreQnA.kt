package com.example.twowaits.homePages

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieAnimationView
import com.example.twowaits.R
import com.example.twowaits.apiCalls.dashboardApiCalls.Answer
import com.example.twowaits.databinding.CreateAnswerBinding
import com.example.twowaits.databinding.CreateCommentBinding
import com.example.twowaits.databinding.MoreQNABinding
import com.example.twowaits.homePages.questionsAnswers.CreateAnswerBody
import com.example.twowaits.homePages.questionsAnswers.CreateCommentBody
import com.example.twowaits.homePages.questionsAnswers.LikeAnswerBody
import com.example.twowaits.recyclerAdapters.ItemClicked
import com.example.twowaits.recyclerAdapters.QuestionsAnswersRecyclerAdapter
import com.example.twowaits.viewmodels.HomePageViewModel
import com.example.twowaits.viewmodels.questionsAnswersViewModel.QuestionsAnswersViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class MoreQnA: Fragment(R.layout.more_q_n_a), ItemClicked {
    private lateinit var binding: MoreQNABinding
    private var isClicked = false
    private lateinit var adapter: QuestionsAnswersRecyclerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = MoreQNABinding.bind(view)
        val viewModel = ViewModelProvider(this)[HomePageViewModel::class.java]
        val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView?.visibility = View.GONE
        val drawerLayout = activity?.findViewById<DrawerLayout>(R.id.drawerLayout)
        drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        viewModel.getQnA()
        viewModel.getQnALiveData.observe(viewLifecycleOwner) {
            if (it.isEmpty()) noItems()
            adapter = QuestionsAnswersRecyclerAdapter("MORE_QnA", it.toMutableList(), this)
            binding.moreQnA.adapter = adapter
            binding.moreQnA.layoutManager = object : LinearLayoutManager(context) {
                override fun canScrollVertically(): Boolean = false
            }
        }
        viewModel.errorGetQnALiveData.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

        binding.swipeToRefresh.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.action_moreQnA2_self)
            }, 440)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
                bottomNavigationView?.visibility = View.VISIBLE
                val drawerLayout = activity?.findViewById<DrawerLayout>(R.id.drawerLayout)
                drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }
        })
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
        isClicked = !isClicked
        return isClicked
    }

    override fun bookmarkBtnClicked(question_id: Int) {

    }

    override fun shareBtnClicked(question: String, answersList: List<Answer>) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        var answerFormat = ""
        for (i in answersList.indices) {
            answerFormat += "A${i+1}) ${answersList[i].answer_id}"
            if (i != answersList.size-1) answerFormat += "\n\n"
        }
        val format = "Q) $question\n\n$answerFormat"
        shareIntent.putExtra(Intent.EXTRA_TEXT, format)
        val chooser = Intent.createChooser(shareIntent, "Share this QnA using...")
        startActivity(chooser)
    }

    override fun addAnswerClicked(question: String, question_id: Int, position: Int) {
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
            viewModel.createAnswer(
                CreateAnswerBody(dialog.findViewById<TextInputEditText>(R.id.answerOfQ).text.toString().trim(),
                question_id)
            )
            dialog.findViewById<LottieAnimationView>(R.id.ProgressBar).visibility = View.VISIBLE
            viewModel.createAnswerData.observe(viewLifecycleOwner) {
                if (it == "success") {
                    Toast.makeText(context, "Added your answer successfully", Toast.LENGTH_SHORT)
                        .show()
                    dialog.cancel()
                    updateRecyclerView(position)
                } else {
                    dialog.findViewById<LottieAnimationView>(R.id.ProgressBar).visibility =
                        View.GONE
                    Toast.makeText(context, "Please try again!\n$it", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun addCommentClicked(answer: String, answer_id: Int, position: Int) {
        val viewModel = ViewModelProvider(this)[QuestionsAnswersViewModel::class.java]
        val dialog = Dialog(requireContext())
        dialog.setContentView(CreateCommentBinding.inflate(layoutInflater).root)
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
                CreateCommentBody(answer_id, dialog.findViewById<TextInputEditText>(R.id.answerOfQ).text.toString().trim())
            )
            dialog.findViewById<LottieAnimationView>(R.id.ProgressBar).visibility = View.VISIBLE
            viewModel.createCommentData.observe(viewLifecycleOwner) {
                if (it == "success") {
                    Toast.makeText(context, "Added your comment successfully", Toast.LENGTH_SHORT)
                        .show()
                    dialog.cancel()
                    updateRecyclerView(position)
                } else {
                    dialog.findViewById<LottieAnimationView>(R.id.ProgressBar).visibility =
                        View.GONE
                    Toast.makeText(context, "Please try again!\n$it", Toast.LENGTH_SHORT).show()
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
        homePageViewModel.getQnALiveData.observe(viewLifecycleOwner) { qAndA ->
            adapter = QuestionsAnswersRecyclerAdapter("MORE_QnA",
                qAndA.toMutableList(), this)
            binding.moreQnA.adapter = adapter
            adapter.notifyItemInserted(position)
        }
        homePageViewModel.errorGetQnALiveData.observe(viewLifecycleOwner) { errorMsg ->
            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
        }
    }
}