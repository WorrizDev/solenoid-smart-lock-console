package com.example.solenoidsmartlockconsole.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.solenoidsmartlockconsole.model.LockSessionState

private val ScreenBackground = Color(0xFFF4F6F0)
private val CardBackground = Color(0xFFEAF2F2)
private val PrimaryTeal = Color(0xFF0C7071)
private val DeepText = Color(0xFF061418)
private val MutedText = Color(0xFF526064)
private val BorderColor = Color(0xFF6F7F82)
private val SessionBlue = Color(0xFF2E6685)
private val SuccessGreen = Color(0xFF2E7D53)
private val ErrorRed = Color(0xFFB33A3A)
private val WorkingAmber = Color(0xFFB27700)

@Composable
fun SmartLockConsoleScreen(
    lockId: String,
    sessionState: LockSessionState,
    onLockIdChange: (String) -> Unit,
    onGenerateWifiCode: () -> Unit,
    onBluetoothUnlock: () -> Unit,
    onOpenWifiSetup: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ScreenBackground)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 28.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 620.dp)
        ) {
            Text(
                text = "SOLENOID",
                color = MutedText,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Smart Lock Console",
                color = DeepText,
                fontFamily = FontFamily.Serif,
                fontSize = 38.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 42.sp
            )

            Spacer(modifier = Modifier.height(28.dp))

            LockTargetCard(
                lockId = lockId,
                onLockIdChange = onLockIdChange,
                onGenerateWifiCode = onGenerateWifiCode,
                onBluetoothUnlock = onBluetoothUnlock,
                onOpenWifiSetup = onOpenWifiSetup
            )

            Spacer(modifier = Modifier.height(36.dp))

            SessionCard(sessionState = sessionState)
        }
    }
}

@Composable
private fun LockTargetCard(
    lockId: String,
    onLockIdChange: (String) -> Unit,
    onGenerateWifiCode: () -> Unit,
    onBluetoothUnlock: () -> Unit,
    onOpenWifiSetup: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 10.dp, shape = RoundedCornerShape(24.dp), clip = false),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 22.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Lock Target",
                color = DeepText,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = lockId,
                onValueChange = onLockIdChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp),
                label = { Text("Lock ID") },
                singleLine = true,
                textStyle = MaterialTheme.typography.titleLarge.copy(color = DeepText),
                shape = RoundedCornerShape(22.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BorderColor,
                    unfocusedBorderColor = BorderColor,
                    focusedLabelColor = MutedText,
                    unfocusedLabelColor = MutedText,
                    cursorColor = PrimaryTeal,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )

            Button(
                onClick = onGenerateWifiCode,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryTeal)
            ) {
                Text(
                    text = "Generate & Send Code (Wi-Fi)",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            SecondaryButton(
                text = "Bluetooth Unlock",
                textColor = WorkingAmber,
                onClick = onBluetoothUnlock
            )

            SecondaryButton(
                text = "Setup ESP32 WiFi",
                textColor = PrimaryTeal,
                onClick = onOpenWifiSetup
            )
        }
    }
}

@Composable
private fun SecondaryButton(
    text: String,
    textColor: Color,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(width = 1.dp, color = BorderColor),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = textColor)
    ) {
        Text(
            text = text,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun SessionCard(sessionState: LockSessionState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(6.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FBFA)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 28.dp, vertical = 24.dp)
        ) {
            Text(
                text = "Session",
                color = DeepText,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(22.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(11.dp)
                        .background(color = sessionState.statusColor(), shape = CircleShape)
                )

                Text(
                    text = sessionState.message,
                    color = Color(0xFF2B4551),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

private fun LockSessionState.statusColor(): Color {
    return when (this) {
        LockSessionState.Idle -> SessionBlue
        LockSessionState.GeneratingCode,
        LockSessionState.SendingWifiUnlock,
        LockSessionState.CheckingBluetoothPermissions,
        LockSessionState.SendingBluetoothUnlock,
        LockSessionState.SendingWifiCredentials,
        LockSessionState.ScanningBluetooth -> WorkingAmber
        is LockSessionState.Success -> SuccessGreen
        is LockSessionState.Error -> ErrorRed
    }
}
