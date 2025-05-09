package ru.itis.data.mappers

import ru.itis.data.SecureCryptoManager
import ru.itis.data.entity.ActionEntity
import ru.itis.domain.model.Action
import ru.itis.domain.model.ActionType
import java.time.LocalDateTime

// domain -> data
fun Action.toEntity(): ActionEntity = ActionEntity(
    id = id,
    date = SecureCryptoManager.encrypt(date.toString()),
    type = SecureCryptoManager.encrypt(type.name),
    sugar = SecureCryptoManager.encrypt(sugar.toString()),
    food = SecureCryptoManager.encrypt(food.toString()),
    bolus = SecureCryptoManager.encrypt(bolus.toString()),
    basalPercent = SecureCryptoManager.encrypt(basalPercent.toString()),
    basalHours = SecureCryptoManager.encrypt(basalHours.toString()),
    basalMinutes = SecureCryptoManager.encrypt(basalMinutes.toString()),
    basalSeconds = SecureCryptoManager.encrypt(basalSeconds.toString())
)

// data -> domain
fun ActionEntity.toDomain(): Action = Action(
    id = id,
    date = LocalDateTime.parse(SecureCryptoManager.decrypt(date)),
    type = ActionType.valueOf(SecureCryptoManager.decrypt(type)),
    sugar = SecureCryptoManager.decrypt(sugar).toFloat(),
    food = SecureCryptoManager.decrypt(food).toFloat(),
    bolus = SecureCryptoManager.decrypt(bolus).toFloat(),
    basalPercent = SecureCryptoManager.decrypt(basalPercent).toInt(),
    basalHours = SecureCryptoManager.decrypt(basalHours).toInt(),
    basalMinutes = SecureCryptoManager.decrypt(basalMinutes).toInt(),
    basalSeconds = SecureCryptoManager.decrypt(basalSeconds).toInt()
)