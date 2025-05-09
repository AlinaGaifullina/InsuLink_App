package ru.itis.data.mappers

import ru.itis.data.SecureCryptoManager
import ru.itis.data.entity.InsulinSensitivityEntity
import ru.itis.domain.model.InsulinSensitivity

fun InsulinSensitivity.toEntity(): InsulinSensitivityEntity = InsulinSensitivityEntity(
    id = id,
    userId = SecureCryptoManager.encrypt(userId),
    startTime = SecureCryptoManager.encrypt(startTime),
    endTime = SecureCryptoManager.encrypt(endTime),
    value = SecureCryptoManager.encrypt(value.toString())
)

fun InsulinSensitivityEntity.toDomain(): InsulinSensitivity = InsulinSensitivity(
    id = id,
    userId = SecureCryptoManager.decrypt(userId),
    startTime = SecureCryptoManager.decrypt(startTime),
    endTime = SecureCryptoManager.decrypt(endTime),
    value = SecureCryptoManager.decrypt(value).toFloat(),
)