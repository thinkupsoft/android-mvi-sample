package com.thinkup.common.ui

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class PermissionUtil {
    companion object {
        const val REQUEST_PERMISSION_LOCATION = 200
        const val REQUEST_PERMISSION_ACTIVITY = 201
        const val REQUEST_CAMERA = 203
        const val REQUEST_WRITE_STORAGE = 204
        const val REQUEST_PERMISSION_ALL = 202
        const val REQUEST_CALL_ACTION = 205
    }

    fun check(activity: Activity?, permission: String): Boolean {
        activity?.let {
            return ContextCompat.checkSelfPermission(it, permission) == PackageManager.PERMISSION_GRANTED
        }
        return false
    }

    fun request(activity: Activity?, permission: String, request: Int) {
        activity?.let {
            if (ContextCompat.checkSelfPermission(it, permission) != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(it, permission)) {
                    ActivityCompat.requestPermissions(it, arrayOf(permission), request)
                } else {
                    ActivityCompat.requestPermissions(it, arrayOf(permission), request)
                }
            }
        }
    }

    fun request(activity: Activity?, request: Int, vararg permission: String) {
        activity?.let {
            if (ActivityCompat.shouldShowRequestPermissionRationale(it, permission[0])) {
                ActivityCompat.requestPermissions(it, arrayOf(*permission), request)
            } else {
                ActivityCompat.requestPermissions(it, arrayOf(*permission), request)
            }
        }
    }

    fun request(fragment: Fragment?, permission: String, request: Int) {
        fragment?.let {
            if (ContextCompat.checkSelfPermission(it.requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (it.shouldShowRequestPermissionRationale(permission)) {
                    it.requestPermissions(arrayOf(permission), request)
                } else {
                    it.requestPermissions(arrayOf(permission), request)
                }
            }
        }
    }

    fun request(fragment: Fragment?, request: Int, vararg permission: String) {
        fragment?.let {
            if (it.shouldShowRequestPermissionRationale(permission[0])) {
                it.requestPermissions(arrayOf(*permission), request)
            } else {
                it.requestPermissions(arrayOf(*permission), request)
            }
        }
    }
}