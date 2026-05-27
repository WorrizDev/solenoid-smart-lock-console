package com.example.solenoidsmartlockconsole.model

data class UnlockResponse(
    val success: Boolean,
    val message: String,
    val code: String? = null
)
