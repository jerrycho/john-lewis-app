package com.jerry.johnlewis.viewmodel

import app.cash.turbine.test
import com.jerry.johnlewis.BuildConfig
import com.jerry.johnlewis.base.ViewState
import com.jerry.johnlewis.model.ProductListResponse
import com.jerry.johnlewis.network.ProductApi
import com.jerry.johnlewis.repository.NetworkRepository
import com.jerry.johnlewis.ui.product.viewmodel.ProductDetailViewModel
import com.jerry.johnlewis.ui.product.viewmodel.ProductListViewModel
import getProduct
import getProductListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class ProductListModelTest {
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
    fun `test with get detail page ok`() = runBlockingTest {
        launch {
            whenever(mockProductApi.getProductDetail("1")).thenReturn(getProduct())
            val mNetworkRepository = NetworkRepository(mockProductApi)
            val mockViewModel = ProductDetailViewModel(mNetworkRepository)
            mockViewModel.productViewState.test {
                Assert.assertEquals(ViewState.Initial, awaitItem())
                Assert.assertEquals(ViewState.Loading, awaitItem())

                Assert.assertEquals(
                    ViewState.Success(getProduct()),
                    awaitItem()
                )
                awaitComplete()
            }
            mockViewModel.getProduct("1")
        }
    }

}