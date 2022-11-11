package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.Asteroid
import kotlinx.coroutines.flow.Flow

@Dao
interface AsteroidDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(asteroid: AsteroidEntities)

    @Query("SELECT * FROM asteroids ORDER BY closeApproachDate")
    fun getAllAsteroids(): Flow<List<AsteroidEntities>>

    @Query("SELECT * FROM asteroids WHERE id = :id")
    fun getAsteroidDetails(id: Long): Flow<AsteroidEntities>

    @Query("""
        DELETE 
        FROM asteroids
        WHERE DATE(closeApproachDate) < DATE('now','-7 day') 
    """)
    fun deleteOld()

}