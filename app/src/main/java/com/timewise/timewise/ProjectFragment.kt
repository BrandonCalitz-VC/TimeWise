package com.timewise.timewise

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayout
import com.timewise.timewise.databinding.FragmentProjectBinding

class ProjectFragment : Fragment() {
    private lateinit var binding: FragmentProjectBinding
    private var tasks: List<Task> = emptyList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_project, container, false)
        binding = FragmentProjectBinding.bind(view)
        val args = requireArguments()
        val projectId = args.getString("projectId")
        getProjectTasks(projectId){ts ->
            tasks = ts?: emptyList()
            binding.taskList.removeAllViews()
            binding.rowSelection.getTabAt(0)?.select()
            tasks.filter { task -> task.progress <= 0 }.forEach { t->
                val tc = TaskComponent(requireContext(), t.title?:"", t.progress, t.categories?:"")
                tc.setOnClickListener{
                    val frag = TaskFragment()
                    args.putString("taskId", t.id)
                    frag.arguments = args

                    parentFragmentManager.beginTransaction()
                        .replace(R.id.projectFragContainer, frag)
                        .addToBackStack(null)
                        .commit()

                }
                binding.taskList.addView(tc)
            }

        }

        binding.rowSelection.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.taskList.removeAllViews()
                var tempTasks: List<Task> = tasks.filter { task -> task.progress == 0 }
                if(tab?.position ==1) tempTasks = tasks.filter { task -> task.progress in 1..99 }
                else if(tab?.position ==2) tempTasks = tasks.filter { task -> task.progress == 100 }

                tempTasks.forEach { t->
                    val tc = TaskComponent(requireContext(), t.title?:"", t.progress, t.categories?:"")
                    tc.setOnClickListener{
                        val frag = TaskFragment()
                        args.putString("taskId", t.id)
                        frag.arguments = args

                        parentFragmentManager.beginTransaction()
                            .replace(R.id.projectFragContainer, frag)
                            .addToBackStack(null)
                            .commit()
                    }
                    binding.taskList.addView(tc)
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Handle tab reselect
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Handle tab unselect
            }
        })
        binding.rightButton.setOnClickListener{
            val frag = TaskFragment()
            frag.arguments = args
            parentFragmentManager.beginTransaction()
                .replace(R.id.projectFragContainer,frag)
                .addToBackStack(null)
                .commit()
        }
        return view
    }

}