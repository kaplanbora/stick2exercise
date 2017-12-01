package com.kaplanbora.stick2exercise.exercise

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.kaplanbora.stick2exercise.R
import com.kaplanbora.stick2exercise.repository.*

import kotlinx.android.synthetic.main.activity_exercise_list.*
import kotlinx.android.synthetic.main.content_exercise_list.*

class ExerciseListActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, ExerciseActionListener {
    private var exerciseRepository: ExerciseRepository? = null
    private var dbHelper: DbHelper? = null
    private val CREATE_EXERCISE = 1000
    private val EDIT_EXERCISE = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_list)
        setSupportActionBar(toolbar)

        val routine = RoutineRepository.get(intent.extras.getLong("routineId"))
        exerciseRepository = ExerciseRepository(routine)
        dbHelper = DbHelper(applicationContext)
        title = routine.name

        exerciseListView.adapter = ExerciseListAdapter(this, applicationContext, routine.exercises)
        exerciseListView.setOnItemClickListener{adapterView, view, i, l ->
            val intent = Intent(applicationContext, ExerciseActivity::class.java)
            intent.putExtra("exerciseId", l)
            intent.putExtra("exerciseIndex", i)
            intent.putExtra("routineId", routine.id)
            startActivity(intent)
        }

        fab.setOnClickListener { _ ->
            val intent = Intent(applicationContext, CreateExerciseActivity::class.java)
            intent.putExtra("routineId", routine.id)
            startActivityForResult(intent, CREATE_EXERCISE)
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun editExercise(exercise: Exercise) {
        val intent = Intent(applicationContext, EditExerciseActivity::class.java)
        intent.putExtra("exerciseId", exercise.id)
        intent.putExtra("routineId", exerciseRepository!!.routine.id)
        startActivityForResult(intent, EDIT_EXERCISE)
    }

    override fun deleteExercise(exercise: Exercise) {
        exerciseRepository!!.remove(dbHelper!!, exercise)
        FirebaseRepository.deleteExercise(exercise, intent.extras.getLong("routineId"), intent.extras.getLong("userId"))
        refreshListView()
        Snackbar.make(exerciseListRoot, R.string.exercise_delete_message, Snackbar.LENGTH_LONG)
//                .setAction("UNDO", RestoreExercise(this, exercise, dbHelper!!, exerciseRepository!!))
                .setAction("UNDO", RestoreExercise(intent.extras.getLong("routineId"), intent.extras.getLong("userId"), this, exercise, dbHelper!!, exerciseRepository!!))
                .show()
    }

    override fun refreshListView() {
        if (exerciseRepository!!.all().isEmpty()) {
            emptyMessage.visibility = TextView.VISIBLE
        } else {
            emptyMessage.visibility = TextView.INVISIBLE
        }
        exerciseListView.adapter = ExerciseListAdapter(this, applicationContext, exerciseRepository!!.all())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CREATE_EXERCISE && resultCode == Activity.RESULT_OK) {
            val newExercise = exerciseRepository!!.get(-1)
            exerciseRepository!!.add(dbHelper!!, newExercise)
            FirebaseRepository.addExercise(newExercise, intent.extras.getLong("routineId"), intent.extras.getLong("userId"))
        }
        if (requestCode == EDIT_EXERCISE && resultCode == Activity.RESULT_OK) {
            val exercise = exerciseRepository!!.get(data?.extras!!.getLong("exerciseId"))
            exerciseRepository!!.update(dbHelper!!, exercise)
            FirebaseRepository.updateExercise(exercise, intent.extras.getLong("routineId"), intent.extras.getLong("userId"))
        }
    }

    override fun onResume() {
        refreshListView()
        super.onResume()
    }

    override fun onDestroy() {
        dbHelper?.close()
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.routine_list_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
