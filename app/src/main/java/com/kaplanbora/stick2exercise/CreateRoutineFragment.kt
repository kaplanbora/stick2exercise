package com.kaplanbora.stick2exercise

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import kotlinx.android.synthetic.main.fragment_create_routine.view.*

class CreateRoutineFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_create_routine, container, false)
        view.createRoutineCancel.setOnClickListener{ _ -> dismiss() }
        view.createRoutineOk.setOnClickListener{ _ ->
            val input = view.createRoutineInput.text.toString()
            val routine = Routine(input.length.toLong(), input, mutableListOf())
            Data.routines.add(routine)
            val intent = Intent(context, ExerciseListActivity::class.java)
            intent.putExtra("routineId", routine.id)
            startActivity(intent)
        }
        view.createRoutineInput.requestFocus()
        dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        return view
    }
}