package com.example.miniprj2.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.miniprj2.databinding.ActivityCategoryBinding
import com.example.miniprj2.ui.adapter.CategoryAdapter
import com.example.miniprj2.ui.adapter.ProductAdapter
import com.example.miniprj2.viewmodel.CategoryViewModel
import com.example.miniprj2.viewmodel.ProductViewModel

class CategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryBinding
    private val categoryVM: CategoryViewModel by viewModels()
    private val productVM: ProductViewModel   by viewModels()

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        categoryAdapter = CategoryAdapter { category ->
            // Khi click danh mục → load sản phẩm theo danh mục
            productVM.getByCategory(category.id).observe(this) { products ->
                productAdapter.submitList(products)
            }
        }

        productAdapter = ProductAdapter(
            onItemClick = { /* mở detail nếu cần */ },
            onAddToCart = { /* xử lý giỏ hàng nếu cần */ }
        )

        binding.rvCategories.adapter = categoryAdapter
        binding.rvProductsByCategory.adapter = productAdapter

        categoryVM.allCategories.observe(this) { categories ->
            categoryAdapter.submitList(categories)
            // Mặc định load sản phẩm của danh mục đầu tiên
            if (categories.isNotEmpty()) {
                productVM.getByCategory(categories[0].id).observe(this) { products ->
                    productAdapter.submitList(products)
                }
            }
        }
    }
}