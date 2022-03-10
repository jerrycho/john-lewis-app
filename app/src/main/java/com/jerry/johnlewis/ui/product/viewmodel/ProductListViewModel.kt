package com.jerry.johnlewis.ui.product.viewmodel

import com.jerry.johnlewis.base.BaseViewModel
import com.jerry.johnlewis.base.ViewState
import com.jerry.johnlewis.constants.*
import com.jerry.johnlewis.model.ProductListResponse
import com.jerry.johnlewis.repository.NetworkRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val networkRepository : NetworkRepository
    ) : BaseViewModel() {

    private val _productListViewState = MutableStateFlow<ViewState<ProductListResponse>>(ViewState.Initial)
    val productListViewState = _productListViewState.asStateFlow()

    fun getProductList(query : String){
        mUiScope.launch {
            _productListViewState.value = ViewState.Loading
            try {
                val data = mIoScope.async {
                    return@async networkRepository.getProductList(query)
                }.await()
                _productListViewState.value = ViewState.Success(data)
            } catch (e: Exception) {
                _productListViewState.value = ViewState.Failure(returnError(e))
            }
        }
    }
}