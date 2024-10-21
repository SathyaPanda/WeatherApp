package com.sathya.weatherapp.repository.api

data class Sys(
    val country: String,
    val id: String,
    val sunrise: String,
    val sunset: String,
    val type: String
)