package com.jerry.johnlewis.model

data class Product (
    val title : String?,
    val media: Media?,
    val price : Price?,

    val displaySpecialOffer : String?,

    val additionalServices : AdditionalServices?,
    val details: Details?,

    val code : String?,
)