package com.udacity.asteroidradar

import android.app.Application
import com.udacity.asteroidradar.database.AsteroidDatabase

class BaseApplication: Application() {

    val database: AsteroidDatabase by lazy { AsteroidDatabase.getDatabaseInstance(this) }
}