package com.kaplanbora.stick2exercise

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.kaplanbora.stick2exercise.repository.FirebaseRepository
import com.kaplanbora.stick2exercise.repository.User
import com.kaplanbora.stick2exercise.routine.RoutineListActivity
import kotlinx.android.synthetic.main.activity_my_login.*

class MyLoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_login)
        val users = FirebaseRepository.getUsers()

        login.setOnClickListener { _ ->
            val emailInput = email.text.toString()
            val passwordInput = password.text.toString()
            val success = users.any { it.email == emailInput && it.password == passwordInput }
            if (success) {
                val userId = users.first { it.email == emailInput }.id
                FirebaseRepository.getAllRoutines(userId)
                Thread.sleep(200)
                val intent = Intent(applicationContext, RoutineListActivity::class.java)
                intent.putExtra("userId", userId)
                startActivity(intent)
            } else {
                Toast.makeText(applicationContext, getString(R.string.incorrect_login), Toast.LENGTH_LONG).show()
            }
        }

        register.setOnClickListener { _ ->
            val emailInput = email.text.toString()
            val passwordInput = password.text.toString()
            if (users.any { it.email == emailInput }) {
                email.error = getString(R.string.email_used)
            } else {
                val newUser = User(users.size + 1L, emailInput, passwordInput)
                FirebaseRepository.addUser(newUser)
                val intent = Intent(applicationContext, RoutineListActivity::class.java)
                intent.putExtra("userId", newUser.id)
                startActivity(intent)
            }
        }

    }
}
