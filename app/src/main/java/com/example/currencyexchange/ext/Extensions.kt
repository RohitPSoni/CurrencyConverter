package com.example.currencyexchange.ext

import android.view.View
import com.example.currencyexchange.local.DEFAULT_CACHE_TIME_OUT
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

fun showHideUI(visibility: Int, vararg views: View){
    views.forEach {
        it.visibility = visibility
    }
}

fun Long.toDisplayString(): String {
    val simpleDateFormat = SimpleDateFormat(DATE_DISPLAY_FORMAT, Locale.getDefault())
    return simpleDateFormat.format(Date(this))
}

fun Long.isCachedTimeout(timeout: Long = DEFAULT_CACHE_TIME_OUT) =
    TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - this) > timeout