package ru.itis.presentation.screens.basal

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import ru.itis.presentation.R
import ru.itis.presentation.components.BaseButton
import ru.itis.presentation.components.TimerRangeDisplay
import ru.itis.presentation.components.pump_settings.AddIntervalCard
import ru.itis.presentation.components.pump_settings.carb_coef.ExpandableCarbCoefItem
import ru.itis.presentation.utils.TimeUtils
import ru.itis.presentation.utils.roundToOneDecimal

@Composable
fun BasalScreen(
    navController: NavController,
    viewModel: BasalViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val eventHandler = viewModel::event
    val action by viewModel.action.collectAsStateWithLifecycle(null)

    var isAddCardVisible by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)
                .fillMaxSize()
                .padding(start = 20.dp, end = 20.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = stringResource(R.string.basal),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.tertiary)
                            .padding(12.dp),
                    ) {
                        Text(
                            text = stringResource(R.string.set_temporary_basal),
                            color = MaterialTheme.colorScheme.onTertiary,
                            style = MaterialTheme.typography.labelLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    val totalSeconds = state.timerRemainingMillis / 1000
                    val h = totalSeconds / 3600
                    val m = (totalSeconds % 3600) / 60
                    val s = totalSeconds % 60

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        if(state.isTemporaryBasalActive){
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = stringResource(R.string.current_temporary_basal),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onTertiary
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(MaterialTheme.colorScheme.surfaceTint)
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ){
                                Text(
                                    text = state.newTemporaryBasal.toString() + "%",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = MaterialTheme.colorScheme.onSecondary,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = stringResource(R.string.remaining),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onTertiary
                            )
                            Text(
                                text = "%02d : %02d : %02d".format(h, m, s),
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onTertiary,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                            TimerRangeDisplay(
                                timerStartTime = state.timerStartTime,
                                timerRemainingMillis = state.timerRemainingMillis,
                                textColor = MaterialTheme.colorScheme.onTertiary,
                                modifier = Modifier
                            )

                            Spacer(modifier = Modifier.height(16.dp))
                            BaseButton(
                                onClick = {

                                },
                                text = stringResource(R.string.cancel),
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                                backgroundColor = MaterialTheme.colorScheme.primary,
                                textColor = MaterialTheme.colorScheme.onTertiary
                            )
                        } else {
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                OutlinedTextField(
                                    value = if(state.newTemporaryBasal == 0) "" else state.newTemporaryBasal.toString(),
                                    onValueChange = { newValue ->
                                        if ((newValue.length <= 3) && (newValue.isEmpty() || newValue.toIntOrNull()?.let { it in 0..200 } == true)) {
                                            eventHandler.invoke(BasalEvent.OnNewBasalChange(
                                                if(newValue == "") 0 else newValue.toInt())
                                            )
                                        }
                                    },
                                    modifier = Modifier.width(80.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Done,
                                    ),
                                    singleLine = true,
                                    textStyle = MaterialTheme.typography.headlineSmall.copy(
                                        textAlign = TextAlign.Center
                                    ),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.5f),
                                        unfocusedBorderColor = MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.5f),
                                        focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                                        unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                                        focusedContainerColor = MaterialTheme.colorScheme.primary,
                                        unfocusedContainerColor = MaterialTheme.colorScheme.primary
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    modifier = Modifier,
                                    text = "%",
                                    color = MaterialTheme.colorScheme.onTertiary,
                                    style = MaterialTheme.typography.headlineSmall
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = stringResource(R.string.for_how_many),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onTertiary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalArrowTimeButton(
                                onLeftClick = {

                                },
                                onRightClick = {

                                },
                                onHourTextValueChange = {
                                    eventHandler.invoke(BasalEvent.OnHourValueChange(
                                        if(it == "") 0 else it.toInt())
                                    )
                                },
                                onMinuteTextValueChange = {
                                    eventHandler.invoke(BasalEvent.OnMinuteValueChange(
                                        if(it == "") 0 else it.toInt())
                                    )
                                },
                                hourTextValue = state.hourValue,
                                minuteTextValue = state.minuteValue,
                                modifier = Modifier
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            BaseButton(
                                onClick = {
                                    eventHandler.invoke(BasalEvent.SetTimerDuration(1,15))
                                    eventHandler.invoke(BasalEvent.StartTimer)
                                    eventHandler.invoke(BasalEvent.OnSetTemporaryBasalClick)
                                },
                                text = stringResource(R.string.set),
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                                backgroundColor = MaterialTheme.colorScheme.tertiary,
                                textColor = MaterialTheme.colorScheme.onTertiary
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(R.string.total_units) + " " + state.listOfBasal.sumOf { it.value.toDouble() }.toFloat().roundToOneDecimal().toString(),
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End
            )
            state.listOfBasal.forEachIndexed { index, basal ->
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(if(state.basalExpandedItemIndex == index) RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp) else RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .padding(end = 8.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .clip(if(state.basalExpandedItemIndex == index) RoundedCornerShape(topStart = 16.dp) else RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
                            .background(MaterialTheme.colorScheme.secondary)
                            .width(130.dp)
                            .padding(16.dp)
                            .align(Alignment.CenterStart),
                    ) {
                        Text(
                            text = "${basal.startTime}-${basal.endTime}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSecondary,
                            modifier = Modifier
                                .align(Alignment.Center)
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Text(
                            text = "${basal.value} " + stringResource(R.string.unit_hour),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                        IconButton(
                            modifier = Modifier,
                            onClick = {
                                eventHandler.invoke(BasalEvent.OnBasalExpandedItemChange(index))
                            }
                        ) {
                            Icon(
                                painterResource(R.drawable.ic_edit),
                                contentDescription = "icon_edit",
                                modifier = Modifier
                                    .size(18.dp),
                                MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
                ExpandableCarbCoefItem(
                    isExpanded = index == state.basalExpandedItemIndex,
                    onSaveClick = {
                        eventHandler.invoke(BasalEvent.OnSaveBasalItem(it))
                    },
                    onDeleteClick = {
                        eventHandler.invoke(BasalEvent.OnDeleteBasalItem(it))
                    },
                    expandedItem = state.basalExpandedItemIndex,
                    itemStartTime = state.basalItemStartTime,
                    itemEndTime = state.basalItemEndTime,
                    itemUnitPerHourValue = if (state.basalItemValue == 0.0f) "" else state.basalItemValue.toString(),
                    onUpItemClick = {
                        if (it == 0) {
                            var startTime = TimeUtils.parseTime(state.basalItemStartTime)
                            startTime = TimeUtils.addMinutes(startTime, 15)
                            eventHandler.invoke(
                                BasalEvent.OnBasalItemStartTimeChange(
                                    TimeUtils.formatTime(startTime)
                                )
                            )
                        } else if (it == 1) {
                            var endTime = TimeUtils.parseTime(state.basalItemEndTime)
                            endTime = TimeUtils.addMinutes(endTime, 15)
                            eventHandler.invoke(
                                BasalEvent.OnBasalItemEndTimeChange(
                                    TimeUtils.formatTime(endTime)
                                )
                            )
                        }
                    },
                    onDownItemClick = {
                        if (it == 0) {
                            var startTime = TimeUtils.parseTime(state.basalItemStartTime)
                            startTime = TimeUtils.addMinutes(startTime, -15)
                            eventHandler.invoke(
                                BasalEvent.OnBasalItemStartTimeChange(
                                    TimeUtils.formatTime(startTime)
                                )
                            )
                        } else if (it == 1) {
                            var endTime = TimeUtils.parseTime(state.basalItemEndTime)
                            endTime = TimeUtils.addMinutes(endTime, -15)
                            eventHandler.invoke(
                                BasalEvent.OnBasalItemEndTimeChange(
                                    TimeUtils.formatTime(endTime)
                                )
                            )
                        }
                    },
                    onCarbCoefValueChange = {
                        eventHandler.invoke(BasalEvent.OnBasalItemValueChange(
                            if (it.isEmpty()) 0.0f else it.toFloatOrNull() ?: 0.0f
                        ))
                    },
                    backgroundColor = MaterialTheme.colorScheme.secondaryContainer
                )
            }
            if(!isAddCardVisible){
                Spacer(modifier = Modifier.height(16.dp))
                BaseButton(
                    text = stringResource(R.string.add),
                    textColor = MaterialTheme.colorScheme.onSecondary,
                    backgroundColor = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {isAddCardVisible = true}
                )
            } else {
                AddIntervalCard(
                    onCloseClick = {isAddCardVisible = false},
                    onAddClick = {
                        eventHandler.invoke(BasalEvent.OnAddBasalItem)
                        isAddCardVisible = false
                    },
                    startTime = state.basalNewStartTime,
                    endTime = state.basalNewEndTime,
                    unitPerHourValue = if (state.basalItemValue == 0.0f) "" else state.basalItemValue.toString(),
                    onUpClick = {
                        if (it == 0) {
                            var startTime = TimeUtils.parseTime(state.basalNewStartTime)
                            startTime = TimeUtils.addMinutes(startTime, 15)
                            eventHandler.invoke(
                                BasalEvent.OnBasalNewStartTimeChange(
                                    TimeUtils.formatTime(startTime)
                                )
                            )
                        } else if (it == 1) {
                            var endTime = TimeUtils.parseTime(state.basalNewEndTime)
                            endTime = TimeUtils.addMinutes(endTime, 15)
                            eventHandler.invoke(
                                BasalEvent.OnBasalNewEndTimeChange(
                                    TimeUtils.formatTime(endTime)
                                )
                            )
                        }
                    },
                    onDownClick = {
                        if (it == 0) {
                            var startTime = TimeUtils.parseTime(state.basalNewStartTime)
                            startTime = TimeUtils.addMinutes(startTime, -15)
                            eventHandler.invoke(
                                BasalEvent.OnBasalNewStartTimeChange(
                                    TimeUtils.formatTime(startTime)
                                )
                            )
                        } else if (it == 1) {
                            var endTime = TimeUtils.parseTime(state.basalNewEndTime)
                            endTime = TimeUtils.addMinutes(endTime, -15)
                            eventHandler.invoke(
                                BasalEvent.OnBasalNewEndTimeChange(
                                    TimeUtils.formatTime(endTime)
                                )
                            )
                        }
                    },
                    onCarbCoefValueChange = {
                        eventHandler.invoke(BasalEvent.OnBasalNewValueChange(
                            if (it.isEmpty()) 0.0f else it.toFloatOrNull() ?: 0.0f
                        ))
                    },
                    measurementUnit = stringResource(R.string.unit_hour),
                    backgroundColor = MaterialTheme.colorScheme.secondaryContainer
                )
            }
            Spacer(modifier = Modifier.height(170.dp))
        }
        if (state.isNewChanges){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .align(Alignment.BottomCenter)
            ) {
                BaseButton(
                    onClick = {},
                    text = "Сохранить изменения и отправить",
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = MaterialTheme.colorScheme.tertiary,
                    textColor = MaterialTheme.colorScheme.onTertiary
                )
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
fun SetTemporaryBasal(){

}

@Composable
fun CurrentTemporaryBasal(){

}

@Composable
fun HorizontalArrowTimeButton(
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit,
    onHourTextValueChange: (String) -> Unit,
    onMinuteTextValueChange: (String) -> Unit,
    hourTextValue: Int,
    minuteTextValue: Int,
    modifier: Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top
    ) {
        ElevatedButton(
            modifier = Modifier,
            shape = RoundedCornerShape(12.dp),
            onClick = onLeftClick,
            elevation = ButtonDefaults.elevatedButtonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 4.dp / 2
            ),
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
            ),
            contentPadding = PaddingValues(16.dp)
        ) {
            Icon(
                painterResource(R.drawable.ic_arrow_left),
                contentDescription = "icon_edit",
                modifier = Modifier
                    .size(22.dp),
                MaterialTheme.colorScheme.onPrimary
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        TimeTextField(
            value = hourTextValue,
            onValueChange = onHourTextValueChange,
            text = "ч",
            maxValue = 24
        )
        Spacer(modifier = Modifier.width(8.dp))
        TimeTextField(
            value = minuteTextValue,
            onValueChange = onMinuteTextValueChange,
            text = "мин",
            maxValue = 59
        )
        Spacer(modifier = Modifier.width(8.dp))
        ElevatedButton(
            modifier = Modifier,
            shape = RoundedCornerShape(12.dp),
            onClick = onRightClick,
            elevation = ButtonDefaults.elevatedButtonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 4.dp / 2
            ),
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
            ),
            contentPadding = PaddingValues(16.dp)
        ) {
            Icon(
                painterResource(R.drawable.ic_arrow_right),
                contentDescription = "icon_edit",
                modifier = Modifier
                    .size(22.dp),
                MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun TimeTextField(
    value: Int,
    onValueChange: (String) -> Unit,
    text: String,
    maxValue: Int
){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = if(value == 0) "" else value.toString(),
            onValueChange = { newValue ->
                if ((newValue.length <= 2) && (newValue.isEmpty() || newValue.toIntOrNull()?.let { it in 0..maxValue } == true)) {
                    onValueChange(newValue)
                }
            },
            modifier = Modifier.width(80.dp),
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
            ),
            singleLine = true,
            textStyle = MaterialTheme.typography.headlineSmall.copy(
                textAlign = TextAlign.Center
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.5f),
                unfocusedBorderColor = MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.5f),
                focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                focusedContainerColor = MaterialTheme.colorScheme.primary,
                unfocusedContainerColor = MaterialTheme.colorScheme.primary
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier,
            text = text,
            color = MaterialTheme.colorScheme.onTertiary,
            style = MaterialTheme.typography.labelSmall
        )
    }
}