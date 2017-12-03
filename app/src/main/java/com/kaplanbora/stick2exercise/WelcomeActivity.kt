package com.kaplanbora.stick2exercise

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.kaplanbora.stick2exercise.repository.DbHelper
import com.kaplanbora.stick2exercise.repository.Repository
import com.kaplanbora.stick2exercise.routine.RoutineListActivity
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {
    var dbHelper: DbHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        dbHelper = DbHelper(applicationContext)
        Repository.readSettings(dbHelper!!)

        if (Repository.settings.isInitiated) {
            if (Repository.settings.onlineMode) {
                Repository.loadUsers()
                val intent = Intent(applicationContext, MyLoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            } else {
                val intent = Intent(applicationContext, RoutineListActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
        }

        noButton.setOnClickListener { _ ->
            Repository.settings.isInitiated = true
            Repository.settings.onlineMode = false
            Repository.saveSettings(dbHelper!!)
            val intent = Intent(applicationContext, RoutineListActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
         }

        yesButton.setOnClickListener { _ ->
            Repository.settings.isInitiated = true
            Repository.settings.onlineMode = true
            Repository.saveSettings(dbHelper!!)
            val intent = Intent(applicationContext, MyLoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        dbHelper?.close()
        super.onDestroy()
    }
}
