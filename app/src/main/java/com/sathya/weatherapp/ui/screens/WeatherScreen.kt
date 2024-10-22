package com.sathya.weatherapp.ui.screens

import android.location.Location
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import com.sathya.weatherapp.repository.api.NetworkResponse
import com.sathya.weatherapp.viewmodels.WeatherViewModel

@Composable
fun WeatherScreen(viewModel: WeatherViewModel, location: Location?) {

    val weatherResult = viewModel.weatherResult.observeAsState()

    viewModel.getWeatherDataFromLatitudeAndLongitude(location?.latitude.toString(), location?.longitude.toString())

    when(val result = weatherResult.value){
        is NetworkResponse.Error -> {
            Text(text = result.message)
        }
        NetworkResponse.Loading -> {
            CircularProgressIndicator()
        }
        is NetworkResponse.Success -> {
            WeatherView(data = result.data)
        }
        null -> {}
    }
}