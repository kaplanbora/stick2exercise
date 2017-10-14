package com.kaplanbora.stick2exercise

import android.content.Intent
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
        exerciseMinute.minValue = 0
        exerciseMinute.maxValue = minutes.size - 1
        exerciseMinute.wrapSelectorWheel = true
        exerciseMinute.displayedValues = minutes

        exerciseSecond.minValue = 0
        exerciseSecond.maxValue = seconds.size - 1
        exerciseSecond.wrapSelectorWheel = true
        exerciseSecond.displayedValues = seconds

        exerciseNameEdit.requestFocus()

        exerciseCreateButton.setOnClickListener { _ ->
            val routine = Data.getRoutine(intent.getLongExtra("routineId", 0))
            val exercise = Exercise(
                    515,
                    exerciseNameEdit.text.toString(),
                    exerciseTempoEdit.text.toString().toInt(),
                    exerciseMinute.value.toString().toInt(),
                    exerciseSecond.value.toString().toInt())
            routine.exercises.add(exercise)
            val intent = Intent(applicationContext, ExerciseListActivity::class.java)
            intent.putExtra("routineId", routine.id)
            startActivity(intent)
        }
    }
}
