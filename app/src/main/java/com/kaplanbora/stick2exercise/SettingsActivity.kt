package com.kaplanbora.stick2exercise

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.Toast
import com.kaplanbora.stick2exercise.repository.DbHelper
import com.kaplanbora.stick2exercise.repository.Repository
import com.kaplanbora.stick2exercise.routine.RoutineListActivity
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.content_settings.*

class SettingsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var dbHelper: DbHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(toolbar)

        dbHelper = DbHelper(applicationContext)
        val settings = Repository.loadSettings(dbHelper!!)

        val minutes = (0..60).map { it.toString() }.toTypedArray()
        val seconds = (0..50 step 10).map { it.toString() }.toTypedArray()
        minutePicker.minValue = 0
        minutePicker.maxValue = minutes.size - 1
        minutePicker.wrapSelectorWheel = true
        minutePicker.displayedValues = minutes
        minutePicker.value = settings.defaultMinute

        secondPicker.minValue = 0
        secondPicker.maxValue = seconds.size - 1
        secondPicker.wrapSelectorWheel = true
        secondPicker.displayedValues = seconds
        secondPicker.value = settings.defaultSecond

        val soundPopup = PopupMenu(applicationContext, metronomeSound)
        soundPopup.menuInflater.inflate(R.menu.metronome_sounds, soundPopup.menu)
        soundPopup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.soundBeep -> metronomeSound.text = "Beep"
                R.id.soundClick -> metronomeSound.text = "Click"
            }
            true
        }

        metronomeSound.text = settings.metronomeSound
        metronomeSound.setOnClickListener { _ ->
            soundPopup.show()
        }

        screenSwitch.isChecked = settings.screenOn
        autoSwitch.isChecked = settings.autoSwitch
        countInSwitch.isChecked = settings.countInSwitch

        saveButton.setOnClickListener { _ ->
            settings.metronomeSound = metronomeSound.text.toString()
            settings.autoSwitch = autoSwitch.isChecked
            settings.countInSwitch = countInSwitch.isChecked
            settings.defaultMinute = minutePicker.value
            settings.defaultSecond = secondPicker.value
            Repository.saveSettings(dbHelper!!)
            Toast.makeText(applicationContext, getString(R.string.saved), Toast.LENGTH_SHORT).show()
            finish()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_routine_list -> {
                val intent = Intent(applicationContext, RoutineListActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.nav_metronome -> {
                val intent = Intent(applicationContext, MetronomeActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.nav_settings -> {
            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


}
