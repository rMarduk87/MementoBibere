package rpt.tool.mementobibere.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import java.text.SimpleDateFormat
import java.util.Calendar
import kotlin.math.ceil


class AppUtils {
    companion object {


        @SuppressLint("SimpleDateFormat")
        fun getCurrentOnlyDate(): String? {
            val c = Calendar.getInstance().time
            val df = SimpleDateFormat("dd-MM-yyyy")
            return df.format(c)
        }

        fun mlToOzUK(ml: Float) : Float{
            return ml  / 28.413f
        }

        fun ozUSToOzUK(oz: Float): Float{
            return oz * 1.041f
        }

        fun ozUKToMl(oz: Float) : Float{
            return ceil(oz * 28.413f)
        }

        @SuppressLint("SimpleDateFormat")
        fun getCurrentDate(): String? {
            val c = Calendar.getInstance().time
            val df = SimpleDateFormat("dd-MM-yyyy")
            return df.format(c)
        }


        fun getMinDate(): Long {
            val calendarTodayMinOne = Calendar.getInstance()
            calendarTodayMinOne.add(Calendar.DAY_OF_MONTH, 1)
            return calendarTodayMinOne.timeInMillis
        }

        fun getData(str: String): String {
            return str.replace(",", ".")
        }

        fun getApplicationName(context: Context): String {
            val applicationInfo = context.applicationInfo
            val stringId = applicationInfo.labelRes
            return if (stringId == 0) applicationInfo.nonLocalizedLabel.toString() else context.getString(
                stringId
            )
        }

        fun View.gone() {
            this.visibility = View.GONE
        }

        fun View.visible() {
            this.visibility = View.VISIBLE
        }


        val PRIVATE_MODE = 0
        const val THEME_KEY: String = "theme"
        const val USERS_SHARED_PREF = "user_pref"
        const val WEIGHT_KEY = "weight"
        const val WORK_TIME_KEY = "worktime"
        const val TOTAL_INTAKE_KEY = "totalintake"
        const val NOTIFICATION_STATUS_KEY = "notificationstatus"
        const val NOTIFICATION_FREQUENCY_KEY = "notificationfrequency"
        const val SLEEPING_TIME_KEY = "sleepingtime"
        const val WAKEUP_TIME_KEY = "wakeuptime"
        const val FIRST_RUN_KEY = "firstrun"
        const val UNIT_STRING = "unit_string"
        const val WEIGHT_UNIT_KEY = "weight_unit"
        const val SET_WEIGHT_UNIT = "set_weight_unit"
        const val GENDER_KEY : String = "gender"
        const val SET_GENDER_KEY : String = "set_gender"
        const val BLOOD_DONOR_KEY : String = "blood_donor"
        const val SET_BLOOD_KEY : String = "set_blood_donor"
        const val SET_NEW_WORK_TYPE_KEY : String = "set_new_work_type"
        const val CLIMATE_KEY : String = "climate"
        const val SET_CLIMATE_KEY : String = "set_climate"
        const val DATE : String = "date"
    }
}

