package com.example.myapplication.login_activities.fragments

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.PresenterAuth
import com.example.myapplication.R
import com.example.myapplication.databinding.LogInFragmentBinding
import com.example.myapplication.interfaces.IAuthView
import com.example.myapplication.login_activities.TextChangeWatcher
import com.example.myapplication.objects.SharedPrefsIDs
import kotlinx.coroutines.*

class LogInFragment : Fragment(R.layout.log_in_fragment), IAuthView {
    private var binding: LogInFragmentBinding? = null
    private val presenter by lazy {
        PresenterAuth(
            this, activity?.getSharedPreferences(
                SharedPrefsIDs.sharedPrefLogInData,
                MODE_PRIVATE
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("debug", "onCreate login fragment")
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = LogInFragmentBinding.bind(view)
        super.onViewCreated(view, savedInstanceState)
        presenter.init()
        binding?.logInButton?.setOnClickListener {//making error messages gone if they were visible
            turnOffAllErrors()
            Log.d(
                "debug", "name=${binding?.edLoginLayout?.editText?.text.toString().trim()}" +
                        "/${binding?.edPasswordLayout?.editText?.text.toString().trim()}"
            )
            Log.d("debug", "Progressbar coroutine")
            CoroutineScope(Dispatchers.Main).launch {
                presenter.tryLogInAction(
                    binding?.edLoginLayout?.editText?.text.toString().trim(),
                    binding?.edPasswordLayout?.editText?.text.toString().trim()
                )//LogIn cal
            }

        }
        binding?.edPasswordLayout?.editText?.addTextChangedListener(TextChangeWatcher(this))
        binding?.edLoginLayout?.editText?.addTextChangedListener(TextChangeWatcher(this))
        binding?.buttonSignUp?.setOnClickListener {//sending user onto SignUp screen if user wants to sign up
            enterSignUpScreen()
        }
        binding?.MainConstruct?.setOnClickListener {
            Log.d("debug", "Background click")
            hideKeyboard()
        }
        Log.d("debug", "LogIn fragment")
    }

    override fun onDestroy() {
        presenter.onFragmentClose()
        super.onDestroy()
    }

    override fun hideKeyboard() {
        activity?.onWindowFocusChanged(true)
        val view = activity?.currentFocus
        if (view != null) {
            val toHide =
                activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            toHide.hideSoftInputFromWindow(view.windowToken, 0)
        }
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    override fun showProgressBar() {
        binding?.logInButton?.isEnabled = false
        binding?.buttonSignUp?.isEnabled = false
        binding?.progressBar?.visibility = ProgressBar.VISIBLE
        Log.d("debug", "Progressbar visible")
    }

    override fun hideProgressBar() {
        binding?.logInButton?.isEnabled = true
        binding?.buttonSignUp?.isEnabled = true
        binding?.progressBar?.visibility = ProgressBar.INVISIBLE
        Log.d("debug", "Progressbar invisible")
    }

    override fun showPasswordToggle() {
        binding?.edPasswordLayout?.isPasswordVisibilityToggleEnabled =
            binding?.edPasswordLayout?.editText?.text?.isNotEmpty() ?: false
    }

    override fun showToastUnableToLogIN() {
        Toast.makeText(
            requireContext(),
            getString(R.string.logInErrorConnectionIssue),
            Toast.LENGTH_LONG
        ).show()
    }

    override fun showToastInternalRealmError() {
        Toast.makeText(requireContext(), getString(R.string.unknownRealmError), Toast.LENGTH_LONG)
            .show()
    }

    override fun showLogInError() {
        binding?.edPasswordLayout?.apply {
            error = getString(R.string.logInError)
            isErrorEnabled = true
        }
    }

    override fun showLoginExistError() {
        binding?.edLoginLayout?.apply {
            error = getString(R.string.loginAlreadyExists)
            isErrorEnabled = true
        }
    }

    override fun showLoginInvalidFormat() {
        binding?.edLoginLayout?.apply {
            error = getString(R.string.loginInvalidFormat)
            isErrorEnabled = true
        }
    }

    override fun showPasswordInvalidFormat() {
        binding?.edPasswordLayout?.apply {
            error = getString(R.string.passwordInvalidFormat)
            isErrorEnabled = true
        }
    }

    override fun turnOffAllErrors() {
        binding?.edPasswordLayout?.isErrorEnabled = false
        binding?.edLoginLayout?.isErrorEnabled = false
    }

    private fun enterSignUpScreen() {
        findNavController().navigate(R.id.action_logInFragment_to_signUpFragment)
    }

    override fun enterAnotherScreen() {
        Log.d("debug", "Entering catalog")
        findNavController().navigate(R.id.action_logInFragment_to_catalogFragment2)
    }
}