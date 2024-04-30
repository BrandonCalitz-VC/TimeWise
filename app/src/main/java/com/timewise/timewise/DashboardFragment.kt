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
import java.util.Calendar

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
        val currentDate = Calendar.getInstance().time

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

                if (userId != null) {
                    db.collection("User")
                        .document(userId)
                        .get()
                        .addOnSuccessListener { userDocument ->
                            val userGoal = userDocument.getLong("goal")
                            if (userGoal != null) {
                                val progress = (totalMinutesLogged.toFloat() / userGoal.toFloat()) * 100

                                binding.progressCircle.progress = progress.toInt()
                            }
                        }
                        .addOnFailureListener {
                            "Failed to find user and update progress bar"
                        }
                }
            }
            .addOnFailureListener {
                "Failed to find current day or user"
            }
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