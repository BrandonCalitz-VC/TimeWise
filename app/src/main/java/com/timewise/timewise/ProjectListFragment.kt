package com.timewise.timewise

import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.util.Xml
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.google.rpc.context.AttributeContext
import com.timewise.timewise.databinding.FragmentProjectListBinding

class ProjectListFragment : Fragment() {
    lateinit var binding: FragmentProjectListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_project_list, container, false)
        binding = FragmentProjectListBinding.bind(view);
        getUserDetails(Firebase.auth.currentUser?.uid) { user ->
            if (user == null) startActivity(Intent(activity, Auth::class.java))
            val db = Firebase.firestore
            getUserProjects(user!!.fbUserId) { projects ->
                projects?.forEach { project: Project ->
                    val p = ProjectComponent(requireContext(),project.title ?: "", project.progress ?: 0,project.categories ?: "")
                    p.setOnClickListener{
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.projectFragContainer, ProjectFragment())
                            .addToBackStack(null)
                            .commit()
                    }
                    binding.projectList.addView(p)
                }
            }
        }
        binding.rightButton.setOnClickListener{
            parentFragmentManager.beginTransaction()
                .replace(R.id.projectFragContainer, ProjectCreationFragment())
                .commit()
        }


        // Inflate the layout for this fragment
        return view
    }
}