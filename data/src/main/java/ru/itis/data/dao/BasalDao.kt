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

    @Query("SELECT * FROM basal WHERE id = :id")
    suspend fun getById(id: String): BasalEntity?

    @Query("SELECT * FROM basal")
    suspend fun getAll(): List<BasalEntity>

    @Query("SELECT * FROM basal WHERE userId = :userId")
    suspend fun getByUserId(userId: String): List<BasalEntity>

    @Query("DELETE FROM basal WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM basal WHERE userId = :userId")
    suspend fun deleteAllByUserId(userId: String)
}