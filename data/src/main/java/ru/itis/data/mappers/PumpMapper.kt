package ru.itis.data.mappers

import ru.itis.data.SecureCryptoManager
import ru.itis.data.entity.PumpEntity
import ru.itis.domain.model.Pump

fun Pump.toEntity(): PumpEntity = PumpEntity(
    id = id,
    deviceId = SecureCryptoManager.encrypt(deviceId),
    carbMeasurementUnit = SecureCryptoManager.encrypt(carbMeasurementUnit),
    glucoseMeasurementUnit = SecureCryptoManager.encrypt(glucoseMeasurementUnit),
    insulinActiveTime = SecureCryptoManager.encrypt(insulinActiveTime.toString()),
    userId = SecureCryptoManager.encrypt(userId)
)

fun PumpEntity.toDomain(): Pump = Pump(
    id = id,
    deviceId = SecureCryptoManager.decrypt(deviceId),
    carbMeasurementUnit = SecureCryptoManager.decrypt(carbMeasurementUnit),
    glucoseMeasurementUnit = SecureCryptoManager.decrypt(glucoseMeasurementUnit),
    insulinActiveTime = SecureCryptoManager.decrypt(insulinActiveTime).toFloat(),
    userId = SecureCryptoManager.decrypt(userId)
)