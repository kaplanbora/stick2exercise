package com.kaplanbora.stick2exercise.repository

data class Settings(
        var id: Long = 0,
        var metronomeSound: String = "Beep",
        var screenOn: Boolean = false,
        var autoSwitch: Boolean = true,
        var countInSwitch: Boolean = false,
        var defaultMinute: Int = 1,
        var defaultSecond: Int = 0
)

data class Exercise(
        var id: Long,
        var position: Int,
        var name: String,
        val metronome: Metronome,
        val playDuration: PlayDuration,
        val breakDuration: BreakDuration)

data class Routine(
        var id: Long,
        var position: Int,
        var name: String,
        val exercises: MutableList<Exercise> = mutableListOf())

data class Metronome(
        var tempo: Int,
        var subdivUp: Int,
        var subdivDown: Int)

data class PlayDuration(
        var minutes: Int,
        var seconds: Int)

data class BreakDuration(
        var minutes: Int,
        var seconds: Int)
