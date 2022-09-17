package com.example.myapplication

import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey


class RealmUserData():RealmObject{
   @PrimaryKey
    private var user_id: ObjectId=ObjectId.create()
    private var username=""
    private var password=""
    constructor(_userName:String="", _password:String=""):this(){
        username=_userName
        password=_password
    }
}
