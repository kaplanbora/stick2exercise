package com.kaplanbora.stick2exercise.exercise

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.kaplanbora.stick2exercise.R
import com.kaplanbora.stick2exercise.repository.*
import kotlinx.android.synthetic.main.activity_create_exercise.*

class EditExerciseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_exercise)
        title = "Edit Exercise"
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

        breakMinute.minValue = 0
        breakMinute.maxValue = minutes.size - 1
        breakMinute.wrapSelectorWheel = true
        breakMinute.displayedValues = minutes

        breakSecond.minValue = 0
        breakSecond.maxValue = seconds.size - 1
        breakSecond.wrapSelectorWheel = true
        breakSecond.displayedValues = seconds

        val routine = RoutineRepository.get(intent.extras.getLong("routineId"))
        val exercise = routine.exercises.first { it.id == intent.extras.getLong("exerciseId") }

        exerciseCreateButton.text = getString(R.string.edit)
        name.setText(exercise.name)
        tempo.setText(exercise.metronome.tempo.toString())
        playMinute.value = exercise.playDuration.minutes / 10
        playSecond.value = exercise.playDuration.seconds / 10
        breakMinute.value = exercise.breakDuration.minutes / 10
        breakSecond.value = exercise.breakDuration.seconds / 10
        exerciseCreateButton.setOnClickListener { _ ->
            // TODO: Another error check should be for 0 minutes 0 seconds play time
            if (tempo.text.isEmpty()) {
                tempo.error = "Tempo cannot be empty."
            } else {
                exercise.name = name.text.toString().take(100)
                exercise.metronome.tempo = tempo.text.toString().toInt()
                exercise.metronome.subdivUp = 4
                exercise.metronome.subdivDown = 4
                exercise.playDuration.minutes = playMinute.value.toString().toInt()
                exercise.playDuration.seconds = playSecond.value.toString().toInt() * 10
                exercise.breakDuration.minutes = breakMinute.value.toString().toInt()
                exercise.breakDuration.seconds = breakSecond.value.toString().toInt() * 10
                finish()
            }
        }
    }
}
