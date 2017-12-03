package com.kaplanbora.stick2exercise.repository

import android.content.ContentValues

object UserDatabase {
    private val TABLE = "account"
    private val TABLE_COLUMNS = arrayOf(
            "id",
            "email",
            "password"
    )

    private fun User.toContentValue(): ContentValues {
        val content = ContentValues()
        content.put("email", this.email)
        content.put("password", this.password)
        return content
    }

    fun loadUsers(helper: DbHelper) {
        val db = helper.readableDatabase
        val cursor = db.query(TABLE, TABLE_COLUMNS, null, null, null, null, null)
        while (cursor.moveToNext()) {
            Repository.users.add(User(
                    cursor.getLong(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("email")),
                    cursor.getString(cursor.getColumnIndex("password"))
            ))
        }
        cursor.close()
    }

    fun add(helper: DbHelper, user: User) {
        val db = helper.writableDatabase
        val id = db.insert(TABLE, null, user.toContentValue())
        user.id = id
        Repository.users.add(user)
    }

}