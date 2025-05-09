package ru.itis.presentation.utils

import java.util.UUID

object IdGenerator {
    fun newId(): String = UUID.randomUUID().toString()
}