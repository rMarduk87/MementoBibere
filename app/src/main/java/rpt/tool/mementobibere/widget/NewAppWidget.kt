package rpt.tool.mementobibere.widget

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.widget.RemoteViews
import rpt.tool.mementobibere.R
import rpt.tool.mementobibere.ScreenSelectBottleActivity
import rpt.tool.mementobibere.SplashScreenActivity
import rpt.tool.mementobibere.basic.appbasiclibs.utils.Constant
import rpt.tool.mementobibere.basic.appbasiclibs.utils.Date_Helper
import rpt.tool.mementobibere.utils.URLFactory
import rpt.tool.mementobibere.utils.extensions.equalsIgnoreCase
import rpt.tool.mementobibere.utils.managers.SharedPreferencesManager

/**
 * Implementation of App Widget functionality.
 */
@Suppress("DEPRECATED_IDENTITY_EQUALS")
class NewAppWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {

    }

    override fun onDisabled(context: Context) {
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        var mContext: Context? = null
        var drink_water: Float = 0f

        fun updateAppWidget(
            context: Context, appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            mContext = context
            val views = RemoteViews(context.packageName, R.layout.new_app_widget)
            views.setTextViewText(R.id.appwidget_text, get_today_report())

            views.setFloat(R.id.circularProgressbar, "setMax", URLFactory.DAILY_WATER_VALUE)
            views.setInt(R.id.circularProgressbar, "setProgress", drink_water.toInt())

            val launchMain: Intent = Intent(context, SplashScreenActivity::class.java)
            launchMain.putExtra("from_widget", true)
            launchMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            val pendingMainIntent: PendingIntent =
                PendingIntent.getActivity(context, 0, launchMain, PendingIntent.FLAG_IMMUTABLE)
            views.setOnClickPendingIntent(R.id.widget, pendingMainIntent)


            val launchMain2: Intent = Intent(context, ScreenSelectBottleActivity::class.java)
            launchMain2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            val pendingMainIntent2: PendingIntent =
                PendingIntent.getActivity(context, 0, launchMain2, PendingIntent.FLAG_IMMUTABLE)
            views.setOnClickPendingIntent(R.id.add_water, pendingMainIntent2)

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        @SuppressLint("WrongConstant")
        fun get_today_report(): String {
            Constant.SDB = mContext!!.openOrCreateDatabase(
                Constant.DATABASE_NAME,
                SQLiteDatabase.CREATE_IF_NECESSARY,
                null
            )

            val dth: Date_Helper = Date_Helper()

            val arr_data = getData(
                "tbl_drink_details",
                ("DrinkDate ='" + dth.getCurrentDate("dd-MM-yyyy")).toString() + "'"
            )

            if (SharedPreferencesManager.dailyWater === 0f) {
                URLFactory.DAILY_WATER_VALUE = 2500f
            } else {
                URLFactory.DAILY_WATER_VALUE = SharedPreferencesManager.dailyWater
            }

            if (check_blank_data("" + SharedPreferencesManager.waterUnit)) {
                URLFactory.WATER_UNIT_VALUE = "ML"
            } else {
                URLFactory.WATER_UNIT_VALUE = SharedPreferencesManager.waterUnit
            }

            drink_water = 0f
            for (k in arr_data.indices) {
                drink_water += if (URLFactory.WATER_UNIT_VALUE.equalsIgnoreCase("ml")){
                    arr_data[k]["ContainerValue"]!!.toFloat()
                } else{
                    arr_data[k]["ContainerValueOZ"]!!
                        .toDouble().toFloat()
                }
            }
            return "" + drink_water.toInt() + "/" + URLFactory.DAILY_WATER_VALUE + " " +
                    URLFactory.WATER_UNIT_VALUE
        }

        fun check_blank_data(data: String?): Boolean {
            return data == "" || data!!.isEmpty() || data.isEmpty() || data == "null"
        }

        @SuppressLint("Recycle")
        fun getData(table_name: String, where_con: String): ArrayList<HashMap<String, String>> {
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
    }
}

