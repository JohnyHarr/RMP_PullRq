package com.example.myapplication.interfaces

import com.example.myapplication.models_and_DB.RealmItemData
import com.example.myapplication.models_and_DB.RealmUserData
import io.realm.kotlin.query.RealmResults

interface IUserModel
{
    fun logIn(_login: String, _password:String):Boolean
    fun signUp(user: RealmUserData):Boolean
    suspend fun updateData(user: RealmUserData)
    fun check(): RealmResults<RealmItemData>?
    fun getData(query:String): RealmResults<RealmItemData>?
    fun logOut()
    fun getMaxPrice(): Int?
    fun getUser(): RealmUserData?
    fun getItem(id: String): RealmItemData?
    fun close()

}