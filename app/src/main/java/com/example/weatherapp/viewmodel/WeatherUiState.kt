package com.example.weatherapp.viewmodel

import com.example.weatherapp.data.remote.model.ForecastItem

sealed class WeatherUiState {
    object Initial : WeatherUiState()
    object Loading : WeatherUiState()
    data class Success(
        val cityName: String,
        val temperature: Double,
        val condition: String,
        val description: String,
        val humidity: Int,
        val forecast: List<ForecastItem> = emptyList()
    ) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}
