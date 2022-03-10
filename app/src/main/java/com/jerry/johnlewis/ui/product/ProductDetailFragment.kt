package com.jerry.johnlewis.ui.product

import android.os.Bundle
import android.text.Html
import android.view.View

import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle

import com.google.android.material.tabs.TabLayoutMediator
import com.jerry.johnlewis.R
import com.jerry.johnlewis.base.BaseFragment
import com.jerry.johnlewis.base.ViewState
import com.jerry.johnlewis.databinding.FragmentProductDetailBinding
import com.jerry.johnlewis.model.Product
import com.jerry.johnlewis.ui.product.adapter.ProductImageAdapter
import com.jerry.johnlewis.ui.product.viewmodel.ProductDetailViewModel
import com.jerry.johnlewis.util.getPrice
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.StringBuilder
import android.view.LayoutInflater
import com.jerry.johnlewis.databinding.ItemProductBinding
import com.jerry.johnlewis.databinding.ItemProductSpecificationBinding

private const val KEY_SELECTED = "KEY_SELECTED"
class ProductDetailFragment :BaseFragment(R.layout.fragment_product_detail){

    private val viewModel by viewModels<ProductDetailViewModel>()
    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!
    private var productId : String? = null

    companion object {
        fun createBundle(productId: String) = Bundle().apply {
            putString(KEY_SELECTED, productId)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { bundle->
            productId = bundle.getString(KEY_SELECTED)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProductDetailBinding.bind(view)

        productId?.let{
            viewModel?.let {
                viewModel.getProduct(productId)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.productViewState.collect { viewState->
                    when (viewState){
                        is ViewState.Success->{
                            updateUI(viewState.data)
                            showLoading(false)
                        }
                        is ViewState.Failure->{
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
    }

    fun updateUI(product : Product){
        _binding?.let {
            product?.let {

                //title
                product.title?.let {
                    setTitleBarTile(product.title)
                }

                //image
                val pagerAdapter = ProductImageAdapter(product.media?.images?.urls)
                binding.includeProductImagesViewpager.vpImages.adapter = pagerAdapter
                TabLayoutMediator(
                    binding.includeProductImagesViewpager.tbIndicator,
                    binding.includeProductImagesViewpager.vpImages) { tab, position ->
                }.attach()

                //base product information
                product.price?.let {
                    binding.includeProductBaseInformation.tvPrice.text = getPrice(product.price)
                }
                if (product.displaySpecialOffer.isNullOrEmpty())
                    binding.includeProductBaseInformation.tvSpecialOffer.visibility = View.GONE
                else {
                    binding.includeProductBaseInformation.tvSpecialOffer.visibility = View.VISIBLE
                    binding.includeProductBaseInformation.tvSpecialOffer.text = product.displaySpecialOffer
                }
                product.additionalServices?.includedServices?.let { includedServices->
                    if (includedServices.isNotEmpty()){
                        var stringBuilder = StringBuilder()

                        for (i in 0 until includedServices.size) {
                            stringBuilder.append(includedServices.get(i))
                            if (i <= includedServices.size-2) {
                                stringBuilder.append("\n")
                            }
                        }
                        binding.includeProductBaseInformation.tvIncludedServices.text = stringBuilder.toString()
                        stringBuilder.clear()
                    }
                }

                //Product Information
                product.details?.productInformation?.let{ productInformation->
                    binding.includeProductInformation.tvProductInformation.text = Html.fromHtml(productInformation)
                }
                product.code?.let{ code->
                    binding.includeProductInformation.tvProductCode.text = code
                }

                //Product Specification
                product.details?.features?.let { features->
                    if (features.isNotEmpty()){
                        features.first().let { feature->
                                feature?.let {
                                    if (!feature.attributes.isNullOrEmpty()){
                                        feature.attributes.forEach { attribute->
                                            val itemProductSpecificationBinding: ItemProductSpecificationBinding = ItemProductSpecificationBinding.inflate(layoutInflater)
                                            itemProductSpecificationBinding.tvName.text = attribute.name
                                            itemProductSpecificationBinding.tvValue.text = attribute.value

                                            binding.includeProductSpecification.llSpecifications.addView(itemProductSpecificationBinding.root)
                                        }
                                    }
                                }
                        }

                    }
                }
            }
        }
    }

    override fun doRetry() {
        productId?.let{
            viewModel?.let {
                viewModel.getProduct(productId)
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun showBack(): Boolean {
        return true
    }

    override fun afterClickedCancel(){
        activity?.onBackPressed()
    }

}