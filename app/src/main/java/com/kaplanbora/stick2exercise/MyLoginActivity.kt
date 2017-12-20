package com.kaplanbora.stick2exercise

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.kaplanbora.stick2exercise.routine.RoutineListActivity
import kotlinx.android.synthetic.main.activity_my_login.*
import android.net.ConnectivityManager
import com.kaplanbora.stick2exercise.repository.*


class MyLoginActivity : AppCompatActivity() {
    var dbHelper: DbHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_login)

        dbHelper = DbHelper(applicationContext)

        if (isConnected()) {
            FirebaseRepository.loadUsers()
        } else {
            UserDatabase.loadUsers(dbHelper!!)
        }

        RoutineRepository.routines.clear()

        login.setOnClickListener { _ ->
            if (areFieldsEmpty()) {
                return@setOnClickListener
            }
            val emailInput = email.text.toString()
            val passwordInput = password.text.toString()
            val success = Repository.users.any { it.email == emailInput && it.password == passwordInput }
            if (success) {
                val userId = Repository.users.first { it.email == emailInput }.id
                if (isConnected()) {
                    RoutineRepository.routines = FirebaseRepository.getAllRoutines(userId)
                } else {
                    RoutineRepository.userId = userId
                    RoutineRepository.load(dbHelper!!)
                }
                Thread.sleep(1000)
                if (RoutineRepository.routines.isEmpty()) {
                    RoutineRepository.userId = userId
                    RoutineRepository.load(dbHelper!!)
                }
                val intent = Intent(applicationContext, RoutineListActivity::class.java)
                intent.putExtra("userId", userId)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            } else {
                Toast.makeText(applicationContext, getString(R.string.incorrect_login), Toast.LENGTH_LONG).show()
            }
        }

        register.setOnClickListener { _ ->
            if (areFieldsEmpty() || !isConnected()) {
                return@setOnClickListener
            }
            val emailInput = email.text.toString()
            val passwordInput = password.text.toString()
            if (Repository.users.any { it.email == emailInput }) {
                email.error = getString(R.string.email_used)
            } else {
                val newUser = User(-1, emailInput, passwordInput)
                UserDatabase.add(dbHelper!!, newUser)
                FirebaseRepository.addUser(newUser)
                val intent = Intent(applicationContext, RoutineListActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                intent.putExtra("userId", newUser.id)
                startActivity(intent)
            }
        }
    }

    private fun isConnected(): Boolean {
        val connectivity = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivity.activeNetworkInfo != null
    }

    private fun areFieldsEmpty(): Boolean {
        if (email.text.toString() == "") {
            Toast.makeText(applicationContext, "Email cannot be empty.", Toast.LENGTH_LONG).show()
            return true
        } else if (password.text.toString() == "") {
            Toast.makeText(applicationContext, "Password cannot be empty.", Toast.LENGTH_LONG).show()
            return true
        }
        return false
    }

    override fun onDestroy() {
        dbHelper?.close()
        super.onDestroy()
    }
}
