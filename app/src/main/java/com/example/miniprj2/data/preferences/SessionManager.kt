package com.example.miniprj2.data.preferences

import android.content.Context

class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences("MiniPrj2Session", Context.MODE_PRIVATE)

    fun saveSession(userId: Int, username: String) {
        prefs.edit()
            .putBoolean("isLoggedIn", true)
            .putInt("userId", userId)
            .putString("username", username)
            .apply()
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean("isLoggedIn", false)
    fun getUserId(): Int       = prefs.getInt("userId", -1)
    fun getUsername(): String? = prefs.getString("username", null)

    fun clearSession() = prefs.edit().clear().apply()
}