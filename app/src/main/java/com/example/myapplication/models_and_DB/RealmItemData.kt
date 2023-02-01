package com.example.myapplication.models_and_DB

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class RealmItemData(): RealmObject {
    @PrimaryKey
    var _id: String = ""
    var color: Int = 0
    var description: String = ""
    var inStock: Boolean = false
    var itemName: String = ""
    var partition: String = ""
    var price: Int = 0

    constructor(_id: String, _itemName: String, _description: String, _inStock: Boolean, _price: Int, color: Int, _partition: String): this(){
        this._id=_id
        itemName=_itemName
        description=_description
        this.color=color
        inStock=_inStock
        partition=_partition
        price=_price
    }
}