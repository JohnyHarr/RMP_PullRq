package com.example.myapplication

import com.example.myapplication.databinding.ActivityMainBinding

class Presenter(private val binding: ActivityMainBinding) {
    private lateinit var model: Model
    private
    fun initModel(){
        model=Model()
    }
    fun loginAction(_login:String, _password:String){
        model.pushUser(RealmUserData(_login,_password))
    }
    fun showUsersAction(){
        val user:RealmUserData=model.getUser("JohnyHarr");
        binding.greet.text=user
    }
}