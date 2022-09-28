package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityCatalogBinding

class CatalogActivity : AppCompatActivity() {
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
            intent= Intent(this, MainActivity::class.java)
            finish()
            startActivity(intent)

        }
        Log.d("debug", "onCreateCatalog completed")
    }
}