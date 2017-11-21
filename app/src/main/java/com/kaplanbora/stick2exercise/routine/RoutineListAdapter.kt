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
        row.routineCount.text = "${routine.exercises.size} exercises"
        return row
    }
}