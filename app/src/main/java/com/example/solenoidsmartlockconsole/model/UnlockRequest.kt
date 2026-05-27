package com.example.solenoidsmartlockconsole.model

data class UnlockRequest(
    val lockId: String,
    val code: String
)
