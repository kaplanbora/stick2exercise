package com.kaplanbora.stick2exercise

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.routine_row.view.*

data class Routine(var name: String, val exercises: MutableList<Exercise>)

class RoutineAdapter(val routines: List<Routine>) : RecyclerView.Adapter<RoutineAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(routine: Routine) {
            itemView.routineName.text = routine.name
            itemView.routineCount.text = "${routine.exercises.size} exercises"
        }
    }

    override fun getItemCount(): Int {
        return routines.size
    }

    override fun onBindViewHolder(holder: RoutineAdapter.ViewHolder, position: Int) {
        holder.bindItems(routines[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RoutineAdapter.ViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.routine_row, parent, false)
        return ViewHolder(view)
    }

}
