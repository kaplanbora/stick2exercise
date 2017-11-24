package com.kaplanbora.stick2exercise.repository

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener


object FirebaseRepository {
    val db = FirebaseDatabase.getInstance().reference

    fun getAllRoutines(userId: Long) {
        val oneUsersRoutines = db.child("users").child("$userId").child("routines")

        oneUsersRoutines.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (singleSnapshot in dataSnapshot.children) {
                   val routine = Routine(
                            singleSnapshot.key.toLong(),
                            singleSnapshot.child("positon").value.toString().toInt(),
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