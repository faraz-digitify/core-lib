package com.digitify.core.biometric

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun Fragment.showBiometricDialog(
    title: String = "Biometric Authentication",
    subtitle: String = "Enter biometric credentials to proceed.",
    description: String = "Input your Fingerprint or FaceID to ensure it's you!",
    setNegativeButtonText: String = "Cancel",
    cryptoObject: BiometricPrompt.CryptoObject? = null,
    success: (isSuccess: Boolean) -> Unit
) {
    val executor = ContextCompat.getMainExecutor(this.requireContext())
    val callback = object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)
            success.invoke(false)
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            success.invoke(false)
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            success.invoke(true)
        }
    }

    val biometricPrompt = BiometricPrompt(this, executor, callback)
    val promptInfo = createPromptInfo(
        title = title,
        subtitle = subtitle,
        description = description,
        setNegativeButtonText = setNegativeButtonText
    )
    biometricPrompt.apply {
        if (cryptoObject == null) authenticate(promptInfo)
        else authenticate(promptInfo, cryptoObject)
    }
}

private fun createPromptInfo(
    title: String,
    subtitle: String,
    description: String,
    setNegativeButtonText: String
): BiometricPrompt.PromptInfo {
    return BiometricPrompt.PromptInfo.Builder()
        .setTitle(title)
        .setSubtitle(subtitle)
        .setDescription(description)
        .setNegativeButtonText(setNegativeButtonText)
        .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
        .build()
}