package com.timewise.timewise

import android.text.TextUtils
import android.util.Patterns
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import java.lang.Exception
import java.util.Calendar
import java.util.Date

public fun TextInputLayout.handleError(message: String?){
    if (message == null){
        this.isErrorEnabled = false

    }else{
        this.error= message
        this.isErrorEnabled = true
        this.errorIconDrawable = null
    }
}

fun isValidEmail(target: CharSequence): Boolean {
    return if (TextUtils.isEmpty(target)) {
        false
    } else {
        Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }
}
fun isPasswordValid(target: String): Boolean {
    val requirements = listOf(String::isLongEnough, String::hasEnoughDigits, String::isMixedCase, String::hasSpecialChar)
    return requirements.all { check -> check(target) }
}

fun String.isLongEnough() = length >= 8
fun String.hasEnoughDigits() = count(Char::isDigit) > 0
fun String.isMixedCase() = any(Char::isLowerCase) && any(Char::isUpperCase)
fun String.hasSpecialChar() = any { it in "@!,+^-$%&*#?" }
fun getUserDetails(id: String?, onComplete: (User?) -> Unit) {
    try {
        val db = Firebase.firestore
        db.collection("users").whereEqualTo("fbUserId", id).get()
            .addOnSuccessListener { documents ->
                onComplete(documents.firstOrNull()?.toObject<User>())
                return@addOnSuccessListener
            }

    }catch ( e: Exception){
        onComplete(null)
    }
}

fun getUserProjects(id: String?, onComplete: (List<Project>?) -> Unit) {

    try {
        val db = Firebase.firestore
        db.collection("projects").whereEqualTo("fbUserId", id).get()
            .addOnSuccessListener { documents ->
                val projects: MutableList<Project> = mutableListOf()
                for (document in documents) {
                    projects.add(document.toObject<Project>())
                }
                onComplete(projects.toList())
            }

    } catch (e: Exception) {
        onComplete(null)
    }
}
fun getProjectTasks(id: String?, onComplete: (List<Task>?) -> Unit) {

    try {
        val db = Firebase.firestore
        db.collection("tasks").whereEqualTo("projectId", id).get()
            .addOnSuccessListener { documents ->
                val tasks: MutableList<Task> = mutableListOf()
            for (document in documents) {
                tasks.add(document.toObject<Task>())
              }
                onComplete(tasks.toList())
            }

    }catch ( e: Exception){
        onComplete(null)
    }
}
fun getTimeSheet(id: String?, onComplete: (List<TimeLog>?) -> Unit) {

    try {
        val db = Firebase.firestore
        db.collection("timelogs").whereEqualTo("taskId", id).get()
            .addOnSuccessListener { documents ->
            val timelogs: MutableList<TimeLog> = mutableListOf()
            for (document in documents) {
                timelogs.add(document.toObject<TimeLog>())
              }
                onComplete(timelogs.toList())
            }

    }catch ( e: Exception){
        onComplete(null)
    }
}
fun getTimeLogs(id: String?, onComplete: (List<TimeLog>?) -> Unit) {
    try {
        val db = Firebase.firestore
        db.collection("timelogs").whereEqualTo("fbuserId", id).get()
            .addOnSuccessListener { documents ->
                val timelogs: MutableList<TimeLog> = mutableListOf()
                for (document in documents) {
                    timelogs.add(document.toObject<TimeLog>())
                }
                onComplete(timelogs.toList())
            }
            .addOnFailureListener {
                onComplete(null)
            }
    } catch (e: Exception) {
        onComplete(null)
    }
}

fun getProject(id: String?, onComplete: (Project?) -> Unit) {
    try {
        val db = Firebase.firestore
        db.collection("projects").whereEqualTo("id", id).get()
            .addOnSuccessListener { documents ->
                onComplete(documents.firstOrNull()?.toObject<Project>())
                return@addOnSuccessListener
            }
    }catch ( e: Exception){
        onComplete(null)
    }
}
fun getTask(id: String?, onComplete: (Task?) -> Unit) {
    try {
        val db = Firebase.firestore
        db.collection("tasks").whereEqualTo("id", id).get()
            .addOnSuccessListener { documents ->
                onComplete(documents.firstOrNull()?.toObject<Task>())
                return@addOnSuccessListener
            }
    }catch ( e: Exception){
        onComplete(null)
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

public data class User(
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val fbUserId: String? = null,
    val goal: Int? = null,
    val maxgoal: Int? = null
)
public data class Project(
    val id: String? = null,
    val fbUserId: String? = null,
    val title: String? = null,
    val description: String? = null,
    val startDate: Date? = null,
    val endDate: Date? = null,
    val categories: String? = null,
    val progress: Int = 0,
)
public data class Task(
    val id: String? = null,
    val projectId: String? = null,
    val title: String? = null,
    val description: String? = null,
    val startDate: Date? = null,
    val endDate: Date? = null,
    val categories: String? = null,
    val progress: Int = 0,
    val attachments: List<String>? = null
)
public data class TimeLog(
    val id: String? = null,
    val taskId: String? = null,
    val fbuserId: String? = null,
    val minutes: Int? = null,
    val date: Date? = null
)