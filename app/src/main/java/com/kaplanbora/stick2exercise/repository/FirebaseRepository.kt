package com.kaplanbora.stick2exercise.repository

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener


object FirebaseRepository {
    val db = FirebaseDatabase.getInstance().reference

    fun restore(routine: Routine, userId: Long) {
        addRoutine(routine, userId)
        val toupdate = RoutineRepository.routines.filter { it.position > routine.position }
        toupdate.forEach { updateRoutine(it, userId) }
    }

    fun deleteRoutine(routine: Routine, userId: Long) {
        val routinePath = db.child("users").child("$userId").child("routines").child("${routine.id}")
        routinePath.removeValue()
        val toupdate = RoutineRepository.routines.filter { it.position >= routine.position }
        toupdate.forEach { updateRoutine(it, userId) }
    }

    fun addRoutine(routine: Routine, userId: Long) {
        val routinePath = db.child("users").child("$userId").child("routines").child("${routine.id}")
        routinePath.child("name").setValue(routine.name)
        routinePath.child("position").setValue("${routine.position}")
    }

    fun updateRoutine(routine: Routine, userId: Long) {
        val routinePath = db.child("users").child("$userId").child("routines").child("${routine.id}")
        routinePath.child("name").setValue(routine.name)
        routinePath.child("position").setValue("${routine.position}")
    }

    fun addUser(user: User) {
        Log.d("FIREBASE_ADD_USER", user.toString())
        val userPath = db.child("users").child("${user.id}")
        userPath.child("email").setValue(user.email)
        userPath.child("password").setValue(user.password)
        userPath.child("routines").setValue(null)
    }

    fun getUsers(): MutableList<User> {
        val users = db.child("users")
        val list: MutableList<User> = mutableListOf()
        users.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    list.add(User(
                            snapshot.key.toLong(),
                            snapshot.child("email").value.toString(),
                            snapshot.child("password").value.toString()
                    ))
                }
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })
        return list
    }

    fun getAllRoutines(userId: Long) {
        val oneUsersRoutines = db.child("users").child("$userId").child("routines")

        oneUsersRoutines.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (singleSnapshot in dataSnapshot.children) {
                    val routine = Routine(
                            singleSnapshot.key.toLong(),
                            singleSnapshot.child("position").value.toString().toInt(),
                            singleSnapshot.child("name").value.toString()
                    )
                    for (snapshot in singleSnapshot.child("exercises").children) {
                        routine.exercises.add(
                                Exercise(
                                        snapshot.key.toLong(),
                                        snapshot.child("position").value.toString().toInt(),
                                        snapshot.child("name").value.toString(),
                                        Metronome(
                                                snapshot.child("tempo").value.toString().toInt(),
                                                snapshot.child("subdivUp").value.toString().toInt(),
                                                snapshot.child("subdivDown").value.toString().toInt()
                                        ),
                                        PlayDuration(
                                                snapshot.child("playMinutes").value.toString().toInt(),
                                                snapshot.child("playSeconds").value.toString().toInt()
                                        ),
                                        BreakDuration(
                                                snapshot.child("breakMinutes").value.toString().toInt(),
                                                snapshot.child("breakSeconds").value.toString().toInt()
                                        )
                                )
                        )
                    }
                    RoutineRepository.routines.add(routine)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // ...
            }
        })
    }
}