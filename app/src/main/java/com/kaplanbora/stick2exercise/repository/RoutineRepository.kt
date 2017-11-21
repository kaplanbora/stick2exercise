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

    fun addRestore(dbHelper: DbHelper, routine: Routine) {
        routineList.filter { it.order >= routine.order }.forEach { it.order += 1 }
        add(dbHelper, routine)
    }

    fun loadRoutines(helper: DbHelper) {
        routineList = RoutineDatabase.selectAll(helper)
    }

    fun remove(dbHelper: DbHelper, routine: Routine) {
        RoutineDatabase.delete(dbHelper, routine.id)
        routineList.remove(routine)
        routineList.filter { it.order > routine.order }.forEach { it.order -= 1 }
        routineList.forEach { RoutineDatabase.update(dbHelper, it) }
    }
}
