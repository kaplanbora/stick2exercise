package com.kaplanbora.stick2exercise.repository


object Repository {
    var settings = Settings()

    fun loadSettings(dbHelper: DbHelper): Settings {
        settings = SettingsDatabase.get(dbHelper)
        return settings
    }

    fun saveSettings(dbHelper: DbHelper) {
        SettingsDatabase.update(dbHelper, settings)
    }
}