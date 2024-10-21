package com.sathya.weatherapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sathya.weatherapp.R
import com.sathya.weatherapp.repository.api.WeatherModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun WeatherView(data : WeatherModel){
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Start
        ){
            Icon(
                imageVector = Icons.Default.LocationOn,
                tint = Color.White,
                contentDescription = "Location icon",
                modifier = Modifier.size(40.dp)
            )
            Text(text = data.name, fontSize = 30.sp, fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold, color = Color.White)
            Text(text = ", ${data.sys.country}", fontSize = 18.sp, fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold, color = Color.White)
        }
        Text(text = data.main.temp + "°F", fontSize = 40.sp, fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(top = 20.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = data.weather[0].description, fontSize = 20.sp, fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold, color = Color.White)
        Row (
            modifier = Modifier.padding(top = 8.dp)
        ){
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            val instant = Instant.ofEpochMilli(data.dt.toLong())
            val date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
            Text(text = formatter.format(date), fontSize = 20.sp, fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold, color = Color.White)
        }
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data("https://openweathermap.org/img/wn/${data.weather[0].icon}@2x.png")
                .crossfade(true)
                .build(),
            error = painterResource(R.drawable.ic_launcher_foreground),
            placeholder = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(220.dp)
                .width(240.dp)
        )
        Card (
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ) {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column {
                        Text(text = "Feels like")
                        Text(text = "${data.main.feels_like}°F")
                    }
                    Column {
                        Text(text = "Pressure")
                        Text(text = data.main.pressure)
                    }
                    Column {
                        Text(text = "Humidity")
                        Text(text = data.main.humidity)
                    }
                }
                HorizontalDivider(thickness = 2.dp, modifier = Modifier.padding(start = 5.dp, end = 5.dp))
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ){
                    Column {
                        Text(text = "Max Temp")
                        Text(text = "${data.main.temp_max}°F")
                    }
                    Column {
                        Text(text = "Min Temp")
                        Text(text = "${data.main.temp_min}°F")
                    }
                    Column {
                        Text(text = "Wind Speed")
                        Text(text = data.wind.speed)
                    }
                }
            }
        }
    }
}