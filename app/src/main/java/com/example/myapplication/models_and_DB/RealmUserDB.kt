package com.example.myapplication.models_and_DB

import android.util.Log
import io.realm.kotlin.Realm
import io.realm.kotlin.mongodb.User
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import kotlinx.coroutines.runBlocking

object RealmUserDB {
    private var syncCfg: SyncConfiguration? = null
    private var login: String="_"
    fun getSyncCfg(user: User, _login: String): SyncConfiguration? {
        login=_login
        runBlocking {
            Log.d("debug", "logged in")
            syncCfg = SyncConfiguration.Builder(user, "default", setOf(RealmItemData::class, RealmUserData::class)).build()
        }
        return syncCfg
    }

    fun getSyncCfg(): SyncConfiguration? {
        return syncCfg
    }

    fun getLogin(): String{
        return login
    }

    fun reset() {
        syncCfg = null
    }
}