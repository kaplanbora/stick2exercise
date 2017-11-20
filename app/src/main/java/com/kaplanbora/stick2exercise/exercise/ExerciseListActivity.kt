package com.kaplanbora.stick2exercise.exercise

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.kaplanbora.stick2exercise.R
import com.kaplanbora.stick2exercise.repository.RoutineRepo

import kotlinx.android.synthetic.main.activity_exercise_list.*
import kotlinx.android.synthetic.main.content_exercise_list.*

class ExerciseListActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_list)
        setSupportActionBar(toolbar)

        val routine = RoutineRepo.get(intent.extras.getLong("routineId"))
        title = routine.name

        exerciseListView.adapter = ExerciseListAdapter(applicationContext, routine.exercises)
        exerciseListView.setOnItemClickListener{adapterView, view, i, l ->
            val intent = Intent(applicationContext, ExerciseActivity::class.java)
            intent.putExtra("exerciseId", l)
            intent.putExtra("exerciseIndex", i)
            intent.putExtra("routineId", routine.id)
            startActivity(intent)
        }

//        val adapter: ExerciseListAdapter = exerciseListView.adapter as ExerciseListAdapter
//        adapter.notifyDataSetChanged()

        fab.setOnClickListener { _ ->
            val intent = Intent(applicationContext, CreateExerciseActivity::class.java)
            intent.putExtra("routineId", routine.id)
            startActivity(intent)
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onResume() {
        super.onResume()
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
        menuInflater.inflate(R.menu.entrance, menu)
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