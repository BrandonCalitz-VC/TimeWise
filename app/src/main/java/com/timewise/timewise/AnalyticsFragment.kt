package com.timewise.timewise

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.timewise.timewise.databinding.FragmentAnalyticsBinding
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.core.cartesian.series.Line
import com.anychart.enums.Anchor
import com.anychart.enums.MarkerType
import com.anychart.enums.TooltipPositionMode
import com.anychart.enums.HoverMode
import com.google.firebase.firestore.toObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import kotlin.collections.ArrayList

class AnalyticsFragment : Fragment() {
    private lateinit var binding: FragmentAnalyticsBinding
    private var startDate: Date? = null
    private var endDate: Date? = null
    private var timesheet: List<TimeLog> = emptyList()
    private val userId: String? = Firebase.auth.currentUser?.uid

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_analytics, container, false)
        binding = FragmentAnalyticsBinding.bind(view)
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        startDate = calendar.time
        calendar.add(Calendar.DAY_OF_WEEK, 6)
        endDate = calendar.time

        refresh()
        binding.startDate.setOnClickListener {
            val picker = MaterialDatePicker.Builder.datePicker()
                .setSelection(startDate?.time)
                .setTitleText("Select start date")
                .build()
            picker.addOnPositiveButtonClickListener {
                startDate = Date(it)
                refresh()
            }
            picker.show(childFragmentManager, "tag")
        }

        binding.endDate.setOnClickListener {
            val picker = MaterialDatePicker.Builder.datePicker()
                .setSelection(endDate?.time)
                .setTitleText("Select end date")
                .build()
            picker.addOnPositiveButtonClickListener {
                endDate = Date(it)
                refresh()
            }
            picker.show(childFragmentManager, "tag")
        }

        return view
    }

    @SuppressLint("SimpleDateFormat")
    private fun refresh() {
        binding.startDate.text = SimpleDateFormat("dd/MM/yyyy").format(startDate)
        binding.endDate.text = SimpleDateFormat("dd/MM/yyyy").format(endDate)

        val db = Firebase.firestore
        val timelogRef = db.collection("timelogs")
        timelogRef
            .whereGreaterThanOrEqualTo("date", startDate!!)
            .whereLessThanOrEqualTo("date", endDate!!)
            .get().addOnSuccessListener { documents ->
                val list = mutableListOf<TimeLog>()
                for (timelogRef in documents) {
                    val timeLog = timelogRef.toObject<TimeLog>()
                    if (timeLog.fbuserId == userId) list.add(timeLog)
                }
                timesheet = list.toList()

                getUserDetails(userId) { user ->
                    binding.hrsWorked.text = timesheet.sumOf { timelog -> timelog.minutes ?: 0 }.div(60.0).toString()
                    val avgs = mutableListOf<Int>()
                    timesheet.groupBy { tl -> tl.date }.forEach { (_, s) -> avgs.add(s.sumOf { v -> v.minutes!! }) }

                    binding.avgHrs.text = avgs.average().div(60.0).toString()
                    binding.metGoal.text = avgs.filter { a -> a.div(60.0) >= user?.goal!! }.count().toString()
                    binding.belowGoal.text = avgs.filter { a -> a.div(60.0) < user?.goal!! }.count().toString()

                    setupChart(user?.goal?.toFloat() ?: 0f)
                }
            }
    }

    private fun setupChart(goal: Float) {
        val anyChartView: AnyChartView = binding.root.findViewById(R.id.DataChart)
        val cartesian = AnyChart.line()

        cartesian.animation(true)
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
        cartesian.yAxis(0).title("Hours Worked")
        cartesian.xAxis(0).title("Date")
        cartesian.xAxis(0).labels().padding(5, 5, 5, 5)

        val seriesData = ArrayList<DataEntry>()
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
        timesheet.groupBy { it.date }.forEach { (date, logs) ->
            val hours = logs.sumOf { it.minutes ?: 0 } / 60.0
            seriesData.add(ValueDataEntry(dateFormatter.format(date), hours))
        }

        val series: Line = cartesian.line(seriesData)
        series.name("Hours Worked")
        series.hovered().markers().enabled(true)
        series.hovered().markers()
            .type(MarkerType.CIRCLE)
            .size(4.0)
        series.tooltip().position("right").anchor(Anchor.LEFT_CENTER).offsetX(5.0).offsetY(5.0)

        // Add MaxLine line
        val goalSeriesData = seriesData.map { dataEntry ->
            ValueDataEntry(dataEntry.getValue("x").toString(), goal)
        }
        val goalSeries = cartesian.line(goalSeriesData)
        goalSeries.name("Goal")
        goalSeries.stroke("3 red dash")
        goalSeries.hovered().markers().enabled(false)

        cartesian.legend().enabled(true)
        cartesian.legend().fontSize(13.0)
        cartesian.legend().padding(0, 0, 10, 0)

        anyChartView.setChart(cartesian)
    }

    private fun getUserDetails(userId: String?, callback: (User?) -> Unit) {
        val db = Firebase.firestore
        db.collection("users").document(userId!!).get().addOnSuccessListener { document ->
            val user = document.toObject<User>()
            callback(user)
        }
    }
}
