package com.sathya.weatherapp

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.sathya.weatherapp.ui.screens.WeatherPage
import com.sathya.weatherapp.ui.theme.WeatherAppTheme
import com.sathya.weatherapp.ui.theme.violet
import com.sathya.weatherapp.viewmodels.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val weatherViewModel: WeatherViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Set up the permission launcher
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                // Permission is granted, fetch the weather based on location
                fetchWeatherBasedOnLocation()
            } else {
                // Permission is denied, handle the case
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }

        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color = violet
                ){
                    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                    DynamicSkyBackground(hour);
                }
            }
        }
    }

    @Composable
    fun DynamicSkyBackground(hour: Int) {

        val skyGradient = when (hour) {
            in 6..12 -> Brush.linearGradient(
                colors = listOf(Color(0xFFB0E0E6), Color(0xFF87CEFA)) // Morning Gradient
            )
            in 12..18 -> Brush.linearGradient(
                colors = listOf(Color(0xFF87CEFA), Color(0xFF4682B4)) // Afternoon Gradient
            )
            in 18..20 -> Brush.linearGradient(
                colors = listOf(Color(0xFFFFD700), Color(0xFF4682B4)) // Evening Gradient (with sunset color)
            )
            else -> Brush.linearGradient(
                colors = listOf(Color(0xFF2C3E50), Color(0xFF34495E)) // Night Gradient
            )
        }

        Box(
            modifier = Modifier.fillMaxSize()
                .background(brush = skyGradient)
        ) {
            WeatherPage(
                modifier = Modifier.padding(top = 20.dp).padding(10.dp),
                viewModel = weatherViewModel
            )
        }
    }

    @SuppressLint("MissingPermission") // Since we check permission before calling
    private fun fetchWeatherBasedOnLocation() {
        Log.i("SAT**", "location")
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location ->
            location.let {
                // Use the latitude and longitude to fetch the weather data
                val latitude = it.latitude
                val longitude = it.longitude
                fetchWeatherForLocation(latitude, longitude)
            } ?: run {
                Toast.makeText(this, "Failed to retrieve location", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchWeatherForLocation(lat: Double, lon:Double){
        Toast.makeText(this, "Fetching weather for location: $lat, $lon", Toast.LENGTH_SHORT).show()
    }
}