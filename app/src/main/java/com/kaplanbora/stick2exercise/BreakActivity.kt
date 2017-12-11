package com.kaplanbora.stick2exercise

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.WindowManager
import android.widget.Toast
import com.kaplanbora.stick2exercise.exercise.ExerciseActivity
import com.kaplanbora.stick2exercise.repository.Repository
import com.kaplanbora.stick2exercise.repository.RoutineRepository
import kotlinx.android.synthetic.main.activity_break.*

class BreakActivity : AppCompatActivity() {
    var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_break)
        if (Repository.settings.screenOn) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        val routineId = intent.extras.getLong("routineId")
        val nextIndex = intent.extras.getInt("nextExerciseIndex")
        val nextExercise = RoutineRepository.get(routineId).exercises[nextIndex]
        val isPlaying = intent.extras.getBoolean("isPlaying", false)
        val msDuration = ((nextExercise.breakDuration.minutes * 60) + nextExercise.breakDuration.seconds).toLong() * 1000
        timer = object : CountDownTimer(msDuration, 1000) {
            override fun onTick(msLeft: Long) {
                timerMinute.text = String.format("%02d", msToMin(msLeft))
                timerSecond.text = String.format("%02d", msToSec(msLeft))
            }

            override fun onFinish() {
                timerMinute.text = "00"
                timerSecond.text = "00"
                if (RoutineRepository.get(routineId).exercises.size > nextIndex) {
                    val intent = Intent(applicationContext, ExerciseActivity::class.java)
                    intent.putExtra("exerciseIndex", nextIndex)
                    intent.putExtra("routineId", routineId)
                    intent.putExtra("isPlaying", isPlaying)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Finished exercise routine!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }

        timer!!.start()

        exerciseName.text = nextExercise.name

        skipButton.setOnClickListener { _ ->
            if (RoutineRepository.get(routineId).exercises.size > nextIndex) {
                val intent = Intent(applicationContext, ExerciseActivity::class.java)
                intent.putExtra("exerciseIndex", nextIndex)
                intent.putExtra("routineId", routineId)
                intent.putExtra("isPlaying", isPlaying)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(applicationContext, "Finished exercise routine!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }

    private fun msToMin(ms: Long): Long = (ms / 1000) / 60

    private fun msToSec(ms: Long): Long = (ms / 1000) % 60
}
