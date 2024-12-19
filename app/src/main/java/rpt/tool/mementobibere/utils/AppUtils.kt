package rpt.tool.mementobibere.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
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

        fun convertUpperCase(appCompatTextView: AppCompatTextView) {
            appCompatTextView.text =
                appCompatTextView.text.toString().uppercase(Locale.getDefault())
        }

        fun getHeightFeetElements(): MutableList<Double>? {
            val height_feet_elements: MutableList<Double> = ArrayList()
            height_feet_elements.add(2.0)
            height_feet_elements.add(2.1)
            height_feet_elements.add(2.2)
            height_feet_elements.add(2.3)
            height_feet_elements.add(2.4)
            height_feet_elements.add(2.5)
            height_feet_elements.add(2.6)
            height_feet_elements.add(2.7)
            height_feet_elements.add(2.8)
            height_feet_elements.add(2.9)
            height_feet_elements.add(2.10)
            height_feet_elements.add(2.11)
            height_feet_elements.add(3.0)
            height_feet_elements.add(3.1)
            height_feet_elements.add(3.2)
            height_feet_elements.add(3.3)
            height_feet_elements.add(3.4)
            height_feet_elements.add(3.5)
            height_feet_elements.add(3.6)
            height_feet_elements.add(3.7)
            height_feet_elements.add(3.8)
            height_feet_elements.add(3.9)
            height_feet_elements.add(3.10)
            height_feet_elements.add(3.11)
            height_feet_elements.add(4.0)
            height_feet_elements.add(4.1)
            height_feet_elements.add(4.2)
            height_feet_elements.add(4.3)
            height_feet_elements.add(4.4)
            height_feet_elements.add(4.5)
            height_feet_elements.add(4.6)
            height_feet_elements.add(4.7)
            height_feet_elements.add(4.8)
            height_feet_elements.add(4.9)
            height_feet_elements.add(4.10)
            height_feet_elements.add(4.11)
            height_feet_elements.add(5.0)
            height_feet_elements.add(5.1)
            height_feet_elements.add(5.2)
            height_feet_elements.add(5.3)
            height_feet_elements.add(5.4)
            height_feet_elements.add(5.5)
            height_feet_elements.add(5.6)
            height_feet_elements.add(5.7)
            height_feet_elements.add(5.8)
            height_feet_elements.add(5.9)
            height_feet_elements.add(5.10)
            height_feet_elements.add(5.11)
            height_feet_elements.add(6.0)
            height_feet_elements.add(6.1)
            height_feet_elements.add(6.2)
            height_feet_elements.add(6.3)
            height_feet_elements.add(6.4)
            height_feet_elements.add(6.5)
            height_feet_elements.add(6.6)
            height_feet_elements.add(6.7)
            height_feet_elements.add(6.8)
            height_feet_elements.add(6.9)
            height_feet_elements.add(6.10)
            height_feet_elements.add(6.11)
            height_feet_elements.add(7.0)
            height_feet_elements.add(7.1)
            height_feet_elements.add(7.2)
            height_feet_elements.add(7.3)
            height_feet_elements.add(7.4)
            height_feet_elements.add(7.5)
            height_feet_elements.add(7.6)
            height_feet_elements.add(7.7)
            height_feet_elements.add(7.8)
            height_feet_elements.add(7.9)
            height_feet_elements.add(7.10)
            height_feet_elements.add(7.11)
            height_feet_elements.add(8.0)
            return height_feet_elements
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
        const val databaseName = "lets_hydrate_app_database.db"
    }
}

