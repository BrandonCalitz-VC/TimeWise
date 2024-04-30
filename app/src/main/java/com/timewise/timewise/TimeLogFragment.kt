package com.timewise.timewise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.timewise.timewise.databinding.FragmentTimeLogBinding
import java.util.Date
import java.util.UUID


class TimeLogFragment : Fragment() {
    private lateinit var binding: FragmentTimeLogBinding
    private var date: Date? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_time_log, container, false)
        val args = requireArguments()
        binding = FragmentTimeLogBinding.bind(view)
        date = Date(MaterialDatePicker.todayInUtcMilliseconds())
        binding.etDate.setText(date.toString())
        binding.etDate.setOnClickListener{
            val picker = MaterialDatePicker.Builder.datePicker()

                .setSelection(
                    MaterialDatePicker.todayInUtcMilliseconds()
                )
                .setTitleText("Select dates")
                .build()
            picker.addOnPositiveButtonClickListener {
                date = Date(it)
                binding.etDate.setText( date.toString())
            }

            picker.show(childFragmentManager,"tag")
        }
        binding.btnSubmit.setOnClickListener{
            val minutes: Int? = binding.etMinutes.text.toString().trim().toIntOrNull()
            var isValid = true
            if(minutes == null || minutes <= 0){
                binding.etMinutesLayout.handleError("Invalid Minute Value")
                isValid = false
            }else{
                binding.etMinutesLayout.handleError(null)
            }
            if(date == null){
                binding.etDateLayout.handleError("Invalid Date")
                isValid = false
            }else{
                binding.etDateLayout.handleError(null)
            }
            if (!isValid) return@setOnClickListener

            val db = Firebase.firestore

            db.collection("timelogs").add(TimeLog(UUID.randomUUID().toString(),args.getString("taskId"),minutes,date))
                .addOnSuccessListener {
                parentFragmentManager.popBackStack()
            }

        }

        return view
    }
}