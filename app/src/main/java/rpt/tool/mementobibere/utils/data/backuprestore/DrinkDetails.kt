package rpt.tool.mementobibere.utils.data.backuprestore

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DrinkDetails {
    @SerializedName("DrinkDateTime")
    @Expose
    var drinkDateTime: String? = null

    @SerializedName("DrinkTime")
    @Expose
    var drinkTime: String? = null

    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("DrinkDate")
    @Expose
    var drinkDate: String? = null

    @SerializedName("ContainerMeasure")
    @Expose
    var containerMeasure: String? = null

    @SerializedName("ContainerValue")
    @Expose
    var containerValue: String? = null

    @SerializedName("ContainerValueOZ")
    @Expose
    var containerValueOZ: String? = null

    //===============
    @SerializedName("TodayGoal")
    @Expose
    var todayGoal: String? = null

    @SerializedName("TodayGoalOZ")
    @Expose
    var todayGoalOZ: String? = null
}