package com.jerry.johnlewis.ui.product.adapter

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jerry.johnlewis.databinding.ItemProductBinding
import com.jerry.johnlewis.model.ProductListItem
import com.jerry.johnlewis.ui.product.listener.OnProductListItemClickListener
import com.jerry.johnlewis.util.getImageUrl
import com.jerry.johnlewis.util.getPrice

class ProductAdapter : ListAdapter<ProductListItem, ProductAdapter.ProductViewHolder>(DIFF_CALLBACK) {

    private lateinit var listener : OnProductListItemClickListener

    open fun setListener(listener: OnProductListItemClickListener){
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return from(parent)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position),listener)
    }

    class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ProductListItem?, listener: OnProductListItemClickListener) {
            item?.let {
                item.title?.let{
                    binding.tvTitle.text = item.title
                }

                binding.tvPrice.text = getPrice(item.price)

                item.image?.let{
                    Glide.with(binding.root.context)
                        .load(getImageUrl(item.image))
                        .centerCrop()
                        .into(binding.ivProductImage)
                }


                listener?.let {
                    binding.root.setOnClickListener{
                        listener.onItemClick(item!!)
                    }
                }
            }
        }
    }

    companion object {

        fun from(parent: ViewGroup): ProductViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemProductBinding.inflate(layoutInflater, parent, false)
            return ProductViewHolder(binding)
        }

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductListItem>() {
            override fun areItemsTheSame(oldItem: ProductListItem, newItem: ProductListItem): Boolean {
                return oldItem.productId == newItem.productId
            }
            override fun areContentsTheSame(oldItem: ProductListItem, newItem: ProductListItem): Boolean {
                return oldItem.productId == newItem.productId && oldItem.title == newItem.title
            }
        }
    }


}