package com.jerry.johnlewis.other

import com.jerry.johnlewis.model.Price
import com.jerry.johnlewis.util.getImageUrl
import com.jerry.johnlewis.util.getPrice
import org.junit.Assert
import org.junit.Test

class FunctionTest {

    @Test
    fun test_get_price_function() {
        val price = Price(
            currency = null,
            was = null,
            then1 = null,
            then2 = null,
            now = "1.00"
        )
        val expired = "£1.00"
        val actual = getPrice(price)
        Assert.assertEquals(expired, actual)
    }

    @Test
    fun test_get_price_function_with_null() {
        val price = Price(
            currency = null,
            was = null,
            then1 = null,
            then2 = null,
            now = null
        )
        val expired = "£---"
        val actual = getPrice(price)
        Assert.assertEquals(expired, actual)
    }

    @Test
    fun test_get_image_function() {
        val expired = "https://apple.jpg"
        val actual = getImageUrl("//apple.jpg")
        Assert.assertEquals(expired, actual)
    }

    @Test
    fun test_get_image_function_null_case() {
        val expired = ""
        val actual = getImageUrl(null)
        Assert.assertEquals(expired, actual)
    }

    @Test
    fun test_get_image_function_empty_string_case() {
        val expired = "https:"
        val actual = getImageUrl("")
        Assert.assertEquals(expired, actual)
    }
}