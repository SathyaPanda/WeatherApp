package com.sathya.weatherapp.repository.api

data class Weather(
    val description: String,
    val icon: String,
    val id: String,
    val main: String
)