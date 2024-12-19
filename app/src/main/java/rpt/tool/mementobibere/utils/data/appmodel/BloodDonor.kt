package rpt.tool.mementobibere.utils.data.appmodel

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import rpt.tool.mementobibere.utils.data.AppModel
import rpt.tool.mementobibere.utils.data.DbModel
import rpt.tool.mementobibere.utils.data.database.models.AlarmDetailsModel
import rpt.tool.mementobibere.utils.data.database.models.AlarmSubDetailsModel
import rpt.tool.mementobibere.utils.data.database.models.BloodDonorModel
import rpt.tool.mementobibere.utils.data.database.models.ContainerDetailsModel
import rpt.tool.mementobibere.utils.data.mappers.addMapper
import rpt.tool.mementobibere.utils.data.mappers.alarm.AlarmDetailsToAlarmDetailsModel
import rpt.tool.mementobibere.utils.data.mappers.blooddonor.BloodDonorToBloodDonorModel
import rpt.tool.mementobibere.utils.data.mappers.container.ContainerDetailsToContainerDetailsModel
import rpt.tool.mementobibere.utils.data.mappers.subalarm.AlarmSubDetailsToAlarmSubDetailsModel
import java.io.Serializable

@Keep
class BloodDonor (

    var id: String? = null,
    var date: String? = null
)
    : AppModel(), Serializable {

    init {
        addMapper(BloodDonorToBloodDonorModel())
    }


    override fun <T : DbModel> toDBModel(): T {
        return mappers.single { it.destination == BloodDonorModel::class.java }.map(this) as T
    }
}