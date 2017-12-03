package com.kaplanbora.stick2exercise.exercise

import android.view.View
import com.kaplanbora.stick2exercise.repository.*

class RestoreExercise(
        val routineId: Long,
        val userId: Long,
        val listener: ExerciseActionListener,
        val exercise: Exercise,
        val dbHelper: DbHelper,
        val repository: ExerciseRepository
) : View.OnClickListener {
    override fun onClick(v: View) {
        repository.restore(dbHelper, exercise)
        if (Repository.settings.onlineMode) {
            FirebaseRepository.restoreExercise(exercise, routineId, userId)
        }
        listener.refreshListView()
    }
}