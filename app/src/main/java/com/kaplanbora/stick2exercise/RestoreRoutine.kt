package com.kaplanbora.stick2exercise

import android.view.View

class RestoreRoutine(val listener: RoutineActionListener, val routine: Routine) : View.OnClickListener {
    override fun onClick(v: View) {
        RoutineRepo.addRoutine(routine)
        listener.refreshRoutines()
    }
}