package ru.itis.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import ru.itis.data.entity.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("SELECT * FROM user WHERE id = :userId")
    suspend fun getUser(userId: String): UserEntity?

    @Query("DELETE FROM user WHERE id = :userId")
    suspend fun deleteUser(userId: String)
}