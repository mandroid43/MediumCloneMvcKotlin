package com.manapps.mandroid.mediumclonemvckotlin.Utils

import android.content.Context
import android.text.TextUtils
import android.util.Patterns
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout
import com.manapps.mandroid.mediumclonemvckotlin.R
import java.util.regex.Pattern

object Validations {
    fun isValidPassword(password: String,textInputLayout: TextInputLayout,
                        editText: EditText,
                        context: Context): Boolean {

        var isValidPassword = false
        if (password.isNullOrEmpty()) {
            textInputLayout.error = context.resources.getString(R.string.emptyPassword)
        } else {
            if (password.length == 8) {
                isValidPassword = true
            }
            if (!isValidPassword) {
                textInputLayout.error = context.resources.getString(R.string.invalidPassword)
                editText.requestFocus()
            } else {
                textInputLayout.error = null
            }
        }
        return isValidPassword

        //        val PASSWORD_PATTERN = Pattern.compile(
//            "^" +
//                    "(?=.*[0-9])" +  //at least 1 digit
//                    "(?=.*[a-zA-Z])" +  //any letter
//                    "(?=\\S+$)" +  //no white spaces
//                    ".{8,50}" +  //at least 8 characters
//                    "$"
//        )
//        return PASSWORD_PATTERN.matcher(password).matches()

    }

    fun isValidEmail(
        email: String,
        textInputLayout: TextInputLayout,
        editText: EditText,
        context: Context
    ): Boolean {
        var isValidEmail = false
        if (email.isEmpty()) {
            textInputLayout.error = context.resources.getString(R.string.emptyEmail)
        } else {
            isValidEmail =
                (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches())
            if (!isValidEmail) {
                textInputLayout.error = context.resources.getString(R.string.invalidEmail)
                editText.requestFocus()
            } else {
                textInputLayout.error = null
            }
        }
        return isValidEmail


        //more customized email validation expression
//        var isValid = false
//        val emailPattern = "^[^@]+@[a-zA-Z0-9._-]+\\.+[a-z._-]+$"
//        isValid = if (email.trim { it <= ' ' }.matches(emailPattern)) {
//            true
//        } else {
//            false
//        }
        // return isValid
    }

}