package com.manapps.mandroid.mediumclonemvckotlin.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import com.manapps.mandroid.mediumclonemvckotlin.MainActivity
import com.manapps.mandroid.mediumclonemvckotlin.Utils.Constants
import com.manapps.mandroid.mediumclonemvckotlin.Utils.SharedPref
import com.manapps.mandroid.mediumclonemvckotlin.Utils.Utils
import com.manapps.mandroid.mediumclonemvckotlin.Utils.Validations
import com.manapps.mandroid.mediumclonemvckotlin.databinding.ActivityLoginBinding
import com.manapps.mandroid.mediumclonemvckotlin.models.entities.LoginData
import com.manapps.mandroid.mediumclonemvckotlin.models.request.LoginRequest
import com.manapps.mandroid.mediumclonemvckotlin.models.response.UserResponse
import com.manapps.mandroid.mediumclonemvckotlin.networking.APIClinet
import com.manapps.mandroid.mediumclonemvckotlin.networking.APIInterface
import com.manapps.mandroid.mediumclonemvckotlin.networking.NetworkHelper
import com.manapps.mandroid.mediumclonemvckotlin.signup.CreateAccountActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var apiInterface: APIInterface


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBindings()
        initViews()
        apiInterface = APIClinet.publicApi
        binding.btLogin.setOnClickListener {
            checkValidationsAndProceed(
                binding.etEmail.text.toString().trim(),
                binding.etPassword.text.toString().trim()
            )
        }
        binding.createAccountTv.setOnClickListener {
            Utils.moveTo(this, CreateAccountActivity::class.java)
        }
    }

    private fun initViews() {
        binding.etEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                Validations.isValidEmail(
                    binding.etEmail.text.toString().trim { it <= ' ' },
                    binding.emailInputLayout,
                    binding.etEmail,
                    this@LoginActivity
                )

            }
        })

        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                Validations.isValidPassword(
                    binding.etPassword.text.toString().trim { it <= ' ' },
                    binding.passwordInputLayout,
                    binding.etPassword,
                    this@LoginActivity
                )

            }
        })

        binding.etPassword.setOnEditorActionListener({ _, actionId, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                checkValidationsAndProceed(
                    binding.etEmail.text.toString().trim(),
                    binding.etPassword.text.toString().trim()
                )
            }
false
        })
    }


    private fun checkValidationsAndProceed(email: String, password: String) {
        if (Validations.isValidEmail(email, binding.emailInputLayout, binding.etEmail, this)
            &&
            Validations.isValidPassword(password, binding.passwordInputLayout, binding.etPassword, this)
        ) {
            if (NetworkHelper.isNetworkConnected(this)) {
                sendLoginRequest(LoginRequest(LoginData(email, password)))
            } else {
                NetworkHelper.noNetworkMessage(this)
            }
        }

    }


    private fun initBindings() {
        try {
            binding = ActivityLoginBinding.inflate(layoutInflater)
            setContentView(binding.root)
        } catch (e: IllegalStateException) {
        } catch (e: Exception) {
        }

    }

    private fun sendLoginRequest(loginRequest: LoginRequest) {
        setProgressDialogEnable()
        val sendLoginCall: Call<UserResponse> = apiInterface.sendlLoginRequest(loginRequest)
        sendLoginCall.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                try {
                    setProgressDialogDisable()
                    if (response.isSuccessful) {
                        val loginUserModel = response.body()
                        loginUserModel?.let {
                            APIClinet.authToken = loginUserModel.user.token
                            SharedPref.saveUserData(
                                this@LoginActivity,
                                Constants.Email,
                                loginUserModel.user.email
                            )
                            SharedPref.saveUserData(
                                this@LoginActivity,
                                Constants.Token,
                                loginUserModel.user.token
                            )
                            Utils.moveTo(this@LoginActivity, MainActivity::class.java)
                        }


                    } else {
                        //handle error message here acc to api response
                        Utils.showMessage(this@LoginActivity, "Invalid Email or Password")

                    }
                } catch (e: Exception) {
                    Utils.showLogMessage(e.toString())
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                setProgressDialogDisable()
                Utils.showMessage(this@LoginActivity, t.message)
            }
        })
    }

    private fun setProgressDialogDisable() {
        Utils.setVisibilityGone(binding.progressBar)
    }

    private fun setProgressDialogEnable() {
        Utils.setVisibilityVisible(binding.progressBar)
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}