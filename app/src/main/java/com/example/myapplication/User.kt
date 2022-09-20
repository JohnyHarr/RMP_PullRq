package com.example.myapplication

import io.realm.kotlin.types.ObjectId
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey


class RealmUserData:RealmObject{
   @PrimaryKey
    var login=""
    //get(){return username}
    var password=""
    var userFirstName=""
    var userLastName=""
   // get(){return password}
    }

