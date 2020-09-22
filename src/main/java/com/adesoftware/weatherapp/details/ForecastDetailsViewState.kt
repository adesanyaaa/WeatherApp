package com.adesoftware.weatherapp.details

data class ForecastDetailsViewState(
    val temp: Float,
    val description: String,
    val date: String,
    val iconUrl: String)