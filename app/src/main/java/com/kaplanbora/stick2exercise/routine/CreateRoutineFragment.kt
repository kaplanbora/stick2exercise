package com.kaplanbora.stick2exercise.routine

import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.kaplanbora.stick2exercise.R
import kotlinx.android.synthetic.main.fragment_create_routine.view.*

class CreateRoutineFragment : DialogFragment() {
    var listener: RoutineActionListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        listener = context as RoutineActionListener
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_create_routine, container, false)
        view.createRoutineCancel.setOnClickListener { _ -> dismiss() }
        view.createRoutineOk.setOnClickListener { _ ->
            listener?.createRoutine(view.createRoutineInput.text.toString().take(100))
            dismiss()
        }
        view.createRoutineInput.requestFocus()
        dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        return view
    }
}