package com.example.myapplication

//import android.app.Activity
import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), View.OnTouchListener{
    private lateinit var binding: ActivityMainBinding
    private var name=""
    private var password=""
    private var activityWerePaused=false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
       // Log.d("name", "${binding.greet.text}")
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onResume() {
        super.onResume()
        if (activityWerePaused){
        binding.greet.text="Welcome back $name"
        }
        binding.button.setOnClickListener{
            buttonOnClick()//temp solution. It's gonna ask presenter to do something in future.
        }
       binding.butPasswordVisible.setOnTouchListener(this)

    }

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean{//this function is used for implementing password visibility feature
          when(motionEvent.action){
            MotionEvent.ACTION_DOWN -> {
                binding.edPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                Log.d("name", "password shall be shown")

            }
            MotionEvent.ACTION_UP -> {
                binding.edPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.butPasswordVisible.performClick()
                Log.d("name", "password shall not be shown")
            }
             else -> return false
        }
        return true
    }

    override fun onPause() {
        super.onPause()
        activityWerePaused=true
    }

    private fun buttonOnClick()
    {
        name = binding.editTextTextPersonName.text.toString().trim()
        password=binding.edPassword.text.toString().trim()
        binding.greet.text=getText(R.string.greet_if_logged).toString()+" $name"
        Log.d("name", "name=$name/$password")
    }

}