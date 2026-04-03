package com.example.miniprj2.data.repository

import com.example.miniprj2.data.db.dao.OrderDao
import com.example.miniprj2.data.db.dao.OrderDetailDao
import com.example.miniprj2.data.db.entity.Order
import com.example.miniprj2.data.db.entity.OrderDetail
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.*

class OrderRepository(
    private val orderDao: OrderDao,
    private val detailDao: OrderDetailDao
) {
    suspend fun getOrCreatePendingOrder(userId: Int): Order {
        return orderDao.getPendingOrder(userId) ?: run {
            val now = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            val id  = orderDao.insert(Order(userId = userId, orderDate = now)).toInt()
            orderDao.getOrderById(id)!!
        }
    }

    suspend fun addItem(orderId: Int, productId: Int, quantity: Int, unitPrice: Double) {
        detailDao.insert(OrderDetail(orderId = orderId, productId = productId, quantity = quantity, unitPrice = unitPrice))
    }

    fun getCartItems(orderId: Int): Flow<List<OrderDetail>> = detailDao.getDetailsByOrderId(orderId)

    suspend fun checkout(order: Order) = orderDao.update(order.copy(status = "PAID"))
}