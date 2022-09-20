package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.SharedPrefsIDs.sharedPrefName
import com.example.myapplication.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), View.OnTouchListener, AuthView{
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
            binding.loginError.visibility=View.GONE
            binding.passwordError.visibility=View.GONE
            presenter.tryLogInAction(binding.edLogin.text.toString().trim(),binding.edPassword.text.toString().trim())//LogIn call
            Log.d("debug", "name=${binding.edLogin.text}/${binding.edPassword.text}")
        }

        binding.butPasswordVisible.setOnTouchListener(this)

        binding.buttonSignUp.setOnClickListener{//sending user onto SignUp screen if user wants to sign up
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

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean{//this function is used for implementing password visibility feature
          when(motionEvent.action){
            MotionEvent.ACTION_DOWN -> {
                binding.edPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                Log.d("GUI", "password shall be shown")

            }
            MotionEvent.ACTION_UP -> {
                binding.edPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.butPasswordVisible.performClick()
                Log.d("GUI", "password shall not be shown")
            }
             else -> return false
        }
        return true
    }



    override fun showLogInError() {
        binding.passwordError.text=getString(R.string.logInError)
        binding.passwordError.visibility=View.VISIBLE
    }

    override fun showLoginExistError() {
    }

    override fun showLoginEmptyError() {
        binding.loginError.text=getString(R.string.loginEmpty)
        binding.loginError.visibility=View.VISIBLE

    }

    override fun showPasswordEmptyError() {
        binding.passwordError.text=getString(R.string.passwordEmpty)
        binding.passwordError.visibility=View.VISIBLE
    }

    override fun enterAnotherScreen() {
        //!!!!!TEMP(there is no need to delete users. It's used for debugging)!!!!!
        presenter.deleteUsers()
        val intent=Intent(this,CatalogActivity::class.java)
        startActivity(intent)
        finish()
    }


}