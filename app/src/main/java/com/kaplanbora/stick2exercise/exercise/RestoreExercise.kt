package com.kaplanbora.stick2exercise.exercise

import android.view.View
import com.kaplanbora.stick2exercise.repository.*

class RestoreExercise(
        val listener: ExerciseActionListener,
        val exercise: Exercise,
        val dbHelper: DbHelper,
        val repository: ExerciseRepository
) : View.OnClickListener {
    override fun onClick(v: View) {
        repository.restore(dbHelper, exercise)
        listener.refreshListView()
    }
}