package rpt.tool.mementobibere

import android.annotation.SuppressLint
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.widget.ImageView
import rpt.tool.mementobibere.basic.appbasiclibs.BaseAppCompatActivity
import rpt.tool.mementobibere.databinding.ActivitySplashScreenBinding
import rpt.tool.mementobibere.utils.AppUtils
import rpt.tool.mementobibere.utils.URLFactory
import rpt.tool.mementobibere.utils.managers.SharedPreferencesManager
import rpt.tool.mementobibere.widget.NewAppWidget


@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : BaseAppCompatActivity() {

    private var time: Long = SHOW
    private val timeoutHandler = Handler()
    private lateinit var binding : ActivitySplashScreenBinding
    private lateinit var sharedPref: SharedPreferences
    var handler: Handler? = null
    var runnable: Runnable? = null


    companion object {
        const val SHOW: Long = 75
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = getSharedPreferences(
            AppUtils.USERS_SHARED_PREF,
            AppUtils.PRIVATE_MODE)

        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        val finalizer = Runnable {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        timeoutHandler.postDelayed(finalizer, time)


        binding.layout.setOnClickListener{
            timeoutHandler.removeCallbacks(finalizer)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        val intent = Intent(act, NewAppWidget::class.java)
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
        val ids = AppWidgetManager.getInstance(act).getAppWidgetIds(
            ComponentName(
                act!!,
                NewAppWidget::class.java
            )
        )
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        act!!.sendBroadcast(intent)

    }

    @SuppressLint("UnsafeIntentLaunch")
    override fun onResume() {
        super.onResume()

        if (SharedPreferencesManager.dailyWater == 0f) {
            URLFactory.DAILY_WATER_VALUE = 2500f
        } else {
            URLFactory.DAILY_WATER_VALUE = SharedPreferencesManager.dailyWater
        }

        if (sh!!.check_blank_data("" + SharedPreferencesManager.waterUnit)) {
            URLFactory.WATER_UNIT_VALUE = "ml"
        } else {
            URLFactory.WATER_UNIT_VALUE = SharedPreferencesManager.waterUnit
        }

        runnable = Runnable {
            if (SharedPreferencesManager.hideWelcomeScreen) {
                intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
            } else {
                SharedPreferencesManager.weightUnit = true
                SharedPreferencesManager.personWeight = "80"
                SharedPreferencesManager.userName = ""
                intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
            }
            startActivity(intent)
            finish()
        }
        handler = Handler()
        handler!!.postDelayed(runnable!!, time)
    }
}