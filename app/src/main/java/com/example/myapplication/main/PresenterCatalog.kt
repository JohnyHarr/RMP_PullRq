package com.example.myapplication.main

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.example.myapplication.interfaces.ICatalog
import com.example.myapplication.models_and_DB.QueryBuilder
import com.example.myapplication.models_and_DB.UserModel
import com.example.myapplication.objects.SharedPrefsIDs.isLogged
import io.realm.kotlin.mongodb.exceptions.ServiceException

class PresenterCatalog(
    private var view: ICatalog,
    private val sharedPref: SharedPreferences?
) {
    private var model = UserModel()
    fun init() {
        /*runBlocking {
            model.insertTestItemData(
                RealmItemData(
                    "https://cdn.danielonline.ru/upload/iblock/720/fqnaj1yhhoqi0pzu9b5gv82stl4ub9yz.png",
                    "ItemName2", "descr3", true, 600,-16777216, "default"
                )
            )
        }*/
        view.setPriceFilterValueTo(model.getMaxPrice()?.toFloat() ?: 100F)
        view.setRecyclerLayoutManagerToGrid()
        model.getUser()?.let {
            view.setupNavHeaderWithUserData(it) }
        model.check()?.let { view.setRecyclerData(it) }
    }

    private fun clearPrefs() {
        sharedPref?.edit(commit = true) {
            putBoolean(isLogged, false)
        }
    }

    fun onFragmentClose(){
        model.close()
    }

    fun updateData(filterValues: FilterValues){
        model.getData(QueryBuilder()
            .and("price","<=", filterValues.maxPrice)
            .and("price", ">=", filterValues.minPrice)
            .and("color","=",filterValues.color)
            .and("inStock", "=", filterValues.inStock).getQuery())
            ?.let { view.setRecyclerData(it) }
    }

    fun logOut() {
        Log.d("debug", "Logout")
        clearPrefs()
        try {
            model.logOut()
        } catch (exc: ServiceException) {
            Log.d("debug", "Service error while logging out")
        }
        view.returnToLoginScreen()
    }
}