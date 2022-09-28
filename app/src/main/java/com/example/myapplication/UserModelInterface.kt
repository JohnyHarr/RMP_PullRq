package com.example.myapplication

interface UserModelInterface
{
    fun logIn(_login: String, _password:String):Boolean
    fun signUp(user: RealmUserData):Boolean
    fun updateData(user: RealmUserData)
    fun logOut()
    fun closeRealm()

}