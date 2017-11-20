package com.kaplanbora.stick2exercise.routine

import com.kaplanbora.stick2exercise.repository.Routine

interface RoutineActionListener {
    fun editRoutine(routine: Routine)
    fun deleteRoutine(routine: Routine)
    fun createRoutine(name: String)
    fun applyEdit(name: String, id: Long)
    fun refreshListView()
}