package ru.itis.presentation.utils

data class Insulin(
    val id: String,          // Уникальный идентификатор
    val name: String,        // Название инсулина (например, "НовоРапид")
    val activeTime: Int,     // Время активности в минутах
    val peakTime: Int,       // Время пика активности
    val type: String         // Тип (быстрый/длинный)
) {
    // Форматированное время активности для отображения
    fun getActiveTimeFormatted(): String {
        val hours = activeTime / 60
        val minutes = activeTime % 60
        return "$hours ч ${minutes} мин"
    }
}

// Репозиторий с данными
object InsulinRepository {
    val insulinList = listOf(
        Insulin("1", "НовоРапид", 300, 90, "Быстрый"),
        Insulin("2", "Хумалог", 240, 60, "Быстрый"),
        Insulin("3", "Лантус", 1440, -1, "Длинный"),
        Insulin("4", "Тресиба", 1800, -1, "Длинный")
    )
}