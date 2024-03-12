package rpt.tool.mementobibere

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import rpt.tool.mementobibere.databinding.ActivitySplashScreenBinding
import rpt.tool.mementobibere.utils.AppUtils

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private var time: Long = ANIMATION_TIME
    private val timeoutHandler = Handler()
    private lateinit var binding : ActivitySplashScreenBinding

    companion object {

        const val ANIMATION_TIME: Long = 750
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        val finalizer = Runnable {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        timeoutHandler.postDelayed(finalizer, time)


        binding.drawerLayout.setOnClickListener{
            timeoutHandler.removeCallbacks(finalizer)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }
}