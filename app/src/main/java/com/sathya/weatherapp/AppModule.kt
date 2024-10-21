package com.sathya.weatherapp

import android.content.Context
import com.sathya.weatherapp.repository.storage.CityPreferences
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    fun provideCityPreferences(@ApplicationContext context: Context): CityPreferences {
        return CityPreferences(context)
    }
}