package com.example.myapplication.models_and_DB

import android.util.Log
import com.example.myapplication.interfaces.IUserModel
import com.example.myapplication.objects.Consts
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.internal.platform.runBlocking
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.exceptions.AuthException
import io.realm.kotlin.mongodb.exceptions.InvalidCredentialsException
import io.realm.kotlin.mongodb.exceptions.ServiceException
import io.realm.kotlin.mongodb.exceptions.UserAlreadyExistsException
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import io.realm.kotlin.query.RealmResults


class UserModel : IUserModel {
    private var realmUserCfg: SyncConfiguration? = RealmUserDB.getSyncCfg()
    private var realmUserDB: Realm? = realmUserCfg?.let {
        RealmUserDB.getInstance(it)
    }
    private val app = App.create(Consts.app_id)

    override fun logIn(_login: String, _password: String): Boolean {
        try {
            Log.d("debug", "trying to log in")
            runBlocking {
                val user = app.login(Credentials.emailPassword(_login, _password))
                realmUserCfg = RealmUserDB.getSyncCfg(user, _login)
                realmUserDB = realmUserCfg?.let {
                    RealmUserDB.getInstance(it)
                }
            }
        } catch (exc: AuthException) {
            throw exc
        } catch (exc: InvalidCredentialsException) {
            throw exc
        } catch (exc: IllegalArgumentException) {
            throw exc
        } catch (exc: IllegalStateException) {
            throw exc
        } catch (exc: ServiceException) {
            Log.d("debug", "3"); throw exc
        }
        return true
    }

    override fun signUp(user: RealmUserData): Boolean {
        try {
            runBlocking {
                app.emailPasswordAuth.registerUser(user._id, user.password)
                Log.d("debug", "user registered")
            }
        } catch (exc: UserAlreadyExistsException) {
            Log.e("debug", "User already exists")
            return false
        } catch (exc: ServiceException) {

            Log.e("debug", "Some error occurred")
            throw exc
        }
        return logIn(user._id, user.password)
    }

    override fun logOut() {
        try {
            runBlocking {
                if (realmUserCfg == null)
                    Log.d("debug", "realmUserCfg is null")
                if (realmUserDB == null)
                    Log.d("debug", "realmUserDB is null")
                app.currentUser?.logOut()
                realmUserDB?.close()
            }
        } catch (exc: ServiceException) {
            throw exc
        } finally {
            RealmUserDB.reset()
        }
    }

    override suspend fun updateData(user: RealmUserData) {
        realmUserDB?.write {
            copyToRealm(user)
        }
    }

    override fun check(): RealmResults<RealmUserData>? {
        return realmUserDB?.query<RealmUserData>()?.find()
    }
}