package com.kaplanbora.stick2exercise.repository

enum class InternetMode {
    ONLINE,
    OFFLINE
}

object Repository {
    var mode: InternetMode = InternetMode.ONLINE

    fun isOnline(): Boolean = mode == InternetMode.ONLINE

    fun getUsers(): MutableList<User> {
        if (isOnline()) {
            return FirebaseRepository.getUsers()
        } else {
            // TODO: Replace with local database
            return FirebaseRepository.getUsers()
        }
    }
}