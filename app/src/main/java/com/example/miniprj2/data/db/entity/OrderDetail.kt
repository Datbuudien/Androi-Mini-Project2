package com.example.miniprj2.data.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "order_details",
    foreignKeys = [
        ForeignKey(
            entity = Order::class,
            parentColumns = ["id"],
            childColumns = ["orderId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Product::class,
            parentColumns = ["id"],
            childColumns = ["productId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("orderId"), Index("productId")]
)
data class OrderDetail(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val orderId: Int,
    val productId: Int,
    val quantity: Int,
    val unitPrice: Double
)