package com.thinkup.mvi

import android.app.Application
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.thinkup.mvi.exception.GracefulCrashHandler
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TkupApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Firebase.initialize(this)
        setUpCrashHandling(this)
    }

    private fun setUpCrashHandling(application: TkupApplication) {
        val systemHandler = Thread.getDefaultUncaughtExceptionHandler()
        val gracefulCrashHandler: Thread.UncaughtExceptionHandler = GracefulCrashHandler(application, systemHandler)
        Thread.setDefaultUncaughtExceptionHandler(gracefulCrashHandler)
    }
}