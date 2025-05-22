package ru.itis.presentation.components

import androidx.compose.runtime.Composable
import android.Manifest
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestBluetoothPermissions(
    onPermissionsGranted: () -> Unit,
    onPermissionsDenied: () -> Unit
) {
    val context = LocalContext.current
    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT
        )
    )

    LaunchedEffect(permissionState) {
        if (permissionState.allPermissionsGranted) {
            onPermissionsGranted()
        } else {
            permissionState.launchMultiplePermissionRequest()
        }
    }

    if (!permissionState.allPermissionsGranted) {
        AlertDialog(
            onDismissRequest = onPermissionsDenied,
            title = { Text("Требуются разрешения") },
            text = { Text("Для работы с Bluetooth необходимо предоставить разрешения") },
            confirmButton = {
                Button(onClick = { permissionState.launchMultiplePermissionRequest() }) {
                    Text("Запросить разрешения")
                }
            },
            dismissButton = {
                Button(onClick = onPermissionsDenied) {
                    Text("Отмена")
                }
            }
        )
    }
}