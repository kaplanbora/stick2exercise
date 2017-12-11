package com.kaplanbora.stick2exercise

import android.content.Intent
import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import com.kaplanbora.stick2exercise.repository.Metronome
import com.kaplanbora.stick2exercise.routine.RoutineListActivity
import kotlinx.android.synthetic.main.activity_metronome.*
import kotlinx.android.synthetic.main.content_metronome.*

class MetronomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var currentBeat = 1
    val metronome = Metronome(120, 4, 4)
    var timerOn = false
    var currentMinute = 0
    var currentSecond = 0
    var timer: CountDownTimer? = null
    var metronomePlayer: CountDownTimer? = null
    var player1: MediaPlayer? = null
    var player2: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metronome)
        setSupportActionBar(toolbar)

        player1 = MediaPlayer.create(applicationContext, R.raw.metro_1)
        player2 = MediaPlayer.create(applicationContext, R.raw.metro_other)
        val prefs = this.getPreferences(android.content.Context.MODE_PRIVATE)
        val editor = prefs.edit()
        metronome.tempo = prefs.getInt("lastTempo", 120)
        metronome.subdivUp = subdivUp.text.toString().toInt()
        metronome.subdivDown = subdivDown.text.toString().toInt()
        updateTempo(0)

        val subdivUpPopup = PopupMenu(applicationContext, subdivUp)
        subdivUpPopup.menuInflater.inflate(R.menu.subdivup_menu, subdivUpPopup.menu)
        subdivUpPopup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.up1 -> setSubdivUp("1")
                R.id.up2 -> setSubdivUp("2")
                R.id.up3 -> setSubdivUp("3")
                R.id.up4 -> setSubdivUp("4")
                R.id.up5 -> setSubdivUp("5")
                R.id.up6 -> setSubdivUp("6")
                R.id.up7 -> setSubdivUp("7")
                R.id.up8 -> setSubdivUp("8")
                R.id.up9 -> setSubdivUp("9")
                R.id.up10 -> setSubdivUp("10")
                R.id.up11 -> setSubdivUp("11")
                R.id.up12 -> setSubdivUp("12")
                R.id.up13 -> setSubdivUp("13")
                R.id.up14 -> setSubdivUp("14")
                R.id.up15 -> setSubdivUp("15")
                R.id.up16 -> setSubdivUp("16")
                else -> false
            }
        }

        subdivUp.setOnClickListener { _ ->
            subdivUpPopup.show()
        }

        val subdivDownPopup = PopupMenu(applicationContext, subdivDown)
        subdivDownPopup.menuInflater.inflate(R.menu.subdivdown_menu, subdivDownPopup.menu)
        subdivDownPopup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.down4 -> setSubdivDown("4")
                R.id.down8 -> setSubdivDown("8")
                R.id.down16 -> setSubdivDown("16")
                else -> false
            }
        }

        subdivDown.setOnClickListener { _ ->
            subdivDownPopup.show()
        }

        startButton.setOnClickListener { _ ->
            editor.putInt("lastTempo", metronome.tempo)
            editor.apply()
            if (timer == null) {
                timerOn = true
                metronomePlayer = createMetronome(99 * 60 * 1000)
                timer = createTimer(99 * 60 * 1000)
                startButton.text = getString(R.string.pause)
                timer!!.start()
                metronomePlayer!!.start()
            } else if (timerOn) {
                timerOn = false
                startButton.text = getString(R.string.start)
                timer!!.cancel()
                metronomePlayer!!.cancel()
            } else {
                timerOn = true
                timer = createTimer(99 * 60 * 1000)
                startButton.text = getString(R.string.pause)
                timer!!.start()
                metronomePlayer!!.start()
            }
        }

        resetButton.setOnClickListener { _ ->
            endTimer()
            metronomePlayer?.cancel()
            currentBeat = 1
            beat.text = "0"
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    private fun createMetronome(duration: Long): CountDownTimer {
        return object : CountDownTimer(duration, calculateInterval()) {
            override fun onTick(msLeft: Long) {
                beat.text = "$currentBeat"
                if (currentBeat == 1) {
                    player1!!.start()
                    currentBeat += 1
                } else if (currentBeat == metronome.subdivUp) {
                    player2!!.start()
                    currentBeat = 1
                } else {
                    player2!!.start()
                    currentBeat += 1
                }
            }

            override fun onFinish() {
                currentBeat = 0
                beat.text = "$currentBeat"
            }
        }
    }

    fun calculateInterval(): Long {
        val tempo = (60.0 * 1000.0 / metronome.tempo.toDouble())
        val signature = (4.0 / metronome.subdivDown.toDouble())
        return (tempo * signature).toLong()
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
        metronomePlayer?.cancel()
    }

    override fun onPause() {
        super.onPause()
        if (timerOn) {
            startButton.callOnClick()
        }
    }

    fun setSubdivUp(note: String): Boolean {
        metronome.subdivUp = note.toInt()
        subdivUp.text = note
        return true
    }

    fun setSubdivDown(note: String): Boolean {
        metronome.subdivDown = note.toInt()
        subdivDown.text = note
        return true
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
