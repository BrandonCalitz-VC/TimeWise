package com.timewise.timewise

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.chip.Chip
import com.timewise.timewise.databinding.TimelogComponentBinding
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Date


@SuppressLint("ViewConstructor", "SimpleDateFormat")
class TimeLogComponent @JvmOverloads constructor(
    context: Context,
    date: Date,
    minutes: Int,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attributeSet, defStyle) {
    private var binding: TimelogComponentBinding
    init {
        val view:View = inflate(context, R.layout.timelog_component, this)
        binding = TimelogComponentBinding.bind(view)
        val typedArray = context.obtainStyledAttributes(
            attributeSet,
            R.styleable.TimeLogComponent,
            defStyle,
            0
        )
        try{
            val formatter = SimpleDateFormat("dd/MM/yyyy")
            binding.date.text = formatter.format(date)

            binding.hrs.text = String.format("%d:%d", minutes / 60, minutes %60)
        }finally {
            typedArray.recycle()
        }




    }
}