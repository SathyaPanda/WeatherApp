package com.sathya.weatherapp.repository.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("q") city : String,
        @Query("appid") appId : String
    ) : Response<WeatherModel>


    @GET("data/2.5/weather")
    suspend fun getWeatherDataWithCordinates(
        @Query("lat") latitude : String,
        @Query("lon") longitude : String,
        @Query("appid") appId: String
    ) : Response<WeatherModel>

}