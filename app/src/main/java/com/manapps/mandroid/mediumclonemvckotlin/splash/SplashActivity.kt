package com.manapps.mandroid.mediumclonemvckotlin.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.manapps.mandroid.mediumclonemvckotlin.MainActivity
import com.manapps.mandroid.mediumclonemvckotlin.Utils.Constants
import com.manapps.mandroid.mediumclonemvckotlin.Utils.SharedPref
import com.manapps.mandroid.mediumclonemvckotlin.Utils.Utils
import com.manapps.mandroid.mediumclonemvckotlin.databinding.ActivitySplashBinding
import com.manapps.mandroid.mediumclonemvckotlin.login.LoginActivity
import java.lang.IllegalStateException
import java.util.*
import kotlin.concurrent.timerTask

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBindings()
        checkSessionHistory()
    }

    private fun checkSessionHistory() {
        val email: String? = SharedPref.getSavedUserData(this, Constants.Email)
        if (email.isNullOrEmpty()) {
            Timer().schedule(timerTask {
                Utils.moveTo(this@SplashActivity, LoginActivity::class.java)
            }, 1000)
        }
        else{
            Timer().schedule(timerTask {
                Utils.moveTo(this@SplashActivity, MainActivity::class.java)
            }, 1000)
        }
    }

    private fun initBindings() {
        try {
            binding = ActivitySplashBinding.inflate(layoutInflater)
            setContentView(binding.root)
        } catch (e: IllegalStateException) {
        } catch (e: Exception) {
        }

    }
}