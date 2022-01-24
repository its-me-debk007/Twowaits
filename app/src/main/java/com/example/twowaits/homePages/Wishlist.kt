package com.example.twowaits.homePages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.twowaits.databinding.WishlistBinding
import com.example.twowaits.recyclerAdapters.WishlistRecyclerAdapter

class Wishlist: Fragment() {
    private var _binding: WishlistBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = WishlistBinding.inflate(inflater, container, false)

            binding.WishlistRecyclerView.adapter = WishlistRecyclerAdapter(1)
            binding.WishlistRecyclerView.layoutManager = LinearLayoutManager(container?.context)

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}