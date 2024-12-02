package rpt.tool.mementobibere.utils.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import rpt.tool.mementobibere.basic.appbasiclibs.utils.Preferences_Helper
import rpt.tool.mementobibere.utils.URLFactory
import rpt.tool.mementobibere.utils.helpers.BackupHelper
import rpt.tool.mementobibere.utils.log.d
import java.util.Calendar
import java.util.Locale

class BackupReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        d(DEBUG_TAG, "Recurring alarm 2; requesting download service.")

        val action = "" + intent.action

        if (action == "android.intent.action.BOOT_COMPLETED") {
            val ph: Preferences_Helper = Preferences_Helper(context)

            if (ph.getBoolean(URLFactory.AUTO_BACK_UP)) {
                val _id = System.currentTimeMillis().toInt()
                val auto_backup_time = Calendar.getInstance(Locale.getDefault())
                auto_backup_time[Calendar.HOUR_OF_DAY] = 1
                auto_backup_time[Calendar.MINUTE] = 0
                auto_backup_time[Calendar.SECOND] = 0
                auto_backup_time[Calendar.MILLISECOND] = 0

                ph.savePreferences(URLFactory.AUTO_BACK_UP_ID, _id)

                if (ph.getInt(URLFactory.AUTO_BACK_UP_TYPE) === 0) {
                    MyAlarmManager.scheduleAutoBackupAlarm(context, auto_backup_time, _id, 0)
                } else if (ph.getInt(URLFactory.AUTO_BACK_UP_TYPE) === 1) {
                    MyAlarmManager.scheduleAutoBackupAlarm(context, auto_backup_time, _id, 1)
                } else if (ph.getInt(URLFactory.AUTO_BACK_UP_TYPE) === 2) {
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