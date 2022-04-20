package com.example.assignment.utils

import android.content.Context
import android.net.ConnectivityManager
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/** Internet Connection Detector */
fun Context.isNetworkAvailable(): Boolean {
    val service = Context.CONNECTIVITY_SERVICE
    val manager = getSystemService(service) as ConnectivityManager?
    val network = manager?.activeNetworkInfo
    return (network != null)
}

fun formatDate(input: String?): String {
    return try {
        val originalFormat: DateFormat =
            SimpleDateFormat("mm/dd/yyyy hh:mm a", Locale.ENGLISH)
        val targetFormat: DateFormat = SimpleDateFormat("dd MMM yyyy hh:mm a")
//        val targetFormat: DateFormat = SimpleDateFormat("dd MMM yyyy hh:mm a")
        val date: Date = originalFormat.parse(input)
        targetFormat.format(date)
    } catch (e: ParseException) {
        e.printStackTrace()
        ""
    }
}

private fun getDayNumberSuffix(day: Int): String? {
    return if (day in 11..13) {
        "th"
    } else when (day % 10) {
        1 -> "st"
        2 -> "nd"
        3 -> "rd"
        else -> "th"
    }
}

fun isFutureDate(input: String?): Boolean {
    val dateFormat: DateFormat =
        SimpleDateFormat("mm/dd/yyyy hh:mm a", Locale.ENGLISH)
    val inputDate: Date = dateFormat.parse(input)
    val currentDate = Calendar.getInstance().time
    return inputDate.after(currentDate)
}

fun isPastDate(input: String?): Boolean {
    val dateFormat: DateFormat =
        SimpleDateFormat("mm/dd/yyyy hh:mm a", Locale.ENGLISH)
    val inputDate: Date = dateFormat.parse(input)
    val currentDate = Calendar.getInstance().time
    return inputDate.before(currentDate)
}

fun searchClosest(value: Int, a: ArrayList<Int>): Int {
    if (value < a[0]) {
        return a[0]
    }
    if (value > a[a.size - 1]) {
        return a[a.size - 1]
    }
    var lo = 0
    var hi = a.size - 1
    while (lo <= hi) {
        val mid = (hi + lo) / 2
        when {
            value < a[mid] -> {
                hi = mid - 1
            }
            value > a[mid] -> {
                lo = mid + 1
            }
            else -> {
                return a[mid]
            }
        }
    }
    return if (a[lo] - value < value - a[hi]) a[lo] else a[hi]
}
