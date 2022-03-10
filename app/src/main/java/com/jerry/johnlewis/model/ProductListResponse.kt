package com.jerry.johnlewis.model

import com.jerry.johnlewis.constants.SHOWN_ITEM_COUNT

data class ProductListResponse (
    val products : List<ProductListItem> = emptyList()
){
    fun getProductList() : List<ProductListItem> {
        if (products.size>= SHOWN_ITEM_COUNT)
            return products.subList(0, SHOWN_ITEM_COUNT)
        return products
    }
}