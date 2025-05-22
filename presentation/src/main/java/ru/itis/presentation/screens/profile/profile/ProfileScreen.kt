package ru.itis.presentation.screens.profile.profile

import android.app.Activity
import android.bluetooth.BluetoothDevice
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import ru.itis.presentation.R
import ru.itis.presentation.components.BaseButton
import ru.itis.presentation.components.DropdownTextField
import ru.itis.presentation.components.RequestBluetoothPermissions
import ru.itis.presentation.components.pump_settings.HeaderView
import ru.itis.presentation.components.SwitchButton
import ru.itis.presentation.components.pump_settings.carb_coef.ExpandableViewCarbCoefs
import ru.itis.presentation.components.pump_settings.glucose.ExpandableViewGlucose
import ru.itis.presentation.components.pump_settings.ins_sensitivity.ExpandableViewInsSens
import ru.itis.presentation.navigation.graphs.AuthNavScreen
import ru.itis.presentation.navigation.graphs.BolusNavScreen
import ru.itis.presentation.navigation.graphs.bottom_bar.ProfileNavScreen
import ru.itis.presentation.utils.InsulinRepository
import ru.itis.presentation.utils.TimeUtils

@Composable
fun BluetoothDataDisplay(data: String, isVisible: Boolean) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Text(
                text = data,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

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
            ProfileSideEffect.NavigateBolusInjection -> navController.navigate(BolusNavScreen.BolusInjection.route)
            ProfileSideEffect.NavigateAccessDelegation -> navController.navigate(ProfileNavScreen.AccessDelegation.route)
            ProfileSideEffect.NavigateSignInScreen -> navController.navigate(AuthNavScreen.SignIn.route)
            else -> Unit
        }
    }

    val context = LocalContext.current
    val activity = context as? Activity

    BluetoothDataDisplay(state.bluetoothData, state.isReceivingData)

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
                    .padding(horizontal = 16.dp)
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
                    PumpBoxContent(state, eventHandler, activity)
                }
            }
        }
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
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(R.string.insulin),
            style = MaterialTheme.typography.labelSmall,
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
            backgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
            textColor = MaterialTheme.colorScheme.onTertiary,
            iconColor = MaterialTheme.colorScheme.onTertiary,
            textStyle = MaterialTheme.typography.labelLarge,
            textFieldModifier = Modifier
                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                //.border(1.dp, color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f), shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Тип аккаунта",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(start = 8.dp, bottom = 4.dp),
        )

        OutlinedTextField(
            value = "Главный аккаунт",
            shape = RoundedCornerShape(8.dp),
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_lock),
                    contentDescription = null,
                    modifier = Modifier,
                    tint = MaterialTheme.colorScheme.onTertiary
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                focusedTextColor = MaterialTheme.colorScheme.onTertiary,
                unfocusedTextColor = MaterialTheme.colorScheme.onTertiary,
                cursorColor = MaterialTheme.colorScheme.onTertiary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            textStyle = MaterialTheme.typography.labelLarge,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))
        BaseButton(
            onClick = { eventHandler.invoke(ProfileEvent.OnDelegateButtonClick) },
            text = "Делегировать доступ",
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = MaterialTheme.colorScheme.tertiary,
            textColor = MaterialTheme.colorScheme.onTertiary,
        )
        Spacer(modifier = Modifier.height(40.dp))

        TextButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {eventHandler.invoke(ProfileEvent.OnLogOutButtonClick)}
        ) {
            Text(
                text = "Выйти из аккаунта",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onTertiary.copy(alpha = 0.6f),
                textDecoration = TextDecoration.Underline,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun PumpBoxContent(
    state: ProfileState,
    eventHandler: (ProfileEvent) -> Unit,
    activity: Activity?
) {
    var showDeviceSelection by remember { mutableStateOf(false) }

    // Запрос разрешений
    RequestBluetoothPermissions(
        onPermissionsGranted = {
            // Разрешения получены - можно показывать Bluetooth UI
            showDeviceSelection = true
        },
        onPermissionsDenied = {
            // Обработка отказа в разрешениях

        }
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (!state.bluetoothEnabled) {
            Text(
                text = "Bluetooth выключен",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            BaseButton(
                text = "Включить Bluetooth",
                onClick = {
                    activity?.let {
                        eventHandler(ProfileEvent.OnEnableBluetooth(it))
                    }
                          },
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = MaterialTheme.colorScheme.surfaceTint,
                textColor = MaterialTheme.colorScheme.onSecondary,
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Статус подключения
        Text(
            text = if (state.isPumpConnected) state.pumpName else stringResource(R.string.pump_not_connected),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Индикатор состояния
        when (state.connectionProgress) {
            is ConnectionProgress.Connecting -> {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Подключение...",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            is ConnectionProgress.Error -> {
                Text(
                    text = (state.connectionProgress as ConnectionProgress.Error).message,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }
            else -> {}
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Кнопки управления
        if (!state.isPumpConnected) {
            BaseButton(
                text = stringResource(R.string.connect),
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = MaterialTheme.colorScheme.surfaceTint,
                textColor = MaterialTheme.colorScheme.onSecondary,
                onClick = {
                    eventHandler.invoke(ProfileEvent.OnConnectButtonClick)
                    showDeviceSelection = true
                }
            )
        } else {
            BaseButton(
                text = stringResource(R.string.disconnect),
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.onPrimary,
                onClick = { eventHandler.invoke(ProfileEvent.OnDisconnectButtonClick) }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }

    // Диалог выбора устройства
    if (showDeviceSelection) {
        PumpDeviceSelectionDialog(
            state = state,
            onDeviceSelected = { device ->
                eventHandler.invoke(ProfileEvent.OnDeviceSelected(device))
                showDeviceSelection = false
            },
            onSearchDevices = { eventHandler.invoke(ProfileEvent.OnSearchDevicesClick) },
            onCancelSearch = { eventHandler.invoke(ProfileEvent.OnCancelSearchClick) },
            onDismiss = { showDeviceSelection = false }
        )
    }
}

@Composable
private fun PumpDeviceSelectionDialog(
    state: ProfileState,
    onDeviceSelected: (BluetoothDeviceUI) -> Unit,
    onSearchDevices: () -> Unit,
    onCancelSearch: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .width(300.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.primary,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Выберите помпу среди найденных устройств",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onTertiary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                if (state.isSearchingDevices) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Поиск устройств...",
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    BaseButton(
                        onClick = onCancelSearch,
                        modifier = Modifier.fillMaxWidth(),
                        text = "Отменить поиск",
                        backgroundColor = MaterialTheme.colorScheme.secondary,
                        textColor = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    if (state.foundDevices.isEmpty()) {
                        Text("Устройства не найдены", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    BaseButton(
                        onClick = onSearchDevices,
                        modifier = Modifier.fillMaxWidth(),
                        text = "Поиск устройств",
                        backgroundColor = MaterialTheme.colorScheme.secondary,
                        textColor = MaterialTheme.colorScheme.onSecondary
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Найденные устройства:",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onTertiary,
                        textAlign = TextAlign.Start
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }

                LazyColumn(modifier = Modifier.heightIn(max = 200.dp)) {
                    items(state.foundDevices) { device ->
                        PumpDeviceItem(
                            device = device,
                            onClick = { onDeviceSelected(device) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                BaseButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    text = "Отмена",
                    backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                    textColor = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
private fun PumpDeviceItem(
    device: BluetoothDeviceUI,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
//            Icon(
//                imageVector = Icons.Default.MedicalServices,
//                contentDescription = "Помпа",
//                tint = MaterialTheme.colorScheme.primary
//            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = device.name,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onTertiary
                )
            }

            if (device.isConnecting) {
                Spacer(modifier = Modifier.weight(1f))
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
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
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(R.string.carbohydrates_measurement_unit),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        SwitchButton(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
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
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = stringResource(R.string.carbohydrates_measurement_unit),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        SwitchButton(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
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
        Spacer(modifier = Modifier.height(20.dp))

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
                .fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
            ),
            textStyle = MaterialTheme.typography.labelLarge,
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
        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier,
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                // CarbCoef
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
                        onSaveClick = {
                            eventHandler.invoke(ProfileEvent.OnSaveCarbCoefItem(it))
                        },
                        onDeleteClick = {
                            eventHandler.invoke(ProfileEvent.OnDeleteCarbCoefItem(it))
                        },
                        onAddClick = {
                            eventHandler.invoke(ProfileEvent.OnAddCarbCoefItem)
                        },
                        expandedItem = state.carbCoefExpandedItemIndex,
                        newStartTime = state.carbCoefNewStartTime,
                        newEndTime = state.carbCoefNewEndTime,
                        newUnitPerHourValue = if (state.carbCoefNewValue == 0.0f) "" else state.carbCoefNewValue.toString(),
                        newOnUpClick = {
                            if (it == 0) {
                                var startTime = TimeUtils.parseTime(state.carbCoefNewStartTime)
                                startTime = TimeUtils.addMinutes(startTime, 15)
                                eventHandler.invoke(
                                    ProfileEvent.OnCarbCoefNewStartTimeChange(
                                        TimeUtils.formatTime(startTime)
                                    )
                                )
                            } else if (it == 1) {
                                var endTime = TimeUtils.parseTime(state.carbCoefNewEndTime)
                                endTime = TimeUtils.addMinutes(endTime, 15)
                                eventHandler.invoke(
                                    ProfileEvent.OnCarbCoefNewEndTimeChange(
                                        TimeUtils.formatTime(endTime)
                                    )
                                )
                            }
                        },
                        newOnDownClick = {
                            if (it == 0) {
                                var startTime = TimeUtils.parseTime(state.carbCoefNewStartTime)
                                startTime = TimeUtils.addMinutes(startTime, -15)
                                eventHandler.invoke(
                                    ProfileEvent.OnCarbCoefNewStartTimeChange(
                                        TimeUtils.formatTime(startTime)
                                    )
                                )
                            } else if (it == 1) {
                                var endTime = TimeUtils.parseTime(state.carbCoefNewEndTime)
                                endTime = TimeUtils.addMinutes(endTime, -15)
                                eventHandler.invoke(
                                    ProfileEvent.OnCarbCoefNewEndTimeChange(
                                        TimeUtils.formatTime(endTime)
                                    )
                                )
                            }
                        },
                        newOnCarbCoefValueChange = {
                            eventHandler.invoke(
                                ProfileEvent.OnCarbCoefNewValueChange(
                                    if (it.isEmpty()) 0.0f else it.toFloatOrNull() ?: 0.0f
                                )
                            )
                        },
                        itemStartTime = state.carbCoefItemStartTime,
                        itemEndTime = state.carbCoefItemEndTime,
                        itemUnitPerHourValue = if (state.carbCoefItemValue == 0.0f) "" else state.carbCoefItemValue.toString(),
                        itemOnUpClick = {
                            if (it == 0) {
                                var startTime = TimeUtils.parseTime(state.carbCoefItemStartTime)
                                startTime = TimeUtils.addMinutes(startTime, 15)
                                eventHandler.invoke(
                                    ProfileEvent.OnCarbCoefItemStartTimeChange(
                                        TimeUtils.formatTime(startTime)
                                    )
                                )
                            } else if (it == 1) {
                                var endTime = TimeUtils.parseTime(state.carbCoefItemEndTime)
                                endTime = TimeUtils.addMinutes(endTime, 15)
                                eventHandler.invoke(
                                    ProfileEvent.OnCarbCoefItemEndTimeChange(
                                        TimeUtils.formatTime(endTime)
                                    )
                                )
                            }
                        },
                        itemOnDownClick = {
                            if (it == 0) {
                                var startTime = TimeUtils.parseTime(state.carbCoefItemStartTime)
                                startTime = TimeUtils.addMinutes(startTime, -15)
                                eventHandler.invoke(
                                    ProfileEvent.OnCarbCoefItemStartTimeChange(
                                        TimeUtils.formatTime(startTime)
                                    )
                                )
                            } else if (it == 1) {
                                var endTime = TimeUtils.parseTime(state.carbCoefItemEndTime)
                                endTime = TimeUtils.addMinutes(endTime, -15)
                                eventHandler.invoke(
                                    ProfileEvent.OnCarbCoefItemEndTimeChange(
                                        TimeUtils.formatTime(endTime)
                                    )
                                )
                            }
                        },
                        itemOnCarbCoefValueChange = {
                            eventHandler.invoke(
                                ProfileEvent.OnCarbCoefItemValueChange(
                                    if (it.isEmpty()) 0.0f else it.toFloatOrNull() ?: 0.0f
                                )
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            
            // InsSens
            Card(
                modifier = Modifier,
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column {
                    HeaderView(
                        headerText = stringResource(R.string.insulin_sensitivity),
                        onClickItem = {
                            eventHandler.invoke(ProfileEvent.OnInsSensExpandedChange)
                        },
                        isExpanded = state.isInsSensExpanded,
                    )
                    ExpandableViewInsSens(
                        listOfInsSens = state.listOfInsSens,
                        isExpanded = state.isInsSensExpanded,
                        onEditClick = {
                            eventHandler.invoke(ProfileEvent.OnInsSensExpandedItemChange(it))
                        },
                        onSaveClick = {
                            eventHandler.invoke(ProfileEvent.OnSaveInsSensItem(it))
                        },
                        onDeleteClick = {
                            eventHandler.invoke(ProfileEvent.OnDeleteInsSensItem(it))
                        },
                        onAddClick = {
                            eventHandler.invoke(ProfileEvent.OnAddInsSensItem)
                        },
                        expandedItem = state.insSensExpandedItemIndex,
                        newStartTime = state.insSensNewStartTime,
                        newEndTime = state.insSensNewEndTime,
                        newValue = if (state.insSensNewValue == 0.0f) "" else state.insSensNewValue.toString(),
                        newOnUpClick = {
                            if (it == 0) {
                                var startTime = TimeUtils.parseTime(state.insSensNewStartTime)
                                startTime = TimeUtils.addMinutes(startTime, 15)
                                eventHandler.invoke(
                                    ProfileEvent.OnInsSensNewStartTimeChange(
                                        TimeUtils.formatTime(startTime)
                                    )
                                )
                            } else if (it == 1) {
                                var endTime = TimeUtils.parseTime(state.insSensNewEndTime)
                                endTime = TimeUtils.addMinutes(endTime, 15)
                                eventHandler.invoke(
                                    ProfileEvent.OnInsSensNewEndTimeChange(
                                        TimeUtils.formatTime(endTime)
                                    )
                                )
                            }
                        },
                        newOnDownClick = {
                            if (it == 0) {
                                var startTime = TimeUtils.parseTime(state.insSensNewStartTime)
                                startTime = TimeUtils.addMinutes(startTime, -15)
                                eventHandler.invoke(
                                    ProfileEvent.OnInsSensNewStartTimeChange(
                                        TimeUtils.formatTime(startTime)
                                    )
                                )
                            } else if (it == 1) {
                                var endTime = TimeUtils.parseTime(state.insSensNewEndTime)
                                endTime = TimeUtils.addMinutes(endTime, -15)
                                eventHandler.invoke(
                                    ProfileEvent.OnInsSensNewEndTimeChange(
                                        TimeUtils.formatTime(endTime)
                                    )
                                )
                            }
                        },
                        newOnInsSensValueChange = {
                            eventHandler.invoke(
                                ProfileEvent.OnInsSensNewValueChange(
                                    if (it.isEmpty()) 0.0f else it.toFloatOrNull() ?: 0.0f
                                )
                            )
                        },
                        itemStartTime = state.insSensItemStartTime,
                        itemEndTime = state.insSensItemEndTime,
                        itemValue = if (state.insSensItemValue == 0.0f) "" else state.insSensItemValue.toString(),
                        itemOnUpClick = {
                            if (it == 0) {
                                var startTime = TimeUtils.parseTime(state.insSensItemStartTime)
                                startTime = TimeUtils.addMinutes(startTime, 15)
                                eventHandler.invoke(
                                    ProfileEvent.OnInsSensItemStartTimeChange(
                                        TimeUtils.formatTime(startTime)
                                    )
                                )
                            } else if (it == 1) {
                                var endTime = TimeUtils.parseTime(state.insSensItemEndTime)
                                endTime = TimeUtils.addMinutes(endTime, 15)
                                eventHandler.invoke(
                                    ProfileEvent.OnInsSensItemEndTimeChange(
                                        TimeUtils.formatTime(endTime)
                                    )
                                )
                            }
                        },
                        itemOnDownClick = {
                            if (it == 0) {
                                var startTime = TimeUtils.parseTime(state.insSensItemStartTime)
                                startTime = TimeUtils.addMinutes(startTime, -15)
                                eventHandler.invoke(
                                    ProfileEvent.OnInsSensItemStartTimeChange(
                                        TimeUtils.formatTime(startTime)
                                    )
                                )
                            } else if (it == 1) {
                                var endTime = TimeUtils.parseTime(state.insSensItemEndTime)
                                endTime = TimeUtils.addMinutes(endTime, -15)
                                eventHandler.invoke(
                                    ProfileEvent.OnInsSensItemEndTimeChange(
                                        TimeUtils.formatTime(endTime)
                                    )
                                )
                            }
                        },
                        itemOnInsSensValueChange = {
                            eventHandler.invoke(
                                ProfileEvent.OnInsSensItemValueChange(
                                    if (it.isEmpty()) 0.0f else it.toFloatOrNull() ?: 0.0f
                                )
                            )
                        },
                        isMmolLiter = state.isMmolLiter,
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Glucose
            Card(
                modifier = Modifier,
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column {
                    HeaderView(
                        headerText = stringResource(R.string.target_glucose),
                        onClickItem = {
                            eventHandler.invoke(ProfileEvent.OnGlucoseExpandedChange)
                        },
                        isExpanded = state.isGlucoseExpanded,
                    )
                    ExpandableViewGlucose(
                        listOfGlucose = state.listOfGlucose,
                        isExpanded = state.isGlucoseExpanded,
                        onEditClick = {
                            eventHandler.invoke(ProfileEvent.OnGlucoseExpandedItemChange(it))
                        },
                        onSaveClick = {
                            eventHandler.invoke(ProfileEvent.OnSaveGlucoseItem(it))
                        },
                        onDeleteClick = {
                            eventHandler.invoke(ProfileEvent.OnDeleteGlucoseItem(it))
                        },
                        onAddClick = {
                            eventHandler.invoke(ProfileEvent.OnAddGlucoseItem)
                        },
                        expandedItem = state.glucoseExpandedItemIndex,
                        newStartTime = state.glucoseNewStartTime,
                        newEndTime = state.glucoseNewEndTime,
                        newStartValue = if (state.glucoseNewStartValue == 0.0f) "" else state.glucoseNewStartValue.toString(),
                        newEndValue = if (state.glucoseNewEndValue == 0.0f) "" else state.glucoseNewEndValue.toString(),
                        newOnUpClick = {
                            if (it == 0) {
                                var startTime = TimeUtils.parseTime(state.glucoseNewStartTime)
                                startTime = TimeUtils.addMinutes(startTime, 15)
                                eventHandler.invoke(
                                    ProfileEvent.OnGlucoseNewStartTimeChange(
                                        TimeUtils.formatTime(startTime)
                                    )
                                )
                            } else if (it == 1) {
                                var endTime = TimeUtils.parseTime(state.glucoseNewEndTime)
                                endTime = TimeUtils.addMinutes(endTime, 15)
                                eventHandler.invoke(
                                    ProfileEvent.OnGlucoseNewEndTimeChange(
                                        TimeUtils.formatTime(endTime)
                                    )
                                )
                            }
                        },
                        newOnDownClick = {
                            if (it == 0) {
                                var startTime = TimeUtils.parseTime(state.glucoseNewStartTime)
                                startTime = TimeUtils.addMinutes(startTime, -15)
                                eventHandler.invoke(
                                    ProfileEvent.OnGlucoseNewStartTimeChange(
                                        TimeUtils.formatTime(startTime)
                                    )
                                )
                            } else if (it == 1) {
                                var endTime = TimeUtils.parseTime(state.glucoseNewEndTime)
                                endTime = TimeUtils.addMinutes(endTime, -15)
                                eventHandler.invoke(
                                    ProfileEvent.OnGlucoseNewEndTimeChange(
                                        TimeUtils.formatTime(endTime)
                                    )
                                )
                            }
                        },
                        newOnGlucoseStartValueChange = {
                            eventHandler.invoke(
                                ProfileEvent.OnGlucoseNewStartValueChange(
                                    if (it.isEmpty()) 0.0f else it.toFloatOrNull() ?: 0.0f
                                )
                            )
                        },
                        newOnGlucoseEndValueChange = {
                            eventHandler.invoke(
                                ProfileEvent.OnGlucoseNewEndValueChange(
                                    if (it.isEmpty()) 0.0f else it.toFloatOrNull() ?: 0.0f
                                )
                            )
                        },
                        itemStartTime = state.glucoseItemStartTime,
                        itemEndTime = state.glucoseItemEndTime,
                        itemStartValue = if (state.glucoseItemStartValue == 0.0f) "" else state.glucoseItemStartValue.toString(),
                        itemEndValue = if (state.glucoseItemEndValue == 0.0f) "" else state.glucoseItemEndValue.toString(),
                        itemOnUpClick = {
                            if (it == 0) {
                                var startTime = TimeUtils.parseTime(state.glucoseItemStartTime)
                                startTime = TimeUtils.addMinutes(startTime, 15)
                                eventHandler.invoke(
                                    ProfileEvent.OnGlucoseItemStartTimeChange(
                                        TimeUtils.formatTime(startTime)
                                    )
                                )
                            } else if (it == 1) {
                                var endTime = TimeUtils.parseTime(state.glucoseItemEndTime)
                                endTime = TimeUtils.addMinutes(endTime, 15)
                                eventHandler.invoke(
                                    ProfileEvent.OnGlucoseItemEndTimeChange(
                                        TimeUtils.formatTime(endTime)
                                    )
                                )
                            }
                        },
                        itemOnDownClick = {
                            if (it == 0) {
                                var startTime = TimeUtils.parseTime(state.glucoseItemStartTime)
                                startTime = TimeUtils.addMinutes(startTime, -15)
                                eventHandler.invoke(
                                    ProfileEvent.OnGlucoseItemStartTimeChange(
                                        TimeUtils.formatTime(startTime)
                                    )
                                )
                            } else if (it == 1) {
                                var endTime = TimeUtils.parseTime(state.glucoseItemEndTime)
                                endTime = TimeUtils.addMinutes(endTime, -15)
                                eventHandler.invoke(
                                    ProfileEvent.OnGlucoseItemEndTimeChange(
                                        TimeUtils.formatTime(endTime)
                                    )
                                )
                            }
                        },
                        itemOnGlucoseStartValueChange = {
                            eventHandler.invoke(
                                ProfileEvent.OnGlucoseItemStartValueChange(
                                    if (it.isEmpty()) 0.0f else it.toFloatOrNull() ?: 0.0f
                                )
                            )
                        },
                        itemOnGlucoseEndValueChange = {
                            eventHandler.invoke(
                                ProfileEvent.OnGlucoseItemEndValueChange(
                                    if (it.isEmpty()) 0.0f else it.toFloatOrNull() ?: 0.0f
                                )
                            )
                        },
                        isMmolLiter = state.isMmolLiter,
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(100.dp))
    }
}