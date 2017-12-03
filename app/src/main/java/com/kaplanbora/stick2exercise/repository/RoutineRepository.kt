package com.kaplanbora.stick2exercise.repository

object RoutineRepository {
    // Public to use easily with firebase. Change back to private when done.
    var routines: MutableList<Routine> = mutableListOf()
    var userId = -1L

    fun get(id: Long): Routine = routines.first { it.id == id }

    fun all(): MutableList<Routine> {
        routines.sortBy { it.position }
        return routines
    }

    fun nextPosition(): Int = routines.size + 1

    fun add(dbHelper: DbHelper, routine: Routine): Long {
        val id = RoutineDatabase.insert(dbHelper, routine)
        routine.id = id
        routines.add(routine)
        return id
    }

    fun restore(dbHelper: DbHelper, routine: Routine) {
        val shiftedRoutines = routines.filter { it.position >= routine.position }
        add(dbHelper, routine)
        shiftedRoutines.forEach { it.position += 1 }
        shiftedRoutines.forEach { RoutineDatabase.update(dbHelper, it) }
    }

    fun load(dbHelper: DbHelper) {
        routines = RoutineDatabase.selectAll(dbHelper, userId)
        routines.forEach { ExerciseDatabase.selectAll(dbHelper, it) }
    }

    fun remove(dbHelper: DbHelper, routine: Routine) {
        RoutineDatabase.delete(dbHelper, routine.id)
        routines.remove(routine)
        val shiftedRoutines = routines.filter { it.position > routine.position }
        shiftedRoutines.forEach { it.position -= 1 }
        shiftedRoutines.forEach { RoutineDatabase.update(dbHelper, it) }
    }

    fun update(dbHelper: DbHelper, routine: Routine) {
        RoutineDatabase.update(dbHelper, routine)
    }
}
