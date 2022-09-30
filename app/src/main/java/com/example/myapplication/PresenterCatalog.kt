package com.example.myapplication

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.example.myapplication.SharedPrefsIDs.isLogged
import io.realm.kotlin.mongodb.exceptions.AuthException
import io.realm.kotlin.mongodb.exceptions.ServiceException

class PresenterCatalog(private var view: CatalogActivity,private val sharedPref: SharedPreferences) {
    private lateinit var model: UserModel

    fun init(){
        model= UserModel()
        try {
            model.logIn(
                sharedPref.getString(SharedPrefsIDs.loggedUserLogin, "empty")!!,
                sharedPref.getString(
                    SharedPrefsIDs.loggedUserPassword, "empty"
                )!!
            )
        }
        catch (exc: AuthException){
            clearPrefs()
            view.returnToLoginScreen()
            view.showToastUserStoppedOrDeleted()
        }
        catch (exc: IllegalArgumentException){
            clearPrefs()
            view.showToastInternalRealmError()
            view.returnToLoginScreen()
        }
        catch (exc: IllegalStateException){
            clearPrefs()
            view.showToastInternalRealmError()
            view.returnToLoginScreen()
        }
        catch (exc: ServiceException){
            clearPrefs()
            Log.d("input" ,"!!!LogIn interrupt")
            view.showToastUnableToLogIN()
            view.returnToLoginScreen()
        }
    }

    fun check(){
        val result=model.check()
        for (i in result.indices)
            Log.d("debug", "Data: ${result[i]._id}")
    }

    private fun clearPrefs(){
        sharedPref.edit(commit = true){
            putBoolean(isLogged, false)
        }
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