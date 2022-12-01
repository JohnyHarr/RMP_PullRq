package com.example.myapplication.models_and_DB

import android.util.Log
import com.example.myapplication.interfaces.IUserModel
import com.example.myapplication.objects.Consts
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.internal.platform.runBlocking
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.User
import io.realm.kotlin.mongodb.exceptions.AuthException
import io.realm.kotlin.mongodb.exceptions.InvalidCredentialsException
import io.realm.kotlin.mongodb.exceptions.ServiceException
import io.realm.kotlin.mongodb.exceptions.UserAlreadyExistsException
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import io.realm.kotlin.query.RealmResults


class UserModel : IUserModel {
    private var realmUserCfg: SyncConfiguration? = RealmUserDB.getSyncCfg()
    private var realmUserDB: Realm? = realmUserCfg?.let {
        Realm.open(it)
        //RealmUserDB.getInstance(it)
    }
    private var user: User?=null

    private val app = App.create(Consts.app_id)

    override fun logIn(_login: String, _password: String): Boolean {
        try {
            Log.d("debug", "trying to log in")
            runBlocking {
                user = app.login(Credentials.emailPassword(_login, _password))
               user?.let { realmUserCfg = RealmUserDB.getSyncCfg(it, _login) }
                realmUserCfg?.let {
                    realmUserDB = Realm.open(it)
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

    suspend fun insertTestItemData(item: RealmItemData){
        Log.d("debug", "trying to write data")
        realmUserDB?.write {
            copyToRealm(item)
        }
    }

    override fun check(): RealmResults<RealmItemData>? {
        val results=realmUserDB?.query<RealmItemData>()?.find()
        Log.d("debug", "Query elems: ")
        results?.forEach { it-> Log.d("debug", "id: "+ it._id+" descr "+it.description+" inStock: "+it.inStock+" itemName: "
                +it.itemName+" price "+it.price+" color: "+it.color) }
        return results
    }

    override fun getData(query: String): RealmResults<RealmItemData>? {
        if(query=="")
            return null
        val results=realmUserDB?.query<RealmItemData>(query)?.find()
        Log.d("debug", "Query elems: ")
        results?.forEach { it-> Log.d("debug", "id: "+ it._id+" descr "+it.description+" inStock: "+it.inStock+" itemName: "
                +it.itemName+" price "+it.price+" color: "+it.color) }
        return results
    }

    override fun getMaxPrice(): Int? {
        val result= realmUserDB?.query<RealmItemData>()?.max("price", Int::class)?.find()
                Log.d("debug", "Max price: $result")
        return result
    }

    override fun getUser(): RealmUserData? {
       return realmUserDB?.query<RealmUserData>("_id = $0", RealmUserDB.getLogin())?.find()?.first()
    }

    override fun getItem(id: String):RealmItemData? {
        return try{
            realmUserDB?.query<RealmItemData>("_id = $0", id)?.find()?.first()
        } catch (exc: Exception){
            null
        }
    }

    override fun close() {
        if(realmUserDB?.isClosed()==false)
            realmUserDB?.close()
    }
}