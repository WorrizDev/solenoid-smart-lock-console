package com.example.solenoidsmartlockconsole.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.solenoidsmartlockconsole.data.LockRepository
import com.example.solenoidsmartlockconsole.model.LockSessionState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class SmartLockViewModel(
    private val repository: LockRepository = LockRepository()
) : ViewModel() {
    private val _lockId = MutableStateFlow(repository.defaultLockId())
    val lockId: StateFlow<String> = _lockId.asStateFlow()

    private val _sessionState = MutableStateFlow<LockSessionState>(LockSessionState.Idle)
    val sessionState: StateFlow<LockSessionState> = _sessionState.asStateFlow()

    private val _wifiSetupStatus = MutableStateFlow("Waiting for ESP32 Wi-Fi credentials.")
    val wifiSetupStatus: StateFlow<String> = _wifiSetupStatus.asStateFlow()

    private val _bluetoothDevices = MutableStateFlow<List<String>>(emptyList())
    val bluetoothDevices: StateFlow<List<String>> = _bluetoothDevices.asStateFlow()

    fun onLockIdChange(value: String) {
        _lockId.value = value
        repository.rememberLockId(value)
    }

    fun generateAndSendWifiCode() {
        viewModelScope.launch {
            val targetLockId = normalizedLockIdOrError() ?: return@launch

            _sessionState.value = LockSessionState.GeneratingCode
            delay(250)

            val code = generateTemporaryCode()
            _sessionState.value = LockSessionState.SendingWifiUnlock

            val response = runCatching {
                repository.sendWifiUnlock(lockId = targetLockId, code = code)
            }.getOrElse { error ->
                _sessionState.value = LockSessionState.Error("Wi-Fi unlock failed: ${error.message.orEmpty()}")
                return@launch
            }

            _sessionState.value = if (response.success) {
                LockSessionState.Success("${response.message} Code: ${response.code.orEmpty()}")
            } else {
                LockSessionState.Error(response.message)
            }
        }
    }

    fun unlockWithBluetooth() {
        viewModelScope.launch {
            val targetLockId = normalizedLockIdOrError() ?: return@launch

            _sessionState.value = LockSessionState.CheckingBluetoothPermissions
            delay(200)

            if (!repository.hasBluetoothPermissions()) {
                _sessionState.value = LockSessionState.Error("Bluetooth permissions are required before unlock.")
                return@launch
            }

            _sessionState.value = LockSessionState.SendingBluetoothUnlock
            val response = runCatching {
                repository.sendBluetoothUnlock(lockId = targetLockId)
            }.getOrElse { error ->
                _sessionState.value = LockSessionState.Error("Bluetooth unlock failed: ${error.message.orEmpty()}")
                return@launch
            }

            _sessionState.value = if (response.success) {
                LockSessionState.Success(response.message)
            } else {
                LockSessionState.Error(response.message)
            }
        }
    }

    fun sendWifiCredentials(ssid: String, password: String) {
        viewModelScope.launch {
            val cleanSsid = ssid.trim()
            if (cleanSsid.isEmpty()) {
                _wifiSetupStatus.value = "SSID is required."
                _sessionState.value = LockSessionState.Error("SSID is required.")
                return@launch
            }

            _wifiSetupStatus.value = LockSessionState.SendingWifiCredentials.message
            _sessionState.value = LockSessionState.SendingWifiCredentials

            val response = runCatching {
                repository.sendWifiCredentials(ssid = cleanSsid, password = password)
            }.getOrElse { error ->
                val message = "Wi-Fi setup failed: ${error.message.orEmpty()}"
                _wifiSetupStatus.value = message
                _sessionState.value = LockSessionState.Error(message)
                return@launch
            }

            _wifiSetupStatus.value = response.message
            _sessionState.value = if (response.success) {
                LockSessionState.Success(response.message)
            } else {
                LockSessionState.Error(response.message)
            }
        }
    }

    fun scanForBluetoothDevices() {
        viewModelScope.launch {
            _sessionState.value = LockSessionState.ScanningBluetooth
            val devices = runCatching {
                repository.scanBluetoothDevices()
            }.getOrDefault(emptyList())

            _bluetoothDevices.value = devices
            _sessionState.value = if (devices.isEmpty()) {
                LockSessionState.Error("No Bluetooth devices found yet.")
            } else {
                LockSessionState.Success("Found ${devices.size} Bluetooth placeholder device.")
            }
        }
    }

    private fun normalizedLockIdOrError(): String? {
        val targetLockId = lockId.value.trim()
        if (targetLockId.isEmpty()) {
            _sessionState.value = LockSessionState.Error("Lock ID is required.")
            return null
        }
        return targetLockId
    }

    private fun generateTemporaryCode(): String {
        return Random.nextInt(from = 100000, until = 1000000).toString()
    }
}
