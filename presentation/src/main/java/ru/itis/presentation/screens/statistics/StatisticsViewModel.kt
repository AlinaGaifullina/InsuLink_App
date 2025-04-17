package ru.itis.presentation.screens.statistics

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
import javax.inject.Inject


data class StatisticsState(
    val userPhone: String = "",
    val userFirstName: String = "",
    val userLastName: String = "",
    val userCity: String? = null,
    val userCountry: String? = null,
)

sealed interface StatisticsEvent {
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
//        when (statisticsEvent) {
//
//        }
    }
}