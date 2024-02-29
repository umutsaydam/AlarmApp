package com.umutsaydam.alarmapp.helpers

import android.content.Context
import android.media.RingtoneManager
import android.net.Uri

class RingtonePlayer(private val context: Context, private val ringtoneUri: String) :
    IRingtonePlayer {
    private val ringtone = RingtoneManager.getRingtone(context, Uri.parse(ringtoneUri))

    override fun playRingtone() {
        if (!ringtone.isPlaying)
            ringtone.play()
    }

    override fun stopRingtone() {
        if (ringtone.isPlaying)
            ringtone.stop()
    }
}