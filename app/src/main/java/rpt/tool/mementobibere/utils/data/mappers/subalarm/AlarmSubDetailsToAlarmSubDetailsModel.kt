package rpt.tool.mementobibere.utils.data.mappers.subalarm

import it.pokersrl.poker.utils.data.mappers.ModelMapper
import rpt.tool.mementobibere.utils.data.appmodel.AlarmSubDetails
import rpt.tool.mementobibere.utils.data.database.models.AlarmSubDetailsModel


class AlarmSubDetailsToAlarmSubDetailsModel : ModelMapper<AlarmSubDetails, AlarmSubDetailsModel> {
    override val destination: Class<AlarmSubDetailsModel> = AlarmSubDetailsModel::class.java

    override fun map(source: AlarmSubDetails): AlarmSubDetailsModel {
        return AlarmSubDetailsModel(
            id = source.id!!.toInt(),
            AlarmTime = source.alarmTime!!,
            AlarmId = source.alarmId!!,
            SuperId = source.superId!!
        )
    }
}