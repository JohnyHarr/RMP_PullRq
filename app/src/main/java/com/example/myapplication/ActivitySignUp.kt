package com.example.myapplication


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.SharedPrefsIDs.sharedPrefName
import com.example.myapplication.databinding.ActivitySignUpBinding
import kotlinx.coroutines.runBlocking


class ActivitySignUp : AppCompatActivity(), AuthView{
   private lateinit var binding: ActivitySignUpBinding
   private lateinit var presenter: PresenterAuth
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
                finish()
            }

        }
        binding.edLoginLayout.editText!!.addTextChangedListener(TextChangeWatcher(this))
        binding.edPasswordLayout.editText!!.addTextChangedListener(TextChangeWatcher(this))
        presenter= PresenterAuth(this, getSharedPreferences(sharedPrefName,Context.MODE_PRIVATE))
        presenter.init()

        Log.d("debug", "onCreate completed")
    }

  /*  override fun Activity.hideKeyboard() {
        hideKeyboard()
    }*/

    override fun showPasswordToggle() {
        binding.edPasswordLayout.isPasswordVisibilityToggleEnabled = binding.edPasswordLayout.editText!!.text.isNotEmpty()
    }

    override fun hideKeyboard(){
        val view=this.currentFocus
        if (view!=null){
            val toHide=getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            toHide.hideSoftInputFromWindow(view.windowToken, 0)
        }
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    override fun showToastUnableToLogIN() {
        Toast.makeText(this,getString(R.string.logInErrorConnectionIssue), Toast.LENGTH_LONG).show()
    }

    override fun showToastInternalRealmError() {
        Toast.makeText(this, getString(R.string.unknownRealmError),Toast.LENGTH_LONG).show()
    }

    override fun showLogInError() {
        binding.edPasswordLayout.error=getString(R.string.logInError)
        binding.edPasswordLayout.isErrorEnabled=true
    }

    override fun showLoginExistError() {
        binding.edLoginLayout.error=getString(R.string.loginAlreadyExists)
        binding.edLoginLayout.isErrorEnabled=true
    }

    override fun showLoginInvalidFormat() {
        binding.edLoginLayout.error=getString(R.string.loginInvalidFormat)
        binding.edLoginLayout.isErrorEnabled=true
    }

    override fun showPasswordInvalidFormat() {
        binding.edPasswordLayout.error=getString(R.string.passwordInvalidFormat)
        binding.edPasswordLayout.isErrorEnabled=true
    }

    override fun turnOffAllErrors(){
        binding.edPasswordLayout.isErrorEnabled=false
        binding.edLoginLayout.isErrorEnabled=false
    }

    override fun enterAnotherScreen() {
    }


}