package com.loschimbitas.icm_taller3_loschimbitas.util

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import android.location.Location

class TrackerLocation(application: Application) : AndroidViewModel(application) {

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(application)
    private val locationLiveData = MutableLiveData<Location>()
    private var locationCallback: LocationCallback? = null

    fun getLocationLiveData(): LiveData<Location> {
        return locationLiveData
    }

    @SuppressLint("MissingPermission")
    fun requestLocationUpdates() {
        val locationRequest = LocationRequest.create()
            .setInterval(0)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location: Location? = locationResult.lastLocation
                if (location != null) {
                    locationLiveData.value = location
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(locationRequest,
            locationCallback as LocationCallback, null)
    }

    fun stopLocationUpdates() {
        if (locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback!!)
            locationCallback = null
        }
    }
}
