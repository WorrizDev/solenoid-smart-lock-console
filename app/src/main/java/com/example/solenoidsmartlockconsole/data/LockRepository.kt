package com.example.solenoidsmartlockconsole.data

import com.example.solenoidsmartlockconsole.model.UnlockResponse

class LockRepository(
    private val wifiUnlockClient: WifiUnlockClient = WifiUnlockClient(),
    private val bluetoothUnlockClient: BluetoothUnlockClient = BluetoothUnlockClient(),
    private val settingsStore: SettingsStore = SettingsStore()
) {
    fun defaultLockId(): String = settingsStore.defaultLockId()

    fun rememberLockId(lockId: String) {
        settingsStore.rememberLockId(lockId)
    }

    suspend fun sendWifiUnlock(lockId: String, code: String): UnlockResponse {
        return wifiUnlockClient.sendUnlockCode(lockId = lockId, code = code)
    }

    suspend fun sendWifiCredentials(ssid: String, password: String): UnlockResponse {
        return wifiUnlockClient.sendWifiCredentials(ssid = ssid, password = password)
    }

    fun hasBluetoothPermissions(): Boolean = bluetoothUnlockClient.hasRequiredPermissions()

    suspend fun sendBluetoothUnlock(lockId: String): UnlockResponse {
        return bluetoothUnlockClient.unlock(lockId = lockId)
    }

    suspend fun scanBluetoothDevices(): List<String> {
        return bluetoothUnlockClient.scanForDevices()
    }
}
