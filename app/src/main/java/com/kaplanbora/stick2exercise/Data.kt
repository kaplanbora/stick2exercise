package com.kaplanbora.stick2exercise

import com.kaplanbora.stick2exercise.ExerciseRepo.generateExerciseId

data class Exercise(
        val id: Long,
        var name: String,
        var tempo: Int,
        var subdivUp: Int,
        var subdivDown: Int,
        var playMin: Int,
        var playSec: Int,
        var breakMin: Int,
        var breakSec: Int)

data class Routine(val id: Long, var name: String, val exercises: MutableList<Exercise> = mutableListOf())

object ExerciseRepo {
    private var exerciseId: Long = 1
    fun generateExerciseId(): Long {
        val id = exerciseId
        exerciseId += 1
        return id
    }
}

object RoutineRepo {
    private var routineId: Long = 1
    private val routines: MutableList<Routine> = mutableListOf()

    fun generateRoutineId(): Long {
        val id = routineId
        routineId += 1
        return id
    }

    fun addRoutine(routine: Routine) {
        routines.add(routine)
    }

    fun populate() {
        routines.add(
                Routine(generateRoutineId(), "Example Routine", mutableListOf(
                        Exercise(generateExerciseId(), "Example Exercise 1", 120, 4, 4, 5, 0, 0, 0))
                )
        )
    }

    fun get(id: Long): Routine {
        return routines.first { it.id == id }
    }

    fun getList(): MutableList<Routine> {
        return routines
    }

}

