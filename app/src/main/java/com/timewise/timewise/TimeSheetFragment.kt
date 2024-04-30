package com.timewise.timewise

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.timewise.timewise.databinding.FragmentTimeSheetBinding

class TimeSheetFragment : Fragment() {
    private lateinit var binding: FragmentTimeSheetBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_time_sheet, container, false)
        binding = FragmentTimeSheetBinding.bind(view)
        val args = requireArguments()

        getTimeSheet(args.getString("taskId")){
            timeLogs ->
            timeLogs?.forEach {
                val tl = TimeLogComponent(requireContext(), it.date!!, it.minutes!!)
                binding.timelogList.addView(tl)
            }
        }


        binding.leftButton.setOnClickListener{
            parentFragmentManager.popBackStack()
        }

        binding.rightButton.setOnClickListener{
            val frag = TimeLogFragment()
            frag.arguments = args
            parentFragmentManager.beginTransaction()
                .replace(R.id.projectFragContainer, frag)
                .addToBackStack(null)
                .commit()
        }



        return view
    }
}