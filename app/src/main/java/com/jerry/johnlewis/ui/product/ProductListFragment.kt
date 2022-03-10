package com.jerry.johnlewis.ui.product

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment

import androidx.recyclerview.widget.DividerItemDecoration
import com.jerry.johnlewis.R
import com.jerry.johnlewis.base.BaseFragment
import com.jerry.johnlewis.base.ViewState
import com.jerry.johnlewis.constants.QUERY_DEFAULT
import com.jerry.johnlewis.databinding.FragmentProductListBinding
import com.jerry.johnlewis.model.ProductListItem
import com.jerry.johnlewis.ui.product.adapter.ProductAdapter
import com.jerry.johnlewis.ui.product.listener.OnProductListItemClickListener
import com.jerry.johnlewis.ui.product.viewmodel.ProductListViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProductListFragment : BaseFragment(R.layout.fragment_product_list){

    private val viewModel by viewModels<ProductListViewModel>()
    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!

    private lateinit var productAdapter: ProductAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentProductListBinding.bind(view)

        binding?.swipeToRefresh?.apply {
            setOnRefreshListener {
                isRefreshing = false
                viewModel.getProductList(QUERY_DEFAULT)
            }
        }

        //setup recycle view
        _binding?.productList?.apply {
            productAdapter = ProductAdapter()
            setHasFixedSize(true)

            productAdapter.setListener(object : OnProductListItemClickListener {
                override fun onItemClick(item : ProductListItem){
                    doNavToDetail(item)
                }
            })

            adapter = productAdapter

            addItemDecoration( DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
            addItemDecoration( DividerItemDecoration(this.context, DividerItemDecoration.HORIZONTAL))
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.productListViewState.collect { viewState->

                    when (viewState) {
                        is ViewState.Success ->{
                            showLoading(false)
                            productAdapter.submitList(viewState.data.getProductList())
                        }
                        is ViewState.Failure ->{
                            showLoading(false)
                            displayRetryDialog(viewState.errorAny)
                        }
                        is ViewState.Loading->{
                            showLoading(true)

                        }
                    }
                }
            }
        }
        _binding?.let {
            setTitleBarTile(getString(R.string.dishwashers))
        }

        viewModel?.let {
            viewModel.getProductList(QUERY_DEFAULT)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    fun doNavToDetail(item: ProductListItem) {
        item?.productId?.let {
            NavHostFragment.findNavController(this).navigate(
                R.id.action_productListFragment_to_productDetailFragment,
                ProductDetailFragment.createBundle(it)
            )
        }
    }

    override fun doRetry() {
        viewModel.getProductList(QUERY_DEFAULT)
    }
}