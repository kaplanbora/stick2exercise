package com.kaplanbora.stick2exercise.routine

import android.view.View
import com.kaplanbora.stick2exercise.repository.Routine
import com.kaplanbora.stick2exercise.repository.RoutineRepo

class RestoreRoutine(val listener: RoutineActionListener, val routine: Routine) : View.OnClickListener {
    override fun onClick(v: View) {
        RoutineRepo.addRoutine(routine)
        listener.refreshListView()
    }
}