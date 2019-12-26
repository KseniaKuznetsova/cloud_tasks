package com.example.testapprr

import android.annotation.SuppressLint
import android.view.View
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

@SuppressLint("SimpleDateFormat")
fun String.convertDateToLong(): Long {
    val df = SimpleDateFormat("dd.MM.yyyy HH:mm")
    return df.parse(this).time
}

@SuppressLint("SimpleDateFormat")
fun Long.convertLongToTime(): String {
    val date = Date(this)
    val format = SimpleDateFormat("dd.MM.yyyy HH:mm")
    return format.format(date)
}

@SuppressLint("SimpleDateFormat")
fun convertCalendarToData(date: Calendar): String {
    return String.format("%1\$td.%1\$tm.%1\$tY %1\$tH:%1\$tM", date)
}
