package rpt.tool.mementobibere.basic.appbasiclibs.utils

import rpt.tool.mementobibere.basic.appbasiclibs.BaseActivity
import rpt.tool.mementobibere.basic.appbasiclibs.utils.Constant.sh
import rpt.tool.mementobibere.utils.log.d
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit


class Date_Helper : BaseActivity() {

    fun FormateDateFromString(
        inputFormat: String?,
        outputFormat: String?,
        inputDate: String?
    ): String {
        var parsed: Date? = null
        var outputDate = ""

        /*SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, Locale.getDefault());
    SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, Locale.getDefault());*/
        val df_input = SimpleDateFormat(inputFormat, Locale.getDefault())
        val df_output = SimpleDateFormat(outputFormat, Locale.getDefault())

        try {
            parsed = df_input.parse(inputDate)
            outputDate = df_output.format(parsed)
        } catch (e: Exception) {
        }

        return outputDate
    }

    fun getDate(milliSeconds: Long, dateFormat: String?): String {
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(dateFormat)

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar = Calendar.getInstance(Locale.getDefault())
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }



    fun getLastDateOfMonth(month: Int, year: Int, format: String?): String {
        val calendar = Calendar.getInstance(Locale.getDefault())
        // passing month-1 because 0-->jan, 1-->feb... 11-->dec
        calendar[year, month - 1] = 1
        calendar[Calendar.DATE] = calendar.getActualMaximum(Calendar.DATE)
        val date = calendar.time
        val DATE_FORMAT: DateFormat = SimpleDateFormat(format) //"MM/dd/yyyy"
        return DATE_FORMAT.format(date)
    }

    fun getMillisecondFromDate(givenDateString: String?, format: String?): Long {
        val sdf = SimpleDateFormat(format)
        var timeInMilliseconds: Long = 0
        try {
            val mDate = sdf.parse(givenDateString)
            timeInMilliseconds = mDate.time
            //System.out.println("Date in milli :: " + timeInMilliseconds);
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return timeInMilliseconds
    }

    val millisecond: Long
        get() {
            val cal = Calendar.getInstance(Locale.getDefault())
            cal[Calendar.HOUR] = 0
            cal[Calendar.MINUTE] = 0
            cal[Calendar.SECOND] = 0
            cal[Calendar.AM_PM] = 0
            return cal.timeInMillis
        }

    val currentGMTMillisecond: Long
        get() {
            val current_cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault())
            return current_cal.timeInMillis
        }

    fun getMillisecond(year: Int, month: Int, day: Int): Long {
        val cal = Calendar.getInstance(Locale.getDefault())
        cal[Calendar.YEAR] = year
        cal[Calendar.MONTH] = month
        cal[Calendar.DAY_OF_MONTH] = day
        cal[Calendar.HOUR_OF_DAY] = 0
        cal[Calendar.MINUTE] = 0
        cal[Calendar.SECOND] = 0
        cal[Calendar.AM_PM] = 0
        return cal.timeInMillis
    }

    fun getMillisecond(year: Int, month: Int, day: Int, hour: Int, minute: Int, format: Int): Long {
        val cal = Calendar.getInstance(Locale.getDefault())
        cal[Calendar.YEAR] = year
        cal[Calendar.MONTH] = month
        cal[Calendar.DAY_OF_MONTH] = day
        cal[Calendar.HOUR] = hour
        cal[Calendar.MINUTE] = minute
        cal[Calendar.SECOND] = 0
        cal[Calendar.AM_PM] = format
        return cal.timeInMillis
    }

    fun getMillisecond(
        year: Int,
        month: Int,
        day: Int,
        hour: Int,
        minute: Int,
        format: String
    ): Long {
        val cal = Calendar.getInstance(Locale.getDefault())
        cal[Calendar.YEAR] = year
        cal[Calendar.MONTH] = month
        cal[Calendar.DAY_OF_MONTH] = day
        cal[Calendar.HOUR] = hour
        cal[Calendar.MINUTE] = minute
        cal[Calendar.SECOND] = 0
        if (format.uppercase(Locale.getDefault()) == "PM") cal[Calendar.AM_PM] = 1
        else cal[Calendar.AM_PM] = 0
        return cal.timeInMillis
    }

    fun getMillisecond(year: Int, month: Int, day: Int, hour: Int, minute: Int): Long {
        val cal = Calendar.getInstance(Locale.getDefault())
        cal[Calendar.YEAR] = year
        cal[Calendar.MONTH] = month
        cal[Calendar.DAY_OF_MONTH] = day
        cal[Calendar.HOUR_OF_DAY] = hour
        cal[Calendar.MINUTE] = minute
        cal[Calendar.SECOND] = 0
        return cal.timeInMillis
    }

    val currentMillisecond: Long
        get() {
            val cal = Calendar.getInstance(Locale.getDefault())
            return cal.timeInMillis
        }

    fun getTimeWithAP(time: String): String {
        val fformat: String
        val separated = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var fhour = ("" + separated[0]).toInt()
        val fmin = ("" + separated[1]).toInt()
        if (fhour == 0) {
            fhour += 12
            fformat = "AM"
        } else if (fhour == 12) {
            fformat = "PM"
        } else if (fhour > 12) {
            fhour -= 12
            fformat = "PM"
        } else {
            fformat = "AM"
        }

        return sh!!.get_2_point("" + fhour) + ":" + sh!!.get_2_point("" + fmin) + " " + fformat
    }

    fun getDaySuffix(n: Int): String {
        if (n < 1 || n > 31) return "Invalid date"
        if (n in 11..13) return "th"

        return when (n % 10) {
            1 -> "st"
            2 -> "nd"
            3 -> "rd"
            else -> "th"
        }
    }

    fun getFullMonth(dateInString: String?, format: String?): String {
        val sdf = SimpleDateFormat(format)
        var formated = ""
        try {
            val date = sdf.parse(dateInString)
            formated = SimpleDateFormat("MMMM").format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return formated
    }

    fun getShortMonth(dateInString: String?, format: String?): String {
        val sdf = SimpleDateFormat(format)
        var formated = ""
        try {
            val date = sdf.parse(dateInString)
            formated = SimpleDateFormat("MMM").format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return formated
    }

    fun getMonth(dateInString: String?, format: String?): String {
        val sdf = SimpleDateFormat(format)
        var formated = ""
        try {
            val date = sdf.parse(dateInString)
            formated = SimpleDateFormat("MM").format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return formated
    }

    fun getDay(dateInString: String?, format: String?): String {
        val sdf = SimpleDateFormat(format)
        var formated = ""
        try {
            val date = sdf.parse(dateInString)
            formated = SimpleDateFormat("dd").format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return formated
    }

    fun getYear(dateInString: String?, format: String?): String {
        val sdf = SimpleDateFormat(format)
        var formated = ""
        try {
            val date = sdf.parse(dateInString)
            formated = SimpleDateFormat("yyyy").format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return formated
    }

    fun getDayswithPrefix(dateInString: String?, format: String?): String {
        val sdf = SimpleDateFormat(format)
        var formated = "0"
        try {
            val date = sdf.parse(dateInString)
            formated = SimpleDateFormat("dd").format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return formated + getDaySuffix(formated.toInt())
    }

    fun getCurrentDateTime(is24TimeFormat: Boolean): String {
        val dateFormat = if (is24TimeFormat) SimpleDateFormat(
            "yyyy-MM-dd HH:mm",
            Locale.getDefault()
        )
        else SimpleDateFormat("yyyy-MM-dd KK:mm a", Locale.getDefault())

        val date = Date()
        return dateFormat.format(date)
    }

    fun set_format_date(year: Int, month: Int, day: Int, format: String?): String {
        val sdf = SimpleDateFormat(format)
        val formatedDate = sdf.format(Date(year, month, day))
        return formatedDate
    }

    val currentDate: String
        get() {
            val dateFormat = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
            val date = Date()
            return dateFormat.format(date)
        }

    fun getFormatDate(format: String?): String {
        //SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

    fun getCurrentDate(format: String?): String {
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

    fun getCurrentTime(is24TimeFormat: Boolean): String {
        val dateFormat = if (is24TimeFormat) SimpleDateFormat("HH:mm", Locale.getDefault())
        else SimpleDateFormat("KK:mm a", Locale.getDefault())

        val date = Date()
        return dateFormat.format(date)
    }

    fun DayDifferent(str_date1: String?, str_date2: String?): Long {
        /*String inputString1 = "23 01 1997";
        String inputString2 = "27 04 1997";*/
        var diff: Long = 0
        var days_diff: Long = 0
        val myFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        try {
            val date1 = myFormat.parse(str_date1)
            val date2 = myFormat.parse(str_date2)
            diff = date2.time - date1.time
            println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS))
            days_diff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return days_diff
    }

    fun DayDifferent(str_date1: String?, str_date2: String?, format: String?): Long {
        /*String inputString1 = "23 01 1997";
        String inputString2 = "27 04 1997";*/
        var diff: Long = 0
        var days_diff: Long = 0
        val myFormat = SimpleDateFormat(format, Locale.getDefault())
        try {
            val date1 = myFormat.parse(str_date1)
            val date2 = myFormat.parse(str_date2)
            diff = date2.time - date1.time
            println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS))
            days_diff = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return days_diff
    }

    fun getDaysAgo(date: String): String {
        val dateString = date
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        var convertedDate = Date()
        var serverDate = Date()

        try {
            convertedDate = dateFormat.parse(dateString)
            val c = Calendar.getInstance(Locale.getDefault())
            val formattedDate = dateFormat.format(c.time)
            serverDate = dateFormat.parse(formattedDate)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        //long days1 = (convertedDate.getTime() - serverDate.getTime());
        val days1 = (serverDate.time - convertedDate.time)

        val seconds = days1 / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        val months = days / 31
        val years = days / 365

        println("serverDate:" + serverDate.time)
        println("convertedDate:" + convertedDate.time)
        println("days1:$days1")
        println("seconds:$seconds")
        println("minutes:$minutes")
        println("hours:$hours")
        println("days:$days")
        println("months:$months")
        println("years:$years")

        return if (seconds < 86400) // 24  60 60 ( less than 1 day )
        {
            "today"
        } else if (seconds < 172800) // 48  60  60 ( less than 2 day )
        {
            "yesterday"
        } else if (seconds < 2592000) // 30  24  60 * 60 ( less than 1 month )
        {
            "$days days ago"
        } else if (seconds < 31104000) // 12  30  24  60  60
        {
            if (months <= 1) "one month ago" else "$months months ago"
        } else {
            if (years <= 1) "one year ago" else "$years years ago"
        }
    }

    fun check_current_time_between_2date(start_date: String?, end_date: String?): Boolean {
        try {
            val mToday = Date()
            val sdf = SimpleDateFormat("hh:mm aa")
            val curTime = sdf.format(mToday)

            val start = sdf.parse(start_date)
            val end = sdf.parse(end_date)
            val userDate = sdf.parse(curTime)

            if (end.before(start)) {
                val mCal = Calendar.getInstance(Locale.getDefault())
                mCal.time = end
                mCal.add(Calendar.DAY_OF_YEAR, 1)
                end.time = mCal.timeInMillis
            }

            d("curTime", userDate.toString())
            d("start", start.toString())
            d("end", end.toString())

            return userDate.after(start) && userDate.before(end)
        } catch (e: Exception) {
            return false
        }
    }

    fun check_specific_time_between_2date(
        start_date: String?,
        end_date: String?,
        my_date: String?
    ): Boolean {
        try {
            val sdf = SimpleDateFormat("hh:mm aa")

            val start = sdf.parse(start_date)
            val end = sdf.parse(end_date)
            val userDate = sdf.parse(my_date)

            if (end.before(start)) {
                val mCal = Calendar.getInstance(Locale.getDefault())
                mCal.time = end
                mCal.add(Calendar.DAY_OF_YEAR, 1)
                end.time = mCal.timeInMillis
            }

            d("curTime", userDate.toString())
            d("start", start.toString())
            d("end", end.toString())

            return if (userDate === start) true
            else if (userDate.after(start) && userDate.before(end)) true
            else false
        } catch (e: Exception) {
            return false
        }
    }

    fun getGMTDate(dateFormat: String?): String {
        val formatter = SimpleDateFormat(dateFormat)
        val cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("GMT")

        return formatter.format(cal.time)
    }

    fun get_total_days_of_month(month: Int, year: Int): Int {
        val day = 31

        if (month == 4 || month == 6 || month == 9 || month == 11) return 30
        else if (month == 2) {
            return if (year % 4 == 0) 29
            else 28
        }
        return day
    }

    fun different_time(current_time: String?, time: String?): Boolean {
        val simpleDateFormat = SimpleDateFormat("HH:mm")
        var CurrentTime: Date? = null
        var Time: Date? = null

        try {
            CurrentTime = simpleDateFormat.parse(current_time)
            Time = simpleDateFormat.parse(time)
        } catch (e: ParseException) {
            //Some thing if its not working
        }

        val difference = Time!!.time - CurrentTime!!.time

        return difference >= 0
    }

    fun different_time(current_time: String?, time: String?, format: String?): Boolean {
        val simpleDateFormat = SimpleDateFormat(format)
        var CurrentTime: Date? = null
        var Time: Date? = null

        try {
            CurrentTime = simpleDateFormat.parse(current_time)
            Time = simpleDateFormat.parse(time)
        } catch (e: ParseException) {
            //Some thing if its not working
        }

        val difference = Time!!.time - CurrentTime!!.time

        return difference >= 0
    }

    fun get_2_point(no: String): String {
        var no = no
        if (no.length == 1) no = "0$no"
        return no
    }

    fun getTimeHour(time: String): String {
        val separated = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val fhour = ("" + separated[0]).toInt()
        return get_2_point("" + fhour)
    }

    fun getTimeMin(time: String): String {
        val separated = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val fmin = ("" + separated[1]).toInt()
        return get_2_point("" + fmin)
    }

    fun getTimeFormat(time: String): String {
        val fformat: String
        val separated = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val fhour = ("" + separated[0]).toInt()
        fformat = if (fhour == 0) {
            "AM"
        } else if (fhour == 12) {
            "PM"
        } else if (fhour > 12) {
            "PM"
        } else {
            "AM"
        }
        return "" + fformat
    }

    fun getCurrentTimeSecond(is24TimeFormat: Boolean): String {
        val dateFormat = if (is24TimeFormat) SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        else SimpleDateFormat("KK:mm:ss a", Locale.getDefault())

        val date = Date()
        return dateFormat.format(date)
    }
}