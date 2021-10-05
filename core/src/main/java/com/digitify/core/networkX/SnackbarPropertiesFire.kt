package com.digitify.core.networkX

import com.digitify.core.networkX.NetworkXCallback
import com.google.android.material.snackbar.BaseTransientBottomBar

class SnackbarPropertiesFire(
    var connectionCallback: NetworkXCallback?,
    @BaseTransientBottomBar.Duration var duration: Int,
    var noInternetConnectionMessage: String,
    var onAirplaneModeMessage: String,
    var snackbarActionText: String,
    var showActionToDismiss: Boolean,
    var snackbarDismissActionText: String
)