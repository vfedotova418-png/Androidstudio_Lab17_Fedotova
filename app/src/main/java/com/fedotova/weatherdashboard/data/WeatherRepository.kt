package com.fedotova.weatherdashboard.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.random.Random

class WeatherRepository {
    private var shouldFail = false
    suspend fun fetchTemperature(): Int {
        delay(2000)
        if (shouldFail) {
            throw Exception("Сервер недоступен")
        }
        return Random.nextInt(15, 35)
    }
    suspend fun fetchHumidity(): Int {
        delay(1500)
        return Random.nextInt(40, 80)
    }
    suspend fun fetchWindSpeed(): Int {
        delay(1000)
        return Random.nextInt(0, 20)
    }
    fun toggleErrorSimulation() {
        shouldFail = !shouldFail
    }
    suspend fun calculateWeatherIndex(
        temp: Int,
        humidity: Int,
        wind: Int
    ): Int {
        return withContext(Dispatchers.Default) {
            var result = 0
            for (i in 1..1000000) {
                result += (temp + humidity + wind) / 3
            }
            result / 1000000
        }
    }
}