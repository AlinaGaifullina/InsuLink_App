package ru.itis.domain.bluetooth

import org.json.JSONObject
import java.io.IOException
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

// Data Models
sealed class PumpEvent {
    data class InsulinDelivery(
        val units: Double,
        val timestamp: String
    ) : PumpEvent()

    data class EmergencyStop(
        val timestamp: String
    ) : PumpEvent()

    data class ErrorEvent(
        val errorCode: String,
        val timestamp: String? = null
    ) : PumpEvent()

    data class ReservoirChange(
        val isCompleted: Boolean
    ) : PumpEvent()
}

data class PumpEncryptedData(
    val iv: String,
    val payload: String,
    val tag: String
)

// Crypto Manager (оптимизированная версия)
object PumpCryptoManager {
    private const val AES_MODE = "AES/GCM/NoPadding"
    private const val TAG_LENGTH_BITS = 128

    @OptIn(ExperimentalEncodingApi::class)
    fun decryptPumpData(
        ivBase64: String,
        payloadBase64: String,
        tagBase64: String,
        key: ByteArray
    ): PumpEvent {
        try {
            val iv = Base64.decode(ivBase64)
            val encrypted = Base64.decode(payloadBase64)
            val tag = Base64.decode(tagBase64)

            val cipher = Cipher.getInstance(AES_MODE)
            val secretKey = SecretKeySpec(key, "AES")
            val gcmSpec = GCMParameterSpec(TAG_LENGTH_BITS, iv)

            cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmSpec)
            val decrypted = cipher.doFinal(encrypted + tag)
            val json = String(decrypted, Charsets.UTF_8)

            return parsePumpEvent(json)
        } catch (e: Exception) {
            throw IOException("Decryption failed: ${e.message}")
        }
    }

    private fun parsePumpEvent(json: String): PumpEvent {
        val type = JSONObject(json).getString("type")

        return when (type) {
            "manual_insulin_delivery" -> {
                val units = JSONObject(json).getDouble("units")
                val timestamp = JSONObject(json).getString("timestamp")
                PumpEvent.InsulinDelivery(units, timestamp)
            }
            "emergency_pump_stop" -> {
                PumpEvent.EmergencyStop(JSONObject(json).getString("timestamp"))
            }
            "error" -> {
                PumpEvent.ErrorEvent(JSONObject(json).getString("error_code"))
            }
            "reservoir_change_started" -> PumpEvent.ReservoirChange(false)
            "reservoir_change_completed" -> PumpEvent.ReservoirChange(true)
            else -> throw IllegalArgumentException("Unknown event type: $type")
        }
    }
}