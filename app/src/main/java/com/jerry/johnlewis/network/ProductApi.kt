package com.jerry.johnlewis.network

import com.jerry.johnlewis.model.Product
import com.jerry.johnlewis.model.ProductListResponse
import retrofit2.http.*

interface ProductApi {

    @GET("/search/api/rest/v2/catalog/products/search/keyword")
    suspend fun getProductList(@Query("q") query: String, @Query("key") key: String) : ProductListResponse

    @GET("/mobile-apps/api/v2/products/{productId}")
    suspend fun getProductDetail(@Path("productId") productId: String): Product

}