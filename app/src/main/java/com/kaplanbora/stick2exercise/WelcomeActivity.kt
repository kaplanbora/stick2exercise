package com.kaplanbora.stick2exercise

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kaplanbora.stick2exercise.repository.DbHelper
import com.kaplanbora.stick2exercise.repository.Repository
import com.kaplanbora.stick2exercise.routine.RoutineListActivity
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {
    var dbHelper: DbHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        Log.d("SETTINGS_BEFORE", Repository.settings.toString())
        val prefs = this.getPreferences(android.content.Context.MODE_PRIVATE)
        dbHelper = DbHelper(applicationContext)
        val settings = Repository.readSettings(dbHelper!!)
        Log.d("SETTINGS_AFTER", Repository.settings.toString())

        val isInitiated = prefs.getBoolean("isInitiated", false)
        if (isInitiated) {
            if (settings.onlineMode) {
                val intent = Intent(applicationContext, MyLoginActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(applicationContext, RoutineListActivity::class.java)
                startActivity(intent)
            }
        }

        noButton.setOnClickListener { _ ->
            val editor = prefs.edit()
            editor.putBoolean("isInitiated", true)
            editor.apply()
            Repository.settings.onlineMode = false
            Repository.saveSettings(dbHelper!!)
         }

        yesButton.setOnClickListener { _ ->
            val editor = prefs.edit()
            editor.putBoolean("isInitiated", true)
            editor.apply()
            // Ask permission first
            Repository.settings.onlineMode = true

        }
    }

    override fun onDestroy() {
        dbHelper?.close()
        super.onDestroy()
    }
}
