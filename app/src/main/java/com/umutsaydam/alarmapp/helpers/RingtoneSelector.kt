package com.umutsaydam.alarmapp.helpers

import android.app.Activity
import android.content.Intent
import android.media.RingtoneManager
import android.media.RingtoneManager.EXTRA_RINGTONE_PICKED_URI
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

class RingtoneSelector(fragment: Fragment) : IRingtoneSelector {
    private var currRingtoneUri: String? = null

    private val resultLauncher =
        fragment.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri: Uri? = result.data?.getParcelableExtra(EXTRA_RINGTONE_PICKED_URI)
                uri?.let {
                    currRingtoneUri = uri.toString()

                }
            }
        }

    override fun selectRingtone() {
        val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM)
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Alert Tone")
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, null as Uri?)
        resultLauncher.launch(intent)
    }

    override fun selectDefaultRingtone() {
        var alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        }
        currRingtoneUri = alarmUri.toString()
    }

    override fun currentRingtone(): String {
        if (currRingtoneUri == null) {
            selectDefaultRingtone()
        }
        return currRingtoneUri!!
    }
}
