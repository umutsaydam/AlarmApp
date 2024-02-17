package com.umutsaydam.alarmapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.umutsaydam.alarmapp.R
import com.umutsaydam.alarmapp.databinding.AlarmListItemBinding
import com.umutsaydam.alarmapp.models.AlarmModel
import com.umutsaydam.alarmapp.utils.SetCheckedListener

class AlarmAdapter(private val setOnCheckedListener: SetCheckedListener) :
    RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {
    private lateinit var binding: AlarmListItemBinding

    class AlarmViewHolder(private val itemBinding: AlarmListItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(alarm: AlarmModel) {
            itemBinding.tvClockTime.text = alarm.alarmTime.toString()
            itemBinding.tvClockTitle.text = alarm.alarmTitle
            itemBinding.switchAlarmToggle.isChecked = alarm.alarmEnabled
            setCardBackground(alarm.alarmEnabled)
        }

        private fun setCardBackground(alarmEnabled: Boolean) {
            itemBinding.cvAlarm.setCardBackgroundColor(
                itemBinding.root.resources.getColor(
                    if (alarmEnabled) R.color.alarm_enable
                    else R.color.alarm_disable
                )
            )
        }

        fun setCheckedListener(alarm: AlarmModel, onCheckedListener: SetCheckedListener) {
            itemBinding.switchAlarmToggle.setOnCheckedChangeListener { p0, p1 ->
                onCheckedListener.setOnCheckedListener(alarm)
                setCardBackground(alarm.alarmEnabled)
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<AlarmModel>() {
        override fun areItemsTheSame(oldItem: AlarmModel, newItem: AlarmModel): Boolean {
            return oldItem.alarmId == newItem.alarmId
        }

        override fun areContentsTheSame(oldItem: AlarmModel, newItem: AlarmModel): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        binding = AlarmListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlarmViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        holder.bind(differ.currentList[position])

        holder.setCheckedListener(differ.currentList[position], setOnCheckedListener)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}