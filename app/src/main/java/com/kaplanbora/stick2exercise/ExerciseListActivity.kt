package com.kaplanbora.stick2exercise

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.content_exercise_list.*

import kotlinx.android.synthetic.main.activity_exercise_list.*

class ExerciseListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_list)
        setSupportActionBar(toolbar)

//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }

        val routine = Data.getRoutine(intent.getLongExtra("routineId", 0))
        exerciseListView.adapter = ExerciseListAdapter(applicationContext, routine.exercises)

    }

}
