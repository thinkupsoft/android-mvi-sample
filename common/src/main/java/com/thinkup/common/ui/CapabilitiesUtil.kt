package com.thinkup.common.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.provider.Settings
import androidx.fragment.app.Fragment

class CapabilitiesUtil {

    companion object {
        const val REQUEST_PERMISSION_LOCATION = 200
        const val REQUEST_ENABLE_BLUETOOTH = 201
    }

    fun isBleEnabled(): Boolean {
        val adapter = BluetoothAdapter.getDefaultAdapter()
        return adapter != null && adapter.isEnabled
    }

    @SuppressLint("MissingPermission")
    fun requestBluetooth(activity: Activity?) {
        activity?.let {
            val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            it.startActivityForResult(enableIntent, REQUEST_ENABLE_BLUETOOTH)
        }
    }

    fun requestBluetooth(fragment: Fragment?) {
        fragment?.let {
            val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            it.startActivityForResult(enableIntent, REQUEST_ENABLE_BLUETOOTH)
        }
    }

    fun isLocationEnabled(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            lm.isLocationEnabled
        } else {
            val mode = Settings.Secure.getInt(
                context.contentResolver, Settings.Secure.LOCATION_MODE,
                Settings.Secure.LOCATION_MODE_OFF
            )
            mode != Settings.Secure.LOCATION_MODE_OFF
        }
    }

    fun requestLocation(activity: Activity?) {
        activity?.let {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            it.startActivityForResult(intent, REQUEST_PERMISSION_LOCATION)
        }
    }

    fun requestLocation(fragment: Fragment?) {
        fragment?.let {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            it.startActivityForResult(intent, REQUEST_PERMISSION_LOCATION)
        }
    }
}