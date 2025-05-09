package ru.itis.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.itis.data.entity.ActionEntity

@Dao
interface ActionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(action: ActionEntity)

    @Query("SELECT * FROM user_action")
    suspend fun getAll(): List<ActionEntity>

    @Query("SELECT * FROM user_action WHERE date BETWEEN :start AND :end")
    suspend fun getByDateRange(start: String, end: String): List<ActionEntity>
}