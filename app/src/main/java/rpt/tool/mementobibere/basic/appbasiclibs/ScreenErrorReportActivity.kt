package rpt.tool.mementobibere.basic.appbasiclibs

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import rpt.tool.mementobibere.R
import rpt.tool.mementobibere.basic.appbasiclibs.utils.ExceptionHandler


class ScreenErrorReportActivity : BaseActivity() {

    var error: TextView? = null
    var btn_cancel_error: Button? = null
    var btn_send_error: Button? = null


    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler(this))
        setContentView(R.layout.screen_error_report)

        mContext = this@ScreenErrorReportActivity

        error = findViewById<View>(R.id.error) as TextView
        btn_cancel_error = findViewById<Button>(R.id.btn_cancel_error)
        btn_send_error = findViewById<Button>(R.id.btn_send_error)

        error!!.text = getIntent().getStringExtra("error")

        val policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        btn_send_error!!.setOnClickListener(View.OnClickListener {
            try {
                val intent: Intent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "riccardo.pezzolati@gmail.com"))
                intent.putExtra(Intent.EXTRA_SUBJECT, "Android Bug Report")
                intent.putExtra(Intent.EXTRA_TEXT, "" + error!!.text.toString())
                startActivity(intent)
            } catch (ex: Exception) {
                Toast.makeText(this@ScreenErrorReportActivity, ex.toString(), Toast.LENGTH_LONG).show()
            }
        })

        btn_cancel_error!!.setOnClickListener(View.OnClickListener {
            AppClose.Companion.exitApplication(
                mContext!!
            )
        })
    }
}