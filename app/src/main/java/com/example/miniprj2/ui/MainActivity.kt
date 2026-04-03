package com.example.miniprj2.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.miniprj2.databinding.ActivityMainBinding
import com.example.miniprj2.ui.adapter.ProductAdapter
import com.example.miniprj2.viewmodel.OrderViewModel
import com.example.miniprj2.viewmodel.ProductViewModel
import com.example.miniprj2.viewmodel.UserViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val productVM: ProductViewModel by viewModels()
    private val userVM: UserViewModel       by viewModels()
    private val orderVM: OrderViewModel     by viewModels()

    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapter()
        observeProducts()
        setupButtons()
    }

    private fun setupAdapter() {
        adapter = ProductAdapter(
            onItemClick = { product ->
                val intent = Intent(this, ProductDetailActivity::class.java)
                intent.putExtra("PRODUCT_ID", product.id)
                startActivity(intent)
            },
            onAddToCart = { product ->
                if (!userVM.isLoggedIn()) {
                    Toast.makeText(this, "Vui lòng đăng nhập trước!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LoginActivity::class.java))
                } else {
                    // Đảm bảo đã load order rồi mới add
                    orderVM.loadOrCreateOrder(userVM.getUserId())
                    orderVM.currentOrder.observe(this) { order ->
                        if (order != null) {
                            orderVM.addToCart(product.id, 1, product.price)
                            Toast.makeText(this, "Đã thêm ${product.name} vào giỏ", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        )
        binding.rvProducts.adapter = adapter
    }

    private fun observeProducts() {
        productVM.todayProducts.observe(this) { products ->
            adapter.submitList(products)
            binding.tvEmpty.visibility =
                if (products.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
        }
    }

    private fun setupButtons() {
        // Nút đăng nhập / đăng xuất
        updateLoginButton()

        binding.btnLogin.setOnClickListener {
            if (userVM.isLoggedIn()) {
                userVM.logout()
                updateLoginButton()
                Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show()
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }

        // Nút xem danh mục
        binding.btnCategories.setOnClickListener {
            startActivity(Intent(this, CategoryActivity::class.java))
        }

        // Nút giỏ hàng
        binding.btnCart.setOnClickListener {
            if (!userVM.isLoggedIn()) {
                Toast.makeText(this, "Vui lòng đăng nhập trước!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
            } else {
                startActivity(Intent(this, CartActivity::class.java))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateLoginButton()
    }

    private fun updateLoginButton() {
        if (userVM.isLoggedIn()) {
            binding.btnLogin.text = "Đăng xuất (${userVM.getUsername()})"
        } else {
            binding.btnLogin.text = "Đăng nhập"
        }
    }
}