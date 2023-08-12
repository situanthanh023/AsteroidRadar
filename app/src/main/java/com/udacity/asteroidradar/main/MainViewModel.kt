package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.AsteroidApiFilter
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch

class MainViewModel(
    application: Application
) : ViewModel() {

    private val database = getDatabase(application)

    private val repository = AsteroidsRepository(database)

    private val _dataList = repository.asteroids

    val dataList: LiveData<List<Asteroid>>
        get() = _dataList

    private val _navigateToSelectedProperty = MutableLiveData<Asteroid?>()

    val navigateToSelectedProperty: MutableLiveData<Asteroid?>
        get() = _navigateToSelectedProperty

    private val _dataImage = MutableLiveData<PictureOfDay>()

    val dataImage: LiveData<PictureOfDay>
        get() = _dataImage

    init {
        getData()
        getImageOfDay()
    }


    private fun getData() {
        viewModelScope.launch {
            try {
                repository.refreshAsteroids(asteroidType = AsteroidApiFilter.SHOW_ALL)
            } catch (e: Exception) {
                // TODO
            }
        }
    }

    private fun getImageOfDay() {
        viewModelScope.launch {
            try {
                _dataImage.value = AsteroidApi.retrofitService.getImageOfDay(Constants.API_KEY)
            } catch (e: Exception) {
                // TODO
            }
        }
    }

    fun displayPropertyDetails(asteroid: Asteroid) {
        _navigateToSelectedProperty.value = asteroid
    }

    fun displayPropertyDetailsComplete() {
        _navigateToSelectedProperty.value = null
    }

    fun updateFilter(filter: AsteroidApiFilter) {
        viewModelScope.launch {
            try {
                repository.refreshAsteroids(asteroidType = filter)
            } catch (e: Exception) {
                // TODO
            }
        }
    }
}