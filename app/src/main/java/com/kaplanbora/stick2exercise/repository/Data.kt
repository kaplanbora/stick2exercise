package com.kaplanbora.stick2exercise.repository

object ExerciseRepo {
    private var exerciseId: Long = 1

    fun generateId(): Long {
        val id = exerciseId
        exerciseId += 1
        return id
    }
}

object RoutineRepo {
    private var routineId: Long = 1
    private val routines: MutableList<Routine> = mutableListOf()

    fun generateId(): Long {
        val id = routineId
        routineId += 1
        return id
    }

    fun delete(routine: Routine) {
        routines.remove(routine)
    }

    fun addRoutine(routine: Routine) {
        routines.add(routine)
    }

    fun populate() {
        routines.add(
                Routine(generateId(), 1, "Example Routine", mutableListOf(
                        Exercise(ExerciseRepo.generateId(), 1, "Example Exercise", Metronome(120, 4, 4), PlayDuration(5, 0), BreakDuration(1, 0)))
                )
        )
    }

    fun get(id: Long): Routine = routines.first { it.id == id }

    fun getList(): MutableList<Routine> {
        routines.sortBy { it.order }
        return routines
    }
}

