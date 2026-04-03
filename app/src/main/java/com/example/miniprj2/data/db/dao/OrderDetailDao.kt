package com.example.miniprj2.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.miniprj2.data.db.entity.OrderDetail
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(detail: OrderDetail)

    @Query("SELECT * FROM order_details WHERE orderId = :orderId")
    fun getDetailsByOrderId(orderId: Int): Flow<List<OrderDetail>>
}