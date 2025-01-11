package com.example.harmonizer.helpers

import android.content.Context
import com.auth0.android.jwt.JWT

fun saveAuthData(context: Context, token: String, email: String, password: String) {
    val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    prefs.edit().putString("jwt_token", token).apply()
    prefs.edit().putString("email", email).apply()
    prefs.edit().putString("password", password).apply()
}

fun getStoredJwt(context: Context): String? {
    val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    return prefs.getString("jwt_token", null)
}

fun getBearerValue(context: Context): String {
    val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    return "Bearer ${prefs.getString("jwt_token", null)}";
}

fun getStoredEmail(context: Context): String? {
    val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    return prefs.getString("email", null)
}

fun getStoredPassword(context: Context): String? {
    val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    return prefs.getString("password", null)
}

fun isTokenValid(token: String): Boolean {
    return try {
        val jwt = JWT(token)
        !jwt.isExpired(10)  // Token valid if not expired, with 10 seconds leeway
    } catch (e: Exception) {
        false  // Invalid if token can't be parsed or errors out
    }
}

