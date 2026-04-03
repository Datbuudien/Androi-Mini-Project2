package com.example.miniprj2.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.miniprj2.data.db.entity.Product
import com.example.miniprj2.databinding.ItemProductBinding
import java.text.NumberFormat
import java.util.Locale

class ProductAdapter(
    private val onItemClick: (Product) -> Unit,
    private val onAddToCart: (Product) -> Unit
) : ListAdapter<Product, ProductAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            val fmt = NumberFormat.getNumberInstance(Locale("vi", "VN"))
            binding.tvProductName.text  = product.name
            binding.tvProductPrice.text = "${fmt.format(product.price)} đ"
            binding.root.setOnClickListener { onItemClick(product) }
            binding.btnAddToCart.setOnClickListener { onAddToCart(product) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(a: Product, b: Product) = a.id == b.id
        override fun areContentsTheSame(a: Product, b: Product) = a == b
    }
}