package com.kaplanbora.stick2exercise.repository


object Repository {
    var settings = Settings()

    fun readSettings(dbHelper: DbHelper): Settings {
        settings = SettingsDatabase.get(dbHelper)
        return settings
    }

    fun saveSettings(dbHelper: DbHelper) {
        SettingsDatabase.update(dbHelper, settings)
    }

    fun getUsers(): MutableList<User> {
        if (true) {
            return FirebaseRepository.getUsers()
        } else {
            // TODO: Replace with local database
            return FirebaseRepository.getUsers()
        }
    }
}