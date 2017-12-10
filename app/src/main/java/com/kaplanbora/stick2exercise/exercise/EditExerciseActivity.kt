package com.kaplanbora.stick2exercise.exercise

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.PopupMenu
import android.widget.Toast
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

        val subdivUpPopup = PopupMenu(applicationContext, subdivUp)
        subdivUpPopup.menuInflater.inflate(R.menu.subdivup_menu, subdivUpPopup.menu)
        subdivUpPopup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.up1 -> subdivUp.text = "1"
                R.id.up2 -> subdivUp.text = "2"
                R.id.up3 -> subdivUp.text = "3"
                R.id.up4 -> subdivUp.text = "4"
                R.id.up5 -> subdivUp.text = "5"
                R.id.up6 -> subdivUp.text = "6"
                R.id.up7 -> subdivUp.text = "7"
                R.id.up8 -> subdivUp.text = "8"
                R.id.up9 -> subdivUp.text = "9"
                R.id.up10 -> subdivUp.text = "10"
                R.id.up11 -> subdivUp.text = "11"
                R.id.up12 -> subdivUp.text = "12"
                R.id.up13 -> subdivUp.text = "13"
                R.id.up14 -> subdivUp.text = "14"
                R.id.up15 -> subdivUp.text = "15"
                R.id.up16 -> subdivUp.text = "16"
            }
            true
        }

        subdivUp.setOnClickListener { _ ->
            subdivUpPopup.show()
        }

        val subdivDownPopup = PopupMenu(applicationContext, subdivDown)
        subdivDownPopup.menuInflater.inflate(R.menu.subdivdown_menu, subdivDownPopup.menu)
        subdivDownPopup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.down4 -> subdivDown.text = "4"
                R.id.down8 -> subdivDown.text = "8"
                R.id.down16 -> subdivDown.text = "16"
            }
            true
        }

        subdivDown.setOnClickListener { _ ->
            subdivDownPopup.show()
        }

        exerciseCreateButton.text = getString(R.string.edit)
        name.setText(exercise.name)
        subdivUp.text = exercise.metronome.subdivUp.toString()
        subdivDown.text = exercise.metronome.subdivDown.toString()
        tempo.setText(exercise.metronome.tempo.toString())
        playMinute.value = exercise.playDuration.minutes
        playSecond.value = exercise.playDuration.seconds / 10
        breakMinute.value = exercise.breakDuration.minutes
        breakSecond.value = exercise.breakDuration.seconds / 10
        exerciseCreateButton.setOnClickListener { _ ->
            if (tempo.text.isEmpty()) {
                Toast.makeText(applicationContext, getString(R.string.error_empty_tempo), Toast.LENGTH_LONG)
                        .show()
            } else if (tempo.text.toString().toInt() < 30 || tempo.text.toString().toInt() > 300) {
                Toast.makeText(applicationContext, getString(R.string.error_tempo_limits), Toast.LENGTH_LONG)
                        .show()
            } else if (playMinute.value.toString() == "0" && playSecond.value.toString() == "0"){
                Toast.makeText(applicationContext, getString(R.string.error_zero_duration), Toast.LENGTH_LONG)
                        .show()
            } else {
                exercise.name = name.text.toString().take(100)
                exercise.metronome.tempo = tempo.text.toString().toInt()
                exercise.metronome.subdivUp = subdivUp.text.toString().toInt()
                exercise.metronome.subdivDown = subdivDown.text.toString().toInt()
                exercise.playDuration.minutes = playMinute.value.toString().toInt()
                exercise.playDuration.seconds = playSecond.value.toString().toInt() * 10
                exercise.breakDuration.minutes = breakMinute.value.toString().toInt()
                exercise.breakDuration.seconds = breakSecond.value.toString().toInt() * 10
                val intent = Intent()
                intent.putExtra("exerciseId", exercise.id)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }
}
