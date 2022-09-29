package com.example.myapplication

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.example.myapplication.SharedPrefsIDs.isLogged
import io.realm.kotlin.query.RealmResults

class PresenterCatalog(private var view: CatalogActivity,private val sharedPref: SharedPreferences) {
    private lateinit var model: UserModel

    fun init(){
        model= UserModel()
        model.logIn(sharedPref.getString(SharedPrefsIDs.loggedUserLogin, "empty")!!, sharedPref.getString(
            SharedPrefsIDs.loggedUserPassword, "empty")!!)
    }

    fun check(){
        val result=model.check()
        for (i in result!!.indices)
            Log.d("debug", "Data: ${result[i]._id}")
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