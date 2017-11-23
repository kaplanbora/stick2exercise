package com.kaplanbora.stick2exercise.exercise

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.view.LayoutInflater
import android.widget.PopupMenu
import com.kaplanbora.stick2exercise.R
import com.kaplanbora.stick2exercise.repository.Exercise
import kotlinx.android.synthetic.main.exercise_row.view.*


class ExerciseListAdapter(private val listener: ExerciseActionListener, val context: Context, private val exerciseList: List<Exercise>) : BaseAdapter() {
    override fun getCount(): Int = exerciseList.size

    override fun getItem(position: Int): Any = exerciseList.first { it.position == position + 1 }

    override fun getItemId(position: Int): Long = exerciseList.first { it.position == position + 1 }.id

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val row = inflater.inflate(R.layout.exercise_row, parent, false)
        val exercise = getItem(position) as Exercise
        row.exerciseName.text = "${position + 1}. ${exercise.name}"
        if (exercise.playDuration.seconds == 0) {
            row.exerciseDuration.text = "${exercise.playDuration.minutes} Minutes"
        } else {
            row.exerciseDuration.text = "${exercise.playDuration.minutes} Minutes ${exercise.playDuration.seconds} Seconds"
        }
        row.exerciseTempo.text = "${exercise.metronome.tempo} BPM"
        val popup = PopupMenu(context, row.exerciseMenu)
        popup.menuInflater.inflate(R.menu.exercise_menu, popup.menu)
        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.exerciseMenuDelete -> listener.deleteExercise(exercise)
                R.id.exerciseMenuEdit -> listener.editExercise(exercise)
            }
            true
        }
        row.exerciseMenu.setOnClickListener { _ ->
            popup.show()
        }
        return row
    }
}
