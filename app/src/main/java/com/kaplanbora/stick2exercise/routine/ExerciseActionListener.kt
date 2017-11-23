package com.kaplanbora.stick2exercise.routine

import com.kaplanbora.stick2exercise.repository.Exercise

interface ExerciseActionListener {
    fun createExercise(exercise: Exercise)
}