package com.digitify.core.networkX

import com.digitify.core.R
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.provider.Settings
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


/**
 * Check if the device is connected with the Internet.
 */
fun Context.isConnectedToInternet(): Boolean {
    val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)

    return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
}

/**
 * Check if the device is in airplane mode.
 */
fun Context.isAirplaneModeOn(): Boolean {
    return Settings.Global.getInt(
        contentResolver,
        Settings.Global.AIRPLANE_MODE_ON,
        0
    ) != 0
}

/**
 * Ping google.com to check if the internet connection is active.
 * It must be called from a background thread.
 */
fun hasActiveInternetConnection(): Boolean {
    try {
        val urlConnection =
            URL("https://www.google.com").openConnection() as HttpURLConnection

        urlConnection.setRequestProperty("User-Agent", "Test")
        urlConnection.setRequestProperty("Connection", "close")
        urlConnection.connectTimeout = 1500
        urlConnection.connect()

        return urlConnection.responseCode == 200
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return false
}

/**
 * Open the system settings.
 */
fun Context.turnOnMobileData() {
    try {
        startActivity(Intent(Settings.ACTION_SETTINGS))
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(this, "It cannot open settings!", Toast.LENGTH_LONG).show()
    }
}

/**
 * Open the wifi settings.
 */
fun Context.turnOnWifi() {
    try {
        startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(this, "It cannot open settings!", Toast.LENGTH_LONG).show()
    }
}

/**
 * Open the airplane mode settings.
 */
fun Context.turnOffAirplaneMode() {
    try {
        startActivity(Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS))
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(this, "It cannot open settings!", Toast.LENGTH_LONG).show()
    }
}

fun AppCompatActivity.showNetworkXSnackBarFire() {
    val networkXSnackBarFire = NetworkXSnackBarFire.Builder(
        (window.decorView as FrameLayout).findViewById(R.id.content),
        lifecycle
    ).apply {
        snackbarProperties.apply {
            connectionCallback = object : NetworkXCallback { // Optional
                override fun hasActiveConnection(hasActiveConnection: Boolean) {
                }
            }
        }
    }.build()
}

