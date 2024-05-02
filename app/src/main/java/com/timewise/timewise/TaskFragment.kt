package com.timewise.timewise

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.util.Pair
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.timewise.timewise.databinding.FragmentTaskBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID

class TaskFragment : Fragment() {
    private var taskId: String? = null;
    private lateinit var projectId: String;
    private lateinit var binding: FragmentTaskBinding
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private var startDate: Long = Long.MIN_VALUE
    private val selectedCategories = mutableListOf<String>()
    private var endDate: Long = Long.MIN_VALUE
    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_task, container, false)
        binding = FragmentTaskBinding.bind(view)
        val args = arguments
        projectId = requireArguments().getString("projectId")!!
        taskId = arguments?.getString("taskId")

        getTask(taskId){task ->
            if (task == null) return@getTask
            binding.etName.setText(task.title)
            binding.etDescription.setText(task.description)
            startDate = task.startDate?.time ?: Long.MIN_VALUE
            endDate = task.endDate?.time ?: Long.MIN_VALUE
            binding.progressSlider.progress = task.progress
            binding.etName.setText(task.title)
            binding.etCalendar.setText(
                SimpleDateFormat("dd/MM/yyyy").format(Date(startDate)) + " - "
                        + SimpleDateFormat("dd/MM/yyyy").format(Date(endDate)))
        }

        binding.leftButton.setOnClickListener{
            parentFragmentManager.popBackStack()
        }

        binding.rightButton.setOnClickListener{
            val frag = TimeSheetFragment()
            frag.arguments = args
            parentFragmentManager.beginTransaction()
                .replace(R.id.projectFragContainer, frag)
                .addToBackStack(null)
                .commit()
        }

        binding.addAttachmentButton.setOnClickListener {
            binding.addAttachmentButton.setOnClickListener {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                intent.type = "image/*"

                imagePickerLauncher.launch(intent)
            }
        }

        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                val selectedImageUri: Uri? = result.data?.data
                binding.AttachmentImg.setImageURI(selectedImageUri)
            }
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




        binding.etCalendar.setOnClickListener{
            val picker = MaterialDatePicker.Builder.dateRangePicker()
                .setSelection(
                    Pair(
                    MaterialDatePicker.thisMonthInUtcMilliseconds(),
                    MaterialDatePicker.todayInUtcMilliseconds()
                )
                )
                .setTitleText("Select dates")
                .build()
            picker.addOnPositiveButtonClickListener {
                startDate = it.first?: MaterialDatePicker.todayInUtcMilliseconds()
                endDate = it.second?: MaterialDatePicker.todayInUtcMilliseconds()
                binding.etCalendar.setText(
                    SimpleDateFormat("dd/MM/yyyy").format(Date(startDate)) + " - "
                            + SimpleDateFormat("dd/MM/yyyy").format(Date(endDate)))
            }

            picker.show(childFragmentManager,"tag")
        }

        binding.save.setOnClickListener{
            val taskName: String = binding.etName.text.toString().trim()
            val taskDescription: String = binding.etDescription.text.toString().trim()
            val taskProgress: Int = binding.progressSlider.progress
            var isValid = true
            if (taskName.isEmpty()){
                binding.etNameLayout.handleError("Invalid Task name")
                isValid = false
            }else{
                binding.etNameLayout.handleError(null)
            }
            if (taskDescription.isEmpty()){
                binding.etDescriptionLayout.handleError("Invalid Task description")
                isValid = false
            }else{
                binding.etDescriptionLayout.handleError(null)
            }
            if (startDate== Long.MIN_VALUE || endDate== Long.MIN_VALUE){
                binding.etCalendarLayout.handleError("Invalid Task Dates")
                isValid = false
            }else{
                binding.etCalendarLayout.handleError(null)
            }

            if(!isValid) return@setOnClickListener

            val db = Firebase.firestore

            if(taskId==null){
                db.collection("tasks").add(Task(UUID.randomUUID().toString(),
                    projectId, taskName, taskDescription, Date(startDate), Date(endDate),
                    null, taskProgress, null)).addOnSuccessListener {
                        parentFragmentManager.popBackStack()
                }
            }else{
                db.collection("tasks").whereEqualTo("id", taskId).get()
                    .addOnSuccessListener { tasksDocs->
                        val doc =  db.collection("tasks").document(tasksDocs.first().id)
                        doc.update(mapOf(
                            "title" to taskName,
                            "description" to taskDescription,
                            "startDate" to Date(startDate),
                            "endDate" to Date(endDate),
                            "categories" to null,
                            "progress" to taskProgress,
                            "attachments" to null,
                        ),)
                    }
            }

        }
        return view
    }

}