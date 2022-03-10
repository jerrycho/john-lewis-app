package com.jerry.johnlewis.repository

import com.jerry.johnlewis.BuildConfig
import com.jerry.johnlewis.network.ProductApi
import getProduct
import getProductListResponse
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class NetworkRepositoryTest {
    private val dispatcher = UnconfinedTestDispatcher()

    private val mockProductApi: ProductApi = mock()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test with get product success`() {
        runTest {
            whenever(mockProductApi.getProductDetail("1")).thenReturn(getProduct())
            val mockNetworkRepository = NetworkRepository(mockProductApi)
            val mockResponse = mockNetworkRepository.getProductDetail("1")

            val actualTitle = mockResponse.title
            val expectedTitle = getProduct().title

            assertEquals(expectedTitle, actualTitle)
        }
    }

    @Test
    fun `test with get list success`() {
        runTest {
            whenever(mockProductApi.getProductList("q", BuildConfig.KEY)).thenReturn(getProductListResponse())
            val mockNetworkRepository = NetworkRepository(mockProductApi)
            val mockResponse = mockNetworkRepository.getProductList("q")

            val actual = mockResponse.products.size
            val expected= getProductListResponse().products.size

            assertEquals(expected, actual)
        }
    }
}