package com.kaplanbora.stick2exercise

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.kaplanbora.stick2exercise.repository.RoutineRepo
import kotlinx.android.synthetic.main.fragment_create_routine.view.*

class EditRoutineFragment : DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_create_routine, container, false)
        val routine = RoutineRepo.get(arguments.getLong("routineId", -1))
        view.createRoutineInput.setText(routine.name)
        view.createRoutineCancel.setOnClickListener{ _ -> dismiss() }
        view.createRoutineOk.setOnClickListener{ _ ->
            val input = view.createRoutineInput.text.toString().take(100)
            routine.name = input
            dismiss()
        }
        view.createRoutineInput.requestFocus()
        dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        return view
    }
}