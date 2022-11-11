package com.udacity.asteroidradar.main

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDao
import com.udacity.asteroidradar.database.AsteroidEntities
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

enum class AsteroidApiStatus {LOADING, ERROR, DONE}

class MainViewModel(
    private val dao: AsteroidDao
) : ViewModel() {

    private val _status = MutableLiveData<AsteroidApiStatus>()
    val status: LiveData<AsteroidApiStatus> = _status

    val allAsteroids: LiveData<List<AsteroidEntities>> = dao.getAllAsteroids().asLiveData()

//    init {
//        getAsteroidData()
//        getPictureOfDay()
//    }
    fun asteroidDetails(id:Long): LiveData<AsteroidEntities> {
        return dao.getAsteroidDetails(id).asLiveData()
    }

    private fun addAsteroid(asteroid: Asteroid) {
        val newAsteroid = AsteroidEntities(
            id = asteroid.id,
            codename = asteroid.codename,
            closeApproachDate = asteroid.closeApproachDate,
            absoluteMagnitude = asteroid.absoluteMagnitude,
            estimatedDiameter = asteroid.estimatedDiameter,
            relativeVelocity = asteroid.relativeVelocity,
            distanceFromEarth = asteroid.distanceFromEarth,
            isPotentiallyHazardous = asteroid.isPotentiallyHazardous
        )
        dao.insert(newAsteroid)
    }

    private fun deleteOldAsteroids() {
        dao.deleteOld()
    }

    private fun getAsteroidData() {
        viewModelScope.launch {
            _status.value = AsteroidApiStatus.LOADING
            try {
                val asteroidList = parseAsteroidsJsonResult(JSONObject(AsteroidApi.retrofitService.getAsteroids("" ,"", "DEMO_KEY")))
                _status.value = AsteroidApiStatus.DONE
                for (asteroid in asteroidList) {
                    addAsteroid(asteroid)
                }
            } catch (e:Exception) {
                _status.value = AsteroidApiStatus.ERROR
            }
        }

    }

    private fun getPictureOfDay() {



        viewModelScope.launch {

            val newPicture = AsteroidApi.retrofitService.getPictureOfDay("DEMO_KEY")


        }

    }

    //api key: "SNrG4C86m2Zxhx9b7HAAnOGdJQqB6BzYRLlTi0fp"



//    private val _listAsteroids = MutableLiveData<List<Asteroid>>()
//    val listAsteroid: LiveData<List<Asteroid>>
//        get() = _listAsteroids
//
//    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
//    val pictureOfDay: LiveData<PictureOfDay>
//        get() = _pictureOfDay

}


class AsteroidViewModelFactory(
    private val dao: AsteroidDao
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}