package com.example.myapplication

import io.realm.kotlin.query.RealmResults

interface UserModelInterface
{
    fun init()
    fun closeRealm()
    fun pushUser(user: RealmUserData)
    fun getUser(_login: String): RealmUserData?
    fun getUsers(): RealmResults<RealmUserData>?
    fun deleteUsers()
}