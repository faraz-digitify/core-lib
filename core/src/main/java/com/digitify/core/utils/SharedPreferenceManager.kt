/*
 * *
 *  * Created by farazrasheed on 25/08/2021, 12:11 PM
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 25/08/2021, 12:11 PM
 *
 */

package com.digitify.core.utils

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import com.digitify.core.adjustRefferal.ReferralInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SharedPreferenceManager @Inject constructor(@ApplicationContext val context: Context) {
    private val PREFS_NAME = "YAPB2CPref"
    private val inviterAdjustId = "inviterAdjustId"
    private var sharedPref: SharedPreferences

    init {
        sharedPref = initializeEncryptedSharedPreferencesManager()
    }

    private fun initializeEncryptedSharedPreferencesManager(): SharedPreferences {

        val spec = KeyGenParameterSpec.Builder(
            MasterKey.DEFAULT_MASTER_KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(MasterKey.DEFAULT_AES_GCM_MASTER_KEY_SIZE)
            .build()
        val masterKey = MasterKey.Builder(context)
            .setKeyGenParameterSpec(spec)
            .build()
        return EncryptedSharedPreferences.create(
            context,
            PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun save(KEY_NAME: String, text: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(KEY_NAME, text)
        editor.apply()
    }

    fun save(KEY_NAME: String, value: Int) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putInt(KEY_NAME, value)
        editor.apply()
    }

    fun save(KEY_NAME: String, status: Boolean) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putBoolean(KEY_NAME, status)
        editor.apply()
    }

    fun getValueString(KEY_NAME: String): String? {
        return sharedPref.getString(KEY_NAME, null)
    }

    fun getValueInt(KEY_NAME: String): Int {
        return sharedPref.getInt(KEY_NAME, 0)
    }

    fun getValueBoolien(KEY_NAME: String, defaultValue: Boolean): Boolean {
        return sharedPref.getBoolean(KEY_NAME, defaultValue)
    }

    fun clearSharedPreference() {
        try {
            sharedPref.edit().clear().apply()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun removeValue(KEY_NAME: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.remove(KEY_NAME)
        editor.apply()
    }

    fun saveUserNameWithEncryption(text: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(KEY_USERNAME, text)
        editor.apply()

    }

    fun saveUserDialCodeWithEncryption(text: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(KEY_USERDIALCODE, text)
        editor.apply()

    }

    private fun isNumeric(str: String): Boolean {
        return str.matches("-?\\d+(\\.\\d+)?".toRegex())  //match a number with optional '-' and decimal.
    }

    fun getDecryptedUserName(): String? {
        return getValueString(KEY_USERNAME)
    }

    fun getDecryptedUserDialCode(): String? {
        return getValueString(KEY_USERDIALCODE)
    }

    fun savePassCodeWithEncryption(text: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(KEY_PASSCODE, text)
        editor.apply()

    }

    fun getDecryptedPassCode(): String? {
        return getValueString(KEY_PASSCODE)
    }

    fun getThemeValue(): String? {
        return sharedPref.getString(KEY_THEME, "")
    }

    fun setThemeValue(themeValue: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(KEY_THEME, themeValue)
        editor.apply()
    }

    fun getReferralInfo(): ReferralInfo? {
        return getValueString(inviterAdjustId)?.let {
            return if (it.isNullOrBlank()) {
                null
            } else {
                Gson().fromJson(
                    getValueString(inviterAdjustId),
                    ReferralInfo::class.java
                )
            }
        }
    }

    fun setReferralInfo(referralInfo: ReferralInfo?) {
        save(
            inviterAdjustId,
            if (referralInfo != null) Gson().toJson(referralInfo) else ""
        )
    }

}