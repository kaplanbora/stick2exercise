package com.kaplanbora.stick2exercise

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
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

        breakSecond.minValue = 0
        breakSecond.maxValue = seconds.size - 1
        breakSecond.wrapSelectorWheel = true
        breakSecond.displayedValues = seconds
        breakSecond.value = 0

        exerciseNameEdit.requestFocus()

        exerciseCreateButton.setOnClickListener { _ ->
            val routine = RoutineRepo.get(intent.extras.getLong("routineId"))
            val exercise = Exercise(
                    ExerciseRepo.generateExerciseId(),
                    exerciseNameEdit.text.toString(),
                    exerciseTempoEdit.text.toString().toInt(),
                    4,
                    4,
                    playMinute.value.toString().toInt(),
                    playSecond.value.toString().toInt(),
                    breakMinute.value.toString().toInt(),
                    breakMinute.value.toString().toInt()
            )
            routine.exercises.add(exercise)
            finish()
        }
    }
}
