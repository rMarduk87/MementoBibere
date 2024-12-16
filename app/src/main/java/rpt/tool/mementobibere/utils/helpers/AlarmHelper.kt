package rpt.tool.mementobibere.utils.helpers

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import androidx.core.database.getStringOrNull
import rpt.tool.mementobibere.basic.appbasiclibs.utils.Constant
import rpt.tool.mementobibere.basic.appbasiclibs.utils.Date_Helper
import rpt.tool.mementobibere.utils.URLFactory
import rpt.tool.mementobibere.utils.data.backuprestore.AlarmDetails
import rpt.tool.mementobibere.utils.data.backuprestore.AlarmSubDetails
import rpt.tool.mementobibere.utils.data.backuprestore.BackupRestore
import rpt.tool.mementobibere.utils.extensions.equalsIgnoreCase
import rpt.tool.mementobibere.utils.log.d
import rpt.tool.mementobibere.utils.managers.SharedPreferencesManager
import rpt.tool.mementobibere.utils.receiver.MyAlarmManager
import java.util.Calendar
import java.util.Locale

@Suppress("DEPRECATED_IDENTITY_EQUALS")
internal class AlarmHelper
    (private val mContext: Context) {
    var dth: Date_Helper = Date_Helper()
    var alarmDetailsList: MutableList<AlarmDetails> = ArrayList<AlarmDetails>()


    init {
        Constant.SDB = mContext.openOrCreateDatabase(
            Constant.DATABASE_NAME,
            Context.MODE_PRIVATE,
            null
        )
    }

    fun createAlarm() {
        loadData()
        setAlarm()
    }

    fun setAlarm() {
        val backupRestore: BackupRestore = BackupRestore()
        backupRestore.alarmDetails = alarmDetailsList
        backupRestore.isManualReminderActive(SharedPreferencesManager.isManualReminder)

        for (k in alarmDetailsList.indices) {
            if (backupRestore.alarmDetails[k].alarmType!!.equalsIgnoreCase("M")
                && backupRestore.isManualReminderActive
            ) {
                val hourOfDay = ("" +
                        dth.FormateDateFromString(
                            "hh:mm a", "HH",
                            backupRestore.alarmDetails[k].alarmTime!!.trim()
                        )).toInt()
                val minute = ("" +
                        dth.FormateDateFromString(
                            "hh:mm a", "mm",
                            backupRestore.alarmDetails[k].alarmTime!!.trim()
                        )).toInt()

                if (backupRestore.alarmDetails[k].sunday === 1
                ) MyAlarmManager.scheduleManualRecurringAlarm(
                    mContext, Calendar.SUNDAY, hourOfDay, minute,
                    ("" + backupRestore.alarmDetails[k].alarmSundayId).toInt()
                )

                if (backupRestore.alarmDetails[k].monday === 1
                ) MyAlarmManager.scheduleManualRecurringAlarm(
                    mContext, Calendar.MONDAY, hourOfDay, minute,
                    ("" + backupRestore.alarmDetails[k].alarmMondayId).toInt()
                )

                if (backupRestore.alarmDetails[k].tuesday === 1
                ) MyAlarmManager.scheduleManualRecurringAlarm(
                    mContext, Calendar.TUESDAY, hourOfDay, minute,
                    ("" + backupRestore.alarmDetails[k].alarmTuesdayId).toInt()
                )

                if (backupRestore.alarmDetails[k].wednesday === 1
                ) MyAlarmManager.scheduleManualRecurringAlarm(
                    mContext, Calendar.WEDNESDAY, hourOfDay, minute,
                    ("" + backupRestore.alarmDetails[k].alarmWednesdayId).toInt()
                )

                if (backupRestore.alarmDetails[k].thursday === 1
                ) MyAlarmManager.scheduleManualRecurringAlarm(
                    mContext, Calendar.THURSDAY, hourOfDay, minute,
                    ("" + backupRestore.alarmDetails[k].alarmThursdayId).toInt()
                )

                if (backupRestore.alarmDetails[k].friday === 1
                ) MyAlarmManager.scheduleManualRecurringAlarm(
                    mContext, Calendar.FRIDAY, hourOfDay, minute,
                    ("" + backupRestore.alarmDetails[k].alarmFridayId).toInt()
                )

                if (backupRestore.alarmDetails[k].saturday === 1
                ) MyAlarmManager.scheduleManualRecurringAlarm(
                    mContext, Calendar.SATURDAY, hourOfDay, minute,
                    ("" + backupRestore.alarmDetails[k].alarmSaturdayId).toInt()
                )
            }

            val alarmSubDetailsList: List<AlarmSubDetails> =
                backupRestore.alarmDetails[k].alarmSubDetails

            for (j in alarmSubDetailsList.indices) {
                if (!backupRestore.isManualReminderActive) {
                    val hourOfDay = ("" +
                            dth.FormateDateFromString(
                                "hh:mm a", "HH",
                                alarmSubDetailsList[j].alarmTime!!.trim()
                            )).toInt()
                    val minute = ("" +
                            dth.FormateDateFromString(
                                "hh:mm a", "mm",
                                alarmSubDetailsList[j].alarmTime!!.trim()
                            )).toInt()

                    val updateTime = Calendar.getInstance(Locale.getDefault())
                    updateTime[Calendar.HOUR_OF_DAY] = hourOfDay
                    updateTime[Calendar.MINUTE] = minute
                    updateTime[Calendar.SECOND] = 0

                    MyAlarmManager.scheduleAutoRecurringAlarm(
                        mContext,
                        updateTime,
                        ("" + alarmSubDetailsList[j].alarmId).toInt()
                    )
                }
            }
        }
    }

    private fun loadData() {
        val arr_data = getData("tbl_alarm_details")

        alarmDetailsList.clear()

        for (k in arr_data.indices) {
            val alarmDetails: AlarmDetails = AlarmDetails()
            alarmDetails.alarmId = arr_data[k]["AlarmId"]
            alarmDetails.alarmInterval = arr_data[k]["AlarmInterval"]
            alarmDetails.alarmTime = arr_data[k]["AlarmTime"]
            alarmDetails.alarmType = arr_data[k]["AlarmType"]
            alarmDetails.id = arr_data[k]["id"]

            alarmDetails.alarmSundayId = arr_data[k]["SundayAlarmId"]
            alarmDetails.alarmMondayId = arr_data[k]["MondayAlarmId"]
            alarmDetails.alarmTuesdayId = arr_data[k]["TuesdayAlarmId"]
            alarmDetails.alarmWednesdayId = arr_data[k]["WednesdayAlarmId"]
            alarmDetails.alarmThursdayId = arr_data[k]["ThursdayAlarmId"]
            alarmDetails.alarmFridayId = arr_data[k]["FridayAlarmId"]
            alarmDetails.alarmSaturdayId = arr_data[k]["SaturdayAlarmId"]

            alarmDetails.isOff = arr_data[k]["IsOff"]!!.toInt()
            alarmDetails.sunday = arr_data[k]["Sunday"]!!.toInt()
            alarmDetails.monday = arr_data[k]["Monday"]!!.toInt()
            alarmDetails.tuesday = arr_data[k]["Tuesday"]!!.toInt()
            alarmDetails.wednesday = arr_data[k]["Wednesday"]!!.toInt()
            alarmDetails.thursday = arr_data[k]["Thursday"]!!.toInt()
            alarmDetails.friday = arr_data[k]["Friday"]!!.toInt()
            alarmDetails.saturday = arr_data[k]["Saturday"]!!.toInt()

            val alarmSubDetailsList: MutableList<AlarmSubDetails> = ArrayList<AlarmSubDetails>()

            val arr_data2 = getData("tbl_alarm_sub_details", "SuperId=" + arr_data[k]["id"])

            d("arr_data2 : ", "" + arr_data2.size)

            for (j in arr_data2.indices) {
                val alarmSubDetails: AlarmSubDetails = AlarmSubDetails()
                alarmSubDetails.alarmId = arr_data2[j]["AlarmId"]
                alarmSubDetails.alarmTime = arr_data2[j]["AlarmTime"]
                alarmSubDetails.id = arr_data2[j]["id"]
                alarmSubDetails.superId = arr_data2[j]["SuperId"]
                alarmSubDetailsList.add(alarmSubDetails)
            }

            alarmDetails.alarmSubDetails = alarmSubDetailsList

            alarmDetailsList.add(alarmDetails)
        }
    }


    @SuppressLint("Recycle")
    fun getData(table_name: String): ArrayList<HashMap<String, String>> {
        val maplist = ArrayList<HashMap<String, String>>()

        val query = "SELECT * FROM $table_name"

        val exsist = checkTableExistanse(table_name)

        if(exsist){
            val c: Cursor = Constant.SDB!!.rawQuery(query, null)

            if (c.moveToFirst()) {
                do {
                    val map = HashMap<String, String>()
                    for (i in 0 until c.columnCount) {
                        if(c.getStringOrNull(i) != null){
                            map[c.getColumnName(i)] = c.getString(i)
                        }
                    }

                    maplist.add(map)
                } while (c.moveToNext())
            }
        }

        return maplist
    }

    private fun checkTableExistanse(tableName: String): Boolean {
        val query =
            ("select DISTINCT tbl_name from sqlite_master where tbl_name = '$tableName").toString() + "'"
        Constant.SDB!!.rawQuery(query, null).use { cursor ->
            if (cursor != null) {
                if (cursor.count > 0) {
                    return true
                }
            }
            return false
        }
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
                    if(c.getStringOrNull(i) != null){
                        map[c.getColumnName(i)] = c.getString(i)
                    }
                }

                maplist.add(map)
            } while (c.moveToNext())
        }

        return maplist
    }
}

private fun String.toInt(): Int {
return this.toInt()
}
