package com.example.miniprj2.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.miniprj2.data.db.dao.*
import com.example.miniprj2.data.db.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Database(
    entities = [User::class, Category::class, Product::class, Order::class, OrderDetail::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao
    abstract fun productDao(): ProductDao
    abstract fun orderDao(): OrderDao
    abstract fun orderDetailDao(): OrderDetailDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "miniprj2_database"
                )
                    .addCallback(SeedCallback())
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }

    private class SeedCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    seedData(database)
                }
            }
        }

        private suspend fun seedData(db: AppDatabase) {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            // Users mẫu
            db.userDao().insert(User(username = "admin",  password = "123456",   fullName = "Quản trị viên",  email = "admin@miniprj2.com"))
            db.userDao().insert(User(username = "user1",  password = "password", fullName = "Nguyễn Văn A",   email = "a@miniprj2.com"))

            // Danh mục
            db.categoryDao().insertAll(listOf(
                Category(id = 1, name = "Trái cây nhiệt đới"),
                Category(id = 2, name = "Trái cây nhập khẩu"),
                Category(id = 3, name = "Trái cây theo mùa")
            ))

            // Sản phẩm bán hôm nay
            db.productDao().insertAll(listOf(
                Product(name = "Xoài cát Hòa Lộc",      price = 45000.0,  description = "Xoài ngọt, thơm, cát mịn",          categoryId = 1, saleDate = today),
                Product(name = "Thanh Long ruột đỏ",     price = 30000.0,  description = "Thanh long nhập khẩu cao cấp",       categoryId = 2, saleDate = today),
                Product(name = "Dưa hấu không hạt",      price = 15000.0,  description = "Dưa hấu tươi mát, ngọt lịm",        categoryId = 3, saleDate = today),
                Product(name = "Sầu riêng Musang King",  price = 250000.0, description = "Sầu riêng Malaysia cao cấp",         categoryId = 2, saleDate = today),
                Product(name = "Nhãn lồng Hưng Yên",    price = 35000.0,  description = "Nhãn ngọt, hạt nhỏ, đặc sản",       categoryId = 1, saleDate = today),
                Product(name = "Vải thiều Lục Ngạn",    price = 40000.0,  description = "Vải đặc sản mùa hè Bắc Giang",      categoryId = 3, saleDate = today)
            ))
        }
    }
}