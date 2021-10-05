package com.digitify.core.biometric

import android.content.Context
import androidx.biometric.BiometricManager
import javax.inject.Inject

class BiometricUtils @Inject constructor() {
    fun hasBioMetricFeature(
        context: Context
    ): Boolean {
        val biometricManager = BiometricManager.from(context)
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            else -> false
        }
    }
}