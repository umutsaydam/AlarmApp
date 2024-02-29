package com.umutsaydam.alarmapp.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "alarms")
@TypeConverters
data class AlarmModel(
    @PrimaryKey(autoGenerate = true)
    var alarmId: Int,
    val alarmTitle: String? = "",
    var alarmTime: Long = 0,
    var alarmRepeat: List<Int> = listOf(),
    var alarmEnabled: Boolean = true,
    var alarmVibrating: Boolean = false,
    val alarmRingtoneUri: String? = "",
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readLong(),
        parcel.createIntArray()?.toList() ?: listOf<Int>(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(alarmId)
        parcel.writeString(alarmTitle)
        parcel.writeLong(alarmTime)
        parcel.writeList(alarmRepeat)
        parcel.writeByte(if (alarmEnabled) 1 else 0)
        parcel.writeByte(if (alarmVibrating) 1 else 0)
        parcel.writeString(alarmRingtoneUri)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AlarmModel> {
        override fun createFromParcel(parcel: Parcel): AlarmModel {
            return AlarmModel(parcel)
        }

        override fun newArray(size: Int): Array<AlarmModel?> {
            return arrayOfNulls(size)
        }
    }
}