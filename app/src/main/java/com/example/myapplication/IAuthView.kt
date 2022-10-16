package com.example.myapplication

interface IAuthView: IToastRealmSessionErrors{
    fun showLogInError()//show LogIn error if login or password are wrong
    fun showLoginInvalidFormat()
    fun showProgressBar()
    fun hideProgressBar()
    fun showPasswordInvalidFormat()
    fun enterAnotherScreen()// used to create new action and send user onto another screen
    fun showLoginExistError()
    fun turnOffAllErrors()
    fun hideKeyboard()
    fun showPasswordToggle()
}