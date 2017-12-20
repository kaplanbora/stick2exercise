package com.kaplanbora.stick2exercise.routine

import android.view.View
import com.kaplanbora.stick2exercise.repository.*

class RestoreRoutine(
        val listener: RoutineActionListener,
        val routine: Routine,
        val dbHelper: DbHelper
) : View.OnClickListener {
    override fun onClick(v: View) {
        RoutineRepository.restore(dbHelper, routine)
        val exerciseRepo = ExerciseRepository(routine)
        routine.exercises.forEach { exerciseRepo.add(dbHelper, it) }
        listener.refreshListView()
    }
}