package com.jerry.johnlewis.repository

import com.jerry.johnlewis.model.ProductListResponse
import com.jerry.johnlewis.network.ProductApi
import javax.inject.Inject
import com.jerry.johnlewis.BuildConfig
import com.jerry.johnlewis.model.Product

class NetworkRepository @Inject constructor(
    private val mProductApi: ProductApi
) {

    suspend fun getProductList(query:String): ProductListResponse {
        return  mProductApi.getProductList(query, BuildConfig.KEY)
    }

    suspend fun getProductDetail(productId:String): Product {
        return  mProductApi.getProductDetail(productId)
    }

}