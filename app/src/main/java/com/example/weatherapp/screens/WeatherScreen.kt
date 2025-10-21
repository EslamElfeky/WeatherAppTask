package com.example.weatherapp.screens

import WeatherViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.weatherapp.data.remote.model.ForecastItem
import com.example.weatherapp.viewmodel.WeatherUiState

@Composable
fun WeatherScreen(viewModel: WeatherViewModel) {
    var searchText by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search Bar
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Enter city name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = { viewModel.searchCity(searchText) }) {
                    Icon(Icons.Default.Search, "Search")
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = { viewModel.searchCity(searchText) }
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Location Button
        Button(
            onClick = { viewModel.getCurrentLocation() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.LocationOn, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Use Current Location")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Content
        when (val state = uiState) {
            is WeatherUiState.Initial -> {
                Text(
                    "Search for a city or use your current location",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            is WeatherUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            is WeatherUiState.Success -> {
                WeatherContent(state)
            }
            is WeatherUiState.Error -> {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
fun WeatherContent(state: WeatherUiState.Success) {
    Column {
        // Current Weather Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = state.cityName,
                    style = MaterialTheme.typography.headlineMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${state.temperature.toInt()}°C",
                    style = MaterialTheme.typography.displayLarge
                )
                Text(
                    text = state.condition,
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = state.description.capitalize(),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Humidity: ${state.humidity}%",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // 5-Day Forecast
        if (state.forecast.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "5-Day Forecast",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            LazyColumn {
                items(state.forecast.take(40).chunked(8).take(5)) { dayForecasts ->
                    val dayForecast = dayForecasts[0]
                    ForecastItem(dayForecast)
                }
            }
        }
    }
}

@Composable
fun ForecastItem(item: ForecastItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = item.dt_txt.substring(0, 10),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = item.weather[0].description.capitalize(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                text = "${item.main.temp.toInt()}°C",
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}
