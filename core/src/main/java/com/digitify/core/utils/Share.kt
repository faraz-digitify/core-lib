package com.digitify.core.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.util.Log
import androidx.annotation.Keep

@Keep
        /**
         * Opens the url in the available application
         * @return A boolean representing if the action was successful or not
         */
fun Context.openUrl(url: String, newTask: Boolean = false): Boolean {
    return try {
        Intent().apply {
            action = ACTION_VIEW
            addCategory(CATEGORY_BROWSABLE)
            data = Uri.parse(url)
            if (newTask) addFlags(FLAG_ACTIVITY_NEW_TASK)
        }.also {
            val possibleActivitiesList: List<ResolveInfo> =
                packageManager.queryIntentActivities(it, PackageManager.MATCH_ALL)
            if (possibleActivitiesList.size > 1) {
                it.resolveActivity(packageManager)?.run {
                    startActivity(createChooser(it, ""))
                }
            } else {
                it.resolveActivity(packageManager)?.run {
                    startActivity(it)
                }
            }

        }
        true
    } catch (e: Exception) {
        false
    }
}

/**
 * Opens the share context menu
 * @return A boolean representing if the action was successful or not
 */
fun Context.share(
    text: String?,
    subject: String? = "",
    title: String? = null
) {
    try {
        Intent(ACTION_SEND).apply {
            type = "text/plain"
            subject?.let { putExtra(EXTRA_SUBJECT, subject) }
            text?.let { putExtra(EXTRA_TEXT, text) }
        }.also {
            val possibleActivitiesList: List<ResolveInfo> =
                packageManager.queryIntentActivities(it, PackageManager.MATCH_ALL)
            if (possibleActivitiesList.size > 1) {
                it.resolveActivity(packageManager)?.run {
                    startActivity(createChooser(it, title ?: ""))
                }
            } else {
                it.resolveActivity(packageManager)?.run {
                    startActivity(it)
                }
            }
        }
        // success.invoke(true)
    } catch (e: ActivityNotFoundException) {
        Log.w("ShareKT",e.message!!)
    }
}

/**
 * Opens the email application
 * @param email A recipient email
 * @param subject An optional subject of email
 * @param text An option body of the email
 * @return A boolean representing if the action was successful or not
 */
fun Context.sendEmail(
    email: String? = null,
    subject: String? = null,
    text: String? = null
): Boolean {
    return try {
        Intent().apply {
            action = ACTION_SENDTO
            data = Uri.parse("mailto:")
            putExtra(EXTRA_EMAIL, arrayOf(email))
            subject?.let { putExtra(EXTRA_SUBJECT, it) }
            text?.let { putExtra(EXTRA_TEXT, it) }
        }.also {
            val possibleActivitiesList: List<ResolveInfo> =
                packageManager.queryIntentActivities(it, PackageManager.MATCH_ALL)
            if (possibleActivitiesList.size > 1) {
                it.resolveActivity(packageManager)?.run {
                    startActivity(createChooser(it, "Send Email"))
                }

            } else {
                it.resolveActivity(packageManager)?.run {
                    startActivity(it)
                }
            }
        }
        return true
    } catch (e: Exception) {
        false
    }
}

/**
 * Opens the dialer that handles the given number
 * @param number A phone number to open in the dialer
 * @return A boolean representing if the action was successful or not
 */
fun Context.makeCall(number: String?): Boolean {
    return try {
        val intent = Intent(ACTION_DIAL).apply {
            data = Uri.parse("tel:$$number")
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
        true
    } catch (e: Exception) {
        false
    }
}

fun Context.isWhatsAppInstalled(): Boolean {
    val pm: PackageManager = this.packageManager
    val appInstalled: Boolean
    appInstalled = try {
        pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
    return appInstalled
}

fun Context.openWhatsApp() {
    val contact = "+971 4 365 3789" // use country code with your phone number
    val url =
        "https://api.whatsapp.com/send?phone=$contact"
    val i = Intent(ACTION_VIEW)
    i.data = Uri.parse(url)
    if (i.resolveActivity(packageManager) != null)
        startActivity(i)
}

/**
 * Opens the SMS application to send an SMS
 * @param number A phone number to send an SMS
 * @param text An optional predefined text message for the SMS
 * @return A boolean representing if the action was successful or not
 */
fun Context.sendSms(number: String, text: String = ""): Boolean {
    return try {
        Intent(ACTION_VIEW, Uri.parse("sms:$number")).apply {
            putExtra("sms_body", text)
        }.also {
            it.resolveActivity(packageManager)?.run {
                startActivity(it)
            }
        }
        true
    } catch (e: Exception) {
        false
    }
}

/**
 * Opens your application page inside the play store
 * @return A boolean representing if the action was successful or not
 */

fun Context.openPlayStore(): Boolean =
    openUrl("")

fun Context.openTwitter(url: String) {
    if (isPackageInstalled("com.twitter.android")) {
        Intent(ACTION_VIEW).apply {
            data = Uri.parse(url)
            addFlags(FLAG_ACTIVITY_NEW_TASK)
        }.also { intent ->
            intent.resolveActivity(packageManager)?.let {
                startActivity(intent)
            }
        }
    } else {
        openUrl(url)
    }
}

fun Context.openFacebook(uriString: String, url: String) {
    if (isPackageInstalled("com.facebook.katana")) {
        Intent(ACTION_VIEW).apply {
            data = Uri.parse(uriString)
            addFlags(FLAG_ACTIVITY_NEW_TASK)
        }.also { intent ->
            intent.resolveActivity(packageManager)?.let {
                startActivity(intent)
            }
        }
    } else {
        openUrl(url)
    }
}

fun Context.openInstagram(url: String) {
    if (isPackageInstalled("com.instagram.android")) {
        Intent(ACTION_VIEW).apply {
            data = Uri.parse(url)
            addFlags(FLAG_ACTIVITY_NEW_TASK)
        }.also { intent ->
            intent.resolveActivity(packageManager)?.let {
                startActivity(intent)
            }
        }
    } else {
        openUrl(url)
    }
}

fun Context.isPackageInstalled(packageName: String): Boolean {
    return try {
        packageManager.getPackageInfo(packageName, 0) != null
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}

fun Context.isApplicationInstalledAndEnable(packageName: String): Boolean {
    return try {
        packageManager.getApplicationInfo(packageName, 0).enabled
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}

