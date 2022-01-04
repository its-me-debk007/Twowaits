package com.example.twowaits.homePages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.twowaits.databinding.ProfileBinding
import coil.load
import coil.transform.CircleCropTransformation
import com.example.twowaits.repository.dashboardRepositories.ProfileRepository

class Profile : Fragment() {
    private var _binding: ProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ProfileBinding.inflate(inflater, container, false)

            val repository = ProfileRepository()
            repository.profileDetails("Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNjQxMjI0NTUwLCJpYXQiOjE2NDExMzgxNTAsImp0aSI6Ijg4MmE4ZjViOWVhMTQyMDBhODMwMjJjM2JhOTdmODU0IiwidXNlcl9pZCI6M30.Ony7ZNpGSUjD2K3kLvih_auiTtky-xjPMfRur5g06fQ")
            repository.profileMutableLiveData.observe(viewLifecycleOwner, {
                binding.ProfilePic.load(it.profile_pic){
                    transformations(CircleCropTransformation())
                }
                binding.NameOfUser.text = it.name
                binding.DetailsOfUser.text = "${it.year} | ${it.course} | ${it.branch}"
                binding.CollegeOfUser.text = it.college
            })
        repository.errorMutableLiveData.observe(viewLifecycleOwner, {
            Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        })

        binding.AddPicBtn.setOnClickListener {
        }

        return binding.root
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}