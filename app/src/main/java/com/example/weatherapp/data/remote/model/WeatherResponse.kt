package com.example.weatherapp.data.remote.model

data class WeatherResponse(
    val name: String,
    val main: Main,
    val weather: List<Weather>,
    val dt: Long
)
