package ru.itis.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.itis.data.entity.CarbCoefEntity

@Dao
interface CarbCoefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(coef: CarbCoefEntity)

    @Update
    suspend fun update(coef: CarbCoefEntity)

    @Query("SELECT * FROM carb_coef WHERE id = :id")
    suspend fun getById(id: String): CarbCoefEntity?

    @Query("SELECT * FROM carb_coef WHERE userId = :userId")
    suspend fun getByUserId(userId: String): List<CarbCoefEntity>

    @Query("SELECT * FROM carb_coef")
    suspend fun getAll(): List<CarbCoefEntity>

    @Query("DELETE FROM carb_coef WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM carb_coef WHERE userId = :userId")
    suspend fun deleteAllByUserId(userId: String)
}