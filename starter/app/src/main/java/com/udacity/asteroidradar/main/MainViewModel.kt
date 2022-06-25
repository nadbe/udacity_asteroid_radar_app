package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.NearEarthObject
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.network.AsteroidApi
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val asteroidsRepository = AsteroidsRepository()

    init {
        viewModelScope.launch {
            try {
                asteroidsRepository.refreshAsteroids("2022-06-25","2022-07-01")
            } catch (e:Exception){

            }
        }
    }

    val asteroidsList = asteroidsRepository.asteroidsList

}