package com.kaplanbora.stick2exercise.repository

import android.content.ContentValues
import android.database.Cursor
import android.util.Log

object ExerciseDatabase {
    private val TABLE = "exercise"
    private val TABLE_COLUMNS = arrayOf(
            "id",
            "routine_id",
            "position",
            "name",
            "tempo",
            "subdiv_up",
            "subdiv_down",
            "play_minutes",
            "play_seconds",
            "break_minutes",
            "break_seconds"
    )

    private fun toContentValues(exercise: Exercise, routineId: Long): ContentValues {
        val content = ContentValues()
        content.put("routine_id", routineId)
        content.put("position", exercise.position)
        content.put("name", exercise.name)
        content.put("tempo", exercise.metronome.tempo)
        content.put("subdiv_up", exercise.metronome.subdivUp)
        content.put("subdiv_down", exercise.metronome.subdivDown)
        content.put("play_minutes", exercise.playDuration.minutes)
        content.put("play_seconds", exercise.playDuration.seconds)
        content.put("break_minutes", exercise.breakDuration.minutes)
        content.put("break_seconds", exercise.breakDuration.seconds)
        return content
    }

    private fun toExercise(cursor: Cursor): Exercise = Exercise(
            cursor.getLong(cursor.getColumnIndex("id")),
            cursor.getInt(cursor.getColumnIndex("position")),
            cursor.getString(cursor.getColumnIndex("name")),
            Metronome(
                    cursor.getInt(cursor.getColumnIndex("tempo")),
                    cursor.getInt(cursor.getColumnIndex("subdiv_up")),
                    cursor.getInt(cursor.getColumnIndex("subdiv_down"))
            ),
            PlayDuration(
                    cursor.getInt(cursor.getColumnIndex("play_minutes")),
                    cursor.getInt(cursor.getColumnIndex("play_seconds"))
            ),
            BreakDuration(
                    cursor.getInt(cursor.getColumnIndex("break_minutes")),
                    cursor.getInt(cursor.getColumnIndex("break_seconds"))
            )
    )

    fun insert(helper: DbHelper, exercise: Exercise, routineId: Long): Long {
        val db = helper.writableDatabase
        val id = db.insert(TABLE, null, toContentValues(exercise, routineId))
        Log.d("DB_INSERT_OPERATION", "Exercise: $exercise \t Returned Id: $id")
        return id
    }

    fun selectAll(helper: DbHelper, routine: Routine) {
        val db = helper.readableDatabase
        val cursor = db.query(TABLE, TABLE_COLUMNS, "routine_id = ?", arrayOf("${routine.id}"), null, null, null)
        while (cursor.moveToNext()) {
            routine.exercises.add(toExercise(cursor))
        }
        cursor.close()
    }

    fun delete(helper: DbHelper, id: Long) {
        val db = helper.writableDatabase
        val count = db.delete(TABLE, "id = ?", arrayOf("$id"))
        Log.d("DB_DELETE_OPERATION", "Exercise Id: $id \t Affected Rows: $count")
    }

    fun update(helper: DbHelper, exercise: Exercise, routineId: Long) {
        val db = helper.writableDatabase
        val count = db.update(TABLE, toContentValues(exercise, routineId), "id = ?", arrayOf("${exercise.id}"))
        Log.d("DB_UPDATE_OPERATION", "Exercise Id: ${exercise.id} \t Affected Rows: $count")
    }
}