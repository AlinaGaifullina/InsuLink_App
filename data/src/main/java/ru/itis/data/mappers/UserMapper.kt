package ru.itis.data.mappers

import ru.itis.data.SecureCryptoManager
import ru.itis.data.entity.UserEntity
import ru.itis.domain.model.User


fun User.toEntity(): UserEntity = UserEntity(
    id = id,
    firstName = SecureCryptoManager.encrypt(firstName),
    lastName = SecureCryptoManager.encrypt(lastName),
    patronymic = SecureCryptoManager.encrypt(patronymic),
    gender = SecureCryptoManager.encrypt(gender),
    birthDate = SecureCryptoManager.encrypt(birthDate),
    height = SecureCryptoManager.encrypt(height.toString()),
    weight = SecureCryptoManager.encrypt(weight.toString()),
    insulin = SecureCryptoManager.encrypt(insulin)
)

fun UserEntity.toDomain(): User = User(
    id = id,
    firstName = SecureCryptoManager.decrypt(firstName),
    lastName = SecureCryptoManager.decrypt(lastName),
    patronymic = SecureCryptoManager.decrypt(patronymic),
    gender = SecureCryptoManager.decrypt(gender),
    birthDate = SecureCryptoManager.decrypt(birthDate),
    height = SecureCryptoManager.decrypt(height).toFloat(),
    weight = SecureCryptoManager.decrypt(weight).toFloat(),
    insulin = SecureCryptoManager.decrypt(insulin)
)