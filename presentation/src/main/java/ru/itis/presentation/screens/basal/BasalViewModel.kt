package ru.itis.presentation.screens.basal

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.itis.domain.model.Basal
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlin.math.max
import kotlin.collections.*


data class BasalState(
    val currentTemporaryBasal: Int = 100,
    val newTemporaryBasal: Int = 100,
    val isTemporaryBasalActive: Boolean = false,
    val timerRemainingMillis: Long = 0, // Оставшееся время в миллисекундах
    val timerStartTime: Long? = null,  // Время старта таймера (System.currentTimeMillis())
    val isTimerRunning: Boolean = false,
    val hourValue: Int = 0,
    val minuteValue: Int = 15,


    //Basal
    val listOfBasal: List<Basal> = listOf(
        Basal("1","00:00","04:00",2.0f),
        Basal("1","00:00","04:00",2.0f),
        Basal("1","00:00","04:00",2.0f),
        Basal("1","00:00","04:00",2.0f),
        Basal("1","00:00","04:00",2.0f),
    ),
    val basalExpandedItemIndex: Int? = null,
    val basalNewStartTime: String = "00:00",
    val basalNewEndTime: String = "03:00",
    val basalNewValue: Float = 0.0f,
    val basalItemStartTime: String = "00:00",
    val basalItemEndTime: String = "03:00",
    val basalItemValue: Float = 0.0f,
)

sealed interface BasalEvent {
    data class OnHourValueChange(val value: Int) : BasalEvent
    data class OnMinuteValueChange(val value: Int) : BasalEvent
    data class OnNewBasalChange(val value: Int) : BasalEvent
    data class OnBasalExpandedItemChange(val value: Int) : BasalEvent
    data class OnBasalNewValueChange(val value: Float) : BasalEvent
    data class OnBasalNewStartTimeChange(val value: String) : BasalEvent
    data class OnBasalNewEndTimeChange(val value: String) : BasalEvent
    data class OnBasalItemValueChange(val value: Float) : BasalEvent
    data class OnBasalItemStartTimeChange(val value: String) : BasalEvent
    data class OnBasalItemEndTimeChange(val value: String) : BasalEvent
    data class OnSaveBasalItem(val value: Int) : BasalEvent
    data class OnDeleteBasalItem(val value: Int) : BasalEvent
    object OnAddBasalItem : BasalEvent

    object StartTimer : BasalEvent
    object StopTimer : BasalEvent
    data class SetTimerDuration(val hours: Int, val minutes: Int) : BasalEvent
}

sealed interface BasalSideEffect {

}

@HiltViewModel
class BasalViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state: MutableStateFlow<BasalState> = MutableStateFlow(BasalState())
    val state: StateFlow<BasalState> = _state

    private val _action = MutableSharedFlow<BasalSideEffect?>()
    val action: SharedFlow<BasalSideEffect?>
        get() = _action.asSharedFlow()

    init {
        viewModelScope.launch {
//            // Загрузка состояния таймера из БД при старте
//            val savedTimer = timerDao.getLastTimer()
//            savedTimer?.let {
//                _state.value = _state.value.copy(
//                    timerRemainingMillis = it.remainingMillis,
//                    timerStartTime = it.startTime,
//                    isTimerRunning = it.isRunning
//                )
//
//                if (it.isRunning) {
//                    startTimerInternal()
//                }
//            }
        }
    }


    fun event(basalEvent: BasalEvent) {
        ViewModelProvider.NewInstanceFactory
        when (basalEvent) {
            is BasalEvent.OnHourValueChange -> onHourValueChange(basalEvent.value)
            is BasalEvent.OnMinuteValueChange -> onMinuteValueChange(basalEvent.value)
            is BasalEvent.OnNewBasalChange -> onNewBasalChange(basalEvent.value)
            is BasalEvent.OnBasalExpandedItemChange -> onBasalExpandedItemChange(basalEvent.value)
            is BasalEvent.OnBasalNewValueChange -> onBasalNewValueChange(basalEvent.value)
            is BasalEvent.OnBasalNewStartTimeChange -> onBasalNewStartTimeChange(basalEvent.value)
            is BasalEvent.OnBasalNewEndTimeChange -> onBasalNewEndTimeChange(basalEvent.value)
            is BasalEvent.OnBasalItemValueChange -> onBasalItemValueChange(basalEvent.value)
            is BasalEvent.OnBasalItemStartTimeChange -> onBasalItemStartTimeChange(basalEvent.value)
            is BasalEvent.OnBasalItemEndTimeChange -> onBasalItemEndTimeChange(basalEvent.value)
            is BasalEvent.OnSaveBasalItem -> onSaveBasalItem(basalEvent.value)
            is BasalEvent.OnDeleteBasalItem -> onDeleteBasalItem(basalEvent.value)
            BasalEvent.OnAddBasalItem -> onAddBasalItem()
            is BasalEvent.SetTimerDuration -> {
                val totalMillis = (basalEvent.hours * 3600L + basalEvent.minutes * 60L) * 1000
                _state.tryEmit(_state.value.copy(
                    timerRemainingMillis = totalMillis
                ))
                saveTimerToDb()
            }
            BasalEvent.StartTimer -> {
                _state.tryEmit(_state.value.copy(
                    timerStartTime = System.currentTimeMillis(),
                    isTimerRunning = true
                ))
                saveTimerToDb()
                startTimerInternal()
            }
            BasalEvent.StopTimer -> {
                _state.tryEmit(_state.value.copy(
                    isTimerRunning = false,
                    timerStartTime = null
                ))
                saveTimerToDb()
            }
        }
    }

    private fun onNewBasalChange(value: Int) {
        _state.tryEmit(_state.value.copy(newTemporaryBasal = value))
    }

    private fun onHourValueChange(hourValue: Int) {
        _state.tryEmit(_state.value.copy(hourValue = hourValue))
    }

    private fun onMinuteValueChange(minuteValue: Int) {
        _state.tryEmit(_state.value.copy(minuteValue = minuteValue))
    }

    private fun startTimerInternal() {
        viewModelScope.launch {
            while (_state.value.isTimerRunning) {
                delay(1000)
                updateTimer()
            }
        }
    }

    private fun updateTimer() {
        val currentState = _state.value
        currentState.timerRemainingMillis.let { remainingMillis ->
            // Проверяем, что таймер все еще запущен
            if (remainingMillis > 0) {
                // Уменьшаем оставшееся время на 1000 миллисекунд
                val newRemainingMillis = max(0, remainingMillis - 1000)

                _state.tryEmit(currentState.copy(
                    timerRemainingMillis = newRemainingMillis,
                    isTimerRunning = newRemainingMillis > 0
                ))

                saveTimerToDb()
            }
        }
    }

    private fun saveTimerToDb() {

    }

    //Basal
    private fun onBasalExpandedItemChange(index: Int) {
        val isEmpty = _state.value.basalExpandedItemIndex == null
        if(isEmpty){
            _state.tryEmit(_state.value.copy(
                basalExpandedItemIndex = index,
                basalItemValue = _state.value.listOfBasal[index].value,
                basalItemStartTime = _state.value.listOfBasal[index].startTime,
                basalItemEndTime = _state.value.listOfBasal[index].endTime,
            ))
        } else {
            _state.tryEmit(_state.value.copy(
                basalExpandedItemIndex = if (index == _state.value.basalExpandedItemIndex){
                    null
                } else index,
                basalItemValue = _state.value.listOfBasal[index].value,
                basalItemStartTime = _state.value.listOfBasal[index].startTime,
                basalItemEndTime = _state.value.listOfBasal[index].endTime,
            ))
        }
    }

    private fun onBasalNewValueChange(value: Float) {
        _state.tryEmit(_state.value.copy(basalNewValue = value))
    }

    private fun onBasalNewStartTimeChange(startTime: String) {
        _state.tryEmit(_state.value.copy(basalNewStartTime = startTime))
    }

    private fun onBasalNewEndTimeChange(endTime: String) {
        _state.tryEmit(_state.value.copy(basalNewEndTime = endTime))
    }

    private fun onBasalItemValueChange(value: Float) {
        _state.tryEmit(_state.value.copy(basalItemValue = value))
    }

    private fun onBasalItemStartTimeChange(startTime: String) {
        _state.tryEmit(_state.value.copy(basalItemStartTime = startTime))
    }

    private fun onBasalItemEndTimeChange(endTime: String) {
        _state.tryEmit(_state.value.copy(basalItemEndTime = endTime))
    }

    private fun onSaveBasalItem(index: Int) {
        _state.tryEmit(_state.value.copy(
            listOfBasal = _state.value.listOfBasal.mapIndexed { i, item ->
                if (i == index) {
                    item.copy(
                        startTime = _state.value.basalItemStartTime,
                        endTime = _state.value.basalItemEndTime,
                        value = _state.value.basalItemValue
                    )
                } else {
                    item
                }
            },
            basalExpandedItemIndex = null
        ))
    }

    private fun onDeleteBasalItem(index: Int) {
        val newListOfCarbCoef = _state.value.listOfBasal.toMutableList()
        newListOfCarbCoef.removeAt(index)
        _state.tryEmit(_state.value.copy(
            listOfBasal = newListOfCarbCoef,
            basalExpandedItemIndex = null
        ))
    }

    private fun onAddBasalItem() {
        val newListOfCarbCoef = _state.value.listOfBasal.toMutableList()
        newListOfCarbCoef.add(
            Basal(
                id = "",
                startTime = _state.value.basalNewStartTime,
                endTime = _state.value.basalNewEndTime,
                value = _state.value.basalNewValue
            )
        )
        _state.tryEmit(_state.value.copy(
            listOfBasal = newListOfCarbCoef,
            basalNewValue = 0.0f,
            basalNewStartTime = "00:00",
            basalNewEndTime = "00:00"
        ))
    }
}