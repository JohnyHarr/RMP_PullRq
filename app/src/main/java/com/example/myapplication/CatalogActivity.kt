package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityCatalogBinding

class CatalogActivity : AppCompatActivity(), IToastRealmSessionErrors {
    private lateinit var binding: ActivityCatalogBinding
    private lateinit var  presenterCatalog: PresenterCatalog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCatalogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenterCatalog= PresenterCatalog(this, getSharedPreferences(SharedPrefsIDs.sharedPrefName, MODE_PRIVATE))
        presenterCatalog.init()
        binding.logOutBut.text="logOut"
        binding.logOutBut.setOnClickListener{
            Log.d("debug", "Start logout")
            presenterCatalog.logOut()
            returnToLoginScreen()
        }
        Log.d("debug", "onCreateCatalog completed")
    }

    fun returnToLoginScreen(){
        val intent= Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()

    }

    override fun showToastUnableToLogIN(){
        Toast.makeText(this, getString(R.string.logInErrorConnectionIssue), Toast.LENGTH_LONG).show()
    }

    override fun showToastInternalRealmError() {
        Toast.makeText(this, getString(R.string.unknownRealmError), Toast.LENGTH_LONG).show()
    }

    fun showToastUserStoppedOrDeleted() {
        Toast.makeText(this, getString(R.string.userStoppedOrDeleted), Toast.LENGTH_LONG).show()
    }
}