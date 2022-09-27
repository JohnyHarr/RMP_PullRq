package com.example.myapplication

import android.util.Log
import com.example.myapplication.Consts.app_id
import com.example.myapplication.Consts.db_filename
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

    override fun logIn(_login: String, _password: String): Boolean {
        try {
            runBlocking {
                try {
                    val user = app.login(Credentials.emailPassword(_login, _password))
                    realmUserCfg = SyncConfiguration.Builder(user, setOf(RealmUserData::class))
                        .initialSubscriptions { realmUserDB ->
                            add(
                                realmUserDB.query<RealmUserData>("_id=$0", _login),
                                db_filename
                            )
                        }.build()
                }
                catch (exc: AuthException) { throw exc }
            }
        }
        catch (exc: AuthException){return false}
        realmUserDB=Realm.open(realmUserCfg)
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
            return false
        }
        return logIn(user._id, user.password)
    }

    override fun closeRealm() {
        realmUserDB.close()
    }



    override fun deleteUsers() {
        realmUserDB.writeBlocking {
            // fetch all users from the realm
            val users: RealmResults<RealmUserData> = this.query<RealmUserData>().find()
            // call delete on the results of a query to delete those objects permanently
            delete(users)
        }
        //Realm.deleteRealm(realmUserCfg)//USE IF YOU NEED TO REWRITE User class
    }

}