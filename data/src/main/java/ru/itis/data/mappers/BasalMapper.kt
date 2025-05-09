package ru.itis.data.mappers

import ru.itis.data.SecureCryptoManager
import ru.itis.data.entity.BasalEntity
import ru.itis.domain.model.Basal

fun Basal.toEntity(): BasalEntity = BasalEntity(
    id = id,
    userId = SecureCryptoManager.encrypt(userId),
    startTime = SecureCryptoManager.encrypt(startTime),
    endTime = SecureCryptoManager.encrypt(endTime),
    value = SecureCryptoManager.encrypt(value.toString())
)

fun BasalEntity.toDomain(): Basal = Basal(
    id = id,
    userId = SecureCryptoManager.decrypt(userId),
    startTime = SecureCryptoManager.decrypt(startTime),
    endTime = SecureCryptoManager.decrypt(endTime),
    value = SecureCryptoManager.decrypt(value).toFloat(),
)