package ru.itis.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.itis.data.entity.BasalEntity


@Dao
interface BasalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: BasalEntity)

    @Update
    suspend fun update(value: BasalEntity)

    @Query("SELECT * FROM basal")
    suspend fun getAll(): List<BasalEntity>
}