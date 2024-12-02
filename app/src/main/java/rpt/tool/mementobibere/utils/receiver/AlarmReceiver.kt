package rpt.tool.mementobibere.utils.receiver

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import rpt.tool.mementobibere.utils.helpers.AlarmHelper
import rpt.tool.mementobibere.utils.helpers.NotificationHelper
import rpt.tool.mementobibere.utils.log.d
import java.util.Calendar
import java.util.Locale

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        d(DEBUG_TAG, "Recurring alarm; requesting download service.")

        WakeLocker.acquire(context)
        WakeLocker.release()

        //Toast.makeText(context,"Call",1).show();
        val action = "" + intent.action

        if (action.equals("SNOOZE_ACTION", ignoreCase = true)) {
            //Log.d(DEBUG_TAG, "IF");

            val snoozeIntent = Intent(context, AlarmReceiver::class.java)
            val snoozePendingIntent = PendingIntent.getBroadcast(context, 0, snoozeIntent,
                PendingIntent.FLAG_IMMUTABLE)
            val alarms = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarms.setExact(
                AlarmManager.RTC_WAKEUP,
                Calendar.getInstance(Locale.getDefault()).timeInMillis + 2 * 60000,
                snoozePendingIntent
            )

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(0)
        } else if (action == "android.intent.action.BOOT_COMPLETED") {
            //Toast.makeText(context,"BOOT_COMPLETED",1).show();

            val alarmHelper = AlarmHelper(context)
            alarmHelper.createAlarm()
        } else {
            //Log.d(DEBUG_TAG, "ELSE");

            val notificationHelper = NotificationHelper(context)
            notificationHelper.createNotification()
        }

        /*if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
            alarm.SetAlarm(context);
        }*/
    }

    companion object {
        private const val DEBUG_TAG = "AlarmReceiver"
    }
}