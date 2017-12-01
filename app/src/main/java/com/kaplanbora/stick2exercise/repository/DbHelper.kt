package com.kaplanbora.stick2exercise.repository

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(val context: Context) : SQLiteOpenHelper(context, "Stick2Exercise", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val userTable = """
            CREATE TABLE account(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                email TEXT NOT NULL,
                password TEXT NOT NULL);""".trimIndent()

        val tempRoutine = """
            CREATE TABLE temp_routine(
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            routine_id INTEGER NOT NULL,
                FOREIGN KEY (routine_id) REFERENCES routine(id)
                    ON DELETE CASCADE);""".trimIndent()

        val tempExercise = """
            CREATE TABLE temp_exercise(
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            exercise_id INTEGER NOT NULL,
                FOREIGN KEY (exercise_id) REFERENCES exercise(id)
                    ON DELETE CASCADE);""".trimIndent()

        val routineTable = """
            CREATE TABLE routine(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                position INTEGER NOT NULL,
                name TEXT,
                    FOREIGN KEY (user_id) REFERENCES account(id)
                        ON DELETE CASCADE);""".trimIndent()

        val exerciseTable = """
            CREATE TABLE exercise(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                routine_id INTEGER NOT NULL,
                position INTEGER NOT NULL,
                name TEXT,
                tempo INTEGER NOT NULL,
                subdiv_up INTEGER NOT NULL,
                subdiv_down INTEGER NOT NULL,
                play_minutes INTEGER NOT NULL,
                play_seconds INTEGER NOT NULL,
                break_minutes INTEGER NOT NULL,
                break_seconds INTEGER NOT NULL,
                    FOREIGN KEY (routine_id) REFERENCES routine(id)
                        ON DELETE CASCADE);""".trimIndent()

        // TODO: Add example routine and exercises.
        db?.execSQL(userTable)
        db?.execSQL(routineTable)
        db?.execSQL(exerciseTable)
        db?.execSQL(tempExercise)
        db?.execSQL(tempRoutine)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}