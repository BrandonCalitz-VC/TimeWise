package com.timewise.timewise

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.timewise.timewise.databinding.FragmentAnalyticsBinding
import org.checkerframework.checker.units.qual.C
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Calendar
import java.util.Date


class AnalyticsFragment : Fragment() {
    private lateinit var binding: FragmentAnalyticsBinding
    private var startDate :Date? = null
    private var endDate:Date? =  null
    private var timesheet: List<TimeLog> = emptyList()
    private val userId:String? = Firebase.auth.currentUser?.uid
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


        refreash()
        binding.startDate.setOnClickListener{
            val picker = MaterialDatePicker.Builder.datePicker()

                .setSelection(
                    startDate?.time
                )
                .setTitleText("Select dates")
                .build()
            picker.addOnPositiveButtonClickListener {
                startDate = Date(it);
                refreash()
            }

            picker.show(childFragmentManager,"tag")
        }

        binding.endDate.setOnClickListener{
            val picker = MaterialDatePicker.Builder.datePicker()

                .setSelection(
                    endDate?.time
                )
                .setTitleText("Select dates")
                .build()
            picker.addOnPositiveButtonClickListener {
                endDate = Date(it)
                refreash()
            }

            picker.show(childFragmentManager,"tag")
        }


        return view
    }

    fun refreash(){
        binding.startDate.text = SimpleDateFormat("dd/MM/yyyy").format(startDate)
        binding.endDate.text = SimpleDateFormat("dd/MM/yyyy").format(endDate)

        val db = Firebase.firestore
        var timelogref = db.collection("timelogs")
        timelogref
            .whereGreaterThanOrEqualTo("date",startDate!!)
            .whereLessThanOrEqualTo("date",endDate!!)
            .get().addOnSuccessListener {
                var list = mutableListOf<TimeLog>()
                it.forEach { timelogRef->
                    val timeLog = timelogRef.toObject<TimeLog>()
                    if (timeLog.fbuserId == userId)list.add(timeLog)
                }
                timesheet = list.toList()

                val user = getUserDetails(userId){user->
                    binding.hrsWorked.text = timesheet.sumOf { timelog -> timelog.minutes ?: 0 }.div(60.0).toString()
                    var avgs = mutableListOf<Int>()
                    timesheet.groupBy { tl->tl.date }.forEach{ (t, s) ->avgs.add( s.sumOf { v->v.minutes!!})}

                    binding.avgHrs.text = avgs.average().div(60.0).toString()
                    binding.metGoal.text = avgs.filter { a-> a.div(60.0) >= user?.goal!! }.count().toString()
                    binding.belowGoal.text = avgs.filter { a-> a.div(60.0) < user?.goal!! }.count().toString()
                }

            }
    }
}