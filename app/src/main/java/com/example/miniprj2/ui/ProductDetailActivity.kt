package com.example.miniprj2.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.miniprj2.data.db.AppDatabase
import com.example.miniprj2.databinding.ActivityProductDetailBinding
import com.example.miniprj2.viewmodel.OrderViewModel
import com.example.miniprj2.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding
    private val userVM: UserViewModel   by viewModels()
    private val orderVM: OrderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val productId = intent.getIntExtra("PRODUCT_ID", -1)
        if (productId == -1) { finish(); return }

        lifecycleScope.launch {
            val product = AppDatabase.getDatabase(applicationContext).productDao().getProductById(productId)
            product?.let {
                val fmt = NumberFormat.getNumberInstance(Locale("vi", "VN"))
                binding.tvDetailName.text        = it.name
                binding.tvDetailPrice.text       = "${fmt.format(it.price)} đ"
                binding.tvDetailDescription.text = it.description

                binding.btnAddToCart.setOnClickListener {
                    if (!userVM.isLoggedIn()) {
                        Toast.makeText(this@ProductDetailActivity, "Vui lòng đăng nhập!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@ProductDetailActivity, LoginActivity::class.java))
                    } else {
                        orderVM.loadOrCreateOrder(userVM.getUserId())
                        orderVM.currentOrder.observe(this@ProductDetailActivity) { order ->
                            if (order != null) {
                                orderVM.addToCart(product.id, 1, product.price)
                                Toast.makeText(this@ProductDetailActivity, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }
}