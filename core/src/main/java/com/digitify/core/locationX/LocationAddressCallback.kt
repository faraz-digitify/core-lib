package com.yap.core.locationX

interface LocationAddressCallback<T> {
    fun onAddressAvailable(response: T) {
    }

    fun onAddressNotAvailable(errorCode: Int, reasonMsg: String) {
    }
}