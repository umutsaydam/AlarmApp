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
        fun bind(
            alarm: AlarmModel,
            onCheckedListener: SetCheckedListener,
            setClickListener: SetClickListener,
        ) {
            itemBinding.tvClockTime.text = alarm.alarmHourMinuteFormat
            itemBinding.tvClockTitle.text = alarm.alarmTitle
            itemBinding.switchAlarmToggle.isChecked = alarm.alarmEnabled
            setCardBackground(alarm.alarmEnabled)
            itemBinding.tvClockRepeat.text = convertToDays(alarm.alarmRepeat)
            setCheckedListener(alarm, onCheckedListener)
            setOnClickListener(alarm, setClickListener)
            setOnLongClickListener(alarm, setClickListener)
        }

        private fun convertToDays(alarmRepeat: List<Int>): String {
            val days: String = ConvertNumsToDays.convertNumsToDays(itemBinding.root.resources.getString(R.string.everyday), alarmRepeat).toString()
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

        private fun setCheckedListener(alarm: AlarmModel, onCheckedListener: SetCheckedListener) {
            itemBinding.switchAlarmToggle.setOnCheckedChangeListener { _, _ ->
                onCheckedListener.setOnCheckedListener(adapterPosition)
                setCardBackground(alarm.alarmEnabled)
            }
        }

        private fun setOnLongClickListener(
            alarmModel: AlarmModel,
            setClickListener: SetClickListener,
        ) {
            itemBinding.cvAlarm.setOnLongClickListener(View.OnLongClickListener {
                setClickListener.setOnLongClickListener(alarmModel)
                return@OnLongClickListener true
            })
        }

        private fun setOnClickListener(alarmModel: AlarmModel, setClickListener: SetClickListener) {
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
        holder.bind(differ.currentList[position], setOnCheckedListener, setClickListener)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}