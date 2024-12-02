package rpt.tool.mementobibere.utils

import android.media.Ringtone
import java.text.DecimalFormat

object URLFactory {
    var PRIVACY_POLICY_URL: String = "https://www.termsfeed.com/live/d1615b20-2bc9-4048-8b73-b674c2aeb1c5"

    var APP_SHARE_URL: String = "https://share.html"

    var DATE_FORMAT: String = "dd-MM-yyyy"

    var DAILY_WATER_VALUE: Float = 0f
    var WATER_UNIT_VALUE: String = "ml"

    // Preferences
    var DAILY_WATER: String = "daily_water"
    var WATER_UNIT: String = "water_unit"

    var SELECTED_CONTAINER: String = "selected_container"

    var HIDE_WELCOME_SCREEN: String = "hide_welcome_screen"


    //========
    var USER_NAME: String = "user_name"
    var USER_GENDER: String = "user_gender"
    var BLOOD_DONOR: String = "blood_donor"
    var USER_PHOTO: String = "user_photo"

    var SET_USER_GENDER: String = "set_user_gender"
    var SET_BLOOD_DONOR: String = "set_blood_donor"
    var SET_USER_NAME: String = "set_user_name"
    var SET_WORK_OUT: String = "set_work_out"
    var SET_CLIMATE: String = "set_climate"
    var SET_WEIGHT: String = "set_weight"
    var SET_HEIGHT: String = "set_height"

    var PERSON_HEIGHT: String = "person_height"
    var PERSON_HEIGHT_UNIT: String = "person_height_unit"

    var PERSON_WEIGHT: String = "person_weight"
    var PERSON_WEIGHT_UNIT: String = "person_weight_unit"


    var SET_MANUALLY_GOAL: String = "set_manually_goal"
    var SET_MANUALLY_GOAL_VALUE: String = "set_manually_goal_value"


    var WAKE_UP_TIME: String = "wakeup_time"
    var WAKE_UP_TIME_HOUR: String = "wakeup_time_hour"
    var WAKE_UP_TIME_MINUTE: String = "wakeup_time_minute"

    var BED_TIME: String = "bed_time"
    var BED_TIME_HOUR: String = "bed_time_hour"
    var BED_TIME_MINUTE: String = "bed_time_minute"

    var INTERVAL: String = "interval"

    var REMINDER_OPTION: String = "reminder_option" // o for auto, 1 for off, 2 for silent
    var REMINDER_VIBRATE: String = "reminder_vibrate"
    var REMINDER_SOUND: String = "reminder_sound"

    var DISABLE_NOTIFICATION: String = "disable_notification"
    var IS_MANUAL_REMINDER: String = "manual_reminder_active"

    var DISABLE_SOUND_WHEN_ADD_WATER: String = "disable_sound_when_add_water"

    var IGNORE_NEXT_STEP: String = "ignore_next_step"

    var MIGRATION: String =  "migration"


    // precision
    var decimalFormat: DecimalFormat = DecimalFormat("#0.00")
    var decimalFormat2: DecimalFormat = DecimalFormat("#0.0")


    var notification_ringtone: Ringtone? = null


    var RELOAD_DASHBOARD: Boolean = true

    var LOAD_VIDEO_ADS: Boolean = false

    var APP_DIRECTORY_NAME: String = "Let\'s hydrate"
    var APP_PROFILE_DIRECTORY_NAME: String = "profile"

    var AUTO_BACK_UP: String = "auto_backup"
    var AUTO_BACK_UP_TYPE: String = "auto_backup_type"
    var AUTO_BACK_UP_ID: String = "auto_backup_id"

    var IS_ACTIVE: String = "is_active"
    var IS_PREGNANT: String = "is_pregnant"
    var IS_BREATFEEDING: String = "is_breastfeeding"

    var WEATHER_CONSITIONS: String = "weather_conditions"

    var MALE_WATER: Double = 35.71
    var ACTIVE_MALE_WATER: Double = 50.0
    var DEACTIVE_MALE_WATER: Double = 14.29

    var FEMALE_WATER: Double = 28.57
    var ACTIVE_FEMALE_WATER: Double = 40.0
    var DEACTIVE_FEMALE_WATER: Double = 11.43

    var PREGNANT_WATER: Double = 700.0
    var BREASTFEEDING_WATER: Double = 700.0

    var WEATHER_SUNNY: Double = 1.0
    var WEATHER_CLOUDY: Double = 0.85
    var WEATHER_RAINY: Double = 0.68
    var WEATHER_SNOW: Double = 0.88
}