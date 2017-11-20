package com.kaplanbora.stick2exercise.repository

data class Exercise(
        val id: Long,
        var order: Int,
        var name: String,
        val metronome: Metronome,
        val playDuration: PlayDuration,
        val breakDuration: BreakDuration)

data class Routine(
        val id: Long,
        var order: Int,
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
