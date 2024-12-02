package rpt.tool.mementobibere.utils.helpers

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import rpt.tool.mementobibere.MainActivity
import rpt.tool.mementobibere.R
import rpt.tool.mementobibere.ScreenSelectBottleActivity
import rpt.tool.mementobibere.SelectSnoozeActivity
import rpt.tool.mementobibere.basic.appbasiclibs.utils.Constant
import rpt.tool.mementobibere.basic.appbasiclibs.utils.Date_Helper
import rpt.tool.mementobibere.basic.appbasiclibs.utils.Preferences_Helper
import rpt.tool.mementobibere.utils.URLFactory
import rpt.tool.mementobibere.utils.extensions.equalsIgnoreCase
import rpt.tool.mementobibere.utils.log.d

internal class NotificationHelper(private val mContext: Context) {
    var dth: Date_Helper = Date_Helper()
    var ph: Preferences_Helper = Preferences_Helper(mContext)

    init {
        if (URLFactory.notification_ringtone == null) URLFactory.notification_ringtone =
            RingtoneManager.getRingtone(mContext, sound)
    }

    fun createNotification() {
        d("createNotification", "" + ph.getInt(URLFactory.REMINDER_OPTION))
        d("createNotification V", "" + ph.getBoolean(URLFactory.REMINDER_VIBRATE))



        if (ph.getInt(URLFactory.REMINDER_OPTION) === 1) return

        if (reachedDailyGoal() && ph.getBoolean(URLFactory.DISABLE_NOTIFICATION)) return


        val intent: Intent = Intent(mContext, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)

        val resultPendingIntent: PendingIntent =
            PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val snoozeIntent: Intent = Intent(mContext, SelectSnoozeActivity::class.java)
        snoozeIntent.setAction("SNOOZE_ACTION")
        snoozeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val snoozePendingIntent: PendingIntent =
            PendingIntent.getActivity(mContext, 0, snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val addWaterIntent: Intent = Intent(mContext, ScreenSelectBottleActivity::class.java)
        addWaterIntent.setAction("ADD_WATER_ACTION")
        addWaterIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val addWaterPendingIntent: PendingIntent = PendingIntent.getActivity(
            mContext,
            0,
            addWaterIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )


        val mBuilder = NotificationCompat.Builder(mContext)
        mBuilder.setSmallIcon(R.drawable.ic_small_app_icon)
        mBuilder.setLargeIcon(
            BitmapFactory.decodeResource(
                mContext.resources,
                R.mipmap.ic_launcher
            )
        )
        mBuilder.setContentTitle(mContext.resources.getString(R.string.str_drink_water))
            .setContentText("" + get_today_report())
            .setAutoCancel(true)
            .setContentIntent(resultPendingIntent)
            .setColor(ContextCompat.getColor(mContext, R.color.colorPrimary))

        if (ph.getInt(URLFactory.REMINDER_OPTION) === 0 && !ph.getBoolean(URLFactory.REMINDER_VIBRATE)) {
            mBuilder.setDefaults(Notification.DEFAULT_ALL)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                mBuilder.setSound(sound)
            }
        } else if (ph.getInt(URLFactory.REMINDER_OPTION) === 0 && ph.getBoolean(URLFactory.REMINDER_VIBRATE)) {
            mBuilder.setDefaults(Notification.DEFAULT_SOUND)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                mBuilder.setSound(sound)
            }
        } else if (ph.getInt(URLFactory.REMINDER_OPTION) === 2 && !ph.getBoolean(URLFactory.REMINDER_VIBRATE)) {
            mBuilder.setDefaults(Notification.DEFAULT_VIBRATE)
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                mBuilder.setSound(null)
            }
        } else {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                mBuilder.setSound(null)
            }
        }

        mBuilder.addAction(
            R.drawable.ic_plus,
            mContext.resources.getString(R.string.str_add_water),
            addWaterPendingIntent
        )
        mBuilder.addAction(
            R.drawable.ic_notification,
            mContext.resources.getString(R.string.str_snooze),
            snoozePendingIntent
        )


        //mBuilder.addAction(replyAction);
        val mNotificationManager: NotificationManager =
            mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationManager.deleteNotificationChannel(NOTIFICATION_CHANNEL_ID)
            mNotificationManager.deleteNotificationChannel(NOTIFICATION_VIBRATE_CHANNEL_ID)

            if (ph.getInt(URLFactory.REMINDER_OPTION) === 0) {
                if (!ph.getBoolean(URLFactory.REMINDER_VIBRATE)) {
                    val importance: Int = NotificationManager.IMPORTANCE_HIGH
                    val notificationChannel: NotificationChannel = NotificationChannel(
                        NOTIFICATION_CHANNEL_ID, "Reminder", importance
                    )
                    notificationChannel.enableLights(true)
                    notificationChannel.lightColor = Color.RED
                    notificationChannel.setSound(null, null)
                    notificationChannel.enableVibration(true)
                    notificationChannel.vibrationPattern = longArrayOf(
                        100,
                        200,
                        300,
                        400,
                        500,
                        400,
                        300,
                        200,
                        400
                    )


                    checkNotNull(mNotificationManager)
                    mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID)


                    mNotificationManager.createNotificationChannel(notificationChannel)
                } else {
                    val importance: Int = NotificationManager.IMPORTANCE_HIGH
                    val notificationChannel: NotificationChannel = NotificationChannel(
                        NOTIFICATION_VIBRATE_CHANNEL_ID, "Vibrate Reminder", importance
                    )
                    notificationChannel.enableLights(true)
                    notificationChannel.setSound(null, null)
                    notificationChannel.lightColor = Color.RED
                    notificationChannel.enableVibration(false)
                    notificationChannel.vibrationPattern = longArrayOf(0)


                    checkNotNull(mNotificationManager)
                    mBuilder.setChannelId(NOTIFICATION_VIBRATE_CHANNEL_ID)

                    mNotificationManager.createNotificationChannel(notificationChannel)
                }

                try {
                    if (!URLFactory.notification_ringtone!!.isPlaying){
                        URLFactory.notification_ringtone!!.play()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                if (!ph.getBoolean(URLFactory.REMINDER_VIBRATE)) {
                    val channel_none: NotificationChannel = NotificationChannel(
                        NOTIFICATION_SILENT_CHANNEL_ID,
                        "Silent Reminder",
                        NotificationManager.IMPORTANCE_HIGH
                    )
                    channel_none.setSound(null, null)
                    channel_none.enableVibration(true)
                    channel_none.vibrationPattern = longArrayOf(
                        100,
                        200,
                        300,
                        400,
                        500,
                        400,
                        300,
                        200,
                        400
                    )

                    checkNotNull(mNotificationManager)
                    mBuilder.setChannelId(NOTIFICATION_SILENT_CHANNEL_ID)
                    mNotificationManager.createNotificationChannel(channel_none)
                } else {
                    val channel_none: NotificationChannel = NotificationChannel(
                        NOTIFICATION_SILENT_VIBRATE_CHANNEL_ID,
                        "Silent-Vibrate Reminder",
                        NotificationManager.IMPORTANCE_HIGH
                    )
                    channel_none.setSound(null, null)
                    channel_none.enableVibration(false)
                    channel_none.vibrationPattern = longArrayOf(0)
                    checkNotNull(mNotificationManager)
                    mBuilder.setChannelId(NOTIFICATION_SILENT_VIBRATE_CHANNEL_ID)
                    mNotificationManager.createNotificationChannel(channel_none)
                }
            }
        }
        checkNotNull(mNotificationManager)
        mNotificationManager.notify(0,  /* Request Code */mBuilder.build())
    }

    val sound: Uri
        get() {
            var uri = Settings.System.DEFAULT_NOTIFICATION_URI

            Log.d("getSound", "" + ph.getInt(URLFactory.REMINDER_SOUND))

            if (ph.getInt(URLFactory.REMINDER_SOUND) === 1) uri =
                Uri.parse("android.resource://" + mContext.packageName + "/" + R.raw.bell)
            else if (ph.getInt(URLFactory.REMINDER_SOUND) === 2) uri =
                Uri.parse("android.resource://" + mContext.packageName + "/" + R.raw.blop)
            else if (ph.getInt(URLFactory.REMINDER_SOUND) === 3) uri =
                Uri.parse("android.resource://" + mContext.packageName + "/" + R.raw.bong)
            else if (ph.getInt(URLFactory.REMINDER_SOUND) === 4) uri =
                Uri.parse("android.resource://" + mContext.packageName + "/" + R.raw.click)
            else if (ph.getInt(URLFactory.REMINDER_SOUND) === 5) uri =
                Uri.parse("android.resource://" + mContext.packageName + "/" + R.raw.echo_droplet)
            else if (ph.getInt(URLFactory.REMINDER_SOUND) === 6) uri =
                Uri.parse("android.resource://" + mContext.packageName + "/" + R.raw.mario_droplet)
            else if (ph.getInt(URLFactory.REMINDER_SOUND) === 7) uri =
                Uri.parse("android.resource://" + mContext.packageName + "/" + R.raw.ship_bell)
            else if (ph.getInt(URLFactory.REMINDER_SOUND) === 8) uri =
                Uri.parse("android.resource://" + mContext.packageName + "/" + R.raw.simple_droplet)
            else if (ph.getInt(URLFactory.REMINDER_SOUND) === 9) uri =
                Uri.parse("android.resource://" + mContext.packageName + "/" + R.raw.tiny_droplet)

            return uri
        }

    fun reachedDailyGoal(): Boolean {
        Constant.SDB = mContext.openOrCreateDatabase(
            Constant.DATABASE_NAME,
            Context.MODE_PRIVATE,
            null
        )

        if (ph.getFloat(URLFactory.DAILY_WATER) === 0f) {
            URLFactory.DAILY_WATER_VALUE = 2500f
        } else {
            URLFactory.DAILY_WATER_VALUE = ph.getFloat(URLFactory.DAILY_WATER)
        }

        val arr_data = getdata(
            "tbl_drink_details",
            ("DrinkDate ='" + dth.getCurrentDate("dd-MM-yyyy")).toString() + "'"
        )

        var drink_water = 0f
        for (k in arr_data.indices) {
            drink_water += if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")){
                arr_data[k]["ContainerValue"]!!.toDouble().toFloat()
            } else{
                arr_data[k]["ContainerValueOZ"]!!.toDouble().toFloat()
            }
        }

        return if (drink_water >= URLFactory.DAILY_WATER_VALUE) true
        else false
    }

    @SuppressLint("WrongConstant")
    fun get_today_report(): String {
        Constant.SDB = mContext.openOrCreateDatabase(
            Constant.DATABASE_NAME,
            SQLiteDatabase.CREATE_IF_NECESSARY,
            null
        )

        if (ph.getFloat(URLFactory.DAILY_WATER) === 0f) {
            URLFactory.DAILY_WATER_VALUE = 2500f
        } else {
            URLFactory.DAILY_WATER_VALUE = ph.getFloat(URLFactory.DAILY_WATER)
        }

        if (check_blank_data("" + ph.getString(URLFactory.WATER_UNIT))) {
            URLFactory.WATER_UNIT_VALUE = "ML"
        } else {
            URLFactory.WATER_UNIT_VALUE = ph.getString(URLFactory.WATER_UNIT)!!
        }

        val arr_data = getdata(
            "tbl_drink_details",
            ("DrinkDate ='" + dth.getCurrentDate("dd-MM-yyyy")).toString() + "'"
        )

        var drink_water = 0f
        for (k in arr_data.indices) {
            drink_water += if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")){
                arr_data[k]["ContainerValue"]!!.toDouble().toFloat()
            } else{
                arr_data[k]["ContainerValueOZ"]!!.toDouble().toFloat()
            }
        }

        return mContext.resources.getString(R.string.str_have_u_had_any_water_yet)
    }

    fun check_blank_data(data: String?): Boolean {
        return data == "" || data!!.isEmpty() || data.isEmpty() || data == "null" || data == null
    }

    fun getdata(table_name: String, where_con: String): ArrayList<HashMap<String, String>> {
        val maplist = ArrayList<HashMap<String, String>>()

        var query = "SELECT * FROM $table_name"

        query += " where $where_con"

        val c: Cursor = Constant.SDB!!.rawQuery(query, null)

        println("SELECT QUERY : $query")

        if (c.moveToFirst()) {
            do {
                val map = HashMap<String, String>()
                for (i in 0 until c.columnCount) {
                    map[c.getColumnName(i)] = c.getString(i)
                }

                maplist.add(map)
            } while (c.moveToNext())
        }

        return maplist
    }

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "10001"
        private const val NOTIFICATION_SILENT_CHANNEL_ID = "10002"
        private const val NOTIFICATION_VIBRATE_CHANNEL_ID = "10003"
        private const val NOTIFICATION_SILENT_VIBRATE_CHANNEL_ID = "10004"
    }
}