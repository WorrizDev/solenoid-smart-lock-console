package com.example.solenoidsmartlockconsole.model

sealed interface LockSessionState {
    val message: String

    data object Idle : LockSessionState {
        override val message = "Idle"
    }

    data object GeneratingCode : LockSessionState {
        override val message = "Generating code..."
    }

    data object SendingWifiUnlock : LockSessionState {
        override val message = "Sending Wi-Fi unlock..."
    }

    data object CheckingBluetoothPermissions : LockSessionState {
        override val message = "Checking Bluetooth permissions..."
    }

    data object SendingBluetoothUnlock : LockSessionState {
        override val message = "Sending Bluetooth unlock..."
    }

    data object SendingWifiCredentials : LockSessionState {
        override val message = "Sending Wi-Fi credentials..."
    }

    data object ScanningBluetooth : LockSessionState {
        override val message = "Scanning for Bluetooth devices..."
    }

    data class Success(override val message: String) : LockSessionState

    data class Error(override val message: String) : LockSessionState
}
