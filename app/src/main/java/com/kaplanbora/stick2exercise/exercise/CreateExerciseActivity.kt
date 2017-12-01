package com.kaplanbora.stick2exercise.exercise

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.kaplanbora.stick2exercise.R
import com.kaplanbora.stick2exercise.repository.*
import kotlinx.android.synthetic.main.activity_create_exercise.*

class CreateExerciseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_exercise)
        title = "Create Exercise"
        val minutes = (0..60).map { it.toString() }.toTypedArray()
        val seconds = (0..50 step 10).map { it.toString() }.toTypedArray()
        playMinute.minValue = 0
        playMinute.maxValue = minutes.size - 1
        playMinute.wrapSelectorWheel = true
        playMinute.displayedValues = minutes

        playSecond.minValue = 0
        playSecond.maxValue = seconds.size - 1
        playSecond.wrapSelectorWheel = true
        playSecond.displayedValues = seconds
        playSecond.value = 0

        breakMinute.minValue = 0
        breakMinute.maxValue = minutes.size - 1
        breakMinute.wrapSelectorWheel = true
        breakMinute.displayedValues = minutes
        breakMinute.value = 0

        breakSecond.minValue = 0
        breakSecond.maxValue = seconds.size - 1
        breakSecond.wrapSelectorWheel = true
        breakSecond.displayedValues = seconds
        breakSecond.value = 0

        name.requestFocus()

        exerciseCreateButton.setOnClickListener { _ ->
            if (tempo.text.isEmpty()) {
                Toast.makeText(applicationContext, getString(R.string.error_empty_tempo), Toast.LENGTH_LONG)
                        .show()
            } else if (playMinute.value.toString() == "0" && playSecond.value.toString() == "0"){
                Toast.makeText(applicationContext, getString(R.string.error_zero_duration), Toast.LENGTH_LONG)
                        .show()
            } else {
                val routine = RoutineRepository.get(intent.extras.getLong("routineId"))
                val exercise = Exercise(
                        -1,
                        routine.exercises.size + 1,
                        name.text.toString().take(100),
                        Metronome(
                                tempo.text.toString().toInt(),
                                4,
                                4
                        ),
                        PlayDuration(
                                playMinute.value.toString().toInt(),
                                playSecond.value.toString().toInt() * 10
                        ),
                        BreakDuration(
                                breakMinute.value.toString().toInt(),
                                breakSecond.value.toString().toInt() * 10
                        )
                )
                routine.exercises.add(exercise)
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }
}
