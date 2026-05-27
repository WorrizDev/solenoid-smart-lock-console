package com.example.solenoidsmartlockconsole

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.example.solenoidsmartlockconsole.ui.BluetoothScreen
import com.example.solenoidsmartlockconsole.ui.SmartLockConsoleScreen
import com.example.solenoidsmartlockconsole.ui.WifiSetupScreen
import com.example.solenoidsmartlockconsole.viewmodel.SmartLockViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: SmartLockViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SolenoidSmartLockApp(viewModel = viewModel)
        }
    }
}

@Composable
private fun SolenoidSmartLockApp(viewModel: SmartLockViewModel) {
    var currentScreen by rememberSaveable { mutableStateOf(AppScreen.Console.name) }
    val lockId by viewModel.lockId.collectAsState()
    val sessionState by viewModel.sessionState.collectAsState()
    val wifiSetupStatus by viewModel.wifiSetupStatus.collectAsState()
    val bluetoothDevices by viewModel.bluetoothDevices.collectAsState()

    SolenoidTheme {
        when (AppScreen.valueOf(currentScreen)) {
            AppScreen.Console -> {
                SmartLockConsoleScreen(
                    lockId = lockId,
                    sessionState = sessionState,
                    onLockIdChange = viewModel::onLockIdChange,
                    onGenerateWifiCode = viewModel::generateAndSendWifiCode,
                    onBluetoothUnlock = viewModel::unlockWithBluetooth,
                    onOpenWifiSetup = { currentScreen = AppScreen.WifiSetup.name }
                )
            }

            AppScreen.WifiSetup -> {
                WifiSetupScreen(
                    statusMessage = wifiSetupStatus,
                    onBack = { currentScreen = AppScreen.Console.name },
                    onSendCredentials = viewModel::sendWifiCredentials
                )
            }

            AppScreen.Bluetooth -> {
                BluetoothScreen(
                    lockId = lockId,
                    sessionState = sessionState,
                    devices = bluetoothDevices,
                    onBack = { currentScreen = AppScreen.Console.name },
                    onStartScan = viewModel::scanForBluetoothDevices
                )
            }
        }
    }
}

private enum class AppScreen {
    Console,
    WifiSetup,
    Bluetooth
}

@Composable
private fun SolenoidTheme(content: @Composable () -> Unit) {
    val colors = lightColorScheme(
        primary = Color(0xFF0C7071),
        onPrimary = Color.White,
        secondary = Color(0xFF2E6E7E),
        background = Color(0xFFF4F6F0),
        surface = Color(0xFFEAF2F2),
        onSurface = Color(0xFF061418)
    )

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}
