package com.timewise.timewise

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Selection
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.util.Pair
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.timewise.timewise.databinding.FragmentProjectCreationBinding
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.UUID

class ProjectCreationFragment : Fragment() {
    private lateinit var binding: FragmentProjectCreationBinding
    private var startDate: Long = Long.MIN_VALUE
    private var endDate: Long = Long.MIN_VALUE
    private val selectedCategories = mutableListOf<String>()

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
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
                binding.etCalendar.setText(SimpleDateFormat("dd/MM/yyyy").format(Date(startDate)) + " - "
                        +SimpleDateFormat("dd/MM/yyyy").format(Date(endDate)))
            }

            picker.show(childFragmentManager,"tag")
        }

        val categoryList = mutableListOf("UI","Backend","Meeting")
        val categorySpinner: Spinner = view.findViewById(R.id.categorySpinner)
        val selectedCategoriesTextView: TextView = view.findViewById(R.id.etcatName)

        val categoryAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, categoryList)
        categorySpinner.adapter = categoryAdapter

        binding.AddCategory.setOnClickListener {
            val selectedCategory = categorySpinner.selectedItem.toString()
            if (!selectedCategories.contains(selectedCategory)) {
                selectedCategories.add(selectedCategory)
                selectedCategoriesTextView.append("\n$selectedCategory")
            }
        }

        binding.NewCategory.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())

            builder.setTitle("Add New Category")

            val input = EditText(requireContext())
            input.hint = "Enter Category Name"
            builder.setView(input)


            builder.setPositiveButton("Add") { dialog, which ->
                val newCategory = input.text.toString().trim()
                if (newCategory.isNotEmpty()) {
                    categoryList.add(newCategory)
                    categoryAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(requireContext(), "Please enter a category name", Toast.LENGTH_SHORT).show()
                }
            }

            builder.setNegativeButton("Cancel") { dialog, which ->
                dialog.cancel()
            }

            val dialog = builder.create()
            dialog.show()
        }





        binding.submit.setOnClickListener{
            val name : String = binding.etName.text.toString().trim()
            val description: String = binding.etDescription.text.toString().trim()
            val progress: Int = binding.progressSlider.progress
            val category: String = binding.etcatName.text.toString().trim()

            var isValid = true

            if(name.isEmpty()){
                binding.etNameLayout.handleError("Invalid Name")
                isValid = false
            }else binding.etNameLayout.handleError(null)

            if(description.isEmpty()){
                binding.etDescriptionLayout.handleError("Invalid description")
                isValid = false
            }else binding.etDescriptionLayout.handleError(null)

            if(startDate == Long.MIN_VALUE || endDate==Long.MIN_VALUE){
                binding.etCalendarLayout.handleError("Invalid start and endDate")
                isValid = false
            }else binding.etCalendarLayout.handleError(null)

            if(!isValid) return@setOnClickListener


            val db = Firebase.firestore
            db.collection("projects").add(Project(UUID.randomUUID().toString(), Firebase.auth.currentUser!!.uid,
                name, description, Date(startDate), Date(endDate), category, progress )).addOnSuccessListener {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.projectFragContainer, ProjectListFragment())
                        .commit()
            }
        }



        return view
    }


}