package rpt.tool.mementobibere.utils.data.mappers.alarm

import it.pokersrl.poker.utils.data.mappers.ModelMapper
import rpt.tool.mementobibere.utils.data.appmodel.AlarmDetails
import rpt.tool.mementobibere.utils.data.database.models.AlarmDetailsModel


class AlarmDetailsToAlarmDetailsModel : ModelMapper<AlarmDetails, AlarmDetailsModel> {
    override val destination: Class<AlarmDetailsModel> = AlarmDetailsModel::class.java

    override fun map(source: AlarmDetails): AlarmDetailsModel {
        return AlarmDetailsModel(
            id = source.id!!.toInt(),
            Sunday = source.sunday,
            Monday = source.monday,
            Tuesday = source.tuesday,
            Wednesday = source.wednesday,
            Thursday = source.thursday,
            Friday = source.friday,
            Saturday = source.saturday,
            SundayAlarmId = source.alarmSundayId!!,
            MondayAlarmId = source.alarmMondayId!!,
            TuesdayAlarmId = source.alarmTuesdayId!!,
            WednesdayAlarmId = source.alarmWednesdayId!!,
            ThursdayAlarmId = source.alarmThursdayId!!,
            FridayAlarmId = source.alarmFridayId!!,
            SaturdayAlarmId = source.alarmSaturdayId!!,
            AlarmTime = source.alarmTime!!,
            AlarmId = source.alarmId!!,
            AlarmType = source.alarmType!!,
            AlarmInterval = source.alarmInterval!!,
        )
    }
}