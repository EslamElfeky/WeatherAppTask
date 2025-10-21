package com.example.weatherapp.data.remote.repository

import com.example.weatherapp.data.local.deo.WeatherDao
import com.example.weatherapp.data.local.model.WeatherCache
import com.example.weatherapp.data.remote.api.WeatherApiService
import com.example.weatherapp.data.remote.model.ForecastItem
import com.example.weatherapp.data.remote.model.Main
import com.example.weatherapp.data.remote.model.Weather
import com.example.weatherapp.data.remote.model.WeatherResponse

class WeatherRepository(
    private val api: WeatherApiService,
    private val dao: WeatherDao
) {
    private val apiKey = "0c39c66bff7b18070eefcf86af61a70a"
    suspend fun getLastCachedWeather(): WeatherCache? {
        return dao.getLastWeather()
    }
    suspend fun getCurrentWeather(city: String): Result<WeatherResponse> {
        return try {
            val response = api.getCurrentWeather(city, apiKey)
            // Cache the result
            dao.insertWeather(
                WeatherCache(
                    cityName = response.name,
                    temperature = response.main.temp,
                    condition = response.weather[0].main,
                    description = response.weather[0].description,
                    humidity = response.main.humidity,
                    timestamp = System.currentTimeMillis()
                )
            )
            Result.success(response)
        } catch (e: Exception) {
            // Try to get cached data
            val cached = dao.getWeather(city)
            if (cached != null && System.currentTimeMillis() - cached.timestamp < 3600000) {
                // Return cached data if less than 1 hour old
                Result.success(
                    WeatherResponse(
                        name = cached.cityName,
                        main = Main(cached.temperature, cached.temperature, cached.humidity),
                        weather = listOf(Weather(cached.condition, cached.description, "")),
                        dt = cached.timestamp / 1000
                    )
                )
            } else {
                Result.failure(e)
            }
        }
    }

    suspend fun getWeatherByLocation(lat: Double, lon: Double): Result<WeatherResponse> {
        return try {
            val response = api.getWeatherByCoords(lat, lon, apiKey)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getForecast(city: String): Result<List<ForecastItem>> {
        return try {
            val response = api.getForecast(city, apiKey)
            Result.success(response.list)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}