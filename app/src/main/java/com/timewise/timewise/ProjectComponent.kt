package com.timewise.timewise

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import com.google.android.material.chip.Chip
import com.timewise.timewise.databinding.ProjectComponentBinding


class ProjectComponent @JvmOverloads constructor(
    context: Context,
    projectName: String,
    projectProgreess: Int,
    projectCategories: String,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attributeSet, defStyle) {
    private var binding: ProjectComponentBinding
    init {
        val view:View = inflate(context, R.layout.project_component, this)
        binding = ProjectComponentBinding.bind(view)
        val typedArray = context.obtainStyledAttributes(
            attributeSet,
            R.styleable.ProjectComponent,
            defStyle,
            0
        )
        try{
            binding.ProjectName.text = typedArray.getString(R.styleable.ProjectComponent_projectName)?:projectName?:""
            binding.progress.progress = typedArray.getString(R.styleable.ProjectComponent_projectProgress)?.toInt()?:projectProgreess?:0
            val categories = typedArray.getString(R.styleable.ProjectComponent_projectCategories)?:projectCategories?:""
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