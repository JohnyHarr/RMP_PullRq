package com.example.myapplication.login_activities


import android.annotation.SuppressLint
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
import com.example.myapplication.databinding.ActivitySignUpBinding
import com.example.myapplication.interfaces.IAuthView
import kotlinx.coroutines.runBlocking


class ActivitySignUp : AppCompatActivity(), IAuthView {
   private val binding by lazy{ActivitySignUpBinding.inflate(layoutInflater)}
   private val presenter by lazy {
       PresenterAuth(this, getSharedPreferences(sharedPrefName, MODE_PRIVATE)) }
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        binding.buttonSignUp.setOnClickListener{
            showProgressBar()
            turnOffAllErrors()
            runBlocking {
                if (presenter.createUser(
                        binding.edLoginLayout.editText?.text.toString().trim(),
                        binding.edPasswordLayout.editText?.text.toString().trim(),
                        binding.edFirstnameLayout.editText?.text.toString().trim(),
                        binding.edLastnameLayout.editText?.text.toString().trim()
                    )
                )
                finish()
            }
            hideProgressBar()
        }
        binding.edLoginLayout.editText?.addTextChangedListener(TextChangeWatcher(this))
        binding.edPasswordLayout.editText?.addTextChangedListener(TextChangeWatcher(this))
        presenter.init()

        Log.d("debug", "onCreate completed")
    }

    override fun showPasswordToggle() {
        binding.edPasswordLayout.isPasswordVisibilityToggleEnabled =
            binding.edPasswordLayout.editText?.text?.isNotEmpty() ?: false
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
        binding.progressBarSignUp.visibility= View.VISIBLE
    }

    override fun hideProgressBar() {
        binding.progressBarSignUp.visibility= View.GONE
    }

    override fun showToastUnableToLogIN() {
        Toast.makeText(this,getString(R.string.logInErrorConnectionIssue), Toast.LENGTH_LONG).show()
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

    override fun enterAnotherScreen() {
        val intent= Intent(this, CatalogActivity::class.java)
        startActivity(intent)
        presenter.deInit()
        finish()
    }


}