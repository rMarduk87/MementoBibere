package rpt.tool.mementobibere.utils.helpers

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import rpt.tool.mementobibere.basic.appbasiclibs.utils.Constant
import rpt.tool.mementobibere.basic.appbasiclibs.utils.Date_Helper
import rpt.tool.mementobibere.basic.appbasiclibs.utils.Preferences_Helper
import rpt.tool.mementobibere.utils.URLFactory
import rpt.tool.mementobibere.utils.data.backuprestore.AlarmDetails
import rpt.tool.mementobibere.utils.data.backuprestore.AlarmSubDetails
import rpt.tool.mementobibere.utils.data.backuprestore.BackupRestore
import rpt.tool.mementobibere.utils.data.backuprestore.BloodDonor
import rpt.tool.mementobibere.utils.data.backuprestore.ContainerDetails
import rpt.tool.mementobibere.utils.data.backuprestore.DrinkDetails
import rpt.tool.mementobibere.utils.data.backuprestore.ReachedGoal
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter

internal class BackupHelper(private val mContext: Context) {
    var dth: Date_Helper = Date_Helper()
    var ph: Preferences_Helper = Preferences_Helper(mContext)

    fun createAutoBackSetup() {
        if (!ph.getBoolean(URLFactory.AUTO_BACK_UP)) return


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkStoragePermissions()
        } else {
            backup_data()
        }
    }

    fun checkStoragePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
            } else {
                backup_data()
            }
        }
    }

    fun backup_data() {
        var arr_data = getdata("tbl_container_details")

        val backupRestore: BackupRestore = BackupRestore()

        val containerDetailsList: MutableList<ContainerDetails> = ArrayList<ContainerDetails>()

        for (k in arr_data.indices) {
            val containerDetails: ContainerDetails = ContainerDetails()
            containerDetails.containerID = arr_data[k]["ContainerID"]
            containerDetails.containerMeasure = arr_data[k]["ContainerMeasure"]
            containerDetails.containerValue = arr_data[k]["ContainerValue"]
            containerDetails.containerValueOZ = arr_data[k]["ContainerValueOZ"]
            containerDetails.isOpen = arr_data[k]["IsOpen"]
            containerDetails.id = arr_data[k]["id"]
            containerDetails.isCustom = arr_data[k]["IsCustom"]
            containerDetailsList.add(containerDetails)
        }

        arr_data = getdata("tbl_drink_details")

        val drinkDetailsList: MutableList<DrinkDetails> = ArrayList<DrinkDetails>()

        for (k in arr_data.indices) {
            val drinkDetails: DrinkDetails = DrinkDetails()
            drinkDetails.drinkDateTime = arr_data[k]["DrinkDateTime"]
            drinkDetails.drinkDate = arr_data[k]["DrinkDate"]
            drinkDetails.drinkTime = arr_data[k]["DrinkTime"]
            drinkDetails.containerMeasure = arr_data[k]["ContainerMeasure"]
            drinkDetails.containerValue = arr_data[k]["ContainerValue"]
            drinkDetails.containerValueOZ = arr_data[k]["ContainerValueOZ"]
            drinkDetails.id = arr_data[k]["id"]
            drinkDetails.todayGoal = arr_data[k]["TodayGoal"]
            drinkDetails.todayGoalOZ = arr_data[k]["TodayGoalOZ"]
            drinkDetailsList.add(drinkDetails)
        }

        arr_data = getdata("tbl_alarm_details")

        val alarmDetailsList: MutableList<AlarmDetails> = ArrayList<AlarmDetails>()

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

            val arr_data2 = getdata("tbl_alarm_sub_details", "SuperId=" + arr_data[k]["id"])

            Log.d("arr_data2 : ", "" + arr_data2.size)

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

        arr_data = getdata("tbl_blood_donor")

        val bloodDonorList: MutableList<BloodDonor> = ArrayList<BloodDonor>()

        for (k in arr_data.indices) {
            val bloodDonor: BloodDonor = BloodDonor()
            bloodDonor.id = arr_data[k]["id"]
            bloodDonor.date = arr_data[k]["BloodDonorDate"]
            bloodDonorList.add(bloodDonor)
        }

        arr_data = getdata("tbl_reached_goal")

        val reachedList: MutableList<ReachedGoal> = ArrayList<ReachedGoal>()

        for (k in arr_data.indices) {
            val reachedGoal: ReachedGoal = ReachedGoal()
            reachedGoal.id = arr_data[k]["id"]
            reachedGoal.date = arr_data[k]["BloodDonorDate"]
            reachedGoal.containerValue = arr_data[k]["ContainerValue"]
            reachedGoal.containerValueOZ = arr_data[k]["ContainerValueOZ"]
            reachedList.add(reachedGoal)
        }


        backupRestore.containerDetails = containerDetailsList
        backupRestore.drinkDetails = drinkDetailsList
        backupRestore.alarmDetails = alarmDetailsList
        backupRestore.bloodDonorList = bloodDonorList
        backupRestore.reachedGoalList = reachedList

        backupRestore.totalDrink = ph.getFloat(URLFactory.DAILY_WATER)

        backupRestore.totalHeight = ph.getString(URLFactory.PERSON_WEIGHT)
        backupRestore.totalWeight = ph.getString(URLFactory.PERSON_HEIGHT)

        backupRestore.isCMUnit(ph.getBoolean(URLFactory.PERSON_HEIGHT_UNIT))
        backupRestore.isKgUnit(ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT))
        backupRestore.isMlUnit(ph.getBoolean(URLFactory.PERSON_WEIGHT_UNIT))


        backupRestore.reminderOption = ph.getInt(URLFactory.REMINDER_OPTION)
        backupRestore.reminderSound = ph.getInt(URLFactory.REMINDER_SOUND)
        backupRestore.isDisableNotifiction(ph.getBoolean(URLFactory.DISABLE_NOTIFICATION))
        backupRestore.isManualReminderActive(ph.getBoolean(URLFactory.IS_MANUAL_REMINDER))
        backupRestore.isReminderVibrate(ph.getBoolean(URLFactory.REMINDER_VIBRATE))

        backupRestore.userName = ph.getString(URLFactory.USER_NAME)
        backupRestore.userGender = ph.getBoolean(URLFactory.USER_GENDER)
        backupRestore.bloodDonor = ph.getBoolean(URLFactory.BLOOD_DONOR)

        backupRestore.isDisableSound(ph.getBoolean(URLFactory.DISABLE_SOUND_WHEN_ADD_WATER))


        backupRestore.isAutoBackup(ph.getBoolean(URLFactory.AUTO_BACK_UP))
        backupRestore.autoBackupType = ph.getInt(URLFactory.AUTO_BACK_UP_TYPE)
        backupRestore.setAutoBackupID(ph.getInt(URLFactory.AUTO_BACK_UP_ID))


        val jsondata: String = Gson().toJson(backupRestore)
        val jsonParser1: JsonParser = JsonParser()
        val jsonObject: JsonObject = jsonParser1.parse(jsondata) as JsonObject

        store_response(jsonObject.toString())
    }

    fun store_response(plainBody: String?) {
        val f: File = File(
            (Environment.getExternalStorageDirectory()
                .toString() + "/" + URLFactory.APP_DIRECTORY_NAME).toString() + "/"
        )

        if (!f.exists()) f.mkdir()

        if (f.exists()) {
            val dt: String = dth.getCurrentDate("dd-MMM-yyyy hh:mm:ss a")

            val full_file_name: String = ((Environment.getExternalStorageDirectory()
                .toString() + "/" + URLFactory.APP_DIRECTORY_NAME).toString() + "/Backup_"
                    + dt + ".txt")

            val file = File(full_file_name)

            if (!file.exists()) {
                try {
                    file.createNewFile()
                } catch (ioe: IOException) {
                    ioe.printStackTrace()
                }
            }

            try {
                val fileOutputStream = FileOutputStream(file)
                val writer = OutputStreamWriter(fileOutputStream)
                //writer.append(cipherBody);
                writer.append(plainBody)
                writer.close()
                fileOutputStream.close()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun getdata(table_name: String): ArrayList<HashMap<String?, String>> {
        val maplist = ArrayList<HashMap<String?, String>>()

        val query = "SELECT * FROM $table_name"

        val c: Cursor = Constant.SDB!!.rawQuery(query, null)

        if (c.moveToFirst()) {
            do {
                val map = HashMap<String?, String>()
                for (i in 0 until c.columnCount) {
                    map[c.getColumnName(i)] = c.getString(i)
                }

                maplist.add(map)
            } while (c.moveToNext())
        }

        return maplist
    }

    fun getdata(table_name: String, where_con: String): ArrayList<HashMap<String, String>> {
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