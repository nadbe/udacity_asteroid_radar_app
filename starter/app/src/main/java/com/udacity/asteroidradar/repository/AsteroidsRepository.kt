package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.NearEarthObject
import com.udacity.asteroidradar.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.network.AsteroidApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AsteroidsRepository {

    var asteroidsList = MutableLiveData<List<Asteroid>>()

    suspend fun refreshAsteroids(startDate: String, endDate: String) {
        withContext(Dispatchers.IO) {

            var nearEarthObject = AsteroidApi.retrofitService.getNearEarthObject(
                startDate,
                endDate,
                BuildConfig.API_KEY
            )
            asteroidsList.value = nearEarthObject.asDomainModel()
        }
    }
}