package rpt.tool.mementobibere.utils.helpers

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import rpt.tool.mementobibere.MainActivity
import rpt.tool.mementobibere.R
import rpt.tool.mementobibere.utils.AppUtils
import java.util.*

class NotificationHelper(val ctx: Context) {
    private var notificationManager: NotificationManager? = null

    private val CHANNEL_ONE_ID = "rpt.tools.mementobibere.CHANNELONE"
    private val CHANNEL_ONE_NAME = "Channel One"


    private fun createChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val prefs = ctx.getSharedPreferences(AppUtils.USERS_SHARED_PREF, AppUtils.PRIVATE_MODE)
            val notificationsNewMessageRingtone = prefs.getString(
                AppUtils.NOTIFICATION_TONE_URI_KEY, RingtoneManager.getDefaultUri(
                    RingtoneManager.TYPE_NOTIFICATION
                ).toString()
            )
            val notificationChannel = NotificationChannel(
                CHANNEL_ONE_ID,
                CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.BLUE
            notificationChannel.setShowBadge(true)
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

            if (notificationsNewMessageRingtone!!.isNotEmpty()) {
                val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build()
                notificationChannel.setSound(Uri.parse(notificationsNewMessageRingtone), audioAttributes)
            }

            getManager()!!.createNotificationChannel(notificationChannel)
        }
    }

    @SuppressLint("RemoteViewLayout")
    fun getNotification(
        title: String,
        body: String,
        notificationsTone: String?
    ): NotificationCompat.Builder? {
        createChannels()
        val view = RemoteViews(ctx.packageName,R.layout.memento_bibere_notification_layout)
        view.setTextViewText(R.id.title,title)
        view.setTextViewText(R.id.text, body)
        val notification = NotificationCompat.Builder(ctx.applicationContext, CHANNEL_ONE_ID)
            .setCustomContentView(view)
            .setAutoCancel(true)
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    ctx.resources,
                    R.mipmap.ic_launcher
                )
            )
            .setSmallIcon(R.drawable.ic_small_logo)

        notification.setShowWhen(true)

        notification.setSound(Uri.parse(notificationsTone))

        notification.setContent(view)

        val notificationIntent = Intent(ctx, MainActivity::class.java)

        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val contentIntent =
            PendingIntent.getActivity(ctx, 99, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

        notification.setContentIntent(contentIntent)

        return notification
    }

    private fun shallNotify(): Boolean {
        val prefs = ctx.getSharedPreferences(AppUtils.USERS_SHARED_PREF, AppUtils.PRIVATE_MODE)
        val sqliteHelper = SqliteHelper(ctx)

        val startTimestamp = prefs.getLong(AppUtils.START_TIME_KEY, prefs.getLong(AppUtils.WAKEUP_TIME_KEY,0))
        val stopTimestamp = prefs.getLong(AppUtils.STOP_TIME_KEY, prefs.getLong(AppUtils.SLEEPING_TIME_KEY, 0))
        val totalIntake = prefs.getFloat(AppUtils.TOTAL_INTAKE_KEY, 0f)

        if (startTimestamp == 0L || stopTimestamp == 0L || totalIntake == 0f)
            return false

        val percent = sqliteHelper.getIntook(AppUtils.getCurrentOnlyDate()!!) * 100 / totalIntake

        val now = Calendar.getInstance().time

        val start = Date(startTimestamp)
        val stop = Date(stopTimestamp)

        val passedSeconds = compareTimes(now, start)
        val totalSeconds = compareTimes(stop, start)

        // percentage which should have been consumed by now:
        val currentTarget = passedSeconds.toFloat() / totalSeconds.toFloat() * 100f

        val doNotDisturbOff = passedSeconds >= 0 && compareTimes(now, stop) <= 0

        val notify = doNotDisturbOff && (percent < currentTarget)
        Log.i("MementoBibere",
            "notify: $notify, dndOff: $doNotDisturbOff, " +
                    "currentTarget: $currentTarget, percent: $percent"
        )
        return notify
    }


    private fun compareTimes(currentTime: Date, timeToRun: Date): Long {
        val currentCal = Calendar.getInstance()
        currentCal.time = currentTime

        val runCal = Calendar.getInstance()
        runCal.time = timeToRun
        runCal.set(Calendar.DAY_OF_MONTH, currentCal.get(Calendar.DAY_OF_MONTH))
        runCal.set(Calendar.MONTH, currentCal.get(Calendar.MONTH))
        runCal.set(Calendar.YEAR, currentCal.get(Calendar.YEAR))

        return currentCal.timeInMillis - runCal.timeInMillis
    }

    fun notify(id: Long, notification: NotificationCompat.Builder?) {
        if (shallNotify()) {
            getManager()!!.notify(id.toInt(), notification!!.build())
        }
    }

    private fun getManager(): NotificationManager? {
        if (notificationManager == null) {
            notificationManager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
        return notificationManager
    }
}
