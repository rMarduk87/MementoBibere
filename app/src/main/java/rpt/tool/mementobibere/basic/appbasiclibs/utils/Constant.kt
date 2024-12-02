package rpt.tool.mementobibere.basic.appbasiclibs.utils

import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.database.sqlite.SQLiteDatabase
import rpt.tool.mementobibere.basic.appbasiclibs.BaseActivity

object Constant : BaseActivity() {
    const val DEVELOPER_MODE: Boolean = true

    // DATABASE
    var SDB: SQLiteDatabase? = null
    const val DATABASE_NAME: String = "rpt_hydratate.db"

    // custom share
    var share_purchase_title: String = "Share To"
    var launchables: List<ResolveInfo?>? = null
    var pm: PackageManager? = null
    var launchables_sel: MutableList<ResolveInfo?>? = null

    const val general_share_title: String = "Share"

    //FOR INTENT
    const val PICK_CONTACT: Int = 1000

    const val no_internet_message: String = "No Internet Connection!!!"

    const val youTubeUrlRegEx: String = "^(https?)?(://)?(www.)?(m.)?((youtube.com)|(youtu.be))/"
    val videoIdRegex: Array<String> = arrayOf(
        "\\?vi?=([^&]*)",
        "watch\\?.*v=([^&]*)",
        "(?:embed|vi?)/([^/?]*)",
        "^([A-Za-z0-9\\-]*)"
    )
}