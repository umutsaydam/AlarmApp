package com.umutsaydam.alarmapp.ui.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.umutsaydam.alarmapp.repository.AlarmRepository

class AlarmsViewModelFactory(
    private val context: Context,
    private val alarmRepository: AlarmRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AlarmViewModel(context, alarmRepository) as T
    }
}