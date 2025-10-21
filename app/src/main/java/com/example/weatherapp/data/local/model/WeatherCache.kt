package com.example.weatherapp.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_cache")
data class WeatherCache(
    @PrimaryKey val cityName: String,
    val temperature: Double,
    val condition: String,
    val description: String,
    val humidity: Int,
    val timestamp: Long
)

