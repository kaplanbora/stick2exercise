package com.kaplanbora.stick2exercise

data class Exercise(val id: Long, var name: String, var tempo: Int, var duration: Int)

data class Routine(val id: Long, var name: String, val exercises: MutableList<Exercise>)

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

