package com.example.miniprj2.data.repository

import com.example.miniprj2.data.db.dao.CategoryDao
import kotlinx.coroutines.flow.Flow
import com.example.miniprj2.data.db.entity.Category

class CategoryRepository(private val dao: CategoryDao) {
    fun getAllCategories(): Flow<List<Category>> = dao.getAllCategories()
}