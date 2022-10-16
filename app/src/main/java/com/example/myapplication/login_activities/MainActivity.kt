package com.example.myapplication.login_activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.*
import com.example.myapplication.objects.SharedPrefsIDs.sharedPrefName
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.interfaces.IAuthView


class MainActivity : AppCompatActivity(), IAuthView {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val presenter by lazy {
        PresenterAuth(this, getSharedPreferences(sharedPrefName, MODE_PRIVATE))
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        presenter.init()//initializing presenter
        binding.logInButton.setOnClickListener {//making error messages gone if they were visible
            turnOffAllErrors()
            presenter.tryLogInAction(
                binding.edLoginLayout.editText?.text.toString().trim(),
                binding.edPasswordLayout.editText?.text.toString().trim())//LogIn cal
            Log.d("debug", "name=${binding.edLoginLayout.editText?.text.toString().trim()}/${binding.edPasswordLayout.editText?.text.toString().trim()}")
        }
        binding.edPasswordLayout.editText?.addTextChangedListener(TextChangeWatcher(this))
        binding.edLoginLayout.editText?.addTextChangedListener(TextChangeWatcher(this))
        binding.buttonSignUp.setOnClickListener{//sending user onto SignUp screen if user wants to sign up
            enterSignUpScreen()
        }
        binding.MainConstruct.setOnClickListener{
            Log.d("debug", "Background click")
            hideKeyboard()
            currentFocus?.clearFocus()
        }
        Log.d("debug", "onCreate completed")
    }

   override fun hideKeyboard(){
       val view=this.currentFocus
       if (view!=null){
           val toHide=getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
           toHide.hideSoftInputFromWindow(view.windowToken, 0)
       }
       window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
   }

    override fun showProgressBar() {
        binding.progressBar.visibility= View.VISIBLE
    }

    override fun hideProgressBar() {
        binding.progressBar.visibility=View.GONE
    }

    override fun showPasswordToggle() {
        binding.edPasswordLayout.isPasswordVisibilityToggleEnabled =
            binding.edPasswordLayout.editText?.text?.isNotEmpty() ?: false
    }

    override fun showToastUnableToLogIN(){
        Toast.makeText(this, getString(R.string.logInErrorConnectionIssue),Toast.LENGTH_LONG).show()
    }

    override fun showToastInternalRealmError() {
        Toast.makeText(this, getString(R.string.unknownRealmError),Toast.LENGTH_LONG).show()
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

    private fun enterSignUpScreen(){
        val intent=Intent(this, ActivitySignUp::class.java)
       //
        startActivity(intent)
    }

    override fun enterAnotherScreen() {

        val intent=Intent(this, CatalogActivity::class.java)
        startActivity(intent)
        presenter.deInit()
        finish()
    }

}