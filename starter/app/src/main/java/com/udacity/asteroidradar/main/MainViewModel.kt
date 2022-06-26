package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.network.NasaImageApi
import com.udacity.asteroidradar.network.getFormattedDate
import com.udacity.asteroidradar.repository.AsteroidsRepository
import com.udacity.asteroidradar.repository.NasaImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val asteroidsRepository = AsteroidsRepository(database)
    private val imageRepository = NasaImageRepository()

    val asteroidsList = asteroidsRepository.asteroidsList
    val pictureOfDay = imageRepository.pictureOfDay

    fun performNetworkRequest() {
            viewModelScope.launch {
                try {
                    asteroidsRepository.refreshAsteroids(
                        getFormattedDate(0),
                        getFormattedDate(Constants.DEFAULT_END_DATE_DAYS)
                    )
                } catch (e: Exception) {

                }
            }
    }

    fun loadImageOfTheDay(){
        viewModelScope.launch {
            try {
                imageRepository.loadNasaPictureData()
            } catch (e: Exception) {

            }
        }
    }


    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct MainViewModel")
        }
    }
}