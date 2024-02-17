package com.umutsaydam.alarmapp.db

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.umutsaydam.alarmapp.models.AlarmModel

@Dao
interface AlarmDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(alarm: AlarmModel): Long

    @Query("SELECT * FROM alarms")
    fun getAllAlarms(): LiveData<List<AlarmModel>>

    @Query("SELECT * FROM alarms WHERE alarmId = :alarmId")
    fun getSingleAlarm(alarmId: Int): LiveData<AlarmModel>

    @Delete
    suspend fun deleteAlarm(alarmModel: AlarmModel)
}
