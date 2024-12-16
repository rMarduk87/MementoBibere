package rpt.tool.mementobibere.basic.appbasiclibs

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import rpt.tool.mementobibere.basic.appbasiclibs.utils.Alert_Helper
import rpt.tool.mementobibere.basic.appbasiclibs.utils.Bitmap_Helper
import rpt.tool.mementobibere.basic.appbasiclibs.utils.Database_Helper
import rpt.tool.mementobibere.basic.appbasiclibs.utils.Date_Helper
import rpt.tool.mementobibere.basic.appbasiclibs.utils.ExceptionHandler
import rpt.tool.mementobibere.basic.appbasiclibs.utils.Intent_Helper
import rpt.tool.mementobibere.basic.appbasiclibs.utils.Json_Helper
import rpt.tool.mementobibere.basic.appbasiclibs.utils.Map_Helper
import rpt.tool.mementobibere.basic.appbasiclibs.utils.String_Helper
import rpt.tool.mementobibere.basic.appbasiclibs.utils.Utility_Function
import rpt.tool.mementobibere.basic.appbasiclibs.utils.Zip_Helper
import rpt.tool.mementobibere.utils.helpers.DB_Helper

open class BaseActivity : Activity() {
    var mContext: Context? = null
    var act: Activity? = null

    var uf: Utility_Function? = null
    var ah: Alert_Helper? = null
    var jh: Json_Helper? = null
    var bh: Bitmap_Helper? = null
    var dth: Date_Helper? = null
    var dh: Database_Helper? = null
    var mah: Map_Helper? = null
    @JvmField
    var sh: String_Helper? = null
    var zh: Zip_Helper? = null
    var ih: Intent_Helper? = null
    var dbh: DB_Helper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mContext = this@BaseActivity
        act = this@BaseActivity

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
        zh = Zip_Helper(mContext!!)

        uf!!.permission_StrictMode()

        dbh = DB_Helper(mContext!!, act!!)
    }
}
