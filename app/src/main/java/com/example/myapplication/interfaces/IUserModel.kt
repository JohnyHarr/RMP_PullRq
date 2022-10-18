package com.example.myapplication.interfaces

import com.example.myapplication.models_and_DB.RealmUserData
import io.realm.kotlin.query.RealmResults

interface IUserModel
{
    fun logIn(_login: String, _password:String):Boolean
    fun signUp(user: RealmUserData):Boolean
    suspend fun updateData(user: RealmUserData)
    fun check(): RealmResults<RealmUserData>?
    fun logOut()
    fun closeRealm()

}