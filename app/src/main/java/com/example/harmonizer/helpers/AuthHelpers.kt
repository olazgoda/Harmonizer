package com.example.harmonizer.helpers

import android.content.Context
import com.auth0.android.jwt.JWT

fun saveAuthData(context: Context, token: String, email: String, password: String) {
    val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    prefs.edit().putString("jwt_token", token).apply()
    prefs.edit().putString("email", email).apply()
    prefs.edit().putString("password", password).apply()
}

fun clearAuthData(context: Context) {
    val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    prefs.edit().remove("jwt_token").apply()
    prefs.edit().remove("email").apply()
    prefs.edit().remove("password").apply()
}

fun getStoredJwt(context: Context): String? {
    val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    return prefs.getString("jwt_token", null)
}

fun getBearerValue(context: Context): String {
    val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    return "Bearer ${prefs.getString("jwt_token", null)}";
}

fun getFirstAppLaunch(context: Context): Boolean {
    val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    return prefs.getBoolean("first_app_launch", true)
}

fun updateFirstAppLaunch(context: Context, firstAppLaunch: Boolean) {
    val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    prefs.edit().putBoolean("first_app_launch", firstAppLaunch).apply()
}

fun isTokenValid(token: String): Boolean {
    return try {
        val jwt = JWT(token)
        !jwt.isExpired(10)  // Token valid if not expired, with 10 seconds leeway
    } catch (e: Exception) {
        false  // Invalid if token can't be parsed or errors out
    }
}

