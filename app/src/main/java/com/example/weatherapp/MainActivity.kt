package com.example.weatherapp

import WeatherViewModel
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.room.Room
import com.example.weatherapp.data.local.deo.WeatherDatabase
import com.example.weatherapp.data.remote.api.RetrofitInstance
import com.example.weatherapp.data.remote.repository.WeatherRepository
import com.example.weatherapp.screens.WeatherScreen

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Room Database
        val database = Room.databaseBuilder(
            applicationContext,
            WeatherDatabase::class.java,
            "weather_db"
        ).build()

        // Initialize Repository and ViewModel
        val repository = WeatherRepository(RetrofitInstance.api, database.weatherDao())
        viewModel = WeatherViewModel(repository, applicationContext)

        // Request location permission
        requestLocationPermission()

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WeatherScreen(viewModel)
                }
            }
        }
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1000
            )
        }
    }






    }
