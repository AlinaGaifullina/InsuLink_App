package ru.itis.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TimerState(
    val totalMillis: Long,
    val startTime: Long? = null,
    val isRunning: Boolean = false
) : Parcelable