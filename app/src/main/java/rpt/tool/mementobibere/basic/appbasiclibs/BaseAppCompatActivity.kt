package rpt.tool.mementobibere.basic.appbasiclibs

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import rpt.tool.mementobibere.R
import rpt.tool.mementobibere.basic.appbasiclibs.utils.*
import rpt.tool.mementobibere.utils.helpers.DB_Helper

open class BaseAppCompatActivity : AppCompatActivity() {
    var mContext: Context? = null
    var act: Activity? = null

    var uf: Utility_Function? = null
    var ah: Alert_Helper? = null
    var jh: Json_Helper? = null
    var bh: Bitmap_Helper? = null
    var dth: Date_Helper? = null
    var dh: Database_Helper? = null
    var mah: Map_Helper? = null
    var sh: String_Helper? = null
    var ph: Preferences_Helper? = null
    var zh: Zip_Helper? = null
    var ih: Intent_Helper? = null
    var dbh: DB_Helper? = null

    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mContext = this@BaseAppCompatActivity
        act = this@BaseAppCompatActivity

        Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler(act!!))

        uf = Utility_Function(mContext!!, act!!)
        ah = Alert_Helper(mContext!!)
        jh = Json_Helper(mContext!!)
        bh = Bitmap_Helper(mContext!!)
        dth = Date_Helper()
        dh = Database_Helper(mContext!!, act!!)
        ih = Intent_Helper(mContext!!, act!!)
        mah = Map_Helper()
        sh = String_Helper(mContext!!, act!!)
        ph = Preferences_Helper(mContext!!, act)
        zh = Zip_Helper(mContext!!)
        dbh = DB_Helper(mContext!!, act!!)

        uf!!.permission_StrictMode()
    }

    companion object {
        fun getThemeColor(ctx: Context): Int {
            return ctx.resources.getColor(R.color.colorPrimaryDark)
        }

        fun getThemeColorArray(ctx: Context?): IntArray {
            val colors = intArrayOf(
                Color.parseColor("#001455da"),
                Color.parseColor("#FF1455da"))

            return colors
        }
    }
}
