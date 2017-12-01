package com.kaplanbora.stick2exercise

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.kaplanbora.stick2exercise.R.string.metronome

class MetronomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metronome)
        title = getString(metronome)
    }
}
