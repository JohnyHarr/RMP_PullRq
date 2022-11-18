package com.example.myapplication.models_and_DB

import android.util.Log
import com.example.myapplication.objects.Consts
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.mongodb.User
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import kotlinx.coroutines.runBlocking

object RealmUserDB {
    private var UserDB: Realm? = null
    private var syncCfg: SyncConfiguration? = null

    fun getInstance(syncConf: SyncConfiguration): Realm? {
        Log.d("debug", "Realm session check")
        if (UserDB == null) {
            Log.d("debug", "open realm")
            UserDB = Realm.open(syncConf)
        }
        return UserDB
    }

    fun getSyncCfg(user: User, _login: String): SyncConfiguration? {
        runBlocking {

            Log.d("debug", "logged in")
            syncCfg = SyncConfiguration.Builder(user, setOf(RealmUserData::class))
                .initialSubscriptions { realmUserDB ->
                    add(
                        realmUserDB.query<RealmUserData>("_id=$0", _login),
                        Consts.db_sub_name
                    )
                }.build()
        }
        return syncCfg
    }

    fun getSyncCfg(): SyncConfiguration? {
        return syncCfg
    }

    fun reset() {
        UserDB = null
        syncCfg = null
    }
}