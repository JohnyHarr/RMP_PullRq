package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.SharedPrefsIDs.sharedPrefName
import com.example.myapplication.databinding.ActivityMainBinding


open class MainActivity : AppCompatActivity(),AuthView{
    private lateinit var binding: ActivityMainBinding
    private lateinit var presenter: PresenterAuth

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter= PresenterAuth(this, getSharedPreferences(sharedPrefName,Context.MODE_PRIVATE))
        presenter.init()//initializing Realm
        binding.logInButton.setOnClickListener {//making error messages gone if they were visible
            turnOffAllErrors()
            presenter.tryLogInAction(binding.edLoginLayout.editText!!.text.toString().trim(),binding.edPasswordLayout.editText!!.text.toString().trim())//LogIn call
            Log.d("debug", "name=${binding.edLoginLayout.editText!!.text.toString().trim()}/${binding.edPasswordLayout.editText!!.text.toString().trim()}")
        }
        binding.edPasswordLayout.editText!!.addTextChangedListener(TextChangeWatcher(this))
        binding.buttonSignUp.setOnClickListener{//sending user onto SignUp screen if user wants to sign up
            val intent=Intent(this,ActivitySignUp::class.java)
            startActivity(intent)
        }
        binding.registrationKey.setOnClickListener{
            val intent=Intent(this,ActivitySignUp::class.java)
            startActivity(intent)
        }
        Log.d("debug", "onCreate completed")
    }

    override fun onResume() {
        super.onResume()
        presenter.onActivityResume()//reopen Realm
        presenter.showUsers()
    }

    override fun onPause() {
        super.onPause()
        presenter.onActivityPause()//closing Realm because user doesn't need it right now
    }

    override fun showLogInError() {
        binding.edPasswordLayout.error=getString(R.string.logInError)
        binding.edPasswordLayout.isErrorEnabled=true
    }

    override fun showLoginExistError() {
        binding.edLoginLayout.error=getString(R.string.loginAlreadyExists)
        binding.edLoginLayout.isErrorEnabled=true
    }

    override fun showLoginEmptyError() {
        binding.edLoginLayout.error=getString(R.string.loginEmpty)
        binding.edLoginLayout.isErrorEnabled=true

    }

    override fun showPasswordEmptyError() {
        binding.edPasswordLayout.error=getString(R.string.passwordEmpty)
        binding.edPasswordLayout.isErrorEnabled=true
    }

    override fun turnOffAllErrors(){
        binding.edPasswordLayout.isErrorEnabled=false
        binding.edLoginLayout.isErrorEnabled=false
    }

    override fun enterAnotherScreen() {
        //!!!!!TEMP(there is no need to delete users. It's used for debugging)!!!!!
        presenter.deleteUsers()
        val intent=Intent(this,CatalogActivity::class.java)
        startActivity(intent)
        finish()
    }

}