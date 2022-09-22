package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity

interface AuthView{
    fun showLogInError()//show LogIn error if login or password are wrong
    fun showPasswordEmptyError()
    fun showLoginEmptyError()
    fun enterAnotherScreen()// used to create new action and send user onto another screen
    fun showLoginExistError()
    fun turnOffAllErrors()
    fun setRegistrationState()
}