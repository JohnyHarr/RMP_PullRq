package com.example.myapplication

//import android.app.Activity
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


interface AuthView{
    fun showLogInError()
    fun showPasswordEmptyError()
    fun showLoginEmptyError()
    fun enterMainScreen()
}


class MainActivity : AppCompatActivity(), View.OnTouchListener, AuthView{
    private lateinit var binding: ActivityMainBinding
    private lateinit var presenter: PresenterAuth


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.logInButton.setOnClickListener {
            binding.loginError.visibility=View.GONE
            binding.passwordError.visibility=View.GONE
            presenter.tryLogInAction(binding.edLogin.text.toString().trim(),binding.edPassword.text.toString().trim())
            Log.d("name", "name=${binding.edLogin.text}/${binding.edPassword.text}")
        }

        binding.butPasswordVisible.setOnTouchListener(this)
        var signUp=false
        binding.buttonSignUp.setOnClickListener{
            if(!signUp)
            {
                setIntoSignUpState()
            }
            else {
                presenter.createUser(binding.edLogin.text.toString().trim(),
                    binding.edPassword.text.toString().trim(),
                    binding.edFirstName.text.toString().trim(),
                    binding.edLastName.text.toString())
                    setIntoSignInState()
            }
        }
        presenter= PresenterAuth(this, getSharedPreferences(sharedPrefName,Context.MODE_PRIVATE))

        Log.d("name", "onCreate completed")
    }

    override fun onResume() {
        super.onResume()
        presenter.init()
        presenter.onActivityResume()
        presenter.showUsers()


    }

    override fun onPause() {
        super.onPause()
        presenter.onActivityPause()
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

    override fun showLoginEmptyError() {
        binding.loginError.text=getString(R.string.loginEmpty)
        binding.loginError.visibility=View.VISIBLE

    }

    override fun showPasswordEmptyError() {
        binding.passwordError.text=getString(R.string.passwordEmpty)
        binding.passwordError.visibility=View.VISIBLE
    }

    override fun enterMainScreen() {
        //!!!!!TEMP!!!!!
        presenter.deleteUsers()
        val intent=Intent(this,CatalogActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun setIntoSignUpState(){
        binding.edFirstName.visibility=View.VISIBLE
        binding.edLastName.visibility=View.VISIBLE
        binding.logInButton.visibility=View.INVISIBLE
    }

    private fun setIntoSignInState(){
        binding.edFirstName.visibility=View.GONE
        binding.edLastName.visibility=View.GONE
        binding.logInButton.visibility=View.VISIBLE
    }

}