package com.example.diary_final.apiitem

data class CityItem(
    val code: String,
    val location: List<CityLocation>
)

data class CityLocation(
    val name: String,
    val id: String
)
data class WeatherItem(
    val code: String,
    val daily: List<WeatherDaily>
)

data class WeatherDaily(
    val fxDate: String,
    val textDay: String
)
