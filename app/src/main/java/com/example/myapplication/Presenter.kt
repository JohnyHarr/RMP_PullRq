package com.example.myapplication

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.myapplication.Consts.login_min_len
import com.example.myapplication.Consts.password_min_len
import com.example.myapplication.SharedPrefsIDs.isLogged
import com.example.myapplication.SharedPrefsIDs.loggedUserLogin
import com.example.myapplication.SharedPrefsIDs.loggedUserPassword
import io.realm.kotlin.mongodb.exceptions.AuthException
import io.realm.kotlin.mongodb.exceptions.ServiceException


class PresenterAuth(private var view: AuthView,private val sharedPref: SharedPreferences) {
    private var model: UserModel?=null

    fun init(){
        if(sharedPref.getBoolean(isLogged, false)) {
                view.enterAnotherScreen()
        }
    }

    private fun validateLoginPasswordFormat(_login: String, _password: String):Boolean{
        var checkError =false
        if(_login.filter { it in 'A'..'Z' || it in 'a'..'z' || it in '0'..'9'}.length != _login.length
            ||_login.none { it in 'A'..'Z' || it in 'a'..'z' }
            ||_login.length<login_min_len){
            checkError=true
            view.showLoginInvalidFormat()
        }
        if(_login.filter { it in 'A'..'Z' || it in 'a'..'z' || it in '0'..'9'}.length != _login.length
            ||_login.none { it in 'A'..'Z' || it in 'a'..'z' }
            ||_password.length< password_min_len){
            view.showPasswordInvalidFormat()
            checkError=true
        }
        return checkError
    }

    private fun saveUserPrefs(_login: String, _password: String){
        sharedPref.edit(commit = true){
            putBoolean(isLogged, true)
            putString(loggedUserLogin, _login)
            putString(loggedUserPassword, _password)
        }
    }

    fun tryLogInAction(_login:String, _password:String): Boolean{
        try {
            if (validateLoginPasswordFormat(_login, _password)) return false
            model = UserModel()
            model?.logIn(_login, _password)


        }
        catch (exc: AuthException){
            view.showLogInError()
            return false
        }
        catch (exc: IllegalArgumentException){
            view.showToastInternalRealmError()
            return false
        }
        catch (exc: IllegalStateException){
            view.showToastInternalRealmError()
            return false
        }
        catch (exc: ServiceException){
            view.showToastUnableToLogIN()
            return false
        }
        view.enterAnotherScreen()
        saveUserPrefs(_login,_password)
        return true
    }

    suspend fun createUser(_login: String, _password: String, _firstName:String, _lastName: String): Boolean{
        if(validateLoginPasswordFormat(_login,_password)) return false
        model= UserModel()
        try {
            if (model?.signUp(RealmUserData(_login, _password, _firstName, _lastName)) != true) {
                view.showLoginExistError()
                return false
            }
        }
        catch (err: ServiceException){
            view.showToastUnableToLogIN()
        }
            model?.updateData(RealmUserData(_login, _password, _firstName, _lastName))
        return true
    }



    fun deInit(){
        model?.closeRealm()
    }


}
