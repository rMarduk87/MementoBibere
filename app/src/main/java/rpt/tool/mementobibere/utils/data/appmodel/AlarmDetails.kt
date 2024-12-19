package rpt.tool.mementobibere.utils.data.appmodel

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import rpt.tool.mementobibere.utils.data.AppModel
import rpt.tool.mementobibere.utils.data.DbModel
import rpt.tool.mementobibere.utils.data.database.models.AlarmDetailsModel
import rpt.tool.mementobibere.utils.data.database.models.ContainerDetailsModel
import rpt.tool.mementobibere.utils.data.mappers.addMapper
import rpt.tool.mementobibere.utils.data.mappers.alarm.AlarmDetailsToAlarmDetailsModel
import rpt.tool.mementobibere.utils.data.mappers.container.ContainerDetailsToContainerDetailsModel
import java.io.Serializable

@Keep
class AlarmDetails (

    var id: String? = null,
    var alarmTime: String? = null,
    var alarmId: String? = null,
    var alarmType: String? = null,
    var alarmInterval: String? = null,
    var alarmSundayId: String? = null,
    var alarmMondayId: String? = null,
    var alarmTuesdayId: String? = null,
    var alarmWednesdayId: String? = null,
    var alarmThursdayId: String? = null,
    var alarmFridayId: String? = null,
    var alarmSaturdayId: String? = null,
    var isOff: Int = 0,
    var sunday: Int = 0,
    var monday: Int = 0,
    var tuesday: Int = 0,
    var wednesday: Int = 0,
    var thursday: Int = 0,
    var friday: Int = 0,
    var saturday: Int = 0
)
    : AppModel(), Serializable {

    init {
        addMapper(AlarmDetailsToAlarmDetailsModel())
    }


    override fun <T : DbModel> toDBModel(): T {
        return mappers.single { it.destination == AlarmDetailsModel::class.java }.map(this) as T
    }
}