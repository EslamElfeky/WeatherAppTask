package com.example.weatherapp.data.local.deo

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import com.example.weatherapp.data.local.model.WeatherCache

@Dao
interface WeatherDao {
    @Query("SELECT * FROM weather_cache ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastWeather(): WeatherCache?
    @Query("SELECT * FROM weather_cache WHERE cityName = :city")
    suspend fun getWeather(city: String): WeatherCache?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: WeatherCache)
}

@Database(entities = [WeatherCache::class], version = 1)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}