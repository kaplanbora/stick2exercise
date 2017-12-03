package com.kaplanbora.stick2exercise

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.kaplanbora.stick2exercise.repository.Metronome
import com.kaplanbora.stick2exercise.routine.RoutineListActivity
import kotlinx.android.synthetic.main.activity_metronome.*
import kotlinx.android.synthetic.main.content_metronome.*

class MetronomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    val metronome = Metronome(120, 4, 4)
    var timerOn = false
    var currentMinute = 0
    var currentSecond = 0
    var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metronome)
        setSupportActionBar(toolbar)

        val player1 = MediaPlayer.create(applicationContext, R.raw.metro_1)
        val player2 = MediaPlayer.create(applicationContext, R.raw.metro_other)
        val prefs = this.getPreferences(android.content.Context.MODE_PRIVATE)
        val editor = prefs.edit()
        metronome.tempo = prefs.getInt("lastTempo", 120)
        metronome.subdivUp = subdivUp.text.toString().toInt()
        metronome.subdivDown = subdivDown.text.toString().toInt()
        updateTempo(0)

        startButton.setOnClickListener { _ ->
            player1.start()
            editor.putInt("lastTempo", metronome.tempo)
            editor.apply()
            if (timer == null) {
                timerOn = true
                timer = createTimer(99 * 60 * 1000)
                startButton.text = getString(R.string.pause)
                timer!!.start()
            } else if (timerOn) {
                timerOn = false
                startButton.text = getString(R.string.start)
                timer!!.cancel()
            } else {
                timerOn = true
                timer = createTimer(99 * 60 * 1000)
                startButton.text = getString(R.string.pause)
                timer!!.start()
            }
        }

        resetButton.setOnClickListener { _ ->
            endTimer()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    fun playMetronome(player1: MediaPlayer, player2: MediaPlayer) {
        val currentTick = 0


    }

    private fun createTimer(duration: Long): CountDownTimer {
        return object : CountDownTimer(duration, 1000) {
            override fun onTick(msLeft: Long) {
                updateTimer()
            }

            override fun onFinish() {
                endTimer()
            }
        }
    }

    private fun endTimer() {
        timer?.cancel()
        timerOn = false
        timer = null
        currentSecond = 0
        currentMinute = 0
        startButton.text = getString(R.string.start)
        timerMinute.text = "00"
        timerSecond.text = "00"
    }

    private fun updateTimer() {
        if (currentSecond == 59) {
            currentMinute += 1
            currentSecond = 0
        } else {
            currentSecond += 1
        }
        timerMinute.text = String.format("%02d", currentMinute)
        timerSecond.text = String.format("%02d", currentSecond)
    }

    fun tempoChange(view: View) {
        when (view.id) {
            R.id.minusOneButton -> updateTempo(-1)
            R.id.minusTenButton -> updateTempo(-10)
            R.id.plusOneButton -> updateTempo(1)
            R.id.plusTenButton -> updateTempo(10)
        }
    }

    private fun updateTempo(update: Int) {
        if ((metronome.tempo + update) >= 30 && (metronome.tempo + update) <= 300) {
            metronome.tempo += update
            tempoText.text = "${metronome.tempo.toString()} BPM"
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }

    override fun onPause() {
        super.onPause()
        if (timerOn){
            startButton.callOnClick()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.exercise, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                val intent = Intent(applicationContext, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_routine_list -> {
                val intent = Intent(applicationContext, RoutineListActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_metronome -> {
            }
            R.id.nav_settings -> {
                val intent = Intent(applicationContext, SettingsActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_logout -> {
                val intent = Intent(applicationContext, MyLoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
