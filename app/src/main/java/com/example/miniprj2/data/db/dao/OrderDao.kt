package com.example.miniprj2.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.miniprj2.data.db.entity.Order
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Insert
    suspend fun insert(order: Order): Long

    @Update
    suspend fun update(order: Order)

    @Query("SELECT * FROM orders WHERE userId = :userId AND status = 'PENDING' LIMIT 1")
    suspend fun getPendingOrder(userId: Int): Order?

    @Query("SELECT * FROM orders WHERE id = :orderId LIMIT 1")
    suspend fun getOrderById(orderId: Int): Order?

    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY orderDate DESC")
    fun getOrdersByUser(userId: Int): Flow<List<Order>>
}