package com.sathya.weatherapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sathya.weatherapp.Constants
import com.sathya.weatherapp.repository.api.NetworkResponse
import com.sathya.weatherapp.repository.api.RetrofitInstance
import com.sathya.weatherapp.repository.api.WeatherModel
import com.sathya.weatherapp.repository.storage.CityPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val cityPreferences: CityPreferences
) : ViewModel() {

    private val weatherApi = RetrofitInstance.weatherApi
    private val _weatherResult = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResult : LiveData<NetworkResponse<WeatherModel>> = _weatherResult

    private val _city = MutableStateFlow("")

    init {
        loadLastCity()
    }

    // Access the last searched city
    private fun loadLastCity() {
        viewModelScope.launch {
            cityPreferences.lastCityFlow.collect { lastCity ->
                _city.value = lastCity
                getWeatherData(lastCity)
            }
        }
    }

    // Save a new city
    fun updateCity(newCity: String) {
        viewModelScope.launch {
            cityPreferences.saveCity(newCity)
            _city.value = newCity
            getWeatherData(newCity)
        }
    }

    private fun getWeatherData(city : String){
        _weatherResult.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = weatherApi.getWeather(city, Constants.appId)
                if (response.isSuccessful){
                    Log.i("WEATHER", "Response : " + response.body().toString())
                    response.body()?.let {
                        _weatherResult.value = NetworkResponse.Success(it)
                    }
                }else {
                    _weatherResult.value = NetworkResponse.Error("Failed to load data")
                }
            }catch (e : Exception){
                _weatherResult.value = NetworkResponse.Error("Failed to load data")
            }
        }
    }
}