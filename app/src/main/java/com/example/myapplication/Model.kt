package com.example.myapplication

import android.util.Log
import com.example.myapplication.Consts.app_id
import com.example.myapplication.Consts.db_sub_name
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.internal.platform.runBlocking
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.exceptions.AuthException
import io.realm.kotlin.mongodb.exceptions.ServiceException
import io.realm.kotlin.mongodb.exceptions.UserAlreadyExistsException
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import io.realm.kotlin.query.RealmResults


class UserModel: UserModelInterface {
    private lateinit var realmUserCfg: SyncConfiguration
    private lateinit var realmUserDB: Realm
    private val app=App.create(app_id)

    override fun logIn(_login: String, _password: String) :Boolean {
        try {

            runBlocking {

                    Log.d("debug", "trying to log in")
                    val user = app.login(Credentials.emailPassword(_login, _password))
                    Log.d("debug", "logged in")
                    realmUserCfg = SyncConfiguration.Builder(user, setOf(RealmUserData::class))
                        .initialSubscriptions { realmUserDB ->
                            add(
                                realmUserDB.query<RealmUserData>("_id=$0", _login),
                                db_sub_name
                            )
                        }.build()


            }
            realmUserDB=Realm.open(realmUserCfg)
        }
        catch (exc: AuthException){throw exc}
        catch (exc: IllegalArgumentException){throw exc}
        catch (exc: IllegalStateException){throw exc}
        catch (exc: ServiceException) {Log.d("debug", "3"); throw exc }

        return true
    }

    override fun signUp(user: RealmUserData): Boolean {
        try {
            runBlocking {
                app.emailPasswordAuth.registerUser(user._id, user.password)
                Log.d("debug", "user registered")
            }
        }
        catch (exc: UserAlreadyExistsException){
            Log.e("debug", "User already exists")
            return false
        }
        catch (exc: ServiceException){

            Log.e("debug", "Some error occurred")
            throw exc
        }
        return logIn(user._id, user.password)
    }

    override fun closeRealm() {
        realmUserDB.close()
    }

    override fun logOut() {
        runBlocking {
            realmUserCfg.user.logOut()
        }
        realmUserDB.close()
    }

    override suspend fun updateData(user: RealmUserData) {
        realmUserDB.write{
            copyToRealm(user)
        }
    }

    override fun check(): RealmResults<RealmUserData> {
        return realmUserDB.query<RealmUserData>().find()
    }
}