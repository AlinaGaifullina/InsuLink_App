package ru.itis.data.mappers

import ru.itis.data.SecureCryptoManager
import ru.itis.data.entity.TargetGlucoseEntity
import ru.itis.domain.model.TargetGlucose

fun TargetGlucose.toEntity(): TargetGlucoseEntity = TargetGlucoseEntity(
    id = id,
    userId = SecureCryptoManager.encrypt(userId),
    startTime = SecureCryptoManager.encrypt(startTime),
    endTime = SecureCryptoManager.encrypt(endTime),
    startValue = SecureCryptoManager.encrypt(startValue.toString()),
    endValue = SecureCryptoManager.encrypt(endValue.toString())
)

fun TargetGlucoseEntity.toDomain(): TargetGlucose = TargetGlucose(
    id = id,
    userId = SecureCryptoManager.decrypt(userId),
    startTime = SecureCryptoManager.decrypt(startTime),
    endTime = SecureCryptoManager.decrypt(endTime),
    startValue = SecureCryptoManager.decrypt(startValue).toFloat(),
    endValue = SecureCryptoManager.decrypt(endValue).toFloat()
)