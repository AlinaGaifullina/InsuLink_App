package ru.itis.data.repository_impl

import ru.itis.data.dao.UserDao
import ru.itis.data.mappers.toDomain
import ru.itis.data.mappers.toEntity
import ru.itis.domain.model.User
import ru.itis.domain.repository.UserRepository
import javax.inject.Inject


class UserRepositoryImpl @Inject constructor(
    private val dao: UserDao
) : UserRepository {

    override suspend fun saveUser(user: User) {
        dao.saveUser(user.toEntity())
    }

    override suspend fun updateUser(user: User) {
        if (dao.getUser(user.id) != null) {
            dao.updateUser(user.toEntity())
        } else {
            throw UserNotFoundException("User not found")
        }
    }

    override suspend fun getUser(userId: String): User? {
        return dao.getUser(userId)?.toDomain()
    }

    override suspend fun deleteUser(userId: String) {
        dao.deleteUser(userId)
    }
}

class UserNotFoundException(message: String) : Exception(message)

