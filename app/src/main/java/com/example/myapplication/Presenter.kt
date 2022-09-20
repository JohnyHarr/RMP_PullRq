package com.example.myapplication

import android.content.SharedPreferences

import android.util.Log
import androidx.core.content.edit
import com.example.myapplication.SharedPrefsIDs.isLogged
import com.example.myapplication.SharedPrefsIDs.loggedUserLogin
import com.example.myapplication.SharedPrefsIDs.loggedUserPassword



class PresenterAuth(private var view: AuthView,private val sharedPref: SharedPreferences) {
    private lateinit var model: UserModel

    fun init(){
        model= UserModel()
        model.init()
        if(sharedPref.getBoolean(isLogged, false)) {
            view.enterAnotherScreen()
            tryLogInAction(sharedPref.getString(loggedUserLogin, "empty")!!, sharedPref.getString(loggedUserPassword, "empty")!!)
        }
        model.closeRealm()
    }

    private fun checkLoginPasswordIsEmpty(_login: String, _password: String):Boolean{
        var checkError =false
        if(_login.isEmpty()) {
            view.showLoginEmptyError()
            checkError=true
        }
        if(_password.isEmpty()) {
            view.showPasswordEmptyError()
            checkError=true
        }
        return checkError
    }

    fun tryLogInAction(_login:String, _password:String){
        if(checkLoginPasswordIsEmpty(_login,_password)) return
        val userData:RealmUserData?=model.getUser(_login)
        if(userData!=null){
            if(userData.password != _password)
                view.showLogInError()
            else{
                Log.d("debug", "Login successful")
               sharedPref.edit(commit = true){
                   putBoolean(isLogged, true)
                   putString(loggedUserLogin, _login)
                   putString(loggedUserPassword, _password)
               }
                view.enterAnotherScreen()
        }
        }
        else view.showLogInError()
    }

    fun createUser(_login: String, _password: String, _firstName:String, _lastName: String): Boolean{
        if(checkLoginPasswordIsEmpty(_login,_password)) return false
        if(model.getUser(_login)!=null){
            view.showLoginExistError()
            return false
        }
        val userData=RealmUserData().apply {
            login=_login
            password=_password
            userFirstName=_firstName
            userLastName=_lastName
        }
        Log.d("debug", "trying to push user")
        model.pushUser(userData)
        return true
    }

    fun showUsers(){
       val result=model.getUsers()
        for(i in result.indices){
            Log.d("debug", "User_login: ${result[i].login} " +
                    "User_password: ${result[i].password}")
        }
    }

    fun deleteUsers(){
        model.deleteUsers()
       sharedPref.edit(commit = true){
           putBoolean(isLogged, false)
           putString(loggedUserLogin,"")
           putString(loggedUserPassword,"")
       }
    }

    fun onActivityPause(){
        Log.d("debug", "Activity paused; closing UsersRealm")
        model.closeRealm()
    }

    fun onActivityResume(){
        Log.d("debug", "Activity resumed; initializing UsersRealm")
        model.init()
    }
}