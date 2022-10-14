package com.example.myapplication

import android.app.Activity

interface AuthView: IToastRealmSessionErrors{
    fun showLogInError()//show LogIn error if login or password are wrong
    fun showLoginInvalidFormat()
    fun showPasswordInvalidFormat()
    fun enterAnotherScreen()// used to create new action and send user onto another screen
    fun showLoginExistError()
    fun turnOffAllErrors()
    fun hideKeyboard()
    fun showPasswordToggle()
}