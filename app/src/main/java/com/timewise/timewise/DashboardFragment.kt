package com.timewise.timewise

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.timewise.timewise.databinding.FragmentDashboardBinding
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import kotlin.math.round

class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashboardBinding
    private val db = FirebaseFirestore.getInstance()
    private val userId = Firebase.auth.currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_dashboard, container, false)
        binding = FragmentDashboardBinding.bind(view)

        calcSetProgress()
        calcSetStreak()

        return view
    }

    private fun calcSetProgress() {
        val currentDate = getStartOfDay(
            Date.from(
                LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()
            )
        )
        getUserDetails(userId) { user ->
            var progress = 0f
            getTimeLogs(userId) { timeLogs ->
                if (user?.goal == null) return@getTimeLogs
                if (timeLogs != null) {
                    var totalMinutesLogged = 0
                    var logs =
                        timeLogs.filter { x -> getStartOfDay(x.date!!).compareTo(currentDate) == 0 }
                    totalMinutesLogged = logs.sumOf { x -> x.minutes!! }
                    progress = ((totalMinutesLogged.toFloat() / 60) / user.goal.toFloat()) * 100
                    binding.progressCircle.progress = progress.toInt()
                    binding.progressText.text =
                        (totalMinutesLogged.toFloat() / 60).toString() + "/" + user?.goal.toString()
                    val dailyTotals = timeLogs.groupBy { getStartOfDay(it.date!!) }
                        .mapValues { entry -> entry.value.sumOf { it.minutes!! } / 60 }
                    var date = currentDate
                    var streak = 0
                    while (true) {
                        date = Date.from(
                            LocalDate.now().minusDays(streak.toLong())
                                .atStartOfDay(ZoneId.systemDefault()).toInstant()
                        )
                        if (dailyTotals.containsKey(date) && dailyTotals[date]!! >= user.goal) {
                            streak++
                        } else {
                            break
                        }
                    }
                    binding.streakText.text = "Streak: $streak🔥"
                }


            }
        }
    }

    fun getStartOfDay(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }

    private fun calcSetStreak() {
        val userId = Firebase.auth.currentUser?.uid

        if (userId != null) {
            db.collection("User")
                .document(userId)
                .get()
                .addOnSuccessListener { userDocument ->
                    val userGoal = userDocument.getLong("goal")
                    if (userGoal != null) {
                        val today = Calendar.getInstance()
                        var streak = 0

                        today.add(Calendar.DAY_OF_YEAR, -1)

                        for (i in 0 until today.get(Calendar.DAY_OF_YEAR)) {
                            val currentDate = today.time

                            db.collection("TimeLog")
                                .whereEqualTo("date", currentDate)
                                .whereEqualTo("userId", userId)
                                .get()
                                .addOnSuccessListener { documents ->
                                    var totalMinutesLogged = 0
                                    for (document in documents) {
                                        val minutes = document.getLong("minutes")
                                        if (minutes != null) {
                                            totalMinutesLogged += minutes.toInt()
                                        }
                                    }

                                    if (totalMinutesLogged >= userGoal * 60) {
                                        streak++
                                    } else {
                                        streak = 0
                                    }

                                    binding.streakText.text = "Streak: $streak🔥"
                                }
                                .addOnFailureListener {
                                    "Failed to compare user timelog date and current date"
                                }

                            today.add(Calendar.DAY_OF_YEAR, -1)
                        }
                    }
                }
                .addOnFailureListener {
                    "Failed to find current user"
                }
        }

    }

}