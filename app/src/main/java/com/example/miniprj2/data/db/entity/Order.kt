package com.example.miniprj2.data.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "orders",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("userId")]
)
data class Order(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val orderDate: String,
    val totalAmount: Double = 0.0,
    val status: String = "PENDING"  // PENDING | PAID
)