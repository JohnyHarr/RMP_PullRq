package com.example.myapplication

import android.util.Log
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults



class UserModel: UserModelInterface {
    private val realmUsersCfg=RealmConfiguration.Builder(schema = setOf(RealmUserData::class)).build()
    private lateinit var realmUserDB: Realm

    override fun init() {
        realmUserDB=Realm.open(realmUsersCfg)
    }

    override fun closeRealm() {
        realmUserDB.close()
    }

    override fun pushUser(user: RealmUserData){
        realmUserDB.writeBlocking {
            Log.d("debug", "User ${user.login} with ${user.password}")
            copyToRealm(user)
        }
    }
    suspend fun pushUserAsync(user: RealmUserData){
        realmUserDB.write{
            copyToRealm(user)
        }
    }

    override fun getUser(_login: String):RealmUserData? {
        Log.d("debug", "trying to get user")
        return realmUserDB.query<RealmUserData>("login=$0", _login).first().find()
    }

    override fun getUsers():RealmResults<RealmUserData>{
        return  realmUserDB.query<RealmUserData>().find()
    }

    override fun deleteUsers() {
        realmUserDB.writeBlocking {
            // fetch all users from the realm
            val frogs: RealmResults<RealmUserData> = this.query<RealmUserData>().find()
            // call delete on the results of a query to delete those objects permanently
            delete(frogs)
        }
       // Realm.deleteRealm(realmUsersCfg)//USE IF YOU NEED TO REWRITE User class
    }

}