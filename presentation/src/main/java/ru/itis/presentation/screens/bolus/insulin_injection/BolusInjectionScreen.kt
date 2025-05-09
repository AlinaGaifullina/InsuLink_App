package ru.itis.presentation.screens.bolus.insulin_injection

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import ru.itis.presentation.R
import ru.itis.presentation.components.BaseButton
import ru.itis.presentation.navigation.BottomNavigationItem


@Composable
fun BolusInjectionScreen(
    navController: NavController,
    viewModel: BolusInjectionViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val eventHandler = viewModel::event
    val action by viewModel.action.collectAsStateWithLifecycle(null)

    val scrollState = rememberScrollState()
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(action) {
        when (action) {
            BolusInjectionSideEffect.NavigateProfile -> navController.navigate(BottomNavigationItem.Profile.graph)
            else -> Unit
        }
    }

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
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        if(state.isProcessActive){
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(80.dp))
                Text(
                    text = stringResource(R.string.wait_insulin_injection),
                    modifier = Modifier,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSecondary,
                    textAlign = TextAlign.Center
                )
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceTint)
            ) {
                Text(
                    text = "${state.bolusValue} Е",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(vertical = 16.dp, horizontal = 24.dp),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.secondaryContainer
                )
            }
            BaseButton(
                onClick = {
                    showDialog = true
                    eventHandler.invoke(BolusInjectionEvent.OnStopButtonClick)
                          },
                text = stringResource(R.string.stop),
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth()
                    .height(50.dp)
                    .align(Alignment.CenterHorizontally)
                ,
                backgroundColor = MaterialTheme.colorScheme.primary,
                textColor = MaterialTheme.colorScheme.onPrimary
            )

            if (showDialog) {
                Dialog(
                    onDismissRequest = {showDialog = false},
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
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(R.string.stop_insulin_injection_process),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onPrimary,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(28.dp))

                            BaseButton(
                                onClick = {
                                    eventHandler.invoke(BolusInjectionEvent.OnContinueInjectModalButtonClick)
                                    showDialog = false
                                },
                                text = stringResource(R.string.continue_),
                                modifier = Modifier.fillMaxWidth(),
                                backgroundColor = MaterialTheme.colorScheme.secondary,
                                textColor = MaterialTheme.colorScheme.onSecondary
                            )

                            BaseButton(
                                onClick = {
                                    eventHandler.invoke(BolusInjectionEvent.OnStopModalButtonClick)
                                    showDialog = false
                                },
                                text = stringResource(R.string.stop),
                                modifier = Modifier.fillMaxWidth(),
                                backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                                textColor = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
        } else {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = stringResource(R.string.process_stopped),
                modifier = Modifier,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSecondary
            )
            BaseButton(
                onClick = { eventHandler.invoke(BolusInjectionEvent.OnOkButtonClick) },
                text = stringResource(R.string.good),
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