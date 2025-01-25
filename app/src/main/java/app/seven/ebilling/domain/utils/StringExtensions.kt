package app.seven.ebilling.domain.utils

fun String.titlecase(): String {
    return this.split(" ").joinToString(separator = " ") { it.capitalizeFirst() }
}

fun String.capitalizeFirst(): String {
    return this[0].uppercase() + this.substring(1).lowercase()
}

