package com.example.myapplication.models_and_DB

import android.graphics.Color

class QueryBuilder() {
    private var query: String=""

    constructor(query: String): this(){
        this.query=query
    }

    fun <T> and(field: String, operator: String, arg: T): QueryBuilder{
        if(field.isEmpty()||operator.isEmpty())
            throw QueryBuilderArgsException("some of the args is empty or null")
        if(arg==null)
            return QueryBuilder(query)
        if(query.isNotEmpty())
            query+=" and "
        return QueryBuilder("$query$field $operator $arg")
    }

    fun <T> or(field: String, operator: String, arg: T): QueryBuilder{
        if(field.isEmpty()||operator.isEmpty()||arg==null)
            throw QueryBuilderArgsException("some of the args is empty or null")
        if(arg!="" || arg is Int && Color.alpha(arg as Int)!=0)
            query+= " or $field $operator $arg"
        return QueryBuilder(query)
    }

    fun getQuery(): String{
        return query
    }

}