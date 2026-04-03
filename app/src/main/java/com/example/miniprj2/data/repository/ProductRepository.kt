package com.example.miniprj2.data.repository

import com.example.miniprj2.data.db.dao.ProductDao
import com.example.miniprj2.data.db.entity.Product
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.*

class ProductRepository(private val dao: ProductDao) {

    fun getTodayProducts(): Flow<List<Product>> {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        return dao.getProductsByDate(today)
    }

    fun getProductsByCategory(categoryId: Int): Flow<List<Product>> =
        dao.getProductsByCategory(categoryId)

    suspend fun getProductById(id: Int): Product? = dao.getProductById(id)
}