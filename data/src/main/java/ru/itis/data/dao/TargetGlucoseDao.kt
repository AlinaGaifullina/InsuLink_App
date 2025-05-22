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

    @Query("SELECT * FROM target_glucose WHERE id = :id")
    suspend fun getById(id: String): TargetGlucoseEntity?

    @Query("SELECT * FROM target_glucose WHERE userId = :userId")
    suspend fun getByUserId(userId: String): List<TargetGlucoseEntity>

    @Query("SELECT * FROM target_glucose")
    suspend fun getAll(): List<TargetGlucoseEntity>

    @Query("DELETE FROM target_glucose WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM target_glucose WHERE userId = :userId")
    suspend fun deleteAllByUserId(userId: String)
}