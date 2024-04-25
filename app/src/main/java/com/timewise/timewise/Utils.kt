package com.timewise.timewise

import android.content.ContentValues.TAG
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject

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

public fun getUserDetails(id: String):User?{
    val db = Firebase.firestore
    var user:User? = null;
    db.collection("users").whereEqualTo("fbUserId",id).get()
        .addOnSuccessListener { documents ->
        for (document in documents) {
            user = document.toObject<User>()
            return@addOnSuccessListener
        }
    }
    return user

}


public data class User(
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val fbUserId: String? = null,
    val goal: Int? = null
)