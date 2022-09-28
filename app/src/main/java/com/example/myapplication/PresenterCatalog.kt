package com.example.myapplication

import android.content.SharedPreferences
import android.graphics.ColorSpace
import android.util.Log
import androidx.core.content.edit
import com.example.myapplication.SharedPrefsIDs.isLogged

class PresenterCatalog(private var view: CatalogActivity,private val sharedPref: SharedPreferences) {
    private lateinit var model: UserModel

    fun init(){
        model= UserModel()
        model.logIn(sharedPref.getString(SharedPrefsIDs.loggedUserLogin, "empty")!!, sharedPref.getString(
            SharedPrefsIDs.loggedUserPassword, "empty")!!)
    }

    fun logOut(){
        Log.d("debug", "Logout")
        sharedPref.edit(commit = true){
            putBoolean(isLogged, false)
        }
        model.logOut()
        model.closeRealm()
    }
}