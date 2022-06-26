package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.network.NasaImageApi
import com.udacity.asteroidradar.network.getFormattedDate
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val asteroidsRepository = AsteroidsRepository(database)

    val asteroidsList = asteroidsRepository.asteroidsList

    private val _pictureOfDayUrl = MutableLiveData<String>()
    val pictureOfDayUrl: LiveData<String>
    get() = _pictureOfDayUrl

    init {
        _pictureOfDayUrl.value = ""
    }

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

    fun loadImageOfTheDayUrl(){
        viewModelScope.launch {
            try {
                var pictureOfDay: PictureOfDay
                withContext(Dispatchers.IO) {
                    pictureOfDay = NasaImageApi.retrofitService.getNasaImageOfTheDay(BuildConfig.API_KEY)
                }
                _pictureOfDayUrl.value = pictureOfDay.url
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