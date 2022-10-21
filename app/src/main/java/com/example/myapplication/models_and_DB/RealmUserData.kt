package com.example.myapplication.models_and_DB

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class RealmUserData(): RealmObject{
 @PrimaryKey var _id: String = ""
 var password: String = ""
 var userFirstName: String = ""
 var userLastName: String = ""

 constructor(_login: String, _password: String, _userFname: String, _userLname: String): this() {
  _id=_login
  password=_password
  userFirstName=_userFname
  userLastName=_userLname
 }
}

