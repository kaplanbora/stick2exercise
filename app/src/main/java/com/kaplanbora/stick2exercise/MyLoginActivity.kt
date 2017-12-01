package com.kaplanbora.stick2exercise

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.kaplanbora.stick2exercise.repository.FirebaseRepository
import com.kaplanbora.stick2exercise.repository.Repository
import com.kaplanbora.stick2exercise.repository.User
import com.kaplanbora.stick2exercise.routine.RoutineListActivity
import kotlinx.android.synthetic.main.activity_my_login.*
import java.net.InetAddress
import java.net.UnknownHostException

class MyLoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_login)

//        if (isConnected()) {
//            Repository.mode = InternetMode.ONLINE
//        } else {
//            Log.d("CONNECTION_MODE", "No internet connection detected. Switching to offline mode.")
//            Repository.mode = InternetMode.OFFLINE
//        }

        val users = Repository.getUsers()

        login.setOnClickListener { _ ->
            if (areFieldsEmpty()) {
                return@setOnClickListener
            }
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
            if (areFieldsEmpty()) {
                return@setOnClickListener
            }
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

    fun isConnected(): Boolean {
        try {
            val result = InetAddress.getByName("www.google.com")
            return !result.equals("")
        } catch (e: UnknownHostException) {
            return false;
        }
    }

    fun areFieldsEmpty(): Boolean {
        if (email.text.toString() == "") {
            Toast.makeText(applicationContext, "Email cannot be empty.", Toast.LENGTH_LONG).show()
            return true
        } else if (password.text.toString() == "") {
            Toast.makeText(applicationContext, "Password cannot be empty.", Toast.LENGTH_LONG).show()
            return true
        }
        return false
    }
}
