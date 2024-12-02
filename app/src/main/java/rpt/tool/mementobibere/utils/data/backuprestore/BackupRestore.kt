package rpt.tool.mementobibere.utils.data.backuprestore

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BackupRestore {
    @SerializedName("AlarmDetails")
    @Expose
    var alarmDetails: List<AlarmDetails> = ArrayList()

    @SerializedName("ContainerDetails")
    @Expose
    var containerDetails: List<ContainerDetails> = ArrayList()

    @SerializedName("DrinkDetails")
    @Expose
    var drinkDetails: List<DrinkDetails> = ArrayList()

    @SerializedName("BloodDonorList")
    @Expose
    var bloodDonorList: List<BloodDonor> = ArrayList()

    @SerializedName("ReachedGoalList")
    @Expose
    var reachedGoalList: List<ReachedGoal> = ArrayList()
    //=================================
    @SerializedName("total_weight")
    @Expose
    var totalWeight: String? = null

    @SerializedName("total_drink")
    @Expose
    var totalDrink: Float = 0f

    @SerializedName("isKgUnit")
    @Expose
    var isKgUnit: Boolean = true
        private set

    @SerializedName("isMlUnit")
    @Expose
    var isMlUnit: Boolean = true
        private set

    fun isKgUnit(isKgUnit: Boolean) {
        this.isKgUnit = isKgUnit
    }

    fun isMlUnit(isMlUnit: Boolean) {
        this.isMlUnit = isMlUnit
    }

    //=========
    @SerializedName("total_height")
    @Expose
    var totalHeight: String? = null

    @SerializedName("user_name")
    @Expose
    var userName: String? = null

    @SerializedName("user_gender")
    @Expose
    var userGender: Boolean = true

    @SerializedName("blood_donor")
    @Expose
    var bloodDonor: Boolean = true

    @SerializedName("isCMUnit")
    @Expose
    var isCMUnit: Boolean = true
        private set


    fun isCMUnit(isCMUnit: Boolean) {
        this.isCMUnit = isCMUnit
    }

    //===========================
    @SerializedName("reminder_option")
    @Expose
    var reminderOption: Int? = null

    @SerializedName("reminder_vibrate")
    @Expose
    var isReminderVibrate: Boolean = true
        private set

    @SerializedName("reminder_sound")
    @Expose
    var reminderSound: Int? = null

    @SerializedName("disable_notification")
    @Expose
    var isDisableNotifiction: Boolean = false
        private set

    @SerializedName("manual_reminder_active")
    @Expose
    var isManualReminderActive: Boolean = true
        private set


    fun isReminderVibrate(reminder_vibrate: Boolean) {
        this.isReminderVibrate = reminder_vibrate
    }

    fun isDisableNotifiction(disable_notification: Boolean) {
        this.isDisableNotifiction = disable_notification
    }

    fun isManualReminderActive(manual_reminder_active: Boolean) {
        this.isManualReminderActive = manual_reminder_active
    }


    //================
    @SerializedName("disable_sound")
    @Expose
    var isDisableSound: Boolean = false
        private set

    fun isDisableSound(disable_sound: Boolean) {
        this.isDisableSound = disable_sound
    }


    //================================
    @SerializedName("auto_backup")
    @Expose
    var isAutoBackup: Boolean = false
        private set

    fun isAutoBackup(auto_backup: Boolean) {
        this.isAutoBackup = auto_backup
    }

    @SerializedName("auto_backup_type")
    @Expose
    var autoBackupType: Int = 0


    @SerializedName("auto_backup_id")
    @Expose
    var autoBackupId: Int = 0
        private set

    fun setAutoBackupID(auto_backup_id: Int) {
        this.autoBackupId = auto_backup_id
    }


    //================================
    @SerializedName("is_active")
    @Expose
    var isActive: Boolean = false
        private set

    fun isActive(is_active: Boolean) {
        this.isActive = is_active
    }


    @SerializedName("is_pregnant")
    @Expose
    var isPregnant: Boolean = false
        private set

    fun isPregnant(is_pregnant: Boolean) {
        this.isPregnant = is_pregnant
    }


    @SerializedName("is_breastfeeding")
    @Expose
    var isBreastfeeding: Boolean = false
        private set

    fun isBreastfeeding(is_breastfeeding: Boolean) {
        this.isBreastfeeding = is_breastfeeding
    }


    @SerializedName("weather_conditions")
    @Expose
    var weatherConditions: Int = 0
}