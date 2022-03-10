package com.jerry.johnlewis.util

import com.jerry.johnlewis.model.Price

fun getImageUrl(image: String?) : String{
    image?.let{
        return "https:".plus(image!!)
    }
    return ""
}

fun getPrice(price: Price?) : String{
    price?.now?.let{ nowPrice->
        return "£".plus(nowPrice)
    }
    return "£---"
}