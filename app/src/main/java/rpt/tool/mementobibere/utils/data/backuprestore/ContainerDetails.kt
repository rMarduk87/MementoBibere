package rpt.tool.mementobibere.utils.data.backuprestore

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ContainerDetails {
    @SerializedName("ContainerID")
    @Expose
    var containerID: String? = null

    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("ContainerMeasure")
    @Expose
    var containerMeasure: String? = null

    @SerializedName("ContainerValue")
    @Expose
    var containerValue: String? = null

    @SerializedName("ContainerValueOZ")
    @Expose
    var containerValueOZ: String? = null

    @SerializedName("IsOpen")
    @Expose
    var isOpen: String? = null

    @SerializedName("IsCustom")
    @Expose
    var isCustom: String? = null
}