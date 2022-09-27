package com.example.myapplication


import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.SharedPrefsIDs.sharedPrefName
import com.example.myapplication.databinding.ActivitySignUpBinding


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
            if (presenter.createUser(binding.edLoginLayout.editText?.text.toString().trim(),
                    binding.edPasswordLayout.editText?.text.toString().trim(),
                    binding.edFirstnameLayout.editText?.text.toString().trim(),
                    binding.edLastnameLayout.editText?.text.toString().trim()))
                finish()

        }
        binding.edPasswordLayout.editText!!.addTextChangedListener(TextChangeWatcher(this))
        presenter= PresenterAuth(this, getSharedPreferences(sharedPrefName,Context.MODE_PRIVATE))
        presenter.init()

        Log.d("debug", "onCreate completed")
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
    }


}