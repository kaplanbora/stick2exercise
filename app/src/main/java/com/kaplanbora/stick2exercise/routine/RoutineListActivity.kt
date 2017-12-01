package com.kaplanbora.stick2exercise.routine

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
import com.kaplanbora.stick2exercise.MetronomeActivity
import com.kaplanbora.stick2exercise.MyLoginActivity
import com.kaplanbora.stick2exercise.exercise.ExerciseListActivity
import com.kaplanbora.stick2exercise.R
import com.kaplanbora.stick2exercise.SettingsActivity
import com.kaplanbora.stick2exercise.repository.DbHelper
import com.kaplanbora.stick2exercise.repository.FirebaseRepository
import com.kaplanbora.stick2exercise.repository.Routine
import com.kaplanbora.stick2exercise.repository.RoutineRepository
import kotlinx.android.synthetic.main.activity_routine_list.*
import kotlinx.android.synthetic.main.content_routine_list.*

class RoutineListActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, RoutineActionListener {
    private var dbHelper: DbHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_routine_list)
        setSupportActionBar(toolbar)

        dbHelper = DbHelper(applicationContext)
        refreshListView()
        routinesListView.setOnItemClickListener { adapterView, view, i, l ->
            val newintent = Intent(applicationContext, ExerciseListActivity::class.java)
            newintent.putExtra("routineId", l)
            newintent.putExtra("userId", intent.extras.getLong("userId"))
            startActivity(newintent)
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
        RoutineRepository.remove(dbHelper!!, routine)
        FirebaseRepository.deleteRoutine(routine, intent.extras.getLong("userId"))
        refreshListView()
        Snackbar.make(routinesListRoot, R.string.routine_delete_message, Snackbar.LENGTH_LONG)
                .setAction("UNDO", RestoreRoutine(intent.extras.getLong("userId"), this, routine, dbHelper!!))
                .show()
    }

    override fun editRoutine(routine: Routine) {
        val fragment = EditRoutineFragment()
        val bundle = Bundle()
        bundle.putString("routineName", routine.name)
        bundle.putLong("routineId", routine.id)
        fragment.arguments = bundle
        fragment.show(supportFragmentManager, "edit_routine")
    }

    override fun applyEdit(name: String, id: Long) {
        val routine = RoutineRepository.get(id)
        routine.name = name
        RoutineRepository.update(dbHelper!!, routine)
        refreshListView()
        FirebaseRepository.updateRoutine(routine, intent.extras.getLong("userId"))
    }

    override fun createRoutine(name: String) {
        val routine = Routine(-1, RoutineRepository.nextPosition(), name, mutableListOf())
        val id = RoutineRepository.add(dbHelper!!, routine)
        FirebaseRepository.addRoutine(routine, intent.extras.getLong("userId"))
        val newintent = Intent(applicationContext, ExerciseListActivity::class.java)
        newintent.putExtra("routineId", id)
        newintent.putExtra("userId", intent.extras.getLong("userId"))
        startActivity(intent)
    }

    override fun refreshListView() {
        if (RoutineRepository.all().isEmpty()) {
            emptyMessage.visibility = TextView.VISIBLE
        } else {
            emptyMessage.visibility = TextView.INVISIBLE
        }
        routinesListView.adapter = RoutineListAdapter(this, applicationContext, RoutineRepository.all())
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
        menuInflater.inflate(R.menu.routine_list_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                val intent = Intent(applicationContext, SettingsActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.create -> {
                CreateRoutineFragment().show(supportFragmentManager, "create_routine")
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_routine_list -> {}
            R.id.nav_metronome -> {
                val intent = Intent(applicationContext, MetronomeActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_settings -> {
                val intent = Intent(applicationContext, SettingsActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_logout -> {
                routinesListView.adapter = RoutineListAdapter(this, applicationContext, mutableListOf())
                val intent = Intent(applicationContext, MyLoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
