package com.example.myapplication

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class Model {
    private val realmCfg=RealmConfiguration.Builder(schema = setOf(RealmUserData::class)).build()
    private val realm= Realm.open(realmCfg)
    fun pushUser(user: RealmUserData){
        realm.writeBlocking {
            copyToRealm(user.apply {})
        }
    }
    fun getUser(_login: String): RealmUserData{
        return
    }
}