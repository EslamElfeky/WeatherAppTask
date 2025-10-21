import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.remote.repository.WeatherRepository
import com.example.weatherapp.viewmodel.WeatherUiState
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val repository: WeatherRepository,
    private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Initial)
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    init {
        loadLastWeather()
    }

    private fun loadLastWeather() {
        viewModelScope.launch {
            val lastWeather = repository.getLastCachedWeather()
            if (lastWeather != null) {
                _uiState.value = WeatherUiState.Success(
                    cityName = lastWeather.cityName,
                    temperature = lastWeather.temperature,
                    condition = lastWeather.condition,
                    description = lastWeather.description,
                    humidity = lastWeather.humidity,
                    forecast = emptyList()
                )
                // Try to get forecast for cached city
                val forecastResult = repository.getForecast(lastWeather.cityName)
                if (forecastResult.isSuccess && _uiState.value is WeatherUiState.Success) {
                    val current = _uiState.value as WeatherUiState.Success
                    _uiState.value = current.copy(forecast = forecastResult.getOrNull() ?: emptyList())
                }
            } else {
                getCurrentLocation()
            }
        }
    }

    fun searchCity(city: String) {
        if (city.isBlank()) return

        viewModelScope.launch {
            _uiState.value = WeatherUiState.Loading

            val weatherResult = repository.getCurrentWeather(city)
            val forecastResult = repository.getForecast(city)

            if (weatherResult.isSuccess) {
                val weather = weatherResult.getOrNull()!!
                _uiState.value = WeatherUiState.Success(
                    cityName = weather.name,
                    temperature = weather.main.temp,
                    condition = weather.weather[0].main,
                    description = weather.weather[0].description,
                    humidity = weather.main.humidity,
                    forecast = forecastResult.getOrNull() ?: emptyList()
                )
            } else {
                _uiState.value = WeatherUiState.Error(
                    "Failed to fetch weather: ${weatherResult.exceptionOrNull()?.message}"
                )
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation() {
        _uiState.value = WeatherUiState.Loading

        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 10000
            fastestInterval = 5000
        }

        fusedLocationClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            null
        ).addOnSuccessListener { location ->
            if (location != null) {
                viewModelScope.launch {
                    val result = repository.getWeatherByLocation(
                        location.latitude,
                        location.longitude
                    )
                    if (result.isSuccess) {
                        val weather = result.getOrNull()!!
                        searchCity(weather.name)
                    } else {
                        _uiState.value = WeatherUiState.Error(
                            "Failed to get weather: ${result.exceptionOrNull()?.localizedMessage}"
                        )
                    }
                }
            } else {
                _uiState.value = WeatherUiState.Error("Unable to get location. Please enable GPS")
            }
        }.addOnFailureListener { e ->
            _uiState.value = WeatherUiState.Error(
                "Location error: ${e.localizedMessage}. Please check permissions and GPS"
            )
        }
    }
}