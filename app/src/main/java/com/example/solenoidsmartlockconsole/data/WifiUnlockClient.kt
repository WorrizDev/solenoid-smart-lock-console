package com.example.solenoidsmartlockconsole.data

import com.example.solenoidsmartlockconsole.model.UnlockRequest
import com.example.solenoidsmartlockconsole.model.UnlockResponse
import kotlinx.coroutines.delay

class WifiUnlockClient(
    private val baseUrl: String = DEFAULT_BASE_URL
) {
    suspend fun sendUnlockCode(lockId: String, code: String): UnlockResponse {
        val request = UnlockRequest(lockId = lockId, code = code)
        val endpoint = "$baseUrl/unlock"

        // TODO: Replace this fake delay with an HTTP POST to endpoint using request as JSON.
        delay(700)

        return UnlockResponse(
            success = true,
            message = "Fake Wi-Fi unlock accepted by $endpoint for ${request.lockId}.",
            code = request.code
        )
    }

    suspend fun sendWifiCredentials(ssid: String, password: String): UnlockResponse {
        val endpoint = "$baseUrl/setup-wifi"
        val payloadPreview = ssid.trim()
        val passwordPlaceholder = if (password.isBlank()) "without password" else "with password"

        // TODO: Replace this fake delay with POST /setup-wifi body {"ssid": ssid, "password": password}.
        delay(700)

        return UnlockResponse(
            success = true,
            message = "Fake Wi-Fi setup sent to $endpoint for SSID \"$payloadPreview\" $passwordPlaceholder."
        )
    }

    companion object {
        const val DEFAULT_BASE_URL = "http://192.168.4.1"
    }
}
