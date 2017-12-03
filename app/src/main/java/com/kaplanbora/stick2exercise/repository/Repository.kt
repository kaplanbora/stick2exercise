package com.kaplanbora.stick2exercise.repository


object Repository {
    var settings = Settings()
    var users: MutableList<User> = mutableListOf()

    fun loadSettings(dbHelper: DbHelper): Settings {
        settings = SettingsDatabase.get(dbHelper)
        return settings
    }

    fun saveSettings(dbHelper: DbHelper) {
        SettingsDatabase.update(dbHelper, settings)
    }
}