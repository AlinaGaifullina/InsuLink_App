package ru.itis.presentation.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
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
import ru.itis.presentation.components.DropdownTextField
import ru.itis.presentation.components.ExpandableViewCarbCoefs
import ru.itis.presentation.components.HeaderView
import ru.itis.presentation.components.SwitchButton
import ru.itis.presentation.navigation.graphs.bottom_bar.ProfileNavScreen
import ru.itis.presentation.utils.InsulinRepository


@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val eventHandler = viewModel::event
    val action by viewModel.action.collectAsStateWithLifecycle(null)

    LaunchedEffect(action) {
        when (action) {
            ProfileSideEffect.NavigateEditProfileScreen -> navController.navigate(ProfileNavScreen.Profile.route)
            else -> Unit
        }
    }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.tertiary),
                        start = Offset(0f, 0f),
                        end = Offset.Infinite
                    )
                )
        ) {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                SwitchButton(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = 16.dp),
                    selectedColor = MaterialTheme.colorScheme.secondary,
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    textSelectedColor = MaterialTheme.colorScheme.onSecondary,
                    textBackgroundColor = MaterialTheme.colorScheme.onPrimary,
                    height = 40.dp,
                    titleFirst = stringResource(R.string.profile),
                    titleSecond = stringResource(R.string.pump)
                ) { isProfileMode ->
                    if(isProfileMode != state.isProfileMode) eventHandler.invoke(ProfileEvent.OnModeChange)
                }
                if(state.isProfileMode) {
                    ProfileBoxContent(state, eventHandler)
                } else {
                    PumpBoxContent(state, eventHandler)
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        if(state.isProfileMode) {
            ProfileMainContent(state, eventHandler)
        } else {
            PumpMainContent(state, eventHandler)
        }
    }
}

@Composable
fun ProfileBoxContent(state: ProfileState, eventHandler: (ProfileEvent) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceTint)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(0.8f),
                text = state.surname + " " + state.name + " " + state.patronymic,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondary
            )
            Icon(
                painter = painterResource(R.drawable.ic_edit),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier
                    .weight(0.1f)
                    .clickable { eventHandler.invoke(ProfileEvent.OnEditProfileButtonClick) }
            )
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
    Column {
        ProfileInfoRow(
            title = stringResource(R.string.gender),
            text = if(state.isMale == null) {
                "не указан"
            } else if(state.isMale == true){
                stringResource(R.string.gender_male)
            } else stringResource(R.string.gender_female)
        )
        Spacer(modifier = Modifier.height(8.dp))
        ProfileInfoRow(
            title = stringResource(R.string.user_birth_date),
            text = state.birthDate
        )
        Spacer(modifier = Modifier.height(8.dp))
        ProfileInfoRow(
            title = stringResource(R.string.user_height),
            text = state.height.toString()
        )
        Spacer(modifier = Modifier.height(8.dp))
        ProfileInfoRow(
            title = stringResource(R.string.user_weight),
            text = state.weight.toString()
        )
    }
    Spacer(modifier = Modifier.height(20.dp))
}

@Composable
fun ProfileInfoRow(title: String, text: String){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onTertiary,
            modifier = Modifier.weight(0.5f)
        )
        Box(
            modifier = Modifier
                .weight(0.5f)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ProfileMainContent(state: ProfileState, eventHandler: (ProfileEvent) -> Unit) {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.insulin),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(start = 8.dp, bottom = 4.dp),
        )
        DropdownTextField(
            selectedItem = state.insulin,
            itemList = InsulinRepository.insulinList.map { it.name },
            onSelect = {
                eventHandler.invoke(
                    ProfileEvent.OnInsulinChange(it)
                )
            },
            modifier = Modifier,
            backgroundColor = MaterialTheme.colorScheme.primary,
            textColor = MaterialTheme.colorScheme.onPrimary,
            iconColor = MaterialTheme.colorScheme.onPrimary,
            textFieldModifier = Modifier
                .border(2.dp, color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f))
                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
        )
    }
}

@Composable
fun PumpBoxContent(state: ProfileState, eventHandler: (ProfileEvent) -> Unit) {
    Text(
        text = if(state.isPumpConnected) state.pumpName else stringResource(R.string.pump_not_connected),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(20.dp))
    if (!state.isPumpConnected) {
        BaseButton(
            text = stringResource(R.string.connect),
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = MaterialTheme.colorScheme.surfaceTint,
            textColor = MaterialTheme.colorScheme.onSecondary,
            onClick = { eventHandler.invoke(ProfileEvent.OnConnectButtonClick) }
        )
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
fun PumpMainContent(state: ProfileState, eventHandler: (ProfileEvent) -> Unit) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.carbohydrates_measurement_unit),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        SwitchButton(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 16.dp),
            selectedColor = MaterialTheme.colorScheme.secondary,
            backgroundColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
            textSelectedColor = MaterialTheme.colorScheme.onSecondary,
            textBackgroundColor = MaterialTheme.colorScheme.onPrimary,
            height = 40.dp,
            titleFirst = stringResource(R.string.bread_unit),
            titleSecond = stringResource(R.string.grams_unit)
        ) { isBreadUnits ->
            if(isBreadUnits != state.isBreadUnits) eventHandler.invoke(ProfileEvent.OnCarbUnitChange)
        }
        Text(
            text = stringResource(R.string.carbohydrates_measurement_unit),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        SwitchButton(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 16.dp),
            selectedColor = MaterialTheme.colorScheme.secondary,
            backgroundColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
            textSelectedColor = MaterialTheme.colorScheme.onSecondary,
            textBackgroundColor = MaterialTheme.colorScheme.onPrimary,
            height = 40.dp,
            titleFirst = stringResource(R.string.mmol_l_long),
            titleSecond = stringResource(R.string.mg_dl)
        ) { isMmolLiter ->
            if(isMmolLiter != state.isMmolLiter) eventHandler.invoke(ProfileEvent.OnGlucoseUnitChange)
        }

        Text(
            text = stringResource(R.string.active_insulin_time),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = if (state.activeInsulinTime == 0.0f) "" else state.activeInsulinTime.toString(),
            onValueChange = {
                eventHandler.invoke(
                    ProfileEvent.OnActiveInsulinChange(
                        if (it.isEmpty()) 0.0f else it.toFloatOrNull() ?: 0.0f
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
            ),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimary,
                focusedContainerColor = MaterialTheme.colorScheme.primary,
                unfocusedContainerColor = MaterialTheme.colorScheme.primary
            )
        )
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier,
                elevation = CardDefaults.cardElevation(10.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column {
                    HeaderView(
                        headerText = stringResource(R.string.carbohydrate_coefficients),
                        onClickItem = {
                            eventHandler.invoke(ProfileEvent.OnCarbCoefExpandedChange)
                        },
                        isExpanded = state.isCarbCoefExpanded,
                    )
                    ExpandableViewCarbCoefs(
                        listOfCarbCoef = state.listOfCarbCoefs,
                        isExpanded = state.isCarbCoefExpanded,
                        onEditClick = {
                            eventHandler.invoke(ProfileEvent.OnCarbCoefExpandedItemChange(it))
                        },
                        onSaveClick = {},
                        onDeleteClick = {},
                        expandedItem = state.carbCoefExpandedItemIndex
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(80.dp))
    }
}