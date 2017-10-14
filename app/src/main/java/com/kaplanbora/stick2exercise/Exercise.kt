package com.kaplanbora.stick2exercise

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.exercise_row.view.*
import java.time.Duration

data class Exercise(val id: Long, var name: String, var tempo: Int, var duration: Int)

class ExerciseAdapter(val exercises: MutableList<Exercise>) : RecyclerView.Adapter<ExerciseAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(exercise: Exercise, position: Int) {
            itemView.exerciseName.text = "$position. exercise.name"
            itemView.exerciseDuration.text = exercise.duration.toString()
            itemView.exerciseTempo.text = exercise.tempo.toString()

        }
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    override fun onBindViewHolder(holder: ExerciseAdapter.ViewHolder, position: Int) {
        holder.bindItems(exercises[position], position)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ExerciseAdapter.ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.exercise_row, parent, false)
        return ViewHolder(view)
    }

}