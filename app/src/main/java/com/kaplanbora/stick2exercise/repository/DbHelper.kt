package com.kaplanbora.stick2exercise.repository

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(val context: Context) : SQLiteOpenHelper(context, "Stick2Exercise", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val routineTable = """
            CREATE TABLE routine(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                position INTEGER NOT NULL,
                name TEXT);""".trimIndent()

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
        db?.execSQL(routineTable)
        db?.execSQL(exerciseTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}