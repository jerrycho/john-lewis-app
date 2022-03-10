package com.jerry.johnlewis.ui.product.viewmodel

import com.jerry.johnlewis.R
import com.jerry.johnlewis.base.BaseViewModel
import com.jerry.johnlewis.base.ViewState
import com.jerry.johnlewis.constants.*
import com.jerry.johnlewis.model.Product
import com.jerry.johnlewis.model.ProductListResponse
import com.jerry.johnlewis.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val networkRepository : NetworkRepository) : BaseViewModel() {

    private val _productViewState = MutableStateFlow<ViewState<Product>>(ViewState.Initial)
    val productViewState = _productViewState.asStateFlow()

    fun getProduct(productId : String?){
        productId?.let {
            mUiScope.launch {
                _productViewState.value = ViewState.Loading
                try {
                    val data = mIoScope.async {
                        return@async networkRepository.getProductDetail(productId)
                    }.await()
                    if (data == null)
                        _productViewState.value = ViewState.Failure(R.string.invalid_product)
                    else
                        _productViewState.value = ViewState.Success(data)
                } catch (e: Exception) {
                    _productViewState.value = ViewState.Failure(returnError(e))
                }
            }
        }
    }
}