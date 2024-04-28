package com.timewise.timewise

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.timewise.timewise.databinding.FragmentProjectsBinding


class ProjectsFragment : Fragment() {
    private lateinit var binding : FragmentProjectsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_projects, container, false)
        binding = FragmentProjectsBinding.bind(view)
        childFragmentManager.beginTransaction()
            .replace(binding.projectFragContainer.id,ProjectListFragment())
            .addToBackStack(null)
            .commit();
        return view
    }
}