package com.example.twowaits

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class CreatePassword : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.create_password, container, false)
//        Navigation.findNavController(v).navigate(R.id.action_testFragment_to_testFragment2)
        return v
    }
}