package com.umutsaydam.alarmapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.umutsaydam.alarmapp.repository.AlarmRepository

class AlarmsViewModelFactory(private val alarmRepository: AlarmRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AlarmViewModel(alarmRepository) as T
    }
}