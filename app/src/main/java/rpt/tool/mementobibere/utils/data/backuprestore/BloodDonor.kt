package rpt.tool.mementobibere.utils.data.backuprestore

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BloodDonor {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("Date")
    @Expose
    var date: String? = null

}