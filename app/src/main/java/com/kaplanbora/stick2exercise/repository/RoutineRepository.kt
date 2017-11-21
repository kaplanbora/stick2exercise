package com.kaplanbora.stick2exercise.repository

object RoutineRepository {
    var routineList: MutableList<Routine> = mutableListOf()

    fun get(id: Long): Routine = routineList.first { it.id == id }

    fun getList(): MutableList<Routine> {
        routineList.sortBy { it.order }
        return routineList
    }

    fun add(dbHelper: DbHelper, routine: Routine): Long {
        val id = RoutineDatabase.insert(dbHelper, routine)
        routine.id = id
        routineList.add(routine)
        return id
    }

    fun loadRoutines(helper: DbHelper) {
        routineList = RoutineDatabase.selectAll(helper)
    }
}
