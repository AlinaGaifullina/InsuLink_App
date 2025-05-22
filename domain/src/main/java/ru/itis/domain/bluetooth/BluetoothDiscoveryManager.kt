package ru.itis.domain.bluetooth

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.Manifest
import android.bluetooth.BluetoothDevice
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.delay

class BluetoothDiscoveryManager @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val bluetoothManager: BluetoothManager
) {
    @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT])
    suspend fun discoverDevices(): List<BluetoothDevice> = withContext(Dispatchers.IO) {
        val devices = mutableListOf<BluetoothDevice>()
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when(intent.action) {
                    BluetoothDevice.ACTION_FOUND -> {
                        val device = intent.getParcelableExtra<BluetoothDevice>(
                            BluetoothDevice.EXTRA_DEVICE
                        )
                        device?.takeIf {
                            !it.name.isNullOrEmpty() ||
                                    it.uuids?.isNotEmpty() == true
                        }?.let { devices.add(it) }
                    }
                }
            }
        }

        try {
            appContext.registerReceiver(receiver, IntentFilter(BluetoothDevice.ACTION_FOUND))
            bluetoothManager.startDiscovery()
            delay(8000)
        } finally {
            bluetoothManager.cancelDiscovery()
            appContext.unregisterReceiver(receiver)
        }

        return@withContext devices
            .distinctBy { it.address }
            .sortedBy { it.name?.isNotEmpty() }
    }
}