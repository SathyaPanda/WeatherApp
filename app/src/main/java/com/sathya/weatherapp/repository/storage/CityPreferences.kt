package com.sathya.weatherapp.repository.storage

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject

// Create a singleton DataStore instance
val Context.dataStore by preferencesDataStore(name = "weather_prefs")

class CityPreferences @Inject constructor(
    @ApplicationContext private val context : Context
) {
    companion object {
        val LAST_CITY_KEY = stringPreferencesKey("last_city")
    }

    // Retrieve last city name
    val lastCityFlow = context.dataStore.data.map { preferences ->
        preferences[LAST_CITY_KEY] ?: ""
    }

    // Save city name
    suspend fun saveCity(city: String) {
        context.dataStore.edit { preferences ->
            preferences[LAST_CITY_KEY] = city
        }
    }
}