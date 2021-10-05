package com.digitify.core.locationX

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.common.util.CollectionUtils
import com.google.android.gms.maps.model.LatLng
import com.yap.core.locationX.LocationAddressCallback
import com.yap.core.locationX.LocationModel
import java.util.*

class GeoCoderUtils {

    fun execute(
        context: Context,
        latLng: LatLng,
        callback: LocationAddressCallback<LocationModel>
    ) {
        val locationModel = LocationModel()
        try {
            val addresses: MutableList<Address>
            val geocoder = Geocoder(context, Locale.ENGLISH)
            addresses =
                geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (!CollectionUtils.isEmpty(addresses)) {
                val fetchedAddress = addresses[0]
                if (fetchedAddress.maxAddressLineIndex > -1) {
                    locationModel.locationAddress = fetchedAddress.getAddressLine(0)
                    fetchedAddress.locality?.let {
                        locationModel.locationCityName = it
                    }
                    fetchedAddress.subLocality?.let {
                        locationModel.locationAreaName = it
                    }
                    fetchedAddress.adminArea?.let {
                        locationModel.provinceAreaName = it
                    }
                    fetchedAddress.countryName?.let {
                        locationModel.countryName = it
                    }
                    fetchedAddress.countryCode?.let {
                        locationModel.countryCode = it
                    }
                }
                callback.onAddressAvailable(locationModel)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            callback.onAddressNotAvailable(1, "Something went wrong!")
        }
    }
}