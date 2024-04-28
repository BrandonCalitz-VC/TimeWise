package com.timewise.timewise

import android.os.Bundle
import android.text.Selection
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Pair
import com.google.android.material.datepicker.MaterialDatePicker
import com.timewise.timewise.databinding.FragmentProjectCreationBinding

class ProjectCreationFragment : Fragment() {
    private lateinit var binding: FragmentProjectCreationBinding
    private var startDate: Long = 0
    private var endDate: Long = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_project_creation, container, false)
        binding = FragmentProjectCreationBinding.bind(view)
        binding.leftButton.setOnClickListener{
            parentFragmentManager.popBackStack()
        }
        binding.etCalendar.setOnClickListener{
            val picker = MaterialDatePicker.Builder.dateRangePicker()
                .setSelection(Pair(
                    MaterialDatePicker.thisMonthInUtcMilliseconds(),
                    MaterialDatePicker.todayInUtcMilliseconds()
                ))
                .setTitleText("Select dates")
                .build()
            picker.addOnPositiveButtonClickListener {
                startDate = it.first?:MaterialDatePicker.todayInUtcMilliseconds()
                endDate = it.second?:MaterialDatePicker.todayInUtcMilliseconds()
            }

            picker.show(childFragmentManager,"tag")
        }

        return view
    }
}