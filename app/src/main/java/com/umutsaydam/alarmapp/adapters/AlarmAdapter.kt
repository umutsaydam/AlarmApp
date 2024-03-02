package com.umutsaydam.alarmapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.umutsaydam.alarmapp.R
import com.umutsaydam.alarmapp.databinding.AlarmListItemBinding
import com.umutsaydam.alarmapp.models.AlarmModel
import com.umutsaydam.alarmapp.utils.ConvertNumsToDays
import com.umutsaydam.alarmapp.utils.SetCheckedListener
import com.umutsaydam.alarmapp.utils.SetClickListener

class AlarmAdapter(
    private val setOnCheckedListener: SetCheckedListener,
    private val setClickListener: SetClickListener,
) :
    RecyclerView.Adapter<AlarmAdapter.AlarmViewHolder>() {
    private lateinit var binding: AlarmListItemBinding

    class AlarmViewHolder(private val itemBinding: AlarmListItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(alarm: AlarmModel) {
            itemBinding.tvClockTime.text = alarm.alarmTime.toString()
            itemBinding.tvClockTitle.text = alarm.alarmTitle
            itemBinding.switchAlarmToggle.isChecked = alarm.alarmEnabled
            setCardBackground(alarm.alarmEnabled)
            itemBinding.tvClockRepeat.text = convertToDays(alarm.alarmRepeat)
        }

        private fun convertToDays(alarmRepeat: List<Int>): String {
            val days: String = ConvertNumsToDays.convertNumsToDays(alarmRepeat).toString()
            return days.replace(Regex("[\\[\\]]"), "")
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

        fun setOnLongClickListener(alarmModel: AlarmModel, setClickListener: SetClickListener) {
            itemBinding.cvAlarm.setOnLongClickListener(View.OnLongClickListener {
                setClickListener.setOnLongClickListener(alarmModel)
                return@OnLongClickListener true
            })
        }

        fun setOnClickListener(alarmModel: AlarmModel, setClickListener: SetClickListener){
            itemBinding.cvAlarm.setOnClickListener {
                setClickListener.setOnClickListener(alarmModel)
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
        holder.setOnLongClickListener(differ.currentList[position], setClickListener)
        holder.setOnClickListener(differ.currentList[position], setClickListener)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}