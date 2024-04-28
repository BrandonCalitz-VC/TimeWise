package com.timewise.timewise

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.timewise.timewise.databinding.FragmentAnalyticsBinding

class AnalyticsFragment : Fragment() {
    private lateinit var binding: FragmentAnalyticsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_analytics, container, false)
        binding = FragmentAnalyticsBinding.bind(view)

        return view
    }
}