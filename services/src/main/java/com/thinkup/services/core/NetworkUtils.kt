package com.thinkup.services.core

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager

internal class NetworkUtils(private val context: Context) {

    @SuppressLint("MissingPermission")
    fun isConnected(): Boolean {
        return try {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            connectivityManager.activeNetwork != null
        } catch (e: Exception) {
            e.printStackTrace()
            true
        }
    }
}