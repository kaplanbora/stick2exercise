package com.kaplanbora.stick2exercise

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.Toast
import com.kaplanbora.stick2exercise.routine.RoutineListActivity
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.content_settings.*

class SettingsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(toolbar)

        val prefs = this.getPreferences(Context.MODE_PRIVATE)
        val editor = prefs.edit()

        val minutes = (0..60).map { it.toString() }.toTypedArray()
        val seconds = (0..50 step 10).map { it.toString() }.toTypedArray()
        minutePicker.minValue = 0
        minutePicker.maxValue = minutes.size - 1
        minutePicker.wrapSelectorWheel = true
        minutePicker.displayedValues = minutes
        minutePicker.value = prefs.getInt("minutePicker", 0)

        secondPicker.minValue = 0
        secondPicker.maxValue = seconds.size - 1
        secondPicker.wrapSelectorWheel = true
        secondPicker.displayedValues = seconds
        secondPicker.value = prefs.getInt("secondPicker", 0)

        val soundPopup = PopupMenu(applicationContext, metronomeSound)
        soundPopup.menuInflater.inflate(R.menu.metronome_sounds, soundPopup.menu)
        soundPopup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.soundBeep -> metronomeSound.text = "Beep"
                R.id.soundClick -> metronomeSound.text = "Click"
            }
            true
        }

        metronomeSound.text = prefs.getString("metronomeSound", "Beep")
        metronomeSound.setOnClickListener { _ ->
            soundPopup.show()
        }

        screenSwitch.isChecked = prefs.getBoolean("screenSwitch", false)
        autoSwitch.isChecked = prefs.getBoolean("autoSwitch", true)
        countInSwitch.isChecked = prefs.getBoolean("countInSwitch", false)

        saveButton.setOnClickListener { _ ->
            editor.putString("metronomeSound", metronomeSound.text.toString())
            editor.putBoolean("autoSwitch", autoSwitch.isChecked)
            editor.putBoolean("countInSwitch", countInSwitch.isChecked)
            editor.putInt("minutePicker", minutePicker.value)
            editor.putInt("secondPicker", secondPicker.value)
            editor.apply()
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
            R.id.nav_logout -> {
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
