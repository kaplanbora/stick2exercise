package com.kaplanbora.stick2exercise

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.kaplanbora.stick2exercise.R.string.settings

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        title = getString(settings)
        // Use shared preferences
    }
}
