package com.kaplanbora.stick2exercise.exercise

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.kaplanbora.stick2exercise.*
import com.kaplanbora.stick2exercise.repository.Metronome
import com.kaplanbora.stick2exercise.repository.RoutineRepository
import kotlinx.android.synthetic.main.activity_exercise.*
import kotlinx.android.synthetic.main.fragment_exercise.*
import com.kaplanbora.stick2exercise.routine.RoutineListActivity


class ExerciseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var currentBeat = 1
    val metronome = Metronome(120, 4, 4)
    var isPlaying = false
    var timerOn = false
    var timerTick = 0L
    var timer: CountDownTimer? = null
    var metronomePlayer: CountDownTimer? = null
    var player1: MediaPlayer? = null
    var player2: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)
        setSupportActionBar(toolbar)

        player1 = MediaPlayer.create(applicationContext, R.raw.metro_1)
        player2 = MediaPlayer.create(applicationContext, R.raw.metro_other)
        val index = intent.extras.getInt("exerciseIndex")
        val routineId = intent.extras.getLong("routineId")
        val exercise = RoutineRepository.get(routineId).exercises[index]
        metronome.tempo = exercise.metronome.tempo
        metronome.subdivUp = exercise.metronome.subdivUp
        metronome.subdivDown = exercise.metronome.subdivDown
        isPlaying = intent.extras.getBoolean("isPlaying", false)

        title = "${getString(R.string.exercise)} ${exercise.position}"

        name.text = exercise.name
        tempo.text = "${exercise.metronome.tempo} BPM"
        subdiv.text = "${exercise.metronome.subdivUp} / ${exercise.metronome.subdivDown}"
        timerMinute.text = String.format("%02d", exercise.playDuration.minutes)
        timerSecond.text = String.format("%02d", exercise.playDuration.seconds)

        val msDuration = ((exercise.playDuration.minutes * 60) + exercise.playDuration.seconds).toLong() * 1000

        timerButton.setOnClickListener { _ ->
            if (timer == null) {
                isPlaying = true
                timerOn = true
                metronomePlayer = createMetronome(99 * 60 * 1000)
                timer = createTimer(msDuration, index, routineId)
                timerButton.toggle()
                timer!!.start()
                metronomePlayer!!.start()
            } else if (timerOn) {
                isPlaying = false
                timerOn = false
                timerButton.toggle()
                timer!!.cancel()
                metronomePlayer!!.cancel()
            } else {
                isPlaying = true
                timerOn = true
                timer = createTimer(timerTick, index, routineId)
                timerButton.toggle()
                timer!!.start()
                metronomePlayer!!.start()
            }
        }

        nextButton.setOnClickListener { _ ->
            nextExercise(index, routineId)
            timer?.cancel()
            metronomePlayer?.cancel()
        }

        previousButton.setOnClickListener { _ ->
            previousExercise(index, routineId)
            timer?.cancel()
            metronomePlayer?.cancel()
        }

        if (isPlaying) {
            timerButton.callOnClick()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    private fun nextExercise(currentIndex: Int, routineId: Long) {
        if (RoutineRepository.get(routineId).exercises.size > currentIndex + 1) {
            val intent = Intent(applicationContext, BreakActivity::class.java)
            intent.putExtra("nextExerciseIndex", currentIndex + 1)
            intent.putExtra("routineId", routineId)
            intent.putExtra("isPlaying", isPlaying)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(applicationContext, "Finished exercise routine!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun previousExercise(currentIndex: Int, routineId: Long) {
        if (currentIndex > 0) {
            val intent = Intent(applicationContext, ExerciseActivity::class.java)
            intent.putExtra("exerciseIndex", currentIndex - 1)
            intent.putExtra("routineId", routineId)
            intent.putExtra("isPlaying", isPlaying)
            startActivity(intent)
            finish()
        }
    }

    private fun createMetronome(duration: Long): CountDownTimer {
        return object : CountDownTimer(duration, calculateInterval()) {
            override fun onTick(msLeft: Long) {
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
            }
        }
    }

    fun calculateInterval(): Long {
        val tempo = (60.0 * 1000.0 / metronome.tempo.toDouble())
        val signature = (4.0 / metronome.subdivDown.toDouble())
        return (tempo * signature).toLong()
    }

    private fun createTimer(duration: Long, currentIndex: Int, routineId: Long): CountDownTimer {
        return object : CountDownTimer(duration, 1000) {
            override fun onTick(msLeft: Long) {
                timerTick = msLeft
                timerMinute.text = String.format("%02d", msToMin(timerTick))
                timerSecond.text = String.format("%02d", msToSec(timerTick))
            }

            override fun onFinish() {
                timerMinute.text = "00"
                timerSecond.text = "00"
                timer?.cancel()
                metronomePlayer?.cancel()
                timerTick = 0
                timerOn = false
                timer = null
                timerButton.toggle()
                nextExercise(currentIndex, routineId)
            }
        }

    }

    private fun msToMin(ms: Long): Long = (ms / 1000) / 60

    private fun msToSec(ms: Long): Long = (ms / 1000) % 60

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        metronomePlayer?.cancel()
    }

    override fun onPause() {
        super.onPause()
        if (timerOn) {
            timerButton.callOnClick()
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
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
                val intent = Intent(applicationContext, MetronomeActivity::class.java)
                startActivity(intent)
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
