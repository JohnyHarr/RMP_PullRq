package com.example.myapplication

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.example.myapplication.app_activities.fragments.CatalogFragment
import com.example.myapplication.models_and_DB.UserModel
import com.example.myapplication.objects.SharedPrefsIDs.isLogged
import io.realm.kotlin.mongodb.exceptions.ServiceException

class PresenterCatalog(
    private var view: CatalogFragment,
    private val sharedPref: SharedPreferences?
) {
    private var model = UserModel()
    fun init() {

    }

    private fun clearPrefs() {
        sharedPref?.edit(commit = true) {
            putBoolean(isLogged, false)
        }
    }


    fun logOut() {
        Log.d("debug", "Logout")
        clearPrefs()
        try {
            model.logOut()
        } catch (exc: ServiceException) {
            Log.d("debug", "Service error while logging out")
        }
        //model.closeRealm()
    }
}