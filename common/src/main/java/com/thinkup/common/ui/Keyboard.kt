package com.thinkup.common.ui

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager


object Keyboard {
    fun hideKeyboard(activity: Activity?) {
        activity?.let {
            val imm = it.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            var view = it.currentFocus
            if (view == null) {
                view = View(it)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}