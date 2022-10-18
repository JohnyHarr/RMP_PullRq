package com.example.myapplication.login_activities.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.PresenterAuth
import com.example.myapplication.R
import com.example.myapplication.databinding.SignUpFragmentBinding
import com.example.myapplication.interfaces.IAuthView
import com.example.myapplication.login_activities.TextChangeWatcher
import com.example.myapplication.objects.Consts
import com.example.myapplication.objects.SharedPrefsIDs
import kotlinx.coroutines.runBlocking

class SignUpFragment: Fragment(R.layout.sign_up_fragment), IAuthView {
    private val binding by lazy{SignUpFragmentBinding.bind(requireView())}
    private val presenter by lazy { PresenterAuth(this, activity?.getSharedPreferences(
        SharedPrefsIDs.sharedPrefLogInData,
        Context.MODE_PRIVATE
    ))}
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSignUp.setOnClickListener{
            turnOffAllErrors()
            runBlocking {
                if (presenter.createUser(
                        binding.edLoginLayout.editText?.text.toString().trim(),
                        binding.edPasswordLayout.editText?.text.toString().trim(),
                        binding.edFirstnameLayout.editText?.text.toString().trim(),
                        binding.edLastnameLayout.editText?.text.toString().trim()
                    )
                )
                 findNavController().popBackStack()
            }
            hideProgressBar()
        }
        Log.d("debug", "Sign Up fragment")
        binding.edLoginLayout.editText?.addTextChangedListener(TextChangeWatcher(this))
        binding.edPasswordLayout.editText?.addTextChangedListener(TextChangeWatcher(this))
    }

    override fun showPasswordToggle() {
        binding.edPasswordLayout.isPasswordVisibilityToggleEnabled =
            binding.edPasswordLayout.editText?.text?.isNotEmpty() ?: false
    }

    override fun hideKeyboard(){
        /*val view=this.currentFocus
        if (view!=null){
            val toHide=getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            toHide.hideSoftInputFromWindow(view.windowToken, 0)
        }
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)*/
    }

    override fun showProgressBar() {
        binding.progressBarSignUp.visibility= View.VISIBLE
    }

    override fun hideProgressBar() {
        binding.progressBarSignUp.visibility= View.GONE
    }

    override fun showToastUnableToLogIN() {
        Toast.makeText(requireContext(),getString(R.string.logInErrorConnectionIssue), Toast.LENGTH_LONG).show()
    }

    override fun showToastInternalRealmError() {
        Toast.makeText(requireContext(), getString(R.string.unknownRealmError), Toast.LENGTH_LONG).show()
    }

    override fun showLogInError() {
        binding.edPasswordLayout.apply {
            error = getString(R.string.logInError)
            isErrorEnabled = true
        }
    }

    override fun showLoginExistError() {
        binding.edLoginLayout.apply {
            error = getString(R.string.loginAlreadyExists)
            isErrorEnabled = true
        }
    }

    override fun showLoginInvalidFormat() {
        binding.edLoginLayout.apply {
            error = getString(R.string.loginInvalidFormat)
            isErrorEnabled = true
        }
    }

    override fun showPasswordInvalidFormat() {
        binding.edPasswordLayout.apply {
            error = getString(R.string.passwordInvalidFormat)
            isErrorEnabled = true
        }
    }

    override fun turnOffAllErrors(){
        binding.edPasswordLayout.isErrorEnabled=false
        binding.edLoginLayout.isErrorEnabled=false
    }

    override fun enterAnotherScreen() {

    }
}