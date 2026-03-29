package com.fedotova.weatherdashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fedotova.weatherdashboard.data.WeatherData
import com.fedotova.weatherdashboard.data.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class WeatherViewModel : ViewModel() {
    private val repository = WeatherRepository()
    private val _weatherState = MutableStateFlow(WeatherData())
    val weatherState: StateFlow<WeatherData> = _weatherState.asStateFlow()
    init {
        loadWeatherData()
    }
    fun loadWeatherData() {
        viewModelScope.launch {
            _weatherState.value = _weatherState.value.copy(
                isLoading = true,
                error = null,
                loadingProgress = "Запуск загрузки..."
            )
            try {
                coroutineScope {
                    _weatherState.value = _weatherState.value.copy(
                        loadingProgress = "Загружаем температуру, влажность, скорость ветра..."
                    )
                    val temperatureDeferred = async { repository.fetchTemperature() }
                    val humidityDeferred = async { repository.fetchHumidity() }
                    val windSpeedDeferred = async { repository.fetchWindSpeed() }
                    val temperature = temperatureDeferred.await()
                    val humidity = humidityDeferred.await()
                    val windSpeed = windSpeedDeferred.await()
                    _weatherState.value = WeatherData(
                        temperature = temperature,
                        humidity = humidity,
                        windSpeed = windSpeed,
                        isLoading = false,
                        error = null,
                        loadingProgress = "Загрузка завершена!"
                    )
                }
            } catch (e: Exception) {
                _weatherState.value =_weatherState.value.copy(
                    isLoading = false,
                    error = "Ошибка загрузки: ${e.message}",
                    loadingProgress = ""
                )
            }
        }
    }
    fun toggleErrorSimulation() {
        repository.toggleErrorSimulation()
    }
}