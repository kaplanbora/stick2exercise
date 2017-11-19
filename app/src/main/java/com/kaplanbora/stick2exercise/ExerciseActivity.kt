package com.kaplanbora.stick2exercise

import android.os.Bundle
import android.os.CountDownTimer
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_exercise.*
import kotlinx.android.synthetic.main.fragment_exercise.*

class ExerciseActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var timerOn = false
    var timerTick = 0L
    var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)
        setSupportActionBar(toolbar)

        val index = intent.extras.getInt("exerciseIndex")
        val exercise = RoutineRepo.get(intent.extras.getLong("routineId")).exercises[index]

        title = "Exercise ${index + 1}"

        name.text = exercise.name
        tempo.text = "${exercise.tempo} BPM"
        subdiv.text = "${exercise.subdivUp} / ${exercise.subdivDown}"
        timerMinute.text = String.format("%02d", exercise.playMin)
        timerSecond.text = String.format("%02d", exercise.playSec)

        val msDuration = ((exercise.playMin * 60) + exercise.playSec + 1).toLong() * 1000

        timerButton.setOnClickListener { _ ->
            if (timer == null) {
                timerOn = true
                timer = createTimer(msDuration)
                timer!!.start()
                timerButton.setImageDrawable(resources.getDrawable(android.R.drawable.ic_media_pause))
            } else if (timerOn) {
                timerOn = false
                timer!!.cancel()
                timerButton.setImageDrawable(resources.getDrawable(android.R.drawable.ic_media_play))
            } else {
                timerOn = true
                timer = createTimer(timerTick)
                timer!!.start()
                timerButton.setImageDrawable(resources.getDrawable(android.R.drawable.ic_media_pause))
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
                timerButton.setImageDrawable(resources.getDrawable(android.R.drawable.ic_media_play))
            }
        }

    }

    private fun msToMin(ms: Long): Long {
        return (ms / 1000) / 60
    }

    private fun msToSec(ms: Long): Long {
        return (ms / 1000) % 60
    }

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
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.exercise, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
