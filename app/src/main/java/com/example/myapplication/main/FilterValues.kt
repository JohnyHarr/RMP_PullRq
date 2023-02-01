package com.example.myapplication.main

data class FilterValues(
    var minPrice: Float?,
    var maxPrice: Float?,
    var priceMaxStartPos: Float,
    var priceMinStartPos: Float,
    var color: Int?,
    var inStock: Boolean?
)