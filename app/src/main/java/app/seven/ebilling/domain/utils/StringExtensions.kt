package app.seven.ebilling.domain.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.titlecase(): String {
    return this.split(" ").joinToString(separator = " ") { it.capitalizeFirst() }
}

fun String.capitalizeFirst(): String {
    return this[0].uppercase() + this.substring(1).lowercase()
}

fun convertMillisToDate(millis: Long?): String {
    if(millis == null) return ""

    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}
