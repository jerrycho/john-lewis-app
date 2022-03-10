package com.jerry.johnlewis.model

data class ProductListItem(
    val productId : String?,
    val title : String?,
    val image : String?,
    val price : Price? = null
) {

}