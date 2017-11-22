package com.kaplanbora.stick2exercise.repository

class ExerciseRepository(val routine: Routine) {
    private val exercises = routine.exercises

    fun get(id: Long): Exercise = exercises.first { it.id == id }

    fun all(): MutableList<Exercise> {
        exercises.sortBy { it.position }
        return exercises
    }

    fun nextPosition(): Int = exercises.size + 1

    fun add(dbHelper: DbHelper, exercise: Exercise): Long {
        val id = ExerciseDatabase.insert(dbHelper, exercise, routine.id)
        exercise.id = id
        exercises.add(exercise)
        return id
    }

    fun restore(dbHelper: DbHelper, exercise: Exercise) {
        val shiftedExercises = exercises.filter { it.position >= exercise.position }
        add(dbHelper, exercise)
        shiftedExercises.forEach { it.position += 1 }
        shiftedExercises.forEach { ExerciseDatabase.update(dbHelper, it, routine.id) }
    }

    fun remove(dbHelper: DbHelper, exercise: Exercise) {
        ExerciseDatabase.delete(dbHelper, exercise.id)
        exercises.remove(exercise)
        val shiftedExercises = exercises.filter { it.position > exercise.position }
        shiftedExercises.forEach { it.position -= 1 }
        shiftedExercises.forEach { ExerciseDatabase.update(dbHelper, it, routine.id) }
    }

    fun update(dbHelper: DbHelper, exercise: Exercise) {
        ExerciseDatabase.update(dbHelper, exercise, routine.id)
    }
}
