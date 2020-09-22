package com.adesoftware.weatherapp.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

sealed class Location {
    data class ZipCode(val zipCode: String): Location()
}

private const val KEY_ZIPCODE = "key_zipCode"

class LocationRepository(context: Context) {
    private val preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    private val _savedLocation: MutableLiveData<Location> = MutableLiveData()

    val savedLocation: LiveData<Location> = _savedLocation

    init {
        preferences.registerOnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key != KEY_ZIPCODE) return@registerOnSharedPreferenceChangeListener

            broadcastSavedZipCode()
        }
        broadcastSavedZipCode()
    }

    fun saveLocation(location: Location) {
        when(location) {
            is Location.ZipCode -> preferences.edit().putString("KEY_ZIPCODE", location.zipCode).apply()
        }
    }

    private fun broadcastSavedZipCode() {
        val zipCode = preferences.getString(KEY_ZIPCODE, "")
        if (zipCode != null && zipCode.isNotBlank()) {
            _savedLocation.value = Location.ZipCode(zipCode)
        }
    }
}