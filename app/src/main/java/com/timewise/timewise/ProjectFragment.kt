package com.timewise.timewise

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.timewise.timewise.databinding.FragmentProjectBinding

class ProjectFragment : Fragment() {
    private lateinit var binding: FragmentProjectBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_project, container, false)
        binding = FragmentProjectBinding.bind(view)


        return view
    }

}