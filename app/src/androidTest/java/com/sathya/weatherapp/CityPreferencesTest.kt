package com.sathya.weatherapp

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import org.junit.runner.RunWith
import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth.assertThat
import com.sathya.weatherapp.repository.storage.CityPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class CityPreferencesTest {
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var context: Context
    private lateinit var cityPreferences: CityPreferences


    @Before
    fun setUp(){
        // Get the context from the InstrumentationRegistry (works in Android instrumented tests)
        context = InstrumentationRegistry.getInstrumentation().targetContext

        // Initialize CityPreferences with the instrumented context
        cityPreferences = CityPreferences(context)
    }

    @Test
    fun testSaveAndRetrieveCity() = runTest(testDispatcher) {
        // Arrange
        val cityName = "New York"
        val cityKey = stringPreferencesKey("last_city")

        // Act: Save the city and retrieve it
        runBlocking {
            cityPreferences.saveCity(cityName)
        }

        // Retrieve the saved city
        val savedCity = cityPreferences.lastCityFlow.first()

        // Assert that the retrieved city matches the saved one
        assertThat(savedCity).isEqualTo(cityName)
    }
}