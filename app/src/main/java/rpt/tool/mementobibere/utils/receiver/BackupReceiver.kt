package rpt.tool.mementobibere.utils.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import rpt.tool.mementobibere.utils.helpers.BackupHelper
import rpt.tool.mementobibere.utils.log.d
import rpt.tool.mementobibere.utils.managers.SharedPreferencesManager
import java.util.Calendar
import java.util.Locale

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class BackupReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        d(DEBUG_TAG, "Recurring alarm 2; requesting download service.")

        val action = "" + intent.action

        if (action == "android.intent.action.BOOT_COMPLETED") {

            if (SharedPreferencesManager.autoBackUp) {
                val _id = System.currentTimeMillis().toInt()
                val auto_backup_time = Calendar.getInstance(Locale.getDefault())
                auto_backup_time[Calendar.HOUR_OF_DAY] = 1
                auto_backup_time[Calendar.MINUTE] = 0
                auto_backup_time[Calendar.SECOND] = 0
                auto_backup_time[Calendar.MILLISECOND] = 0
                
                SharedPreferencesManager.autoBackUpId = _id

                if (SharedPreferencesManager.autoBackUpType === 0) {
                    MyAlarmManager.scheduleAutoBackupAlarm(context, auto_backup_time, _id, 0)
                } else if (SharedPreferencesManager.autoBackUpType === 1) {
                    MyAlarmManager.scheduleAutoBackupAlarm(context, auto_backup_time, _id, 1)
                } else if (SharedPreferencesManager.autoBackUpType === 2) {
                    MyAlarmManager.scheduleAutoBackupAlarm(context, auto_backup_time, _id, 2)
                }
            }
        } else {
            val backupHelper = BackupHelper(context)
            backupHelper.createAutoBackSetup()
        }
    }

    companion object {
        private const val DEBUG_TAG = "AlarmReceiver"
    }
}