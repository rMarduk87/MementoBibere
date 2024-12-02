package rpt.tool.mementobibere.utils.data.backuprestore

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AlarmSubDetails {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("AlarmTime")
    @Expose
    var alarmTime: String? = null

    @SerializedName("AlarmId")
    @Expose
    var alarmId: String? = null

    @SerializedName("SuperId")
    @Expose
    var superId: String? = null
}