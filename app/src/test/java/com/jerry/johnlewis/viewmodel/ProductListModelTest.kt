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
    fun `test with get list page ok`() = runBlockingTest {
        launch {
            whenever(mockProductApi.getProductList("q", BuildConfig.KEY)).thenReturn(getProductListResponse())
            val mNetworkRepository = NetworkRepository(mockProductApi)
            val mockViewModel = ProductListViewModel(mNetworkRepository)
            mockViewModel.productListViewState.test {
                Assert.assertEquals(ViewState.Initial, awaitItem())
                Assert.assertEquals(ViewState.Loading, awaitItem())

                Assert.assertEquals(
                    ViewState.Success(getProductListResponse()),
                    awaitItem()
                )
                awaitComplete()
            }
            mockViewModel.getProductList("q")
        }
    }


}