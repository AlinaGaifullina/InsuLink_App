package ru.itis.domain.bluetooth

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.util.Log
import androidx.annotation.RequiresPermission
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

class PumpBluetoothController @Inject constructor(
    private val bluetoothManager: BluetoothManager,
    private val cryptoKey: ByteArray // Должен быть внедрен через DI
) {
    private var listener: ((PumpEvent) -> Unit)? = null
    private var isListening = false

    @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT])
    suspend fun connectToPump(device: BluetoothDevice): Result<Unit> {
        return bluetoothManager.connectToDevice(device)
    }

    fun startListening(onEvent: (PumpEvent) -> Unit) {
        listener = onEvent
        isListening = true

        CoroutineScope(Dispatchers.IO).launch {
            while (isListening) {
                bluetoothManager.receiveData()
                    .onSuccess { data ->
                        Log.d("BLUETOOTH", "Получены данные: $data")
                        processReceivedData(data)
                    }
                    .onFailure { error ->
                        listener?.invoke(PumpEvent.ErrorEvent("connection_error: ${error.message}"))
                    }
                delay(100) // Throttle
            }
        }
    }

    fun stopListening() {
        isListening = false
        listener = null
    }

    private fun processReceivedData(data: String) {
        try {
            val json = JSONObject(data)
            val event = PumpCryptoManager.decryptPumpData(
                ivBase64 = json.getString("iv"),
                payloadBase64 = json.getString("payload"),
                tagBase64 = json.getString("tag"),
                key = cryptoKey
            )
            listener?.invoke(event)
        } catch (e: Exception) {
            listener?.invoke(PumpEvent.ErrorEvent("data_parse_error"))
        }
    }

    suspend fun sendCommand(command: String): Result<Unit> {
        return bluetoothManager.sendData(command)
    }

    fun disconnect() {
        bluetoothManager.disconnect()
        stopListening()
    }
}