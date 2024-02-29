package com.umutsaydam.alarmapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.umutsaydam.alarmapp.models.AlarmModel
import com.umutsaydam.alarmapp.utils.Converters

@Database(
    entities = [AlarmModel::class],
    version =7,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AlarmDatabase : RoomDatabase() {
    abstract fun getAlarmDao(): AlarmDao

    companion object {
        @Volatile
        private var instance: AlarmDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            AlarmDatabase::class.java,
            "alarms.db"
        ).fallbackToDestructiveMigration().build()
    }
}