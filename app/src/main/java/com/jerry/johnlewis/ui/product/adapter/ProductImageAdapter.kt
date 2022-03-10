package com.jerry.johnlewis.ui.product.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jerry.johnlewis.databinding.IncludeProductImageBinding
import com.jerry.johnlewis.util.getImageUrl

class ProductImageAdapter (val urls: List<String>?) :

    RecyclerView.Adapter<ProductImageAdapter.ProductImageViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductImageAdapter.ProductImageViewHolder {
        val binding = IncludeProductImageBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductImageViewHolder, position: Int) {

        urls?.let {
            holder.bind(urls[position])
        }
    }

    override fun getItemCount(): Int {
        urls?.let{
            return urls.size
        }
        return 0
    }

    class ProductImageViewHolder(val binding: IncludeProductImageBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(item:String){
            item?.let {
                Glide.with(binding.root.context)
                    .load(getImageUrl(item))
                    .centerCrop()
                    .into(binding.ivProductImage)
            }
        }
    }
}