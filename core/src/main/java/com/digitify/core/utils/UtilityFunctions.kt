package com.digitify.core.utils

import android.os.Build
import java.util.regex.Pattern

class UtilityFunctions {
    fun isValidEID(citizenNumber: String?): Boolean {
        return citizenNumber?.let {
            val expression = "^[0-9]{3}-[0-9]{4}-[0-9]{7}-[0-9]{1}$"
            val pattern = Pattern.compile(expression)
            val matcher = pattern.matcher(citizenNumber)
            return matcher.matches()
        } ?: false
    }

    fun isEmulator(): Boolean {
        return (Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
                || "google_sdk" == Build.PRODUCT)
    }
}
