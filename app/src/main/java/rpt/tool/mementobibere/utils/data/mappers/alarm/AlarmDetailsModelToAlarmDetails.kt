package rpt.tool.mementobibere.utils.data.mappers.alarm

import it.pokersrl.poker.utils.data.mappers.ModelMapper
import rpt.tool.mementobibere.utils.data.appmodel.AlarmDetails
import rpt.tool.mementobibere.utils.data.database.models.AlarmDetailsModel

class AlarmDetailsModelToAlarmDetails  : ModelMapper<AlarmDetailsModel, AlarmDetails> {
    override val destination: Class<AlarmDetails> = AlarmDetails::class.java

    override fun map(source: AlarmDetailsModel): AlarmDetails {
        return AlarmDetails(
            id = source.id.toString(),
            sunday = source.Sunday,
            monday = source.Monday,
            tuesday = source.Tuesday,
            wednesday = source.Wednesday,
            thursday = source.Thursday,
            friday = source.Friday,
            saturday = source.Saturday,
            alarmSundayId = source.SundayAlarmId,
            alarmMondayId = source.MondayAlarmId,
            alarmTuesdayId = source.TuesdayAlarmId,
            alarmWednesdayId = source.WednesdayAlarmId,
            alarmThursdayId = source.ThursdayAlarmId,
            alarmFridayId = source.FridayAlarmId,
            alarmSaturdayId = source.SaturdayAlarmId,
            alarmTime = source.AlarmTime,
            alarmId = source.AlarmId,
            alarmType = source.AlarmType,
            alarmInterval = source.AlarmInterval
        )
    }
}