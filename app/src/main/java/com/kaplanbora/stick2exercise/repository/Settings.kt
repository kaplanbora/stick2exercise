package com.kaplanbora.stick2exercise.repository

data class Settings(
        var metronomeSound: String = "Beep",
        var autoSwitch: Boolean = true,
        var countInSwitch: Boolean = false,
        var minutePicker: Int = 1,
        var secondPicker: Int = 0
)