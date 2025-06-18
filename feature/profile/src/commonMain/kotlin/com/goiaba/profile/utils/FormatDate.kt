package com.goiaba.profile.utils

fun formatDate(dateString: String): String {
    return try {
        // Extract date part and format it nicely
        val datePart = dateString.take(10)
        val timePart = dateString.substring(11, 19)
        "$datePart at $timePart"
    } catch (e: Exception) {
        dateString
    }
}

