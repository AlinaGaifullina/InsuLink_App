package ru.itis.presentation.screens.bolus.bolus

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import ru.itis.presentation.R
import ru.itis.presentation.components.BaseButton
import ru.itis.presentation.components.HorizontalArrowButton
import ru.itis.presentation.navigation.graphs.BolusNavScreen


@Composable
fun BolusScreen(
    navController: NavController,
    viewModel: BolusViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val eventHandler = viewModel::event
    val action by viewModel.action.collectAsStateWithLifecycle(null)

    LaunchedEffect(action) {
        when (action) {
            BolusSideEffect.NavigateCalculateBolus -> navController.navigate(BolusNavScreen.CalculateBolus.createRoute(
                nutritionValue = state.nutritionValue,
                glucoseValue = state.glucoseValue
            ))
            BolusSideEffect.NavigateBack -> navController.navigateUp()
            else -> Unit
        }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.tertiary),
                    start = Offset(0f, 0f),
                    end = Offset.Infinite
                )
            )
            .verticalScroll(scrollState)
            .padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.inject_bolus),
            modifier = Modifier,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSecondary
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.current_glucose),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondary,
            )
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalArrowButton(
                onRightClick = {
                    val currentValue = state.glucoseValue
                    val newValue = (currentValue + 0.1f).coerceAtMost(50.0f)
                    val roundedValue = "%.1f".format(newValue).toFloatSafe()
                    eventHandler.invoke(BolusEvent.OnGlucoseValueChange(roundedValue))
                },
                onLeftClick = {
                    val currentValue = state.glucoseValue
                    val newValue = (currentValue - 0.1f).coerceAtLeast(0.0f)
                    val roundedValue = "%.1f".format(newValue).toFloatSafe()
                    eventHandler.invoke(BolusEvent.OnGlucoseValueChange(roundedValue))
                },
                onTextValueChange = { newValue ->
                    val floatValue = when {
                        newValue.isEmpty() -> 0.0f
                        else -> newValue.toFloatSafe()
                    }
                    eventHandler.invoke(BolusEvent.OnGlucoseValueChange(floatValue))
                },
                textValue = when {
                    state.glucoseValue == 0.0f -> ""
                    else -> "%.1f".format(state.glucoseValue).replace(",", ".")
                },
                modifier = Modifier
            )
            Text(
                text = if(state.isMmolLiter) stringResource(R.string.mmol_l_long) else stringResource(R.string.mg_dl),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSecondary
            )
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = stringResource(R.string.nutrition),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondary
                )
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalArrowButton(
                onRightClick = {
                    val currentValue = state.nutritionValue
                    val newValue = (currentValue + 0.1f).coerceAtMost(50.0f)
                    val roundedValue = "%.1f".format(newValue).toFloatSafe()
                    eventHandler.invoke(BolusEvent.OnNutritionValueChange(roundedValue))
                },
                onLeftClick = {
                    val currentValue = state.nutritionValue
                    val newValue = (currentValue - 0.1f).coerceAtLeast(0.0f)
                    val roundedValue = "%.1f".format(newValue).toFloatSafe()
                    eventHandler.invoke(BolusEvent.OnNutritionValueChange(roundedValue))
                },
                onTextValueChange = { newValue ->
                    val floatValue = when {
                        newValue.isEmpty() -> 0.0f
                        else -> newValue.toFloatSafe()
                    }
                    eventHandler.invoke(BolusEvent.OnNutritionValueChange(floatValue))
                },
                textValue = when {
                    state.nutritionValue == 0.0f -> ""
                    else -> "%.1f".format(state.nutritionValue).replace(",", ".")
                },
                modifier = Modifier
            )
            Text(
                text = if(state.isBreadUnits) stringResource(R.string.bread_unit) else stringResource(R.string.carbohydrates_measurement_unit),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 16.dp)
        ) {
            BaseButton(
                onClick = { eventHandler.invoke(BolusEvent.OnCalculateButtonClick) },
                text = stringResource(R.string.calculate),
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
                    .height(50.dp)
                    .align(Alignment.CenterHorizontally)
                ,
                backgroundColor = MaterialTheme.colorScheme.secondary,
                textColor = MaterialTheme.colorScheme.onSecondary
            )
            BaseButton(
                onClick = { eventHandler.invoke(BolusEvent.OnCancelingButtonClick) },
                text = stringResource(R.string.canceling),
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
                    .height(50.dp)
                    .align(Alignment.CenterHorizontally)
                ,
                backgroundColor = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

fun String.toFloatSafe(): Float {
    return this.replace(",", ".").toFloatOrNull() ?: 0f
}