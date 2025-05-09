package ru.itis.data.mappers

import ru.itis.data.SecureCryptoManager
import ru.itis.data.entity.CarbCoefEntity
import ru.itis.domain.model.CarbCoef

fun CarbCoef.toEntity(): CarbCoefEntity = CarbCoefEntity(
    id = id,
    userId = SecureCryptoManager.encrypt(userId),
    startTime = SecureCryptoManager.encrypt(startTime),
    endTime = SecureCryptoManager.encrypt(endTime),
    coef = SecureCryptoManager.encrypt(coef.toString())
)

fun CarbCoefEntity.toDomain(): CarbCoef = CarbCoef(
    id = id,
    userId = SecureCryptoManager.decrypt(userId),
    startTime = SecureCryptoManager.decrypt(startTime),
    endTime = SecureCryptoManager.decrypt(endTime),
    coef = SecureCryptoManager.decrypt(coef).toFloat(),
)