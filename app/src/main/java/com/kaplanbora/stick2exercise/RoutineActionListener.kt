package com.kaplanbora.stick2exercise

import com.kaplanbora.stick2exercise.repository.Routine

interface RoutineActionListener {
    fun editRoutine(routine: Routine)
    fun deleteRoutine(routine: Routine)
    fun refreshRoutines()
}