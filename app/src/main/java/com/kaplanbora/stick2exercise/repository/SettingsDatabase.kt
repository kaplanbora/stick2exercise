package com.kaplanbora.stick2exercise.repository

import android.content.ContentValues

object SettingsDatabase {
    private val TABLE = "settings"
    private val TABLE_COLUMNS = arrayOf(
            "id",
            "metronome_sound",
            "screen_on",
            "auto_switch",
            "countin_switch",
            "default_minute",
            "default_second"
    )

    private fun Boolean.toInt() = if (this) 1 else 0

    private fun Settings.toContentValue(): ContentValues {
        val content = ContentValues()
        content.put("metronome_sound", this.metronomeSound)
        content.put("screen_on", this.screenOn.toInt())
        content.put("auto_switch", this.autoSwitch.toInt())
        content.put("countin_switch", this.countInSwitch.toInt())
        content.put("default_minute", this.defaultMinute)
        content.put("default_second", this.defaultSecond)
        return content
    }

    fun get(helper: DbHelper): Settings {
        val db = helper.readableDatabase
        val cursor = db.query(TABLE, TABLE_COLUMNS, "id = ?", arrayOf("1"), null, null, null)
        val settings = Settings()
        while (cursor.moveToNext()) {
            settings.id = cursor.getLong(cursor.getColumnIndex("id"))
            settings.metronomeSound = cursor.getString(cursor.getColumnIndex("metronome_sound"))
            settings.screenOn = cursor.getInt(cursor.getColumnIndex("screen_on")) == 1
            settings.autoSwitch = cursor.getInt(cursor.getColumnIndex("auto_switch")) == 1
            settings.countInSwitch = cursor.getInt(cursor.getColumnIndex("countin_switch")) == 1
            settings.defaultMinute = cursor.getInt(cursor.getColumnIndex("default_minute"))
            settings.defaultSecond = cursor.getInt(cursor.getColumnIndex("default_second"))
        }
        cursor.close()
        return settings
    }

    fun update(helper: DbHelper, settings: Settings) {
        val db = helper.writableDatabase
        db.update(TABLE, settings.toContentValue(), "id = ?", arrayOf("${settings.id}"))
    }
}