package com.example.myapplication

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.example.myapplication.interfaces.IAuthView
import com.example.myapplication.login_exceptions.CredentialsInvalidFormat
import com.example.myapplication.login_exceptions.LoginFormatException
import com.example.myapplication.login_exceptions.PasswordFormatException
import com.example.myapplication.models_and_DB.RealmUserData
import com.example.myapplication.models_and_DB.UserModel
import com.example.myapplication.objects.Consts.login_min_len
import com.example.myapplication.objects.Consts.password_min_len
import com.example.myapplication.objects.SharedPrefsIDs.isLogged
import com.example.myapplication.objects.SharedPrefsIDs.loggedUserLogin
import com.example.myapplication.objects.SharedPrefsIDs.loggedUserPassword
import io.realm.kotlin.mongodb.exceptions.AuthException
import io.realm.kotlin.mongodb.exceptions.InvalidCredentialsException
import io.realm.kotlin.mongodb.exceptions.ServiceException
import kotlinx.coroutines.*


class PresenterAuth(private var view: IAuthView, private val sharedPref: SharedPreferences?) {
    private var model: UserModel? = null

    fun init() {
        if (sharedPref?.getBoolean(isLogged, false) == true) {
            runBlocking {
                tryLogInAction(
                    sharedPref.getString(loggedUserLogin, "").toString(), sharedPref.getString(
                        loggedUserPassword, ""
                    ).toString()
                )
            }
        }
    }

    private fun validateLoginPasswordFormat(_login: String, _password: String) {
        var loginIsValid=true
        var passwordIsValid=true
        if (_login.filter { it in 'A'..'Z' || it in 'a'..'z' || it in '0'..'9' }.length != _login.length
            || _login.none { it in 'A'..'Z' || it in 'a'..'z' }
            || _login.length < login_min_len
        )
            loginIsValid=false
        if (_login.filter { it in 'A'..'Z' || it in 'a'..'z' || it in '0'..'9' }.length != _login.length
            || _login.none { it in 'A'..'Z' || it in 'a'..'z' }
            || _password.length < password_min_len
        )
            passwordIsValid=false;
        if(!passwordIsValid and !loginIsValid)
            throw CredentialsInvalidFormat("Invalid login and password format")
        if(!loginIsValid)
            throw LoginFormatException("invalid login format")
        if(!passwordIsValid)
            throw  PasswordFormatException("invalid password format")
    }

    private fun saveUserPrefs(_login: String, _password: String) {
        sharedPref?.edit(commit = true) {
            putBoolean(isLogged, true)
            putString(loggedUserLogin, _login)
            putString(loggedUserPassword, _password)
        }
    }

    fun onFragmentClose(){
        model?.close()
    }

    suspend fun tryLogInAction(_login: String, _password: String): Boolean {
        try {
            view.showProgressBar()
            validateLoginPasswordFormat(_login, _password)
            withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                model = UserModel()
                model?.logIn(_login, _password)
                Log.d("debug", "log In coroutine")
            }
        } catch (exc: LoginFormatException){
            view.showLoginInvalidFormat()
            return false
        } catch (exc: PasswordFormatException){
            view.showPasswordInvalidFormat()
            return false
        }catch (exc: CredentialsInvalidFormat){
            view.showLoginInvalidFormat()
            view.showPasswordInvalidFormat()
            return false
        } catch (exc: AuthException) {
            view.showLogInError()
            return false
        } catch (exc: IllegalArgumentException) {
            view.showToastInternalRealmError()
            return false
        } catch (exc: InvalidCredentialsException) {
            view.showToastInternalRealmError()
            return false
        } catch (exc: IllegalStateException) {
            view.showToastInternalRealmError()
            return false
        } catch (exc: ServiceException) {
            view.showToastUnableToLogIN()
            return false
        } finally {
            view.hideProgressBar()
        }
        view.enterAnotherScreen()
        saveUserPrefs(_login, _password)
        return true
    }

    suspend fun createUser(
        _login: String,
        _password: String,
        _firstName: String,
        _lastName: String
    ): Boolean {
        try {
            view.showProgressBar()
            validateLoginPasswordFormat(_login, _password)
            model = UserModel()
            if (CoroutineScope(Dispatchers.IO).async {
                    return@async model?.signUp(
                        RealmUserData(_login, _password, _firstName, _lastName)
                    ) != true
                }.await()) {
                view.showLoginExistError()
                return false
            } else {
                view.enterAnotherScreen()
                saveUserPrefs(_login, _password)
                //tryLogInAction(_login,_password)
            }
        } catch (exc: LoginFormatException){
            view.showLoginInvalidFormat()
            return false
        } catch (exc: PasswordFormatException){
            view.showPasswordInvalidFormat()
            return false
        } catch (exc: CredentialsInvalidFormat){
            view.showLoginInvalidFormat()
            view.showPasswordInvalidFormat()
            return false
        } catch (err: ServiceException) {
            view.showToastUnableToLogIN()
        } finally {
            view.hideProgressBar()
        }
        runBlocking {
            model?.updateData(RealmUserData(_login, _password, _firstName, _lastName))
        }
        return true
    }


}
