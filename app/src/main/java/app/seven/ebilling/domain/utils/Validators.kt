package app.seven.ebilling.domain.utils

fun validatePhoneNumber(phone: String): String {
    return if (phone.length != 11) "Please enter a valid phone number" else ""
}

fun isValidPhoneNumber(phone: String): Boolean {
    return phone.length == 11
}

fun removeCountryCode(phone: String?): String {
    if (phone == null) return ""
   return if (phone.contains("234")) {
      phone.replace("234", "0")
   } else {
      phone
   }
}

fun addCountryCode(phone: String?): String {
    if (phone == null) return ""
   return if (phone.startsWith("0")) {
      phone.replace("0", "234")
   } else {
      phone
   }
}
