package com.example.miniprj2.data.repository

import com.example.miniprj2.data.db.dao.UserDao
import com.example.miniprj2.data.db.entity.User

class UserRepository(private val dao: UserDao) {
    suspend fun login(username: String, password: String): User? = dao.login(username, password)
    suspend fun register(user: User) = dao.insert(user)
}