# Solenoid Smart Lock Console

Native Android prototype for controlling an ESP32-based solenoid lock over Wi-Fi and Bluetooth. The first version focuses on a Compose console UI, MVVM state handling, and placeholder-ready communication classes.

## Features

- Jetpack Compose and Material 3 lock console UI.
- Editable Lock ID field with default value `lock-001`.
- Generate and send a 6-digit temporary unlock code.
- Bluetooth unlock flow with runtime permission placeholders.
- ESP32 Wi-Fi setup screen for SSID and password entry.
- Placeholder clients for future ESP32 HTTP and BLE communication.

## Tech Stack

- Kotlin
- Android Gradle Plugin
- Gradle wrapper
- Jetpack Compose
- Material 3
- AndroidX Lifecycle ViewModel

## Setup Instructions

1. Install Android Studio or an Android SDK with API 35.
2. Open this folder in Android Studio, or build from the project root.
3. Let Android Studio create `local.properties` with your local SDK path. This file is intentionally ignored by Git.
4. Build the debug APK:

```powershell
.\gradlew.bat assembleDebug
```

On Linux or macOS:

```bash
./gradlew assembleDebug
```

## Project Structure

- `MainActivity.kt` wires Compose navigation and the shared `SmartLockViewModel`.
- `ui/SmartLockConsoleScreen.kt` contains the main lock console.
- `ui/WifiSetupScreen.kt` collects ESP32 network credentials.
- `ui/BluetoothScreen.kt` prepares the future BLE scanner surface.
- `viewmodel/SmartLockViewModel.kt` owns lock ID, session state, setup status, and user actions.
- `model/LockSessionState.kt`, `UnlockRequest.kt`, and `UnlockResponse.kt` define app state and request/response models.
- `data/LockRepository.kt`, `WifiUnlockClient.kt`, `BluetoothUnlockClient.kt`, and `SettingsStore.kt` isolate communication and settings placeholders.

## ESP32 Communication Notes

The Wi-Fi client is prepared for this ESP32 base URL:

```text
http://192.168.4.1
```

Unlock request:

```http
POST /unlock
```

```json
{
  "lockId": "lock-001",
  "code": "123456"
}
```

Wi-Fi setup request:

```http
POST /setup-wifi
```

```json
{
  "ssid": "...",
  "password": "..."
}
```

The current implementation intentionally returns fake responses so the app builds before real networking and BLE dependencies are added.

## Limitations

- App only sends commands; ESP32 hardware performs the actual unlock.
- Wi-Fi unlock works only when ESP32 is reachable through a local network or setup access point.
- Bluetooth range is limited.
- Prototype commands are not production-secure.
- The solenoid requires external power and a relay or MOSFET driver.
- Runtime permissions are required for Bluetooth and Wi-Fi related operations.
- Authentication, encryption, replay protection, and hardware testing are not complete.

## Disclaimer

This is a prototype smart lock controller for experimentation and development only. Do not rely on it as a production security device.
