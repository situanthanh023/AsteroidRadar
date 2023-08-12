package com.udacity.asteroidradar.repository

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.AsteroidApiFilter
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asAsteroidEntities
import com.udacity.asteroidradar.database.asAsteroids
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidsRepository(private val database: AsteroidsDatabase) {

    val asteroids: LiveData<List<Asteroid>> = database.asteroidDao.getAsteroids().map {
        it.asAsteroids()
    }

    @SuppressLint("WeekBasedYear")
    suspend fun refreshAsteroids(asteroidType: AsteroidApiFilter) {
        withContext(Dispatchers.IO) {
            if (AsteroidApiFilter.SHOW_TODAY == asteroidType || AsteroidApiFilter.SHOW_ALL == asteroidType) {
                val asteroids = parseAsteroidsJsonResult(
                    JSONObject(
                        AsteroidApi.retrofitService.getProperties(
                            "", asteroidType.type, API_KEY
                        )
                    )
                )
                database.asteroidDao.clear()
                database.asteroidDao.insertAll(asteroids.asAsteroidEntities())
            } else {
                val asteroids = parseAsteroidsJsonResult(
                    JSONObject(
                        AsteroidApi.retrofitService.getProperties(
                            "", asteroidType.type, API_KEY
                        )
                    )
                )
                database.asteroidDao.insertAll(asteroids.asAsteroidEntities())
            }
        }
    }
}