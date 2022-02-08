package com.example.twowaits.authPages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.twowaits.Data
import com.example.twowaits.R
import com.example.twowaits.databinding.ChooseYourRoleBinding
import com.example.twowaits.repository.authRepositories.LoginRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch

@DelicateCoroutinesApi
class ChooseYourRole : Fragment() {
    private var _binding: ChooseYourRoleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ChooseYourRoleBinding.inflate(inflater, container, false)

        binding.student.setOnCheckedChangeListener { _, _ ->
            val repository = LoginRepository()
            repository.getAuthTokens(Data.EMAIL, Data.PASSWORD)
            binding.ProgressBar.visibility = View.VISIBLE
            repository.getTokenLiveData.observe(viewLifecycleOwner) { tokensResponse ->
                lifecycleScope.launch {
                    Data.saveData("log_in_status", "STUDENT")
                    Data.saveData("accessToken", tokensResponse.access)
                    Data.saveData("refreshToken", tokensResponse.refresh)
                }
                Data.ACCESS_TOKEN = tokensResponse.access
                Data.REFRESH_TOKEN = tokensResponse.refresh
                findNavController().navigate(R.id.action_chooseYourRole_to_enterDetailsStudent)
            }
        }
        binding.teacher.setOnCheckedChangeListener { _, _ ->
            val repository = LoginRepository()
            repository.getAuthTokens(Data.EMAIL, Data.PASSWORD)
            binding.ProgressBar.visibility = View.VISIBLE
            repository.getTokenLiveData.observe(viewLifecycleOwner) { tokensResponse ->
                lifecycleScope.launch {
                    Data.saveData("log_in_status", "FACULTY")
                    Data.saveData("accessToken", tokensResponse.access)
                    Data.saveData("refreshToken", tokensResponse.refresh)
                }
                Data.ACCESS_TOKEN = tokensResponse.access
                Data.REFRESH_TOKEN = tokensResponse.refresh
                binding.teacher.isChecked = false
                binding.student.isChecked = false
                binding.ProgressBar.visibility = View.INVISIBLE
                findNavController().navigate(R.id.action_chooseYourRole_to_enterDetailsFaculty)
            }
            repository.errorLiveData.observe(viewLifecycleOwner) {
                Toast.makeText(context, it + "\nPlease choose your option again", Toast.LENGTH_LONG)
                    .show()
                binding.teacher.isChecked = false
                binding.student.isChecked = false
                binding.ProgressBar.visibility = View.INVISIBLE
            }
        }
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_chooseYourRole_to_otpVerification)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}