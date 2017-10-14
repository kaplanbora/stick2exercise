package com.kaplanbora.stick2exercise

class Data {
    companion object {
        var routines: MutableList<Routine> = mutableListOf(
                Routine(0, "Example Routine", mutableListOf(Exercise(0, "Example Exercise", 120, 120))),
                Routine(1, "Routwo", mutableListOf(Exercise(1, "Routwo Ex1", 140, 300))))
        fun getRoutine(id: Long): Routine {
            return routines.first { it.id == id }
        }
    }
}