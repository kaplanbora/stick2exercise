package com.kaplanbora.stick2exercise.routine

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.kaplanbora.stick2exercise.exercise.ExerciseListActivity
import com.kaplanbora.stick2exercise.R
import com.kaplanbora.stick2exercise.repository.Routine
import com.kaplanbora.stick2exercise.repository.RoutineRepo
import kotlinx.android.synthetic.main.fragment_create_routine.view.*

class CreateRoutineFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_create_routine, container, false)
        view.createRoutineCancel.setOnClickListener{ _ -> dismiss() }
        view.createRoutineOk.setOnClickListener{ _ ->
            val input = view.createRoutineInput.text.toString().take(100)
            val routine = Routine(RoutineRepo.generateId(), 0, input, mutableListOf())
            RoutineRepo.addRoutine(routine)
            val intent = Intent(context, ExerciseListActivity::class.java)
            intent.putExtra("routineId", routine.id)
            dismiss()
            startActivity(intent)
        }
        view.createRoutineInput.requestFocus()
        dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        return view
    }
}