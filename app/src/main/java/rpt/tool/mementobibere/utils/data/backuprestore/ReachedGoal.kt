package rpt.tool.mementobibere.utils.data.backuprestore

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReachedGoal {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("Date")
    @Expose
    var date: String? = null

    @SerializedName("ContainerValue")
    @Expose
    var containerValue: String? = null

    @SerializedName("ContainerValueOZ")
    @Expose
    var containerValueOZ: String? = null
}