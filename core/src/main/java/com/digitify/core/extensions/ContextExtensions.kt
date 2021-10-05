package com.digitify.core.extensions

import android.content.Context
import android.widget.Toast


/**
Created by Faheem Riaz on 02/08/2021.
 **/

fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
