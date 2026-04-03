package com.example.miniprj2.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.miniprj2.data.db.AppDatabase
import com.example.miniprj2.data.db.entity.OrderDetail
import com.example.miniprj2.databinding.ActivityCartBinding
import com.example.miniprj2.viewmodel.OrderViewModel
import com.example.miniprj2.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private val userVM: UserViewModel   by viewModels()
    private val orderVM: OrderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = userVM.getUserId()
        orderVM.loadOrCreateOrder(userId)

        orderVM.currentOrder.observe(this) { order ->
            if (order == null) return@observe

            orderVM.getCartItems(order.id).observe(this) { items ->
                displayCartItems(items)
            }

            binding.btnCheckout.setOnClickListener {
                if (order.status == "PENDING") {
                    orderVM.checkout()
                } else {
                    Toast.makeText(this, "Đơn hàng đã được thanh toán", Toast.LENGTH_SHORT).show()
                }
            }
        }

        orderVM.checkoutSuccess.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Thanh toán thành công! Cảm ơn bạn.", Toast.LENGTH_LONG).show()
                binding.tvCartSummary.text = "✅ Đã thanh toán thành công!"
                binding.btnCheckout.isEnabled = false
            }
        }
    }

    private fun displayCartItems(items: List<OrderDetail>) {
        val fmt = NumberFormat.getNumberInstance(Locale("vi", "VN"))
        val sb = StringBuilder()
        var total = 0.0

        if (items.isEmpty()) {
            binding.tvCartItems.text = "Giỏ hàng trống"
            binding.tvCartSummary.text = ""
            return
        }

        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)
            items.forEach { detail ->
                val product = db.productDao().getProductById(detail.productId)
                val subtotal = detail.quantity * detail.unitPrice
                total += subtotal
                sb.appendLine("• ${product?.name ?: "Sản phẩm"}")
                sb.appendLine("  ${detail.quantity} x ${fmt.format(detail.unitPrice)} đ = ${fmt.format(subtotal)} đ")
                sb.appendLine()
            }
            binding.tvCartItems.text   = sb.toString()
            binding.tvCartSummary.text = "Tổng cộng: ${fmt.format(total)} đ"
        }
    }
}