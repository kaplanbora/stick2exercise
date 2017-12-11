package com.kaplanbora.stick2exercise.exercise

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.kaplanbora.stick2exercise.*
import com.kaplanbora.stick2exercise.repository.RoutineRepository
import kotlinx.android.synthetic.main.activity_exercise.*
import kotlinx.android.synthetic.main.fragment_exercise.*
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
        val routineId = intent.extras.getLong("routineId")
        val exercise = RoutineRepository.get(routineId).exercises[index]


        title = "${getString(R.string.exercise)} ${exercise.position}"

        name.text = exercise.name
        tempo.text = "${exercise.metronome.tempo} BPM"
        subdiv.text = "${exercise.metronome.subdivUp} / ${exercise.metronome.subdivDown}"
        timerMinute.text = String.format("%02d", exercise.playDuration.minutes)
        timerSecond.text = String.format("%02d", exercise.playDuration.seconds)

        val msDuration = ((exercise.playDuration.minutes * 60) + exercise.playDuration.seconds).toLong() * 1000

        timerButton.setOnClickListener { _ ->
            if (timer == null) {
                timerOn = true
                timer = createTimer(msDuration, index, routineId)
                timerButton.toggle()
                timer!!.start()
            } else if (timerOn) {
                timerOn = false
                timerButton.toggle()
                timer!!.cancel()
            } else {
                timerOn = true
                timer = createTimer(timerTick, index, routineId)
                timerButton.toggle()
                timer!!.start()
            }
        }

        nextButton.setOnClickListener { _ ->
            timer?.cancel()
            nextExercise(index, routineId)
        }

        previousButton.setOnClickListener { _ ->
            timer?.cancel()
            previousExercise(index, routineId)
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
            startActivity(intent)
            finish()

        }
    }

    private fun previousExercise(currentIndex: Int, routineId: Long) {
        if (currentIndex > 0) {
            val intent = Intent(applicationContext, ExerciseActivity::class.java)
            intent.putExtra("nextExerciseIndex", currentIndex - 1)
            intent.putExtra("routineId", routineId)
            startActivity(intent)
            finish()
        }
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
                timer!!.cancel()
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
