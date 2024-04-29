package com.timewise.timewise

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import com.google.android.material.chip.Chip
import com.timewise.timewise.databinding.TaskComponentBinding


@SuppressLint("ViewConstructor")
class TaskComponent @JvmOverloads constructor(
    context: Context,
    taskName: String,
    taskProgreess: Int,
    taskCategories: String,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attributeSet, defStyle) {
    private var binding: TaskComponentBinding
    init {
        val view:View = inflate(context, R.layout.task_component, this)
        binding = TaskComponentBinding.bind(view)
        val typedArray = context.obtainStyledAttributes(
            attributeSet,
            R.styleable.TaskComponent,
            defStyle,
            0
        )
        try{
            binding.TaskName.text = typedArray.getString(R.styleable.TaskComponent_taskName)?:taskName?:""
            binding.progress.progress = typedArray.getString(R.styleable.TaskComponent_taskProgress)?.toInt()?:taskProgreess?:0
            val categories = typedArray.getString(R.styleable.TaskComponent_taskCategories)?:taskCategories?:""
            categories.split(",").forEach { str ->
                val chip = Chip(context)
                chip.text = str.trim()
                chip.minHeight = 20
                chip.textSize = 11F
                chip.chipBackgroundColor = ColorStateList.valueOf(Color.parseColor("#001AFF"))
                chip.setTextColor(Color.WHITE)
                binding.categories.addView(chip)
            }
        }finally {
            typedArray.recycle()
        }




    }
}