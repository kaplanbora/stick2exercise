package com.kaplanbora.stick2exercise

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.view.LayoutInflater
import android.widget.TextView


class RoutineListAdapter(val context: Context, val routineList: List<Routine>) : BaseAdapter() {

//    class ViewHolder(name: TextView, count: TextView) {
//        var routineName = name
//        var exerciseCount = count
//    }

    override fun getCount(): Int {
        return routineList.size
    }

    override fun getItem(position: Int): Any {
        return routineList[position]
    }

    override fun getItemId(position: Int): Long {
        return routineList[position].id
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val row = inflater.inflate(R.layout.routine_row, parent, false)
//        convertView.tag = ViewHolder(routineName, routineCount)
        val name = row.findViewById<TextView>(R.id.routineName)
        val count = row.findViewById<TextView>(R.id.routineCount)
        val routine = getItem(position) as Routine
        name.text = routine.name
        count.text = "${routine.exercises.size} exercises"
        return row
    }
}