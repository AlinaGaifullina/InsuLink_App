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
import ru.itis.domain.model.HistorySizeType
import javax.inject.Inject


data class ActionsState(
    val listOfActions: List<Action> = listOf(),
    val historySizeType: HistorySizeType = HistorySizeType.WEEK
)

sealed interface ActionsEvent {
    data class OnHistorySizeChange(val value: HistorySizeType) : ActionsEvent
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
        when (actionsEvent) {
            is ActionsEvent.OnHistorySizeChange -> onHistorySizeChange(actionsEvent.value)
        }
    }

    private fun onHistorySizeChange(size: HistorySizeType) {
        _state.tryEmit(_state.value.copy(historySizeType = size))
    }
}