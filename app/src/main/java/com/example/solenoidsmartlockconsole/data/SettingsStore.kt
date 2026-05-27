package com.example.solenoidsmartlockconsole.data

class SettingsStore {
    private var cachedLockId: String = DEFAULT_LOCK_ID

    fun defaultLockId(): String = cachedLockId

    fun rememberLockId(lockId: String) {
        val cleanLockId = lockId.trim()
        if (cleanLockId.isNotEmpty()) {
            cachedLockId = cleanLockId
        }
    }

    companion object {
        const val DEFAULT_LOCK_ID = "lock-001"
    }
}
