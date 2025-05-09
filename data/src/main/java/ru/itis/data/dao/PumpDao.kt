package ru.itis.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.itis.data.entity.PumpEntity

@Dao
interface PumpDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pump: PumpEntity)

    @Update
    suspend fun update(pump: PumpEntity)

    @Query("SELECT * FROM pump WHERE id = :pumpId")
    suspend fun getById(pumpId: String): PumpEntity?

    @Query("SELECT * FROM pump WHERE userId = :userId")
    suspend fun getByUserId(userId: String): List<PumpEntity>

    @Query("DELETE FROM pump WHERE id = :pumpId")
    suspend fun delete(pumpId: String)
}