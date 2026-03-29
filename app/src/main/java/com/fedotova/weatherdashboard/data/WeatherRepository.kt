package com.fedotova.weatherdashboard.data

import kotlinx.coroutines.delay
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
}