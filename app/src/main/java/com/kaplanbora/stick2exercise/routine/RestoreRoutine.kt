package com.kaplanbora.stick2exercise.routine

import android.view.View
import com.kaplanbora.stick2exercise.repository.DbHelper
import com.kaplanbora.stick2exercise.repository.Routine
import com.kaplanbora.stick2exercise.repository.RoutineRepository

class RestoreRoutine(val listener: RoutineActionListener, val routine: Routine, val dbHelper: DbHelper) : View.OnClickListener {
    override fun onClick(v: View) {
        RoutineRepository.addRestore(dbHelper, routine)
        listener.refreshListView()
        // TODO: Add restore code for exercises in this repository
        // ExerciseRepository.addAll(routine.exercises)
    }
}