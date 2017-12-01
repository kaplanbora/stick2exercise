package com.kaplanbora.stick2exercise.exercise

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.kaplanbora.stick2exercise.R
import com.kaplanbora.stick2exercise.repository.RoutineRepository
import kotlinx.android.synthetic.main.activity_exercise.*
import kotlinx.android.synthetic.main.fragment_exercise.*
import com.kaplanbora.stick2exercise.MetronomeActivity
import com.kaplanbora.stick2exercise.MyLoginActivity
import com.kaplanbora.stick2exercise.SettingsActivity
import com.kaplanbora.stick2exercise.routine.RoutineListActivity


class ExerciseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var timerOn = false
    var timerTick = 0L
    var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)
        setSupportActionBar(toolbar)

        // TODO: Why index?
        val index = intent.extras.getInt("exerciseIndex")
        val exercise = RoutineRepository.get(intent.extras.getLong("routineId")).exercises[index]

        title = "Exercise ${exercise.position}"

        name.text = exercise.name
        tempo.text = "${exercise.metronome.tempo} BPM"
        subdiv.text = "${exercise.metronome.subdivUp} / ${exercise.metronome.subdivDown}"
        timerMinute.text = String.format("%02d", exercise.playDuration.minutes)
        timerSecond.text = String.format("%02d", exercise.playDuration.seconds)

        val msDuration = ((exercise.playDuration.minutes * 60) + exercise.playDuration.seconds).toLong() * 1000

        timerButton.setOnClickListener { _ ->
            if (timer == null) {
                timerOn = true
                timer = createTimer(msDuration)
                timerButton.toggle()
                timer!!.start()
//                timerButton.background = resources.getDrawable(android.R.drawable.ic_media_pause)
            } else if (timerOn) {
                timerOn = false
                timerButton.toggle()
                timer!!.cancel()
//                timerButton.background = resources.getDrawable(android.R.drawable.ic_media_play)
            } else {
                timerOn = true
                timer = createTimer(timerTick)
                timerButton.toggle()
                timer!!.start()
//                timerButton.background = resources.getDrawable(android.R.drawable.ic_media_pause)
            }
        }


        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    private fun createTimer(duration: Long): CountDownTimer {
        return object : CountDownTimer(duration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timerTick = millisUntilFinished
                Log.d("Tick", timerTick.toString())
                timerMinute.text = String.format("%02d", msToMin(timerTick))
                timerSecond.text = String.format("%02d", msToSec(timerTick))
            }

            override fun onFinish() {
                timerMinute.text = "00"
                timerSecond.text = "00"
                timer!!.cancel()
                timerTick = 0
                timerOn = false
                timer = null
            }
        }

    }

    private fun msToMin(ms: Long): Long = (ms / 1000) / 60

    private fun msToSec(ms: Long): Long = (ms / 1000) % 60

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
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
//            R.id.edit -> {
//                val intent = Intent(applicationContext, EditExerciseActivity::class.java)
//                val routine = RoutineRepository.get(intent.extras.getLong("routineId"))
//                val exercise = routine.exercises.get(intent.extras.getInt("exerciseIndex"))
//                intent.putExtra("exerciseId", exercise.id)
//                intent.putExtra("routineId", routine.id)
//                startActivityForResult(intent, EDIT_EXERCISE)
//            }
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
