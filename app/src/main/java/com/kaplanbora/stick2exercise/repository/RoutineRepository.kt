package com.kaplanbora.stick2exercise.repository

import android.content.ContentValues

object RoutineRepository {
    var routineList: MutableList<Routine> = mutableListOf()

    fun get(id: Long): Routine = routineList.first { it.id == id }

    fun add(routine: Routine) {
        routineList.add(routine)
    }

    fun insert(helper: DbHelper, routine: Routine) {
        val db = helper.writableDatabase
        val content = ContentValues()
        content.put("order", routine.order)
        content.put("name", routine.name)
        val id = db.insert("routine", null, content)
        routine.id = id
    }

    fun selectAll(helper: DbHelper): MutableList<Routine> {
        val db = helper.readableDatabase
        val cursor = db.query("routine", arrayOf("id", "order", "name"), null, null, null, null, null)
        val routines: MutableList<Routine> = mutableListOf()
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndex("id"))
            val order = cursor.getInt(cursor.getColumnIndex("order"))
            val name = cursor.getString(cursor.getColumnIndex("name"))
            routines.add(Routine(id, order, name))
        }
        cursor.close()
        return routines
    }

    fun loadRoutines(helper: DbHelper) {
        routineList = selectAll(helper)
    }
}
