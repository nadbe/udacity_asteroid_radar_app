package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.asDatabaseModel
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.network.AsteroidApi
import com.udacity.asteroidradar.network.NasaImageApi
import com.udacity.asteroidradar.network.getNextSevenDaysFormattedDates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AsteroidsRepository (private val database: AsteroidsDatabase) {

    val asteroidsList: LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getAllAsteroids()) {
        it.asDomainModel()
    }

    suspend fun refreshAsteroids(startDate: String, endDate: String) {
        withContext(Dispatchers.IO) {

            var nearEarthObject = AsteroidApi.retrofitService.getNearEarthObject(
                startDate,
                endDate,
                BuildConfig.API_KEY
            )
            database.asteroidDao.insertAllAsteroids(*nearEarthObject.asDatabaseModel())
            database.asteroidDao.deleteOutdatedAsteroids(getNextSevenDaysFormattedDates())
        }
    }

}