package com.manapps.mandroid.mediumclonemvckotlin.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import com.manapps.mandroid.mediumclonemvckotlin.MainActivity
import com.manapps.mandroid.mediumclonemvckotlin.R
import com.manapps.mandroid.mediumclonemvckotlin.Utils.Constants
import com.manapps.mandroid.mediumclonemvckotlin.Utils.SharedPref
import com.manapps.mandroid.mediumclonemvckotlin.Utils.Utils
import com.manapps.mandroid.mediumclonemvckotlin.Utils.Validations
import com.manapps.mandroid.mediumclonemvckotlin.databinding.ActivityCreateAccountBinding
import com.manapps.mandroid.mediumclonemvckotlin.models.entities.SignupData
import com.manapps.mandroid.mediumclonemvckotlin.models.request.SignupRequest
import com.manapps.mandroid.mediumclonemvckotlin.models.response.UserResponse
import com.manapps.mandroid.mediumclonemvckotlin.networking.APIClinet
import com.manapps.mandroid.mediumclonemvckotlin.networking.APIInterface
import com.manapps.mandroid.mediumclonemvckotlin.networking.NetworkHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateAccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateAccountBinding
    private lateinit var apiInterface: APIInterface
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBindings()
        initViews()
        apiInterface = APIClinet.publicApi
        binding.btLogin.setOnClickListener {
            checkValidationsAndProceed(
                binding.etUserName.text.toString().trim(),
                binding.etEmail.text.toString().trim(),
                binding.etPassword.text.toString().trim()
            )
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
                    this@CreateAccountActivity
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
                    this@CreateAccountActivity
                )
            }
        })

        binding.etPassword.setOnEditorActionListener { _, actionId, event ->
            if (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER || actionId == EditorInfo.IME_ACTION_DONE) {
                checkValidationsAndProceed(
                    binding.etUserName.text.toString().trim(),
                    binding.etEmail.text.toString().trim(),
                    binding.etPassword.text.toString().trim()
                )
            }
            false
        }
    }


    private fun validateUserName(userName: String): Boolean {
        var isValidUserName = false
        if (userName.isEmpty()) {
            binding.userNameInputLayout.error = getString(R.string.emptyUserName)
        } else {
            binding.userNameInputLayout.error = null
            isValidUserName = true
        }
        return isValidUserName
    }

    private fun checkValidationsAndProceed(userName: String, email: String, password: String) {
        if (validateUserName(userName)
            &&
            Validations.isValidEmail(email, binding.emailInputLayout, binding.etEmail, this)
            &&
            Validations.isValidPassword(password, binding.passwordInputLayout,binding.etPassword,this)
        ) {
            if (NetworkHelper.isNetworkConnected(this)) {
                sendCreateAccountRequest(SignupRequest(SignupData(email, password, userName)))
            } else {
                NetworkHelper.noNetworkMessage(this)
            }
        }

    }


    private fun initBindings() {
        try {
            binding = ActivityCreateAccountBinding.inflate(layoutInflater)
            setContentView(binding.root)
        } catch (e: IllegalStateException) {
        } catch (e: Exception) {
        }

    }

    private fun sendCreateAccountRequest(signupRequest: SignupRequest) {
        setProgressDialogEnable()
        val sendLoginCall: Call<UserResponse> = apiInterface.sendCreateAccountRequest(signupRequest)
        sendLoginCall.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                try {
                    setProgressDialogDisable()
                    if (response.isSuccessful) {
                        val createAccountUserModel = response.body()
                        createAccountUserModel?.let {
                            APIClinet.authToken = createAccountUserModel.user.token
                            SharedPref.saveUserData(
                                this@CreateAccountActivity,
                                Constants.Email,
                                createAccountUserModel.user.email
                            )
                            SharedPref.saveUserData(
                                this@CreateAccountActivity,
                                Constants.Token,
                                createAccountUserModel.user.token
                            )
                            Utils.moveTo(this@CreateAccountActivity, MainActivity::class.java)
                        }


                    } else {
                        //handle error message here acc to api response
                        Utils.showMessage(this@CreateAccountActivity, "Invalid Email or UserName")

                    }
                } catch (e: Exception) {
                    Utils.showLogMessage(e.toString())
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                setProgressDialogDisable()
                Utils.showMessage(this@CreateAccountActivity, t.message)
            }
        })
    }

    private fun setProgressDialogDisable() {
        Utils.setVisibilityGone(binding.progressBar)
    }

    private fun setProgressDialogEnable() {
        Utils.setVisibilityVisible(binding.progressBar)
    }

}