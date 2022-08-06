package com.thinkup.mvi.exception

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import com.thinkup.mvi.TkupApplication
import com.thinkup.mvi.MainActivity
import com.thinkup.common.ZERO_VALUE
import com.thinkup.common.services.SessionException
import com.thinkup.common.services.UnavailableException
import java.lang.ref.WeakReference
import kotlin.system.exitProcess

class GracefulCrashHandler(
    private val application: TkupApplication,
    private val systemHandler: Thread.UncaughtExceptionHandler?
) : Thread.UncaughtExceptionHandler, Application.ActivityLifecycleCallbacks {

    companion object {
        private const val EXTRA_RESTARTED = "crash_handler_restarted"
        const val EXTRA_SESSION_EXPIRED = "crash_session_expired"
        const val EXTRA_ERROR = "crash_handler_error"
    }

    private var startedActivities = Int.ZERO_VALUE()
    private var lastStartedActivity = WeakReference<Activity>(null)

    init {
        application.registerActivityLifecycleCallbacks(this)
    }

    override fun uncaughtException(thread: Thread, error: Throwable) {
        val lastActivity = lastStartedActivity.get()
        var shouldKillProcess = true
        lastActivity?.let {
            val wasAlreadyRestarted = lastActivity.intent.getBooleanExtra(EXTRA_RESTARTED, false)
            shouldKillProcess = reviveApp(wasAlreadyRestarted, error)
            firebaseCrash(thread, error)
        } ?: run {
            firebaseCrash(thread, error)
        }
        finishLastActivity(lastActivity)
        if (shouldKillProcess && (error !is SessionException && error !is UnavailableException))
            killCurrentProcess()
    }

    private fun firebaseCrash(thread: Thread, error: Throwable) {
        systemHandler?.uncaughtException(thread, error)
    }

    private fun reviveApp(wasAlreadyRestarted: Boolean, error: Throwable): Boolean {
        val intent: Intent
        var shouldKillProcess = true
        if (error is SessionException) {
            shouldKillProcess = false
            intent = Intent(application, MainActivity::class.java)
            intent.putExtra(EXTRA_SESSION_EXPIRED, true)
        } else if (error is UnavailableException) {
            intent = Intent(application, UnavailableActivity::class.java)
        } else if (wasAlreadyRestarted) {
            intent = Intent(application, MainActivity::class.java)
        } else {
            intent = Intent(application, CrashActivity::class.java)
            intent.putExtra(EXTRA_RESTARTED, true)
            intent.putExtra(EXTRA_ERROR, Exception(error.message))
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        application.startActivity(intent)
        return shouldKillProcess
    }

    private fun finishLastActivity(lastActivity: Activity?) {
        if (lastActivity != null) {
            //We finish the activity, this solves a bug which causes infinite recursion.
            //See: https://github.com/ACRA/acra/issues/42
            lastActivity.finish()
            lastStartedActivity.clear()
        }
    }

    private fun killCurrentProcess() {
        android.os.Process.killProcess(android.os.Process.myPid())
        exitProcess(10)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
        startedActivities++
        lastStartedActivity = WeakReference<Activity>(activity)
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
        startedActivities--
        if (startedActivities <= Int.ZERO_VALUE()) {
            lastStartedActivity = WeakReference<Activity>(null)
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }
}