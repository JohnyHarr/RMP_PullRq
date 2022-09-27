package com.example.myapplication

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.myapplication.Consts.password_min_len
import com.example.myapplication.SharedPrefsIDs.isLogged
import com.example.myapplication.SharedPrefsIDs.loggedUserLogin
import com.example.myapplication.SharedPrefsIDs.loggedUserPassword


class PresenterAuth(private var view: AuthView,private val sharedPref: SharedPreferences) {
    private var model: UserModel?=null

    fun init(){
       // model= UserModel()
     //   model.init()
        if(sharedPref.getBoolean(isLogged, false)) {
            if(tryLogInAction(sharedPref.getString(loggedUserLogin, "empty")!!, sharedPref.getString(loggedUserPassword, "empty")!!))
                view.enterAnotherScreen()
            else
                sharedPref.edit(commit = true){
                    putBoolean(isLogged, false)
                }

        }
        //model.closeRealm()
    }

    private fun checkLoginPasswordIsEmpty(_login: String, _password: String):Boolean{
        var checkError =false
        if(_login.isEmpty()) {
            view.showLoginEmptyError()
            checkError=true
        }
        if(_password.length<password_min_len) {
            view.showPasswordEmptyError()
            checkError=true
        }
        return checkError
    }

    fun tryLogInAction(_login:String, _password:String): Boolean{
        if(checkLoginPasswordIsEmpty(_login,_password)) return false
        model= UserModel()
        if(model?.logIn(_login,_password)==false) {
            view.showLogInError()
            return false
        }
        view.enterAnotherScreen()
        sharedPref.edit(commit = true){
            putBoolean(isLogged, true)
            putString(loggedUserLogin, _login)
            putString(loggedUserPassword, _password)
        }
        return true
    }

    fun createUser(_login: String, _password: String, _firstName:String, _lastName: String): Boolean{
        if(checkLoginPasswordIsEmpty(_login,_password)) return false
        model= UserModel()
        if(model?.signUp(RealmUserData(_login, _password, _firstName, _lastName))!=true){
            view.showLoginExistError()
            return false
        }
        return true
    }


    fun deleteUsers(){
        model?.deleteUsers()
       sharedPref.edit(commit = true){
           putBoolean(isLogged, false)
           putString(loggedUserLogin,"")
           putString(loggedUserPassword,"")
       }
    }

}
