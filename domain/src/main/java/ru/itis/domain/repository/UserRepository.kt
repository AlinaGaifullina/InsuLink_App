package ru.itis.domain.repository

import ru.itis.domain.model.User

interface UserRepository {
    suspend fun saveUser(user: User)
    suspend fun getUser(userId: String): User?
    suspend fun deleteUser(userId: String)
    suspend fun updateUser(user: User)
}