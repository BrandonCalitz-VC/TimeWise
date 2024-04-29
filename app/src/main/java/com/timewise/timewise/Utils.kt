package com.timewise.timewise

import android.text.TextUtils
import android.util.Patterns
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import java.lang.Exception
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
            for (document in documents) {
                onComplete(document.toObject<User>())
                return@addOnSuccessListener
              }
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

    }catch ( e: Exception){
        onComplete(null)
    }
}



public data class User(
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val fbUserId: String? = null,
    val goal: Int? = null
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