package ru.itis.presentation.screens.statistics

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.itis.domain.model.Action
import ru.itis.domain.model.HistorySizeType
import javax.inject.Inject


data class StatisticsState(
    val userPhone: String = "",
    val statisticsType: HistorySizeType = HistorySizeType.WEEK,
    val isLoading: Boolean = false,
    val listOfActions: List<Action> = listOf(),
)

sealed interface StatisticsEvent {
    data class OnStatisticsTypeChange(val value: HistorySizeType) : StatisticsEvent
}

sealed interface StatisticsSideEffect {

}

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state: MutableStateFlow<StatisticsState> = MutableStateFlow(StatisticsState())
    val state: StateFlow<StatisticsState> = _state

    private val _action = MutableSharedFlow<StatisticsSideEffect?>()
    val action: SharedFlow<StatisticsSideEffect?>
        get() = _action.asSharedFlow()

    fun event(statisticsEvent: StatisticsEvent) {
        ViewModelProvider.NewInstanceFactory
        when (statisticsEvent) {
            is StatisticsEvent.OnStatisticsTypeChange -> onStatisticsTypeChange(statisticsEvent.value)
        }
    }

    private fun onStatisticsTypeChange(type: HistorySizeType) {
        viewModelScope.launch {
            _state.tryEmit(_state.value.copy(isLoading = true))

            delay(500)

            _state.tryEmit(
                _state.value.copy(
                    statisticsType = type,
                    isLoading = false
                )
            )
        }
    }
}