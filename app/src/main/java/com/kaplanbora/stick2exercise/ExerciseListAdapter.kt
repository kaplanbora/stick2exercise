package com.kaplanbora.stick2exercise

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.view.LayoutInflater
import android.widget.TextView
import com.kaplanbora.stick2exercise.repository.Exercise


class ExerciseListAdapter(val context: Context, val exerciseList: List<Exercise>) : BaseAdapter() {
    override fun getCount(): Int {
        return exerciseList.size
    }

    override fun getItem(position: Int): Any {
        return exerciseList[position]
    }

    override fun getItemId(position: Int): Long {
        return exerciseList[position].id
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val row = inflater.inflate(R.layout.exercise_row, parent, false)
        val name = row.findViewById<TextView>(R.id.exerciseName)
        val duration = row.findViewById<TextView>(R.id.exerciseDuration)
        val tempo = row.findViewById<TextView>(R.id.exerciseTempo)
        val exercise = getItem(position) as Exercise
        name.text = "${position + 1}. ${exercise.name}"
        if (exercise.playDuration.seconds == 0) {
            duration.text = "${exercise.playDuration.minutes} Minutes"
        } else {
            duration.text = "${exercise.playDuration.minutes} Minutes ${exercise.playDuration.seconds} Seconds"
        }
        tempo.text = "${exercise.metronome.tempo} BPM"
        return row
    }
}
