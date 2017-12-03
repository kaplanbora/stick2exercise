package com.kaplanbora.stick2exercise.repository

data class Settings(
        var onlineMode: Boolean = false,
        var metronomeSound: String = "Beep",
        var screenOn: Boolean = false,
        var autoSwitch: Boolean = true,
        var countInSwitch: Boolean = false,
        var defaultMinute: Int = 1,
        var defaultSecond: Int = 0
)