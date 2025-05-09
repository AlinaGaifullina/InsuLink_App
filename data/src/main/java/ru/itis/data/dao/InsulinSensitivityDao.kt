package ru.itis.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.itis.data.entity.InsulinSensitivityEntity

@Dao
interface InsulinSensitivityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(value: InsulinSensitivityEntity)

    @Update
    suspend fun update(value: InsulinSensitivityEntity)

    @Query("SELECT * FROM insulin_sensitivity")
    suspend fun getAll(): List<InsulinSensitivityEntity>
}