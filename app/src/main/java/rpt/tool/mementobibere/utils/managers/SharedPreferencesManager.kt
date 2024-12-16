package rpt.tool.mementobibere.utils.managers

import android.content.Context
import android.content.SharedPreferences
import rpt.tool.mementobibere.Application
import rpt.tool.mementobibere.utils.URLFactory

object SharedPreferencesManager {
    private val ctx: Context
        get() = Application.instance

    private fun createSharedPreferences(): SharedPreferences {
        return ctx.getSharedPreferences(URLFactory.USERS_SHARED_PREF, Context.MODE_PRIVATE)
    }

    private val sharedPreferences by lazy { createSharedPreferences() }

    var setManuallyGoal: Boolean
        get() = sharedPreferences.getBoolean(URLFactory.SET_MANUALLY_GOAL,false)
        set(value) = sharedPreferences.edit().putBoolean(URLFactory.SET_MANUALLY_GOAL, value).apply()
    var bloodDonorKey: Boolean
        get() = sharedPreferences.getBoolean(URLFactory.BLOOD_DONOR,false)
        set(value) = sharedPreferences.edit().putBoolean(URLFactory.BLOOD_DONOR, value).apply()
    var setGender: Boolean
        get() = sharedPreferences.getBoolean(URLFactory.SET_USER_GENDER, false)
        set(value) = sharedPreferences.edit().putBoolean(URLFactory.SET_USER_GENDER, value).apply()
    var setBloodDonor: Boolean
        get() = sharedPreferences.getBoolean(URLFactory.SET_BLOOD_DONOR, false)
        set(value) = sharedPreferences.edit().putBoolean(URLFactory.SET_BLOOD_DONOR, value).apply()
    var setWorkOut: Boolean
        get() = sharedPreferences.getBoolean(URLFactory.SET_WORK_OUT, false)
        set(value) = sharedPreferences.edit().putBoolean(URLFactory.SET_WORK_OUT, value).apply()
    var setClimate: Boolean
        get() = sharedPreferences.getBoolean(URLFactory.SET_CLIMATE, false)
        set(value) = sharedPreferences.edit().putBoolean(URLFactory.SET_CLIMATE, value).apply()
    var setWeight: Boolean
        get() = sharedPreferences.getBoolean(URLFactory.SET_WEIGHT, false)
        set(value) = sharedPreferences.edit().putBoolean(URLFactory.SET_WEIGHT, value).apply()
    var sleepingTime: String
        get() = sharedPreferences.getString(URLFactory.BED_TIME,"10:00 pm").toString()
        set(value) = sharedPreferences.edit().putString(URLFactory.BED_TIME, value).apply()
    var wakeUpTime: String
        get() = sharedPreferences.getString(URLFactory.WAKE_UP_TIME,"08:00 pm").toString()
        set(value) = sharedPreferences.edit().putString(URLFactory.WAKE_UP_TIME, value).apply()
    var personWeight: String
        get() = sharedPreferences.getString(URLFactory.PERSON_WEIGHT, "80.0").toString()
        set(value) = sharedPreferences.edit().putString(URLFactory.PERSON_WEIGHT, value).apply()
    var weightUnit: Boolean
        get() = sharedPreferences.getBoolean(URLFactory.PERSON_WEIGHT_UNIT,true)
        set(value) = sharedPreferences.edit().putBoolean(URLFactory.PERSON_WEIGHT_UNIT, value).apply()
    var climate: Int
        get() = sharedPreferences.getInt(URLFactory.WEATHER_CONSITIONS, 0)
        set(value) = sharedPreferences.edit().putInt(URLFactory.WEATHER_CONSITIONS, value).apply()
    var dailyWater: Float
        get() = sharedPreferences.getFloat(URLFactory.DAILY_WATER, 0f)
        set(value) = sharedPreferences.edit().putFloat(URLFactory.DAILY_WATER, value).apply()
    var waterUnit: String
        get() = sharedPreferences.getString(URLFactory.WATER_UNIT, "ml").toString()
        set(value) = sharedPreferences.edit().putString(URLFactory.WATER_UNIT, value).apply()
    var hideWelcomeScreen: Boolean
        get() = sharedPreferences.getBoolean(URLFactory.HIDE_WELCOME_SCREEN,true)
        set(value) = sharedPreferences.edit().putBoolean(URLFactory.HIDE_WELCOME_SCREEN, value).apply()
    var userGender: Boolean
        get() = sharedPreferences.getBoolean(URLFactory.USER_GENDER,true)
        set(value) = sharedPreferences.edit().putBoolean(URLFactory.USER_GENDER, value).apply()
    var isActive: Boolean
        get() = sharedPreferences.getBoolean(URLFactory.IS_ACTIVE,true)
        set(value) = sharedPreferences.edit().putBoolean(URLFactory.IS_ACTIVE, value).apply()
    var disableNotification: Boolean
        get() = sharedPreferences.getBoolean(URLFactory.DISABLE_NOTIFICATION,true)
        set(value) = sharedPreferences.edit().putBoolean(URLFactory.DISABLE_NOTIFICATION, value).apply()
    var notificationFreq: Float
        get() = sharedPreferences.getFloat(URLFactory.INTERVAL, 30f)
        set(value) = sharedPreferences.edit().putFloat(URLFactory.INTERVAL, value).apply()
    var wakeUpTimeHour: Int
        get() = sharedPreferences.getInt(URLFactory.WAKE_UP_TIME_HOUR, 8)
        set(value) = sharedPreferences.edit().putInt(URLFactory.WAKE_UP_TIME_HOUR, value).apply()
    var wakeUpTimeMinute: Int
        get() = sharedPreferences.getInt(URLFactory.WAKE_UP_TIME_MINUTE, 0)
        set(value) = sharedPreferences.edit().putInt(URLFactory.WAKE_UP_TIME_MINUTE, value).apply()
    var sleepTimeHour: Int
        get() = sharedPreferences.getInt(URLFactory.BED_TIME_HOUR, 10)
        set(value) = sharedPreferences.edit().putInt(URLFactory.BED_TIME_HOUR, value).apply()
    var sleepTimeMinute: Int
        get() = sharedPreferences.getInt(URLFactory.BED_TIME_MINUTE, 30)
        set(value) = sharedPreferences.edit().putInt(URLFactory.BED_TIME_MINUTE, value).apply()
    var migration: Boolean
        get() = sharedPreferences.getBoolean(URLFactory.MIGRATION,false)
        set(value) = sharedPreferences.edit().putBoolean(URLFactory.MIGRATION, value).apply()
    var userName: String
        get() = sharedPreferences.getString(URLFactory.USER_NAME, "").toString()
        set(value) = sharedPreferences.edit().putString(URLFactory.USER_NAME, value).apply()
    var personHeight: String
        get() = sharedPreferences.getString(URLFactory.PERSON_HEIGHT, "").toString()
        set(value) = sharedPreferences.edit().putString(URLFactory.PERSON_HEIGHT, value).apply()
    var ignoreNextStep: Boolean
        get() = sharedPreferences.getBoolean(URLFactory.IGNORE_NEXT_STEP,false)
        set(value) = sharedPreferences.edit().putBoolean(URLFactory.IGNORE_NEXT_STEP, value).apply()
    var isManualReminder: Boolean
        get() = sharedPreferences.getBoolean(URLFactory.IS_MANUAL_REMINDER,false)
        set(value) = sharedPreferences.edit().putBoolean(URLFactory.IS_MANUAL_REMINDER, value).apply()
    var autoBackUp: Boolean
        get() = sharedPreferences.getBoolean(URLFactory.AUTO_BACK_UP,false)
        set(value) = sharedPreferences.edit().putBoolean(URLFactory.AUTO_BACK_UP, value).apply()
    var heightUnit: Boolean
        get() = sharedPreferences.getBoolean(URLFactory.PERSON_HEIGHT_UNIT,false)
        set(value) = sharedPreferences.edit().putBoolean(URLFactory.PERSON_HEIGHT_UNIT, value).apply()
    var reminderOpt: Int
        get() = sharedPreferences.getInt(URLFactory.REMINDER_OPTION, 1)
        set(value) = sharedPreferences.edit().putInt(URLFactory.REMINDER_OPTION, value).apply()
    var reminderSound: Int
        get() = sharedPreferences.getInt(URLFactory.REMINDER_SOUND, 1)
        set(value) = sharedPreferences.edit().putInt(URLFactory.REMINDER_SOUND, value).apply()
    var reminderVibrate: Boolean
        get() = sharedPreferences.getBoolean(URLFactory.REMINDER_VIBRATE,false)
        set(value) = sharedPreferences.edit().putBoolean(URLFactory.REMINDER_VIBRATE, value).apply()
    var disableSoundWhenAddWater: Boolean
        get() = sharedPreferences.getBoolean(URLFactory.DISABLE_SOUND_WHEN_ADD_WATER,false)
        set(value) = sharedPreferences.edit().putBoolean(URLFactory.DISABLE_SOUND_WHEN_ADD_WATER, value).apply()
    var autoBackUpType: Int
        get() = sharedPreferences.getInt(URLFactory.AUTO_BACK_UP_TYPE, 1)
        set(value) = sharedPreferences.edit().putInt(URLFactory.AUTO_BACK_UP_TYPE, value).apply()
    var autoBackUpId: Int
        get() = sharedPreferences.getInt(URLFactory.AUTO_BACK_UP_ID, 1)
        set(value) = sharedPreferences.edit().putInt(URLFactory.AUTO_BACK_UP_ID, value).apply()
}