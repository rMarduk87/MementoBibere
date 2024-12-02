package rpt.tool.mementobibere

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RelativeLayout
import rpt.tool.mementobibere.basic.appbasiclibs.BaseActivity
import rpt.tool.mementobibere.databinding.ActivitySelectSnoozeBinding
import rpt.tool.mementobibere.utils.receiver.AlarmReceiver
import java.util.Calendar
import java.util.Locale


class SelectSnoozeActivity : BaseActivity() {

    private lateinit var binding : ActivitySelectSnoozeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectSnoozeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setLayout(
            RelativeLayout.LayoutParams.FILL_PARENT,
            RelativeLayout.LayoutParams.FILL_PARENT
        )

        val notificationManager: NotificationManager =
            act!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(0)

        body()
    }


    private fun body() {
        binding.one.text = "5 " + sh!!.get_string(R.string.str_minutes)
        binding.two.text = "10 " + sh!!.get_string(R.string.str_minutes)
        binding.three.text = "15 " + sh!!.get_string(R.string.str_minutes)

        binding.one.setOnClickListener{
            setSnooze(5)
            finish()
        }

        binding.two.setOnClickListener{
            setSnooze(10)
            finish()
        }

        binding.three.setOnClickListener{
            setSnooze(15)
            finish()
        }
    }

    private fun setSnooze(minutes: Int) {
        val snoozeIntent: Intent = Intent(act, AlarmReceiver::class.java)
        val snoozePendingIntent: PendingIntent = PendingIntent.getBroadcast(act, 0, snoozeIntent,
            PendingIntent.FLAG_IMMUTABLE)
        val alarms: AlarmManager = act!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarms.setExact(
            AlarmManager.RTC_WAKEUP, Calendar.getInstance(
                Locale.getDefault()

            ).timeInMillis + minutes * 60000, snoozePendingIntent
        )
    }
}