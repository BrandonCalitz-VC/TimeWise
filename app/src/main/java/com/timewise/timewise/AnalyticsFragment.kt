package com.timewise.timewise

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.firestore.toObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import kotlin.collections.ArrayList
import kotlin.math.roundToInt

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
        startDate = getStartOfDay(calendar.time)
        calendar.add(Calendar.DAY_OF_WEEK, 6)

        endDate = getStartOfDay(calendar.time)

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

    private fun refresh() {
        binding.startDate.text = startDate?.let { SimpleDateFormat("dd/MM/yyyy").format(it) } ?: "No start date"
        binding.endDate.text = endDate?.let { SimpleDateFormat("dd/MM/yyyy").format(it) } ?: "No end date"

        getTimeLogs(userId) { timelogs ->
            if (timelogs != null) {
                timesheet = timelogs.filter { it.date != null && it.date >= startDate!! && it.date <= endDate!! }
                if(timesheet.isEmpty()) return@getTimeLogs;
                getUserDetails(userId) { user ->
                    if (user == null) {
                        Log.e("AnalyticsFragment", "User details are null")
                        return@getUserDetails
                    }

                    val totalMinutes = timesheet.sumOf { it.minutes ?: 0 }
                    binding.hrsWorked.text = (totalMinutes / 60.0).roundToInt().toString()

                    val avgs = mutableListOf<Int>()
                    val groupedByDate = timesheet.groupBy { getStartOfDay(it.date!!) }
                    for (logs in groupedByDate.values) {
                        avgs.add(logs.sumOf { it.minutes ?: 0 })
                    }

                    binding.avgHrs.text = (avgs.average() / 60.0).roundToInt().toString()
                    binding.metGoal.text =
                        avgs.filter { it / 60.0 >= user.goal!! }.count().toString()
                    binding.belowGoal.text = calculateMissedGoals(groupedByDate, user.goal!!).toString()

                    setupChart(groupedByDate, user.goal.toFloat())
                }
            } else {
                Log.e("AnalyticsFragment", "No timelogs found")
            }
        }
    }

    private fun calculateMissedGoals(groupedByDate: Map<Date, List<TimeLog>>, goal: Int): Int {
        var missedGoalCount = 0
        var currentDate = startDate
        while (currentDate!!.before(endDate) || currentDate == endDate) {
            val logsForDate = groupedByDate[getStartOfDay(currentDate)]
            val hours = logsForDate?.sumOf { it.minutes ?: 0 }?.div(60.0) ?: 0.0
            if (hours < goal) {
                missedGoalCount++
            }
            val calendar = Calendar.getInstance()
            calendar.time = currentDate
            calendar.add(Calendar.DATE, 1)
            currentDate = calendar.time
        }
        return missedGoalCount
    }

    private fun setupChart(groupedByDate: Map<Date, List<TimeLog>>, goal: Float) {
        val anyChartView: AnyChartView = binding.DataChart
        anyChartView.refreshDrawableState()

        val cartesian = AnyChart.line()
        cartesian.animation(true)
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
        cartesian.yAxis(0).title("Hours Worked")
        cartesian.xAxis(0).title("Date")
        cartesian.xAxis(0).labels().padding(5, 5, 5, 5)

        val seriesData = ArrayList<DataEntry>()
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd")

        var currentDate = startDate
        while (currentDate!!.before(endDate) || currentDate == endDate) {
            val logsForDate = groupedByDate[getStartOfDay(currentDate)]
            val hours = logsForDate?.sumOf { it.minutes ?: 0 }?.div(60.0) ?: 0.0
            seriesData.add(ValueDataEntry(dateFormatter.format(currentDate), hours))
            val calendar = Calendar.getInstance()
            calendar.time = currentDate
            calendar.add(Calendar.DATE, 1)
            currentDate = calendar.time
        }

        val series: Line = cartesian.line(seriesData)
        series.name("Hours Worked")
        series.hovered().markers().enabled(true)
        series.hovered().markers()
            .type(MarkerType.CIRCLE)
            .size(4.0)
        series.tooltip().position("right").anchor(Anchor.LEFT_CENTER).offsetX(0.0).offsetY(0.0)

        // Add constant goal line
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

    private fun getStartOfDay(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }
}
