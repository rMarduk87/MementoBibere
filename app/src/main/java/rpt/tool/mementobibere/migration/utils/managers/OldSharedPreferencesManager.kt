package rpt.tool.mementobibere.migration.utils.managers

import android.content.Context
import android.content.SharedPreferences
import android.media.RingtoneManager
import rpt.tool.mementobibere.Application
import rpt.tool.mementobibere.utils.AppUtils

object OldSharedPreferencesManager {
    private val ctx: Context
        get() = Application.instance

    private fun createSharedPreferences(): SharedPreferences {
        return ctx.getSharedPreferences(AppUtils.USERS_SHARED_PREF, Context.MODE_PRIVATE)
    }

    private val sharedPreferences by lazy { createSharedPreferences() }

    var bloodDonorKey: Int
        get() = sharedPreferences.getInt(AppUtils.BLOOD_DONOR_KEY,0)
        set(value) = sharedPreferences.edit().putInt(AppUtils.BLOOD_DONOR_KEY, value).apply()
    var totalIntake: Float
        get() = sharedPreferences.getFloat(AppUtils.TOTAL_INTAKE_KEY, 0f)
        set(value) = sharedPreferences.edit().putFloat(AppUtils.TOTAL_INTAKE_KEY, value).apply()
    var firstRun: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.FIRST_RUN_KEY, true)
        set(value) = sharedPreferences.edit().putBoolean(AppUtils.FIRST_RUN_KEY, value).apply()
    var unitString: String
        get() = sharedPreferences.getString(AppUtils.UNIT_STRING,"ml")!!
        set(value) = sharedPreferences.edit().putString(AppUtils.UNIT_STRING, value).apply()
    var notificationFreq: Float
        get() = sharedPreferences.getInt(AppUtils.NOTIFICATION_FREQUENCY_KEY, 30).toFloat()
        set(value) = sharedPreferences.edit().putInt(AppUtils.NOTIFICATION_FREQUENCY_KEY, value.toInt()).apply()
    var setGender: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.SET_GENDER_KEY, false)
        set(value) = sharedPreferences.edit().putBoolean(AppUtils.SET_GENDER_KEY, value).apply()
    var setBloodDonor: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.SET_BLOOD_KEY, false)
        set(value) = sharedPreferences.edit().putBoolean(AppUtils.SET_BLOOD_KEY, value).apply()
    var setWorkOut: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.SET_NEW_WORK_TYPE_KEY, false)
        set(value) = sharedPreferences.edit().putBoolean(AppUtils.SET_NEW_WORK_TYPE_KEY, value).apply()
    var setClimate: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.SET_CLIMATE_KEY, false)
        set(value) = sharedPreferences.edit().putBoolean(AppUtils.SET_CLIMATE_KEY, value).apply()
    var setWeight: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.SET_WEIGHT_UNIT, false)
        set(value) = sharedPreferences.edit().putBoolean(AppUtils.SET_WEIGHT_UNIT, value).apply()
    var sleepingTime: Long
        get() = sharedPreferences.getLong(AppUtils.SLEEPING_TIME_KEY,1558369800000)
        set(value) = sharedPreferences.edit().putLong(AppUtils.SLEEPING_TIME_KEY, value).apply()
    var wakeUpTime: Long
        get() = sharedPreferences.getLong(AppUtils.WAKEUP_TIME_KEY,1558323000000)
        set(value) = sharedPreferences.edit().putLong(AppUtils.WAKEUP_TIME_KEY, value).apply()
    var notificationStatus: Boolean
        get() = sharedPreferences.getBoolean(AppUtils.NOTIFICATION_STATUS_KEY, true)
        set(value) = sharedPreferences.edit().putBoolean(AppUtils.NOTIFICATION_STATUS_KEY, value).apply()
    var gender: Int
        get() = sharedPreferences.getInt(AppUtils.GENDER_KEY, 0)
        set(value) = sharedPreferences.edit().putInt(AppUtils.GENDER_KEY, value).apply()
    var workType: Int
        get() = sharedPreferences.getInt(AppUtils.WORK_TIME_KEY, 0)
        set(value) = sharedPreferences.edit().putInt(AppUtils.WORK_TIME_KEY, value).apply()
    var weight: Int
        get() = sharedPreferences.getInt(AppUtils.WEIGHT_KEY, 0)
        set(value) = sharedPreferences.edit().putInt(AppUtils.WEIGHT_KEY, value).apply()
    var weightUnit: Int
        get() = sharedPreferences.getInt(AppUtils.WEIGHT_UNIT_KEY,0)
        set(value) = sharedPreferences.edit().putInt(AppUtils.WEIGHT_UNIT_KEY, value).apply()
    var climate: Int
        get() = sharedPreferences.getInt(AppUtils.CLIMATE_KEY, 0)
        set(value) = sharedPreferences.edit().putInt(AppUtils.CLIMATE_KEY, value).apply()
    var date: String
        get() = sharedPreferences.getString(
            AppUtils.DATE,
            AppUtils.getCurrentDate()!!)!!
        set(value) = sharedPreferences.edit().putString(AppUtils.DATE, value).apply()
}