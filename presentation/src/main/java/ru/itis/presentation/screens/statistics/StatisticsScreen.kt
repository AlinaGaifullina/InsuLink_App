package ru.itis.presentation.screens.statistics

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import ru.itis.domain.model.Action
import ru.itis.domain.model.HistorySizeType
import ru.itis.presentation.R
import ru.itis.presentation.components.ThreeButtonsRow
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.time.temporal.TemporalAdjusters
import java.time.format.TextStyle


@Composable
fun StatisticsScreen(
    navController: NavController,
    viewModel: StatisticsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val eventHandler = viewModel::event
    val currentDate = LocalDate.now()

    // Функция для фильтрации действий по периодам
    val filteredActions = remember(state.listOfActions, state.statisticsType) {
        when (state.statisticsType) {
            HistorySizeType.DAY -> {
                state.listOfActions.filter { it.date.toLocalDate() == currentDate }
                    .groupBy { it.date.toLocalDate() }
            }
            HistorySizeType.WEEK -> {
                val weekStart = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                val weekEnd = currentDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
                state.listOfActions.filter { it.date.toLocalDate() in weekStart..weekEnd }
                    .groupBy { it.date.toLocalDate() }
            }
            HistorySizeType.MONTH1 -> {
                val monthStart = currentDate.withDayOfMonth(1)
                val monthEnd = currentDate.with(TemporalAdjusters.lastDayOfMonth())
                state.listOfActions.filter { it.date.toLocalDate() in monthStart..monthEnd }
                    .groupBy { it.date.toLocalDate() }
            }
            else -> state.listOfActions.groupBy { it.date.toLocalDate() }
        }
    }

    // Получаем дни для отображения в обратном порядке
    val daysToShow = remember(filteredActions, state.statisticsType) {
        when (state.statisticsType) {
            HistorySizeType.DAY -> listOf(currentDate)
            HistorySizeType.WEEK -> {
                val start = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                (0..6).map { start.plusDays(it.toLong()) }
                    .filter { it <= currentDate }
                    .sortedDescending()
            }
            HistorySizeType.MONTH1 -> {
                val start = currentDate.withDayOfMonth(1)
                (0 until currentDate.dayOfMonth).map { start.plusDays(it.toLong()) }
                    .sortedDescending()
            }
            else -> filteredActions.keys.sortedDescending()
        }
    }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp, top = 40.dp, bottom = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.glucose_statistics),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Spacer(modifier = Modifier.height(16.dp))

        ThreeButtonsRow(
            text1 = "День",
            text2 = "Неделя",
            text3 = "Месяц",
            onButtonClick = {
                eventHandler.invoke(StatisticsEvent.OnStatisticsTypeChange(it))
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        if(state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(56.dp),
                strokeWidth = 4.dp,
                color = MaterialTheme.colorScheme.secondary
            )
        } else {
            // График и статистика
            Image(
                painter = if(state.statisticsType == HistorySizeType.MONTH1)
                    painterResource(R.drawable.chart_month)
                else
                    painterResource(R.drawable.chart_week),
                contentDescription = "",
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Текстовые данные
            val (averageValue, daysInfo, dateRange) = when (state.statisticsType) {
                HistorySizeType.MONTH1 -> Triple(
                    "7.0 мм/л",
                    "${currentDate.dayOfMonth}/31 ${currentDate.month.displayText()}",
                    "1-${currentDate.with(TemporalAdjusters.lastDayOfMonth()).dayOfMonth} ${currentDate.month.displayText()}"
                )
                HistorySizeType.WEEK -> {
                    val daysPassed = currentDate.dayOfWeek.value // дней прошло с понедельника
                    Triple(
                        "7.3 мм/л",
                        "$daysPassed/7 дней",
                        "${currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).format("d")}-${currentDate.format("d MMMM")}"
                    )
                }
                else -> Triple(
                    "7.3 мм/л",
                    "1 день",
                    currentDate.format("d MMMM yyyy")
                )
            }

            Text(
                text = "Среднее значение: $averageValue",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = daysInfo,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(MaterialTheme.colorScheme.surfaceTint))

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Записи",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = dateRange,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(Modifier.height(20.dp))

            // Список записей
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(daysToShow) { day ->
                    filteredActions[day]?.let { actions ->
                        DayGlucoseCard(
                            dateText = when (state.statisticsType) {
                                HistorySizeType.DAY -> "Сегодня, ${day.format("d MMMM")}"
                                HistorySizeType.WEEK -> "${day.dayOfWeek.displayText()}, ${day.format("d MMMM")}"
                                else -> day.format("d MMMM yyyy")
                            },
                            listOfActions = actions
                        )
                        Spacer(Modifier.height(20.dp))
                    }
                }
            }
        }
    }
}

// Extension-функции для форматирования
private fun DayOfWeek.displayText(): String {
    return getDisplayName(TextStyle.FULL, Locale("ru"))
}

private fun LocalDate.format(pattern: String): String {
    return format(DateTimeFormatter.ofPattern(pattern, Locale("ru")))
}

private fun Month.displayText(): String {
    return getDisplayName(TextStyle.FULL, Locale("ru"))
}


@Composable
fun DayGlucoseCard(
    dateText: String,
    listOfActions: List<Action>
){
    Card(
        modifier = Modifier,
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text(
                    text = dateText,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                    ,
                    textAlign = TextAlign.Start
                )
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                listOfActions.forEachIndexed { index, action ->
                    GlucoseRow(action, index)
                }
            }
        }
    }
}

@Composable
fun GlucoseRow(
    action: Action,
    index: Int
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        if(index != 0){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f))
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = action.date.format(DateTimeFormatter.ofPattern("HH:mm", Locale("ru"))),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .padding(vertical = 10.dp, horizontal = 16.dp),
            )
            Text(
                text = "${action.sugar} мм/л",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 16.dp),
            )
        }
    }
}