package ru.itis.domain.bluetooth

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import java.io.IOException
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlin.Result

class BluetoothManager @Inject constructor(
    private val context: Context
) {
    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private var currentDevice: BluetoothDevice? = null
    private var bluetoothSocket: BluetoothSocket? = null

    fun isBluetoothEnabled(): Boolean {
        return bluetoothAdapter?.isEnabled ?: false
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun enableBluetooth(activity: Activity) {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun getPairedDevices(): Set<BluetoothDevice> {
        return bluetoothAdapter?.bondedDevices ?: emptySet()
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    fun startDiscovery(): Boolean {
        return bluetoothAdapter?.startDiscovery() ?: false
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    fun cancelDiscovery() {
        bluetoothAdapter?.cancelDiscovery()
    }

    @RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN])
    suspend fun connectToDevice(device: BluetoothDevice): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            // UUID для SPP (Serial Port Profile)
            //val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
            val uuid = UUID.fromString("4FAFC201-1FB5-459E-8FCC-C5C9C331914B")
            val socket = device.createRfcommSocketToServiceRecord(uuid)

            bluetoothAdapter?.cancelDiscovery()
            socket.connect()

            bluetoothSocket = socket
            currentDevice = device

            Result.success(Unit)
        } catch (e: IOException) {
            Result.failure(e)
        }
    }

    suspend fun connectToAnyHeadphones(device: BluetoothDevice): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                // 1. Попробовать стандартные UUID
                val uuids = listOf(
                    UUID.fromString("00001108-0000-1000-8000-00805F9B34FB"), // A2DP
                    UUID.fromString("0000111E-0000-1000-8000-00805F9B34FB"), // HSP
                    UUID.fromString("0000110B-0000-1000-8000-00805F9B34FB")  // AVRCP
                )

                for (uuid in uuids) {
                    try {
                        val socket = device.createRfcommSocketToServiceRecord(uuid)
                        withTimeout(5000) {
                            socket.connect()
                        }
                        return@withContext Result.success(Unit)
                    } catch (e: Exception) {
                        continue
                    }
                }

                // 2. Попробовать альтернативный метод
                try {
                    val method = device.javaClass.getMethod("createRfcommSocket", Int::class.javaPrimitiveType)
                    val socket = method.invoke(device, 1) as BluetoothSocket
                    withTimeout(5000) {
                        socket.connect()
                    }
                    return@withContext Result.success(Unit)
                } catch (e: Exception) {
                    // Продолжаем
                }

                // 3. Если всё провалилось
                Result.failure(IOException("Не удалось подключиться ни по одному из протоколов"))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun sendData(data: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val outputStream = bluetoothSocket?.outputStream
            outputStream?.write(data.toByteArray())
            Result.success(Unit)
        } catch (e: IOException) {
            Result.failure(e)
        }
    }

    suspend fun receiveData(): Result<String> = withContext(Dispatchers.IO) {
        try {
            val inputStream = bluetoothSocket?.inputStream
            val buffer = ByteArray(1024)
            val bytes = inputStream?.read(buffer)

            if (bytes != null && bytes > 0) {
                val receivedData = String(buffer, 0, bytes)
                Result.success(receivedData)
            } else {
                Result.failure(IOException("No data received"))
            }
        } catch (e: IOException) {
            Result.failure(e)
        }
    }

    fun disconnect() {
        try {
            bluetoothSocket?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object {
        const val REQUEST_ENABLE_BT = 1
    }
}