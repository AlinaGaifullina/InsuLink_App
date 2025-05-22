package ru.itis.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TimerRangeDisplay(
    timerStartTime: Long?,
    timerRemainingMillis: Long,
    textColor: Color,
    modifier: Modifier
) {
    val timeRange = remember(timerStartTime, timerRemainingMillis) {
        if (timerStartTime != null) {
            val startTimeFormatted = SimpleDateFormat("HH:mm", Locale.getDefault())
                .format(
                    Date(timerStartTime))

            val endTimeMillis = timerStartTime + timerRemainingMillis
            val endTimeFormatted = SimpleDateFormat("HH:mm", Locale.getDefault())
                .format(Date(endTimeMillis))

            "$startTimeFormatted - $endTimeFormatted"
        } else {
            "--:-- - --:--"
        }
    }

    Text(
        text = timeRange,
        style = MaterialTheme.typography.labelSmall,
        color = textColor,
        modifier = modifier
    )
}