package com.digitify.core.networkX

import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import com.google.android.material.snackbar.Snackbar
import com.digitify.core.R

class NetworkXSnackBarFire(
    private val parent: ViewGroup,
    lifecycle: Lifecycle,
    private val snackbarProperties: SnackbarPropertiesFire,
) : LifecycleObserver {

    private val applicationContext = parent.context.applicationContext

    private var noInternetObserveComponent: NetworkXObserveComponent

    private var connectionCallback: NetworkXCallback? = snackbarProperties.connectionCallback

    private var snackBar: NetworkXFireSnackbar? = null

    init {
        noInternetObserveComponent = NetworkXObserveComponent(
            applicationContext,
            lifecycle,
            object : NetworkXObserveComponent.NoInternetObserverListener {
                override fun onStart() {
                    /* no-op */
                }

                override fun onConnected() {

                    connectionCallback?.hasActiveConnection(true)

                    dismiss()

                }

                override fun onDisconnected(isAirplaneModeOn: Boolean) {

                    connectionCallback?.hasActiveConnection(false)

                    show()

                }

                override fun onStop() {

                    destroy()

                }

            }
        )
    }

    private fun getSnackbar(): NetworkXFireSnackbar? {
        if (snackBar == null) {

            val isAirplaneModeOn = applicationContext.isAirplaneModeOn()

            snackBar = NetworkXFireSnackbar.make(
                parent,
                if (isAirplaneModeOn)
                    snackbarProperties.onAirplaneModeMessage
                else
                    snackbarProperties.noInternetConnectionMessage,
                snackbarProperties.duration
            ).apply {

                // Update action button
                if (snackbarProperties.showActionToDismiss) {
                    setAction(
                        snackbarProperties.snackbarDismissActionText
                    ) {
                        // dismiss
                    }
                } else {
                    setAction(
                        snackbarProperties.snackbarActionText
                    ) {
                        if (isAirplaneModeOn) {
                            context.turnOffAirplaneMode()
                        } else {
                            context.turnOnMobileData()
                        }
                    }
                }

            }

        }

        return snackBar
    }

    fun dismiss() {
        getSnackbar()?.dismiss()

        snackBar = null
    }

    fun show() {
        if (applicationContext.isAirplaneModeOn())
            snackbarProperties.onAirplaneModeMessage
        else
            snackbarProperties.noInternetConnectionMessage

        getSnackbar()?.show()
    }

    fun destroy() {
        getSnackbar()?.dismiss()
    }

    /**
     * Manually start listening.
     */
    fun start() {
        noInternetObserveComponent.start()
    }

    /**
     * Manually stop listening.
     */
    fun stop() {
        noInternetObserveComponent.stop()
    }

    class Builder(
        private val parent: ViewGroup,
        private val lifecycle: Lifecycle
    ) {
        val snackbarProperties = SnackbarPropertiesFire(
            connectionCallback = null,
            duration = Snackbar.LENGTH_INDEFINITE,
            noInternetConnectionMessage = parent.context.getString(R.string.common_display_text_error_no_internet),
            onAirplaneModeMessage =
            parent.context.getString(R.string.default_airplane_mode_snackbar_message),
            snackbarActionText = parent.context.getString(R.string.common_button_settings),
            showActionToDismiss = false,
            snackbarDismissActionText = parent.context.getString(R.string.common_button_ok)
        )

        fun build(): NetworkXSnackBarFire {
            return NetworkXSnackBarFire(
                parent,
                lifecycle,
                snackbarProperties
            )
        }

    }

}