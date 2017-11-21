package com.kaplanbora.stick2exercise.repository

import android.content.ContentValues
import android.util.Log

object RoutineDatabase {
    private val TABLE = "routine"
    private val TABLE_COLUMNS = arrayOf("id", "position", "name")

    fun insert(helper: DbHelper, routine: Routine): Long {
        val db = helper.writableDatabase
        val content = ContentValues()
        content.put("position", routine.position)
        content.put("name", routine.name)
        val id = db.insert(TABLE, null, content)
        Log.d("DB_INSERT_OPERATION", "Routine: $routine \t Returned Id: $id")
        return id
    }

    fun selectAll(helper: DbHelper): MutableList<Routine> {
        val db = helper.readableDatabase
        val cursor = db.query(TABLE, TABLE_COLUMNS, null, null, null, null, null)
        val routines: MutableList<Routine> = mutableListOf()
        while (cursor.moveToNext()) {
            var id = cursor.getLong(cursor.getColumnIndex("id"))
            var order = cursor.getInt(cursor.getColumnIndex("position"))
            var name = cursor.getString(cursor.getColumnIndex("name"))
            routines.add(Routine(id, order, name))
        }
        cursor.close()
        return routines
    }

    fun delete(helper: DbHelper, id: Long) {
        val db = helper.writableDatabase
        val count = db.delete(TABLE, "id = ?", arrayOf("$id"))
        Log.d("DB_DELETE_OPERATION", "Routine Id: $id \t Affected Rows: $count")
    }

    fun update(helper: DbHelper, routine: Routine) {
        val db = helper.writableDatabase
        val content = ContentValues()
        content.put("position", routine.position)
        content.put("name", routine.name)
        val count = db.update(TABLE, content, "id = ?", arrayOf("${routine.id}"))
        Log.d("DB_UPDATE_OPERATION", "Routine Id: ${routine.id} \t Affected Rows: $count")
    }
}