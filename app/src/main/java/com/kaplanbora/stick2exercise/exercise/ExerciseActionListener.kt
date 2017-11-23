package com.kaplanbora.stick2exercise.exercise

import com.kaplanbora.stick2exercise.repository.Exercise

interface ExerciseActionListener {
    fun editExercise(exercise: Exercise)
    fun deleteExercise(exercise: Exercise)
    fun refreshListView()
}