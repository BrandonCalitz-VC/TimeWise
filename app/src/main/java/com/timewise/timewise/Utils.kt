package com.timewise.timewise

import android.text.TextUtils
import android.util.Patterns
import com.google.android.material.textfield.TextInputLayout

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