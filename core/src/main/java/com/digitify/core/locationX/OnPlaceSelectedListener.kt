package com.digitify.core.locationX

import com.google.android.libraries.places.api.model.Place

interface OnPlaceSelectedListener {
    fun onPlaceSelected(place: Place)
    fun onPlaceFailure(message: String) {}
    fun onCancelled() {}
}