package ru.itis.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.itis.data.entity.TargetGlucoseEntity

@Dao
interface TargetGlucoseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(target: TargetGlucoseEntity)

    @Update
    suspend fun update(target: TargetGlucoseEntity)

    @Query("SELECT * FROM target_glucose")
    suspend fun getAll(): List<TargetGlucoseEntity>
}