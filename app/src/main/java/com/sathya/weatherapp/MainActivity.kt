package com.sathya.weatherapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.core.app.ActivityCompat
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
            modifier = Modifier
                .fillMaxSize()
                .background(brush = skyGradient)
        ) {
            WeatherPage(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .padding(10.dp),
                viewModel = weatherViewModel
            )
        }
    }

    fun getCurrentLocation(context: Context, onLocationReceived: (Location?) -> Unit) {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermission()
                    return
                }
                fusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        Toast.makeText(this, "null received", Toast.LENGTH_SHORT).show()
                    } else {
                        onLocationReceived(location)  // Use callback to return location
                    }
                }
            } else {
                Toast.makeText(this, "Turn on Location", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermission()
        }
    }


    private fun isLocationEnabled() : Boolean{
        val locationManager : LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun requestPermission(){
        ActivityCompat.requestPermissions(
            this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_ACCESS_LOCATION)
    }

    companion object{
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == PERMISSION_REQUEST_ACCESS_LOCATION){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(applicationContext, "Granted", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(applicationContext, "Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkPermissions() : Boolean{
        return (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
    }

    private fun fetchWeatherForLocation(lat: Double, lon:Double){
        Toast.makeText(this, "Fetching weather for location: $lat, $lon", Toast.LENGTH_SHORT).show()

        weatherViewModel.getWeatherDataFromLatitudeAndLongitude(lat.toString(), lon.toString())
    }
}