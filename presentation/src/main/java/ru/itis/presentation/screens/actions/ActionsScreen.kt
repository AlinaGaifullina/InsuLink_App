package ru.itis.presentation.screens.actions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import ru.itis.domain.model.Action
import ru.itis.domain.model.ActionType
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
fun ActionsScreen(
    navController: NavController,
    viewModel: ActionsViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val eventHandler = viewModel::event
    val action by viewModel.action.collectAsStateWithLifecycle(null)

    val scrollState = rememberScrollState()

    // Группируем действия по дням
    val actionsByDay = remember(state.listOfActions) {
        state.listOfActions.groupBy { action ->
            action.date.toLocalDate() // Группируем по дате (без времени)
        }
    }

    // В ViewModel или в Composable
    val currentDate = LocalDate.now()
    val selectedPeriod by remember { mutableIntStateOf(1) } // 1 - день, 2 - неделя, 3 - месяц


// Функция для фильтрации действий
    val filteredActions = remember(state.listOfActions, state.historySizeType) {
        when (state.historySizeType) {
            HistorySizeType.DAY -> {
                // Только сегодняшние действия
                state.listOfActions
                    .filter { it.date.toLocalDate() == currentDate }
                    .groupBy { it.date.toLocalDate() }
            }

            HistorySizeType.WEEK -> {
                // Текущая неделя (пн-вс)
                val weekStart = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                val weekEnd = currentDate.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
                state.listOfActions
                    .filter { it.date.toLocalDate() in weekStart..weekEnd }
                    .groupBy { it.date.toLocalDate() }
            }

            HistorySizeType.MONTH1 -> {
                // Текущий месяц (1-число - последний день)
                val monthStart = currentDate.withDayOfMonth(1)
                val monthEnd = currentDate.with(TemporalAdjusters.lastDayOfMonth())
                state.listOfActions
                    .filter { it.date.toLocalDate() in monthStart..monthEnd }
                    .groupBy { it.date.toLocalDate() }
            }

            else -> state.listOfActions.groupBy { it.date.toLocalDate() }
        }
    }

    // Для недели - от текущего дня к понедельнику
    val daysInWeek by remember(currentDate) {
        derivedStateOf {
            val start = currentDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            (0..6).map { start.plusDays(it.toLong()) }
                .filter { it <= currentDate }
                .sortedDescending() // Сортируем в обратном порядке
        }
    }

// Для месяца - от текущего дня к 1 числу
    val daysInMonth by remember(currentDate) {
        derivedStateOf {
            val start = currentDate.withDayOfMonth(1)
            (0 until currentDate.dayOfMonth).map { start.plusDays(it.toLong()) }
                .sortedDescending() // Сортируем в обратном порядке
        }
    }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp),
            //.verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.action_log),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(top = 40.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        ThreeButtonsRow(
            text1 = "День",
            text2 = "Неделя",
            text3 = "Месяц",
            onButtonClick = {
                eventHandler.invoke(ActionsEvent.OnHistorySizeChange(it))
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Сортируем дни по убыванию (новые сверху)
        val sortedDays = actionsByDay.keys.sortedDescending()

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            when (state.historySizeType) {
                HistorySizeType.DAY -> {
                    item {
                        filteredActions[currentDate]?.let { actions ->
                            DayActionsCard(
                                dateText = "Сегодня, 15 мая",
                                listOfActions = actions
                            )
                        } ?: Text("Нет действий за сегодня")
                        Spacer(Modifier.height(20.dp))
                    }
                }

                HistorySizeType.WEEK -> {
                    items(daysInWeek.size) { index ->
                        val day = daysInWeek[index]
                        filteredActions[day]?.let { actions ->
                            DayActionsCard(
                                dateText = if (day == currentDate) "Четверг, 15 мая"
                                else "${day.dayOfWeek.displayText()}, ${day.format("d MMMM")}",
                                listOfActions = actions
                            )
                            Spacer(Modifier.height(20.dp))
                        }
                    }
                }

                HistorySizeType.MONTH1 -> {
                    items(daysInMonth.size) { index ->
                        val day = daysInMonth[index]
                        filteredActions[day]?.let { actions ->
                            DayActionsCard(
                                dateText = when {
                                    day == currentDate -> "15 мая"
                                    day.dayOfMonth == 1 -> "1 ${day.month.displayText()} ${day.year}"
                                    else -> day.format("d MMMM")
                                },
                                listOfActions = actions
                            )
                            Spacer(Modifier.height(20.dp))
                        }
                    }
                }

                HistorySizeType.MONTH3 -> TODO()
            }
        }
        Spacer(Modifier.height(60.dp))
    }
}

@Composable
fun DayActionsCard(
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
                    if(action.type == ActionType.BOLUS){
                        BolusCard(action, index)
                    } else {
                        TemporaryBasalCard(action, index)
                    }
                }
            }
        }
    }
}

@Composable
fun BolusCard(
    action: Action,
    index: Int
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.secondaryContainer)
    ) {
        if(index != 0){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.onPrimary)
            )
        }
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.tertiary)
            ){
                Text(
                    text = action.date.format(DateTimeFormatter.ofPattern("HH:mm", Locale("ru"))),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onTertiary,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    textAlign = TextAlign.Start
                )
            }
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = "Введен болюс - ${action.bolus} E",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Text(
                    text = "Сахар: ${action.sugar} мм/л",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
                Text(
                    text = "Питание: ${action.food} ХЕ",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
fun TemporaryBasalCard(
    action: Action,
    index: Int
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        if(index != 0){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.onPrimary)
            )
        }
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.tertiary)
            ){
                Text(
                    text = action.date.format(DateTimeFormatter.ofPattern("HH:mm", Locale("ru"))),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onTertiary,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    textAlign = TextAlign.Start
                )
            }
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = "Установлен временный базальный",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Text(
                    text = "Доля: ${action.basalPercent}%",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
                Text(
                    text = "На сколько: ${action.basalHours}:${action.basalMinutes}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
        }
        Spacer(Modifier.height(8.dp))
    }
}


private fun DayOfWeek.displayText(): String {
    return when (this) {
        DayOfWeek.MONDAY -> "Понедельник"
        DayOfWeek.TUESDAY -> "Вторник"
        DayOfWeek.WEDNESDAY -> "Среда"
        DayOfWeek.THURSDAY -> "Четверг"
        DayOfWeek.FRIDAY -> "Пятница"
        DayOfWeek.SATURDAY -> "Суббота"
        DayOfWeek.SUNDAY -> "Воскресенье"
    }
}

private fun LocalDate.format(pattern: String): String {
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale("ru"))
    return this.format(formatter)
}

private fun Month.displayText(): String {
    return getDisplayName(TextStyle.FULL, Locale("ru"))
}