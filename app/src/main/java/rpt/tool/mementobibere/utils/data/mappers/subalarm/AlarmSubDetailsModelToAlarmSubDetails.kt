package rpt.tool.mementobibere.utils.data.mappers.subalarm

import it.pokersrl.poker.utils.data.mappers.ModelMapper
import rpt.tool.mementobibere.utils.data.appmodel.AlarmDetails
import rpt.tool.mementobibere.utils.data.appmodel.AlarmSubDetails
import rpt.tool.mementobibere.utils.data.database.models.AlarmDetailsModel
import rpt.tool.mementobibere.utils.data.database.models.AlarmSubDetailsModel

class AlarmSubDetailsModelToAlarmSubDetails  : ModelMapper<AlarmSubDetailsModel, AlarmSubDetails> {
    override val destination: Class<AlarmSubDetails> = AlarmSubDetails::class.java

    override fun map(source: AlarmSubDetailsModel): AlarmSubDetails {
        return AlarmSubDetails(
            id = source.id.toString(),
            alarmTime = source.AlarmTime,
            alarmId = source.AlarmId,
            superId = source.SuperId,

        )
    }
}