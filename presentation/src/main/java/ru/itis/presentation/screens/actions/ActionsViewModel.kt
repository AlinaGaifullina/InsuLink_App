package ru.itis.presentation.screens.actions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import ru.itis.domain.model.Action
import ru.itis.domain.model.ActionType
import ru.itis.domain.model.HistorySizeType
import java.time.LocalDateTime
import javax.inject.Inject


data class ActionsState(
    val listOfActions: List<Action> = listOf(
        Action(
            id = "1",
            date = LocalDateTime.of(2025, 5, 7, 15, 30),
            type = ActionType.BOLUS,
            sugar = 5.1f,
            food = 2.0f,
            bolus = 2.0f,
            basalPercent = 0,
            basalHours = 0,
            basalMinutes = 0,
            basalSeconds = 0
        ),
        Action(
            id = "2",
            date = LocalDateTime.of(2025, 5, 8, 15, 30),
            type = ActionType.BOLUS,
            sugar = 5.1f,
            food = 2.0f,
            bolus = 2.0f,
            basalPercent = 0,
            basalHours = 0,
            basalMinutes = 0,
            basalSeconds = 0
        ),
        Action(
            id = "3",
            date = LocalDateTime.of(2025, 5, 8, 16, 30),
            type = ActionType.TEMPORARY_BASAL,
            sugar = 5.1f,
            food = 2.0f,
            bolus = 2.0f,
            basalPercent = 80,
            basalHours = 1,
            basalMinutes = 20,
            basalSeconds = 15
        )
    ),
    val historySizeType: HistorySizeType = HistorySizeType.WEEK
)

sealed interface ActionsEvent {

}

sealed interface ActionsSideEffect {

}

@HiltViewModel
class ActionsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state: MutableStateFlow<ActionsState> = MutableStateFlow(ActionsState())
    val state: StateFlow<ActionsState> = _state

    private val _action = MutableSharedFlow<ActionsSideEffect?>()
    val action: SharedFlow<ActionsSideEffect?>
        get() = _action.asSharedFlow()

    fun event(actionsEvent: ActionsEvent) {
        ViewModelProvider.NewInstanceFactory
//        when (actionsEvent) {
//
//        }
    }
}