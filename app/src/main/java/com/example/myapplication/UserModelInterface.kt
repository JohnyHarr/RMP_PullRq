package com.example.myapplication

import io.realm.kotlin.query.RealmResults

interface UserModelInterface
{
    fun logIn(_login: String, _password:String):Boolean
    fun signUp(user: RealmUserData):Boolean
    suspend fun updateData(user: RealmUserData)
    fun check(): RealmResults<RealmUserData>?
    fun logOut()
    fun closeRealm()

}