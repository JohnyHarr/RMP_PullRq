package com.example.myapplication.main

import com.example.myapplication.interfaces.IPdp
import com.example.myapplication.models_and_DB.UserModel

class PdpPresenter(private var view: IPdp) {
    private var model = UserModel()

    fun init(itemId: String) {
        /*model.check()?.forEach {
            if(itemId==it._id){
                view.setupViewWithData(it)
                return
            }
        }*/
        model.getItem(itemId)?.let { view.setupViewWithData(it) }
    }

    fun onFragmentDestroy(){
        model.close()
    }
}