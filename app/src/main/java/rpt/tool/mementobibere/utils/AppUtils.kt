package rpt.tool.mementobibere.utils

import android.annotation.SuppressLint
import android.net.ParseException
import rpt.tool.mementobibere.migration.data.models.MonthChartModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import kotlin.math.ceil


class AppUtils {
    companion object {


        @SuppressLint("SimpleDateFormat")
        fun getCurrentOnlyDate(): String? {
            val c = Calendar.getInstance().time
            val df = SimpleDateFormat("dd-MM-yyyy")
            return df.format(c)
        }

        fun mlToOzUS(ml: Float): Float {
            return ml / 29.574f
        }

        fun mlToOzUK(ml: Float) : Float{
            return ml  / 28.413f
        }

        fun ozUSToozUK(oz: Float): Float{
            return oz * 1.041f
        }

        fun ozUSToMl(oz: Float): Float{
            return ceil(oz * 29.574f)
        }

        fun ozUKToMl(oz: Float) : Float{
            return ceil(oz * 28.413f)
        }

        fun ozUKToOzUS(oz: Float): Float{
            return oz / 1.041f
        }

        fun lblToKg(w: Int): Int {
            return (w/2.205).toInt()
        }

        fun kgToLbl(w: Int): Int {
            return (w*2.205).toInt()
        }

        fun calculateExtensions(newUnitint: Int): String {
            when(newUnitint)
            {
                0-> return "ml"
                1-> return "0z UK"
                2-> return "0z US"
            }
            return "ml"
        }


        fun firstConversion(value: Float, unit: Int): Float {
            var converted = value
            when(unit){
                1-> converted =
                    mlToOzUK(value)
                2-> converted =
                    mlToOzUS(value)
            }
            return converted
        }

        fun extractIntConversion(value: String?): Int {
            when(value)
            {
                "ml" -> return 0
                "0z UK" -> return 1
                "0z US" -> return 2
            }
            return 0
        }

        fun isValidDate(wakeupTime: String, sleepingTime: String): Boolean {

            var calendarStringW = wakeupTime.split(":")
            var calendarStringS = sleepingTime.split(":")

            val calendarWake = Calendar.getInstance()
            calendarWake.set(2023,9,27,calendarStringW[0].toInt(),calendarStringW[1].toInt())

            val calendarSleep = Calendar.getInstance()
            calendarSleep.set(2023,9,27,calendarStringS[0].toInt(),calendarStringS[1].toInt())

            return !isSameDateTime(
                calendarWake,
                calendarSleep
            ) &&
                    !isCalendar2MajorOfCalendar(
                        calendarWake,
                        calendarSleep
                    )
        }

        private fun isSameDateTime(cal1: Calendar, cal2: Calendar): Boolean {
            // compare if is the same ERA, YEAR, DAY, HOUR, MINUTE and SECOND
            return cal1[Calendar.HOUR_OF_DAY] == cal2[Calendar.HOUR_OF_DAY] &&
                    cal1[Calendar.MINUTE] == cal2[Calendar.MINUTE]
        }

        private fun isCalendar2MajorOfCalendar(cal1: Calendar, cal2: Calendar): Boolean {
            // compare if is the same ERA, YEAR, DAY, HOUR, MINUTE and SECOND
            return cal2[Calendar.HOUR_OF_DAY] > cal1[Calendar.HOUR_OF_DAY] &&
                            cal2[Calendar.MINUTE] > cal1[Calendar.MINUTE]
        }

        fun getMaxWeight(weightUnit: Int): Int {
            when(weightUnit){
                0-> return 200
                1-> return 441
            }
            return 200
        }

        fun getMinWeight(weightUnit: Int): Int {
            when(weightUnit){
                0-> return 20
                1-> return 44
            }
            return 20
        }

        fun getCurrentDate(): String? {
            val c = Calendar.getInstance().time
            val df = SimpleDateFormat("dd-MM-yyyy")
            return df.format(c)
        }

        fun getMaxDate(): Long {
            var calendarTodayMinOne = Calendar.getInstance()
            calendarTodayMinOne.add(Calendar.DAY_OF_MONTH, -1)
            return calendarTodayMinOne.timeInMillis
        }

        fun getMinDate(): Long {
            var calendarTodayMinOne = Calendar.getInstance()
            calendarTodayMinOne.add(Calendar.DAY_OF_MONTH, 1)
            return calendarTodayMinOne.timeInMillis
        }

        fun convertToSelected(selectedOption: Float, unit: String): Float {
            when(extractIntConversion(unit)){
                0-> return extractSelection(
                    selectedOption
                )
                1-> return extractSelection(
                    ozUKToMl(
                        selectedOption
                    )
                )
                2-> return extractSelection(
                    ozUSToMl(
                        selectedOption
                    )
                )
            }
            return selectedOption
        }

        private fun extractSelection(selectedOption: Float): Float {
            return when(selectedOption){
                50f->0f
                100f->1f
                150f->2f
                200f->3f
                250f->4f
                300f->5f
                350f->6f
                else->7f
            }
        }

        fun calculateOption(inTook: Float, totalIntake: Float): Float? {
            return totalIntake - inTook
        }

        @Throws(ParseException::class)
        fun getDateList(strStartDate: String?, strEndDate: String?, formatOutput: String):
                List<MonthChartModel>? {

            val dateList: MutableList<MonthChartModel> = ArrayList()
            val inputFormatter: DateFormat = SimpleDateFormat("dd-MM-yyyy")
            val outputFormatterIndex = SimpleDateFormat("dd-MM-yyyy")
            val outputFormatter: DateFormat = SimpleDateFormat(formatOutput)

            val startDate: Date = inputFormatter.parse(strStartDate)
            val endDate: Date = inputFormatter.parse(strEndDate)

            val startWith = Calendar.getInstance()
            startWith.time = startDate
            startWith[Calendar.DAY_OF_MONTH] = 1
            while (startWith.time.time <= endDate.time) {
                var dataForOutputIndex = outputFormatterIndex.format(startWith.time)
                var dataForOutputText = outputFormatter.format(startWith.time)
                dateList.add(MonthChartModel(dataForOutputIndex,dataForOutputText))
                startWith
                    .add(Calendar.MONTH, 1)
            }
            return dateList
        }

        fun getDateListForYear(strStartDate: String?, strEndDate: String?, formatOutput: String):
                List<String>? {

            val dateList: MutableList<String> = ArrayList()
            val inputFormatter: DateFormat = SimpleDateFormat("dd-MM-yyyy")
            val outputFormatter: DateFormat = SimpleDateFormat(formatOutput)

            val startDate: Date = inputFormatter.parse(strStartDate)
            val endDate: Date = inputFormatter.parse(strEndDate)

            val startWith = Calendar.getInstance()
            startWith.time = startDate
            startWith[Calendar.DAY_OF_MONTH] = 1
            while (startWith.time.time <= endDate.time) {
                var dataForOutput = outputFormatter.format(startWith.time)
                if(!dateList.contains(dataForOutput)){
                    dateList.add(dataForOutput)
                }
                startWith
                    .add(Calendar.MONTH, 1)
            }
            return dateList
        }

        fun getWeekList(strStartDate: String): List<String> {

            val now = Calendar.getInstance()

            val format = SimpleDateFormat("dd-MM-yyyy")
            val startDate: Date = format.parse(strStartDate)

            now.time = startDate

            val dateList: MutableList<String> = ArrayList()
            val delta = -now[Calendar.DAY_OF_WEEK] + 1 //add 2 if your week start on monday

            now.add(Calendar.DAY_OF_MONTH, delta)
            for (i in 0..6) {
                dateList.add( format.format(now.time))
                now.add(Calendar.DAY_OF_MONTH, 1)
            }

            return dateList
        }

        fun getTotalDays(currentDate: String, year: String): Int {
            val cal = GregorianCalendar()
            var feb = if(cal.isLeapYear(year.toInt())){
                29
            }
            else{
                28
            }
            when(currentDate.toInt()){
                1-> return 31
                2-> return feb
                3-> return 31
                4-> return 30
                5-> return 31
                6-> return 30
                7-> return 31
                8-> return 31
                9-> return 30
                10-> return 31
                11-> return 30
                12-> return 31
            }
            return 0
        }

        val PRIVATE_MODE = 0

        const val UNIT_KEY: String = "current_unit"
        const val UNIT_NEW_KEY: String = "new_unit"
        const val THEME_KEY: String = "theme"
        const val USERS_SHARED_PREF = "user_pref"
        const val WEIGHT_KEY = "weight"
        const val WORK_TIME_KEY = "worktime"
        const val TOTAL_INTAKE_KEY = "totalintake"
        const val NOTIFICATION_STATUS_KEY = "notificationstatus"
        const val NOTIFICATION_FREQUENCY_KEY = "notificationfrequency"
        const val NOTIFICATION_MSG_KEY = "notificationmsg"
        const val SLEEPING_TIME_KEY = "sleepingtime"
        const val WAKEUP_TIME_KEY = "wakeuptime"
        const val NOTIFICATION_TONE_URI_KEY = "notificationtone"
        const val FIRST_RUN_KEY = "firstrun"
        const val VALUE_50_KEY = "50"
        const val VALUE_100_KEY = "100"
        const val VALUE_150_KEY = "150"
        const val VALUE_200_KEY = "200"
        const val VALUE_250_KEY = "250"
        const val VALUE_300_KEY = "300"
        const val VALUE_350_KEY = "350"
        const val NO_UPDATE_UNIT = "no_update_unit"
        const val UNIT_STRING = "unit_string"
        const val WEIGHT_UNIT_KEY = "weight_unit"
        const val SET_WEIGHT_UNIT = "set_weight_unit"
        const val RESET_NOTIFICATION_KEY: String = "reset_notification"
        const val notificationId = 32194567
        const val LAST_INTOOK_KEY: String = "last_intook"
        const val SEE_SPLASH_KEY : String = "see_splash"
        const val GENDER_KEY : String = "gender"
        const val SET_GENDER_KEY : String = "set_gender"
        const val BLOOD_DONOR_KEY : String = "blood_donor"
        const val SET_BLOOD_KEY : String = "set_blood_donor"
        const val SET_NEW_WORK_TYPE_KEY : String = "set_new_work_type"
        const val CLIMATE_KEY : String = "climate"
        const val SET_CLIMATE_KEY : String = "set_climate"
        const val SEE_TIPS_KEY : String = "see_tips"
        const val START_TUTORIAL_KEY : String = "start_tutorial"
        const val START_DATE = "31-08-2023"
        const val STAT_IS_MONTH_KEY : String = "isMonth"
        const val INDEX_MONTH_KEY : String = "month"
        const val INDEX_YEAR_KEY : String = "year"
        const val DATE : String = "date"


    }
}

