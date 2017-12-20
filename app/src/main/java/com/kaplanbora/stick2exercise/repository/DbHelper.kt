package com.kaplanbora.stick2exercise.repository

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHelper(val context: Context) : SQLiteOpenHelper(context, "Stick2Exercise", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val defaultSettings = """
            INSERT INTO settings (id,metronome_sound,screen_on,auto_switch,countin_switch,default_minute,default_second)
            VALUES (1, 'Beep', 0, 1, 0, 1, 0);""".trimIndent()

        val settingsTable = """
            CREATE TABLE settings(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                metronome_sound TEXT NOT NULL,
                screen_on INTEGER NOT NULL,
                auto_switch INTEGER NOT NULL,
                countin_switch INTEGER NOT NULL,
                default_minute INTEGER NOT NULL,
                default_second INTEGER NOT NULL);""".trimIndent()

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
        db?.execSQL(settingsTable)
        db?.execSQL(routineTable)
        db?.execSQL(exerciseTable)
        db?.execSQL(defaultSettings)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}