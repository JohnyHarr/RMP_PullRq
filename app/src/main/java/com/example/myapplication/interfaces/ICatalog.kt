package com.example.myapplication.interfaces

import com.example.myapplication.models_and_DB.RealmItemData
import com.example.myapplication.models_and_DB.RealmUserData
import io.realm.kotlin.query.RealmResults

interface ICatalog {
    fun returnToLoginScreen()
    fun setRecyclerData(itemList: RealmResults<RealmItemData>)
    fun setRecyclerLayoutManagerToGrid()
    fun setPriceFilterValueTo(value: Float)
    fun setupNavHeaderWithUserData(userData: RealmUserData)
}