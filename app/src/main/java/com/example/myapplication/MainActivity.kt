package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.SharedPrefsIDs.sharedPrefName
import com.example.myapplication.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(),IAuthView{
    private lateinit var binding: ActivityMainBinding
    private lateinit var presenter: PresenterAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter = PresenterAuth(this, getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE))
        presenter.init()//initializing presenter
        binding.logInButton.setOnClickListener {//making error messages gone if they were visible
            turnOffAllErrors()
            presenter.tryLogInAction(
                binding.edLoginLayout.editText!!.text.toString().trim(),
                binding.edPasswordLayout.editText!!.text.toString().trim())//LogIn cal
            Log.d("debug", "name=${binding.edLoginLayout.editText!!.text.toString().trim()}/${binding.edPasswordLayout.editText!!.text.toString().trim()}")
        }
        binding.edPasswordLayout.editText!!.addTextChangedListener(TextChangeWatcher(this))
        binding.edLoginLayout.editText!!.addTextChangedListener(TextChangeWatcher(this))
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
        binding.edPasswordLayout.isPasswordVisibilityToggleEnabled = binding.edPasswordLayout.editText!!.text.isNotEmpty()
    }

    override fun showToastUnableToLogIN(){
        Toast.makeText(this, getString(R.string.logInErrorConnectionIssue),Toast.LENGTH_LONG).show()
    }

    override fun showToastInternalRealmError() {
        Toast.makeText(this, getString(R.string.unknownRealmError),Toast.LENGTH_LONG).show()
    }

    override fun showLoginInvalidFormat() {
        binding.edLoginLayout.error=getString(R.string.loginInvalidFormat)
        binding.edLoginLayout.isErrorEnabled=true
    }

    override fun showPasswordInvalidFormat() {
        binding.edPasswordLayout.error=getString(R.string.passwordInvalidFormat)
        binding.edPasswordLayout.isErrorEnabled=true
    }

    override fun showLogInError() {
        binding.edPasswordLayout.error=getString(R.string.logInError)
        binding.edPasswordLayout.isErrorEnabled=true
    }

    override fun showLoginExistError() {
        binding.edLoginLayout.error=getString(R.string.loginAlreadyExists)
        binding.edLoginLayout.isErrorEnabled=true
    }

    override fun turnOffAllErrors(){
        binding.edPasswordLayout.isErrorEnabled=false
        binding.edLoginLayout.isErrorEnabled=false
    }

    private fun enterSignUpScreen(){
        val intent=Intent(this,ActivitySignUp::class.java)
       //
        startActivity(intent)
    }

    override fun enterAnotherScreen() {

        val intent=Intent(this,CatalogActivity::class.java)
        startActivity(intent)
        presenter.deInit()
        finish()
    }

}