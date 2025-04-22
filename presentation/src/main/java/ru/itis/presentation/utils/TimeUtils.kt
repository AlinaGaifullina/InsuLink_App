package ru.itis.presentation.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TimeUtils {
    companion object {
        // Функция для добавления минут с учетом перехода через сутки
        fun addMinutes(time: Calendar, minutes: Int): Calendar {
            val newTime = time.clone() as Calendar
            newTime.add(Calendar.MINUTE, minutes)
            return newTime
        }

        // Преобразование строки в Calendar
        fun parseTime(timeString: String): Calendar {
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            val date = sdf.parse(timeString) ?: Date()
            val calendar = Calendar.getInstance()
            calendar.time = date
            return calendar
        }

        // Форматирование Calendar в строку
        fun formatTime(time: Calendar): String {
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            return sdf.format(time.time)
        }
    }
}