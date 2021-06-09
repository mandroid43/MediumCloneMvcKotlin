package com.manapps.mandroid.mediumclonemvckotlin.Utils

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout

object Utils {
    fun moveTo(context: Context?, className: Class<*>?) {
        if (context != null) {
            val intent = Intent(context, className)
            context.startActivity(intent)
        }
    }

    fun moveToWithData(context: Context?, className: Class<*>?,key:String,value:String?) {
        if (context != null) {
            val intent = Intent(context, className)
            intent.putExtra(key,value)
            context.startActivity(intent)
        }
    }

    fun moveToAndClearHisory(context: Context?, className: Class<*>?) {
        if (context != null) {
            val intent = Intent(context, className)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)
        }
    }

    fun setVisibilityVisible(visibilityVisible: View) {
        try {
            visibilityVisible.visibility = View.VISIBLE
        } catch (exception: NullPointerException) {
        } catch (exception: Exception) {
        }
    }

    fun setVisibilityGone(visibilityGone: View) {
        try {
            visibilityGone.visibility = View.GONE
        } catch (exception: NullPointerException) {
        } catch (exception: Exception) {
        }

    }

    fun showMessage(context: Context?, message: String?) {
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun showLogMessage(message: String?) {
        Log.e("e", message!!)
    }

    fun showEdittextError(textInputLayout: TextInputLayout?, error: String?) {
        textInputLayout?.let {
            it.error = error
        }
    }

}