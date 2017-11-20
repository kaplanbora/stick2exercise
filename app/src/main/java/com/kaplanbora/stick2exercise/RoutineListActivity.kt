package com.kaplanbora.stick2exercise

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
import com.kaplanbora.stick2exercise.repository.Routine
import com.kaplanbora.stick2exercise.repository.RoutineRepo
import kotlinx.android.synthetic.main.activity_routine_list.*
import kotlinx.android.synthetic.main.content_routine_list.*

class RoutineListActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, RoutineActionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routine_list)
        setSupportActionBar(toolbar)

        refreshRoutines()
        routinesListView.setOnItemClickListener { adapterView, view, i, l ->
            val intent = Intent(applicationContext, ExerciseListActivity::class.java)
            intent.putExtra("routineId", l)
            startActivity(intent)
        }

        fab.setOnClickListener { _ ->
            CreateRoutineFragment().show(supportFragmentManager, "create_routine")
        }


        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun deleteRoutine(routine: Routine) {
        RoutineRepo.delete(routine)
        refreshRoutines()
        Snackbar
                .make(routinesListRoot, R.string.routine_delete_message, Snackbar.LENGTH_LONG)
                .setAction("UNDO", RestoreRoutine(this, routine))
                .show()
    }

    override fun editRoutine(routine: Routine) {
        val fragment = EditRoutineFragment()
        val bundle = Bundle()
        bundle.putLong("routineId", routine.id)
        fragment.arguments = bundle
        fragment.show(supportFragmentManager, "edit_routine")
    }

    override fun onResume() {
        super.onResume()
        refreshRoutines()
    }

    override fun refreshRoutines() {
        if (RoutineRepo.getList().isEmpty()) {
            emptyMessage.visibility = TextView.VISIBLE
        } else {
            emptyMessage.visibility = TextView.INVISIBLE
        }
        routinesListView.adapter = RoutineListAdapter(this, applicationContext, RoutineRepo.getList())
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
