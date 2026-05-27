package com.example.solenoidsmartlockconsole.data

import com.example.solenoidsmartlockconsole.model.UnlockResponse
import kotlinx.coroutines.delay

class BluetoothUnlockClient {
    fun hasRequiredPermissions(): Boolean {
        // TODO: Check BLUETOOTH_SCAN, BLUETOOTH_CONNECT, and location requirements by Android version.
        return true
    }

    suspend fun unlock(lockId: String): UnlockResponse {
        // TODO: Replace this fake delay with BLE connection, service discovery, and command write.
        delay(600)

        return UnlockResponse(
            success = true,
            message = "Fake Bluetooth unlock sent to $lockId."
        )
    }

    suspend fun scanForDevices(): List<String> {
        // TODO: Replace with BLE scan callbacks and permission-gated device discovery.
        delay(600)
        return listOf("ESP32-Solenoid-Lock (placeholder)")
    }
}
