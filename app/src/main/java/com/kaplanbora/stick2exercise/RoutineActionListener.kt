package com.kaplanbora.stick2exercise

interface RoutineActionListener {
    fun editRoutine(routine: Routine)
    fun deleteRoutine(routine: Routine)
    fun refreshRoutines()
}