package com.umutsaydam.alarmapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.umutsaydam.alarmapp.R
import com.umutsaydam.alarmapp.models.RepeatDaysItemModel

class RepeatAdapter(private val days: List<RepeatDaysItemModel>) :
    RecyclerView.Adapter<RepeatAdapter.RepeatHolder>() {

    class RepeatHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var checkBox: CheckBox = itemView.findViewById(R.id.cbRepeatDay)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepeatHolder {
        return RepeatHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.repeat_list_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return days.size
    }

    override fun onBindViewHolder(holder: RepeatHolder, position: Int) {
        val day = days[position]
        holder.checkBox.text = day.checkBoxTitle
    }
}