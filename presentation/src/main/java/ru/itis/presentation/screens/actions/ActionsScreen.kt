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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import ru.itis.presentation.R
import java.time.format.DateTimeFormatter
import java.util.Locale

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

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.action_log),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(top = 40.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Сортируем дни по убыванию (новые сверху)
        val sortedDays = actionsByDay.keys.sortedDescending()

        sortedDays.forEach { day ->
            val actionsForDay = actionsByDay[day] ?: emptyList()
            DayActionsCard(
                dateText = day.format(DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("ru"))),
                listOfActions = actionsForDay
            )
            Spacer(Modifier.height(20.dp))
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