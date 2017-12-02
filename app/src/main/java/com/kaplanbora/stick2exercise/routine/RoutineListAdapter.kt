package com.kaplanbora.stick2exercise.routine

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.*
import com.kaplanbora.stick2exercise.R
import com.kaplanbora.stick2exercise.repository.Routine
import kotlinx.android.synthetic.main.routine_row.view.*

class RoutineListAdapter(private val listener: RoutineActionListener, val context: Context, private val routineList: List<Routine>) : BaseAdapter() {
    override fun getCount(): Int = routineList.size

    override fun getItem(position: Int): Any = routineList.first { it.position == position + 1 }

    override fun getItemId(position: Int): Long = routineList.first { it.position == position + 1 }.id

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val row = inflater.inflate(R.layout.routine_row, parent, false)
        val routine = getItem(position) as Routine
        val popup = PopupMenu(context, row.routineMenu)
        popup.menuInflater.inflate(R.menu.routine_menu, popup.menu)
        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.routineMenuDelete -> listener.deleteRoutine(routine)
                R.id.routineMenuEdit -> listener.editRoutine(routine)
            }
            true
        }
        row.routineMenu.setOnClickListener { _ ->
            popup.show()
        }
        row.routineName.text = routine.name
        if (routine.exercises.isEmpty()) {
            row.routineCount.text = context.getString(R.string.empty_list)
            row.totalDuration.text = ""
        } else {
            var minuteDuration = routine.exercises.foldRight(0, { exercise, acc ->
                acc + exercise.playDuration.minutes + exercise.breakDuration.minutes
            })
            var secondDuration = routine.exercises.foldRight(0, { exercise, acc ->
                acc + exercise.playDuration.seconds + exercise.breakDuration.seconds
            })
            minuteDuration += secondDuration / 60
            secondDuration %= 60
            row.routineCount.text = "${context.getString(R.string.exercise_sayı)} ${routine.exercises.size}"
            row.totalDuration.text = "${context.getString(R.string.total_duration)} $minuteDuration ${context.getString(R.string.minutes)} $secondDuration ${context.getString(R.string.seconds)}"
        }
        return row
    }
}
