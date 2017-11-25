package com.kaplanbora.stick2exercise.routine

import android.view.View
import com.kaplanbora.stick2exercise.repository.DbHelper
import com.kaplanbora.stick2exercise.repository.FirebaseRepository
import com.kaplanbora.stick2exercise.repository.Routine
import com.kaplanbora.stick2exercise.repository.RoutineRepository

class RestoreRoutine(val userId: Long, val listener: RoutineActionListener, val routine: Routine, val dbHelper: DbHelper) : View.OnClickListener {
    override fun onClick(v: View) {
        RoutineRepository.restore(dbHelper, routine)
        FirebaseRepository.restoreRoutine(routine, userId)
        listener.refreshListView()
        // TODO: Add restoreRoutine code for exercises in this repository
        // ExerciseRepository.addAll(routine.exercises)
    }
}