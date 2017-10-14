package com.kaplanbora.stick2exercise

data class Exercise(val id: Long, var name: String, var tempo: Int, var minutes: Int, var seconds: Int)

data class Routine(val id: Long, var name: String, val exercises: MutableList<Exercise>)

class Data {
    companion object {
        var routines: MutableList<Routine> = mutableListOf(
                Routine(0, "Example Routine", mutableListOf(Exercise(0, "Example Exercise", 120, 5, 30))),
                Routine(1, "Routwo", mutableListOf(Exercise(1, "Routwo Ex1", 120, 2, 0), Exercise(2, "Routwo Ex2", 80, 3, 20))))

        fun getRoutine(id: Long): Routine {
            return routines.first { it.id == id }
        }
    }
}

